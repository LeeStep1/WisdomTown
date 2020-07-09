package com.bit.module.oa.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.*;
import com.bit.module.oa.dao.*;
import com.bit.module.oa.enums.*;
import com.bit.module.oa.feign.SysServiceFeign;
import com.bit.module.oa.service.ExecutorService;
import com.bit.module.oa.vo.executor.ExecutorPageVO;
import com.bit.module.oa.vo.executor.ExecutorExportVO;
import com.bit.module.oa.vo.executor.ExecutorVO;
import com.bit.module.oa.vo.executor.InspectExecuteDetail;
import com.bit.module.oa.vo.user.UserVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Executor的Service实现类
 *
 * @author codeGenerator
 */
@Service("executorService")
@Transactional
public class ExecutorServiceImpl extends BaseService implements ExecutorService {

    private static final Logger logger = LoggerFactory.getLogger(ExecutorServiceImpl.class);

    @Autowired
    private ExecutorDao executorDao;

    @Autowired
    private InspectDao inspectDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private CheckInDao checkInDao;

    @Autowired
    private ApplicationDao applicationDao;

    @Autowired
    private SysServiceFeign sysServiceFeign;

    // 一次批量插入大小
    private static final Integer DATA_SIZE = 500;

    /**
     * 政务oa的appId
     */
    private static final Integer OA_APP_ID = 2;

    /**
     * 批量查询用户的数量限制
     */
    private static final Integer QUERY_USER_SIZE = 1000;


    @Override
    public BaseVo findByConditionPage(ExecutorVO executorVO) {
        PageHelper.startPage(executorVO.getPageNum(), executorVO.getPageSize());
        executorVO.setOrder("desc");
        executorVO.setOrderBy("inspect_start_time");
        executorVO.setInspectType(InspectTypeEnum.LOCUS.getKey());

        // 只能找到自己的巡检信息
        executorVO.setExecutorId(getCurrentUserInfo().getId());

        List<ExecutorPageVO> list = executorDao.findByConditionPage(executorVO);
        Set<Long> userIds = list.stream().map(Executor::getExecutorId).collect(Collectors.toSet());
        Map<Long, String> userInfos = getUserInfoFromFeign(userIds);
        list.forEach(executor -> executor.setUsername(userInfos.get(executor.getExecutorId())));
        PageInfo<ExecutorPageVO> pageInfo = new PageInfo<>(list);
        return new BaseVo(pageInfo);
    }

    @Override
    public void giveUp(Executor executor) {
        Executor toCheck = getExecutorIfExist(executor);
        if (!toCheck.getStatus().equals(InspectStatusEnum.READY.getKey()) || toCheck.getInspectEndTime()
                .before(new Date())) {
            logger.error("当前巡检任务不能放弃 : {}", toCheck);
            throw new BusinessException("当前巡检任务不能放弃");
        }
        toCheck.setStatus(InspectStatusEnum.GIVE_UP.getKey());
        toCheck.setAbortReason(executor.getAbortReason());
        executorDao.giveUp(toCheck);
        // 操作日志
        Log log = Log.getLogByOperateAndType(toCheck.getInspectId(), getCurrentUserInfo(), LogOperateEnum.INSPECT_GIVE_UP,
                LogTypeEnum.INSPECT);
        logDao.add(log);
        List<Executor> allExecutor = executorDao.findByInspectId(toCheck.getInspectId());
        updateStatusIfAllGiveUpOrFinish(toCheck, allExecutor);
    }


    @Override
    public void start(Executor executor) {
        Executor toCheck = getExecutorIfExist(executor);
        Date checkAt = new Date();
        if (!toCheck.getStatus().equals(InspectStatusEnum.READY.getKey()) || toCheck.getInspectEndTime()
                .before(checkAt) || toCheck.getInspectStartTime().after(checkAt)) {
            logger.error("当前巡检任务不能开始执行 : {}", toCheck);
            throw new BusinessException("当前巡检任务不能开始执行");
        }
        if (!getCurrentUserInfo().getId().equals(toCheck.getExecutorId())) {
            logger.error("此任务不属于该用户 : {}", toCheck);
            throw new BusinessException("此任务不属于该用户");
        }
        executorDao.updateStatus(toCheck.getId(), InspectStatusEnum.EXECUTE.getKey(), toCheck.getVersion());
        // 操作日志
        Log log = Log.getLogByOperateAndType(toCheck.getInspectId(), getCurrentUserInfo(), LogOperateEnum.INSPECT_EXECUTE,
                LogTypeEnum.INSPECT);
        logDao.add(log);

        inspectDao.updateStatus(toCheck.getInspectId(), InspectStatusEnum.EXECUTE.getKey(), toCheck.getVersion());
        Inspect inspect = inspectDao.findById(toCheck.getInspectId());
        if (!InspectTypeEnum.LOCUS.getKey().equals(inspect.getType())) {
            return;
        }
        List<SimpleSpot> simpleSpots = inspect.parseSpots();
        List<CheckIn> checkIns = simpleSpots.stream().map(s -> createCheckInByExecutorIds(s, toCheck, inspect))
                .collect(Collectors.toList());
        if (CollectionUtils.isNotEmpty(checkIns)) {
            batchAddCheckIns(checkIns);
        }
    }

