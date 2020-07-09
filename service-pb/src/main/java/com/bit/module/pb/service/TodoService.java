package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Todo;
import com.bit.module.pb.vo.TodoVO;

import java.util.List;

/**
 * Todo的Service
 *
 * @author codeGenerator
 */
public interface TodoService {
    /**
     * 根据条件查询Todo
     *
     * @param todoVO
     * @return
     */
    BaseVo findByConditionPage(TodoVO todoVO);

    /**
     * 查询所有Todo
     *
     * @param sorter 排序字符串
     * @return
     */
    List<Todo> findAll(String sorter);

    /**
     * 通过主键查询单个Todo
     *
     * @param id
     * @return
     */
    Todo findById(Long id);

    /**
     * 批量保存Todo
     *
     * @param todos
     */
    void batchAdd(List<Todo> todos);

    /**
     * 保存Todo
     *
     * @param todo
     */
    void add(Todo todo);

    /**
     * 批量更新Todo
     *
     * @param todos
     */
    void batchUpdate(List<Todo> todos);

    /**
     * 更新Todo
     *
     * @param todo
     */
    void update(Todo todo);

    /**
     * 删除Todo
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 批量删除Todo
     *
     * @param ids
     */
    void batchDelete(List<Long> ids);

    /**
     * 根据关联ID删除
     * @param correlationId
     */
    void deleteByCorrelationId(Long correlationId, Integer type);
}
