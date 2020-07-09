package com.bit.module.oa.dao;

import com.bit.module.oa.bean.Executor;
import com.bit.module.oa.vo.executor.ExecutorPageVO;
import com.bit.module.oa.vo.executor.ExecutorExportVO;
import com.bit.module.oa.vo.executor.ExecutorVO;
import com.bit.module.oa.vo.executor.InspectExecuteDetail;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Executor管理的Dao
 * @author 
 *
 */
@Repository
public interface ExecutorDao {
	/**
	 * 根据条件查询Executor
	 * @param executorVO
	 * @return
	 */
	List<ExecutorPageVO> findByConditionPage(ExecutorVO executorVO);

	/**
	 * 主键查询
	 * @param id
	 * @return
	 */
	Executor findById(@Param("id")Long id);

	/**
	 * 通过主键查询单个Executor
	 * @param id
	 * @return
	 */
	InspectExecuteDetail findAllDetailById(@Param(value = "id") Long id);

	/**
	 * 通过巡检任务单查询
	 * @param inspectId
	 * @return
	 */
	List<Executor> findByInspectId(@Param(value = "inspectId") Long inspectId);
	/**
	 * 通过主键查询单个Executor
	 * @param inspectId
	 * @param executorId
	 * @return
	 */
	Executor findByInspectIdAndExecutorId(@Param(value = "inspectId") Long inspectId,
										  @Param(value = "executorId") Long executorId);

	/**
	 * 批量保存Executor
	 * @param executors
	 */
	void batchAdd(@Param(value = "executors") List<Executor> executors);
	/**
	 * 保存Executor
	 * @param executor
	 */
	void add(Executor executor);

	/**
	 * 批量更新Executor
	 *
	 * @param executors
	 */
	void batchUpdateStatus(@Param("status")Integer status, @Param("checkInStatus")Integer checkInStatus,
						   @Param("executors")List<Executor> executors);
	/**
	 * 更新Executor
	 * @param executor
	 */
	void update(Executor executor);
	/**
	 * 删除Executor
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 放弃任务
	 * @param executor
	 */
	void giveUp(Executor executor);

	/**
	 * 修改状态
	 *
	 * @param id
	 * @param status
	 */
	void updateStatus(@Param("id") Long id, @Param("status") Integer status, @Param("version")Integer version);

	/**
	 * 结束任务
	 * @param toCheck
	 */
    void end(Executor toCheck);

    List<ExecutorExportVO> findExportList(Executor toQuery);

	/**
	 * 统计所有的个人巡检的数量
	 *
	 * @param userId
	 * @param startAt
	 * @param endAt
	 * @return
	 */
	Integer countAllByExecutorId(@Param("userId")Long userId, @Param("startAt")Date startAt, @Param("endAt")Date endAt);

	Integer countCompletedByExecutorId(@Param("userId")Long userId, @Param("startAt")Date startAt,
									   @Param("endAt")Date endAt);

	Integer countApplicationByExecutorId(@Param("userId")Long userId, @Param("startAt")Date startAt,
										 @Param("endAt")Date endAt);
}
