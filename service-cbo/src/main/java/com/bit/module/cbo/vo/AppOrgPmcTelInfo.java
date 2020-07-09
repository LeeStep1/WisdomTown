package com.bit.module.cbo.vo;

import com.bit.module.cbo.bean.PmcStaff;
import lombok.Data;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/6 11:12
 **/
@Data
public class AppOrgPmcTelInfo {

	/**
	 * 居民app 社区电话
	 */
	private List<PhoneBookCommunityCompanyVO> appResidentOrgTelInfos;

	/**
	 * 居民app 小区物业电话
	 */
	private List<PhoneBookCommunityCompanyVO> appResidentCommunityPmcTelInfos;

	/**
	 * 社区app 小区物业电话
	 */
	private List<PhoneBookCommunityCompanyVO> appOrgWithPmcTelInfos;
	/**
	 * 社区app 社区电话
	 */
	private List<PhoneBookCommunityCompanyVO> appOrgWithOrgTelInfos;
	/**
	 * 社区管理员列表
	 */
	private List<User> adminUserList;


	/**
	 * 物业app 社区电话
	 */
	private List<PhoneBookCommunityCompanyVO> appPmcOrgTelInfos;
	/**
	 * 物业app 小区电话
	 */
	private List<PhoneBookCommunityCompanyVO> appPmcCommunityPmcTelInfos;
	/**
	 * 物业app 小区物业管理员
	 */
	private List<PmcStaff> appPmcStaffAdminList;
	/**
	 * 物业app 小区物业维修人员
	 */
	private List<PmcStaff> appPmcStaffRepaireList;



	/**
	 * 社区id集合 传参数用
	 */
	private List<Long> orgIds;

	/**
	 * 小区id集合 传参数用
	 */
	private List<Long> communityIds;
	/**
	 * 类型：1社区，2物业
	 */
	private Integer type;
}
