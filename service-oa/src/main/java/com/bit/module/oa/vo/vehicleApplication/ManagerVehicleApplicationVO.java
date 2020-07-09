package com.bit.module.oa.vo.vehicleApplication;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/1/15 14:15
 */
@Data
public class ManagerVehicleApplicationVO implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 车牌号列表，英文逗号分隔
     */
    private String plateNos;
    /**
     * 申请人，用户id
     */
    private Long userId;
    /**
     * 申请人用户名
     */
    private String userName;
    /**
     * 申请单号
     */
    private String applyNo;
    /**
     * 乘车人数
     */
    private Integer passengerNum;
    /**
     * 用车性质 0其它 1会议 2应急 3接待 4招商 5迎检
     */
    private Integer nature;
    /**
     * 用车用途 1公务用车 2租赁
     */
    private Integer usage;
    /**
     * 驾驶员列表，英文逗号分隔，每项的格式为：[驾驶员名称]([驾驶员联系电话])
     */
    private String drivers;
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
     * 申请时间
     */
    private Date applyTime;
    /**
     * 始发地
     */
    private String origin;
    /**
     * 目的地
     */
    private String terminal;
    /**
     * 状态 0未派车 1已派车 2已结束 3已拒绝
     */
    private Integer status;
}
