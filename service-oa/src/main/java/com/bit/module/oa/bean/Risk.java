package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/14 11:02
 */
@Data
public class Risk implements Serializable {
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
     * 是否需要整改（0：false；1：true）
     */
    private Boolean needFix;
    /**
     * 整改内容
     */
    private String fixContent;
    /**
     * 状态（0：未反馈; 1：已反馈）
     */
    private Integer status;
    /**
     * 版本号
     */
    private Integer version;
}
