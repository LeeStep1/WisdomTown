package com.bit.module.sv.controller;

import com.bit.base.vo.BaseVo;
import com.bit.base.vo.SuccessVo;
import com.bit.module.sv.bean.Unit;
import com.bit.module.sv.service.UnitService;
import com.bit.module.sv.vo.RequestVO;
import com.bit.module.sv.vo.UnitVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * Unit的相关api
 * @author xiaoxi.lao
 */
@RestController
@RequestMapping("/unit")
public class UnitController {

	private static final Logger logger = LoggerFactory.getLogger(UnitController.class);

	@Autowired
	private UnitService unitService;
	
    @PostMapping(name = "查询Unit列表", path = "/list")
    public BaseVo listUnits(@RequestBody Unit unit) {
        return unitService.findByType(unit);
    }

    @PostMapping(name = "分页查询Unit列表", path = "/listPage")
    public BaseVo listPage(@RequestBody @Validated UnitVO unitVO) {
    	//分页对象，前台传递的包含查询的参数
        return unitService.findByConditionPage(unitVO);
    }

    @PostMapping(name = "根据id集合查询Unit列表", path = "/listByIds")
    public BaseVo listUnitsByIds(@RequestBody RequestVO requestVO) {
        return unitService.findByIds(requestVO.getIds());
    }

    @GetMapping(name = "查询企业单位详情", path = "/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        Unit unit = unitService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(unit);
		return baseVo;
    }
    
    @PostMapping(name = "新增企业单位", path = "/add")
    public BaseVo add(@Validated @RequestBody Unit unit) {
        unitService.add(unit);
        return new SuccessVo();
    }

    @PostMapping(name = "编辑企业单位", path = "/modify")
    public BaseVo modify(@RequestBody Unit unit) {
        unitService.update(unit);
        return new SuccessVo();
    }

    @DeleteMapping(name = "注销企业单位", path = "/{id}/disable")
    public BaseVo disableUnit(@PathVariable("id") Long id) {
        logger.info("注销企业单位:{}", id);
        unitService.disableStatus(id);
        return new SuccessVo();
    }

    @GetMapping(name = "根据ID删除企业单位", path = "/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        logger.info("删除企业单位:{}", id);
        unitService.delete(id);
        return new SuccessVo();
    }

}
