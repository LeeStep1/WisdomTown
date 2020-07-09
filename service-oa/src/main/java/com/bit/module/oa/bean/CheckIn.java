package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description : 任务签到点
 * @Date ： 2019/2/15 17:46
 */
@Data
public class CheckIn implements Serializable {
    private Long id;
    /**
     * 巡检id
     */
    private Long inspectId;
    /**
     * 巡检执行id
     */
    private Long executeId;
    /**
     * 签到点id
     */
    private Long spotId;
    /**
     * 签到点名称（包含临时点）
     */
    private String spotName;
    /**
     * 签到点经度
     */
    private Double spotLng;
    /**
     * 签到点纬度
     */
    private Double spotLat;
    /**
     * 签到时间
     */
    private Date checkInAt;
    /**
     * 签到人id
     */
    private Long userId;
    /**
     * 签到人姓名
     */
    private String userName;
    /**
     * 创建时间
     */
    private Date createAt;
}
