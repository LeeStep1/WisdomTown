package com.bit.module.oa.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;

import com.bit.module.oa.bean.Schedule;
import com.bit.module.oa.bean.Task;
import com.bit.module.oa.bean.TaskFeedback;
import com.bit.module.oa.bean.TaskSubset;
import com.bit.module.oa.dao.TaskDao;
import com.bit.module.oa.feign.UserServiceFeign;
import com.bit.module.oa.service.TaskService;
import com.bit.module.oa.utils.PushUtil;
import com.bit.module.oa.vo.task.FullTask;
import com.bit.module.oa.vo.task.SimpleTask;
import com.bit.module.oa.vo.task.TaskParam;
import com.bit.module.oa.vo.task.TaskQuery;
import com.bit.core.utils.CacheUtil;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @autor xiaoyu.fang
 * @date 2019/2/13 16:46
 */
@Service("taskService")
public class TaskServiceImpl extends BaseService implements TaskService {

    private static final String TASK_NO = "TASK_NO_";

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UserServiceFeign userServiceFeign;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private PushUtil pushUtil;

    @Override
    @Transactional
    public void add(TaskParam taskParam) {
        Task task = buildTask(taskParam);
        taskDao.add(task);
        // 新增分配人、执行人、抄送人
        if (taskParam.getAssignerId() != null) {
            TaskSubset taskSubset = buildTaskSubset(task);
            taskSubset.setAssignerId(taskParam.getAssignerId());
            taskDao.createAssigner(taskSubset);
        }
        if (task.getExecutor() != null && task.getExecutor().size() > 0) {
            List<TaskSubset> executors = new ArrayList<>();
            task.getExecutor().parallelStream().forEach(consumer -> {
                TaskSubset taskSubset = buildTaskSubset(task);
                taskSubset.setExecutorId(consumer.getId());
                taskSubset.setOwnerName(consumer.getName());
                executors.add(taskSubset);
            });
            taskDao.batchCreateExecutor(executors);
        }
        if (task.getCc() != null && task.getCc().size() > 0) {
            List<TaskSubset> ccs = new ArrayList<>();
            task.getCc().parallelStream().forEach(consumer -> {
                TaskSubset taskSubset = buildTaskSubset(task);
                taskSubset.setCcId(consumer.getId());
                taskSubset.setOwnerName(consumer.getName());
                ccs.add(taskSubset);
            });
            taskDao.batchCreateCc(ccs);
        }

        FullTask fullTask = this.findById(task.getId());
        Set<Long> userIds = new HashSet<>();
        String name = getCurrentUserInfo().getRealName();
        // 总任务
        if (0L == fullTask.getSuperiorId()) {
            // 发给抄送人
            getCustomerInFullTask(userIds, fullTask.getCc());
            Long[] user = userIds.toArray(new Long[]{});
            pushUtil.sendMessage(fullTask.getId(), MessageTemplateEnum.TASK_CREATE_MESSAGE, TargetTypeEnum.USER, user, new String[]{name}, name);
            userIds.clear();
            // 发给执行人、分配人
            userIds.add(fullTask.getAssignerId());
            getCustomerInFullTask(userIds, fullTask.getExecutor());
            user = userIds.toArray(new Long[]{});
            pushUtil.sendMessage(fullTask.getId(), MessageTemplateEnum.TASK_CREATE, TargetTypeEnum.USER, user, new String[]{name}, name);
        } else { // 分解的任务
            userIds.add(fullTask.getTaskPrincipalId());
            getCustomerInFullTask(userIds, fullTask.getCc());
            Long[] user = userIds.toArray(new Long[]{});
            pushUtil.sendMessage(fullTask.getId(), MessageTemplateEnum.TASK_ASSIGN_MESSAGE, TargetTypeEnum.USER, user, new String[]{name}, name);
            userIds.clear();
            getCustomerInFullTask(userIds, fullTask.getExecutor());
            user = userIds.toArray(new Long[]{});
            pushUtil.sendMessage(fullTask.getId(), MessageTemplateEnum.TASK_ASSIGN, TargetTypeEnum.USER, user, new String[]{name}, name);
        }
    }

