package com.bit.module.oa.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Company;
import com.bit.module.oa.bean.VehicleApplication;
import com.bit.module.oa.bean.VehicleLog;
import com.bit.module.oa.dao.*;
import com.bit.module.oa.enums.*;
import com.bit.module.oa.feign.SysServiceFeign;
import com.bit.module.oa.service.VehicleApplicationService;
import com.bit.module.oa.utils.ApplyNoGenerator;
import com.bit.module.oa.utils.PushUtil;
import com.bit.module.oa.utils.RedisDelayHandler;
import com.bit.module.oa.vo.driver.SimpleDriverVO;
import com.bit.module.oa.vo.user.UserVO;
import com.bit.module.oa.vo.vehicle.SimpleVehicleVO;
import com.bit.module.oa.vo.vehicleApplication.*;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.bit.module.oa.constant.redis.RedisConstant.VEHICLE_REJECT_ZSET;

/**
 * VehicleApplication的Service实现类
 * @author codeGenerator
 *
 */
@Service("vehicleApplicationService")
@Transactional
public class VehicleApplicationServiceImpl extends BaseService implements VehicleApplicationService{

	private static final Logger logger = LoggerFactory.getLogger(VehicleApplicationServiceImpl.class);

    /**
     * 批量操作的数据限制
     */
	private static final Integer DATA_SIZE = 500;

    /**
     * 政务oa的appId
     */
	private static final Integer OA_APP_ID = 2;

    /**
     * 批量查询用户的数量限制
     */
	private static final Integer QUERY_USER_SIZE = 1000;

	@Autowired
	private VehicleApplicationDao vehicleApplicationDao;

	@Autowired
	private VehicleLogDao vehicleLogDao;

	@Autowired
	private VehicleDao vehicleDao;

	@Autowired
	private DriverDao driverDao;

	@Autowired
	private CompanyDao companyDao;

	@Autowired
	private SysServiceFeign sysServiceFeign;

	@Autowired
	private RedisDelayHandler redisDelayHandler;

	@Autowired
	private PushUtil pushUtil;

	@Value("${oa.flow.vehicle}")
	private Integer vehicleApplyFlowId;

