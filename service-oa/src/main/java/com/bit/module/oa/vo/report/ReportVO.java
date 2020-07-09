package com.bit.module.oa.vo.report;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/19 14:28
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ReportVO extends BasePageVo {
    private Long id;
    /**
     * 巡检id
     */
    private Long inspectId;
    /**
     * 上报单号
     */
    private String no;
    /**
     * 是否异常（0：false；1：true）
     */
    private Boolean abnormal;
    /**
     * 上报内容
     */
    private String content;
    /**
     * 上报信息图片url集合，英文逗号隔开
     */
    private String picUrls;
    /**
     * 是否需要整改（0：false；1：true）
     */
    private Boolean needFix;
    /**
     * 整改内容
     */
    private String fixContent;
    /**
     * 整改信息图片url集合，英文逗号隔开
     */
    private String fixPicUrls;
    /**
     * 上报人id
     */
    private Long reporterId;
    /**
     * 上报人姓名
     */
    private String reporterName;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 状态 0未确认 1已确认
     */
    private Integer status;
}
