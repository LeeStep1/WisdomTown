package com.bit.module.oa.vo.vehicle;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Vehicle
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class VehicleVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 号牌类型
     */	
	private String plateType;
    /**
     * 车牌号
     */	
	private String plateNo;
    /**
     * 车架号
     */	
	private String vin;
    /**
     * 发动机号
     */	
	private String engineNo;
    /**
     * 品牌
     */	
	private String brand;
    /**
     * 车型
     */	
	private String vehicleType;
    /**
     * 颜色
     */	
	private String color;
    /**
     * 载客量
     */	
	private Integer seatingCapacity;
    /**
     * 动力
     */	
	private String power;
    /**
     * 照片
     */	
	private String photo;
    /**
     * 空闲，0否 1是
     */	
	private Integer idle;
    /**
     * 状态，0停用 1启用
     */	
	private Integer status;

	//columns END

}


