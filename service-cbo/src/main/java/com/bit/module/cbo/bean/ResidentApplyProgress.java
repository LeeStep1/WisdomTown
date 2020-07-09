package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: 办事流程步揍
 * @author: liyang
 * @date: 2019-08-13
 **/
@Data
public class ResidentApplyProgress {

    /**
     * id
     */
    private Long id;

    /**
     * 申请ID
     */
    private Long applyId;

    /**
     * 后台审核的录入的时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date feedbackTime;

    /**
     * 反馈人的id
     */
    private Long feedbackUserId;

    /**
     * 每步转态：0未审核，1通过，2未通过
     */
    private Integer stepStatus;

    /**
     * 拒绝原因
     */
    private String rejectReason;

    /**
     * item表对应ID
     */
    private Long guideItemId;

    /**
     * 对应事件ID
     */
    private Long guideId;

    /**
     * 是否有业务扩展信息:1有，0无
     */
    private Integer extend;
}
