package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Todo;
import com.bit.module.pb.service.TodoService;
import com.bit.module.pb.vo.TodoVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 待办事项接口（弃用）
 * @author generator
 */
@RestController
@RequestMapping(value = "/todo")
public class TodoController {
	private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
	@Autowired
	private TodoService todoService;

    /**
     * 分页查询Todo列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody TodoVO todoVO) {
    	//分页对象，前台传递的包含查询的参数

        return todoService.findByConditionPage(todoVO);
    }

    /**
     * 根据主键ID查询Todo
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Todo todo = todoService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(todo);
		return baseVo;
    }

    /**
     * 新增Todo
     *
     * @param todo Todo实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody Todo todo) {
        todoService.add(todo);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Todo
     *
     * @param todo Todo实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody Todo todo) {
		todoService.update(todo);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除Todo
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        todoService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
