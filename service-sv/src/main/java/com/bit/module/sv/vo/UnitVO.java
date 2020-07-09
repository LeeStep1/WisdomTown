package com.bit.module.sv.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/7/16 16:26
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UnitVO extends BasePageVo {
    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 单位类型  企业单位 其他单位
     */
    private Integer type;

    /**
     * 类型  合资企业 独资企业 国营企业 外资企业 合资企业

     */
    private Integer companyType;

    /**
     * 单位所属行业
     */
    private String industryCode;

    /**
     * 街道
     */
    private String street;

    /**
     * 地址
     */
    private String address;

    /**
     * 经度
     */
    private Double lng;

    /**
     * 纬度
     */
    private Double lat;

    /**
     * 负责人姓名
     */
    private String principalName;

    /**
     * 负责人联系方式
     */
    private String principalPhone;

    /**
     * 危险品标识
     */
    private Boolean dangerous;

    /**
     * 重点污染监控对象
     */
    private Boolean monitor;

    /**
     * 社会信用代码
     */
    private String creditCode;

    /**
     * 年产量
     */
    private String annualOutput;

    /**
     * 年产值(元)
     */
    private Integer annualOutputValue;

    /**
     * 生产工艺
     */
    private String technology;

    /**
     * 企业人数
     */
    private Integer employees;

    /**
     * 面积
     */
    private Double area;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 企业状态 0 注销 1 启用
     */
    private Integer status;

    private Boolean injuria;
}
