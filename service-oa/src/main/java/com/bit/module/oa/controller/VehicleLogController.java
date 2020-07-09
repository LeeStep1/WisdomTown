package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.VehicleLog;
import com.bit.module.oa.service.VehicleLogService;
import com.bit.module.oa.vo.vehicleLog.VehicleLogQO;
import com.bit.module.oa.vo.vehicleLog.VehicleLogVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * VehicleLog的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/vehicleLog")
public class VehicleLogController {
	private static final Logger logger = LoggerFactory.getLogger(VehicleLogController.class);
	@Autowired
	private VehicleLogService vehicleLogService;
	

    /**
     * 分页查询VehicleLog列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody VehicleLogVO vehicleLogVO) {
    	//分页对象，前台传递的包含查询的参数

        return vehicleLogService.findByConditionPage(vehicleLogVO);
    }

    /**
     * 查询派车日志
     * @param vehicleLogQO
     * @return
     */
    @PostMapping("/list")
    public BaseVo list(@RequestBody VehicleLogQO vehicleLogQO) {
        return vehicleLogService.findByCondition(vehicleLogQO);
    }


    /**
     * 根据主键ID查询VehicleLog
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        VehicleLog vehicleLog = vehicleLogService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(vehicleLog);
		return baseVo;
    }
    
    /**
     * 新增VehicleLog
     *
     * @param vehicleLog VehicleLog实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody VehicleLog vehicleLog) {
        vehicleLogService.add(vehicleLog);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改VehicleLog
     *
     * @param vehicleLog VehicleLog实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody VehicleLog vehicleLog) {
		vehicleLogService.update(vehicleLog);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除VehicleLog
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        vehicleLogService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
