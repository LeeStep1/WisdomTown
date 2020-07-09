package com.bit.module.cbo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bit.common.bean.ResidentApplyLog;
import com.bit.module.cbo.bean.ResidentApplySpecialSupport;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ResidentSpecialSupportRosterExcelVO implements Serializable {
    /**
	 * id
     */
    private Long id;

    /**
     * 序号
     */
    @Excel(name = "序号",width = 6,orderNum = "1")
    private Long orderId;

    /**
     * 居民的名称
     */
    @Excel(name = "申请人",width = 12,orderNum = "2")
    private String residentName;

    /**
     * 申请时的证件类型
     */
    @Excel(name = "证件类型",width = 12,orderNum = "3")
    private String cardType;

    /**
     * 证件号
     */
    @Excel(name = "证件号码",width = 12,orderNum = "4")
    private String cardNum;

    /**
     * 手机号
     */
    @Excel(name = "手机号码",width = 12,orderNum = "5")
    private String mobileNum;

    /**
     * 申请记录申请的日期
     */
    @Excel(name = "申请时间",width = 12,orderNum = "6")
    private String applyTime;

    /**
     * 帮扶类型Code
     */
    private String supportType;

    /**
     * 帮扶类型名称
     */
    @Excel(name = "特扶类别",width = 12,orderNum = "7")
    private String supportTypeName;

    /**
     * 经办人名称
     */
    @Excel(name = "经办人",width = 12,orderNum = "8")
    private String operatorName;

    /**
     * 起始扶助日期
     */
    @Excel(name = "起始扶助日期",width = 12,orderNum = "9")
    private String issuanceDate;

    /**
     * 银行卡号
     */
    @Excel(name = "银行卡号",width = 12,orderNum = "10")
    private String bankNum;

    /**
     * 金额
     */
    @Excel(name = "扶助金额",width = 12,orderNum = "11")
    private String amount;

    /**
     * 家属联系方式
     */
    @Excel(name = "家属联系方式",width = 12,orderNum = "12")
    private String familyMobile;

    /**
     * 家庭住址
     */
    @Excel(name = "家庭住址",width = 12,orderNum = "13")
    private String familyAddress;

    /**
     * 是否有效，1：正常，0：已失效
     */
    @Excel(name = "状态",width = 12,orderNum = "14")
    private String status;

    /**
     * 申请的主题
     */
    private String title;

    /**
     * 申请的类别ID
     */
    private Long categoryId;

    /**
     * 申请的类事项ID
     */
    private Long serviceId;

    /**
     * 社区id
     */
    private Long orgId;
	/**
	 * 社区名称
	 */
	private String orgName;

    /**
     * 数据创建时间
     */
	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date createTime;

    /**
     * 关联的申请的id
     */
    private Long applyId;

    /**
     * 注销备注
     */
    private String cancellationRemarks;

    private static final long serialVersionUID = 1L;

    /**
     * 申请的类别名称
     */
    private String categoryName;
	/**
	 * 服务名称
	 */
	private String serviceName;
}