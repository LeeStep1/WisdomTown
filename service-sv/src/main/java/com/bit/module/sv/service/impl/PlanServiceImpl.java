package com.bit.module.sv.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
/*import com.bit.module.mqCore.MqEnum.MessageTemplateEnum;
import com.bit.module.mqCore.MqEnum.TargetTypeEnum;*/
import com.bit.module.sv.bean.IdName;
import com.bit.module.sv.bean.Plan;
import com.bit.module.sv.bean.Task;
import com.bit.module.sv.dao.PlanDao;
import com.bit.module.sv.dao.TaskDao;
import com.bit.module.sv.enums.TaskStatusEnum;
import com.bit.module.sv.enums.TaskTypeEnum;
import com.bit.module.sv.service.PlanService;
import com.bit.module.sv.utils.DateUtils;
import com.bit.module.sv.utils.StringUtils;
import com.bit.module.sv.vo.PlanVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.support.PushUtil;
import com.bit.core.utils.CacheUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bit.soft.push.msEnum.MessageTemplateEnum.SV_TASK_ASSIGN;
import static com.bit.soft.push.msEnum.MessageTemplateEnum.UC_TASK_ASSIGN;

//import static com.bit.module.mqCore.MqEnum.MessageTemplateEnum.*;

@Service("planService")
@Transactional
public class PlanServiceImpl extends BaseService implements PlanService {

    private static final Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

    @Autowired
    private PlanDao planDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private CacheUtil cacheUtil;

    @Autowired
    private PushUtil pushUtil;

    @Override
    public BaseVo addPlan(PlanVO planVO) {
        TaskTypeEnum checkType = TaskTypeEnum.getByValue(planVO.getType());
        if (checkType == null) {
            throw new BusinessException("非法的任务类型");
        }
        planVO.setStatus(TaskStatusEnum.PENDING_CHECK.value);
        Plan toAdd = new Plan();
        BeanUtils.copyProperties(planVO, toAdd);
        toAdd.setCreateAt(DateUtils.getCurrentDate());
        toAdd.setUpdateAt(toAdd.getCreateAt());
        toAdd.setTotalTaskNum(toAdd.getUnits().size());
        toAdd.setCompletedTaskNum(0);
        planDao.insert(toAdd);
        List<IdName> units = planVO.getUnits();
        List<Task> tasks = new ArrayList<>(units.size());
        List<String> taskNos = generatorTaskNos(checkType.phrase, units.size(), planVO.getSource());
        for (int i = 0; i < units.size(); i++) {
            Task task = new Task();
            BeanUtils.copyProperties(toAdd, task);
            task.setId(null);
            task.setPlanId(toAdd.getId());
            task.setUnitId(units.get(i).getId());
            task.setNo(taskNos.get(i));
            tasks.add(task);
        }
        int insertTaskCount = taskDao.batchInsert(tasks);
        logger.info("巡检计划（{}）创建任务总数：{}, 开始发送消息推送...", toAdd.getName(), insertTaskCount);
        pushNewPlan(toAdd, checkType.desc);
        return new BaseVo();
    }

