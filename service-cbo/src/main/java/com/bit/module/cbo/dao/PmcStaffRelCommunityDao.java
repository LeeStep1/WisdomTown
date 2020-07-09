package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.PmcStaff;
import com.bit.module.cbo.bean.PmcStaffRelCommunity;
import com.bit.module.cbo.vo.PmcStaffRelCommunityVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmcStaffRelCommunityDao {
	/**
	 * 多参数查询
	 * @param pmcStaffRelCommunity
	 * @return
	 */
	List<PmcStaffRelCommunity> findByParam(PmcStaffRelCommunity pmcStaffRelCommunity);

	/**
	 * 新增物业员工和小区绑定
	 */
	void add(@Param("list") List<PmcStaffRelCommunity> pmcStaffRelCommunityList);

	/**
	 * 根据物业人员ID删除原有小区关系
	 * @param pmcStaffId 物业员工ID
	 */
	void deleteByPmcStaffId(@Param("pmcStaffId") Long pmcStaffId);

	/**
	 * 根据物业员工ID批量查询所属小区
	 * @param staffIds
	 * @return
	 */
	List<PmcStaffRelCommunityVO> findByStaffIds(@Param("staffIds") List<Long> staffIds);

	/**
	 * 检测根据小区判断是否关联物业公司人员
	 * @param communityId 小区ID
	 * @return
	 */
	Integer staffByCommunityCount(@Param("communityId") Long communityId);

	/**
	 * 根据小区id 批量查询物业员工信息
	 * @param communityIds
	 * @return
	 */
	List<PmcStaff> getStaffInfosByCommunityIds(@Param(value = "communityIds") List<Long> communityIds,@Param(value = "role") Integer role,@Param(value = "staffId")Long staffId);

	/**
	 * 根据id批量删除
	 * @param ids
	 * @return
	 */
	int delByIds(@Param(value = "ids") List<Long> ids);
}
