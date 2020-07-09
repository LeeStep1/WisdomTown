package com.bit.module.system.controller;

import javax.validation.Valid;

import com.bit.module.system.bean.UserRelOaDep;
import com.bit.module.system.service.UserRelOaDepService;
import com.bit.module.system.vo.UserRelOaDepVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bit.base.vo.BaseVo;

/**
 * UserRelOaDep的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/userRelOaDep")
public class UserRelOaDepController {
	private static final Logger logger = LoggerFactory.getLogger(UserRelOaDepController.class);
	@Autowired
	private UserRelOaDepService userRelOaDepService;
	

    /**
     * 分页查询UserRelOaDep列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody UserRelOaDepVO userRelOaDepVO) {
    	//分页对象，前台传递的包含查询的参数

        return userRelOaDepService.findByConditionPage(userRelOaDepVO);
    }

    /**
     * 根据主键ID查询UserRelOaDep
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        UserRelOaDep userRelOaDep = userRelOaDepService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(userRelOaDep);
		return baseVo;
    }
    
    /**
     * 新增UserRelOaDep
     *
     * @param userRelOaDep UserRelOaDep实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody UserRelOaDep userRelOaDep) {
        userRelOaDepService.add(userRelOaDep);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改UserRelOaDep
     *
     * @param userRelOaDep UserRelOaDep实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody UserRelOaDep userRelOaDep) {
		userRelOaDepService.update(userRelOaDep);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除UserRelOaDep
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        userRelOaDepService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
