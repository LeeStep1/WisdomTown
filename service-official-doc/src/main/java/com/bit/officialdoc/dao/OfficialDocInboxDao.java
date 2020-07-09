package com.bit.officialdoc.dao;

import com.bit.officialdoc.entity.OfficialDocInbox;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;
import com.bit.officialdoc.vo.SimpleDoc;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/17 14:58
 */
@Repository
public interface OfficialDocInboxDao {

    /**
     * 批量新增待办公文
     *
     * @param officialDocInboxes
     */
    void batchCreate(List<OfficialDocInbox> officialDocInboxes);

    /**
     * 待办列表分页
     *
     * @param query
     * @return
     */
    List<SimpleDoc> query(DocQuery query);

    /**
     * 查询多个
     *
     * @param ids
     * @return
     */
    List<FullDoc> findByIds(long... ids);

    /**
     * 获取公文ID和读取状态
     *
     * @param id
     * @return
     */
    OfficialDocInbox findDocIdAndReadById(@Param(value = "id") long id);

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
    void archive(Long folderId, Long... ids);

    /**
     * 逻辑删除
     *
     * @param ids
     */
    void logicDelete(long... ids);

    /**
     * 办理
     *
     * @param id
     */
    void dispose(long id);

    /**
     * 恢复删除数据
     *
     * @param ids
     */
    void restore(long... ids);

    /**
     * 统计未读数量
     *
     * @param receiverId
     * @param processed
     * @return
     */
    int countUnRead(@Param(value = "receiverId") Long receiverId, @Param(value = "processed") boolean processed);

    List<OfficialDocInbox> findByDocId(Long docId);
}
