package com.bit.officialdoc.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.dao.OfficialDocArchiveDao;
import com.bit.officialdoc.dao.OfficialDocRecycleBinDao;
import com.bit.officialdoc.entity.CustomFolder;
import com.bit.officialdoc.entity.OfficialDocArchive;
import com.bit.officialdoc.entity.OfficialDocRecycleBin;
import com.bit.officialdoc.service.ArchiveService;
import com.bit.officialdoc.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/16 16:57
 */
@Slf4j
@Service("archiveService")
public class ArchiveServiceImpl extends BaseService implements ArchiveService {

    @Autowired
    private OfficialDocArchiveDao officialDocArchiveDao;

    @Autowired
    private OfficialDocRecycleBinDao officialDocRecycleBinDao;

    @Override
    public BaseVo queryFolder(FolderQuery query) {
        UserInfo userInfo = getUserInfo();
        query.setOwner(userInfo.getId());
        query.setOrderBy("create_at DESC");
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<FolderDoc> folderDocs = officialDocArchiveDao.queryFolder(query);
        //
        int unReadNum = officialDocArchiveDao.countUnRead(query.getOwner(), null);
        PageInfo<FolderDoc> pageInfo = new PageInfo<>(folderDocs);
        pageInfo.setEndRow(unReadNum);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public List<CustomFolder> getFolders() {
        UserInfo userInfo = getUserInfo();
        return officialDocArchiveDao.findByOwnerId(userInfo.getId());
    }

    @Override
    public BaseVo findCustorAndNumById(FolderQuery query) {
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        Long userId = getUserInfo().getId();
        List<FolderParam> folderParams = officialDocArchiveDao.findCustorAndNumById(userId, query.getName());
        PageInfo<FolderParam> pageInfo = new PageInfo<>(folderParams);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public int[] findCountByOwnerId() {
        Long userId = getUserInfo().getId();
        int[] nums = officialDocArchiveDao.findCountByOwnerId(userId);
        return nums;
    }

    @Override
    public void createFolder(CustomFolder folder) {
        UserInfo userInfo = getUserInfo();
        folder.setOwnerId(userInfo.getId());
        folder.setCreateAt(new Date());
        officialDocArchiveDao.createFolder(folder);
    }

    @Override
    public void modifyFolder(long id, String name) {
        officialDocArchiveDao.modifyFolder(id, name);
    }

    @Override
    @Transactional
    public void deleteFolder(long id) {
        Long ownerId = getUserInfo().getId();
        // 删除文件夹
        int resultNum = officialDocArchiveDao.deleteFolder(id, ownerId);
        if (resultNum > 0) {
            // 删除文件夹内的存档
            officialDocArchiveDao.deleteArchive(id, ownerId);
        }
    }

    @Override
    public void cleanFolder(long id) {
        // 删除文件夹内的存档
        Long userId = getUserInfo().getId();
        officialDocArchiveDao.deleteArchive(id, userId);
    }

    @Override
    public BaseVo queryArchive(DocQuery query) {
        Long userId = getUserInfo().getId();
        query.setOwner(userId);
        query.setOrderBy("doc_create_at DESC");
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<SimpleDoc> simpleDocPageInfo = officialDocArchiveDao.queryArchive(query);
        int unRead = officialDocArchiveDao.countUnRead(userId, query.getFolderId());
        PageInfo<SimpleDoc> pageInfo = new PageInfo<>(simpleDocPageInfo);
        pageInfo.setEndRow(unRead);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public void logicDelete(long... archiveId) {
        officialDocArchiveDao.logicDelete(archiveId);
        // 新增删除
        List<FullDoc> fullDocs = officialDocArchiveDao.findArchiveByIds(archiveId);
        List<OfficialDocRecycleBin> recycleBins = OfficialDocServiceImpl.batchCreateRecycleBin(fullDocs, OfficialDocRecycleBin.SOURCE_ARCHIVE, getUserInfo().getId());
        officialDocRecycleBinDao.batchCreate(recycleBins);
    }

    @Override
    public void move(long targetFolderId, long[] archiveIds) {
        officialDocArchiveDao.move(targetFolderId, archiveIds);
    }

    @Override
    public void markRead(long... archiveIds) {
        officialDocArchiveDao.markRead(archiveIds);
    }

    @Override
    public void markUnread(long... archiveIds) {
        officialDocArchiveDao.markUnRead(archiveIds);
    }

    @Override
    public OfficialDocArchive findDocIdAndReadById(Long id) {
        return officialDocArchiveDao.findDocIdAndReadById(id);
    }

    /**
     * 批量新增存档
     *
     * @param fullDocs
     * @param folderId
     */
    public static List<OfficialDocArchive> createArchive(List<FullDoc> fullDocs, Long folderId, Long ownerId) {
        List<OfficialDocArchive> officialDocArchives = new ArrayList<>();
        fullDocs.parallelStream().forEach(fullDoc -> {
            OfficialDocArchive officialDocArchive = new OfficialDocArchive();
            officialDocArchive.setOwnerId(ownerId);
            officialDocArchive.setFolderId(folderId);
            officialDocArchive.setDocId(fullDoc.getDocId() == null ? fullDoc.getId() : fullDoc.getDocId());
            officialDocArchive.setDocTitle(fullDoc.getDocTitle());
            officialDocArchive.setDocTextNumber(fullDoc.getDocTextNumber());
            officialDocArchive.setDocSenderId(fullDoc.getDocSenderId());
            officialDocArchive.setDocSenderName(fullDoc.getDocSenderName());
            officialDocArchive.setDocReceivers(fullDoc.getReceivers());
            officialDocArchive.setDocCreateAt(new Date());
            officialDocArchive.setRead(fullDoc.getRead());
            officialDocArchive.setDeleted(false);
            if (fullDoc.getAttactIds() != null) {
                officialDocArchive.setIsAttach(1);
            } else {
                officialDocArchive.setIsAttach(0);
            }
            officialDocArchives.add(officialDocArchive);
        });
        return officialDocArchives;
    }


    private UserInfo getUserInfo() {
        return getCurrentUserInfo();
    }

}
