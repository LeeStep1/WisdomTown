package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.PhoneBook;
import com.bit.module.cbo.vo.AppOrgPmcTelInfo;
import com.bit.module.cbo.vo.PhoneBookCommunityCompanyVO;
import com.bit.module.cbo.vo.PhoneBookVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PhoneBookDao {
	/**
	 * 根据当前用户查询社区电话
	 * @param phoneBookVO
	 * @return
	 */
	List<PhoneBook> getOrgTelInfoByToken(PhoneBookVO phoneBookVO);

	/**
	 * web端分页查询
	 * @param phoneBookVO
	 * @return
	 */
	List<PhoneBookCommunityCompanyVO> webListPage(PhoneBookVO phoneBookVO);

	/**
	 * 新增数据
	 * @param phoneBook
	 * @return
	 */
	int add(PhoneBook phoneBook);

	/**
	 * 修改数据
	 * @param phoneBook
	 * @return
	 */
	int modify(PhoneBook phoneBook);

	/**
	 * 根据小区id集合 查询电话
	 * @return
	 */
	List<PhoneBookCommunityCompanyVO> getTelInfoByCommunityIds(AppOrgPmcTelInfo appOrgPmcTelInfo);


	/**
	 * 根据社区id集合 查询电话
	 * @return
	 */
	List<PhoneBookCommunityCompanyVO> getTelInfoByOrgIds(AppOrgPmcTelInfo appOrgPmcTelInfo);

	/**
	 * 社区办根据社区id集合 查询电话
	 * @return
	 */
	List<PhoneBookCommunityCompanyVO> getTelsByOrgIds(PhoneBookVO phoneBookVO);
}
