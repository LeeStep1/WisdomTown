package com.bit.module.oa.vo.vehicleLog;

import java.util.Date;
import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * VehicleLog
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleLogVO extends BasePageVo{

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
     * 车牌
     */
	private String plateNo;
    /**
     * 开始时间
     */	
	private Date startTime;
    /**
     * 结束时间
     */	
	private Date endTime;
    /**
     * 查询日期
     */
    private Date queryDate;
    /**
     * 用车申请记录id
     */	
	private Long applicationId;

	//columns END

}