    @Override
    public BaseVo findPrincipalByConditionPage(TaskQuery taskQuery, Boolean isPage) {
        Long userId = getCurrentUserInfo().getId();
        taskQuery.setPrincipalId(userId);
        // 回填数据状态
        buildTaskStatus(taskQuery);
        PageHelper.startPage(taskQuery.getPageNum(), taskQuery.getPageSize()).setOrderByOnly(isPage);
        List<SimpleTask> simpleTasks = taskDao.findByConditionPage(taskQuery);
        // 过期状态
        judgeTaskStatus(simpleTasks, false);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(isPage ? simpleTasks : new PageInfo<>(simpleTasks));
        return baseVo;
    }

    @Override
    public List<SimpleTask> findAssignerChildren(Long superiorId) {
        Long userId = getCurrentUserInfo().getId();
        return taskDao.findAssignerChildren(userId, superiorId);
    }

    @Override
    public BaseVo findAssignerByConditionPage(TaskQuery taskQuery, Boolean isPage) {
        BaseVo baseVo = new BaseVo();
        Long userId = getCurrentUserInfo().getId();
        taskQuery.setOwnerId(userId);
        // 回填数据状态
        buildTaskStatus(taskQuery);
        PageHelper.startPage(taskQuery.getPageNum(), taskQuery.getPageSize()).setOrderByOnly(isPage);
        List<SimpleTask> simpleTasks = taskDao.findAssignerByConditionPage(taskQuery);
        judgeTaskStatus(simpleTasks, true);
        hasOwnerAssiner(simpleTasks, userId);
        baseVo.setData(isPage ? simpleTasks : new PageInfo<>(simpleTasks));
        return baseVo;
    }

    private void hasOwnerAssiner(List<SimpleTask> simpleTasks, Long userId) {
        simpleTasks.parallelStream().forEach(simpleTask -> {
            int result = taskDao.countAssignerChildren(userId, simpleTask.getId());
            if (result == 0) {
                simpleTask.setSuperiorId(0L);
            }
        });
    }

    @Override
    public BaseVo findByCcConditionPage(TaskQuery taskQuery, Boolean isPage) {
        BaseVo baseVo = new BaseVo();
        Long userId = getCurrentUserInfo().getId();
        taskQuery.setOwnerId(userId);
        // 回填数据状态
        buildTaskStatus(taskQuery);
        PageHelper.startPage(taskQuery.getPageNum(), taskQuery.getPageSize()).setOrderByOnly(isPage);
        List<SimpleTask> simpleTasks = taskDao.findCcByConditionPage(taskQuery);
        judgeTaskStatus(simpleTasks, false);
        baseVo.setData(isPage ? simpleTasks : new PageInfo<>(simpleTasks));
        return baseVo;
    }

