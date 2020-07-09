package com.bit.module.pb.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 党建VO
 *
 * @autor xiaoyu.fang
 * @date 2019/1/30 10:55
 */
@Data
public class SimplePartyMember implements Serializable {

    private Long id;

    /**
     * 党员名称
     */
    private String memberName;

    /**
     * 组织ID
     */
    private Long orgId;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 联系电话
     */
    private String mobile;

    /**
     * 党员类型，1正式 2预备
     */
    private Integer memberType;

    /**
     * 状态，0待完善 1正常 2停用
     */
    private Integer status;

    /**
     * 入党时间
     */
    private Date joinTime;

    /**
     * 申请原因
     */
    private String reason;

    /**
     * 停用时间
     */
    private Date executeTime;

    /**
     * 申请类型，1新增党员 2停用党员 3启用党员
     */
    private Integer type;

    /**
     * 目标组织id
     */
    private String toOrgId;
    /**
     * 目标组织名称
     */
    private String toOrgName;

    /**
     * 原服役部队
     */
    private String originalTroops;
    /**
     * 退役时间
     */
    private Date retireTime;
    /**
     * 是否自主择业，0否1是
     */
    private Integer isSelfEmployment;

}
