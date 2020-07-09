package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Log;
import com.bit.module.oa.service.LogService;
import com.bit.module.oa.vo.log.LogVO;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Log的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/log")
public class LogController {
	private static final Logger logger = LoggerFactory.getLogger(LogController.class);
	@Autowired
	private LogService logService;
	

    /**
     * 日志分页
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody LogVO logVO) {
    	//分页对象，前台传递的包含查询的参数
        CheckUtil.notNull(logVO.getRefId());
        CheckUtil.notNull(logVO.getType());
        return logService.findByConditionPage(logVO);
    }


    /**
     * 根据主键ID查询Log
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Log log = logService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(log);
		return baseVo;
    }
    
    /**
     * 根据主键ID删除Log
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        logService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
