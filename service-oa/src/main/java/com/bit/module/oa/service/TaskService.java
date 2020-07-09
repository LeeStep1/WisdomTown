package com.bit.module.oa.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Schedule;
import com.bit.module.oa.bean.TaskFeedback;
import com.bit.module.oa.vo.task.*;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 任务接口
 *
 * @autor xiaoyu.fang
 * @date 2019/2/13 15:40
 */
public interface TaskService {

    /**
     * 新增任务
     *
     * @param taskParam
     */
    void add(TaskParam taskParam);

    /**
     * 获取分页-负责人
     *
     * @param taskQuery
     * @return
     */
    BaseVo findPrincipalByConditionPage(TaskQuery taskQuery, Boolean isPage);

    /**
     * 获取分配人的子级
     *
     * @param superiorId
     * @return
     */
    List<SimpleTask> findAssignerChildren(Long superiorId);

    /**
     * 获取分页-分配人
     *
     * @param taskQuery
     * @return
     */
    BaseVo findAssignerByConditionPage(TaskQuery taskQuery, Boolean isPage);

    /**
     * 获取抄送人分页
     *
     * @param taskQuery
     * @return
     */
    BaseVo findByCcConditionPage(TaskQuery taskQuery, Boolean isPage);

    /**
     * 获取执行人分页
     *
     * @param taskQuery
     * @return
     */
    BaseVo findByExecutorConditionPage(TaskQuery taskQuery, Boolean isPage);

    /**
     * 获取执行人子节点
     *
     * @param superiorId
     * @return
     */
    List<SimpleTask> findExecutorChildren(Long superiorId);

    /**
     * 获取详细信息
     *
     * @param id
     * @return
     */
    FullTask findById(Long id);

    /**
     * 获取子任务分页
     *
     * @param taskQuery
     * @return
     */
    BaseVo findTaskChildrenByTaskId(TaskQuery taskQuery);

    /**
     * 获取子任务列表
     *
     * @param taskQuery
     * @return
     */
    List<SimpleTask> findTaskChildren(TaskQuery taskQuery);

    /**
     * 终止任务
     *
     * @param id
     * @param reason
     */
    void terminationById(Long id, String reason);

    /**
     * 新增反馈
     *
     * @param taskFeedback
     */
    void createTaskFeedback(TaskFeedback taskFeedback);

    /**
     * 根据ID获取反馈详细
     *
     * @param id
     * @return
     */
    TaskFeedback findTaskFeedbackById(Long id);

    /**
     * 反馈分页
     *
     * @param taskQuery
     * @return
     */
    BaseVo findTaskFeedbackByConditionPage(TaskQuery taskQuery);

    Set<Schedule> findScheduleTask(Schedule schedule);
}
