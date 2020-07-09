package com.bit.module.oa.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Application;
import com.bit.module.oa.bean.Executor;
import com.bit.module.oa.bean.Log;
import com.bit.module.oa.dao.ApplicationDao;
import com.bit.module.oa.dao.ExecutorDao;
import com.bit.module.oa.dao.LogDao;
import com.bit.module.oa.enums.*;
import com.bit.module.oa.service.ApplicationService;
import com.bit.module.oa.utils.ApplyNoGenerator;
import com.bit.module.oa.utils.PushUtil;
import com.bit.module.oa.vo.application.ApplicationPageVO;
import com.bit.module.oa.vo.application.ApplicationVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Application的Service实现类
 * @author codeGenerator
 *
 */
@Service("applicationService")
@Transactional
public class ApplicationServiceImpl extends BaseService implements ApplicationService{

	private static final Logger logger = LoggerFactory.getLogger(ApplicationServiceImpl.class);

	@Autowired
	private ApplicationDao applicationDao;

	@Autowired
	private ExecutorDao executorDao;

	@Autowired
	private LogDao logDao;

	@Autowired
	private PushUtil pushUtil;

	@Value("${oa.flow.application}")
	private Integer applicationFlowId;

	/**
	 * 根据条件查询Application
	 * @param applicationVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(ApplicationVO applicationVO){
		PageHelper.startPage(applicationVO.getPageNum(), applicationVO.getPageSize());
		// 只能找到自己的巡检信息
		applicationVO.setExecuteId(getCurrentUserInfo().getId());
		List<ApplicationPageVO> list = applicationDao.findByConditionPage(applicationVO);
		PageInfo<ApplicationPageVO> pageInfo = new PageInfo<>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 通过主键查询单个Application
	 * @param id
	 * @return
	 */
	@Override
	public Application findById(Long id){
		return applicationDao.findById(id);
	}

	/**
	 * 批量保存Application
	 * @param applications
	 */
	@Override
	public void batchAdd(List<Application> applications){
		applicationDao.batchAdd(applications);
	}
	/**
	 * 保存Application
	 * @param application
	 */
	@Override
	public void add(Application application){
		Executor toCheck = executorDao.findById(application.getExecuteId());
		if (toCheck == null) {
			logger.error("巡检任务不存在 : {}", application);
			throw new BusinessException("巡检任务不存在");
		}
		if (canNotApplyStatus(toCheck)) {
			logger.error("当前巡检任务不能申请补卡 : {}", toCheck);
			throw new BusinessException("当前巡检任务不能申请补卡");
		}
		if (!getCurrentUserInfo().getId().equals(toCheck.getExecutorId())) {
			logger.error("此任务不属于该用户 : {}", toCheck);
			throw new BusinessException("此任务不属于该用户");
		}
		UserInfo userInfo = getCurrentUserInfo();
		if (applicationDao.existByExecuteIdAndUserIdAndStatus(toCheck.getId(), userInfo.getId(), ApplicationStatusEnum.APPLY.getKey()) > 0) {
			logger.error("正在申请补卡，请等待审核结果 : {}", application);
			throw new BusinessException("正在申请补卡，请等待审核结果");
		}
		if (applicationDao.existByExecuteIdAndUserIdAndStatus(toCheck.getId(), userInfo.getId(), ApplicationStatusEnum.AUDIT.getKey()) > 0) {
			logger.error("该任务已经补卡成功 : {}", application);
			throw new BusinessException("该任务已经补卡成功");
		}
		String no = generateApplyNo();
		Application apply = new Application();
		Date applyTime = new Date();
		BeanUtils.copyProperties(application, apply);
		apply.setNo(no);
		apply.setApplyTime(applyTime);
		apply.setUserId(userInfo.getId());
		apply.setUserName(userInfo.getRealName());
		apply.setCreateAt(applyTime);
		apply.setStatus(ApplicationStatusEnum.APPLY.getKey());
		applicationDao.add(apply);

		Log log = Log.getLogByOperateAndType(apply.getId(), userInfo, LogOperateEnum.APPLY_PATCH, LogTypeEnum.APPLY);
		logDao.add(log);

		toCheck.setCheckInStatus(CheckInStatusEnum.PATCH.getKey());
		toCheck.setApplyStatus(ApplicationStatusEnum.APPLY.getKey());
		executorDao.update(toCheck);

		Application app = applicationDao.findById(apply.getId());

		// 发送推送
		pushUtil.sendMessageByFlow(app.getId(), applicationFlowId, MessageTemplateEnum.INSPECT_APPLICATION_APPLY,
				TargetTypeEnum.USER, new String[]{userInfo.getRealName(), MessageTemplateEnum.INSPECT_APPLICATION_APPLY.getInfo()},
				userInfo.getRealName(), app.getVersion());
	}

