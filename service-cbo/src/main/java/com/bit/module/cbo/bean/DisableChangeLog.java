package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @Description 残疾人服务名单变更记录
 * @Author chenduo
 * @Date 2019/9/23 16:43
 **/
@Data
public class DisableChangeLog {

	/**
	 * 残疾证号
	 */
	@Length(max = 22,min = 20,message = "残疾证号是20-22位数")
	private String cardNum;
	/**
	 * 签发日期
	 */
	private Date issuanceDate;
	/**
	 * 残疾类别
	 */
	private String disabilityCategory;
	/**
	 * 残疾等级
	 */
	private String disabilityLevel;
	/**
	 * 家属联系方式
	 */
	@Length(max = 11,min = 8,message = "请输入8-11位数字")
	private String familyMobile;
	/**
	 * 家庭住址
	 */
	@Length(max = 60,message = "不超过30个字")
	private String familyAddress;
	/**
	 * 是否低保用户：1：是低保  0：不是低保
	 */
	private Integer livingAble;
	/**
	 * 低保证号
	 */
	@Length(max = 20,min = 5,message = "请输入5-20位号码")
	private String livingCardNum;
	/**
	 * 低保救助金额
	 */
	private String livingAmount;
	/**
	 * 救助人数
	 */
	private String rescueNum;
	/**
	 * 低保证签发时间
	 */
	private Date livingIssuanceDate;
}
