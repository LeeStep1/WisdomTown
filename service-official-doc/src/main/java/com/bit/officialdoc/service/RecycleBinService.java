package com.bit.officialdoc.service;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.entity.OfficialDocRecycleBin;
import com.bit.officialdoc.vo.DocQuery;

/**
 * 回收桶service
 *
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
public interface RecycleBinService {

    /**
     * @param query
     * @return
     */
    BaseVo query(DocQuery query);

    /**
     * 还原公文
     * step.1 物理删除回收桶记录 step.2 将对应的公文/收件箱/归档记录还原成未删除状态
     *
     * @param recycleIds
     */
    void restore(long... recycleIds);

    /**
     * 彻底删除回收公文
     *
     * @param recycleIds
     */
    void undoDelete(long... recycleIds);

    /**
     * 标记已读
     *
     * @param recycleIds
     */
    void markRead(long... recycleIds);

    /**
     * 标记未读
     *
     * @param recycleIds
     */
    void markUnread(long... recycleIds);

    /**
     * 获取公文ID和读取状态
     *
     * @param id
     * @return
     */
    OfficialDocRecycleBin findDocIdAndReadById(long id);

    /**
     * 归档
     *
     * @param folderId
     * @param idsArray
     */
    void archive(long folderId, long... idsArray);
}
