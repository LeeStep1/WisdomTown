package com.bit.module.oa.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.VehicleLog;
import com.bit.module.oa.dao.VehicleLogDao;
import com.bit.module.oa.service.VehicleLogService;
import com.bit.module.oa.vo.vehicleLog.VehicleLogDetailVO;
import com.bit.module.oa.vo.vehicleLog.VehicleLogQO;
import com.bit.module.oa.vo.vehicleLog.VehicleLogVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * VehicleLog的Service实现类
 * @author codeGenerator
 *
 */
@Service("vehicleLogService")
@Transactional
public class VehicleLogServiceImpl extends BaseService implements VehicleLogService{
	
	private static final Logger logger = LoggerFactory.getLogger(VehicleLogServiceImpl.class);
	
	@Autowired
	private VehicleLogDao vehicleLogDao;
	
	/**
	 * 根据条件查询VehicleLog
	 * @param vehicleLogVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(VehicleLogVO vehicleLogVO){
		PageHelper.startPage(vehicleLogVO.getPageNum(), vehicleLogVO.getPageSize());
		List<VehicleLogDetailVO> list = vehicleLogDao.findDetailByConditionPage(vehicleLogVO);
		PageInfo<VehicleLogDetailVO> pageInfo = new PageInfo<>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	@Override
	public BaseVo findByCondition(VehicleLogQO vehicleLogQO) {
		List<VehicleLogDetailVO> list = vehicleLogDao.findDetailByCondition(vehicleLogQO);
		return new BaseVo(list);
	}

	/**
	 * 查询所有VehicleLog
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<VehicleLog> findAll(String sorter){
		return vehicleLogDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个VehicleLog
	 * @param id
	 * @return
	 */
	@Override
	public VehicleLog findById(Long id){
		return vehicleLogDao.findById(id);
	}
	
	/**
	 * 批量保存VehicleLog
	 * @param vehicleLogs
	 */
	@Override
	public void batchAdd(List<VehicleLog> vehicleLogs){
		vehicleLogDao.batchAdd(vehicleLogs);
	}
	/**
	 * 保存VehicleLog
	 * @param vehicleLog
	 */
	@Override
	public void add(VehicleLog vehicleLog){
		vehicleLogDao.add(vehicleLog);
	}
	/**
	 * 批量更新VehicleLog
	 * @param vehicleLogs
	 */
	@Override
	public void batchUpdate(List<VehicleLog> vehicleLogs){
		vehicleLogDao.batchUpdate(vehicleLogs);
	}
	/**
	 * 更新VehicleLog
	 * @param vehicleLog
	 */
	@Override
	public void update(VehicleLog vehicleLog){
		vehicleLogDao.update(vehicleLog);
	}
	/**
	 * 删除VehicleLog
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		vehicleLogDao.batchDelete(ids);
	}
	/**
	 * 删除VehicleLog
	 * @param id
	 */
	@Override
	public void delete(Long id){
		vehicleLogDao.delete(id);
	}
}
