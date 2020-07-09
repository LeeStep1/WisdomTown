package com.bit.module.pb.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.MonthlyPartyDue;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.dao.MonthlyPartyDueDao;
import com.bit.module.pb.dao.OrganizationDao;
import com.bit.module.pb.dao.PartyDueDao;
import com.bit.module.pb.service.MonthlyPartyDueService;
import com.bit.module.pb.vo.MonthlyPartyDueVO;
import com.bit.module.pb.vo.OrganizationMonthlyPartyDueVO;
import com.bit.module.pb.vo.PartyDueStatistics;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MonthlyPartyDue的Service实现类
 * @author codeGenerator
 *
 */
@Service("monthlyPartyDueService")
@Transactional
public class MonthlyPartyDueServiceImpl extends BaseService implements MonthlyPartyDueService{

	private static final Logger logger = LoggerFactory.getLogger(MonthlyPartyDueServiceImpl.class);

	private static final Integer DATA_SIZE = 500;

	@Autowired
	private MonthlyPartyDueDao monthlyPartyDueDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
    private PartyDueDao partyDueDao;

	/**
	 * 根据条件查询MonthlyPartyDue
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(MonthlyPartyDueVO monthlyPartyDueVO){
		PageHelper.startPage(monthlyPartyDueVO.getPageNum(), monthlyPartyDueVO.getPageSize());
		monthlyPartyDueVO.setOrgId(StringUtils.isEmpty(monthlyPartyDueVO.getOrgId())
						? getCurrentUserInfo().getPbOrgId() : monthlyPartyDueVO.getOrgId());
        int lowerLevelOrgCount = organizationDao.findLowerLevelById(monthlyPartyDueVO.getOrgId(), false);
        List<OrganizationMonthlyPartyDueVO> list;
        if (lowerLevelOrgCount > 0) {
            list = monthlyPartyDueDao.findByConditionPage(monthlyPartyDueVO);
        } else {
            list = monthlyPartyDueDao.findPageById(monthlyPartyDueVO);
        }
		PageInfo<OrganizationMonthlyPartyDueVO> pageInfo = new PageInfo<>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有MonthlyPartyDue
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<MonthlyPartyDue> findAll(String sorter){
		return monthlyPartyDueDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个MonthlyPartyDue
	 * @param id
	 * @return
	 */
	@Override
	public MonthlyPartyDue findById(Long id){
		return monthlyPartyDueDao.findById(id);
	}

	/**
	 * 保存MonthlyPartyDue
	 * @param monthlyPartyDue
	 */
	@Override
	public void add(MonthlyPartyDue monthlyPartyDue){
		monthlyPartyDueDao.add(monthlyPartyDue);
	}
	/**
	 * 批量更新MonthlyPartyDue
	 * @param monthlyPartyDues
	 */
	@Override
	public void batchUpdate(List<MonthlyPartyDue> monthlyPartyDues){
		monthlyPartyDueDao.batchUpdate(monthlyPartyDues);
	}
	/**
	 * 更新MonthlyPartyDue
	 * @param monthlyPartyDue
	 */
	@Override
	public void update(MonthlyPartyDue monthlyPartyDue) {
		monthlyPartyDueDao.update(monthlyPartyDue);
	}
	/**
	 * 删除MonthlyPartyDue
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		monthlyPartyDueDao.batchDelete(ids);
	}
	/**
	 * 生成下级组织机构某年某月党费记录
	 * @param createDate
	 * @return
	 */
	@Override
	public void addMonthlyPartyDueForAllOrganization(LocalDate createDate) {
		// 获取所有的基层单位
        LinkedList<Organization> allGrassRootsUnits = new LinkedList<>(organizationDao.findAllGrassRootsUnits());
        while (allGrassRootsUnits.size() > 0) {
            Organization org = allGrassRootsUnits.pop();
            if (org != null) {
                addMonthlyPartyDueForSubordinates(org, createDate);
            }
        }
	}

