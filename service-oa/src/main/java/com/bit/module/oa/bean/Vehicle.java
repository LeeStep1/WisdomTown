package com.bit.module.oa.bean;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Vehicle
 * @author generator
 */
@Data
public class Vehicle implements Serializable {

	//columns START

    /**
     * id
     */
    @NotNull(message = "车辆id不能为空", groups = Update.class)
	private Long id;
    /**
     * 号牌类型
     */
    @NotBlank(message = "号牌类型不能为空", groups = Add.class)
	private String plateType;
    /**
     * 车牌号
     */
    @NotBlank(message = "车牌号不能为空", groups = Add.class)
	private String plateNo;
    /**
     * 车架号
     */
    @NotBlank(message = "车架号不能为空", groups = Add.class)
	private String vin;
    /**
     * 发动机号
     */
    @NotBlank(message = "发动机号不能为空", groups = Add.class)
	private String engineNo;
    /**
     * 品牌
     */
    @NotBlank(message = "品牌不能为空", groups = Add.class)
	private String brand;
    /**
     * 车型
     */
    @NotBlank(message = "车型不能为空", groups = Add.class)
	private String vehicleType;
    /**
     * 颜色
     */
    @NotBlank(message = "颜色不能为空", groups = Add.class)
	private String color;
    /**
     * 载客量
     */
    @NotNull(message = "载客量不能为空", groups = Add.class)
	private Integer seatingCapacity;
    /**
     * 动力
     */
    @NotBlank(message = "动力不能为空", groups = Add.class)
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

    public interface Add {}

    public interface Update {}
}


