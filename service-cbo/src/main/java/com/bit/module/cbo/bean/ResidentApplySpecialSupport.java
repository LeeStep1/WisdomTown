package com.bit.module.cbo.bean;

import com.bit.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/9/19 8:39
 **/
@Data
public class ResidentApplySpecialSupport extends ExtendTypeBase{
	/**
	 * id
	 */
	private Long id;
	/**
	 * 对应的t_cbo_resident_apply_base的id
	 */
	private Long applyId;
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
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
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
	/**
	 * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
	 */
	private Integer dataType;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 创建人id
	 */
	private Long createUserId;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date updateTime;
	/**
	 * 更新人id
	 */
	private Long updateUserId;
	/**
	 * 对应服务名单的id
	 */
	private Long rosterId;



	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ResidentApplySpecialSupport)) return false;

		ResidentApplySpecialSupport that = (ResidentApplySpecialSupport) o;

		if (!supportType.equals(that.supportType)) return false;
		if (!bankNum.equals(that.bankNum)) return false;
		if (!issuanceDate.equals(that.issuanceDate)) return false;
		if (!familyMobile.equals(that.familyMobile)) return false;

		if (!equalsStr(amount,that.amount)){
			return false;
		};
		return equalsStr(familyAddress,that.familyAddress);
	}

	private boolean equalsStr(String str1, String str2){
		if(StringUtil.isEmpty(str1) && StringUtil.isEmpty(str2)){
			return true;
		}
		if (StringUtil.isNotEmpty(str1) && StringUtil.isNotEmpty(str2)){
			if (str1.equals(str2)){
				return true;
			}else {
				return false;
			}
		}
		return false;
	}


}
