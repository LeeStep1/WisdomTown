package com.bit.module.website.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.ServiceType;
import com.bit.module.manager.vo.ServiceTypeVO;

/**
 * @description: 特殊页面展示接口
 * @author: liyang
 * @date: 2019-05-09
 **/
public interface SpecialPageDisplayService {

    /**
     *  查询领导介绍
     * @author liyang
     * @date 2019-05-07
     * @param categoryId : 栏目ID
     * @return : BaseVo
    */
    BaseVo getLeaderIntroductionByCategoryId(Long categoryId);

    /**
     * 查询领导明细
     * @author liyang
     * @date 2019-05-09
     * @param contentId : 内容ID
     * @return : BaseVo
    */
    BaseVo getLeaderDetailById(Long contentId);

    /**
     * 党建组织导航下栏目展示
     * @author liyang
     * @date 2019-05-09
     * @param categoryId : 内容ID
     * @return : BaseVo
     */
    BaseVo getPbOrgNavigationContend(Long categoryId);

    /**
     * 党建组织导航下栏目展示
     * @author liyang
     * @date 2019-05-09
     * @param pageSize : 显示条数
     * @param pageNum : 显示页数
     * @return : BaseVo
     */
    BaseVo getLeaderImage(Integer pageSize,Integer pageNum);

    /**
     * 查询办事指南导航
     * @author liyang
     * @date 2019-05-10
     * @param navigationId : 导航ID
     * @return : BaseVo
     */
    BaseVo getGuidanceNavigation(Long navigationId);

    /**
     * 办事指南导航获取一级栏目下所有内容分页
     * @author liyang
     * @date 2019-05-10
     * @param navigationId : 导航ID
     * @param categoryId : 栏目ID
     * @param serviceTypeVo : 内容分页
     * @return : BaseVo
     */
    BaseVo getGuidanceAllContents(Long navigationId,Long categoryId,ServiceTypeVO serviceTypeVo);

    /**
     * 根据二级栏目ID获取指定指南下内容(分页)
     * @author liyang
     * @date 2019-05-13
     * @param navigationId : 导航ID
     * @param categoryId : 栏目ID
     * @param serviceTypeVo : 内容分页
     * @return : BaseVo
     */
    BaseVo getGuidanceContents(Long navigationId,Long categoryId,ServiceTypeVO serviceTypeVo);

    /**
     * 办事指南根据ID获取内容明细
     * @author liyang
     * @date 2019-05-13
     * @param serviceType : 查询明细
     * @return : BaseVo
     */
    BaseVo getGuidanceContentDetail(ServiceType serviceType);

    /**
     * 首页获取办事指南前N条（常用>二级栏目>二级栏目排序）
     * @author liyang
     * @date 2019-05-13
     * @param pageSize : 查询数目
     * @return : BaseVo
     */
    BaseVo getGuidanceContentForHomePage(Integer pageSize);

    /**
     * 魅力杨柳青展示，根据栏目ID获取内容
     * @author liyang
     * @date 2019-05-29
     * @param categoryId : 栏目ID
     * @return : BaseVo
     */
    BaseVo getGlamourShow(Long categoryId);
}
