package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Application;
import com.bit.module.oa.enums.ApplicationStatusEnum;
import com.bit.module.oa.service.ApplicationService;
import com.bit.module.oa.vo.application.ApplicationVO;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Application的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/application")
public class ApplicationController {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
	@Autowired
	private ApplicationService applicationService;
	

    /**
     * 补卡历史
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ApplicationVO applicationVO) {
    	//分页对象，前台传递的包含查询的参数

        return applicationService.findByConditionPage(applicationVO);
    }

    /**
     * 根据主键ID查询Application
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Application application = applicationService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(application);
		return baseVo;
    }
    
    /**
     * 新增Application
     *
     * @param application Application实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Application application) {
        applicationService.add(application);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Application
     *
     * @param application Application实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody Application application) {
		applicationService.update(application);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除Application
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        applicationService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据巡检执行单id审核通过
     * @param executeId
     * @return
     */
    @PostMapping("/{executeId}/audit")
    public BaseVo audit(@PathVariable("executeId") Long executeId) {
        applicationService.audit(executeId, ApplicationStatusEnum.AUDIT.getKey());
        return new BaseVo();
    }

    /**
     * 审核拒绝
     * @param application
     * @return
     */
    @PostMapping("/reject")
    public BaseVo reject(@RequestBody Application application) {
        CheckUtil.notNull(application.getExecuteId());
        applicationService.reject(application);
        return new BaseVo();
    }

}
