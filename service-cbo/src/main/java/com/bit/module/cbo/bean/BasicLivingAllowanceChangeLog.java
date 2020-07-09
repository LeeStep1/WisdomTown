package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 低保服务名单变更记录
 * @Author chenduo
 * @Date 2019/9/23 16:46
 **/
@Data
public class BasicLivingAllowanceChangeLog {
	/**
	 * 低保证号
	 */
	@NotNull(message = "低保证号不能为空！")
	private String cardNum;

	/**
	 * 救助人数
	 */
	@NotNull(message = "救助人数不能为空！")
	private Integer rescueNum;

	/**
	 * 金额
	 */
	@NotNull(message = "金额不能为空！")
	private String amount;

	/**
	 * 银行卡号
	 */
	@NotNull(message = "银行卡号不能为空！")
	private String bankCard;
	/**
	 * 发放时间
	 */
	@NotNull(message = "发放时间不能为空! ")
	private Date releaseTime;

	/**
	 * 户主姓名
	 */
	private String houseHolderName;
	/**
	 * 户主身份证号
	 */
	private String houseHolderCardNum;
	/**
	 * 家庭电话
	 */
	private String familyMobile;
	/**
	 * 家庭住址
	 */
	private String familyAddress;
	/**
	 * 致贫原因
	 */
	private String poorReason;
}
