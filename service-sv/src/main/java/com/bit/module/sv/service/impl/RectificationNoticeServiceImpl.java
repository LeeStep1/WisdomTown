package com.bit.module.sv.service.impl;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Plan;
import com.bit.module.sv.bean.RectificationNotice;
import com.bit.module.sv.bean.Task;
import com.bit.module.sv.dao.PlanDao;
import com.bit.module.sv.dao.RectificationNoticeDao;
import com.bit.module.sv.dao.TaskDao;
import com.bit.module.sv.dao.UnitDao;
import com.bit.module.sv.enums.TaskStatusEnum;
import com.bit.module.sv.service.RectificationNoticeService;
import com.bit.module.sv.utils.DateUtils;
import com.bit.module.sv.vo.IllegalVO;
import com.bit.module.sv.vo.IncrementalRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("rectificationNoticeService")
@Transactional
public class RectificationNoticeServiceImpl extends BaseService implements RectificationNoticeService {

    private static final Logger logger = LoggerFactory.getLogger(RectificationNoticeServiceImpl.class);

    @Autowired
    private PlanDao planDao;

    @Autowired
    private TaskDao taskDao;

    @Autowired
    private UnitDao unitDao;

    @Autowired
    private RectificationNoticeDao rectificationNoticeDao;

    @Override
    public BaseVo addRectificationNotice(RectificationNotice rectificationNotice) {
        Task toGet = taskDao.selectById(rectificationNotice.getTaskId());
        if (toGet == null) {
            throw new BusinessException("巡检任务不存在");
        }
        rectificationNotice.setCreateAt(DateUtils.getCurrentDate());
        rectificationNotice.setUpdateAt(rectificationNotice.getCreateAt());
        rectificationNoticeDao.insert(rectificationNotice);
        // 更新企业不良记录数量 +1
        unitDao.increaseInjuriaCount(toGet.getUnitId(), 1);
        // 更新任务执行状态
        Task toUpdateTask = new Task();
        toUpdateTask.setId(toGet.getId());
        toUpdateTask.setStatus(TaskStatusEnum.PENDING_RECTIFY.value);
        // 现场整改，任务完成
        if (rectificationNotice.getType() == 1) {
            logger.info("现场整改，任务({})完成，需要更新计划({})的任务完成数量...", toGet.getId(), toGet.getPlanId());
            toUpdateTask.setStatus(TaskStatusEnum.COMPLETED.value);
            // 计划中已完成任务数量 +1
            planDao.increaseCompletedTaskNum(toGet.getPlanId(), 1);
        } else {
            // 第一次整改不通过，需要更新计划的执行状态为执行中
            Plan plan = new Plan();
            plan.setStatus(TaskStatusEnum.CHECKING.value);
            plan.setUpdateAt(rectificationNotice.getUpdateAt());
            plan.setId(toGet.getPlanId());
            planDao.updateByIdSelective(plan);
        }
        toUpdateTask.setUpdateAt(rectificationNotice.getUpdateAt());
        taskDao.updateByIdSelective(toUpdateTask);
        return new BaseVo();
    }

    @Override
    public BaseVo selectByTaskId(Long taskId) {
        RectificationNotice rectificationNotice = rectificationNoticeDao.selectByTaskId(taskId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(rectificationNotice);
        return baseVo;
    }

    @Override
    public BaseVo incrementRectificationNotices(IncrementalRequest request) {
        List<IllegalVO> notices = rectificationNoticeDao.incrementRectificationNotices(request);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(notices);
        return baseVo;
    }

    @Override
    public BaseVo modifyRectificationNotice(RectificationNotice notice) {
        RectificationNotice toGet = rectificationNoticeDao.selectById(notice.getId());
        if (toGet == null) {
            throw new BusinessException("整改通知单不存在");
        }
        Task task = taskDao.selectById(toGet.getTaskId());
        if (task == null) {
            throw new BusinessException("任务不存在");
        }
        // 不允许更新 taskId
        notice.setTaskId(toGet.getTaskId());
        notice.setUpdateAt(DateUtils.getCurrentDate());
        if (notice.getType() == null || notice.getType() == toGet.getType()) {
            rectificationNoticeDao.updateByIdSelective(notice);
            return new BaseVo();
        }
        // 变更了整改类型
        Integer needUpdateStatus = null;
        int completedTaskNum = 0;
        notice.setCreateAt(toGet.getCreateAt());
        // 限期整改,则任务为待整改
        if (notice.getType() == 2) {
            needUpdateStatus = TaskStatusEnum.PENDING_RECTIFY.value;
            completedTaskNum = -1;
        } else {
            taskDao.deleteByRefId(toGet.getTaskId());
            if (task.getStatus() != TaskStatusEnum.COMPLETED.value) {
                needUpdateStatus = TaskStatusEnum.COMPLETED.value;
                // 删除复查项目，如果有
                completedTaskNum = 1;
            }
        }
        rectificationNoticeDao.updateById(notice);
        if (needUpdateStatus == null) {
            logger.info("当前通知单({})不需要更新任务({})执行状态.", notice.getId(), notice.getTaskId());
            return new BaseVo();
        }
        // 更新任务执行状态
        Task toUpdateTask = new Task();
        toUpdateTask.setId(toGet.getTaskId());
        toUpdateTask.setStatus(needUpdateStatus);
        toUpdateTask.setUpdateAt(DateUtils.getCurrentDate());
        taskDao.updateByIdSelective(toUpdateTask);
        // 更新计划任务完成数量
        planDao.increaseCompletedTaskNum(task.getPlanId(), completedTaskNum);
        return new BaseVo();
    }
}
