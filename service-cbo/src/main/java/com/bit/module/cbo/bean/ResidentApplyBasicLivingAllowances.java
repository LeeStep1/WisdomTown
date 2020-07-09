package com.bit.module.cbo.bean;

import com.bit.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: 低保业务信息表
 * @author: liyang
 * @date: 2019-08-12
 **/
@Data
//todo 继承部分为优化的代码，先不提交
public class ResidentApplyBasicLivingAllowances extends ExtendTypeBase{

    /**
     * id
     */
    private Long id;

    /**
     * 申请ID
     */
    private Long applyId;

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
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "发放时间不能为空! ")
    private Date releaseTime;

    /**
     * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     */
    @NotNull(message = "数据来源类型不能为空! ")
    private Integer dataType;

    /**
     * 数据创建时间
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
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 对应服务名单的id
     */
    private Long rosterId;

    /**
     * 经办人名称
     */
    private String operatorName;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ResidentApplyBasicLivingAllowances)) return false;

		ResidentApplyBasicLivingAllowances that = (ResidentApplyBasicLivingAllowances) o;

		if (!cardNum.equals(that.cardNum)) return false;
		if (!rescueNum.equals(that.rescueNum)) return false;
		if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;
		if (!bankCard.equals(that.bankCard)) return false;
		if (!releaseTime.equals(that.releaseTime)) return false;
		if (!houseHolderName.equals(that.houseHolderName)) return false;
		if (!houseHolderCardNum.equals(that.houseHolderCardNum)) return false;
		if (!familyMobile.equals(that.familyMobile)) return false;

		if (!equalsStr(familyAddress,that.familyAddress)){
			return false;
		};

		return poorReason.equals(that.poorReason);
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
