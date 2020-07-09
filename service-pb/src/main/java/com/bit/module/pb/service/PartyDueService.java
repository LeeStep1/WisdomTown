package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.PartyDue;
import com.bit.module.pb.vo.PartyDueBatchVO;
import com.bit.module.pb.vo.PartyDueVO;
import com.bit.module.pb.vo.PersonalMonthlyPartyDueVO;
import com.bit.module.pb.vo.PersonalPartyDueExportVO;

import java.util.List;
/**
 * PartyDue的Service
 * @author codeGenerator
 */
public interface PartyDueService {
	/**
	 * 根据组织条件查询PartyDue
	 * @param partyDueVO
	 * @return
	 */
	BaseVo findByOrgConditionPage(PartyDueVO partyDueVO);

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
	 */
	void batchUpdate(List<PartyDue> partyDues);
	/**
	 * 更新PartyDue
	 * @param partyDue
	 */
	BaseVo updateAmountById(PartyDue partyDue);
	/**
	 * 删除PartyDue
	 * @param id
	 */
	void delete(Long id);
	/**
	 * 批量删除PartyDue
	 * @param ids
	 */
	void batchDelete(List<Long> ids);

	/**
	 * 查询党员月度党费数据
	 * @param partyDueVO
	 * @return
	 */
	BaseVo findPersonalMonthlyPartyDue(PartyDueVO partyDueVO);

	/**
	 * 党员党费转移
	 * @param partyMemberId 需要转移的党员
	 * @param toOrganization 目标组织, 若转移到镇外, 则为空
	 * @return
	 */
	void movePartyDueThisMonth(Long partyMemberId, String toOrganization);

	/**
	 * 停用党员
	 * @param partyMemberId
	 */
	void stopMemberPartyDueThisMonth(Long partyMemberId);

	/**
	 * 每月定时生成党员党费数据
	 */
	void createPartyDueEveryMonth();

    /**
     * 导出党员个人党费
     * @param year
     * @param month
     * @param orgId
     * @return
     */
	List<PersonalPartyDueExportVO> findExportPersonalPartyDue(Integer year, Integer month, String orgId);

	/**
	 * 导出数据
	 * @param year
	 * @param orgId
	 * @return
	 */
	List<PersonalMonthlyPartyDueVO> exportPersonalMonthlyPartyDue(Integer year, String orgId);

	/**
	 * 条件搜索
	 * @param partyDue
	 * @return
	 */
	List<PartyDue> findPartyDueByCondition(PartyDue partyDue);

	/**
	 * APP查询个人党费
	 * @param partyDueVO
	 * @return
	 */
    BaseVo findByMemberConditionPage(PartyDueVO partyDueVO);

	/**
	 * 统计当月党费
	 * @param partyDue
	 * @return
	 */
	BaseVo countPartyDue(PartyDue partyDue);

	/**
	 * 批量设置修改金额接口
	 * @param partyDueBatchVO
	 */
	void batchModifyAmount(PartyDueBatchVO partyDueBatchVO);
}
