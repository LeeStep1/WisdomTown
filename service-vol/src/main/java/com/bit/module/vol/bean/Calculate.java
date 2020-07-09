package com.bit.module.vol.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author chenduo
 * @create 2019-03-14 15:15
 */
@Data
public class Calculate {

    /**
     * 次数
     */
    private Integer number;
    /**
     * 时长
     */
    private BigDecimal hour;
    /**
     * 捐款数
     */
    private BigDecimal money;
}
