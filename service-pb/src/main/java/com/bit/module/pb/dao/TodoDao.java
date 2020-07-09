package com.bit.module.pb.dao;

import com.bit.module.pb.bean.Todo;
import com.bit.module.pb.vo.TodoVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Todo管理的Dao
 * @author 
 *
 */
@Repository
public interface TodoDao {
	/**
	 * 根据条件查询Todo
	 * @param todoVO
	 * @return
	 */
	List<Todo> findByConditionPage(TodoVO todoVO);
	/**
	 * 查询所有Todo
	 * @return
	 */
	List<Todo> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个Todo
	 * @param id	 	 
	 * @return
	 */
	Todo findById(@Param(value = "id") Long id);

	/**
	 * 根据关联id获取待办信息
	 * @param correlationId
	 * @return
	 */
	Todo findByCorrelationId(@Param(value = "correlationId") Long correlationId, @Param(value = "topic") Integer topic);
	/**
	 * 批量保存Todo
	 * @param todos
	 */
	void batchAdd(List<Todo> todos);
	/**
	 * 保存Todo
	 * @param todo
	 */
	void add(Todo todo);
	/**
	 * 批量更新Todo
	 * @param todos
	 */
	void batchUpdate(List<Todo> todos);
	/**
	 * 更新Todo
	 * @param todo
	 */
	void update(Todo todo);
	/**
	 * 删除Todo
	 * @param ids
	 */
	void batchDelete(List<Long> ids);
	/**
	 * 删除Todo
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 根据关联ID删除
	 * @param correlationId
	 */
	void deleteByCorrelationId(@Param(value = "correlationId") Long correlationId, @Param(value = "topic") Integer topic);
}
