package com.bit.officialdoc.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
@Data
public class SimpleDoc {

    /**
     * 此字段多种表达形式
     * 如查询收件箱内的公文时，表示InboxId
     * 查询回收桶内的公文时，表示RecycleId
     * 查询归档的公文时，表示archiveId
     */
    private Long id;

    private Long docId;

    private String docTitle;

    private String docTextNumber;

    private Long docSenderId;

    private String docSenderName;

    private Date docCreateAt;

    private Boolean read;

    private Boolean replied;

    private List<FullDoc.Receiver> receivers = new ArrayList<>();

    /**
     * 附件id集合，英文逗号分隔
     */
    private String attactIds;

    /**
     * 是否有附件(APP需要显示图标)
     * 0：没有附件；1：有附件
     */
    private Integer isAttach;

}
