package com.bit.module.website.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.ServiceType;
import com.bit.module.manager.vo.ServiceTypeVO;
import com.bit.module.website.service.SpecialPageDisplayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 特殊页面展示
 * @author: liyang
 * @date: 2019-05-09
 **/
@RestController
@RequestMapping("/specialPageDisplay")
public class SpecialPageDisplayController {

    @Autowired
    private SpecialPageDisplayService specialPageDisplayService;

    /**
     * @description: 领导介绍接口
     * @author: liyang
     * @date: 2019-05-07
     **/
    @GetMapping("/leadersIntroduction/{categoryId}")
    public BaseVo leadersIntroduction(@PathVariable(value = "categoryId") Long categoryId){

         return specialPageDisplayService.getLeaderIntroductionByCategoryId(categoryId);
    }

    /**
     * @description: 领导介绍明细
     * @author: liyang
     * @date: 2019-05-07
     **/
    @GetMapping("/leaderDetail/{contentId}")
    public BaseVo leaderDetail(@PathVariable(value = "contentId") Long contentId){

        return specialPageDisplayService.getLeaderDetailById(contentId);
    }

    /**
     * @description: 党建组织导航下栏目展示
     * @author: liyang
     * @date: 2019-05-07
     **/
    @GetMapping("/pbOrgNavigationContend/{categoryId}")
    public BaseVo pbOrgNavigationContend(@PathVariable(value = "categoryId") Long categoryId){

        return specialPageDisplayService.getPbOrgNavigationContend(categoryId);
    }

    /**
     * @description: 领导班子头像展示
     * @author: liyang
     * @date: 2019-05-07
     **/
    @GetMapping("/leaderImage/{pageSize}/{pageNum}")
    public BaseVo leaderImage(@PathVariable(value = "pageSize") Integer pageSize,
                              @PathVariable(value = "pageNum") Integer pageNum){

        return specialPageDisplayService.getLeaderImage(pageSize,pageNum);
    }

    /**
     * 查询办事指南导航
     * @author liyang
     * @date 2019-05-10
     * @param navigationId : 导航ID
     * @return : BaseVo
    */
    @GetMapping("/guidanceNavigation/{navigationId}")
    public BaseVo guidanceNavigation(@PathVariable(value = "navigationId") Long navigationId){
        return specialPageDisplayService.getGuidanceNavigation(navigationId);
    }


    /**
     * 办事指南导航获取一级栏目下所有内容分页
     * @author liyang
     * @date 2019-05-10
     * @param navigationId : 导航ID
     * @param categoryId :  一级栏目ID
     * @param serviceTypeVo : 内容分页
     * @return : BaseVo
    */
    @PostMapping("/guidanceAllContents/{navigationId}/{categoryId}")
    public BaseVo guidanceAllContents(@PathVariable(value = "navigationId") Long navigationId,
                                      @PathVariable(value = "categoryId") Long categoryId,
                                      @RequestBody ServiceTypeVO serviceTypeVo){

        return specialPageDisplayService.getGuidanceAllContents(navigationId,categoryId,serviceTypeVo);
    }

    /**
     * 根据二级栏目ID获取指定指南下内容(分页)
     * @author liyang
     * @date 2019-05-13
     * @param navigationId : 导航ID
     * @param categoryId : 二级栏目ID
     * @param serviceType : 内容分页
     * @return : BaseVo
     */
    @PostMapping("/guidanceContents/{navigationId}/{categoryId}")
    public BaseVo guidanceContents(@PathVariable(value = "navigationId") Long navigationId,
                                      @PathVariable(value = "categoryId") Long categoryId,
                                      @RequestBody ServiceTypeVO serviceTypeVo){

        return specialPageDisplayService.getGuidanceContents(navigationId,categoryId,serviceTypeVo);
    }

    /**
     * 办事指南根据ID获取内容明细
     * @author liyang
     * @date 2019-05-13
     * @param navigationId : 导航ID
     * @param categoryId :  栏目ID
     * @param serviceTypeId : 内容ID
     * @return : BaseVo
    */
    @GetMapping("/guidanceContentDetail/{navigationId}/{categoryId}/{serviceTypeId}")
    public BaseVo guidanceContentDetail(@PathVariable(value = "navigationId") Long navigationId,
                                         @PathVariable(value = "categoryId") Long categoryId,
                                         @PathVariable(value = "serviceTypeId") Long serviceTypeId){
        ServiceType serviceType = new ServiceType();
        serviceType.setCategoryId(categoryId);
        serviceType.setId(serviceTypeId);

        return specialPageDisplayService.getGuidanceContentDetail(serviceType);
    }

    /**
     * 首页获取办事指南前N条（常用>二级栏目>二级栏目排序）
     * @author liyang
     * @date 2019-05-13
     * @param pageSize : 查询数目
     * @return : BaseVo
     */
    @GetMapping("/guidanceContentHomePage/{pageSize}")
    public BaseVo guidanceContentHomePage(@PathVariable(value = "pageSize") Integer pageSize){

        return specialPageDisplayService.getGuidanceContentForHomePage(pageSize);
    }

    /**
     * @description: 魅力杨柳青根据栏目ID 查询内容
     * @author: liyang
     * @date: 2019-05-29
     **/
    @GetMapping("/glamourShow/{categoryId}")
    public BaseVo glamourShow(@PathVariable(value = "categoryId") Long categoryId){

        return specialPageDisplayService.getGlamourShow(categoryId);
    }
}
