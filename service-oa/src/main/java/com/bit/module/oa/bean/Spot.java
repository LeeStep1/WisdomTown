package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/2/13 17:26
 */
@Data
public class Spot implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 区域ID
     */
    private Long zoneId;
    /**
     * 经度
     */
    private Double lng;
    /**
     * 纬度
     */
    private Double lat;

    /**
     * 状态，0停用 1启用
     */
    private Integer status;
}