	/**
	 * 更新Application
	 * @param application
	 */
	@Override
	public void update(Application application){
		Application toUpdate = applicationDao.findById(application.getId());
		if (toUpdate == null) {
			logger.error("补卡申请不存在, {}", application);
			throw new BusinessException("补卡申请不存在");
		}
		application.setVersion(toUpdate.getVersion());
		applicationDao.update(application);
	}
	/**
	 * 删除Application
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		applicationDao.batchDelete(ids);
	}

	@Override
	public void audit(Long executeId, Integer status) {
		Application application = getAvailableApply(executeId);
		application.setStatus(status);
		UserInfo userInfo = getCurrentUserInfo();
		application.setApproverId(userInfo.getId());
		applicationDao.update(application);

		Log log = Log.getLogByOperateAndType(application.getId(), userInfo, LogOperateEnum.APPLY_AUDIT, LogTypeEnum.APPLY);
		logDao.add(log);

		Executor executor = executorDao.findById(executeId);
		executor.setCheckInStatus(CheckInStatusEnum.PATCH_SUCCESS.getKey());
		executor.setApplyStatus(ApplicationStatusEnum.AUDIT.getKey());
		executorDao.update(executor);

		// 发送通过补卡审核消息通知
		pushUtil.sendMessage(application.getId(), MessageTemplateEnum.INSPECT_APPLICATION_AUDIT, TargetTypeEnum.USER,
				new Long[]{application.getUserId()},
				new String[]{MessageTemplateEnum.INSPECT_APPLICATION_AUDIT.getInfo()},
                userInfo.getRealName());
	}

	private Application getAvailableApply(Long executeId) {
		Application application = applicationDao.findByExecuteId(executeId);
		if (application == null) {
			logger.error("补卡申请{}不存在", executeId);
			throw new BusinessException("补卡申请不存在");
		}
		if (!ApplicationStatusEnum.APPLY.getKey().equals(application.getStatus())) {
			logger.error("补卡申请已被审批 : {}", application);
			throw new BusinessException("补卡申请已被审批");
		}
		return application;
	}

	@Override
	public void reject(Application application) {
		Application apply = getAvailableApply(application.getExecuteId());
		apply.setRejectReason(application.getRejectReason());
		apply.setStatus(ApplicationStatusEnum.REJECT.getKey());
		UserInfo userInfo = getCurrentUserInfo();
		apply.setApproverId(userInfo.getId());
		applicationDao.update(apply);

		Log log = Log.getLogByOperateAndType(apply.getId(), userInfo, LogOperateEnum.APPLY_REJECT, LogTypeEnum.APPLY);
		logDao.add(log);

		Executor executor = executorDao.findById(apply.getExecuteId());
		executor.setCheckInStatus(CheckInStatusEnum.PATCH_FAILED.getKey());
		executor.setApplyStatus(ApplicationStatusEnum.REJECT.getKey());
		executorDao.update(executor);

		// 发送拒绝补卡审核消息通知
		pushUtil.sendMessage(application.getId(), MessageTemplateEnum.INSPECT_APPLICATION_REJECT, TargetTypeEnum.USER,
				new Long[]{apply.getUserId()},
				new String[]{MessageTemplateEnum.INSPECT_APPLICATION_REJECT.getInfo(), application.getRejectReason()},
				getCurrentUserInfo().getRealName());
	}

	/**
	 * 删除Application
	 * @param id
	 */
	@Override
	public void delete(Long id){
		applicationDao.delete(id);
	}


	/**
	 * 生成巡检报告单
	 * 规则 : 拼音首字母-年月日-当日派单号
	 */
	private String generateApplyNo() {
		String lastApplyNo = applicationDao.findNo();
		return ApplyNoGenerator.generateApplyNo(lastApplyNo, InspectTypeEnum.APPLY.getPrefix());
	}

	private boolean canNotApplyStatus(Executor toCheck) {
		return InspectStatusEnum.READY.getKey().equals(toCheck.getStatus())
				|| InspectStatusEnum.EXECUTE.getKey().equals(toCheck.getStatus())
				|| InspectStatusEnum.GIVE_UP.getKey().equals(toCheck.getStatus())
				|| CheckInStatusEnum.NORMAL.getKey().equals(toCheck.getCheckInStatus())
				|| CheckInStatusEnum.PATCH_SUCCESS.getKey().equals(toCheck.getCheckInStatus());
	}
}