	/**
	 * 根据条件查询VehicleApplication
	 * @param vehicleApplicationVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(VehicleApplicationVO vehicleApplicationVO){
		PageHelper.startPage(vehicleApplicationVO.getPageNum(), vehicleApplicationVO.getPageSize());
		List<VehicleApplication> list = vehicleApplicationDao.findByConditionPage(vehicleApplicationVO);
		PageInfo<VehicleApplication> pageInfo = new PageInfo<VehicleApplication>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	@Override
	public BaseVo findMyVehicleApplicationPage(VehicleApplicationQO vehicleApplicationQO) {
		UserInfo currentUserInfo = getCurrentUserInfo();
		vehicleApplicationQO.setUserId(currentUserInfo.getId());
		PageHelper.startPage(vehicleApplicationQO.getPageNum(), vehicleApplicationQO.getPageSize());
		vehicleApplicationQO.setOrderBy("apply_time");
		vehicleApplicationQO.setOrder("desc");

		List<MyVehicleApplication> list = vehicleApplicationDao.findMyVehicleApplication(vehicleApplicationQO);
		Set<Long> userIds = list.stream().map(MyVehicleApplication::getUserId).collect(Collectors.toSet());
		// 根据userId获取用户信息
		Map<Long, String> userInfoResultMap = getUserInfoFromFeign(userIds);
		list.forEach(app -> app.setUserName(userInfoResultMap.get(app.getUserId())));

		PageInfo<MyVehicleApplication> pageInfo = new PageInfo<>(list);
		return new BaseVo(pageInfo);
	}

	@Override
	public List<MyVehicleApplicationExportVO> findExportMyVehicleUsing(VehicleApplicationExportQO vehicleApplicationQO) {
		List<VehicleApplicationDetail> list = vehicleApplicationDao.findExportDataByCondition(vehicleApplicationQO);
		Set<Long> userIds = list.stream().map(VehicleApplicationDetail::getUserId).collect(Collectors.toSet());
		// 根据userId获取用户信息
		Map<Long, String> userInfoResultMap = getUserInfoFromFeign(userIds);
		list.forEach(app -> app.setUserName(userInfoResultMap.get(app.getUserId())));

		return list.stream().map(source -> {
			MyVehicleApplicationExportVO target = new MyVehicleApplicationExportVO();
			BeanUtils.copyProperties(source, target);
            target.setNature(VehicleApplicationNatureEnum.getByKey(source.getNature()).getDescription());
            target.setUsage(VehicleApplicationUsageEnum.getByKey(source.getUsage()).getDescription());
            String date = splicePlanTime(source);
            target.setPlanTime(date);
			return target;
		}).collect(Collectors.toList());
	}

	@Override
	public BaseVo findLedgerApplicationListPage(VehicleApplicationQO vehicleApplicationQO) {
		PageHelper.startPage(vehicleApplicationQO.getPageNum(), vehicleApplicationQO.getPageSize());
		vehicleApplicationQO.setOrderBy("apply_time");
		vehicleApplicationQO.setOrder("desc");

		List<ManagerVehicleApplicationVO> list = vehicleApplicationDao.findLedgerVehicleApplication(vehicleApplicationQO);
		Set<Long> userIds = list.stream().map(ManagerVehicleApplicationVO::getUserId).collect(Collectors.toSet());
		// 根据userId获取用户信息
		Map<Long, String> userInfoResultMap = getUserInfoFromFeign(userIds);
		list.forEach(app -> app.setUserName(userInfoResultMap.get(app.getUserId())));
		PageInfo<ManagerVehicleApplicationVO> pageInfo = new PageInfo<>(list);
		return new BaseVo(pageInfo);
	}

	@Override
	public BaseVo findHandleApplicationListPage(VehicleApplicationQO vehicleApplicationQO) {
		PageHelper.startPage(vehicleApplicationQO.getPageNum(), vehicleApplicationQO.getPageSize());
		vehicleApplicationQO.setOrderBy("apply_time");
		vehicleApplicationQO.setOrder("desc");
		List<ManagerVehicleApplicationVO> list = vehicleApplicationDao.findHandleVehicleApplication(vehicleApplicationQO);
		Set<Long> userIds = list.stream().map(ManagerVehicleApplicationVO::getUserId).collect(Collectors.toSet());
		// 根据userId获取用户信息
		Map<Long, String> userInfoResultMap = getUserInfoFromFeign(userIds);
		list.forEach(app -> app.setUserName(userInfoResultMap.get(app.getUserId())));
		PageInfo<ManagerVehicleApplicationVO> pageInfo = new PageInfo<>(list);
		return new BaseVo(pageInfo);
	}

	@Override
	public List<HandleVehicleApplicationExportVO> findHandleApplicationExport(
			VehicleApplicationExportQO vehicleApplicationQO) {
		List<VehicleApplicationDetail> list = vehicleApplicationDao.findExportDataByCondition(vehicleApplicationQO);
		return getExportVehicleApplicationData(list);
	}


	@Override
	public List<HandleVehicleApplicationExportVO> findLedgerApplicationExport(
			VehicleApplicationExportQO vehicleApplicationQO) {
		List<VehicleApplicationDetail> list = vehicleApplicationDao.findLedgerExportDataByCondition(vehicleApplicationQO);
		return getExportVehicleApplicationData(list);
	}

	@Override
	public void invalid(Long id) {
		VehicleApplication toCheck = vehicleApplicationDao.findById(id);
		if (toCheck == null) {
			logger.error("没有查到该派车单");
			return;
		}
		if (!VehicleApplicationStatusEnum.DRAFT.getKey().equals(toCheck.getStatus())) {
			logger.error("派车单已经进行中", toCheck);
			return;
		}
		toCheck.setStatus(VehicleApplicationStatusEnum.INVALID.getKey());
		logger.info("会议失效生效中...");
		vehicleApplicationDao.update(toCheck);
	}

	private List<HandleVehicleApplicationExportVO> getExportVehicleApplicationData(
			List<VehicleApplicationDetail> list) {
		Set<Long> userIds = list.stream().map(VehicleApplicationDetail::getUserId).collect(Collectors.toSet());
		// 根据userId获取用户信息
		Map<Long, String> userInfoResultMap = getUserInfoFromFeign(userIds);
		list.forEach(app -> app.setUserName(userInfoResultMap.get(app.getUserId())));
		return list.stream().map(source -> {
			HandleVehicleApplicationExportVO target = new HandleVehicleApplicationExportVO();
			BeanUtils.copyProperties(source, target);
			target.setAssignerName(userInfoResultMap.get(source.getAssignerId()));
			target.setNature(VehicleApplicationNatureEnum.getByKey(source.getNature()).getDescription());
			target.setUsage(VehicleApplicationUsageEnum.getByKey(source.getUsage()).getDescription());
            String planTime = splicePlanTime(source);
            String realTime = spliceRealTime(source);
            target.setPlanTime(planTime);
			target.setRealTime(realTime);
			return target;
		}).collect(Collectors.toList());
	}

    /**
	 * 查询所有VehicleApplication
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<VehicleApplication> findAll(String sorter){
		return vehicleApplicationDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个VehicleApplication
	 * @param id
	 * @return
	 */
	@Override
	public VehicleApplication findById(Long id){
		return vehicleApplicationDao.findById(id);
	}

