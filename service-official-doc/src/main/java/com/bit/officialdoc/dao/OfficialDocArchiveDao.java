package com.bit.officialdoc.dao;

import com.bit.officialdoc.entity.CustomFolder;
import com.bit.officialdoc.entity.OfficialDocArchive;
import com.bit.officialdoc.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/18 17:37
 */
@Repository
public interface OfficialDocArchiveDao {

    /**
     * 新增个人归档
     *
     * @param officialDocArchive
     */
    void createFolder(CustomFolder officialDocArchive);

    /**
     * 文件夹列表
     *
     * @param query
     * @return
     */
    List<FolderDoc> queryFolder(FolderQuery query);

    /**
     * 根据个人ID获取文件夹列表
     *
     * @param ownerId
     * @return
     */
    List<CustomFolder> findByOwnerId(@Param(value = "ownerId") long ownerId);

    /**
     * 获取个人文件夹和存档数量
     *
     * @param ownerId
     * @return
     */
    List<FolderParam> findCustorAndNumById(@Param(value = "ownerId") long ownerId, @Param(value = "name") String name);

    /**
     * 获取各个箱数量
     *
     * @param ownerId
     * @return
     */
    int[] findCountByOwnerId(@Param(value = "ownerId") long ownerId);

    /**
     * 更新个人归档
     *
     * @param name
     */
    void modifyFolder(@Param(value = "id") long id, @Param(value = "name") String name);

    /**
     * 删除文件夹
     *
     * @param id
     */
    int deleteFolder(@Param(value = "id") long id, @Param(value = "ownerId") long ownerId);

    // ============================ 个人存档 ============================ //

    /**
     * 新增个人存档
     *
     * @param officialDocArchives
     */
    void createArchive(List<OfficialDocArchive> officialDocArchives);

    /**
     * 存档列表
     *
     * @param query
     * @return
     */
    List<SimpleDoc> queryArchive(DocQuery query);

    /**
     * 查询多条存档信息
     *
     * @param ids
     * @return
     */
    List<FullDoc> findArchiveByIds(long... ids);

    /**
     * 标记已读
     *
     * @param ids
     */
    void markRead(long... ids);

    /**
     * 标记未读
     *
     * @param ids
     */
    void markUnRead(long... ids);

    /**
     * 删除个人存档
     *
     * @param ids
     */
    void logicDelete(long... ids);

    /**
     * 批量删除存档
     *
     * @param folderId
     */
    void deleteArchive(@Param(value = "folderId") long folderId, @Param(value = "ownerId") long ownerId);

    /**
     * 转移存档
     *
     * @param targetFolderId
     * @param archiveIds
     */
    void move(@Param(value = "targetFolderId") long targetFolderId, @Param(value = "archiveIds") long[] archiveIds);

    /**
     * 根据ID获取公文ID和读取状态
     *
     * @param id
     * @return
     */
    OfficialDocArchive findDocIdAndReadById(@Param(value = "id") Long id);

    /**
     * 将删除的文件恢复到个人存档
     *
     * @param ids
     */
    void restore(long... ids);

    /**
     * 获取未读数量
     *
     * @param ownerId
     * @return
     */
    int countUnRead(@Param(value = "ownerId") Long ownerId, @Param(value = "folderId") Long folderId);

}
