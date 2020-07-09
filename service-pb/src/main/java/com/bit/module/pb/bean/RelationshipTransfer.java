package com.bit.module.pb.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * RelationshipTransfer
 *
 * @author generator
 */
@Data
public class RelationshipTransfer implements Serializable {

    //columns START

    /**
     * id
     */
    private Long id;
    /**
     * 申请人，用户id
     */
    private Long userId;
    /**
     * 申请人组织ID
     */
    private Long userOrgId;
    /**
     * 原组织id
     */
    private String fromOrgId;
    /**
     * 原组织名称
     */
    private String fromOrgName;
    /**
     * 转出原组织时间
     */
    private Date outTime;
    /**
     * 目标组织id
     */
    private String toOrgId;
    /**
     * 目标组织名称
     */
    private String toOrgName;
    /**
     * 转入目标组织时间
     */
    private Date inTime;
    /**
     * 插入时间
     */
    private Date insertTime;
    /**
     * 完成时间
     */
    private Date completeTime;
    /**
     * 状态，0草稿 1审核中 2已通过 3已退回 4等待接收（镇外） 5确认接收（镇外）6未接受（镇外）
     */
    private Integer status;
    /**
     * 党员党费已交至时间
     */
    private String deadline;
    /**
     * 是否退伍军人
     * 1：是；
     * 0：否
     */
    private Integer isExServiceman;
    /**
     * 附件id列表，英文逗号分隔。附件id为文件服务的文件id
     */
    private List<PartyMemberApproval.AttachVO> attach;
    /**
     * 需要修改的党员信息
     */
    private PartyMemberSummary modification;
    /**
     * 待审批组织ID
     */
    private String approveOrgId;
    /**
     * 版本号
     */
    private Integer version;

    //columns END

}


