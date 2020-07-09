package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @Description 特殊扶助服务名单变更记录
 * @Author chenduo
 * @Date 2019/9/23 16:41
 **/
@Data
public class SpecialSupportChangeLog {
	/**
	 * 帮扶类型 1-死亡 2-多重
	 */
	private String supportType;
	/**
	 * 银行卡号
	 */
	@Length(max = 19,min = 16,message = "银行卡号是16-19位数")
	private String bankNum;
	/**
	 * 起始扶助日期
	 */
	private Date issuanceDate;
	/**
	 * 金额
	 */
	private String amount;
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
}
