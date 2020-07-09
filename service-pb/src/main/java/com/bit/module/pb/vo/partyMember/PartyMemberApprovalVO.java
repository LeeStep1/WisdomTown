package com.bit.module.pb.vo.partyMember;

import com.bit.module.pb.bean.PartyMemberApproval;
import com.bit.module.pb.bean.PartyMemberSummary;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/3/3 21:14
 */
@Data
public class PartyMemberApprovalVO implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 申请类型，1新增党员 2停用党员 3启用党员
     */
    private Integer type;
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
    private List<PartyMemberApproval.AttachVO> attach;
    /**
     * 党员id
     */
    private Long memberId;
    /**
     * 需要修改的党员信息
     */
    private PartyMemberSummary modification;
    /**
     * 状态，0草稿 1审核中 2已通过 3已退回 4等待接收（镇外） 5未接受（镇外）
     */
    private Integer status;
    /**
     * 申请人组织ID
     */
    private Long orgId;
    /**
     * 申请人ID
     */
    private Long userId;
    /**
     * 待审批组织ID
     */
    private String approveOrgId;

    private Integer version;
    /**
     * 身份证号
     */
    private String idCard;

}
