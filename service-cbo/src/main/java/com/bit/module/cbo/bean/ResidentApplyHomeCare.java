package com.bit.module.cbo.bean;

import com.bit.utils.StringUtil;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description: 补充业务信息---居家养老
 * @author: liyang
 * @date: 2019-08-12
 **/
@Data
public class ResidentApplyHomeCare extends ExtendTypeBase{

    /**
     * ID
     */
    private Long id;
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
     * 待遇类别名称
     */
    private String treatmentTypeName;

    /**
     * 评估等级  1	轻度  2	中度  3	重度
     */
    @NotNull(message = "评估等级不能为空！")
    private String level;

    /**
     * 评估等级名称
     */
    private String levelName;

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
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @NotNull(message = "上报时间不能为空！")
    private Date reportTime;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新人员ID
     */
    private Long updateUserId;

    /**
     * 申请台账的ID
     */
    private Long applyId;

    /**
     * 对应服务名单的id
     */
    private Long rosterId;

    /**
     * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     */
    private Integer dataType;
	/**
	 * 家庭电话
	 */
	private String familyMobile;
	/**
	 * 家庭地址
	 */
    private String familyAddress;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ResidentApplyHomeCare)) return false;

        ResidentApplyHomeCare that = (ResidentApplyHomeCare) o;

        if (!livingAbility.equals(that.livingAbility)) return false;
        if (!residenceType.equals(that.residenceType)) return false;
        if (!treatmentType.equals(that.treatmentType)) return false;
        if (!level.equals(that.level)) return false;
        if (!bankCard.equals(that.bankCard)) return false;
//        if (!reportTime.equals(that.reportTime)) return false;
        if (!familyMobile.equals(that.familyMobile)) return false;
        if (reportTime != null ? !reportTime.equals(that.reportTime) : that.reportTime != null) return false;
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
