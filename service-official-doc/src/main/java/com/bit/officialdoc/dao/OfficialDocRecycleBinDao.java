package com.bit.officialdoc.dao;

import com.bit.officialdoc.entity.OfficialDocRecycleBin;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;
import com.bit.officialdoc.vo.SimpleDoc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/17 21:06
 */
@Repository
public interface OfficialDocRecycleBinDao {

    /**
     * 批量新增待办公文
     *
     * @param officialDocRecycleBins
     */
    void batchCreate(List<OfficialDocRecycleBin> officialDocRecycleBins);

    /**
     * 待办列表分页
     *
     * @param query
     * @return
     */
    List<SimpleDoc> query(DocQuery query);

    /**
     * 获取公文id和是否已读状态
     *
     * @param id
     * @return
     */
    OfficialDocRecycleBin findDocIdAndReadById(@Param(value = "id") long id);

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
     * 归档
     *
     * @param folderId
     * @param ids
     */
    void archive(Long folderId, long... ids);

    /**
     * 彻底删除
     *
     * @param ids
     */
    void undoDelete(long... ids);

    /**
     * 查询多个
     *
     * @param ids
     * @return
     */
    List<OfficialDocRecycleBin> findByIds(long... ids);

    /**
     * 查询多个
     *
     * @param ids
     * @return
     */
    List<FullDoc> findByIds2(long... ids);

    /**
     * 统计未读数量
     *
     * @param ownerId
     * @return
     */
    int countUnRead(@Param(value = "ownerId") Long ownerId);
}
