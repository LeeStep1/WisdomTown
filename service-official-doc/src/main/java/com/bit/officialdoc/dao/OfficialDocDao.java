package com.bit.officialdoc.dao;

import com.bit.officialdoc.entity.OfficialDoc;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;
import com.bit.officialdoc.vo.SimpleDoc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
@Repository
public interface OfficialDocDao {

    /**
     * 逻辑删除
     *
     * @param ids
     */
    void logicDelete(long... ids);

    /**
     * @param doc
     */
    void create(OfficialDoc doc);

    /**
     * @param doc
     */
    void modify(OfficialDoc doc);

    /**
     * @return
     */
    List<SimpleDoc> query(DocQuery query);

    /**
     * @param docId
     * @return
     */
    FullDoc findById(Long docId);

    /**
     * @param docIds
     * @return
     */
    List<FullDoc> findByIds(long... docIds);

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
     * 更改发送状态
     *
     * @param id
     */
    int sent(Long id);

    /**
     * 归档
     *
     * @param folderId
     * @param ids
     */
    void archive(Long folderId, long... ids);

    /**
     * 删除恢复
     *
     * @param ids
     */
    void restore(long... ids);

    /**
     * 统计未读数量
     *
     * @param senderId
     * @param sent
     * @return
     */
    int countUnRead(@Param(value = "senderId") Long senderId, @Param(value = "sent") boolean sent);
}
