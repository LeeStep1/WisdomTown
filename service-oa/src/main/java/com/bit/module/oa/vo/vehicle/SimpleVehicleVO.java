package com.bit.module.oa.vo.vehicle;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/10 11:02
 */
@Data
public class SimpleVehicleVO implements Serializable {
    private Long id;

    /**
     * 车牌号
     */
    private String plateNo;

    private String brand;

    private String vehicleType;

    private Integer seatingCapacity;

    private Integer status;
}
