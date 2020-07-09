package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalCategory;
import com.bit.module.manager.service.PortalCategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * PortalCategory的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/manager/portalCategory")
public class PortalCategoryController {
	private static final Logger logger = LoggerFactory.getLogger(PortalCategoryController.class);
	@Autowired
	private PortalCategoryService portalCategoryService;
	

    /**
     * 分页查询PortalCategory列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PortalCategory portalCategory) {
    	//分页对象，前台传递的包含查询的参数

        return portalCategoryService.findByConditionPage(portalCategory);
    }

    /**
     * 根据主键ID查询PortalCategory
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        PortalCategory portalCategory = portalCategoryService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalCategory);
		return baseVo;
    }
    
    /**
     * 新增PortalCategory
     *
     * @param portalCategory PortalCategory实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PortalCategory portalCategory) {
        return portalCategoryService.add(portalCategory);
    }
    /**
     * 修改PortalCategory
     * @param portalCategory PortalCategory实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PortalCategory portalCategory) {
		portalCategoryService.update(portalCategory);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除PortalCategory
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        portalCategoryService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }


    /**
     * 新增个人/法人服务类型
     * @author liyang
     * @date 2019-05-13
     * @param portalCategory : 栏目明细
     * @return : BaseVo
     */
    @PostMapping("/serviceType")
    public BaseVo serviceType(@Valid @RequestBody PortalCategory portalCategory){

        return portalCategoryService.addServiceType(portalCategory);
    }

    /**
     * 个人服务 or 法人服务 类型分页查询
     * @author chenduo
     * @param portalCategory
     * @return
     */
    @PostMapping("/serviceTypeListPage")
    public BaseVo serviceTypeListPage(@RequestBody PortalCategory portalCategory){
        return portalCategoryService.serviceTypeListPage(portalCategory);
    }

    /**
     * 个人服务 or 法人服务 批量更新服务排序
     * @param portalCategoryList
     * @return
     */
    @PostMapping("/serviceSort")
    public BaseVo serviceSort(@RequestBody List<PortalCategory> portalCategoryList){
        return portalCategoryService.serviceSort(portalCategoryList);
    }
}
