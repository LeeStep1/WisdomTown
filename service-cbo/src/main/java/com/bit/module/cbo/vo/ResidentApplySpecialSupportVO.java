package com.bit.module.cbo.vo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/9/19 8:39
 **/
@Data
public class ResidentApplySpecialSupportVO {
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
	private Date createTime;
	/**
	 * 创建人id
	 */
	private Long createUserId;
	/**
	 * 更新时间
	 */
	private Date updateTime;
	/**
	 * 更新人id
	 */
	private Long updateUserId;
	/**
	 * 对应服务名单的id
	 */
	private Long rosterId;
}
