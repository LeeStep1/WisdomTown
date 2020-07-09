package com.bit.module.cbo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * @Description
 * @Author Liyang
 * @Date 2019/12/09 13:34
 **/
@Data
public class ResidentExcelVO extends BasePageVo{
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
	 * 真实姓名
	 */
	@Excel(name = "姓名",width = 12,orderNum = "2")
	private String realName;

	/**
	 * 性别1男 2女
	 */
	@Excel(name = "性别",width = 12,orderNum = "3")
	private String sex;

	/**
	 * 证件类型 1 身份证 2士官证 3护照 4港澳通行证
	 */
	@Excel(name = "证件类型",width = 12,orderNum = "4")
	private String cardTypeName;

	/**
	 * 证件号码
	 */
	@Excel(name = "证件号码",width = 12,orderNum = "5")
	private String cardNum;

	/**
	 * 手机号
	 */
	@Excel(name = "手机号",width = 12,orderNum = "6")
	private String mobile;

	/**
	 * 居民类型id 101-自管党员 102-报道党员 103-居家养老 104-困难户 105-特困户 106-边缘户 107-残疾 108-优抚对象 109-享受低保 110-救助对象 111-矫正人员 112-邢释解救人员 113-精神病人 114-志愿者 115-退役军人
	 */
	@Excel(name = "特殊人群类型",width = 12,orderNum = "7")
	private String extendType;

	/**
	 * 账号状态 用户状态 状态 0停用 1正常 2待完善(只有web后台在居民管理中编辑补充后变为正常，不然为待完善,与有无关联房屋无关)
	 */
	@Excel(name = "状态",width = 12,orderNum = "8")
	private String status;

	/**
	 * 国籍
	 */
	@Excel(name = "国籍",width = 12,orderNum = "9")
	private String nationalityName;

	/**
	 * 民族
	 */
	@Excel(name = "民族",width = 12,orderNum = "10")
	private String ethnicityName;

	/**
	 * 政治面貌
	 */
	@Excel(name = "政治面貌",width = 12,orderNum = "11")
	private String faithName;

	/**
	 * 婚姻状况:1未婚、2已婚、3离婚、4丧偶
	 */
	@Excel(name = "婚姻状况",width = 12,orderNum = "12")
	private String maritalStatusName;

	/**
	 * 户籍类别
	 */
	@Excel(name = "户籍类别",width = 12,orderNum = "13")
	private String residenceTypeName;

	/**
	 * 社区名称
	 */
	@Excel(name = "社区名称",width = 12,orderNum = "14")
	private String orgName;

	/**
	 * 小区名称
	 */
	@Excel(name = "小区名称",width = 12,orderNum = "15")
	private String communityName;

	/**
	 * 国籍
	 */
	private Integer nationality;

	/**
	 * 民族
	 */
	private Integer ethnicity;

	/**
	 * 政治面貌
	 */
	private Integer faith;

	/**
	 * 曾用名
	 */
	private String usedName;
	/**
	 * 居住地址中文
	 */
	private String address;

	/**
	 * 证件类型 1 身份证 2士官证 3护照 4港澳通行证
	 */
	private Integer cardType;

	/**
	 * 生日yyyy-MM-dd
	 */
	private String birthday;
	/**
	 * 身份 1,业主2，家属 3，租客
	 */
	private Integer identityType;
	/**
	 * 房屋结构信息，xx小区，xx楼栋，xx单元，xx层
	 */
	private String addressStructure;
	/**
	 * 社区id
	 */
	private Long orgId;
	/**
	 * 居民类型id 101-自管党员 102-报道党员 103-居家养老 104-困难户 105-特困户 106-边缘户 107-残疾 108-优抚对象 109-享受低保 110-救助对象 111-矫正人员 112-邢释解救人员 113-精神病人 114-志愿者 115-退役军人
	 */
	private Integer type;

	/**
	 * 小区id
	 */
	private Long communityId;

	/**
	 * 社区办标识 0-否 1-是
	 */
	private Integer flag;
}
