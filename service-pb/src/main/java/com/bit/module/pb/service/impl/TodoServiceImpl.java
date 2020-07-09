package com.bit.module.pb.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Todo;
import com.bit.module.pb.dao.TodoDao;
import com.bit.module.pb.service.TodoService;
import com.bit.module.pb.vo.TodoVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Todo的Service实现类
 *
 * @author codeGenerator
 */
@Slf4j
@Service("todoService")
public class TodoServiceImpl extends BaseService implements TodoService {

    @Autowired
    private TodoDao todoDao;

    /**
     * 根据条件查询Todo
     *
     * @param todoVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(TodoVO todoVO) {
        PageHelper.startPage(todoVO.getPageNum(), todoVO.getPageSize());
        List<Todo> list = todoDao.findByConditionPage(todoVO);
        PageInfo<Todo> pageInfo = new PageInfo<Todo>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有Todo
     *
     * @param sorter 排序字符串
     * @return
     */
    @Override
    public List<Todo> findAll(String sorter) {
        return todoDao.findAll(sorter);
    }

    /**
     * 通过主键查询单个Todo
     *
     * @param id
     * @return
     */
    @Override
    public Todo findById(Long id) {
        return todoDao.findById(id);
    }

    /**
     * 批量保存Todo
     *
     * @param todos
     */
    @Override
    public void batchAdd(List<Todo> todos) {
        todoDao.batchAdd(todos);
    }

    /**
     * 保存Todo
     *
     * @param todo
     */
    @Override
    public void add(Todo todo) {
        todoDao.add(todo);
    }

    /**
     * 批量更新Todo
     *
     * @param todos
     */
    @Override
    public void batchUpdate(List<Todo> todos) {
        todoDao.batchUpdate(todos);
    }

    /**
     * 更新Todo
     *
     * @param todo
     */
    @Override
    public void update(Todo todo) {
        todoDao.update(todo);
    }

    /**
     * 删除Todo
     *
     * @param ids
     */
    @Override
    public void batchDelete(List<Long> ids) {
        todoDao.batchDelete(ids);
    }

    @Override
    public void deleteByCorrelationId(Long correlationId, Integer type) {
        todoDao.deleteByCorrelationId(correlationId, type);
    }

    /**
     * 删除Todo
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        todoDao.delete(id);
    }
}