	@Override
	public List<OrganizationMonthlyPartyDueVO> findOrganizationMonthlyPartyDue(Integer year, String orgId) {
		orgId = StringUtils.isEmpty(orgId) ? getCurrentUserInfo().getPbOrgId() : orgId;
		List<OrganizationMonthlyPartyDueVO> due = monthlyPartyDueDao.findSubOrganizationMonthlyPartyDue(year, orgId, null);
		if (CollectionUtils.isEmpty(due)) {
			due = monthlyPartyDueDao.findCurrentOrganizationMonthlyPartyDue(year, orgId, null);
		}
		return due;
	}

	@Override
	public BaseVo countPartyDue(MonthlyPartyDue monthlyPartyDue) {
		List<PartyDueStatistics> result = monthlyPartyDueDao.countPartyDueByOrgIdAndYearAndMonthIgnoreNull(monthlyPartyDue);
		return new BaseVo(PartyDueStatistics.groupPartyDueByMonth(result));
	}

	/**
	 * 删除MonthlyPartyDue
	 * @param id
	 */
	@Override
	public void delete(Long id){
		monthlyPartyDueDao.delete(id);
	}

	@SuppressWarnings("unchecked")
	private void addMonthlyPartyDueForSubordinates(Organization grassRoot, LocalDate createDate) {
	    try {
	        logger.info("生成党费的基层单位 : {}", grassRoot);
	        // 查找基层单位所有的子节点集合
            List<Organization> allSubOrganizationFromGrassRoot = organizationDao.findSubordinatesById(grassRoot.getId(), true);
            List<String> allSubOrganizationFromGrassRootIds = allSubOrganizationFromGrassRoot.stream().map(Organization::getId).collect(Collectors.toList());
            // 获取基层单位最底层子节点集合
            List<String> lowestChildNodeIdList = obtainLowestOrganization(allSubOrganizationFromGrassRootIds);
            logger.info("基层单位 : {} 的最底层节点 : {}", grassRoot.getName(), lowestChildNodeIdList);

            // 镇党委直属支部
            if (CollectionUtils.isEmpty(allSubOrganizationFromGrassRootIds) && CollectionUtils.isEmpty(lowestChildNodeIdList)) {
                logger.info("基层单位 {} 没有底层节点, 将统计本节点的党费", grassRoot.getName());
				List<MonthlyPartyDue> monthlyPartyDues = groupLowestOrganizationMonthlyPartyDue(Collections.singletonList(grassRoot.getId()), createDate);
				if (CollectionUtils.isEmpty(monthlyPartyDues)) {
					logger.info("基层单位 {} 缺少党费统计数据", grassRoot.getName());
					return;
				}
				Map<String, Integer> organizationAmountMap = obtainLowestChildOrganizationAmountMap(monthlyPartyDues);
				generateOrganizationAmount(organizationAmountMap, createDate);
				return;
            }

            // 统计基层单位最底层子节点的月党费
            List<MonthlyPartyDue> lowestChildNodeAmountList = groupLowestOrganizationMonthlyPartyDue(lowestChildNodeIdList,
					createDate);
			if (CollectionUtils.isEmpty(lowestChildNodeAmountList) || lowestChildNodeAmountList.get(0) == null) {
				logger.info("基层单位 {} 的下属党支部缺少党费统计数据", grassRoot.getName());
				return;
			}
			Map<String, Integer> organizationAmountMap = obtainLowestChildOrganizationAmountMap(lowestChildNodeAmountList);

            // 累加所有最底层子节点的党费作为基层单位的月党费金额
            organizationAmountMap.put(grassRoot.getId(), lowestChildNodeAmountList.stream().filter(childNode -> childNode.getAmount() != null).mapToInt(MonthlyPartyDue::getAmount).sum());
            // 找出所有的非最底层节点
            List<String> organizations = new ArrayList<>(
                    CollectionUtils.disjunction(allSubOrganizationFromGrassRootIds, lowestChildNodeIdList));
            // 统计非基层单位最底层子节点的党费金额
            countNotLowestOrganizationAmount(organizations, lowestChildNodeAmountList, organizationAmountMap);
            // 生成月度党费数据
            generateOrganizationAmount(organizationAmountMap, createDate);
        } catch (Exception e) {
	        logger.error("生成月党费异常 : {}", e);
	        throw new BusinessException("生成月党费异常");
        }

    }

