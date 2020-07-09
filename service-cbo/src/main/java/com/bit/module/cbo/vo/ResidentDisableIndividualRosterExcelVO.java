package com.bit.module.cbo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bit.common.bean.ResidentApplyLog;
import com.bit.module.cbo.bean.ResidentApplyDisabledIndividuals;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ResidentDisableIndividualRosterExcelVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * 序号
	 */
	@Excel(name = "序号" ,width = 6 ,orderNum = "1")
	private Long orderId;

	/**
	 * 居民的名称
	 */
	@Excel(name = "申请人" ,width = 12 ,orderNum = "2")
	private String residentName;

	/**
	 * 申请时的证件类型
	 */
	@Excel(name = "证件类型" ,width = 12 ,orderNum = "3")
	private String cardType;

	/**
	 * 证件号码
	 */
	@Excel(name = "证件号码" ,width = 12 ,orderNum = "4")
	private String cardNum;

	/**
	 * 手机号
	 */
	@Excel(name = "手机号" ,width = 12 ,orderNum = "5")
	private String mobileNum;

	/**
	 * 申请记录申请的日期
	 */
	@Excel(name = "申请时间" ,width = 12 ,orderNum = "6")
	private String applyTime;

	/**
	 * 经办人名称
	 */
	@Excel(name = "经办人" ,width = 12 ,orderNum = "7")
	private String operatorName;

	/**
	 * 是否有效，1：正常，0：已失效
	 */
	@Excel(name = "状态" ,width = 12 ,orderNum = "8")
	private String status;

	/**
	 * 是否低保用户：1：是低保  0：不是低保
	 */
	@Excel(name = "是否低保" ,width = 12 ,orderNum = "9")
	private String livingAble;

	/**
	 * 残疾类别
	 */
	private String disabilityCategory;

	/**
	 * 残疾类别名称
	 */
	@Excel(name = "残疾类别" ,width = 12 ,orderNum = "10")
	private String disabilityCategoryName;

	/**
	 * 残疾等级：1：一级 2：“二级” 3：“三级” 4：“四级'
	 */
	@Excel(name = "残疾等级" ,width = 12 ,orderNum = "11")
	private String disabilityLevel;

	/**
	 * 残疾证号
	 */
	@Excel(name = "残疾证号" ,width = 12 ,orderNum = "12")
	private String disableCardNum;

	/**
	 * 签发日期
	 */
	@Excel(name = "残疾证发放时间" ,width = 12 ,orderNum = "13")
	private String issuanceDate;

	/**
	 * 家属联系方式
	 */
	@Excel(name = "家属联系方式" ,width = 12 ,orderNum = "14")
	private String familyMobile;

	/**
	 * 家庭住址
	 */
	@Excel(name = "家庭住址" ,width = 12 ,orderNum = "15")
	private String familyAddress;

	/**
	 * 申请的主题
	 */
    private String title;
	/**
	 * 居民的id
	 */
    private Long residentId;

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;
	/**
	 * 创建人id
	 */
    private Long createUserId;
	/**
	 * 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
	 */
    private Integer dataType;

	/**
	 * 关联的申请的id
	 */
    private Long applyId;
	/**
	 * 更新时间
	 */
    private Date updateTime;
	/**
	 * 更新人id
	 */
    private Long updateUserId;

	/**
	 * 注销备注
	 */
    private String cancellationRemarks;
	/**
	 * 附件id
	 */
    private String attachedIds;

	/**
	 * 分类名称
	 */
	private String categoryName;
	/**
	 * 服务名称
	 */
	private String serviceName;

	/**
	 * 判断服务名单编辑的时候扩展信息有没有更改 0- 未更改 1-已更改
	 */
	private Integer flag;
	/**
	 * 低保证日期
	 */
	private Date livingIssuanceDate;
}