package com.bit.module.oa.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;

import com.bit.module.oa.bean.*;
import com.bit.module.oa.constant.redis.RedisConstant;
import com.bit.module.oa.dao.*;
import com.bit.module.oa.enums.*;
import com.bit.module.oa.service.InspectService;
import com.bit.module.oa.utils.ApplyNoGenerator;
import com.bit.module.oa.utils.PushUtil;
import com.bit.module.oa.utils.RedisDelayHandler;
import com.bit.module.oa.vo.inspect.*;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.utils.CheckUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Inspect的Service实现类
 *
 * @author codeGenerator
 */
@Service("inspectService")
@Transactional
public class InspectServiceImpl extends BaseService implements InspectService {

    private static final Logger logger = LoggerFactory.getLogger(InspectServiceImpl.class);

    @Autowired
    private InspectDao inspectDao;

    @Autowired
    private LogDao logDao;

    @Autowired
    private ExecutorDao executorDao;

    @Autowired
    private CheckInDao checkInDao;

    @Autowired
    private SpotDao spotDao;

    @Autowired
    private RedisDelayHandler redisDelayHandler;

    @Autowired
    private PushUtil pushUtil;

    @Autowired
    private RiskDao riskDao;


    // 一次批量插入大小
    private static final Integer DATA_SIZE = 500;

    /**
     * 根据条件查询Inspect
     *
     * @param inspectVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(InspectVO inspectVO) {
        PageHelper.startPage(inspectVO.getPageNum(), inspectVO.getPageSize());
        inspectVO.setOrder("desc");
        inspectVO.setOrderBy("id");
        List<InspectExecutorVO> list = inspectDao.findByConditionPage(inspectVO);
        PageInfo<InspectExecutorVO> pageInfo = new PageInfo<>(list);
        return new BaseVo(pageInfo);
    }

    @Override
    public BaseVo findByConditionPageForApp(InspectVO inspectVO) {
        PageHelper.startPage(inspectVO.getPageNum(), inspectVO.getPageSize());
        if (inspectVO.getQueryType() != null) {
            inspectVO.setQueryId(getCurrentUserInfo().getId());
        }
        inspectVO.setOrder("desc");
        inspectVO.setOrderBy("id");
        List<InspectExecutorVO> list = inspectDao.findByConditionPageForApp(inspectVO);
        if (InspectQueryTypeEnum.EXECUTE.getKey().equals(inspectVO.getQueryType())) {
            list.forEach(inspect -> inspect.setExecutorId(inspectVO.getQueryId()));
        }
        PageInfo<InspectExecutorVO> pageInfo = new PageInfo<>(list);
        return new BaseVo(pageInfo);
    }

    /**
     * 通过主键查询单个Inspect
     *
     * @param id
     * @return
     */
    @Override
    public InspectExecutorVO findById(Long id) {
        InspectExecutorVO executorVO = inspectDao.findById(id);
        Executor executor = executorDao.findByInspectIdAndExecutorId(id, getCurrentUserInfo().getId());
        if (executor != null) {
            executorVO.setStatus(executor.getStatus());
        }
        return executorVO;
    }

    /**
     * 保存Inspect
     *
     * @param inspect
     */
    @Override
    public void add(Inspect inspect) {
        String no = generateApplyNo(InspectTypeEnum.getByKey(inspect.getType()));
        inspect.setNo(no);
        inspect.setStatus(InspectStatusEnum.UNPUBLISHED.getKey());
        inspect.setPublisherId(getCurrentUserInfo().getId());
        inspect.setCreateAt(new Date());
        if (InspectTypeEnum.LOCUS.getKey().equals(inspect.getType()) && CollectionUtils.isNotEmpty(inspect.getSpots())) {
            checkSpot(inspect);
            setSpot(inspect);
        }

        inspectDao.add(inspect);
    }

