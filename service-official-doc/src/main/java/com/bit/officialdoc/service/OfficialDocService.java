package com.bit.officialdoc.service;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;

/**
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
public interface OfficialDocService {

    /**
     * @param query
     * @return
     */
    BaseVo query(DocQuery query);

    /**
     * 新建公文
     *
     * @param doc
     */
    void createDoc(FullDoc doc, Boolean send);

    /**
     * 新建并且发送公文
     * 标记为已发送，并且新增对应的收件箱记录
     *
     * @param doc
     */
    void createAndSend(FullDoc doc);

    /**
     * 修改公文
     *
     * @param sent
     * @param doc
     */
    void modifyDoc(FullDoc doc, Boolean sent);

    /**
     * 标记为已发送，并且新增对应的收件箱记录
     *
     * @param id
     */
    void sendDoc(long id);

    /**
     * 获取完整公文
     *
     * @param docId
     * @return
     */
    FullDoc getDoc(long docId);

    /**
     * 逻辑删除
     * 公文记录逻辑删除，同时新增个人回收桶记录
     *
     * @param docIds
     */
    void logicDelete(long... docIds);

    /**
     * 公文归档
     * 新增个人归档记录
     *
     * @param folderId 文件夹id
     * @param docIds   公文ids
     */
    void archive(long folderId, long... docIds);

    /**
     * 转发公文
     *
     * 新增公文，标记为待发状态
     * @param sourceDocId 源公文id
     * @param doc
     */
    // void transfer(long sourceDocId, FullDoc doc);

    /**
     * 转发公文, 并立刻发送
     * step.1 新增公文，标记为已发状态 step.2 新增收件人/抄送人的收件记录
     * @param sourceDocId 源公文id
     * @param doc
     */
    // void transferAndSend(long sourceDocId, FullDoc doc);

    /**
     * 标记已读
     *
     * @param docIds
     */
    void markRead(long... docIds);

    /**
     * 标记未读
     *
     * @param docIds
     */
    void markUnread(long... docIds);
}