    @Override
    public BaseVo listPlans(PlanVO planVO) {
        PageHelper.startPage(planVO.getPageNum(), planVO.getPageSize());
        if (StringUtils.isBlank(planVO.getOrderBy())) {
            planVO.setOrderBy("create_at");
        }
        if (StringUtils.isBlank(planVO.getOrder())) {
            planVO.setOrder("desc");
        }
        List<Plan> list = planDao.findByConditionPage(planVO);
        PageInfo<Plan> pageInfo = new PageInfo<Plan>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public BaseVo deleteById(Long id) {
        Plan toGet = planDao.selectById(id);
        if (toGet == null) {
            throw new BusinessException("计划不存在");
        }
        if (toGet.getStatus() != TaskStatusEnum.PENDING_CHECK.value) {
            throw new BusinessException("只能删除未开始的计划");
        }
        planDao.deleteById(id);
        taskDao.deleteByPlanId(id);
        return new BaseVo();
    }

    @Override
    public BaseVo modifyById(PlanVO planVO) {
        Plan toGet = planDao.selectById(planVO.getId());
        if (toGet == null) {
            throw new BusinessException("计划不存在");
        }
        if (toGet.getStatus() != TaskStatusEnum.PENDING_CHECK.value) {
            throw new BusinessException("只能修改未开始的计划");
        }
        Plan toUpdate = new Plan();
        BeanUtils.copyProperties(planVO, toUpdate);
        if (toUpdate.getUnits() != null) {
            toUpdate.setTotalTaskNum(toUpdate.getUnits().size());
        }
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        planDao.updateByIdSelective(toUpdate);
        if (CollectionUtils.isEmpty(planVO.getUnits())) {
            return new BaseVo();
        }
        logger.info("巡检对象单位有变更，需要重新产生任务...");
        // 需要重新产生任务
        toGet = planDao.selectById(planVO.getId());
        TaskTypeEnum checkType = TaskTypeEnum.getByValue(toGet.getType());
        List<IdName> units = new Gson().fromJson(new Gson().toJson(toGet.getUnits()), new TypeToken<List<IdName>>(){}.getType());
        List<Task> tasks = new ArrayList<>(units.size());
        List<String> taskNos = generatorTaskNos(checkType.phrase, units.size(), planVO.getSource());
        for (int i = 0; i < units.size(); i++) {
            Task task = new Task();
            BeanUtils.copyProperties(toGet, task);
            task.setId(null);
            task.setPlanId(toGet.getId());
            IdName unit = units.get(i);
            task.setUnitId(unit.getId());
            task.setNo(taskNos.get(i));
            tasks.add(task);
        }
        taskDao.deleteByPlanId(toGet.getId());
        int insertTaskCount = taskDao.batchInsert(tasks);
        logger.info("巡检计划（{}）重新创建任务总数：{}, 开始发送消息推送...", toGet.getName(), insertTaskCount);
        List<IdName> oldIdNameList = new Gson().fromJson(
                new Gson().toJson(toGet.getInspectors()), new TypeToken<List<IdName>>() {}.getType());
        toGet.setInspectors(oldIdNameList);
        pushNewPlan(toGet, checkType.desc);
        return new BaseVo();
    }

    @Override
    public BaseVo selectById(Long id) {
        Plan plan = planDao.selectById(id);
        BaseVo<Plan> baseVo = new BaseVo();
        baseVo.setData(plan);
        return baseVo;
    }

    /**
     * 根据前缀及指定个数生成任务编号
     * @param prefix
     * @param count
     * @param source
     * @return
     */
    private List<String> generatorTaskNos(String prefix, int count, Integer source) {
        String key = null;
        StringBuffer taskNoStr = new StringBuffer();
        switch (source) {
            case 3:
                key = Const.REDIS_SV_TASK_NO;
                taskNoStr.append("SV-");
                break;
            case 4:
                key = Const.REDIS_EP_TASK_NO;
                taskNoStr.append("EP-");
                break;
            case 6:
                key = Const.REDIS_UC_TASK_NO;
                taskNoStr.append("UC-");
                break;
            default:
                logger.info("数据来源（{}） 不明确", source);
                throw new BusinessException("数据来源不明确");
        }
        Object object = cacheUtil.get(key);
        if (object == null) {
            // 如果还没有这个key 则初始化
            cacheUtil.set(key, 0, DateUtils.getTomorrowZeroSeconds());
        }
        object = cacheUtil.get(key);
        cacheUtil.incr(key, count);
        int taskNo = Integer.parseInt(object.toString());
        taskNoStr.append(prefix);
        taskNoStr.append(Const.PREFIX_MINUS);
        taskNoStr.append(DateUtils.date2String(DateUtils.getCurrentDate(), DateUtils.DatePattern.YYYYMMDD1.getValue()));
        taskNoStr.append(Const.PREFIX_MINUS);
        String taskNoPrefix = taskNoStr.toString();
        List<String> taskNoList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            taskNo = taskNo + 1;
            taskNoList.add(taskNoPrefix + StringUtils.leftPad(taskNo + "", 4, Const.ZERO));
        }
        return taskNoList;
    }

    /**
     * 新增计划消息推送(多任务)
     * @param plan
     * @param taskType
     */
    private void pushNewPlan(Plan plan, String taskType) {
        Integer source = plan.getSource();
        if (!StringUtils.checkSourceValid(source)) {
            logger.info("数据来源({})不明确，不需要推送.", source);
            return;
        }
        String creator = getCurrentUserInfo().getRealName();
        Long[] userIds = plan.getInspectors().stream().map(IdName::getId).collect(Collectors.toSet()).toArray(new Long[]{});
        final String startAt = DateUtils.date2String(plan.getStartAt(), DateUtils.DatePattern.YYYYMMDD.getValue());
        final String endAt = DateUtils.date2String(plan.getEndAt(), DateUtils.DatePattern.YYYYMMDD.getValue());
        MessageTemplateEnum templateEnum = MessageTemplateEnum.EP_TASK_ASSIGN;
        switch (source) {
            case 3:
                templateEnum =  MessageTemplateEnum.SV_TASK_ASSIGN;
                break;
            case 6:
                templateEnum =  MessageTemplateEnum.UC_TASK_ASSIGN;
                break;
            default:
                break;
        }
        List<Task> taskList = taskDao.selectUnitByPlanIdAndSource(plan.getId(), source);
        for (Task task : taskList) {
            List<String> params = new ArrayList<>();
            params.add(startAt);
            params.add(endAt);
            params.add(task.getUnitName());
            params.add(taskType);
            pushUtil.sendMessage(
                    task.getId(), templateEnum, TargetTypeEnum.USER, userIds, params.toArray(new String[]{}), creator);
        }
    }
}
