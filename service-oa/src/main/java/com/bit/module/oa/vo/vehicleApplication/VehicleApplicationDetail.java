package com.bit.module.oa.vo.vehicleApplication;

import com.bit.module.oa.bean.VehicleApplication;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2019/1/17 15:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleApplicationDetail extends VehicleApplication {
    private String userName;

    private String companyName;

    private String assignerName;
}
