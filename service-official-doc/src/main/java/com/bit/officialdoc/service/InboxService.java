package com.bit.officialdoc.service;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.entity.OfficialDocInbox;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FullDoc;
import com.bit.officialdoc.vo.SimpleDoc;
import com.github.pagehelper.PageInfo;

/**
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
public interface InboxService {

    /**
     * @param query
     * @return
     */
    BaseVo query(DocQuery query);

    /**
     * @param id
     * @return
     */
    FullDoc getInboxDoc(long id);

    /**
     *
     * @param inboxIds
     */
    void logicDelete(long... inboxIds);

    /**
     * 收件箱归档
     * @param folderId 文件夹id
     * @param inboxIds 收件ids
     */
    void archive(long folderId, long... inboxIds);

    /**
     * 回复公文
     * step.1 新建公文(收件人为当前收件的发送人)， 并标记为待发 step.2 标记当前收件为已读
     *
     * @param inboxId 收件id
     * @param doc     回复doc内容
     */
    // void replay(long inboxId, FullDoc doc);

    /**
     * 回复公文并立刻发送
     * step.1 新建公文(收件人为当前收件的发送人)， 并标记为已 step.2 新增对应的收件箱记录(收件人，抄送人) step.3 标记当前收件为已回复，已读
     *
     * @param inboxId 收件id
     * @param doc     回复doc内容
     */
    // void replayAndSend(long inboxId, FullDoc doc);

    /**
     * 转发公文
     * step.1 新建公文， 标记为待发 step.2 标记当前收件为已读
     *
     * @param inboxId 收件id
     * @param doc     转发doc内容
     */
    // void transfer(long inboxId, FullDoc doc);

    /**
     * 转发公文并立刻发送
     * <p>
     * step.1 新建公文， 标记为已发 step.2 新增对应的收件箱记录(收件人，抄送人) step.3 标记当前收件为已读
     *
     * @param inboxId 收件id
     * @param doc     转发doc内容
     */
    // void transferAndSend(long inboxId, FullDoc doc);

    /**
     * 标记已读
     * @param inboxIds
     */
    void markRead(long... inboxIds);

    /**
     * 标记未读
     * @param inboxIds
     */
    void markUnread(long... inboxIds);

    /**
     * 办理公文
     * @param id
     */
    void dispose(long id);

    /**
     * 获取公文ID和读取状态
     * @param id
     * @return
     */
    OfficialDocInbox findDocIdAndReadById(long id);
}
