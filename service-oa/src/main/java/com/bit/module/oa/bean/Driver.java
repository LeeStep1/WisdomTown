package com.bit.module.oa.bean;

import com.bit.base.exception.CheckException;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Driver
 * @author generator
 */
@Data
public class Driver implements Serializable {

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

    public void checkUpdate() {
        checkId();
    }

    public void checkAdd() {
        checkName().checkDrivingClass().checkDrivingExperience().checkMobile().checkDrivingLicense().checkIdCard();
    }

    private Driver checkId() {
        if (this.getId() == null)
            throw new CheckException("驾驶员ID不能为空");
        return this;
    }

    private Driver checkName() {
        if (StringUtils.isEmpty(this.getName()))
            throw new CheckException("驾驶员姓名不能为空");
        return this;
    }

    private Driver checkDrivingClass() {
        if (StringUtils.isEmpty(this.getDrivingClass()))
            throw new CheckException("驾照等级不能为空");
        return this;
    }

    private Driver checkMobile() {
        if (StringUtils.isEmpty(this.getMobile()))
            throw new CheckException("驾驶员联系电话不能为空");
        return this;
    }

    private Driver checkDrivingExperience() {
        if (this.getDrivingExperience() == null)
            throw new CheckException("驾驶员驾龄不能为空");
        return this;
    }

    private Driver checkDrivingLicense() {
        if (StringUtils.isEmpty(this.getDrivingLicenseFrontSide()) || StringUtils.isEmpty(this.getDrivingLicenseBackSide()))
            throw new CheckException("驾驶员驾照照片不能为空");
        return this;
    }

    private Driver checkIdCard() {
        if (StringUtils.isEmpty(this.getIdCardFrontSide()) || StringUtils.isEmpty(this.getIdCardBackSide()))
            throw new CheckException("驾驶员身份证不能为空");
        return this;
    }

}