	/**
	 * 批量保存VehicleApplication
	 * @param vehicleApplications
	 */
	@Override
	public void batchAdd(List<VehicleApplication> vehicleApplications) {
		vehicleApplicationDao.batchAdd(vehicleApplications);
	}
	/**
	 * 保存VehicleApplication
	 * @param vehicleApplication
	 */
	@Override
	public void apply(VehicleApplication vehicleApplication){
		VehicleApplicationUsageEnum usageEnum = VehicleApplicationUsageEnum.getByKey(vehicleApplication.getUsage());
		UserInfo currentUserInfo = getCurrentUserInfo();
		if (vehicleApplication.getUserId() == null) {
			vehicleApplication.setUserId(currentUserInfo.getId());
		}
		vehicleApplication.setApplyNo(generateApplyNo(usageEnum));
		vehicleApplication.setApplyTime(new Date());
		vehicleApplication.setStatus(VehicleApplicationStatusEnum.DRAFT.getKey());
		vehicleApplicationDao.apply(vehicleApplication);

		// 如果超过用车申请的截止时间还未派车
		redisDelayHandler.setDelayZSet(VEHICLE_REJECT_ZSET, vehicleApplication.getId(), vehicleApplication.getPlanEndTime());

		VehicleApplicationDetail detail = vehicleApplicationDao.findById(vehicleApplication.getId());

		// 发送推送
		pushUtil.sendMessageByFlow(vehicleApplication.getId(), vehicleApplyFlowId, MessageTemplateEnum.VEHICLE_APPLY,
				TargetTypeEnum.USER, new String[]{currentUserInfo.getRealName(), MessageTemplateEnum.VEHICLE_APPLY.getInfo()},
				currentUserInfo.getRealName(), detail.getVersion());
	}
	/**
	 * 更新VehicleApplication
	 * @param vehicleApplication
	 */
	@Override
	public void update(VehicleApplication vehicleApplication) {
		VehicleApplicationDetail toCheck = vehicleApplicationDao.findById(vehicleApplication.getId());
		if (toCheck == null) {
			logger.error("用车申请不存在 : {}", vehicleApplication);
			throw new BusinessException("用车申请不存在");
		}
		vehicleApplication.setVersion(toCheck.getVersion());
		vehicleApplicationDao.update(vehicleApplication);
	}
	/**
	 * 删除VehicleApplication
	 * @param ids
	 */
	@Override
	public void batchDelete(List<Long> ids){
		vehicleApplicationDao.batchDelete(ids);
	}

	@Override
	public VehicleApplicationDetail findVehicleApplicationDetail(VehicleApplication toQuery) {
		VehicleApplicationDetail vehicleApplication = vehicleApplicationDao.findById(toQuery.getId());
		if (toQuery.getUserId() != null && !vehicleApplication.getUserId().equals(toQuery.getUserId())) {
			logger.error("该用户无权限查询此用车记录{}", vehicleApplication);
			throw new BusinessException("该用户无权限查询此用车记录");
		}
		Map<Long, String> info = getUserInfoFromFeign(new HashSet(
				Arrays.asList(vehicleApplication.getUserId(), vehicleApplication.getAssignerId())));
		vehicleApplication.setUserName(info.get(vehicleApplication.getUserId()));
		vehicleApplication.setAssignerName(vehicleApplication.getAssignerId() == null
                ? null : info.get(vehicleApplication.getAssignerId()));
		return vehicleApplication;
	}

