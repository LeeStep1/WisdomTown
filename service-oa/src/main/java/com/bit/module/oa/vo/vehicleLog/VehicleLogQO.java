package com.bit.module.oa.vo.vehicleLog;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/1/16 18:20
 */
@Data
public class VehicleLogQO implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 车辆id
     */
    private List<Long> vehicleId;
    /**
     * 查询时间
     */
    private Date queryDate;
}
