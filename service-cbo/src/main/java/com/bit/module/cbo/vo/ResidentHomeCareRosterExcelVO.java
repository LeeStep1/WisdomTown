package com.bit.module.cbo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bit.base.vo.BasePageVo;
import com.bit.common.bean.ResidentApplyLog;
import com.bit.module.cbo.bean.ResidentApplyHomeCare;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description: 居家养老服务名单EXcle表
 * @author: liyang
 * @date: 2019-08-14
 **/
@Data
public class ResidentHomeCareRosterExcelVO{

    /**
     * ID
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
     * 申请的主题
     */
    @NotNull(message = "申请的主题不能为空！")
    private String title;

    /**
     * 证件类型
     */
    @Excel(name = "证件类型",width = 12 ,orderNum = "3")
    private String cardType;

    /**
     * 证件号码
     */
    @Excel(name = "证件号码",width = 12 ,orderNum = "4")
    private String cardNum;

    /**
     * 手机号码
     */
    @Excel(name = "手机号码",width = 12 ,orderNum = "5")
    private String mobileNum;

    /**
     * 申请记录申请的日期
     */
    @Excel(name = "申请时间",width = 12 ,orderNum = "6")
    private String applyTime;

    /**
     * 待遇类别
     */
    private String treatmentType;

    /**
     * 待遇类别名称
     */
    @Excel(name = "待遇类别",width = 12 ,orderNum = "7")
    private String treatmentTypeName;

    /**
     * 户口类别
     */
    private String residenceType;

    /**
     * 户口类别名称
     */
    @Excel(name = "户口类别",width = 12 ,orderNum = "8")
    private String residenceTypeName;

    /**
     * 评估等级
     */
    private String level;

    /**
     * 评估等级名称
     */
    @Excel(name = "评估等级",width = 12 ,orderNum = "9")
    private String levelName;

    /**
     * 经办人
     */
    @Excel(name = "经办人",width = 12 ,orderNum = "10")
    private String operatorName;

    /**
     * 状态 是否有效，1：正常，0：已失效
     */
    @Excel(name = "状态",width = 12 ,orderNum = "11")
    private String status;

    /**
     * 生活处理能力  1 基本自理”、2 “半自理”、3 “不能自理
     */
    private String livingAbility;

    /**
     * 生活处理能力名称
     */
    @Excel(name = "生活处理能力",width = 12 ,orderNum = "12")
    private String livingAbilityName;

    /**
     * 银行卡号
     */
    @Excel(name = "银行卡号",width = 16 ,orderNum = "13")
    private String bankCard;

    /**
     * 享受金额
     */
    @Excel(name = "享受金额",width = 12 ,orderNum = "14")
    private String amount;

    /**
     * 上报年月
     */
    @Excel(name = "上报年月",width = 12 ,orderNum = "15")
    private String reportTime;

    /**
     * 家庭电话
     */
    @Excel(name = "家庭电话",width = 12 ,orderNum = "16")
    private String familyMobile;

    /**
     * 家庭地址
     */
    @Excel(name = "家庭地址",width = 18 ,orderNum = "17")
    private String familyAddress;

    /**
     * 居民的id
     */
    @NotNull(message = "居民的id不能为空！")
    private Long residentId;

    /**
     * 申请的类别ID
     */
    @NotNull(message = "类别ID不能为空！")
    private Long categoryId;

    /**
     * 申请的类别名称
     */
    private String categoryName;

    /**
     * 申请的事项名称
     */
    private String serviceName;

    /**
     * 申请的事项ID
     */
    @NotNull(message = "事项ID不能为空！")
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * '数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单'
     */
    @NotNull(message = "数据来源不能为空")
    private Integer dataType;

    /**
     * 关联的申请的id,如果是手动创建的人员名单，此项为空
     */
    private Long applyId;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * '更新时间'
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 创建人id
     */
    private Long createUserId;


    /**
     * 居民养老信息业务
     */
    private ResidentApplyHomeCare residentApplyHomeCare;

    /**
     * 查询申请开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date queryApplyBeginTime;

    /**
     * 查询申请截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private Date queryApplyEndTime;

	/**
	 * 注销备注
	 */
	private String cancellationRemarks;
    /**
     * 附件id
     */
    private String attachedIds;
	/**
	 * 判断服务名单编辑的时候扩展信息有没有更改 0- 未更改 1-已更改
	 */
	private Integer flag;
}
