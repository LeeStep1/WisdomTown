package com.bit.module.vol.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-27 12:44
 */
@Data
public class LevelLog {
    /**
     * 申请等级
     */
    private Integer applyLevel;
    /**
     * 原有等级
     */
    private Integer oldLevel;
    /**
     * 提交时间
     */
    private Date createTime;
    /**
     * 审核状态 0-审核中下级审批 1-审核中上级审批 2-已通过 3-已退回
     */
    private Integer auditStatus;
    /**
     * 提交人id
     */
    private Long volunteerId;
}
