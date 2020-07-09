package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.MonthlyPartyDue;
import com.bit.module.pb.vo.MonthlyPartyDueVO;
import com.bit.module.pb.vo.OrganizationMonthlyPartyDueVO;

import java.time.LocalDate;
import java.util.List;
/**
 * MonthlyPartyDue的Service
 * @author codeGenerator
 */
public interface MonthlyPartyDueService {
	/**
	 * 根据条件查询MonthlyPartyDue
	 * @param monthlyPartyDueVO
	 * @return
	 */
	BaseVo findByConditionPage(MonthlyPartyDueVO monthlyPartyDueVO);
	/**
	 * 查询所有MonthlyPartyDue
	 * @param sorter 排序字符串
	 * @return
	 */
	List<MonthlyPartyDue> findAll(String sorter);
	/**
	 * 通过主键查询单个MonthlyPartyDue
	 * @param id
	 * @return
	 */
	MonthlyPartyDue findById(Long id);
	/**
	 * 保存MonthlyPartyDue
	 * @param monthlyPartyDue
	 */
	void add(MonthlyPartyDue monthlyPartyDue);
	/**
	 * 批量更新MonthlyPartyDue
	 * @param monthlyPartyDues
	 */
	void batchUpdate(List<MonthlyPartyDue> monthlyPartyDues);
	/**
	 * 更新MonthlyPartyDue
	 * @param monthlyPartyDue
	 */
	void update(MonthlyPartyDue monthlyPartyDue);
	/**
	 * 删除MonthlyPartyDue
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除MonthlyPartyDue
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 定时生成月度单位报表
     * @param createAt
     */
	void addMonthlyPartyDueForAllOrganization(LocalDate createAt);

	/**
	 * 获取导出党组织月度党费数据
	 * @param year
	 * @param orgId
	 * @return
	 */
    List<OrganizationMonthlyPartyDueVO> findOrganizationMonthlyPartyDue(Integer year, String orgId);

	/**
	 * 统计党费总计
	 * @param monthlyPartyDue
	 * @return
	 */
	BaseVo countPartyDue(MonthlyPartyDue monthlyPartyDue);
}
