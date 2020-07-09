package com.bit.module.oa.vo.vehicleLog;

import com.bit.module.oa.bean.VehicleLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2019/1/16 17:21
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleLogDetailVO extends VehicleLog {
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 品牌
     */
    private String brand;
    /**
     * 车型
     */
    private String vehicleType;
    /**
     * 载客量
     */
    private Integer seatingCapacity;
}