	@Override
	public void reject(VehicleApplication vehicleApplication) {
		VehicleApplication toCheck = checkApplicationStatus(vehicleApplication.getId(), VehicleApplicationStatusEnum.DRAFT);
		VehicleApplication toReject = new VehicleApplication();
		toReject.setId(toCheck.getId());
		toReject.setStatus(VehicleApplicationStatusEnum.REJECT.getKey());
		toReject.setRejectReason(vehicleApplication.getRejectReason());
		UserInfo currentUserInfo = getCurrentUserInfo();
		toReject.setAssignerId(currentUserInfo.getId());
		toReject.setVersion(toCheck.getVersion());
		vehicleApplicationDao.update(toReject);

		// 发送拒绝派车消息通知
		pushUtil.sendMessage(toCheck.getId(), MessageTemplateEnum.VEHICLE_REJECT, TargetTypeEnum.USER,
				new Long[]{toCheck.getUserId()},
				new String[]{MessageTemplateEnum.VEHICLE_REJECT.getInfo(), toReject.getRejectReason()},
				getCurrentUserInfo().getRealName());
	}

	@Override
	public void rent(VehicleRentInfo vehicleRentInfo) {
		VehicleApplication toCheck = checkApplicationStatus(vehicleRentInfo.getId(), VehicleApplicationStatusEnum.DRAFT);
		if (!VehicleApplicationUsageEnum.RENT.getKey().equals(toCheck.getUsage())) {
			logger.error("当前派车单不能租赁 : {}", toCheck);
			throw new BusinessException("当前派车单不能租赁");
		}
		VehicleApplication toRent = new VehicleApplication();
		Company checkCompany = companyDao.findById(vehicleRentInfo.getCompanyId());
		if (checkCompany == null || CompanyStatusEnum.DISABLE.getKey().equals(checkCompany.getStatus())) {
			logger.error("不能对未启用第三方公司进行租赁行为 : {}", checkCompany);
			throw new BusinessException("不能对未启用第三方公司进行租赁行为");
		}
		String drivers = getDrivers(vehicleRentInfo);
		toRent.setId(toCheck.getId());
		toRent.setCompanyId(vehicleRentInfo.getCompanyId());
		toRent.setDrivers(drivers);
        String plateNos = Arrays.toString(vehicleRentInfo.getPlateNos().toArray());
        toRent.setPlateNos(plateNos.substring(1, plateNos.length() - 1));
		UserInfo currentUserInfo = getCurrentUserInfo();
		toRent.setAssignerId(currentUserInfo.getId());
		toRent.setStatus(VehicleApplicationStatusEnum.SEND.getKey());
		toRent.setVersion(toCheck.getVersion());
		logger.info("租赁派车 : {}", toRent);
		vehicleApplicationDao.update(toRent);
	}

