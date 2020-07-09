package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Schedule;
import com.bit.module.oa.bean.Task;
import com.bit.module.oa.bean.TaskFeedback;
import com.bit.module.oa.bean.TaskSubset;
import com.bit.module.oa.vo.task.FullTask;
import com.bit.module.oa.vo.task.SimpleTask;
import com.bit.module.oa.vo.task.TaskQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @autor xiaoyu.fang
 * @date 2019/2/13 14:01
 */
@Repository
public interface TaskDao {

    /**
     * 新增任务
     *
     * @param task
     */
    void add(Task task);

    /**
     * 任务分页
     *
     * @param taskQuery
     * @return
     */
    List<SimpleTask> findByConditionPage(TaskQuery taskQuery);

    /**
     * 根据执行人获取任务信息
     *
     * @param taskQuery
     * @return
     */
    List<SimpleTask> findByExecutorPage(TaskQuery taskQuery);

    /**
     * 获取子任务中本人作为分配人的数量
     *
     * @param assignerId
     * @return
     */
    int countAssignerChildren(@Param(value = "assignerId") Long assignerId, @Param(value = "superiorId") Long superiorId);

    /**
     * 获取子任务中本人作为执行人的数量
     *
     * @param executorId
     * @param superiorId
     * @return
     */
    int countExecutorChildren(@Param(value = "executorId") Long executorId, @Param(value = "superiorId") Long superiorId);


    /**
     * 根据ID 获取任务详细
     *
     * @param id
     * @return
     */
    FullTask findById(@Param(value = "id") Long id);

    /**
     * 终止任务，子任务也要结束
     *
     * @param id
     * @param status
     */
    void terminationById(@Param(value = "id") Long id, @Param(value = "status") Integer status);

    /**
     * 获取子任务数量
     *
     * @param id
     * @return
     */
    int selectNums(Long id);

    /**
     * 新增分配人
     *
     * @param taskSubset
     */
    void createAssigner(TaskSubset taskSubset);

    /**
     * 批量新增执行人
     *
     * @param taskSubsets
     */
    void batchCreateExecutor(List<TaskSubset> taskSubsets);

    /**
     * 批量新增抄送人
     *
     * @param taskSubsets
     */
    void batchCreateCc(List<TaskSubset> taskSubsets);

    /**
     * 分配人分页
     *
     * @param taskQuery
     * @return
     */
    List<SimpleTask> findAssignerByConditionPage(TaskQuery taskQuery);

    /**
     * 获取分配人子节点
     * @param assignerId
     * @param superiorId
     * @return
     */
    List<SimpleTask> findAssignerChildren(@Param(value = "assignerId") Long assignerId, @Param(value = "superiorId") Long superiorId);

    /**
     * 执行人分页
     *
     * @param taskQuery
     * @return
     */
    List<SimpleTask> findExecutorByConditionPage(TaskQuery taskQuery);

    /**
     * 获取执行人子节点
     * @param ownerId
     * @param superiorId
     * @return
     */
    List<SimpleTask> findExecutorChildren(@Param(value = "ownerId") Long ownerId, @Param(value = "superiorId") Long superiorId);

    /**
     * 抄送人分页
     *
     * @param taskQuery
     * @return
     */
    List<SimpleTask> findCcByConditionPage(TaskQuery taskQuery);

    /**
     * 新增反馈
     *
     * @param taskFeedback
     */
    void createFeedback(TaskFeedback taskFeedback);

    /**
     * 根据ID获取反馈详细
     *
     * @param id
     * @return
     */
    TaskFeedback findTaskFeedbackById(@Param(value = "id") Long id);

    /**
     * 反馈分页
     *
     * @param taskQuery
     * @return
     */
    List<TaskFeedback> findTaskFeedbackByConditionPage(TaskQuery taskQuery);

    // ================导出===============
    /**
     * 列表（导出）
     *
     * @param taskExport
     * @return
     */
    List<SimpleTask> findTaskAll(TaskQuery taskExport);

    Set<Schedule> findPrincipalSchedule(Schedule schedule);

    Set<Schedule> findExecutorSchedule(Schedule schedule);
}
