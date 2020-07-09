package com.bit.module.vol.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenduo
 * @create 2019-03-20 22:31
 */
@Data
public class LevelRegulation {
    /**
     * id
     */
    private Long id;
    /**
     * 级别
     */
    private String regulationLevel;
    /**
     * 服务时长
     */
    private BigDecimal serviceTime;
    /**
     * 捐款总额
     */
    private BigDecimal donationAmount;
    /**
     * 服务层级
     */
    private Integer serviceLevel;
}
