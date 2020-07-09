package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * {"id":xx,"name":"xxx","lng":xx,"lat":xx}
 * @Description :
 * @Date ： 2019/2/15 18:04
 */
@Data
public class SimpleSpot implements Serializable {
    private Long id;
    /**
     * 签到点名称
     */
    private String name;
    /**
     * 签到点经度
     */
    private Double lng;
    /**
     * 签到点纬度
     */
    private Double lat;
}
