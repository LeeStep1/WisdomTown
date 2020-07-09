package com.bit.officialdoc.service;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.entity.CustomFolder;
import com.bit.officialdoc.entity.OfficialDocArchive;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FolderQuery;

import java.util.List;

/**
 * @author terry.jiang[taoj555@163.com] on 2019-01-16.
 */
public interface ArchiveService {

    /**
     * 个人文件夹分页
     *
     * @param query
     * @return
     */
    BaseVo queryFolder(FolderQuery query);

    /**
     * 获取个人文件夹列表
     *
     * @return
     */
    List<CustomFolder> getFolders();

    /**
     * 获取个人文件夹列表和存档数量
     *
     * @return
     */
    BaseVo findCustorAndNumById(FolderQuery query);

    /**
     * 获取各个箱的数量
     *
     * @return
     */
    int[] findCountByOwnerId();

    /**
     * 创建个人文件夹
     *
     * @param folder
     */
    void createFolder(CustomFolder folder);

    /**
     * 更改个人文件夹
     *
     * @param name
     */
    void modifyFolder(long id, String name);

    /**
     * 删除文件夹
     *
     * @param id
     */
    void deleteFolder(long id);

    /**
     * 清空文件夹
     *
     * @param id
     */
    void cleanFolder(long id);


    /**
     * 查询归档公文
     *
     * @param query
     * @return
     */
    BaseVo queryArchive(DocQuery query);

    /**
     * 逻辑删除归档公文
     *
     * @param archiveId
     */
    void logicDelete(long... archiveId);

    /**
     * 移动归档
     *
     * @param targetFolderId 目标文件夹id
     * @param archiveIds
     */
    void move(long targetFolderId, long[] archiveIds);

    /**
     * 标记已读
     *
     * @param archiveIds
     */
    void markRead(long... archiveIds);

    /**
     * 标记未读
     *
     * @param archiveIds
     */
    void markUnread(long... archiveIds);

    /**
     * 根据ID获取公文ID和读取状态
     *
     * @param id
     * @return
     */
    OfficialDocArchive findDocIdAndReadById(Long id);
}
