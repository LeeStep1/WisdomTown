package com.bit.officialdoc.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.dao.OfficialDocArchiveDao;
import com.bit.officialdoc.dao.OfficialDocInboxDao;
import com.bit.officialdoc.dao.OfficialDocRecycleBinDao;
import com.bit.officialdoc.entity.OfficialDocArchive;
import com.bit.officialdoc.entity.OfficialDocInbox;
import com.bit.officialdoc.entity.OfficialDocRecycleBin;
import com.bit.officialdoc.service.InboxService;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;
import com.bit.officialdoc.vo.SimpleDoc;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/17 20:25
 */
@Slf4j
@Service("inboxService")
public class InboxServiceImpl extends BaseService implements InboxService {

    @Autowired
    private OfficialDocInboxDao officialDocInboxDao;

    @Autowired
    private OfficialDocRecycleBinDao officialDocRecycleBinDao;

    @Autowired
    private OfficialDocArchiveDao officialDocArchiveDao;

    @Override
    public BaseVo query(DocQuery query) {
        long userId = getUserInfo().getId();
        query.setOwner(userId);
        query.setOrderBy("receive_at DESC");
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<SimpleDoc> simpleDocPageInfo = officialDocInboxDao.query(query);
        // 未读数量
        int unReadNum = officialDocInboxDao.countUnRead(query.getOwner(),query.getProcessed());
        PageInfo<SimpleDoc> pageInfo = new PageInfo<>(simpleDocPageInfo);
        pageInfo.setEndRow(unReadNum);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public FullDoc getInboxDoc(long id) {
        return null;
    }

    @Override
    @Transactional
    public void logicDelete(long... inboxIds) {
        // 逻辑删除
        officialDocInboxDao.logicDelete(inboxIds);
        List<FullDoc> fullDocs = officialDocInboxDao.findByIds(inboxIds);
        // 新增删除
        List<OfficialDocRecycleBin> recycleBins = OfficialDocServiceImpl.batchCreateRecycleBin(fullDocs, OfficialDocRecycleBin.SOURCE_INBOX, getUserInfo().getId());
        officialDocRecycleBinDao.batchCreate(recycleBins);
    }

    @Override
    public void archive(long folderId, long... inboxIds) {
        List<FullDoc> fullDocs = officialDocInboxDao.findByIds(inboxIds);
        Long userId = getUserInfo().getId();
        List<OfficialDocArchive> officialDocArchives = ArchiveServiceImpl.createArchive(fullDocs, folderId, userId);
        officialDocArchiveDao.createArchive(officialDocArchives);
    }

    @Override
    public void markRead(long... inboxIds) {
        officialDocInboxDao.markRead(inboxIds);
    }

    @Override
    public void markUnread(long... inboxIds) {
        officialDocInboxDao.markUnRead(inboxIds);
    }

    @Override
    public void dispose(long id) {
        officialDocInboxDao.dispose(id);
    }

    @Override
    public OfficialDocInbox findDocIdAndReadById(long id) {
        return officialDocInboxDao.findDocIdAndReadById(id);
    }

    private UserInfo getUserInfo(){
        return getCurrentUserInfo();
    }
}
