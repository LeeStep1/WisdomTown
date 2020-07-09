package com.bit.module.sv.dao;

import com.bit.module.sv.bean.Task;
import com.bit.module.sv.vo.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskDao {

    /**
     * 删除任务
     * @param id
     * @return
     */
    int deleteById(Long id);

    /**
     * 强制插入
     * @param task
     * @return
     */
    int insert(Task task);

    /**
     * 有选择的插入
     * @param task
     * @return
     */
    int insertSelective(Task task);

    /**
     * 根据ID获取任务
     * @param id
     * @return
     */
    Task selectById(Long id);

    /**
     * 有选择的更新
     * @param task
     * @return
     */
    int updateByIdSelective(Task task);

    /**
     * 强制更新
     * @param task
     * @return
     */
    int updateById(Task task);

    /**
     * 批量插入
     * @param list
     * @return
     */
    int batchInsert(@Param("list") List<Task> list);

    /**
     * 根据计划删除任务
     * @param planId
     * @return
     */
    int deleteByPlanId(Long planId);

    /**
     * 根据条件查询任务列表
     * @param taskVO
     * @return
     */
    List<Task> findByConditionPage(TaskVO taskVO);

    /**
     * 根据计划获取任务列表
     * @param planId
     * @return
     */
    List<Task> selectByPlanId(Long planId);

    /**
     * 统计任务
     * @param range
     * @param userId
     * @param startAt
     * @param endAt
     * @return
     */
    List<StatisticsVO> countBySourceAndUserIdAndDate(@Param("source") Integer range, @Param("userId") Long userId,
                                                     @Param("startAt") Date startAt, @Param("endAt") Date endAt);

    /**
     * 增量查询任务列表
     * @param request
     * @return
     */
    List<Task> incrementTasks(IncrementalRequest request);

    /**
     * 查询复查任务
     * @param taskId
     * @return
     */
    ReviewChecklist selectByRefId(@Param("refId") Long taskId);

    /**
     * 删除复查任务
     * @param taskId
     * @return
     */
    int deleteByRefId(@Param("refId") Long taskId);

    /**
     * 导出任务数据
     * @param id
     * @return
     */
    ExportVO exportById(Long id);

    /**
     * 根据数据来源查询计划的巡检对象列表
     * @param planId
     * @param source
     * @return
     */
    List<Task> selectUnitByPlanIdAndSource(@Param("planId") Long planId, @Param("source") Integer source);

    /**
     * 获取非城建巡检任务列表
     * @param startAt
     * @param endAt
     * @return
     */
    List<Task> findTaskWithOutUCByEndAt(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

    /**
     * 获取非城建复查任务列表
     * @param startAt
     * @param endAt
     * @return
     */
    List<Task> findReviewTaskWithOutUCByEndAt(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

    /**
     * 根据数据来源获取任务详情
     * @param id
     * @param source
     * @return
     */
    Task selectByIdAndSource(@Param("id") Long id, @Param("source") Integer source);

    /**
     * 获取城建复查任务列表
     * @param startAt
     * @param endAt
     * @return
     */
    List<Task> findReviewTaskOnlyUCByEndAt(@Param("startAt") Date startAt, @Param("endAt") Date endAt);

    /**
     * 获取城建巡检任务列表
     * @param startAt
     * @param endAt
     * @return
     */
    List<Task> findTaskOnlyUCByEndAt(@Param("startAt") Date startAt, @Param("endAt") Date endAt);
}