package com.bit.module.pb.dao;

import com.bit.module.pb.bean.MonthlyPartyDue;
import com.bit.module.pb.vo.MonthlyPartyDueVO;
import com.bit.module.pb.vo.OrganizationMonthlyPartyDueVO;
import com.bit.module.pb.vo.PartyDueStatistics;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * MonthlyPartyDue管理的Dao
 *
 * @author
 */
@Repository
public interface MonthlyPartyDueDao {
	/**
	 * 根据条件查询MonthlyPartyDue
	 *
	 * @param monthlyPartyDueVO
	 * @return
	 */
	List<OrganizationMonthlyPartyDueVO> findByConditionPage(MonthlyPartyDueVO monthlyPartyDueVO);

	/**
	 * @param monthlyPartyDueVO
	 * @return
	 */
	List<OrganizationMonthlyPartyDueVO> findPageById(MonthlyPartyDueVO monthlyPartyDueVO);

	List<MonthlyPartyDue> findByOrgId(MonthlyPartyDueVO monthlyPartyDueVO);

	/**
	 * 查询所有MonthlyPartyDue
	 *
	 * @return
	 */
	List<MonthlyPartyDue> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个MonthlyPartyDue
	 *
	 * @param id
	 * @return
	 */
	MonthlyPartyDue findById(@Param(value = "id") Long id);

	/**
	 * 批量保存MonthlyPartyDue
	 *
	 * @param monthlyPartyDues
	 */
	void batchAdd(@Param(value = "monthlyPartyDues") List<MonthlyPartyDue> monthlyPartyDues);

	/**
	 * 保存MonthlyPartyDue
	 *
	 * @param monthlyPartyDue
	 */
	void add(MonthlyPartyDue monthlyPartyDue);

	/**
	 * 批量更新MonthlyPartyDue
	 *
	 * @param monthlyPartyDues
	 */
	void batchUpdate(List<MonthlyPartyDue> monthlyPartyDues);

	/**
	 * 更新MonthlyPartyDue
	 *
	 * @param monthlyPartyDue
	 */
	void update(MonthlyPartyDue monthlyPartyDue);

	/**
	 * 删除MonthlyPartyDue
	 *
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 删除MonthlyPartyDue
	 *
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

    List<OrganizationMonthlyPartyDueVO> findSubOrganizationMonthlyPartyDue(@Param("year") Integer year,
																		   @Param("orgId") String orgId,
																		   @Param("month") Integer month);

	List<PartyDueStatistics> countPartyDueByOrgIdAndYearAndMonthIgnoreNull(MonthlyPartyDue monthlyPartyDue);

	/**
	 * 获取当前党组织党费
	 * @param year
	 * @param orgId
	 * @param month
	 * @return
	 */
	List<OrganizationMonthlyPartyDueVO> findCurrentOrganizationMonthlyPartyDue(@Param("year") Integer year,
																			   @Param("orgId") String orgId,
																			   @Param("month") Integer month);
}
