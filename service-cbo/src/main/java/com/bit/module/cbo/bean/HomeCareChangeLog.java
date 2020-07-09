package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Description 居家养老服务名单变更记录
 * @Author chenduo
 * @Date 2019/9/23 16:44
 **/
@Data
public class HomeCareChangeLog {
	/**
	 * 生活处理能力  1 基本自理”、2 “半自理”、3 “不能自理
	 */
	private String livingAbility;

	/**
	 * 户口类别 1 市级（非农）  2	市级（农业） 3	非市级（非农）  4	非市级（农业）
	 */
	@NotNull(message = "户口类别不能为空！")
	private String residenceType;

	/**
	 * 户口类别名称
	 */
	private String residenceTypeName;

	/**
	 * 待遇类别   1	优抚 2	低保  3	80岁以上
	 */
	@NotNull(message = "待遇类别不能为空！")
	private String treatmentType;

	/**
	 * 评估等级  1	轻度  2	中度  3	重度
	 */
	@NotNull(message = "评估等级不能为空！")
	private String level;

	/**
	 * 银行卡号
	 */
	@NotNull(message = "银行卡号不能为空！")
	private String bankCard;

	/**
	 * 金额
	 */
	@NotNull(message = "金额不能为空！")
	private String amount;

	/**
	 * 上报时间
	 */
	@NotNull(message = "上报时间不能为空！")
	private Date reportTime;
	/**
	 * 家庭电话
	 */
	private String familyMobile;
	/**
	 * 家庭地址
	 */
	private String familyAddress;
}
