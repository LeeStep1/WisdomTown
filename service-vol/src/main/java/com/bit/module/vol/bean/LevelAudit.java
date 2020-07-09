package com.bit.module.vol.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-20 13:25
 */
@Data
public class LevelAudit {
    /**
     * id
     */
    private Long id;
    /**
     * 志愿者id
     */
    private Long volunteerId;
    /**
     * 申请等级
     */
    private Integer applyLevel;
    /**
     * 审核状态  0-审核中下级审批 1-审核中上级审批 2-已通过 3-已退回
     */
    private Integer auditStatus;
    /**
     * 审核时间
     */
    private Date createTime;
    /**
     * 申请人id
     */
    private Long createUserId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人id
     */
    private Long updateUserId;
    /**
     * 审批的服务站id
     */
    private Long auditStationId;
    /**
     * 所属的服务站id
     */
    private Long belongStationId;
    /**
     * 数据锁
     */
    private Integer version;
    /**
     * 退回原因
     */
    private String rejectReason;
    /**
     * 原先等级
     */
    private Integer oldLevel;
}
