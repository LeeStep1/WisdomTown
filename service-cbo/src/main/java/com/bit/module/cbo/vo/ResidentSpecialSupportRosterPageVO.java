package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.cbo.bean.ResidentApplySpecialSupport;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分页使用
 */
@Data
public class ResidentSpecialSupportRosterPageVO extends BasePageVo implements Serializable {
    /**
	 * id
     */
    private Long id;

    /**
     * 申请的主题
     */
    private String title;

    /**
     * 居民的id
     */
    private Long residentId;

    /**
     * 居民的名称
     */
    private String residentName;

    /**
     * 申请时的证件类型
     */
    private Integer cardType;

    /**
     * 证件号
     */
    private String cardNum;

    /**
     * 手机号
     */
    private String mobileNum;

    /**
     * 申请的类别ID
     */
    private Long categoryId;

    /**
     * 申请的类事项ID
     */
    private Long serviceId;

    /**
     * 社区id
     */
    private Long orgId;

    /**
     * 申请记录申请的日期
     */
    private Date applyTime;

    /**
     * 数据创建时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private Long createUserId;

    /**
     * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     */
    private Integer dataType;

    /**
     * 经办人名称
     */
    private String operatorName;

    /**
     * 关联的申请的id
     */
    private Long applyId;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * 是否有效，1：正常，0：已失效
     */
    private Integer status;

    /**
     * 注销备注
     */
    private String cancellationRemarks;

    /**
     * 附件id
     */
    private String attachedIds;

    /**
     * 帮扶类型
     */
    private String supportType;

    
    private static final long serialVersionUID = 1L;
    /**
     * 特殊帮扶
     */
    private ResidentApplySpecialSupport residentApplySpecialSupport;

    /**
     * 查询申请开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date queryApplyBeginTime;

    /**
     * 查询申请截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date queryApplyEndTime;

}