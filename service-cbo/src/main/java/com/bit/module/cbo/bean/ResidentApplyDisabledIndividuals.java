package com.bit.module.cbo.bean;

import com.bit.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/9/18 16:54
 **/
@Data
public class ResidentApplyDisabledIndividuals extends ExtendTypeBase{
	/**
	 * id
	 */
	private Long id;
	/**
	 * 对应的t_cbo_resident_apply_base的id
	 */
	private Long applyId;
	/**
	 * 残疾证号
	 */
	@Length(max = 22,min = 20,message = "残疾证号是20-22位数")
	private String cardNum;
	/**
	 * 签发日期
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
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
	@Length(max = 100,message = "不超过30个字")
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
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date livingIssuanceDate;
	/**
	 * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
	 */
	private Integer dataType;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
	private Date createTime;
	/**
	 * 创建人
	 */
	private Long createUserId;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date updateTime;
	/**
	 * 更新人
	 */
	private Long updateUserId;
	/**
	 * 对应服务名单的id
	 */
	private Long rosterId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ResidentApplyDisabledIndividuals)) return false;

		ResidentApplyDisabledIndividuals that = (ResidentApplyDisabledIndividuals) o;

		if (!cardNum.equals(that.cardNum)) return false;
		if (!issuanceDate.equals(that.issuanceDate)) return false;
		if (!disabilityCategory.equals(that.disabilityCategory)) return false;
		if (!disabilityLevel.equals(that.disabilityLevel)) return false;
		if (!familyMobile.equals(that.familyMobile)) return false;
		if (!livingAble.equals(that.livingAble)) return false;
		if (rescueNum != null ? !rescueNum.equals(that.rescueNum) : that.rescueNum != null) return false;

		if (!equalsStr(familyAddress,that.familyAddress)){
			return false;
		};
		if (!equalsStr(livingCardNum,that.livingCardNum)){
			return false;
		};
		if (!equalsStr(livingAmount,that.livingAmount)){
			return false;
		};

		return livingIssuanceDate != null ? livingIssuanceDate.equals(that.livingIssuanceDate) : that.livingIssuanceDate == null;
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