    private void setSpot(Inspect inspect) {
        List<Long> collect = inspect.getSpots().stream().filter(s -> s.getId() != null)
                .map(SimpleSpot::getId).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(collect)) {
            Map<Long, Spot> spotMap = spotDao.findByIds(collect).stream()
                    .collect(Collectors.toMap(Spot::getId, s -> s));
            for (SimpleSpot spot : inspect.getSpots()) {
                if (spot.getId() != null) {
                    Spot toAddSpot = spotMap.get(spot.getId());
                    spot.setName(toAddSpot.getName());
                    spot.setLat(toAddSpot.getLat());
                    spot.setLng(toAddSpot.getLng());
                }
            }

        }
    }

    /**
     * 更新Inspect
     *
     * @param inspect
     */
    @Override
    public void update(Inspect inspect) {
        Inspect toUpdate = inspectDao.findById(inspect.getId());
        if (toUpdate == null) {
            logger.error("巡检单不存在, {}", inspect);
            throw new BusinessException("巡检单不存在");
        }
        if (InspectTypeEnum.LOCUS.getKey().equals(inspect.getType()) && CollectionUtils.isNotEmpty(inspect.getSpots())) {
            checkSpot(inspect);
            setSpot(inspect);
        }
        if (!InspectStatusEnum.UNPUBLISHED.getKey().equals(toUpdate.getStatus())) {
            logger.error("巡检单已发布, {}", toUpdate);
            throw new BusinessException("巡检单已发布无法更新");
        }
        inspect.setStatus(InspectStatusEnum.UNPUBLISHED.getKey());
        inspect.setVersion(toUpdate.getVersion());
        inspectDao.update(inspect);
    }

    /**
     * 删除Inspect
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        Inspect toDelete = inspectDao.findById(id);
        if (toDelete == null) {
            logger.error("巡检单不存在, {}", id);
            throw new BusinessException("巡检单不存在");
        }
        if (!InspectStatusEnum.UNPUBLISHED.getKey().equals(toDelete.getStatus())) {
            logger.error("巡检单已发布, {}", id);
            throw new BusinessException("巡检单已发布无法删除");
        }
        inspectDao.delete(id);
    }

    @Override
    public void publish(Inspect inspect) {
        // 如果尚未保存过的就新增
        if (inspect.getId() == null) {
            CheckUtil.notNull(inspect.getName());
            CheckUtil.notNull(inspect.getType());
            this.add(inspect);
        // 如果已经保存的检查是否有修改项，有就更新，没有直接发布
        } else {
            if (updateParamIfNotNull(inspect)) {
                this.update(inspect);
            }
        }

        Inspect toPublish = inspectDao.findById(inspect.getId());
        if (toPublish.getEndTime().before(new Date())) {
            logger.error("巡检单结束时间不能早于当前时间, {}", toPublish);
            throw new BusinessException("巡检单结束时间不能早于当前时间");
        }
        List<SimpleUser> simpleExecutors = toPublish.parseExecutors();
        List<SimpleSpot> simpleSpots = toPublish.parseSpots();
        if (InspectTypeEnum.LOCUS.getKey().equals(toPublish.getType()) && CollectionUtils.isNotEmpty(simpleSpots)) {
            checkSpot(toPublish);
        }

        inspectDao.publish(toPublish.getId(), InspectStatusEnum.READY.getKey(), getCurrentUserInfo().getId(), toPublish.getVersion());
        // 根据执行人批量插入巡检执行信息
        List<Executor> executors = simpleExecutors.stream().map(
                e -> createExecutorByInspect(e, toPublish)).collect(Collectors.toList());
        batchAddExecutor(executors);

        // 批量新增后批量查询executor
        Log log = Log.getLogByOperateAndType(toPublish.getId(), getCurrentUserInfo(), LogOperateEnum.INSPECT_PUBLISH, LogTypeEnum
                .INSPECT);
        logDao.add(log);

        // 发送结束任务延时队列
        redisDelayHandler.setDelayZSet(RedisConstant.INSPECT_END_ZSET, toPublish.getId(), toPublish.getEndTime());
        // 给执行人发待办
        List<Executor> executorList = executorDao.findByInspectId(toPublish.getId());
        executorList.forEach(e -> pushUtil.sendMessage(e.getId(), MessageTemplateEnum.INSPECT_PUBLISH, TargetTypeEnum.USER,
                new Long[]{e.getExecutorId()}, new String[]{getCurrentUserInfo().getRealName()},
                getCurrentUserInfo().getRealName(), e.getVersion()));
        ;
    }

    @Override
    public List<InspectTaskExport> findExportTaskInspect(Inspect inspect) {

        return inspectDao.findTaskInspect(inspect);
    }

    @Override
    public List<InspectSituationExport> findExportSituationInspect(InspectExportQO inspect) {
        return inspectDao.findSituationInspect(inspect);
    }

    @Override
    public void end(Long id) {
        Inspect toEnd = inspectDao.findById(id);
        if (toEnd == null) {
            logger.error("巡检单不存在, {}", toEnd);
            return;
        }
        if (!(InspectStatusEnum.READY.getKey().equals(toEnd.getStatus()) || InspectStatusEnum.EXECUTE.getKey()
                .equals(toEnd.getStatus()))) {
            logger.error("巡检单无需结束, {}", toEnd);
            return;
        }

        inspectDao.updateStatus(toEnd.getId(), InspectStatusEnum.END.getKey(), toEnd.getVersion());

        List<Executor> executors = executorDao.findByInspectId(toEnd.getId());

        if (CollectionUtils.isEmpty(executors)) {
            return;
        }
        List<Executor> endExecutor = executors.stream()
                .filter(executor -> InspectStatusEnum.READY.getKey().equals(executor.getStatus())
                        ||InspectStatusEnum.EXECUTE.getKey().equals(executor.getStatus()))
                .collect(Collectors.toList());

        List<CheckIn> checkIns = checkInDao.findByInspectId(toEnd.getId());
        if (CollectionUtils.isNotEmpty(checkIns)) {
            // 根据executeId分类
            Map<Long, List<CheckIn>> checkInMap = sortByExecuteId(endExecutor, checkIns);

            // 找到已完成的巡检任务
            List<Executor> finishExecutor = endExecutor.stream().filter(executor -> findFinishExecutor(executor, checkInMap))
                    .collect(Collectors.toList());

            // 将已完成的和已结束的做差集，防止已完成的更新为已结束
            endExecutor = (List<Executor>) CollectionUtils.subtract(endExecutor, finishExecutor);
            if (CollectionUtils.isNotEmpty(finishExecutor)) {
                executorDao.batchUpdateStatus(InspectStatusEnum.FINISH.getKey(), CheckInStatusEnum.NORMAL.getKey(), finishExecutor);
            }
        }
        if (CollectionUtils.isNotEmpty(endExecutor)) {
            executorDao.batchUpdateStatus(InspectStatusEnum.END.getKey(), CheckInStatusEnum.MISSING.getKey(), endExecutor);
        }
    }

    @Override
    public InspectCountVO count(Integer type, Date startAt, Date endAt) {
        Long userId = getCurrentUserInfo().getId();
        InspectCountVO inspectCountVO = new InspectCountVO();
        Integer allCount = executorDao.countAllByExecutorId(userId, startAt, endAt);
        inspectCountVO.setAllInspect(allCount);
        if (0 == allCount) {
            inspectCountVO.setCompletedInspect(0);
        } else {
            Integer completedCount = executorDao.countCompletedByExecutorId(userId, startAt, endAt);
            inspectCountVO.setCompletedInspect(completedCount);
        }
        Integer needApplicationCount = executorDao.countApplicationByExecutorId(userId, startAt, endAt);
        inspectCountVO.setApplication(needApplicationCount);
        Integer riskCount = riskDao.countByReporterId(userId, startAt, endAt);
        inspectCountVO.setRisk(riskCount);
        return inspectCountVO;
    }

    private Map<Long, List<CheckIn>> sortByExecuteId(List<Executor> endExecutor, List<CheckIn> checkIns) {
        return endExecutor.stream()
                .collect(Collectors.toMap(Executor::getId,
                        executor ->checkIns.stream().filter(c -> c.getExecuteId().equals(executor.getId()))
                                .collect(Collectors.toList())));
    }

    private boolean findFinishExecutor(Executor executor, Map<Long, List<CheckIn>> checkInMap) {
        if (InspectStatusEnum.READY.getKey().equals(executor.getStatus())) {
            return false;
        }
        // 获取巡检任务的签到list
        List<CheckIn> checkIns = checkInMap.get(executor.getId());
        // 没有签到的点
        List<CheckIn> unCheckPoints = checkIns.stream().filter(checkIn -> checkIn.getCheckInAt() == null)
                .collect(Collectors.toList());
        // 全部签到点已签到返回true
        return CollectionUtils.isEmpty(unCheckPoints);
    }


    private Executor createExecutorByInspect(SimpleUser e, Inspect toPublish) {
        Executor executor = new Executor();
        executor.setInspectId(toPublish.getId());
        executor.setInspectNo(toPublish.getNo());
        executor.setInspectName(toPublish.getName());
        executor.setInspectType(toPublish.getType());
        executor.setInspectStartTime(toPublish.getStartTime());
        executor.setInspectEndTime(toPublish.getEndTime());
        executor.setInspectDeps(toPublish.getDeps());
        executor.setExecutorId(e.getId());
        executor.setCreateAt(new Date());
        executor.setStatus(InspectStatusEnum.READY.getKey());
        return executor;
    }

    private boolean updateParamIfNotNull(Inspect inspect) {
        return StringUtils.isNotEmpty(inspect.getName()) || inspect.getType() != null || inspect.getStartTime() != null
                || inspect.getEndTime() != null || CollectionUtils.isNotEmpty(inspect.getDeps())
                || CollectionUtils.isNotEmpty(inspect.getExecutors()) || StringUtils.isNotEmpty(inspect.getContent())
                || StringUtils.isNotEmpty(inspect.getAttactIds()) || inspect.getCreateAt() != null
                || CollectionUtils.isNotEmpty(inspect.getSpots());
    }

    /**
     * 生成巡检报告单
     * 规则 : 拼音首字母-年月日-当日派单号
     */
    private String generateApplyNo(InspectTypeEnum inspectTypeEnum) {
        String lastApplyNo = inspectDao.findNoByPrefix(inspectTypeEnum.getKey());
        return ApplyNoGenerator.generateApplyNo(lastApplyNo, inspectTypeEnum.getPrefix());
    }


    private void batchAddExecutor(List<Executor> executors) {
        int size = executors.size() / DATA_SIZE + 1;
        for (int i = 0; i < size; i++) {
            List<Executor> partOfAdd = executors.stream().limit(DATA_SIZE).collect(Collectors.toList());
            executorDao.batchAdd(partOfAdd);
            List<Executor> hasInsert = executors.subList(0, executors.size() >= DATA_SIZE ? DATA_SIZE : executors.size());
            hasInsert.clear();
        }
    }


    private void checkSpot(Inspect inspect) {
        if (CollectionUtils.isEmpty(inspect.getSpots())) {
            logger.error("签到点不能为空, {}", inspect.getSpots());
            throw new BusinessException("签到点不能为空");
        }
        List<Spot> toCheckSpot = spotDao
                .findByIds(inspect.getSpots().stream().map(SimpleSpot::getId).collect(Collectors.toList()));
        if (toCheckSpot.stream().anyMatch(spot -> SpotStatusEnum.DISABLE.getKey().equals(spot.getStatus()))) {
            logger.error("签到点未启用, {}", inspect);
            throw new BusinessException("签到点未启用");
        }
    }


    @AllArgsConstructor
    enum InspectQueryTypeEnum {
        PUBLISH(1, "我发布"), EXECUTE(2, "我执行"), PUBLISHED(3, "已发布的任务");

        private Integer key;
        private String description;

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
