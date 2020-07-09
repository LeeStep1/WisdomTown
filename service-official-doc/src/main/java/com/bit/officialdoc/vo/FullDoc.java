package com.bit.officialdoc.vo;

import com.bit.officialdoc.entity.OfficialDoc;
import com.bit.officialdoc.entity.OfficialDocInbox;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
@Data
public class FullDoc {

    /**
     * 此字段多种表达形式
     * 如查询收件箱内的公文时，表示InboxId
     * 查询回收桶内的公文时，表示RecycleId
     * 查询归档的公文时，表示archiveId
     */
    private Long id;

    private Long docId;

    /**
     * 公文类型 0-常规 , 1-回复, 2-转发
     */
    private int docType;

    @NotBlank(message = "公文标题不能为空")
    private String docTitle;

    private String docTextNumber;

    private String docContent;

    private Long docSenderId;

    private String docSenderName;

    private Date docCreateAt = new Date();

    // 是否已读
    private Boolean read;

    // 是否处理
    private Boolean processed;

    // 是否回复
    private Boolean replied;

    private Integer source;

    /**
     * docId
     */
    private Long referenceDocId;
    /**
     * 草稿使用
     */
    private List<Receiver> receivers = new ArrayList<>();

    /**
     * 抄送人
     */
    private List<CcVO> cc = new ArrayList<>();
    /**
     * 附件id集合，英文逗号分隔
     */
    private String attactIds;

    /**
     * 接收前端回传的用户IDS和部门IDS
     */
    private List<Long> receiverIds;

    /**
     * 抄送人ID
     */
    private List<Long> ccIds;

    /**
     * 是否发送 false：未发送；true：发送
     */
    private Boolean sent;

    /**
     * 回填信息
     *
     * @param fullDoc
     * @return
     */
    public static OfficialDoc buildOfficelDoc(FullDoc fullDoc, Boolean sent) {
        OfficialDoc officialDoc = new OfficialDoc();
        if (fullDoc.getDocId() == null) {
            officialDoc.setType(OfficialDoc.TYPE_NORMAL);
            // 已读
            officialDoc.setRead(true);
            officialDoc.setReferenceDocId(fullDoc.getDocId());
        } else {
            // 公文类型
            officialDoc.setType(fullDoc.getDocType());
            officialDoc.setId(fullDoc.getDocId());
        }
        officialDoc.setCreateAt(new Date());
        // 发件人
        officialDoc.setSenderId(fullDoc.getDocSenderId());
        officialDoc.setSenderName(fullDoc.getDocSenderName());
        // 可以修改的内容
        // 公文标题
        officialDoc.setTitle(fullDoc.getDocTitle());
        // 公文字号
        officialDoc.setTextNumber(fullDoc.getDocTextNumber());
        // 内容
        officialDoc.setContent(fullDoc.getDocContent());
        // 收件人
        officialDoc.setReceivers(fullDoc.getReceivers());
        // 抄送人
        officialDoc.setCc(fullDoc.getCc());
        // 附件
        officialDoc.setAttactIds(fullDoc.getAttactIds());
        // 是否发送
        officialDoc.setSent(sent == null ? false : sent);
        return officialDoc;
    }

    /**
     * 回填待办公文
     *
     * @param fullDoc
     * @return
     */
    public static OfficialDocInbox buildOfficialDocInbox(FullDoc fullDoc) {
        OfficialDocInbox officialDocInbox = new OfficialDocInbox();

        officialDocInbox.setReceiveAt(new Date());
        officialDocInbox.setDocId(fullDoc.getDocId() == null ? fullDoc.getId() : fullDoc.getDocId());
        officialDocInbox.setDocTitle(fullDoc.getDocTitle());
        officialDocInbox.setDocTextNumber(fullDoc.getDocTextNumber());

        officialDocInbox.setDocSenderId(fullDoc.getDocSenderId());
        officialDocInbox.setDocSenderName(fullDoc.getDocSenderName());

        officialDocInbox.setDocReceivers(fullDoc.getReceivers());
        officialDocInbox.setDocCreateAt(fullDoc.getDocCreateAt());

        officialDocInbox.setIsAttach(fullDoc.getAttactIds() != null ? 1 : 0);
        officialDocInbox.setProcessed(false);
        officialDocInbox.setRead(false);

        officialDocInbox.setReplied(false);
        officialDocInbox.setDeleted(false);

        return officialDocInbox;
    }


    @Data
    public static class Receiver {
        private Long id;
        private String name;
    }

    @Data
    public static class CcVO {
        private Long id;
        private String name;
    }

}
