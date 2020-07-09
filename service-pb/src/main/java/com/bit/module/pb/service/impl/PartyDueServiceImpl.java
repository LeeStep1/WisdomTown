package com.bit.module.pb.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.bean.PartyDue;
import com.bit.module.pb.dao.OrganizationDao;
import com.bit.module.pb.dao.PartyDueDao;
import com.bit.module.pb.service.PartyDueService;
import com.bit.module.pb.vo.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PartyDue的Service实现类
 * @author codeGenerator
 *
 */
@Service("partyDueService")
@Transactional
public class PartyDueServiceImpl extends BaseService implements PartyDueService{

	private static final Logger logger = LoggerFactory.getLogger(PartyDueServiceImpl.class);

	private static final Integer DATA_SIZE = 500;

	@Autowired
	private PartyDueDao partyDueDao;

	@Autowired
    private OrganizationDao organizationDao;

	// 收费日期
	@Value("${party.due.charge.time}")
    private Integer chargeAt;
	/**
	 * 分页查询PartyDue列表 党员个人交纳情况表
	 */
    @Override
    public BaseVo findByOrgConditionPage(PartyDueVO partyDueVO) {
		partyDueVO.setOrgId(StringUtils.isEmpty(partyDueVO.getOrgId())
				? getCurrentUserInfo().getPbOrgId() : partyDueVO.getOrgId());
		PageHelper.startPage(partyDueVO.getPageNum(), partyDueVO.getPageSize());
		List<PartyDue> list = partyDueDao.findByOrgConditionPage(partyDueVO);
		PageInfo<PartyDue> pageInfo = new PageInfo<>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
    }

	/**
	 * 批量保存PartyDue
	 * @param partyDues
	 */
	@Override
	public void batchAdd(List<PartyDue> partyDues){
		partyDueDao.batchAdd(partyDues);
	}
	/**
	 * 保存PartyDue
	 * @param partyDue
	 */
	@Override
	public void add(PartyDue partyDue) {
		if (null == partyDue.getBase() || null == partyDue.getAmount()) {
			logger.warn("核准基数和应交金额不能为空", partyDue);
			throw new BusinessException("核准基数和应交金额不能为空");
		}
        LocalDate now = LocalDate.now();
        partyDue.setMonth(now.getMonthValue());
        partyDue.setYear(now.getYear());
        List<PartyDue> existPartyDue = partyDueDao.findByCondition(partyDue);
        // 有可能之前用过excel文件导入过数据，不需要重复导入
        if (CollectionUtils.isNotEmpty(existPartyDue)) {
            return;
        }
        partyDueDao.add(partyDue);
	}
	/**
	 * 批量更新PartyDue
	 * @param partyDues
	 */
	@Override
	public void batchUpdate(List<PartyDue> partyDues) {
        LocalDate now = LocalDate.now();
		int size = partyDues.size() / DATA_SIZE + 1;

		for (int i = 0; i < size; i++) {
			List<PartyDue> partOfUpdate = partyDues.stream().limit(DATA_SIZE).collect(Collectors.toList());
			partyDueDao.batchUpsertForUpload(partOfUpdate, now.getYear(), now.getMonthValue());
			List<PartyDue> hasUpdate = partyDues.subList(0, partyDues.size() >= DATA_SIZE ? DATA_SIZE : partyDues.size());
			hasUpdate.clear();
		}
	}
	/**
	 * 更新PartyDue
	 * @param partyDue
	 */
	@Override
	public BaseVo updateAmountById(PartyDue partyDue) {
        PartyDue toUpdate = partyDueDao.findById(partyDue.getId());
		if (toUpdate == null) {
			logger.warn("没有找到id为{}的党费记录", partyDue.getId());
			throw new BusinessException("没有找到该党费记录");
		}

		checkOverTime(toUpdate);

		toUpdate.setBase(partyDue.getBase() == null ? toUpdate.getBase() : partyDue.getBase());
        toUpdate.setAmount(partyDue.getAmount() == null ? toUpdate.getAmount() : partyDue.getAmount());
        toUpdate.setPaidAmount(partyDue.getPaidAmount());
        toUpdate.setRemark(StringUtils.isEmpty(partyDue.getRemark()) ? "" : partyDue.getRemark());
        logger.info("需要修改的党费 : {}", toUpdate);
		partyDueDao.updateAmountById(toUpdate);
		return new BaseVo();
	}