	private String getDrivers(VehicleRentInfo vehicleRentInfo) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < vehicleRentInfo.getDriverName().size(); i++) {
			sb.append(vehicleRentInfo.getDriverName().get(i)).append("(")
					.append(vehicleRentInfo.getDriverPhone().get(i)).append(")");
		}
		return sb.toString();
	}

	@Override
	public void allow(VehicleAllowInfo vehicleAllowInfo) {
		VehicleApplication toCheck = checkApplicationStatus(vehicleAllowInfo.getId(), VehicleApplicationStatusEnum.DRAFT);
		if (!VehicleApplicationUsageEnum.OFFICIAL.getKey().equals(toCheck.getUsage())) {
			logger.error("当前派车单不能进行派车 : {}", toCheck);
			throw new BusinessException("当前派车单不能进行派车");
		}

		String plateNos = getPlateNosFromVehicleId(vehicleAllowInfo);
		String driversInfo = getDriversInfoFromDriverIds(vehicleAllowInfo);
		if (vehicleLogDao.countOccupyLog(vehicleAllowInfo.getVehicleIds(), vehicleAllowInfo.getStartTime(), vehicleAllowInfo.getEndTime()) > 0) {
			logger.error("当前车辆派车时间已被占用 : {}", vehicleAllowInfo);
			throw new BusinessException("当前车辆派车时间已被占用");
		}
		vehicleDao.occupyVehicleById(vehicleAllowInfo.getVehicleIds());
		insertVehicleLog(vehicleAllowInfo);
		VehicleApplication toAllow = new VehicleApplication();
		toAllow.setId(toCheck.getId());
		UserInfo currentUserInfo = getCurrentUserInfo();
		toAllow.setAssignerId(currentUserInfo.getId());
		toAllow.setDispatchTime(new Date());
		toAllow.setPlateNos(plateNos);
		toAllow.setDrivers(driversInfo);
		toAllow.setStartTime(vehicleAllowInfo.getStartTime());
		toAllow.setEndTime(vehicleAllowInfo.getEndTime());
		toAllow.setRemark(vehicleAllowInfo.getRemark());
		toAllow.setStatus(VehicleApplicationStatusEnum.SEND.getKey());
		toAllow.setVersion(toCheck.getVersion());
		vehicleApplicationDao.update(toAllow);

		// 发送确认派车消息通知
		pushUtil.sendMessage(toCheck.getId(), MessageTemplateEnum.VEHICLE_CONFIRM, TargetTypeEnum.USER,
				new Long[]{toCheck.getUserId()}, new String[]{MessageTemplateEnum.VEHICLE_CONFIRM.getInfo()},
				getCurrentUserInfo().getRealName());
	}

	private void insertVehicleLog(VehicleAllowInfo vehicleAllowInfo) {
		List<VehicleLog> vehicleLogList = vehicleAllowInfo.getVehicleIds().stream().map(info -> {
			VehicleLog vehicleLog = new VehicleLog();
			vehicleLog.setStartTime(vehicleAllowInfo.getStartTime());
			vehicleLog.setEndTime(vehicleAllowInfo.getEndTime());
			vehicleLog.setApplicationId(vehicleAllowInfo.getId());
			vehicleLog.setVehicleId(info);
			return vehicleLog;
		}).collect(Collectors.toList());

		int size = vehicleLogList.size() / DATA_SIZE + 1;

		for (int i = 0; i < size; i++) {
			List<VehicleLog> partOfAdd = vehicleLogList.stream().limit(DATA_SIZE).collect(Collectors.toList());
			vehicleLogDao.batchAdd(partOfAdd);
			List<VehicleLog> hasUpdate = vehicleLogList.subList(0, vehicleLogList.size() > DATA_SIZE ? DATA_SIZE : vehicleLogList.size());
			hasUpdate.clear();
		}
	}

	@Override
    public void end(VehicleApplication vehicleApplication) {
		VehicleApplication toCheck = checkApplicationStatus(vehicleApplication.getId(), VehicleApplicationStatusEnum.SEND);
		if (VehicleApplicationUsageEnum.OFFICIAL.getKey().equals(toCheck.getUsage())) {
			vehicleDao.releaseVehicleByApplicationId(toCheck.getId());
		}
		// 释放车辆占用状态
		VehicleApplication toEnd = new VehicleApplication();
		toEnd.setId(toCheck.getId());
		toEnd.setStatus(VehicleApplicationStatusEnum.END.getKey());
		toEnd.setVersion(toCheck.getVersion());
		// 结束用车
		logger.info("结束用车 : {}", toEnd);
		vehicleApplicationDao.update(toEnd);
    }

	/**
	 * 删除VehicleApplication
	 * @param id
	 */
	@Override
	public void delete(Long id){
		vehicleApplicationDao.delete(id);
	}

	/**
	 * 生成派车单
	 * 规则 : 拼音首字母-年月日-当日派单号
	 * 如公务用车 2018年12月1日第二张派车单 : GW-20181201-0002
	 * @param usage
	 * @return
	 */
	private String generateApplyNo(VehicleApplicationUsageEnum usage) {
		String lastApplyNo = vehicleApplicationDao.findByApplyNo(usage.getPrefix());
		return ApplyNoGenerator.generateApplyNo(lastApplyNo, usage.getPrefix());
	}

	private VehicleApplication checkApplicationStatus(Long id, VehicleApplicationStatusEnum expectStatus) {
		VehicleApplication toCheck = vehicleApplicationDao.findById(id);
		if (toCheck == null) {
			logger.error("没有查到该派车单");
			throw new BusinessException("没有查到该派车单");
		}
		if (!expectStatus.getKey().equals(toCheck.getStatus())) {
			logger.error("当前派车单状态不能进行 {} 状态的后续操作", expectStatus.getDescription());
			throw new BusinessException("当前派车单状态不能进行" + expectStatus.getDescription() + "的后续操作");
		}
		return toCheck;
	}

	private String getPlateNosFromVehicleId(VehicleAllowInfo vehicleAllowInfo) {
		List<SimpleVehicleVO> vehicleVOList = vehicleDao.findByIds(vehicleAllowInfo.getVehicleIds());
		if (existDisableVehicle(vehicleVOList)) {
			logger.error("选中车辆中含有未启用车辆 : {}", vehicleVOList);
			throw new BusinessException("选中车辆中含有未启用车辆");
		}
		// 去除无效车辆ID
		vehicleAllowInfo.setVehicleIds(vehicleVOList.stream().map(SimpleVehicleVO::getId).collect(Collectors.toList()));
		String plateNos = Arrays.toString(vehicleVOList.stream().map(vo -> vo.getPlateNo()).toArray());
		plateNos = plateNos.substring(1, plateNos.length() - 1);
		return plateNos;
	}

	private String getDriversInfoFromDriverIds(VehicleAllowInfo vehicleAllowInfo) {
		List<SimpleDriverVO> driverVOList = driverDao.findByIds(vehicleAllowInfo.getDriverIds());
		if (existDisableDriver(driverVOList)) {
			logger.error("选中驾驶员中含有未启用驾驶员 : {}", driverVOList);
			throw new BusinessException("不能选择未启用驾驶员");
		}
		// 去除无效驾驶员ID
		vehicleAllowInfo.setDriverIds(driverVOList.stream().map(SimpleDriverVO::getId).collect(Collectors.toList()));
		String drivers = Arrays.toString(driverVOList.stream().map(vo -> vo.getName() + "(" + vo.getMobile() + ")").toArray());
		drivers = drivers.substring(1, drivers.length() - 1);
		return drivers;
	}

    /**
     * 选中驾驶员列表中是否有未启用驾驶员
     * @param driverVOList
     * @return
     */
	private boolean existDisableDriver(List<SimpleDriverVO> driverVOList) {
		return CollectionUtils.isNotEmpty(
				driverVOList.stream().filter(vo -> DriverStatusEnum.DISABLE.getKey().equals(vo.getStatus()))
				        .collect(Collectors.toList()));
	}

	/**
	 * 选中车辆列表中是否有未启用的车辆
	 * @param vehicleVOList
	 * @return
	 */
	private boolean existDisableVehicle(List<SimpleVehicleVO> vehicleVOList) {
		return CollectionUtils.isNotEmpty(
				vehicleVOList.stream().filter(vo -> VehicleStatusEnum.DISABLE.getKey().equals(vo.getStatus()))
						.collect(Collectors.toList()));
	}

    /**
     * 从sys获取用户信息
     * @param userIds
     * @return
     */
	private Map<Long, String> getUserInfoFromFeign(Set<Long> userIds) {
		Map<Long, String> userInfoResultMap = new HashMap<>();
		Map queryParam = new HashMap();
		queryParam.put("uids", userIds);
		// oa的appId
		queryParam.put("appId", OA_APP_ID);
		queryParam.put("pageSize", QUERY_USER_SIZE);
		logger.info("查询用户信息列表, 请求参数 : ", queryParam);
		BaseVo<List<UserVO>> baseVo = sysServiceFeign.listByAppIdAndIds(queryParam);
		if (baseVo != null) {
			List<UserVO> data = baseVo.getData();
			for (UserVO user : data) {
				userInfoResultMap.put(user.getId(), user.getRealName());
			}
		}
		return userInfoResultMap;
	}

    /**
     * 拼接计划派车时间
     * @param source
     * @return
     */
    private String splicePlanTime(VehicleApplicationDetail source) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String startTime = sdf.format(source.getPlanStartTime());
        String endTime = sdf.format(source.getPlanEndTime());
        return startTime + "--" + endTime;
    }

    /**
     * 拼接实际派车时间
     * @param source
     * @return
     */
    private String spliceRealTime(VehicleApplicationDetail source) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String realStartTime = StringUtils.isEmpty(source.getStartTime()) ? null : sdf.format(source.getStartTime());
        String realEndTime = StringUtils.isEmpty(source.getEndTime()) ? null : sdf.format(source.getEndTime());
        return StringUtils.isEmpty(realStartTime) ? null : realStartTime + "--" + realEndTime;
    }
}
