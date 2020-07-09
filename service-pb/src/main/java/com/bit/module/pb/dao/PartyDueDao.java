package com.bit.module.pb.dao;

import com.bit.module.pb.bean.MonthlyPartyDue;
import com.bit.module.pb.bean.PartyDue;
import com.bit.module.pb.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PartyDue管理的Dao
 * @author
 *
 */
@Repository
public interface PartyDueDao {
	/**
	 * 批量保存PartyDue
	 * @param partyDues
	 */
	void batchAdd(List<PartyDue> partyDues);
	/**
	 * 保存PartyDue
	 * @param partyDue
	 */
	void add(PartyDue partyDue);
	/**
	 * 批量更新PartyDue
	 * @param partyDues
	 * @param year
	 * @param month
	 */
	void batchUpsertForUpload(@Param("partyDues") List<PartyDue> partyDues,
							  @Param("year") int year,
							  @Param("month") int month);

	/**
	 * 批量更新PartyDue
	 * @param ids
	 * @param isPaid
	 */
	void batchUpdate(@Param("ids") List<Integer> ids, @Param("isPaid") boolean isPaid);
	/**
	 * 更新PartyDue
	 * @param partyDue
	 */
	void updateAmountById(PartyDue partyDue);
	/**
	 * 删除PartyDue
	 * @param ids
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除PartyDue
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 根据组织条件查询PartyDue
	 * @param partyDueVO
	 * @return
	 */
	List<PartyDue> findByOrgConditionPage(PartyDueVO partyDueVO);

	/**
	 * 统计基层单位最底层子节点单位党员的月缴纳党费
	 */
	List<MonthlyPartyDue> groupOrganizationMonthlyPartyDue(@Param(value = "orgIds") List<String> id,
														   @Param(value = "year") Integer year,
														   @Param(value = "month") Integer month);

	/**
	 * 党员缴纳党费明细
	 * @param partyDueVO
	 * @return
	 */
	List<PersonalMonthlyPartyDueVO> findPersonalMonthlyPartyDueByCondition(PartyDueVO partyDueVO);

	List<PartyDue> findByOrgIdAndYear(@Param(value = "memberId") String memberId,
									  @Param("orgId") String orgId,
									  @Param(value = "year") Integer year);

	/**
	 * 查询党员月党费详情
	 * @param partyMemberId
	 * @param monthValue
	 * @return
	 */
	PersonalPartyDueVO findByPartyMemberIdAndMonth(@Param("memberId") String partyMemberId, @Param("month") int monthValue);

	/**
	 * 转移党员单位, 因为可能存在党费信息尚未生成的记录, 因此用upsert的方式更新记录
	 * @param partyDue
	 * @return
	 */
	void upsertPartyDueToOtherOrganization(PartyDueVO partyDue);

	/**
	 * 根据id查找党员党费
	 * @param id
	 * @return
	 */
	PartyDue findById(Long id);

	/**
	 * 查询上月党费收缴情况
	 * @param year
	 * @param month
	 * @return
	 */
	List<PartyDue> findByMemberLastMonthPartyDue(@Param("year") int year, @Param("month") int month);

	void batchUpsert(@Param("partyDueList") List<PartyDue> partyDueList);

    /**
     * 党员个人导出数据接口
     * @param year
     * @param month
     * @param orgId
     * @return
     */
	List<PersonalPartyDueExportVO> findPersonalPartyDue(@Param("year") Integer year,
														@Param("month") Integer month,
														@Param("orgId") String orgId);

	List<PersonalMonthlyPartyDueVO> findPersonalMonthlyPartyDue(@Param("year") Integer year,
																@Param("orgId") String orgId);

	List<PartyDue> findByCondition(PartyDue partyDue);

    List<PartyDueStatistics> countPartyDueByOrgIdAndYearAndMonthIgnoreNull(PartyDue partyDue);

	List<PartyDue> findByIds(@Param("ids") List<Integer> ids);
	/**
	 * 更新
	 * @param partyDue
	 */
	void update(PartyDue partyDue);
}
