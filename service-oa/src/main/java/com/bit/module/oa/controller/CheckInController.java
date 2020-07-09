package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.CheckIn;
import com.bit.module.oa.service.CheckInService;
import com.bit.module.oa.vo.checkIn.CheckInVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * CheckIn的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/checkIn")
public class CheckInController {
	private static final Logger logger = LoggerFactory.getLogger(CheckInController.class);
	@Autowired
	private CheckInService checkInService;
	

    /**
     * 巡检任务签到点分页
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody CheckInVO checkInVO) {
    	//分页对象，前台传递的包含查询的参数

        return checkInService.findByConditionPage(checkInVO);
    }

    /**
     * 根据主键ID查询CheckIn
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        CheckIn checkIn = checkInService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(checkIn);
		return baseVo;
    }
    
    /**
     * 新增CheckIn
     *
     * @param checkIn CheckIn实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody CheckIn checkIn) {
        checkInService.add(checkIn);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改CheckIn
     *
     * @param checkIn CheckIn实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody CheckIn checkIn) {
		checkInService.update(checkIn);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除CheckIn
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        checkInService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 签到
     *
     * @param checkIn CheckIn实体
     * @return
     */
    @PostMapping("/signIn")
    public BaseVo signIn(@RequestBody CheckIn checkIn) {
        checkInService.signIn(checkIn);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
