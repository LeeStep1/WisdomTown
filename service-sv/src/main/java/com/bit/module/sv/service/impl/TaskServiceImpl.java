package com.bit.module.sv.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.IdName;
import com.bit.module.sv.bean.Plan;
import com.bit.module.sv.bean.RectificationNotice;
import com.bit.module.sv.bean.Task;
import com.bit.module.sv.dao.PlanDao;
import com.bit.module.sv.dao.RectificationNoticeDao;
import com.bit.module.sv.dao.TaskDao;
import com.bit.module.sv.dao.UnitDao;
import com.bit.module.sv.enums.ResultEnum;
import com.bit.module.sv.enums.TaskStatusEnum;
import com.bit.module.sv.enums.TaskTypeEnum;
import com.bit.module.sv.service.TaskService;
import com.bit.module.sv.utils.DateUtils;
import com.bit.module.sv.utils.StringUtils;
import com.bit.module.sv.vo.*;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.support.PushUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service("taskService")
@Transactional
public class TaskServiceImpl extends BaseService implements TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    @Autowired
    private UnitDao unitDao;

    @Autowired
    private PlanDao planDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private RectificationNoticeDao rectificationNoticeDao;

    @Autowired
    private PushUtil pushUtil;

    @Value("${task.expire.days}")
    private Integer taskExpireDays;

    @Override
    public BaseVo deleteById(Long id) {
        Task toGet = taskDao.selectById(id);
        if (toGet == null) {
            throw new BusinessException("巡检任务不存在");
        }
        if (toGet.getStatus() != TaskStatusEnum.PENDING_CHECK.value) {
            throw new BusinessException("只能删除待巡检的任务");
        }
        int deleteNum = taskDao.deleteById(id);
        if (deleteNum > 0) {
            // 总任务数 -1
            planDao.increaseTotalTaskNum(toGet.getPlanId(), -1);
        }
        // 需要判断改计划是否还有子任务，如果没有子任务，直接删除计划
        List<Task> taskList = taskDao.selectByPlanId(toGet.getPlanId());
        if (CollectionUtils.isNotEmpty(taskList)) {
            return new BaseVo();
        }
        planDao.deleteById(toGet.getPlanId());
        return new BaseVo();
    }

    @Override
    public BaseVo modifyTask(TaskVO taskVO) {
        Task toGet = taskDao.selectById(taskVO.getId());
        if (toGet == null) {
            throw new BusinessException("巡检任务不存在");
        }
        if (toGet.getStatus() != TaskStatusEnum.PENDING_CHECK.value) {
            throw new BusinessException("只能编辑待巡检的任务");
        }
        Task toUpdate = new Task();
        toUpdate.setId(taskVO.getId());
        toUpdate.setStartAt(taskVO.getStartAt());
        toUpdate.setEndAt(taskVO.getEndAt());
        toUpdate.setItems(taskVO.getItems());
        toUpdate.setInspectors(taskVO.getInspectors());
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        taskDao.updateByIdSelective(toUpdate);
        if (CollectionUtils.isEmpty(taskVO.getInspectors())) {
            return new BaseVo();
        }
        Set<Long> newUserIds = taskVO.getInspectors().stream().map(IdName::getId).collect(Collectors.toSet());
        List<IdName> oldIdNameList = new Gson().fromJson(
                new Gson().toJson(toGet.getInspectors()), new TypeToken<List<IdName>>() {}.getType());
        Set<Long> oldUserIds = oldIdNameList.stream().map(IdName::getId).collect(Collectors.toSet());
        if (!oldUserIds.addAll(newUserIds)) {
            return new BaseVo();
        }
        logger.info("巡检任务({})的巡检人员有变更，需要推送新任务提醒...", toGet.getId());
        toGet = taskDao.selectByIdAndSource(toGet.getId(), toGet.getSource());
        pushNewTask(toGet, TaskTypeEnum.getByValue(toGet.getType()).desc);
        return new BaseVo();
    }

    @Override
    public BaseVo listTasks(TaskVO taskVO) {
        PageHelper.startPage(taskVO.getPageNum(), taskVO.getPageSize());
        if (StringUtils.isBlank(taskVO.getOrderBy())) {
            taskVO.setOrderBy("create_at");
        }
        if (StringUtils.isBlank(taskVO.getOrder())) {
            taskVO.setOrder("desc");
        }
        List<Task> list = taskDao.findByConditionPage(taskVO);
        PageInfo<Task> pageInfo = new PageInfo<Task>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public BaseVo selectByIdWithAll(Long id, Integer source) {
        Task task = taskDao.selectByIdAndSource(id, source);
        if (task == null) {
            logger.info("数据来源({}),没有查到对应的任务({})详情", source, id);
            return new BaseVo();
        }
        RectificationNotice rectificationNotice = rectificationNoticeDao.selectByTaskId(id);
        ReviewChecklist reviewChecklist = taskDao.selectByRefId(id);
        Map<String, Object> resultMap = new HashMap<>(3);
        resultMap.put("task", task);
        resultMap.put("rectificationNotice", rectificationNotice);
        resultMap.put("reviewChecklist", reviewChecklist);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(resultMap);
        return baseVo;
    }

    @Override
    public BaseVo startTask(Long id) {
        Task toGet = taskDao.selectById(id);
        if (toGet == null) {
            throw new BusinessException("巡检任务不存在");
        }
        if (toGet.getStatus() != TaskStatusEnum.PENDING_CHECK.value) {
            throw new BusinessException("巡检任务正在进行中");
        }
        Task toUpdateTask = new Task();
        toUpdateTask.setId(id);
        toUpdateTask.setStatus(TaskStatusEnum.CHECKING.value);
        // 默认设置当前时间为实际巡检开始时间
        toUpdateTask.setCheckStartAt(DateUtils.getCurrentDate());

        toUpdateTask.setUpdateAt(toUpdateTask.getCheckStartAt());
        taskDao.updateByIdSelective(toUpdateTask);

        Plan toUpdatePlan = new Plan();
        toUpdatePlan.setId(toGet.getPlanId());
        toUpdatePlan.setStatus(TaskStatusEnum.CHECKING.value);
        toUpdatePlan.setUpdateAt(DateUtils.getCurrentDate());
        planDao.updateByIdSelective(toUpdatePlan);
        return new BaseVo();
    }

    @Override
    public BaseVo taskStatistics(Boolean personal, Integer appId) {
        Long userId = null;
        if (personal ){
            UserInfo userInfo = getCurrentUserInfo();
            if ( userInfo == null) {
                throw new BusinessException("用户不存在，请重新登录");
            }
            userId = userInfo.getId();
        }
        List<StatisticsVO> statisticsVOList = taskDao.countBySourceAndUserIdAndDate(appId, userId,
                DateUtils.getMonthStart(DateUtils.getCurrentDate()), DateUtils.getMonthEnd(DateUtils.getCurrentDate()));
        Long pendingCheck = 0L;
        Long checking = 0L;
        Long completed = 0L;
        for (StatisticsVO vo : statisticsVOList) {
            if (vo.getStatus() == TaskStatusEnum.PENDING_CHECK.value) {
                pendingCheck += vo.getNum();
                continue;
            }
            if (vo.getStatus() == TaskStatusEnum.COMPLETED.value) {
                completed += vo.getNum();
                continue;
            }
            checking += vo.getNum();
        }
        Map<String, Long> result = new HashMap<>(3);
        result.put(TaskStatusEnum.PENDING_CHECK.name(), pendingCheck);
        result.put(TaskStatusEnum.CHECKING.name(), checking);
        result.put(TaskStatusEnum.COMPLETED.name(), completed);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(result);
        return baseVo;
    }

    @Override
    public BaseVo incrementTasks(IncrementalRequest request) {
        if (request.getUnitId() == null) {
            UserInfo userInfo = getCurrentUserInfo();
            if (userInfo == null) {
                throw new BusinessException("用户不存在，请重新登录");
            }
            request.setUserId(userInfo.getId());
        }
        List<Task> tasks = taskDao.incrementTasks(request);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(tasks);
        return baseVo;
    }

    @Override
    public BaseVo modifyChecklist(Checklist checklist) {
        Task toGet = taskDao.selectById(checklist.getId());
        if (toGet == null) {
            throw new BusinessException("巡检任务不存在");
        }
        Task toUpdate = new Task();
        BeanUtils.copyProperties(checklist, toUpdate);
        // 巡检结果是通过的，则清空违反条例
        if (toUpdate.getResult() != null && toUpdate.getResult() == ResultEnum.PASS.value) {
            if (toUpdate.getDescription() == null) {
                toUpdate.setDescription(new RectificationNotice.DescriptionVO());
            }
            toUpdate.setViolationRegulations(new ArrayList<>());
        }
        /*toUpdate.setItems(null);
        setItems(toUpdate, toGet.getItems(), checklist.getItems());*/
        toUpdate.setStatus(null);
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        taskDao.updateByIdSelective(toUpdate);
        // result 没变更直接返回了
        if (checklist.getResult() == null) {
            return new BaseVo();
        }
        toUpdate = new Task();
        toUpdate.setId(toGet.getId());
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        if (toGet.getResult() == null) {
            // 第一次填写巡检结果，并且是通过的
            if (checklist.getResult() == ResultEnum.PASS.value) {
                logger.info("任务({})巡检结果通过，需要修改计划({})的任务完成数量...", toGet.getId(), toGet.getPlanId());
                planDao.increaseCompletedTaskNum(toGet.getPlanId(), 1);
                toUpdate.setStatus(TaskStatusEnum.COMPLETED.value);
                taskDao.updateByIdSelective(toUpdate);
            }
            return new BaseVo();
        }
        // 巡检结果没有变化，直接返回
        if (toGet.getResult() == checklist.getResult()) {
            return new BaseVo();
        }
        logger.info("任务({})巡检结果有变更，需要修改任务状态及计划({})的任务完成数量...", toGet.getId(), toGet.getPlanId());
        toUpdate = new Task();
        toUpdate.setId(toGet.getId());
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        // 巡检结果有变更
        int num = 0;
        // 结果从通过变成不通过
        if (checklist.getResult() == ResultEnum.FAIL.value) {
            // 需要确定当前任务有没有复检单
            ReviewChecklist reviewChecklist = taskDao.selectByRefId(toGet.getId());
            if (reviewChecklist == null) {
                RectificationNotice rectificationNotice = rectificationNoticeDao.selectByTaskId(toGet.getId());
                if (rectificationNotice == null) {
                    toUpdate.setStatus(TaskStatusEnum.CHECKING.value);
                } else {
                    toUpdate.setStatus(TaskStatusEnum.PENDING_RECTIFY.value);
                }
            } else {
                toUpdate.setStatus(TaskStatusEnum.PENDING_REVIEW.value);
            }
            num = -1;
        } else {
            // 删除整改通知书
            logger.info("删除任务({})下的整改通知书...", toGet.getId());
            int deleteRectificationNum = rectificationNoticeDao.deleteByTaskId(toGet.getId());
            if (deleteRectificationNum > 0) {
                // 单位不良记录 -1
                logger.info("单位不良记录数 -1 ");
                unitDao.increaseInjuriaCount(toGet.getUnitId(), -1);
            }
            logger.info("删除任务({})下的复查单...", toGet.getId());
            // 删除复检单
            taskDao.deleteByRefId(toGet.getId());
            // 任务本身就已经完成，无需再更新状态及计划中完成任务的数量
            if (toGet.getStatus() == TaskStatusEnum.COMPLETED.value) {
                return new BaseVo();
            }
            toUpdate.setStatus(TaskStatusEnum.COMPLETED.value);
            num = 1;
        }
        logger.info("修改任务({})状态为：{}", toGet.getId(), toUpdate.getStatus());
        taskDao.updateByIdSelective(toUpdate);

        logger.info("修改计划({})的任务完成数量：{}", toGet.getPlanId(), num);
        planDao.increaseCompletedTaskNum(toGet.getPlanId(), num);
        return new BaseVo();
    }

    @Override
    public BaseVo addReviewChecklist(ReviewChecklist reviewChecklist) {
        Task toGet = taskDao.selectById(reviewChecklist.getRefId());
        if (toGet == null) {
            throw new BusinessException("巡检任务不存在");
        }
        Task task = new Task();
        BeanUtils.copyProperties(reviewChecklist, task);
        task.setCreateAt(DateUtils.getCurrentDate());
        task.setUpdateAt(task.getCreateAt());
        taskDao.insertSelective(task);
        // 修改任务执行状态为待复查
        logger.info("设置复查计划完成，即将修改任务执行状态为‘待复查’...");
        Task toUpdate = new Task();
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        toUpdate.setStatus(TaskStatusEnum.PENDING_REVIEW.value);
        toUpdate.setId(task.getRefId());
        taskDao.updateByIdSelective(toUpdate);
        logger.info("新增复查任务({})完成，开始推送新复查任务提醒...", task.getId());
        toGet = taskDao.selectByIdAndSource(toGet.getId(), toGet.getSource());
        task.setSource(toGet.getSource());
        task.setUnitName(toGet.getUnitName());
        pushReviewTask(task);
        return new BaseVo();
    }

    @Override
    public BaseVo selectByRefId(Long taskId) {
        ReviewChecklist reviewChecklist = taskDao.selectByRefId(taskId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(reviewChecklist);
        return baseVo;
    }

    @Override
    public BaseVo modifyReviewChecklist(ReviewChecklist reviewChecklist, Integer source) {
        Task toGetReview = taskDao.selectById(reviewChecklist.getId());
        if (toGetReview == null) {
            throw new BusinessException("巡检复查单不存在");
        }
        Task task = taskDao.selectByIdAndSource(toGetReview.getRefId(), source);
        if (task == null) {
            throw new BusinessException("巡检任务不存在");
        }
        Task toUpdate = new Task();
        BeanUtils.copyProperties(reviewChecklist, toUpdate);
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        // 修改复查计划
        if (task.getStatus() == TaskStatusEnum.PENDING_REVIEW.value && toUpdate.getResult() == null) {
            taskDao.updateByIdSelective(toUpdate);
            Set<Long> newUserIds = reviewChecklist.getInspectors().stream().map(IdName::getId).collect(Collectors.toSet());
            List<IdName> oldIdNameList = new Gson().fromJson(
                    new Gson().toJson(toGetReview.getInspectors()), new TypeToken<List<IdName>>() {}.getType());
            Set<Long> oldUserIds = oldIdNameList.stream().map(IdName::getId).collect(Collectors.toSet());
            if (!oldUserIds.addAll(newUserIds)) {
                return new BaseVo();
            }
            logger.info("修改复查任务({})完成并且复查人员有变更，需要推送新复查任务提醒...", toUpdate.getId());
            toUpdate.setRefId(task.getId());
            toUpdate.setSource(task.getSource());
            toUpdate.setUnitName(task.getUnitName());
            pushReviewTask(toUpdate);
            return new BaseVo();
        }

        if (task.getStatus() == TaskStatusEnum.PENDING_REVIEW.value && toUpdate.getResult() != null) {
            logger.info("第一次填写复查结果，需要更新任务状态为已完成，并将计划中已完成任务数量 +1");
            // 更新所属任务的执行状态为已完成
            Task toUpdateStatus = new Task();
            toUpdateStatus.setId(task.getId());
            toUpdateStatus.setStatus(TaskStatusEnum.COMPLETED.value);
            toUpdateStatus.setUpdateAt(DateUtils.getCurrentDate());
            taskDao.updateByIdSelective(toUpdateStatus);
            // 更新计划的已完成任务数量 +1
            planDao.increaseCompletedTaskNum(task.getPlanId(), 1);
        }

        // 需要筛选修改的项目
        /*toUpdate.setItems(null);
        setItems(toUpdate, toGetReview.getItems(), reviewChecklist.getItems());*/
        // 结果通过的，则清空
        if (toUpdate.getResult() != null && toUpdate.getResult() == ResultEnum.PASS.value) {
            if (toUpdate.getDescription() == null) {
                toUpdate.setDescription(new RectificationNotice.DescriptionVO());
            }
            toUpdate.setViolationRegulations(new ArrayList<>());
        }
        taskDao.updateByIdSelective(toUpdate);
        return new BaseVo();
    }

    @Override
    public BaseVo selectById(Long taskId) {
        Task task = taskDao.selectById(taskId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(task);
        return baseVo;
    }

    @Override
    public BaseVo exportById(Long id) {
        ExportVO exportVO = taskDao.exportById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(exportVO);
        return baseVo;
    }

    @Override
    public BaseVo dailyPushExpireTask() {
        Date startAt = DateUtils.getDayStartTime(DateUtils.getCurrentDate());
        Date endAt = DateUtils.addTime(startAt, taskExpireDays, Calendar.DAY_OF_MONTH);
        List<Task> taskList = taskDao.findTaskWithOutUCByEndAt(startAt, endAt);
        List<Task> ucTaskList = taskDao.findTaskOnlyUCByEndAt(startAt, endAt);
        if (CollectionUtils.isEmpty(taskList) && CollectionUtils.isEmpty(ucTaskList)) {
            logger.info("没有3天内到期的巡检任务...");
            return new BaseVo();
        }
        if (CollectionUtils.isNotEmpty(ucTaskList)) {
            taskList.addAll(ucTaskList);
        }
        taskList.forEach(task -> pushTaskExpire(task));
        return new BaseVo();
    }

    @Override
    public BaseVo dailyPushExpireReviewTask() {
        Date startAt = DateUtils.getDayStartTime(DateUtils.getCurrentDate());
        Date endAt = DateUtils.addTime(startAt, taskExpireDays, Calendar.DAY_OF_MONTH);
        List<Task> taskList = taskDao.findReviewTaskWithOutUCByEndAt(startAt, endAt);
        List<Task> ucTaskList = taskDao.findReviewTaskOnlyUCByEndAt(startAt, endAt);
        if (CollectionUtils.isEmpty(taskList) && CollectionUtils.isEmpty(ucTaskList)) {
            logger.info("没有3天内到期的复查任务...");
            return new BaseVo();
        }
        if (CollectionUtils.isNotEmpty(ucTaskList)) {
            taskList.addAll(ucTaskList);
        }
        taskList.forEach(task -> pushReviewTaskExpire(task));
        return new BaseVo();
    }

    /**
     * 新增任务消息推送
     * @param task
     * @param taskType
     */
    private void pushNewTask(Task task, String taskType) {
        Integer source = task.getSource();
        if (!StringUtils.checkSourceValid(source)) {
            logger.info("数据来源({})不明确，不需要推送.", source);
            return;
        }
        String creator = getCurrentUserInfo().getRealName();
        List<IdName> idNameList = new Gson().fromJson(
                new Gson().toJson(task.getInspectors()), new TypeToken<List<IdName>>(){}.getType());
        Long[] userIds = idNameList.stream().map(IdName::getId).collect(Collectors.toSet()).toArray(new Long[]{});
        final String startAt = DateUtils.date2String(task.getStartAt(), DateUtils.DatePattern.YYYYMMDD.getValue());
        final String endAt = DateUtils.date2String(task.getEndAt(), DateUtils.DatePattern.YYYYMMDD.getValue());
        MessageTemplateEnum templateEnum = MessageTemplateEnum.EP_TASK_ASSIGN;
        switch (source) {
            case 3:
                templateEnum = MessageTemplateEnum.SV_TASK_ASSIGN;
                break;
            case 6:
                templateEnum = MessageTemplateEnum.UC_TASK_ASSIGN;
                break;
            default:
                break;
        }
        List<String> params = new ArrayList<>();
        params.add(startAt);
        params.add(endAt);
        params.add(task.getUnitName());
        params.add(taskType);
        pushUtil.sendMessage(
                task.getId(), templateEnum, TargetTypeEnum.USER, userIds, params.toArray(new String[]{}), creator);
    }

    /**
     * 新增复查任务消息推送
     * @param task
     */
    private void pushReviewTask(Task task) {
        Integer source = task.getSource();
        if (!StringUtils.checkSourceValid(source)) {
            logger.info("数据来源({})不明确，不需要推送.", source);
            return;
        }
        String creator = getCurrentUserInfo().getRealName();
        Long[] userIds = task.getInspectors().stream().map(IdName::getId).collect(Collectors.toSet()).toArray(new Long[]{});
        final String startAt = DateUtils.date2String(task.getStartAt(), DateUtils.DatePattern.YYYYMMDD.getValue());
        final String endAt = DateUtils.date2String(task.getEndAt(), DateUtils.DatePattern.YYYYMMDD.getValue());
        MessageTemplateEnum templateEnum = MessageTemplateEnum.EP_REVIEW_TASK_ASSIGN;
        switch (source) {
            case 3:
                templateEnum = MessageTemplateEnum.SV_REVIEW_TASK_ASSIGN;
                break;
            case 6:
                templateEnum = MessageTemplateEnum.UC_REVIEW_TASK_ASSIGN;
                break;
            default:
                break;
        }
        List<String> params = new ArrayList<>();
        params.add(startAt);
        params.add(endAt);
        params.add(task.getUnitName());
        pushUtil.sendMessage(
                task.getRefId(), templateEnum, TargetTypeEnum.USER, userIds, params.toArray(new String[]{}), creator);
    }

    /**
     * 巡检任务到期推送
     * @param task
     */
    private void pushTaskExpire(Task task) {
        Integer source = task.getSource();
        if (!StringUtils.checkSourceValid(source)) {
            logger.info("数据来源({})不明确，不需要推送.", source);
            return;
        }
        List<IdName> idNameList = new Gson().fromJson(
                new Gson().toJson(task.getInspectors()), new TypeToken<List<IdName>>(){}.getType());
        Long[] userIds = idNameList.stream().map(IdName::getId).collect(Collectors.toSet()).toArray(new Long[]{});

        long count = DateUtils.daysBetween(
                DateUtils.getDayStartTime(DateUtils.getCurrentDate()), DateUtils.getDayStartTime(task.getEndAt()));

        MessageTemplateEnum templateEnum = MessageTemplateEnum.EP_TASK_EXPIRE;
        switch (source) {
            case 3:
                templateEnum = MessageTemplateEnum.SV_TASK_EXPIRE;
                break;
            case 6:
                templateEnum = MessageTemplateEnum.UC_TASK_EXPIRE;
                break;
            default:
                break;
        }
        String taskType = TaskTypeEnum.getByValue(task.getType()).desc;
        List<String> params = new ArrayList<>();
        params.add(task.getUnitName());
        params.add(taskType);
        params.add(count + 1 + "");
        pushUtil.sendMessage(
                task.getId(), templateEnum, TargetTypeEnum.USER, userIds, params.toArray(new String[]{}), "定时任务");
    }

    /**
     * 复查任务到期推送
     * @param task
     */
    private void pushReviewTaskExpire(Task task) {
        Integer source = task.getSource();
        if (!StringUtils.checkSourceValid(source)) {
            logger.info("数据来源({})不明确，不需要推送.", source);
            return;
        }
        List<IdName> idNameList = new Gson().fromJson(
                new Gson().toJson(task.getInspectors()), new TypeToken<List<IdName>>(){}.getType());
        Long[] userIds = idNameList.stream().map(IdName::getId).collect(Collectors.toSet()).toArray(new Long[]{});

        long count = DateUtils.daysBetween(
                DateUtils.getDayStartTime(DateUtils.getCurrentDate()), DateUtils.getDayStartTime(task.getEndAt()));

        MessageTemplateEnum templateEnum = MessageTemplateEnum.EP_REVIEW_TASK_EXPIRE;
        switch (source) {
            case 3:
                templateEnum = MessageTemplateEnum.SV_REVIEW_TASK_EXPIRE;
                break;
            case 6:
                templateEnum = MessageTemplateEnum.UC_REVIEW_TASK_EXPIRE;
                break;
            default:
                break;
        }
        List<String> params = new ArrayList<>();
        params.add(task.getUnitName());
        params.add(count + 1 + "");
        pushUtil.sendMessage(
                task.getId(), templateEnum, TargetTypeEnum.USER, userIds, params.toArray(new String[]{}), "定时任务");
    }

}
