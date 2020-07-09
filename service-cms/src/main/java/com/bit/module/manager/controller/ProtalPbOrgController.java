package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.ProtalPbOrg;
import com.bit.module.manager.service.ProtalPbOrgService;
import com.bit.module.manager.vo.ProtalPbOrgVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * ProtalPbOrg的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/manager/protalPbOrg")
public class ProtalPbOrgController {
	private static final Logger logger = LoggerFactory.getLogger(ProtalPbOrgController.class);
	@Autowired
	private ProtalPbOrgService protalPbOrgService;
	

    /**
     * 分页查询ProtalPbOrg列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ProtalPbOrgVO protalPbOrgVO) {
    	//分页对象，前台传递的包含查询的参数

        return protalPbOrgService.findByConditionPage(protalPbOrgVO);
    }

    /**
     * @description: 党建组织导航下栏目展示
     * @author: liyang
     * @date: 2019-05-24
     **/
    @GetMapping("/pbOrgNavigationContend/{navigationId}/{categoryId}")
    public BaseVo pbOrgNavigationContend(@PathVariable(value = "navigationId") Long navigationId,
                                         @PathVariable(value = "categoryId") Long categoryId){

        return protalPbOrgService.getPbOrgNavigationContend(navigationId,categoryId);
    }

    /**
     * @description: 领导班子头像展示
     * @author: liyang
     * @date: 2019-05-07
     **/
    @GetMapping("/leaderImage")
    public BaseVo leaderImage(){

        return protalPbOrgService.getLeaderImage();
    }

    /**
     * 根据主键ID查询ProtalPbOrg
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        ProtalPbOrg protalPbOrg = protalPbOrgService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(protalPbOrg);
		return baseVo;
    }
    
    /**
     * 新增党建组织下栏目
     * @param protalPbOrg ProtalPbOrg实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody ProtalPbOrg protalPbOrg) {
        protalPbOrgService.add(protalPbOrg);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改ProtalPbOrg
     *
     * @param protalPbOrg ProtalPbOrg实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody ProtalPbOrg protalPbOrg) {
		protalPbOrgService.update(protalPbOrg);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除ProtalPbOrg
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        protalPbOrgService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
