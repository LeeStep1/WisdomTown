package com.bit.module.sv.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class StatisticsVO implements Serializable {
    /**
     * 状态
     */
    private Integer status;

    /**
     * 待办数量
     */
    private Long num;
}
