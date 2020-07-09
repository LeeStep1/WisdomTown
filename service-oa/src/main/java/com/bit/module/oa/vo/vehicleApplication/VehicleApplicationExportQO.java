package com.bit.module.oa.vo.vehicleApplication;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/1/19 16:57
 */
@Data
public class VehicleApplicationExportQO implements Serializable {
    /**
     * 申请人，用户id
     */
    private Long userId;
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 计划开始时间
     */
    private Date planStartTime;
    /**
     * 计划结束时间
     */
    private Date planEndTime;
    /**
     * 开始时间
     */
    private Date startTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 申请开始时间
     */
    private Date minApplyTime;
    /**
     * 申请开始时间
     */
    private Date maxApplyTime;
    /**
     * 用车性质 0其它 1会议 2应急 3接待 4招商 5迎检
     */
    private Integer nature;
    /**
     * 用车用途 1公务用车 2租赁
     */
    private Integer usage;
    /**
     * 状态 0未派车 1已派车 2已结束 3已拒绝
     */
    private Integer status;
}
