package com.bit.module.oa.vo.driver;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Driver
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DriverVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 姓名
     */	
	private String name;
    /**
     * 年龄
     */	
	private Integer age;
    /**
     * 性别
     */	
	private String sex;
    /**
     * 身高，单位cm
     */	
	private Integer height;
    /**
     * 体重，单位kg
     */	
	private String weight;
    /**
     * 健康状况
     */	
	private String health;
    /**
     * 左眼视力
     */	
	private String leftVision;
    /**
     * 右眼视力
     */	
	private String rightVision;
    /**
     * 驾照等级
     */	
	private String drivingClass;
    /**
     * 驾龄 1一年及一年以下 2一到三年 3三到五年 4五到十年 5十年以上
     */	
	private Integer drivingExperience;
    /**
     * 联系电话
     */	
	private String mobile;
    /**
     * 驾照正面照
     */	
	private String drivingLicenseFrontSide;
    /**
     * 驾照反面照
     */	
	private String drivingLicenseBackSide;
    /**
     * 身份证正面照
     */	
	private String idCardFrontSide;
    /**
     * 身份证反面照
     */	
	private String idCardBackSide;
    /**
     * 状态 0停用 1启用
     */	
	private Integer status;

	//columns END

}


