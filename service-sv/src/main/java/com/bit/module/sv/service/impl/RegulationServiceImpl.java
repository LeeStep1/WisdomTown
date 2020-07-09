package com.bit.module.sv.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Regulation;
import com.bit.module.sv.bean.RegulationContent;
import com.bit.module.sv.dao.LawDao;
import com.bit.module.sv.dao.RegulationContentDao;
import com.bit.module.sv.dao.RegulationDao;
import com.bit.module.sv.service.RegulationService;
import com.bit.module.sv.vo.RegulationContentVO;
import com.bit.module.sv.vo.RegulationPreviewVO;
import com.bit.module.sv.vo.RegulationVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Regulation的Service实现类
 * @author codeGenerator
 *
 */
@Service("regulationService")
@Transactional
public class RegulationServiceImpl extends BaseService implements RegulationService {

	private static final Logger logger = LoggerFactory.getLogger(RegulationServiceImpl.class);

	@Autowired
	private RegulationDao regulationDao;

	@Autowired
	private RegulationContentDao regulationContentDao;

	@Autowired
	private LawDao lawDao;

	/**
	 * 根据条件查询Regulation
	 * @param regulation
	 * @return
	 */
	@Override
	public BaseVo findByCondition(RegulationVO regulation){
		List<Regulation> list = regulationDao.findByCondition(regulation);
		LinkedHashSet<Regulation> result = list.stream().sorted(Comparator.comparingLong(Regulation::getId))
				.collect(Collectors.toCollection(LinkedHashSet::new));
		return new BaseVo(result);
	}

	@Override
	public BaseVo contentListPage(RegulationContentVO content) {
		PageHelper.startPage(content.getPageNum(), content.getPageSize());
		List<RegulationContent> laws = regulationContentDao.findByConditionPage(content);
		PageInfo<RegulationContent> pageInfo = new PageInfo<>(laws);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 通过主键查询单个Regulation
	 * @param id
	 * @return
	 */
	@Override
	public Regulation findById(Long id){
		return regulationDao.findById(id);
	}

	/**
	 * 批量保存Regulation
	 * @param regulations
	 */
	@Override
	public void batchAdd(List<Regulation> regulations){
		regulationDao.batchAdd(regulations);
	}
	/**
	 * 保存Regulation
	 * @param regulation
	 */
	@Override
	public void add(Regulation regulation){
		if (null != regulation.getParentId()) {
			Regulation checkParent = regulationDao.findById(regulation.getParentId());
			if (checkParent == null) {
				logger.error("父级法规不存在或已注销, {}", regulation);
				throw new BusinessException("父级法规不存在");
			}
			if (null != checkParent.getParentId()) {
				logger.error("父级法规不是一级目录, {}", regulation);
				throw new BusinessException("父级法规不是一级目录，无法新增二级目录");
			}
		}
		Date applyTime = new Date();
		regulation.setCreateAt(applyTime);
		regulation.setUpdateAt(applyTime);
		regulationDao.insert(regulation);
		logger.info("新增法规 : {}", regulation);
	}

	/**
	 * 更新Regulation
	 * @param regulation
	 */
	@Override
	public void update(Regulation regulation){
		Regulation toUpdate = regulationDao.findById(regulation.getId());
		if (toUpdate == null) {
			logger.error("法规不存在或已注销, {}", regulation);
			throw new BusinessException("法规不存在");
		}
		toUpdate.update(regulation);
		regulationDao.updateByPrimaryKeySelective(toUpdate);
		lawDao.updateLawUpdateAtById(toUpdate.getLawId());
		logger.info("更新法规 : {}", toUpdate);
	}

	/**
	 * 删除Regulation
	 * @param id
	 */
	@Override
	public void delete(Long id) {
		Regulation toDelete = regulationDao.findById(id);
		if (toDelete == null) {
			logger.error("法规不存在, {}", id);
			throw new BusinessException("法规不存在");
		}
		logger.info("删除法规 : {}", toDelete);
		Set<Long> toDeleteRegulationsIds = new HashSet<>();
		toDeleteRegulationsIds.add(toDelete.getId());
		// 如果是第一级法规，需要遍历删除子节点
		if (toDelete.getParentId() == null) {
			List<Long> childEle = regulationDao.findByParentId(toDelete.getId());
			toDeleteRegulationsIds.addAll(childEle);
		}
		regulationContentDao.deleteByRegulationIdIn(toDeleteRegulationsIds);
		regulationDao.deleteByIdIn(toDeleteRegulationsIds);
		lawDao.updateLawUpdateAtById(toDelete.getLawId());
	}

	@Override
	public void addContent(RegulationContent content) {
		Regulation toCheck = regulationDao.findById(content.getRegulationId());
		if (toCheck == null) {
			logger.error("法规不存在, {}", content);
			throw new BusinessException("法规不存在");
		}
		Date applyTime = new Date();
		content.setCreateAt(applyTime);
		content.setUpdateAt(applyTime);
		regulationContentDao.insert(content);
		logger.info("新增法规正文 : {}", content);
	}

	@Override
	public BaseVo<List<RegulationPreviewVO>> findAll(Long id) {
		List<RegulationPreviewVO> previewRegulations = regulationDao.findAllContentByLawId(id);
		return new BaseVo<>(previewRegulations);
	}

	@Override
	public BaseVo findContentByRegulationId(Long regulationId) {
		List<RegulationContent> contents = regulationContentDao.findByRegulationId(regulationId);
		return new BaseVo(contents);
	}

    @Override
    public void updateContent(RegulationContent content) {

		RegulationContent toUpdate = regulationContentDao.selectByPrimaryKey(content.getId());
		toUpdate.update(content);
		Regulation regulation = regulationDao.findById(toUpdate.getRegulationId());
		regulationContentDao.updateByPrimaryKeySelective(toUpdate);
		lawDao.updateLawUpdateAtById(regulation.getLawId());
		regulationDao.updateRegulationUpdateAtById(toUpdate.getRegulationId());
    }

	@Override
	public void deleteContent(Long id) {
		RegulationContent toDel = regulationContentDao.selectByPrimaryKey(id);
		regulationContentDao.deleteByPrimaryKey(id);

		Regulation regulation = regulationDao.findById(toDel.getRegulationId());
		lawDao.updateLawUpdateAtById(regulation.getLawId());
		regulationDao.updateRegulationUpdateAtById(regulation.getId());
	}

    @Override
    public List<RegulationPreviewVO> getRegulationByContentIds(Collection<Long> contentIds) {
		return regulationDao.getRegulationByContentIds(contentIds);
    }
}
