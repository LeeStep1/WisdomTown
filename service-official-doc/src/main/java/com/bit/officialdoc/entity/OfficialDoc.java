package com.bit.officialdoc.entity;

import com.bit.officialdoc.vo.FullDoc;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author terry.jiang[taoj555@163.com] on 2019-01-14.
 */
@Data
public class OfficialDoc {

    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_REPLY = 1;
    public static final int TYPE_TRANSFER = 2;

    //public static final String EMPTY_ARRAY_STR = "[]";

    /**
     * doc id
     */
    private Long id;

    /**
     * 公文类型 0-常规 , 1-回复, 2-转发
     */
    private int type = TYPE_NORMAL;

    /**
     * 公文标题
     */
    private String title;

    /**
     * 字号
     */
    private String textNumber;

    /**
     * 正文
     */
    private String content;

    /**
     * 发件人id
     */
    private Long senderId;

    /**
     * 发件人姓名
     */
    private String senderName;

    /**
     * 收件人集合
     */
    private List<FullDoc.Receiver> receivers;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 已发送
     */
    private boolean sent = false;

    /**
     * 已读
     */
    private boolean read = false;

    /**
     * 删除
     * false：未删
     * true：已删
     */
    private boolean deleted = false;

    /**
     * 抄送 JsonString
     * [{"id":"1","name":"cc1"}, {"id":"2","name":"cc2"} ...]
     */
    private List<FullDoc.CcVO> cc;

    /**
     * 附件id集合，英文逗号分隔
     */
    private String attactIds;

    /**
     * 关联公文id
     * type = 1 时 , 表示回复的源公文id
     * type = 2 时 , 表示转发的源公文id
     */
    private Long referenceDocId;

}
