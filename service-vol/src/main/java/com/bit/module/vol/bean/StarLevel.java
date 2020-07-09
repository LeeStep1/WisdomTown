package com.bit.module.vol.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenduo
 * @create 2019-03-21 13:46
 */
@Data
public class StarLevel {
    /**
     * 当前星级
     */
    private Integer currentLevel;
    /**
     * 下一星级
     */
    private Integer nextLevel;
    /**
     * 现有时长
     */
    private BigDecimal currentHour;
    /**
     * 需要时长
     */
    private BigDecimal needHour;
    /**
     * 现有捐款
     */
    private BigDecimal currentMoney;
    /**
     * 需要捐款
     */
    private BigDecimal needMoney;
    /**
     * 审核状态  0-审核中下级审批 1-审核中上级审批 2-已通过 3-已退回
     */
    private Integer lastStatus;
}
