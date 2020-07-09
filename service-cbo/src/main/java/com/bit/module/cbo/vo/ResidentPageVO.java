package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/18 19:34
 **/
@Data
public class ResidentPageVO extends BasePageVo{
	/**
	 * id
	 */
	private Long id;
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 曾用名
	 */
	private String usedName;
	/**
	 * 居住地址中文
	 */
	private String address;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 证件类型 1 身份证 2士官证 3护照 4港澳通行证
	 */
	private Integer cardType;
	/**
	 * 证件号码
	 */
	private String cardNum;
	/**
	 * 性别1男 2女
	 */
	private Integer sex;
	/**
	 * 生日yyyy-MM-dd
	 */
	private String birthday;
	/**
	 * 账号状态 用户状态 状态 0停用 1正常 2待完善(只有web后台在居民管理中编辑补充后变为正常，不然为待完善,与有无关联房屋无关)
	 */
	private Integer status;
	/**
	 * 身份 1,业主2，家属 3，租客
	 */
	private Integer identityType;
	/**
	 * 居民类型id 101-自管党员 102-报道党员 103-居家养老 104-困难户 105-特困户 106-边缘户 107-残疾 108-优抚对象 109-享受低保 110-救助对象 111-矫正人员 112-邢释解救人员 113-精神病人 114-志愿者 115-退役军人
	 */
	private String extendType;
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
	 * 社区名称
	 */
	private String orgName;
	/**
	 * 小区名称
	 */
	private String communityName;
	/**
	 * 社区办标识 0-否 1-是
	 */
	private Integer flag;
	/**
	 * 小区id集合
	 */
	private List<Long> communityIds;
}