    @Override
    public void end(Executor executor) {
        Executor toCheck = getExecutorIfExist(executor);
        if (!toCheck.getStatus().equals(InspectStatusEnum.EXECUTE.getKey()) || toCheck.getInspectEndTime()
                .before(new Date())) {
            logger.error("当前巡检任务不能完成 : {}", toCheck);
            throw new BusinessException("当前巡检任务不能完成");
        }
        if (!getCurrentUserInfo().getId().equals(toCheck.getExecutorId())) {
            logger.error("此任务不属于该用户 : {}", toCheck);
            throw new BusinessException("此任务不属于该用户");
        }
        updateCheckInStatusIfLocusType(toCheck);
        toCheck.setStatus(InspectStatusEnum.FINISH.getKey());
        executorDao.end(toCheck);

        // 操作日志
        Log log = Log.getLogByOperateAndType(toCheck.getInspectId(), getCurrentUserInfo(), LogOperateEnum.INSPECT_END,
                LogTypeEnum.INSPECT);
        logDao.add(log);

        List<Executor> allExecutor = executorDao.findByInspectId(toCheck.getInspectId());
        if (allExecutor.stream().anyMatch(e -> e.getStatus().equals(InspectStatusEnum.READY.getKey()) || e.getStatus()
                .equals(InspectStatusEnum.EXECUTE.getKey()))) {
            return;
        }
        inspectDao.updateStatus(toCheck.getInspectId(), InspectStatusEnum.FINISH.getKey(), toCheck.getVersion());
    }

    @Override
    public InspectExecuteDetail findDetail(Long id) {
        InspectExecuteDetail executor = executorDao.findAllDetailById(id);

        if (executor == null) {
            return null;
        }
        executor.setCheckIns(checkInDao.findByExecuteId(executor.getId()));
        executor.setApplication(applicationDao.findByExecuteId(executor.getId()));
        BaseVo<UserVO> userVOBaseVo = sysServiceFeign.queryByUserId(executor.getExecutorId());
        executor.setExecutorName(userVOBaseVo.getData().getRealName());

        if (executor.getApplication() != null) {
            executor.setLogs(logDao.findByRefIdAndType(executor.getApplication().getId(),
                    LogTypeEnum.APPLY.getKey()));
        }

        return executor;
    }

    @Override
    public List<ExecutorExportVO> findExportExecutor(Executor toQuery) {

        return executorDao.findExportList(toQuery);
    }

    private void updateCheckInStatusIfLocusType(Executor toCheck) {
        if (InspectTypeEnum.INTELLIGENT.getKey().equals(toCheck.getInspectType())) {
            return;
        }
        List<CheckIn> checkIns = checkInDao.findByExecuteId(toCheck.getId());
        // 是否存在漏打卡记录
        boolean missingCheckRecord;
        if (CollectionUtils.isEmpty(checkIns)) {
            missingCheckRecord = false;
        } else {
            missingCheckRecord = checkIns.stream().anyMatch(check -> check.getCheckInAt() == null);
        }

        toCheck.setCheckInStatus(
                missingCheckRecord ? CheckInStatusEnum.MISSING.getKey() : CheckInStatusEnum.NORMAL.getKey());
    }


    private CheckIn createCheckInByExecutorIds(SimpleSpot spot, Executor executor, Inspect inspect) {
        CheckIn checkIn = new CheckIn();
        checkIn.setInspectId(inspect.getId());
        checkIn.setExecuteId(executor.getId());
        checkIn.setSpotId(spot.getId());
        checkIn.setSpotName(spot.getName());
        checkIn.setSpotLng(spot.getLng());
        checkIn.setSpotLat(spot.getLat());
        checkIn.setCreateAt(new Date());
        checkIn.setUserId(getCurrentUserInfo().getId());
        checkIn.setUserName(getCurrentUserInfo().getRealName());
        return checkIn;
    }

    private void batchAddCheckIns(List<CheckIn> checkIns) {
        int size = checkIns.size() / DATA_SIZE + 1;
        for (int i = 0; i < size; i++) {
            List<CheckIn> partOfAdd = checkIns.stream().limit(DATA_SIZE).collect(Collectors.toList());
            checkInDao.batchAdd(partOfAdd);
            List<CheckIn> hasInsert = checkIns.subList(0, checkIns.size() >= DATA_SIZE ? DATA_SIZE : checkIns.size());
            hasInsert.clear();
        }
    }

    private void updateStatusIfAllGiveUpOrFinish(Executor toCheck, List<Executor> allExecutor) {
        // 所有人都放弃任务了，则任务终结
        if (allExecutor.stream().allMatch(e -> e.getStatus().equals(InspectStatusEnum.GIVE_UP.getKey()))) {
            inspectDao.updateStatus(toCheck.getInspectId(), InspectStatusEnum.GIVE_UP.getKey(), toCheck.getVersion());
            return;
        }
        // 除去放弃的之外全部完成，则任务完成
        if (allExecutor.stream().filter(e -> !e.getStatus().equals(InspectStatusEnum.GIVE_UP.getKey()))
                .allMatch(e -> e.getStatus().equals(InspectStatusEnum.FINISH.getKey()))) {
            inspectDao.updateStatus(toCheck.getInspectId(), InspectStatusEnum.FINISH.getKey(), toCheck.getVersion());
        }
    }

    private Executor getExecutorIfExist(Executor executor) {
        Executor toCheck = executorDao
                .findByInspectIdAndExecutorId(executor.getInspectId(), getCurrentUserInfo().getId());
        if (toCheck == null) {
            logger.error("任务不存在, {}", executor);
            throw new BusinessException("任务不存在");
        }
        return toCheck;
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
}
