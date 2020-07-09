package com.bit.module.sv.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Unit;
import com.bit.module.sv.dao.UnitDao;
import com.bit.module.sv.dao.UnitIndustryDao;
import com.bit.module.sv.service.UnitService;
import com.bit.module.sv.vo.UnitVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Unit的Service实现类
 * @author codeGenerator
 *
 */
@Service("unitService")
@Transactional
public class UnitServiceImpl extends BaseService implements UnitService {

	private static final Logger logger = LoggerFactory.getLogger(UnitServiceImpl.class);

	@Autowired
	private UnitDao unitDao;

	@Autowired
	private UnitIndustryDao unitIndustryDao;

	/**
	 * 根据条件查询Unit
	 * @param unitVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(UnitVO unitVO){
		PageHelper.startPage(unitVO.getPageNum(), unitVO.getPageSize());
		unitVO.setOrderBy("id");
		unitVO.setOrder("desc");
		List<Unit> list = unitDao.findByConditionPage(unitVO);
		PageInfo<Unit> pageInfo = new PageInfo<>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 通过主键查询单个Unit
	 * @param id
	 * @return
	 */
	@Override
	public Unit findById(Long id){
		return unitDao.findById(id);
	}

	/**
	 * 批量保存Unit
	 * @param units
	 */
	@Override
	public void batchAdd(List<Unit> units){
		unitDao.batchAdd(units);
	}
	/**
	 * 保存Unit
	 * @param unit
	 */
	@Override
	public void add(Unit unit){
		Date applyTime = new Date();
		unit.setCreateAt(applyTime);
		unit.setUpdateAt(applyTime);
		unit.setInjuriaCount(0);
		unit.setStatus(UnitStatusEnum.ENABLED.getKey());
		unitDao.insert(unit);
		if (CollectionUtils.isEmpty(unit.getIndustryCode())) {
			logger.info("单位({})没有所属行业，无需插入单位所属行业数据.", unit.getId());
			return;
		}
		unitIndustryDao.insert(unit.getId(), unit.getIndustryCode());
	}

	/**
	 * 更新Unit
	 * @param unit
	 */
	@Override
	public void update(Unit unit){
		Unit toUpdate = unitDao.findById(unit.getId());
		if (toUpdate == null || UnitStatusEnum.DISABLE.getKey().equals(toUpdate.getStatus())) {
			throw new BusinessException("单位不存在或已注销");
		}
		unit.setUpdateAt(new Date());
		unitDao.updateByPrimaryKeySelective(unit);
		if (unit.getIndustryCode() == null || unit.getIndustryCode().equals(toUpdate.getIndustryCode())) {
			return;
		}
		unitIndustryDao.deleteByUnitId(toUpdate.getId());
		unitIndustryDao.insert(toUpdate.getId(), unit.getIndustryCode());
	}

	/**
	 * 删除Unit
	 * @param id
	 */
	@Override
	public void delete(Long id){
		unitIndustryDao.deleteByUnitId(id);
		unitDao.deleteByPrimaryKey(id);
	}

	@Override
	public void disableStatus(Long id) {
		Unit unit = new Unit();
		unit.setId(id);
		unit.setStatus(UnitStatusEnum.DISABLE.getKey());
		unitDao.updateByPrimaryKeySelective(unit);
	}

	@Override
	public BaseVo findByType(Unit unitVO) {
		List<Unit> units = unitDao.findByType(unitVO);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(units);
		return baseVo;
	}

    @Override
    public BaseVo findByIds(List<Long> ids) {
		if (CollectionUtils.isEmpty(ids)) {
			return new BaseVo(Collections.EMPTY_LIST);
		}

		return new BaseVo(unitDao.findByIds(ids));
    }

    public enum UnitStatusEnum {
		DISABLE(0, "注销"), ENABLED(1, "启用");

		private Integer key;
		private String description;

		UnitStatusEnum(Integer key, String description) {
			this.key = key;
			this.description = description;
		}

		public String getDescription() {
			return description;
		}

		public Integer getKey() {
			return key;
		}

		public void setDescription(String description) {
			this.description = description;
		}
	}
}
