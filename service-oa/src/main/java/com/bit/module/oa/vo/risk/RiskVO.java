package com.bit.module.oa.vo.risk;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/14 11:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RiskVO extends BasePageVo {
    /**
     * id
     */
    private Long id;
    /**
     * 隐患单号
     */
    private String no;
    /**
     * 主题
     */
    private String name;
    /**
     * 上报人，用户id
     */
    private Long reporterId;
    /**
     * 上报人姓名
     */
    private String reporterName;
    /**
     * 上报部门id
     */
    private Long reportDepId;
    /**
     * 上报部门名称
     */
    private String reportDepName;
    /**
     * 上报时间
     */
    private Date reportTime;
    /**
     * 上报开始时间
     */
    private Date reportStartTime;
    /**
     * 上报结束时间
     */
    private Date reportEndTime;
    /**
     * 上报地点
     */
    private String reportLocation;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 异常情况
     */
    private String exceptionContent;
    /**
     * 异常图片url，最多9张图片，英文逗号分隔
     */
    private String exceptionPicUrls;
    /**
     * 整改状态（0：否；1：是）
     */
    private Integer fixStatus;
    /**
     * 整改内容
     */
    private String fixContent;
    /**
     * 状态（0：未反馈; 1：已反馈）
     */
    private Integer status;
}
