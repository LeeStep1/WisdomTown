package com.bit.officialdoc.entity;

import com.bit.officialdoc.vo.FullDoc;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 公文收件箱
 *
 * @author terry.jiang[taoj555@163.com] on 2019-01-14.
 */
@Data
public class OfficialDocInbox {

    /**
     *f
     */
    private Long id;

    /**
     * 收件人id，包含抄送收件人
     */
    private Long receiverId;

    /**
     * 收件日期
     */
    private Date receiveAt;

    /**
     * 公文id
     */
    private Long docId;

    /**
     * 公文标题 冗余字段
     */
    private String docTitle;

    /**
     * 公文字号 冗余字段
     */
    private String docTextNumber;

    /**
     * 公文发送人id 冗余字段
     */
    private Long docSenderId;

    /**
     * 公文发件人名称 冗余字段
     */
    private String docSenderName;

    /**
     * 公文创建时间 冗余字段
     */
    private Date docCreateAt;

    /**
     * 0 - 待办 1 - 已办
     */
    private boolean processed;

    /**
     * 已读
     */
    private boolean read = false;

    /**
     * 已回复
     */
    private boolean replied = false;

    /**
     * 已删除
     */
    private boolean deleted = false;

    /**
     * 是否有附件
     * 0：没有；
     * 1：有
     */
    private Integer isAttach;

    /**
     * 收件人集合
     */
    private List<FullDoc.Receiver> docReceivers;

}
