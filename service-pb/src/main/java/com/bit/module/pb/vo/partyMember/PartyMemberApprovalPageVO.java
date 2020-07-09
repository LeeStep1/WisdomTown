package com.bit.module.pb.vo.partyMember;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/3/3 18:20
 */
@Data
public class PartyMemberApprovalPageVO implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 党员名称
     */
    private String name;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 1正式2预备
     */
    private Integer memberType;
    /**
     * 申请类型，1新增党员 2停用党员 3启用党员
     */
    private Integer type;
    /**
     * 状态，0草稿 1审核中 2已通过 3已退回 4等待接收（镇外） 5未接受（镇外）
     */
    private Integer status;
    /**
     * 待审批组织ID
     */
    private String approveOrgId;

    private Date insertTime;

}
