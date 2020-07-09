package com.bit.officialdoc.entity;

import com.bit.officialdoc.vo.FullDoc;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 公文归档
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
@Data
public class OfficialDocArchive implements Serializable{

    /**
     *
     */
    private Long id;

    /**
     * 所有者id
     */
    private Long ownerId;

    /**
     * 文件夹id
     */
    private Long folderId;

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
     * 公文收件人名称集合 冗余字段
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
     * 删除
     */
    private boolean deleted;

    /**
     * 是否有附件
     * 0：没有；
     * 1：有
     */
    private Integer isAttach;
}
