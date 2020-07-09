package com.bit.module.website.dao;

import com.bit.module.manager.bean.*;
import com.bit.module.manager.vo.PortalPbLeaderVO;
import com.bit.module.manager.vo.ServiceTypeVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description:
 * @author: liyang
 * @date: 2019-05-09
 **/
public interface SpecialPageDisplayDao {

    /**
     * 查询领导介绍
     * @author liyang
     * @date 2019-05-07
     * @param portalOaLeader : 查询条件
     * @return : List<Navigation> 导航集合
    */
    List<PortalOaLeader> getLeaderIntroductionByCategoryIdSql(@Param("portalOaLeader") PortalOaLeader portalOaLeader);

    /**
     * 查询领导明细
     * @author liyang
     * @date 2019-05-09
     * @param portalOaLeader : 查询条件
     * @return : BaseVo
     */
    PortalOaLeader getLeaderDetailByIdSql(@Param("portalOaLeader") PortalOaLeader portalOaLeader);

    /**
     * 党建组织导航下栏目展示
     * @author liyang
     * @date 2019-05-09
     * @param categoryId : 栏目ID
     * @return : BaseVo
     */
    ProtalPbOrg getPbOrgNavigationContendSql(@Param("categoryId") Long categoryId);

    /**
     * 党建组织导航下栏目展示
     * @author liyang
     * @date 2019-05-09
     * @param portalPbLeader : 筛选条件
     * @return : List<PortalPbLeader>
    */
    List<PortalPbLeader> getLeaderImageSql(@Param("portalPbLeader") PortalPbLeaderVO portalPbLeader);

    /**
     * 查询一级栏目
     * @author liyang
     * @date 2019-05-09
     * @param idLength : 位数
     * @param portalCategory : 查询条件
     * @return : List<PortalCategory>
     */
    List<PortalCategory> getTopCategoryByIdSql(@Param("idLength") Integer idLength,@Param("portalCategory") PortalCategory portalCategory);

    /**
     * 查询子栏目
     * @author liyang
     * @date 2019-05-10
     * @param portalCategory : 查询条件
     * @param newIdLength : ID位数
     * @return : List<PortalCategory>
    */
    List<PortalCategory> getSubCategoryByIdSql(@Param("portalCategory") PortalCategory portalCategory,@Param("newIdLength") Integer newIdLength);

    /**
     * 查询一级栏目下所有内容(办事指南)
     * @author liyang
     * @date 2019-05-10
     * @param serviceTypeVo : 查询条件
     * @return : List<PortalCategory>
     */
    List<ServiceType> getAllContentByCategoryIdSql(@Param("serviceTypeVo") ServiceTypeVO serviceTypeVo);

    /**
     * 根据二级栏目ID获取指定指南下内容(分页)
     * @author liyang
     * @date 2019-05-13
     * @param serviceTypeVo : 查询条件
     * @return : List<PortalCategory>
     */
    List<ServiceType> getContentByCategoryIdSql(@Param("serviceTypeVo") ServiceTypeVO serviceTypeVo);

    /**
     * 办事指南根据ID获取内容明细
     * @author liyang
     * @date 2019-05-13
     * @param serviceType : 查询条件
     * @return : BaseVo
     */
    ServiceType getGuidanceContentDetailSql(@Param("serviceType") ServiceType serviceType);

    /**
     * 首页获取办事指南前N条（常用>二级栏目>二级栏目排序）
     * @author liyang
     * @date 2019-05-13
     * @param status : 筛选条件
     * @param pageSize : 查询数目
     * @return : BaseVo
     */
    List<ServiceType> getGuidanceContentForHomePageSql(@Param("status") Integer status,@Param("pageSize") Integer pageSize);

    /**
     * 魅力杨柳青展示，根据栏目ID获取内容
     * @author liyang
     * @date 2019-05-29
     * @param categoryId : 栏目ID
     * @return : BaseVo
     */
    ProtalPbOrg getGlamourShowSql(Long categoryId);
}
