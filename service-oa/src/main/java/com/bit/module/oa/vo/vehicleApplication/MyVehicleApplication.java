package com.bit.module.oa.vo.vehicleApplication;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/1/15 14:15
 */
@Data
public class MyVehicleApplication implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 申请人，用户id
     */
    private Long userId;
    /**
     * 申请人姓名
     */
    private String userName;
    /**
     * 车牌号
     */
    private String plateNos;
    /**
     * 用车性质 0其它 1会议 2应急 3接待 4招商 5迎检
     */
    private Integer nature;
    /**
     * 用车用途 1公务用车 2租赁
     */
    private Integer usage;
    /**
     * 乘车人数
     */
    private Integer passengerNum;
    /**
     * 始发地
     */
    private String origin;
    /**
     * 目的地
     */
    private String terminal;
    /**
     * 计划开始时间
     */
    private Date planStartTime;
    /**
     * 计划结束时间
     */
    private Date planEndTime;
    /**
     * 申请时间
     */
    private Date applyTime;
    /**
     * 状态 0未派车 1已派车 2已结束 3已拒绝
     */
    private Integer status;
}
