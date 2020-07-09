package com.bit.module.pb.vo.partyMember;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/30 11:25
 */
@Data
public class PartyMemberQuery extends BasePageVo implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 党员名称
     */
    private String name;
    /**
     * 党员类型，1正式 2预备
     */
    private Integer memberType;

    /**
     * 申请原因：党员去世、停止党员、、、
     */
    private Integer reason;

    /**
     * 申请类型，1新增党员 2停用党员 3启用党员
     */
    private Integer type;

    /**
     * 是否自主择业，0否1是
     */
    private Integer isSelfEmployment;
    /**
     * 组织ID
     */
    private Long orgId;
    /**
     * 组织ID最大范围
     */
    private Long orgMaxId;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 原组织ID
     */
    private Long fromOrgId;
    /**
     * 原组织名称
     */
    private String fromOrgName;
    /**
     * 目标组织
     */
    private String toOrgName;
    /**
     * 改变时间
     */
    private Date changeTime;
    /**
     * 0：待完善；
     * 1：正常；
     * 2：停用；
     * 3：转移；
     */
    private Integer status;
    /**
     * 状态
     * 0：待完善；
     * 1：正常；
     * 2：停用；
     * 3：转移；
     */
    private List<Integer> statuses;
    /**
     * 党员转移关系
     * 1：镇内转移；
     * 2：镇内转镇外；
     * 3：镇内转移+镇内转镇外
     * 4：镇外转镇内
     */
    private Integer transferType;
    /**
     * 原服役部队
     */
    private String originalTroops;
    /**
     * 待审批组织ID
     */
    private String approveOrgId;

}
