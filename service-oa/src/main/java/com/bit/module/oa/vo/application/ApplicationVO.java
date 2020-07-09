package com.bit.module.oa.vo.application;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/20 9:55
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApplicationVO extends BasePageVo {
    private Long id;

    private String no;
    /**
     * 巡检执行id
     */
    private Long executeId;
    /**
     * 申请人id
     */
    private Long userId;
    /**
     * 申请人姓名（归档）
     */
    private String userName;
    /**
     * 部门id
     */
    private Long depId;
    /**
     * 部门名称（归档）
     */
    private String depName;
    /**
     * 补卡时间
     */
    private Date applyTime;
    /**
     * 补卡原因
     */
    private String applyReason;
    /**
     * 图片url集合，英文逗号隔开
     */
    private String picUrls;
    /**
     * 驳回原因
     */
    private String rejectReason;
    /**
     * 审批人id
     */
    private Long approverId;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 状态 0未审批 1通过 2驳回
     */
    private Integer status;
}
