package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalNavigation;
import com.bit.module.manager.service.PortalNavigationService;
import com.bit.module.manager.vo.PortalNavigationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * PortalNavigation的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/manager/portalNavigation")
public class PortalNavigationController {
	private static final Logger logger = LoggerFactory.getLogger(PortalNavigationController.class);
	@Autowired
	private PortalNavigationService portalNavigationService;

    /**
     * 分页查询PortalNavigation列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PortalNavigationVO portalNavigationVO) {
    	//分页对象，前台传递的包含查询的参数
        return portalNavigationService.findByConditionPage(portalNavigationVO);
    }

    /**
     * 根据主键ID查询PortalNavigation
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        PortalNavigation portalNavigation = portalNavigationService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalNavigation);
		return baseVo;
    }
    
    /**
     * 新增PortalNavigation
     *
     * @param portalNavigation PortalNavigation实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PortalNavigation portalNavigation) {
        return portalNavigationService.add(portalNavigation);
    }
    /**
     * 修改PortalNavigation
     *
     * @param portalNavigation PortalNavigation实体
     * @return
     */
    @PutMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PortalNavigation portalNavigation) {
        return portalNavigationService.update(portalNavigation);
    }
    
    /**
     * 根据主键ID删除PortalNavigation
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        portalNavigationService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 查询id
     * @param portalNavigation
     * @return
     */
    @PostMapping("/queryId")
    public BaseVo queryId(@RequestBody PortalNavigation portalNavigation){
        return portalNavigationService.queryId(portalNavigation);
    }

    /**
     * 根据站点获取导航树
     * @author liyang
     * @date 2019-05-14
     * @param stationId : 站点ID
     * @return : BaseVo
    */
    @GetMapping("/NavigationTree/{stationId}")
    public BaseVo NavigationTree(@PathVariable(value = "stationId") Long stationId){
        return portalNavigationService.getNavigationTreeByStationId(stationId);
    }

    /**
     * 获取导航树(内容发布时使用)
     * @author liyang
     * @date 2019-06-03
     * @param stationId : 站点ID
     * @return : BaseVo
     */
    @GetMapping("/NavigationTreeExSpecial/{stationId}")
    public BaseVo NavigationTreeExSpecial(@PathVariable(value = "stationId") Long stationId){
        return portalNavigationService.getNavigationTreeExSpecial(stationId);
    }


}
