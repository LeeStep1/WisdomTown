package com.bit.module.pb.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 党员信息审核表
 * <p>
 * PartyMemberApproval
 *
 * @author generator
 */
@Data
public class PartyMemberApproval implements Serializable {

    //columns START

    /**
     * id
     */
    private Long id;
    /**
     * 申请类型，1新增党员 2停用党员 3启用党员
     */
    private Integer type;
    /**
     * 申请人，用户id
     */
    private Long userId;
    /**
     * 申请人所在组织id
     */
    private String userOrgId;
    /**
     * 申请原因
     */
    private Integer reason;
    /**
     * 备注
     */
    private String remark;
    /**
     * 附件id列表，英文逗号分隔。附件id为文件服务的文件id
     */
    private List<AttachVO> attach;
    /**
     * 党员id
     */
    private Long memberId;
    /**
     * 需要修改的党员信息
     */
    private PartyMemberSummary modification;
    /**
     * 插入时间
     */
    private Date insertTime;
    /**
     * 完成时间
     */
    private Date completeTime;
    /**
     * 状态，0草稿 1审核中 2已通过 3已退回 4等待接收（镇外） 5未接受（镇外）
     */
    private Integer status;
    /**
     * 待审批组织ID
     */
    private String approveOrgId;
    /**
     * 版本号
     */
    private Integer version;

    //columns END

    @Data
    public static class AttachVO {
        private Long id;
        private String name;
        private String url;
    }
}


