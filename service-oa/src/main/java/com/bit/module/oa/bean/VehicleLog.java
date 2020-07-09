package com.bit.module.oa.bean;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * VehicleLog
 * @author generator
 */
@Data
public class VehicleLog implements Serializable {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 车辆id
     */	
	private Long vehicleId;
    /**
     * 开始时间
     */	
	private Date startTime;
    /**
     * 结束时间
     */	
	private Date endTime;
    /**
     * 用车申请记录id
     */	
	private Long applicationId;

	//columns END

}


