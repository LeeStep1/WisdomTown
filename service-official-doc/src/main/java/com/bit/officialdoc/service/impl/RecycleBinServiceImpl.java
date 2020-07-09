package com.bit.officialdoc.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.dao.OfficialDocArchiveDao;
import com.bit.officialdoc.dao.OfficialDocDao;
import com.bit.officialdoc.dao.OfficialDocInboxDao;
import com.bit.officialdoc.dao.OfficialDocRecycleBinDao;
import com.bit.officialdoc.entity.OfficialDocArchive;
import com.bit.officialdoc.entity.OfficialDocRecycleBin;
import com.bit.officialdoc.service.RecycleBinService;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;
import com.bit.officialdoc.vo.SimpleDoc;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/18 14:18
 */
@Slf4j
@Service("recycleBinService")
public class RecycleBinServiceImpl extends BaseService implements RecycleBinService {

    @Autowired
    private OfficialDocRecycleBinDao officialDocRecycleBinDao;

    @Autowired
    private OfficialDocDao officialDocDao;

    @Autowired
    private OfficialDocInboxDao officialDocInboxDao;

    @Autowired
    private OfficialDocArchiveDao officialDocArchiveDao;

    @Override
    public BaseVo<PageInfo<SimpleDoc>> query(DocQuery query) {
        Long userId = getUserInfo().getId();
        query.setOwner(userId);
        query.setOrderBy("doc_create_at DESC");
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<SimpleDoc> simpleDocPageInfo = officialDocRecycleBinDao.query(query);
        PageInfo<SimpleDoc> pageInfo = new PageInfo<>(simpleDocPageInfo);
        // 获取未数量
        int unReadNum = officialDocRecycleBinDao.countUnRead(userId);
        pageInfo.setEndRow(unReadNum);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    @Transactional
    public void restore(long... recycleIds) {
        // step.1 将对应的公文/收件箱/归档记录还原成未删除状态
        List<OfficialDocRecycleBin> officialDocRecycleBins = officialDocRecycleBinDao.findByIds(recycleIds);
        List<Long> outboxIds = new ArrayList<>();
        List<Long> inboxIds = new ArrayList<>();
        List<Long> folderIds = new ArrayList<>();
        officialDocRecycleBins.parallelStream().forEach(officialDocRecycleBin -> {
            switch (SourceTypeEnum.getByValue(officialDocRecycleBin.getSource())) {
                case OUTBOX:
                    outboxIds.add(officialDocRecycleBin.getReferenceId());
                    break;
                case INBOX:
                    inboxIds.add(officialDocRecycleBin.getReferenceId());
                    break;
                case FOLDER:
                    folderIds.add(officialDocRecycleBin.getReferenceId());
                    break;
                default:
                    break;
            }
        });
        if (outboxIds.size() > 0) {
            long[] ids = outboxIds.stream().mapToLong(t->t.longValue()).toArray();
            officialDocDao.restore(ids);
        }
        if (inboxIds.size() > 0) {
            long[] ids = inboxIds.stream().mapToLong(t->t.longValue()).toArray();
            officialDocInboxDao.restore(ids);
        }
        if (folderIds.size() > 0) {
            long[] ids = folderIds.stream().mapToLong(t->t.longValue()).toArray();
            officialDocArchiveDao.restore(ids);
        }
        // step.2 物理删除回收桶记录
        officialDocRecycleBinDao.undoDelete(recycleIds);
    }

    @Override
    public void undoDelete(long... recycleIds) {
        officialDocRecycleBinDao.undoDelete(recycleIds);
    }

    @Override
    public void markRead(long... recycleIds) {
        officialDocRecycleBinDao.markRead(recycleIds);
    }

    @Override
    public void markUnread(long... recycleIds) {
        officialDocRecycleBinDao.markUnRead(recycleIds);
    }

    @Override
    public OfficialDocRecycleBin findDocIdAndReadById(long id) {
        return officialDocRecycleBinDao.findDocIdAndReadById(id);
    }

    @Override
    public void archive(long folderId, long... idsArray) {
        List<FullDoc> fullDocs = officialDocRecycleBinDao.findByIds2(idsArray);
        Long userId = getUserInfo().getId();
        List<OfficialDocArchive> officialDocArchives = ArchiveServiceImpl.createArchive(fullDocs, folderId, userId);
        officialDocArchiveDao.createArchive(officialDocArchives);
    }

    private UserInfo getUserInfo(){
        return getCurrentUserInfo();
    }

    @AllArgsConstructor
    enum SourceTypeEnum {
        /**
         * 默认配置
         * 来源 0-发件箱 1-收件箱 2-个人归档
         */
        OUTBOX(0, "发件箱"),

        INBOX(1, "收件箱"),

        FOLDER(2, "个人归档");

        private int value;

        private String phrase;

        public static SourceTypeEnum getByValue(int value) {
            for (SourceTypeEnum sourceTypeEnum : values()) {
                if (sourceTypeEnum.value == value) {
                    return sourceTypeEnum;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }

        public String getPhrase() {
            return phrase;
        }
    }
}
