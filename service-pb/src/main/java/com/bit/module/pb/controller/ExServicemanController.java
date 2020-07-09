package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.ExServiceman;
import com.bit.module.pb.service.ExServicemanService;
import com.bit.module.pb.vo.ExServicemanVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 退伍军人接口
 * @author generator
 */
@RestController
@RequestMapping(value = "/exServiceman")
public class ExServicemanController {
	private static final Logger logger = LoggerFactory.getLogger(ExServicemanController.class);
	@Autowired
	private ExServicemanService exServicemanService;
	

    /**
     * 分页查询ExServiceman列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ExServicemanVO exServicemanVO) {
    	//分页对象，前台传递的包含查询的参数

        return exServicemanService.findByConditionPage(exServicemanVO);
    }

    /**
     * 根据主键ID查询ExServiceman
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        ExServiceman exServiceman = exServicemanService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(exServiceman);
		return baseVo;
    }
    
    /**
     * 新增ExServiceman
     *
     * @param exServiceman ExServiceman实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody ExServiceman exServiceman) {
        exServicemanService.add(exServiceman);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改ExServiceman
     *
     * @param exServiceman ExServiceman实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody ExServiceman exServiceman) {
		exServicemanService.update(exServiceman);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除ExServiceman
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        exServicemanService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