    @Override
    public BaseVo findByExecutorConditionPage(TaskQuery taskQuery, Boolean isPage) {
        Long userId = getCurrentUserInfo().getId();
        taskQuery.setOwnerId(userId);
        PageHelper.startPage(taskQuery.getPageNum(), taskQuery.getPageSize()).setOrderByOnly(isPage);
        // 回填数据状态
        buildTaskStatus(taskQuery);
        List<SimpleTask> simpleTasks = taskDao.findExecutorByConditionPage(taskQuery);
        judgeTaskStatus(simpleTasks, true);
        hasOwnerExecutor(simpleTasks, userId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(isPage ? simpleTasks : new PageInfo<>(simpleTasks));
        return baseVo;
    }

    private void hasOwnerExecutor(List<SimpleTask> simpleTasks, Long userId) {
        simpleTasks.parallelStream().forEach(simpleTask -> {
            int result = taskDao.countExecutorChildren(userId, simpleTask.getId());
            if (result == 0) {
                simpleTask.setSuperiorId(0L);
            }
        });
    }

    @Override
    public List<SimpleTask> findExecutorChildren(Long superiorId) {
        Long userId = getCurrentUserInfo().getId();
        return taskDao.findExecutorChildren(userId, superiorId);
    }

    @Override
    public FullTask findById(Long id) {
        Long userId = getCurrentUserInfo().getId();
        FullTask fullTask = taskDao.findById(id);
        if (fullTask != null) {
            if (fullTask.getTaskStatus() == TaskStatus.ING.value) {
                if (fullTask.getTaskEndAt().before(new Date())) {
                    fullTask.setTaskStatus(TaskStatus.FINISH.value);
                }
            }
            if (fullTask.getTaskPrincipalId().equals(userId)) {
                fullTask.setUserIdentity(TaskTypeEnum.PRINCIPAL.value);
                return fullTask;
            }
            if (fullTask.getAssignerId() != null && fullTask.getAssignerId().equals(userId)) {
                fullTask.setUserIdentity(TaskTypeEnum.ASSIGNER.value);
                return fullTask;
            }
            if (fullTask.getExecutor() != null && fullTask.getExecutor().size() > 0) {
                JSONArray executors = JSONArray.parseArray(JSON.toJSONString(fullTask.getExecutor()));
                for (Iterator iterator = executors.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    Long executorId = jsonObject.getLong("id");
                    if (executorId.equals(userId)) {
                        fullTask.setUserIdentity(TaskTypeEnum.EXECUTOR.value);
                        return fullTask;
                    }
                }
            }
            if (fullTask.getCc() != null && fullTask.getCc().size() > 0) {
                JSONArray ccs = JSONArray.parseArray(JSON.toJSONString(fullTask.getCc()));
                for (Iterator iterator = ccs.iterator(); iterator.hasNext(); ) {
                    JSONObject jsonObject = (JSONObject) iterator.next();
                    Long ccId = jsonObject.getLong("id");
                    if (ccId.equals(userId)) {
                        fullTask.setUserIdentity(TaskTypeEnum.EXECUTOR.value);
                        return fullTask;
                    }
                }
            }
            fullTask.setUserIdentity(TaskTypeEnum.DEFAULT.value);
        }
        return fullTask;
    }

    @Override
    public BaseVo findTaskChildrenByTaskId(TaskQuery taskQuery) {
        TaskQuery query = new TaskQuery();
        BaseVo baseVo = new BaseVo();
        FullTask fullTask = taskDao.findById(taskQuery.getTaskId());
        Long userId = getCurrentUserInfo().getId();
        // 如果主任务是  负责人或分配人 则返回所有的子任务
        if (fullTask != null) {
            if (fullTask.getTaskPrincipalId().equals(userId) || (fullTask.getAssignerId() != null && fullTask.getAssignerId().equals(userId))) {
                // 如果是 执行人 则返回自己执行的任务
                if (taskQuery.getTaskId() != null && taskQuery.getTaskId() != 0L) {
                    query.setSuperiorId(taskQuery.getTaskId());
                    PageHelper.startPage(taskQuery.getPageNum(), taskQuery.getPageSize());
                    List<SimpleTask> simpleTasks = taskDao.findByConditionPage(query);
                    judgeTaskStatus(simpleTasks, false);
                    PageInfo<SimpleTask> pageInfo = new PageInfo<>(simpleTasks);
                    baseVo.setData(pageInfo);
                }
            } else {
                // 如果是 执行人 则返回自己执行的任务
                query.setOwnerId(userId);
                query.setSuperiorId(taskQuery.getTaskId());
                PageHelper.startPage(taskQuery.getPageNum(), taskQuery.getPageSize());
                List<SimpleTask> simpleTasks = taskDao.findByExecutorPage(query);
                judgeTaskStatus(simpleTasks, false);
                PageInfo<SimpleTask> pageInfo = new PageInfo<>(simpleTasks);
                baseVo.setData(pageInfo);
            }
        }
        return baseVo;
    }

    @Override
    public List<SimpleTask> findTaskChildren(TaskQuery taskQuery) {
        TaskQuery query = new TaskQuery();
        query.setSuperiorId(taskQuery.getTaskId());
        List<SimpleTask> simpleTasks = taskDao.findTaskAll(query);
        judgeTaskStatus(simpleTasks, false);
        return simpleTasks;
    }

    @Override
    @Transactional
    public void terminationById(Long id, String reason) {
        FullTask fullTask = taskDao.findById(id);
        Long userId = getCurrentUserInfo().getId();
        if (fullTask.getTaskStatus() == TaskStatus.BREAKUP.value) {
            throw new BusinessException("任务已终止");
        }
        // 过期不能终止任务
        if (fullTask.getTaskEndAt().before(new Date())) {
            throw new BusinessException("任务已过期，不能终止");
        }
        // 通用任务只有负责人才能终止任务
        if (fullTask.getTaskType() == TaskType.COMMON.value) {
            if (!fullTask.getTaskPrincipalId().equals(userId)) {
                throw new BusinessException("只有责任人可终止任务");
            }
        }
        // 计划任务，负责人、分配人可以终止
        if (fullTask.getTaskStatus() == TaskType.PLAN.value) {
            if (!fullTask.getTaskPrincipalId().equals(userId) || !fullTask.getAssignerId().equals(userId)) {
                throw new BusinessException("只有负责人或分配人可终止任务");
            }
        }
        taskDao.terminationById(id, TaskStatus.BREAKUP.value);
        // 新增操作记录
        addTaskFeedbackLog(id, "终止任务", reason);
        // 发送消息
        sendTerminationMsg(fullTask);
    }

    private void sendTerminationMsg(FullTask fullTask) {
        Set<Long> userIds = new HashSet<>();
        // 负责人
        userIds.add(fullTask.getTaskPrincipalId());
        // 分配人
        if (fullTask.getAssignerId() != null) {
            userIds.add(fullTask.getAssignerId());
        }
        // 负责人或分配人自己不需要发送消息
        UserInfo userInfo = getCurrentUserInfo();
        userIds.remove(userInfo.getId());
        // 执行人
        getCustomerInFullTask(userIds, fullTask.getExecutor());
        getCustomerInFullTask(userIds, fullTask.getCc());
        Long[] toSendMsgUser = userIds.toArray(new Long[]{});
        // 总任务
        if (0L == fullTask.getSuperiorId()) {
            pushUtil.sendMessage(fullTask.getId(), MessageTemplateEnum.TASK_SUPERIOR_TERMINATION, TargetTypeEnum.USER,
                    toSendMsgUser, new String[]{userInfo.getRealName(), fullTask.getTaskName()},
                    userInfo.getRealName());
        } else { // 子任务
            pushUtil.sendMessage(fullTask.getId(), MessageTemplateEnum.TASK_CHILD_TERMINATION, TargetTypeEnum.USER,
                    toSendMsgUser, new String[]{userInfo.getRealName(), fullTask.getTaskName()},
                    userInfo.getRealName());
        }
    }

    private void getCustomerInFullTask(Set<Long> userIds, List<Task.Consumer> cc) {
        // 抄送人
        JSONArray ccs = JSONArray.parseArray(JSON.toJSONString(cc));
        for (Iterator iterator = ccs.iterator(); iterator.hasNext(); ) {
            JSONObject jsonObject = (JSONObject) iterator.next();
            userIds.add(jsonObject.getLong("id"));
        }
    }

    @Override
    public void createTaskFeedback(TaskFeedback taskFeedback) {
        FullTask fullTask = taskDao.findById(taskFeedback.getTaskId());
        if (fullTask != null && fullTask.getTaskStatus() == TaskStatus.ING.value) {
            /*if (fullTask.getTaskEndAt().before(new Date())) {
                throw new BusinessException("任务已结束，不能反馈");
            }*/
            Set<Long> userIds = new HashSet<>();
            UserInfo userInfo = getCurrentUserInfo();
            userIds.add(fullTask.getTaskPrincipalId());
            if (fullTask.getAssignerId() != null && fullTask.getAssignerId() != null) {
                userIds.add(fullTask.getAssignerId());
            }
            // 执行人
            getCustomerInFullTask(userIds, fullTask.getExecutor());
            // 只有负责人、分配人、执行人才能反馈
            if (userIds.contains(userInfo.getId())) {
                taskFeedback.setUserId(userInfo.getId());
                taskFeedback.setUserName(userInfo.getRealName());
                taskFeedback.setType(TaskFeedbackType.FEEDBACK.value);
                taskFeedback.setCreateAt(new Date());
                taskDao.createFeedback(taskFeedback);
            } else {
                throw new BusinessException("该账户不能提交反馈");
            }

            // 抄送人
            getCustomerInFullTask(userIds, fullTask.getCc());
            Long[] toSendUsers = userIds.toArray(new Long[]{});
            String[] params = {taskFeedback.getUserName(), fullTask.getTaskName()};
            pushUtil.sendMessage(taskFeedback.getTaskId(), MessageTemplateEnum.TASK_FEEDBACK, TargetTypeEnum.USER,
                    toSendUsers, params, userInfo.getRealName());
        } else {
            throw new BusinessException("任务已终止，不能反馈");
        }
    }

    @Override
    public TaskFeedback findTaskFeedbackById(Long id) {
        return taskDao.findTaskFeedbackById(id);
    }

    @Override
    public BaseVo findTaskFeedbackByConditionPage(TaskQuery taskQuery) {
        TaskQuery query = new TaskQuery();
        query.setPageSize(taskQuery.getPageSize());
        query.setPageNum(taskQuery.getPageNum());
        query.setTaskId(taskQuery.getTaskId());
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        List<TaskFeedback> taskFeedbacks = taskDao.findTaskFeedbackByConditionPage(query);
        PageInfo<TaskFeedback> pageInfo = new PageInfo<>(taskFeedbacks);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public Set<Schedule> findScheduleTask(Schedule schedule) {
        schedule.setUserId(getCurrentUserInfo().getId());
        Set<Schedule> scheduleSet = taskDao.findPrincipalSchedule(schedule);
        scheduleSet.addAll(taskDao.findExecutorSchedule(schedule));
        return scheduleSet.stream().sorted(Comparator.comparing(Schedule::getStartTime))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    /**
     * 根据不同状态更变时间
     *
     * @param taskQuery
     */
    private void buildTaskStatus(TaskQuery taskQuery) {
        if (taskQuery.getTaskStatus() != null && taskQuery.getTaskStatus() == TaskStatus.ING.value) {
            if (taskQuery.getTaskEndAt() == null) {
                taskQuery.setTaskStatus(null);
                taskQuery.setIngStatus(TaskStatus.ING.value);
            }
        }
        if (taskQuery.getTaskStatus() != null && taskQuery.getTaskStatus() == TaskStatus.FINISH.value) {
            taskQuery.setTaskStatus(null);
            taskQuery.setOverStatus(TaskStatus.FINISH.value);
        }
    }

    /**
     * 新增用户操作日志
     *
     * @param taskId
     * @param content
     */
    private void addTaskFeedbackLog(Long taskId, String content, String reason) {
        TaskFeedback taskFeedback = new TaskFeedback();
        UserInfo userInfo = getCurrentUserInfo();
        taskFeedback.setUserId(userInfo.getId());
        taskFeedback.setUserName(userInfo.getRealName());

        taskFeedback.setTaskId(taskId);
        taskFeedback.setContent(content);
        taskFeedback.setReason(reason);
        taskFeedback.setCreateAt(new Date());
        taskFeedback.setType(TaskFeedbackType.LOG.value);
        taskDao.createFeedback(taskFeedback);
    }

    /**
     * 判断是否过期
     *
     * @param simpleTasks
     * @return
     */
    private void judgeTaskStatus(List<SimpleTask> simpleTasks, Boolean flag) {
        Long userId = getCurrentUserInfo().getId();
        simpleTasks.parallelStream().forEach(simpleTask -> {
            // 判断是否过期，已结束
            if (simpleTask.getTaskStatus() != TaskStatus.BREAKUP.value) {
                if (simpleTask.getTaskEndAt().before(new Date())) {
                    simpleTask.setTaskStatus(TaskStatus.FINISH.value);
                }
                if (simpleTask.getTier() == null && simpleTask.getNum() != null) {
                    if (simpleTask.getTier() == 0 && simpleTask.getNum() == 1) {
                        simpleTask.setSuperiorId(0L);
                    }
                }
            }
            simpleTask.setUserIdentity(TaskTypeEnum.DEFAULT.value);
            if (flag) {
                if (simpleTask.getTaskPrincipalId().equals(userId)) {
                    simpleTask.setUserIdentity(TaskTypeEnum.PRINCIPAL.value);
                    return;
                }
                if (simpleTask.getAssignerId() != null && simpleTask.getAssignerId().equals(userId)) {
                    simpleTask.setUserIdentity(TaskTypeEnum.ASSIGNER.value);
                    return;
                }
                if (simpleTask.getExecutor() != null && simpleTask.getExecutor().size() > 0) {
                    JSONArray executors = JSONArray.parseArray(JSON.toJSONString(simpleTask.getExecutor()));
                    for (Iterator iterator = executors.iterator(); iterator.hasNext(); ) {
                        JSONObject jsonObject = (JSONObject) iterator.next();
                        Long executorId = jsonObject.getLong("id");
                        if (executorId.equals(userId)) {
                            simpleTask.setUserIdentity(TaskTypeEnum.EXECUTOR.value);
                            return;
                        }
                    }
                }
            }
        });
    }

    private TaskSubset buildTaskSubset(Task task) {
        TaskSubset taskSubset = new TaskSubset();
        // 判断级别，大于0为子级，等于0为父级
        if (task.getSuperiorId() != null && task.getSuperiorId() > 0L) {
            taskSubset.setSuperiorId(task.getSuperiorId());
            taskSubset.setTier(TierEnum.CHILD.value);
        } else {
            taskSubset.setSuperiorId(task.getId());
            taskSubset.setTier(TierEnum.PARENT.value);
        }
        taskSubset.setTaskId(task.getId());
        taskSubset.setCreateAt(new Date());
        return taskSubset;
    }

    private Task buildTask(TaskParam taskParam) {
        UserInfo userInfo = getCurrentUserInfo();
        List<Long> ids = new ArrayList<>();
        Task task = new Task();
        task.setType(taskParam.getTaskType());
        if (taskParam.getSuperiorId() != null && taskParam.getSuperiorId() != 0L) {
            // 父级信息
            FullTask fullTask = taskDao.findById(taskParam.getSuperiorId());
            if (fullTask != null && fullTask.getSuperiorId() != 0L) {
                throw new BusinessException("子任务不能分解");
            }
            if (fullTask != null && fullTask.getTaskType().equals(TaskType.COMMON.value)) {
                throw new BusinessException("通用任务不能分解");
            }
            if (!userInfo.getId().equals(fullTask.getAssignerId())) {
                throw new BusinessException("非该任务的分配人，不能分配任务");
            }
            task.setType(TaskType.PLAN.value);
            task.setSuperiorName(fullTask.getTaskName());
            task.setSuperiorId(fullTask.getId());
            // 添加子任务，则分配人为子任务的负责人
            task.setPrincipalId(fullTask.getAssignerId());
            task.setPrincipalName(fullTask.getAssignerName());
            // 生成子任务编号
            task.setNo(getTaskNo(taskParam.getTaskType(), fullTask.getTaskNo(), taskParam.getSuperiorId()));
        } else {
            task.setSuperiorId(0L);
            task.setNo(getTaskNo(taskParam.getTaskType(), null, null));
            task.setSuperiorName(taskParam.getTaskName());
        }
        task.setName(taskParam.getTaskName());
        // 获取责任人ID+分配人ID+执行人ID+抄送人ID，进行查询再分配名称
        ids.add(taskParam.getTaskPrincipalId());
        ids.add(taskParam.getAssignerId());
        if (taskParam.getExecutor() != null && taskParam.getExecutor().size() > 0) {
            ids.addAll(taskParam.getExecutor());
        }
        if (taskParam.getCc() != null && taskParam.getCc().size() > 0) {
            ids.addAll(taskParam.getCc());
        }
        List<Task.Consumer> executors = new ArrayList<>();
        List<Task.Consumer> ccs = new ArrayList<>();
        Map<String, Object> params = new HashMap<>(2);
        params.put("appId", 2);
        params.put("uids", ids);
        BaseVo baseVo = userServiceFeign.listByAppIdAndIds(params);
        List<User> userList = (List<User>) baseVo.getData();
        userList.parallelStream().forEach(user -> {
            // 如果是子任务，则忽略
            if (task.getPrincipalId() == null) {
                if (user.getId().equals(taskParam.getTaskPrincipalId())) {
                    task.setPrincipalId(user.getId());
                    task.setPrincipalName(user.getRealName());
                }
            }
            if (user.getId().equals(taskParam.getAssignerId())) {
                task.setAssignerId(user.getId());
                task.setAssignerName(user.getRealName());
            }
            if (taskParam.getExecutor() != null && taskParam.getExecutor().size() > 0) {
                taskParam.getExecutor().parallelStream().forEach(taskId -> {
                    if (taskId.equals(user.getId())) {
                        Task.Consumer executor = new Task.Consumer();
                        executor.setId(user.getId());
                        executor.setName(user.getRealName());
                        executors.add(executor);
                    }
                });
            }
            if (taskParam.getCc() != null && taskParam.getCc().size() > 0) {
                taskParam.getCc().parallelStream().forEach(taskId -> {
                    if (taskId.equals(user.getId())) {
                        Task.Consumer cc = new Task.Consumer();
                        cc.setId(user.getId());
                        cc.setName(user.getRealName());
                        ccs.add(cc);
                    }
                });
            }
        });
        task.setExecutor(executors.size() > 0 ? executors : null);
        task.setCc(ccs.size() > 0 ? ccs : null);
        task.setStartAt(taskParam.getTaskStartAt());
        task.setEndAt(taskParam.getTaskEndAt());
        task.setContent(taskParam.getContent());

        task.setStatus(TaskStatus.ING.value);
        if (taskParam.getAttactIds() != null) {
            task.setAttactIds(taskParam.getAttactIds());
        }
        task.setUserId(userInfo.getId());
        task.setCreateAt(new Date());
        return task;
    }

    /**
     * 生成任务编号
     *
     * @return
     */
    private String getTaskNo(Integer type, String no, Long id) {
        StringBuffer stringBuffer = new StringBuffer();
        if (!StringUtils.isEmpty(no)) {
            int num = taskDao.selectNums(id);
            stringBuffer.append(no).append("-").append(num + 1);
            return stringBuffer.toString();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateNowStr = sdf.format(new Date());
        String key = TASK_NO + dateNowStr;
        Long new_no = cacheUtil.incr(key, 1L);
        if (new_no == 1) {
            // 设置过期时间
            cacheUtil.expire(key, 24 * 60 * 60);
        }
        String taskNo = new DecimalFormat("0000").format(new_no);
        stringBuffer.append(type == TaskType.COMMON.value ? "TY-" : "JH-").append(dateNowStr).append("-")
                .append(taskNo);
        return stringBuffer.toString();
    }

    @Data
    @AllArgsConstructor
    public static class User implements Serializable {
        // 用户ID
        private Long id;
        // 用户名称
        private String realName;
    }

    @AllArgsConstructor
    enum TierEnum {
        PARENT(0, "父级"), CHILD(1, "子级");
        private int value;

        private String phrase;

        public static TierEnum getByValue(int value) {
            for (TierEnum tierEnum : values()) {
                if (tierEnum.value == value) {
                    return tierEnum;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }

    @AllArgsConstructor
    enum TaskStatus {
        ING(1, "进行中"), FINISH(2, "已结束"), BREAKUP(3, "已终止");

        private int value;

        private String phrase;

        public static TaskStatus getByValue(int value) {
            for (TaskStatus taskStatus : values()) {
                if (taskStatus.value == value) {
                    return taskStatus;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }

    @AllArgsConstructor
    enum TaskType {
        COMMON(1, "通用任务"), PLAN(2, "计划任务");
        private int value;

        private String phrase;

        public static TaskType getByValue(int value) {
            for (TaskType taskType : values()) {
                if (taskType.value == value) {
                    return taskType;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }

    @AllArgsConstructor
    public enum TaskTypeEnum {
        DEFAULT(0, "默认值"), PRINCIPAL(1, "由我负责"), ASSIGNER(2, "分配给我"), EXECUTOR(3, "由我执行"), CC(4, "抄送给我");

        private Integer value;

        private String phrase;

        public static TaskTypeEnum getByValue(int value) {
            for (TaskTypeEnum taskStatus : values()) {
                if (taskStatus.value == value) {
                    return taskStatus;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }

        public String getPhrase() {
            return phrase;
        }
    }

    @AllArgsConstructor
    enum TaskFeedbackType {
        LOG(1, "操作日志"), FEEDBACK(2, "用户反馈");

        private Integer value;

        private String phrase;

        public static TaskFeedbackType getByValue(int value) {
            for (TaskFeedbackType taskFeedbackType : values()) {
                if (taskFeedbackType.value == value) {
                    return taskFeedbackType;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }
    }

}
