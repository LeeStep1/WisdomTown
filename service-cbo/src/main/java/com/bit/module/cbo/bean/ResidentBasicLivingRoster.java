package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: 居民低保服务名单表
 * @author: liyang
 * @date: 2019-08-14
 **/
@Data
public class ResidentBasicLivingRoster {

    /**
     * id
     */
    private Long id;

    /**
     * '申请的主题'
     */
    @NotNull(message = "申请主体不能为空！")
    private String title;

    /**
     * 居民的id
     */
    @NotNull(message = "居民的id不能为空！")
    private Long residentId;

    /**
     * 居民的名称
     */
    @NotNull(message = "居民的名称不能为空！")
    private String residentName;

    /**
     * 申请时的证件类型
     */
    @NotNull(message = "申请时的证件类型不能为空！")
    private Integer cardType;

    /**
     * 证件号
     */
    @NotNull(message = "证件号不能为空！")
    private String cardNum;

    /**
     * 手机号
     */
    @NotNull(message = "手机号不能为空！")
    private String mobileNum;

    /**
     * 申请的类别ID
     */
    @NotNull(message = "申请的类别ID不能为空！")
    private Long categoryId;

    /**
     * 申请的类事项ID
     */
    @NotNull(message = "申请的类事项ID不能为空！")
    private Long serviceId;

    /**
     * 社区id
     */
    private Long orgId;

    /**
     * 申请记录申请的日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date applyTime;

    /**
     * 数据创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 有效期的开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date beginTime;

    /**
     * 有效期的结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date endTime;

    /**
     * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     */
    @NotNull(message = "数据来源类型不能为空！")
    private Integer dataType;

    /**
     * 关联的申请的id,如果是手动创建的人员名单，此项为空
     */
    private Long applyId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * 经办人名称
     */
    private String operatorName;
    /**
     * 注销备注
     */
    private String cancellationRemarks;
    /**
     * 户主
     */
    private String houseHolderName;
}
