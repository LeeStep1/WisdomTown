package com.bit.officialdoc.entity;

import com.bit.officialdoc.vo.FullDoc;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 公文回收桶 archive
 *
 * @author terry.jiang[taoj555@163.com] on 2019-01-14.
 */
@Data
public class OfficialDocRecycleBin {

    // 来源 0-发件箱 1-收件箱 2-个人归档
    public static final int SOURCE_OUTBOX = 0;
    public static final int SOURCE_INBOX = 1;
    public static final int SOURCE_ARCHIVE = 2;

    /**
     *
     */
    private Long id;

    /**
     * 所有者id
     */
    private Long ownerId;

    /**
     * 来源
     */
    private int source = SOURCE_OUTBOX;

    /**
     * 引用id
     * source == SOURCE_OUTBOX 时, 表示公文id
     * source == SOURCE_INBOX 时, 表示收件箱id
     * source == SOURCE_ARCHIVE 时, 表示归档id
     */
    private Long referenceId;

    /**
     * 删除时间
     */
    private Date deleteAt;

    /**
     * 公文标题 冗余字段
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
     * 公文发件人id 冗余字段
     */
    private Long docSenderId;

    /**
     * 公文发件人名称 冗余字段
     */
    private String docSenderName;

    /**
     * 收件人
     */
    private List<FullDoc.Receiver> docReceivers;

    /**
     * 公文创建时间 冗余字段
     */
    private Date docCreateAt;

    /**
     * 已读
     */
    private boolean read;

    /**
     * 是否有附件
     * 0：没有；
     * 1：有
     */
    private Integer isAttach;

}
