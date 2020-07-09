package com.bit.module.pb.vo.partyMember;

import com.bit.module.pb.bean.PartyMemberApproval;
import com.bit.module.pb.bean.PartyMemberSummary;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 党员转移VO
 *
 * @autor xiaoyu.fang
 * @date 2019/3/4 17:37
 */
@Data
public class RelationshipTransferVO implements Serializable {

    //columns START

    /**
     * id
     */
    private Long id;
    /**
     * 申请人ID
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
     * 目标组织id
     */
    private String toOrgId;
    /**
     * 目标组织名称
     */
    private String toOrgName;
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

    private Integer version;

    //columns END
    /**
     * 身份证号
     */
    private String idCard;
    /**
     * 党员id
     */
    private Long memberId;

}
