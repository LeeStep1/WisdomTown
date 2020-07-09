package com.bit.module.oa.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.CheckIn;
import com.bit.module.oa.bean.Executor;
import com.bit.module.oa.bean.Log;
import com.bit.module.oa.dao.CheckInDao;
import com.bit.module.oa.dao.ExecutorDao;
import com.bit.module.oa.dao.LogDao;
import com.bit.module.oa.enums.InspectStatusEnum;
import com.bit.module.oa.enums.LogOperateEnum;
import com.bit.module.oa.enums.LogTypeEnum;
import com.bit.module.oa.service.CheckInService;
import com.bit.module.oa.vo.checkIn.CheckInVO;
import com.bit.module.oa.vo.checkIn.UserCheckInVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CheckIn的Service实现类
 * @author codeGenerator
 *
 */
@Service("checkInService")
@Transactional
public class CheckInServiceImpl extends BaseService implements CheckInService{

	private static final Logger logger = LoggerFactory.getLogger(CheckInServiceImpl.class);

	@Autowired
	private CheckInDao checkInDao;

	@Autowired
	private ExecutorDao executorDao;

	@Autowired
	private LogDao logDao;

	/**
	 * 根据条件查询CheckIn
	 * @param checkInVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(CheckInVO checkInVO){
		List<CheckIn> list = checkInDao.findByCondition(checkInVO);
		List<UserCheckInVO> checkIns = new ArrayList<>();
		Map<Long, UserCheckInVO> map = new HashMap<>();
		for (CheckIn checkIn : list) {
			if (!checkIns.stream().map(UserCheckInVO::getUserId).collect(Collectors.toSet()).contains(checkIn.getUserId())) {
				UserCheckInVO userCheckInVO = new UserCheckInVO();
				userCheckInVO.setUserId(checkIn.getUserId());
				userCheckInVO.setUserName(checkIn.getUserName());
				userCheckInVO.setCheckIns(new ArrayList<>());
				checkIns.add(userCheckInVO);
				map.put(checkIn.getUserId(), userCheckInVO);
			}
			UserCheckInVO userCheckInVO = map.get(checkIn.getUserId());
			userCheckInVO.getCheckIns().add(checkIn);
		}
		BaseVo baseVo = new BaseVo();
		baseVo.setData(checkIns);
		return baseVo;
	}

	/**
	 * 通过主键查询单个CheckIn
	 * @param id
	 * @return
	 */
	@Override
	public CheckIn findById(Long id){
		return checkInDao.findById(id);
	}

	/**
	 * 批量保存CheckIn
	 * @param checkIns
	 */
	@Override
	public void batchAdd(List<CheckIn> checkIns){
		checkInDao.batchAdd(checkIns);
	}
	/**
	 * 保存CheckIn
	 * @param checkIn
	 */
	@Override
	public void add(CheckIn checkIn){
		checkInDao.add(checkIn);
	}
	/**
	 * 更新CheckIn
	 * @param checkIn
	 */
	@Override
	public void update(CheckIn checkIn){
		CheckIn toUpdate = checkInDao.findById(checkIn.getId());
		if (toUpdate == null) {
			logger.error("签到点不存在, {}", checkIn);
			throw new BusinessException("签到点不存在");
		}
		checkInDao.update(checkIn);
	}
	/**
	 * 删除CheckIn
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		checkInDao.batchDelete(ids);
	}

	@Override
	public void signIn(CheckIn checkIn) {
		CheckIn toCheck = checkInDao.findById(checkIn.getId());
		if (toCheck == null || !getCurrentUserInfo().getId().equals(toCheck.getUserId())) {
			logger.error("任务签到点不存在, {}", checkIn);
			throw new BusinessException("任务签到点不存在");
		}
		Executor executor = executorDao.findById(toCheck.getExecuteId());
		if (executor.getInspectEndTime().before(new Date())) {
			logger.error("超期任务无法签到, {}", executor);
			throw new BusinessException("超期任务无法签到");
		}
		if (!InspectStatusEnum.EXECUTE.getKey().equals(executor.getStatus())) {
			logger.error("任务并非执行状态, {}", executor);
			throw new BusinessException("任务并非执行状态");
		}
		toCheck.setCheckInAt(new Date());
		checkInDao.updateCheckInStatus(toCheck);
		Log log = Log.getLogByOperateAndType(toCheck.getInspectId(), getCurrentUserInfo(), LogOperateEnum.INSPECT_SIGN, LogTypeEnum
				.INSPECT);
		logDao.add(log);
	}

	@Override
	public List<CheckIn> findByInspectId(Long InspectId) {
		return checkInDao.findByInspectId(InspectId);
	}

	/**
	 * 删除CheckIn
	 * @param id
	 */
	@Override
	public void delete(Long id){
		checkInDao.delete(id);
	}
}
