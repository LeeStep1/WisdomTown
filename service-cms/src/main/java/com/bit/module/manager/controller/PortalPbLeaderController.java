package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalPbLeader;
import com.bit.module.manager.service.PortalPbLeaderService;
import com.bit.module.manager.vo.PortalPbLeaderVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 领导班子头像的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/manager/portalPbLeader")
public class PortalPbLeaderController {
	private static final Logger logger = LoggerFactory.getLogger(PortalPbLeaderController.class);
	@Autowired
	private PortalPbLeaderService portalPbLeaderService;

    /**
     * 获取领导班子头像列表
     */
    @GetMapping("/portalPbLeaders")
    public BaseVo portalPbLeaders() {

        return portalPbLeaderService.findAll();
    }

    /**
     * 分页查询PortalPbLeader列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PortalPbLeaderVO portalPbLeaderVO) {
    	//分页对象，前台传递的包含查询的参数

        return portalPbLeaderService.findByConditionPage(portalPbLeaderVO);
    }

    /**
     * 根据主键ID查询PortalPbLeader
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        PortalPbLeader portalPbLeader = portalPbLeaderService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalPbLeader);
		return baseVo;
    }
    
    /**
     * 新增领导班子头像
     * @param portalPbLeader PortalPbLeader实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PortalPbLeader portalPbLeader, HttpServletRequest request) {
        portalPbLeaderService.add(portalPbLeader,request);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改PortalPbLeader
     *
     * @param portalPbLeader PortalPbLeader实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PortalPbLeader portalPbLeader) {
		portalPbLeaderService.update(portalPbLeader);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除PortalPbLeader
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        return portalPbLeaderService.delete(id,request);
    }

    /**
     * 领导班子头像排序
     * @param portalPbLeaderList
     * @return
     */
    @PostMapping("/portalPbLeaderSort")
    public BaseVo serviceTypeSort(@RequestBody List<PortalPbLeader> portalPbLeaderList){
        return portalPbLeaderService.portalPbLeaderSort(portalPbLeaderList);
    }

}
