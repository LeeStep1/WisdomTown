package com.bit.module.system.controller;


import javax.validation.Valid;

import com.bit.module.system.bean.UserResource;
import com.bit.module.system.service.UserResourceService;
import com.bit.module.system.vo.UserResourceQueryVO;
import com.bit.module.system.vo.UserResourceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bit.base.vo.BaseVo;


/**
 * UserResource的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/userResource")
public class UserResourceController {
	private static final Logger logger = LoggerFactory.getLogger(UserResourceController.class);
	@Autowired
	private UserResourceService userResourceService;
	

    /**
     * 分页查询UserResource列表
     *
     */
    @PostMapping("/list")
    public BaseVo listPage() {
    	//分页对象，前台传递的包含查询的参数
        return userResourceService.findByUserId();
    }

    /**
     * 根据主键ID查询UserResource
     *
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        UserResource userResource = userResourceService.findById(id);
        BaseVo baseVo = new BaseVo();
		baseVo.setData(userResource);
		return baseVo;
    }
    
    /**
     * 新增UserResource
     *
     * @param userResourceVO
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody UserResourceVO userResourceVO) {
        userResourceService.add(userResourceVO);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改UserResource
     *
     * @param userResourceVO UserResource实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody UserResourceVO userResourceVO) {
		userResourceService.update(userResourceVO);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除UserResource
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        userResourceService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
