package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/15 9:58
 */
@Data
public class Location implements Serializable {
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;
    /**
     * 巡检执行id
     */
    private Long executeId;
    /**
     * 创建时间
     */
    private Date createAt;
}
