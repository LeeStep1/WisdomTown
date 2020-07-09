package com.bit.module.oa.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Driver;
import com.bit.module.oa.dao.DriverDao;
import com.bit.module.oa.enums.DriverStatusEnum;
import com.bit.module.oa.service.DriverService;
import com.bit.module.oa.vo.driver.DriverExportVO;
import com.bit.module.oa.vo.driver.DriverVO;
import com.bit.module.oa.vo.driver.SimpleDriverVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Driver的Service实现类
 * @author codeGenerator
 *
 */
@Service("driverService")
@Transactional
public class DriverServiceImpl extends BaseService implements DriverService{
	
	private static final Logger logger = LoggerFactory.getLogger(DriverServiceImpl.class);
	
	@Autowired
	private DriverDao driverDao;

	/**
	 * 根据条件查询Driver
	 * @param driverVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(DriverVO driverVO){
		PageHelper.startPage(driverVO.getPageNum(), driverVO.getPageSize());
		List<Driver> list = driverDao.findByConditionPage(driverVO);
		PageInfo<Driver> pageInfo = new PageInfo<Driver>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}


	@Override
	public List<DriverExportVO> findExportDriver(String name) {
		List<Driver> list = driverDao.findByCondition(name);
		return list.stream().map(source -> {
			DriverExportVO target = new DriverExportVO();
			BeanUtils.copyProperties(source, target);
			return target;
		}).collect(Collectors.toList());
	}

	/**
	 * 查询所有驾驶员的基本信息
	 * @param name 驾驶员姓名
	 * @return
	 */
	@Override
	public List<SimpleDriverVO> findAll(String name){
		return driverDao.findAll(name, "id");
	}
	/**
	 * 通过主键查询单个Driver
	 * @param id
	 * @return
	 */
	@Override
	public Driver findById(Long id) {
		return driverDao.findById(id);
	}
	
	/**
	 * 批量保存Driver
	 * @param drivers
	 */
	@Override
	public void batchAdd(List<Driver> drivers){
		driverDao.batchAdd(drivers);
	}
	/**
	 * 保存Driver
	 * @param driver
	 */
	@Override
	public void add(Driver driver){
		Integer checkMobile = driverDao.countByMobile(driver.getMobile());
		if (checkMobile > 0) {
			logger.error("驾驶员手机号{}重复", driver.getMobile());
			throw new BusinessException("驾驶员手机号重复");
		}
		driver.setStatus(DriverStatusEnum.DISABLE.getKey());
		driverDao.add(driver);
	}
	/**
	 * 批量更新Driver
	 * @param drivers
	 */
	@Override
	public void batchUpdate(List<Driver> drivers){
		driverDao.batchUpdate(drivers);
	}
	/**
	 * 更新Driver
	 * @param driver
	 */
	@Override
	public void update(Driver driver) {
		Driver toUpdate = driverDao.findById(driver.getId());
		if (toUpdate == null) {
			logger.error("驾驶员不存在, {}", driver);
			throw new BusinessException("驾驶员不存在");
		}
		driverDao.update(driver);
	}
	/**
	 * 删除Driver
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		driverDao.batchDelete(ids);
	}

    @Override
    public void convertStatus(Long id, Integer status) {
		Driver toConvertStatusDriver = driverDao.findById(id);
		if (toConvertStatusDriver == null) {
			logger.error("驾驶员{}不存在", id);
			throw new BusinessException("驾驶员不存在");
		}
		if (toConvertStatusDriver.getStatus().equals(status)) {
			logger.error("驾驶员的状态已经是{}", status);
			throw new BusinessException("驾驶员状态不匹配");
		}
		driverDao.updateStatus(id, status);
    }

	@Override
	public Boolean checkDriverMobile(String mobile) {
		return driverDao.countByMobile(mobile) > 0;
	}

    /**
	 * 删除Driver
	 * @param id
	 */
	@Override
	public void delete(Long id){
		driverDao.delete(id);
	}
}