	private List<MonthlyPartyDue> groupLowestOrganizationMonthlyPartyDue(List<String> lowestChildNodeIdList, LocalDate now) {
		return partyDueDao.groupOrganizationMonthlyPartyDue(lowestChildNodeIdList, now.getYear(), now.getMonthValue());
	}

	private void generateOrganizationAmount(Map<String, Integer> organizationAmountMap, LocalDate createDate) {
		List<MonthlyPartyDue> toInsert = organizationAmountMap.entrySet().stream().map(entry -> {
			MonthlyPartyDue monthlyPartyDue = new MonthlyPartyDue();
			monthlyPartyDue.setOrgId(entry.getKey());
			monthlyPartyDue.setYear(createDate.getYear());
			monthlyPartyDue.setMonth(createDate.getMonthValue());
			monthlyPartyDue.setAmount(entry.getValue());
			monthlyPartyDue.setInsertTime(new Date());
			return monthlyPartyDue;
		}).collect(Collectors.toList());

		int size = toInsert.size() / DATA_SIZE + 1;
		for (int i = 0; i < size; i++) {
			List<MonthlyPartyDue> partOfUpdate = toInsert.stream().limit(DATA_SIZE).collect(Collectors.toList());
			monthlyPartyDueDao.batchAdd(partOfUpdate);
			List<MonthlyPartyDue> hasInsert = toInsert.subList(0, toInsert.size() >= DATA_SIZE ? DATA_SIZE : toInsert.size());
			hasInsert.clear();
		}
    }

    private Map<String, Integer> obtainLowestChildOrganizationAmountMap(List<MonthlyPartyDue> lowestChildNodeAmountList) {
        return lowestChildNodeAmountList.stream().filter(childNode -> childNode.getAmount() != null)
				.collect(Collectors.toMap(MonthlyPartyDue::getOrgId, MonthlyPartyDue::getAmount));
    }

    private void countNotLowestOrganizationAmount(List<String> organizations, List<MonthlyPartyDue> lowestChildNodeAmountList,
                                                  Map<String, Integer> organizationAmountMap) {
        for (String organizationId : organizations) {
			Long id = Long.valueOf(organizationId);
            // 获取该单位的层级
            int level = countOrganizationLevelById(organizationId);
            Long idAfterRightShift = id >> 8 * (8 - level);
            organizationAmountMap.put(organizationId, lowestChildNodeAmountList.stream()
                    // 过滤不符合子节点的数据
                    .filter(node -> idAfterRightShift.equals(Long.valueOf(node.getOrgId()) >> 8 * (8 - level))
							&& node.getAmount() != null)
                    .mapToInt(MonthlyPartyDue::getAmount).sum());
        }
    }

    /**
     * 获取最底层子节点集合
     * @param subordinatesIds
     * @return
     */
    private List<String> obtainLowestOrganization(List<String> subordinatesIds) {
        return subordinatesIds.stream().filter(id -> !existSubOrgById(id, subordinatesIds)).collect(Collectors.toList());
    }

    /**
     * 查询是否存在子节点
     * @param id
     * @param subordinatesIds
     * @return
     */
    private boolean existSubOrgById(String id, List<String> subordinatesIds) {
        int level = countOrganizationLevelById(id);
        // 到了第8层就是最底层
        if (level == 8) {
            return false;
        }
        // 遍历查找是否存在子节点
		Long countId = Long.valueOf(id);
		for (String subordinatesId : subordinatesIds) {
			Long idAfterRightShift = countId >> 8 * (8 - level);
            if (!subordinatesId.equals(id) && idAfterRightShift.equals(Long.valueOf(subordinatesId) >> 8 * (8 - level))) {
                return true;
            }
        }
        return false;
    }

    private int countOrganizationLevelById(String id) {
		Long count = Long.valueOf(id);
        int level = 2;
        while (count << 8 * level != 0) {
            level++;
}
        return level;
    }
}