	/**
	 * 删除PartyDue
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		partyDueDao.batchDelete(ids);
	}
	/**
	 * 党员交纳党费明细
	 * @param partyDueVO
	 * @return
	 */
	@Override
	public BaseVo findPersonalMonthlyPartyDue(PartyDueVO partyDueVO) {
		PageHelper.startPage(partyDueVO.getPageNum(), partyDueVO.getPageSize());
		List<PersonalMonthlyPartyDueVO> list = partyDueDao.findPersonalMonthlyPartyDueByCondition(partyDueVO);
		PageInfo<PersonalMonthlyPartyDueVO> pageInfo = new PageInfo<>(list);
		BaseVo<PageInfo<PersonalMonthlyPartyDueVO>> baseVo = new BaseVo<>();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	@Override
	public void movePartyDueThisMonth(Long partyMemberId, @Nullable String toOrganization) {
        LocalDate now = LocalDate.now();
        // 如果已经超过规定的缴费日期，不需要进行任何操作
        if (now.getDayOfMonth() >= chargeAt) {
            return;
        }
		PersonalPartyDueVO partyDue = findPartyDueThisMonth(partyMemberId);
        // 已交党费不需要任何修改
        if (partyDue != null && partyDue.getPaidAmount() != null && partyDue.getPaidAmount() > 0) {
            return;
        }
        if (toOrganization == null) {
            // 转移到镇外的党费信息
			if (partyDue != null) {
				partyDueDao.delete(partyDue.getId());
			}
            return;
        }
        // 镇外转镇内则新建一条记录
		PartyDueVO toUpsertPartyDue = new PartyDueVO();
        if (partyDue == null) {
			toUpsertPartyDue.setYear(now.getYear());
			toUpsertPartyDue.setMonth(now.getMonthValue());
			toUpsertPartyDue.setMemberId(Integer.valueOf(partyMemberId.toString()));
			toUpsertPartyDue.setInsertTime(new Date());
			toUpsertPartyDue.setBase(0);
			toUpsertPartyDue.setAmount(0);
		} else {
			BeanUtils.copyProperties(partyDue, toUpsertPartyDue);
		}
        Organization toUpdatePartyDue = organizationDao.findById(toOrganization);
		toUpsertPartyDue.setOrgId(toOrganization);
		toUpsertPartyDue.setOrgName(toUpdatePartyDue.getName());
        partyDueDao.upsertPartyDueToOtherOrganization(toUpsertPartyDue);
	}

    private PersonalPartyDueVO findPartyDueThisMonth(Long partyMemberId) {
        LocalDate now = LocalDate.now();
        return partyDueDao.findByPartyMemberIdAndMonth(String.valueOf(partyMemberId), now.getMonthValue());
    }

    @Override
    public void stopMemberPartyDueThisMonth(Long partyMemberId) {
		PersonalPartyDueVO partyDue = findPartyDueThisMonth(partyMemberId);
        // 已交党费不需要任何修改
        if (partyDue == null || partyDue.getPaidAmount() == null || partyDue.getPaidAmount() > 0) {
            return;
        }
        // 若未交党费则删除当月党费信息
        partyDueDao.delete(partyDue.getId());
    }
	/**
	 * 每月定时生成党员党费数据
	 */
    @Override
    public void createPartyDueEveryMonth() {
        LocalDate now = LocalDate.now();
        // 如果是一月拿上一年的数据，否则拿上月的数据
        Integer year = now.getMonthValue() == 1 ? now.getYear() - 1 : now.getYear();
        Integer month = now.getMonthValue() == 1 ? 12 : now.getMonthValue() - 1;
		List<PartyDue> partyDueList = partyDueDao.findByMemberLastMonthPartyDue(year, month);

		Date insertAt = new Date();
		partyDueList.forEach(partyDue -> {
			partyDue.setBase(partyDue.getBase() == null ? 0 : partyDue.getBase());
			partyDue.setAmount(partyDue.getAmount() == null ? 0 : partyDue.getAmount());
            partyDue.setYear(now.getYear());
            partyDue.setMonth(now.getMonthValue());
            partyDue.setInsertTime(insertAt);
        });
		int size = partyDueList.size() / DATA_SIZE + 1;

		for (int i = 0; i < size; i++) {
			List<PartyDue> partOfUpdate = partyDueList.stream().limit(DATA_SIZE).collect(Collectors.toList());
			partyDueDao.batchUpsert(partOfUpdate);
			partyDueList.subList(0, partyDueList.size() >= DATA_SIZE ? DATA_SIZE : partyDueList.size()).clear();
		}
    }
	/**
	 * 导出党员个人党费
	 * @param year
	 * @param month
	 * @param orgId
	 * @return
	 */
	@Override
	public List<PersonalPartyDueExportVO> findExportPersonalPartyDue(Integer year, Integer month, String orgId) {
		return partyDueDao.findPersonalPartyDue(year, month, orgId);
	}
	/**
	 * 导出数据
	 * @param year
	 * @param orgId
	 * @return
	 */
	@Override
	public List<PersonalMonthlyPartyDueVO> exportPersonalMonthlyPartyDue(Integer year, String orgId) {
		orgId = StringUtils.isEmpty(orgId) ? getCurrentUserInfo().getPbOrgId() : orgId;
		return partyDueDao.findPersonalMonthlyPartyDue(year, orgId);
	}

	@Override
	public List<PartyDue> findPartyDueByCondition(PartyDue partyDue) {
		return partyDueDao.findByCondition(partyDue);
	}
	/**
	 * APP查询个人党费
	 * @param partyDueVO
	 * @return
	 */
	@Override
	public BaseVo findByMemberConditionPage(PartyDueVO partyDueVO) {
		UserInfo currentUserInfo = getCurrentUserInfo();
		partyDueVO.setIdCard(currentUserInfo.getIdcard());
		PageHelper.startPage(partyDueVO.getPageNum(), partyDueVO.getPageSize());
		List<PartyDue> list = partyDueDao.findByOrgConditionPage(partyDueVO);
		PageInfo<PartyDue> pageInfo = new PageInfo<>(list);
		return new BaseVo(pageInfo);
	}

    @Override
    public BaseVo countPartyDue(PartyDue partyDue) {
		List<PartyDueStatistics> result = partyDueDao.countPartyDueByOrgIdAndYearAndMonthIgnoreNull(partyDue);
		return new BaseVo(PartyDueStatistics.groupPartyDueByMonth(result));
    }
	/**
	 * 批量设置修改金额接口
	 * @param partyDueBatchVO
	 */
	@Override
	public void batchModifyAmount(PartyDueBatchVO partyDueBatchVO) {
		if (partyDueBatchVO == null) {
			return;
		}

		// 批量修改时
		List<PartyDue> partyDues = partyDueDao.findByIds(partyDueBatchVO.getIds());
		partyDues.forEach(this::checkOverTime);

		int size = partyDueBatchVO.getIds().size() / DATA_SIZE + 1;

		for (int i = 0; i < size; i++) {
			List<Integer> partOfUpdate = partyDueBatchVO.getIds().stream().limit(DATA_SIZE).collect(Collectors.toList());
			partyDueDao.batchUpdate(partOfUpdate, partyDueBatchVO.getIsPaid());
			List<Integer> hasUpdate = partyDueBatchVO.getIds().subList(0, partyDueBatchVO.getIds().size() >= DATA_SIZE
					? DATA_SIZE : partyDueBatchVO.getIds().size());
			hasUpdate.clear();
		}
	}

	/**
	 * 删除PartyDue
	 * @param id
	 */
	@Override
	public void delete(Long id){
		partyDueDao.delete(id);
	}

	private void checkOverTime(PartyDue toUpdate) {
		// 无法在指定时间之前修改
		LocalDate now = LocalDate.now();
		if (chargeAt < now.getDayOfMonth()
				|| !toUpdate.getMonth().equals(now.getMonthValue())
				|| !toUpdate.getYear().equals(now.getYear())) {
			logger.warn("无法在 {} 月 {} 日之后, 编辑当前记录", now.getMonthValue(), chargeAt);
			throw new BusinessException("无法修改已归档的党费数据");
		}
	}

}
