package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalStation;
import com.bit.module.manager.service.PortalStationService;
import com.bit.module.manager.vo.PortalStationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * PortalStation的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/manager/portalStation")
public class PortalStationController {
	private static final Logger logger = LoggerFactory.getLogger(PortalStationController.class);
	@Autowired
	private PortalStationService portalStationService;
	

    /**
     * 分页查询PortalStation列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PortalStationVO portalStationVO) {
    	//分页对象，前台传递的包含查询的参数

        return portalStationService.findByConditionPage(portalStationVO);
    }

    /**
     * 根据主键ID查询PortalStation
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        PortalStation portalStation = portalStationService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalStation);
		return baseVo;
    }
    
    /**
     * 新增PortalStation
     *
     * @param portalStation PortalStation实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PortalStation portalStation) {

        return portalStationService.add(portalStation);
    }
    /**
     * 修改PortalStation
     *
     * @param portalStation PortalStation实体
     * @return
     */
    @PutMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PortalStation portalStation) {
        return portalStationService.update(portalStation);
    }
    
    /**
     * 根据主键ID删除PortalStation
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        portalStationService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 查询树
     * @return
     */
    @GetMapping("/queryTree")
    public BaseVo queryTree(){
        return portalStationService.queryTree();
    }

    /**
     * 查询id
     * @return
     */
    @GetMapping("/queryId")
    public BaseVo queryId(){
        return portalStationService.queryId();
    }
}
