package com.bit.module.oa.vo.risk;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/14 18:33
 */
@Data
public class RiskExportQO implements Serializable {
    /**
     * 隐患单号
     */
    private String no;
    /**
     * 主题
     */
    private String name;
    /**
     * 上报人姓名
     */
    private String reporterName;
    /**
     * 上报部门id
     */
    private Long reportDepId;
    /**
     * 上报开始时间
     */
    private Date reportStartTime;
    /**
     * 上报结束时间
     */
    private Date reportEndTime;
    /**
     * 状态（0：未反馈; 1：已反馈）
     */
    private Integer status;
}
