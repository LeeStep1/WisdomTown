package com.bit.module.website.service.Impl;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.*;
import com.bit.module.manager.vo.PortalPbLeaderVO;
import com.bit.module.manager.vo.ServiceTypeVO;
import com.bit.module.website.dao.SpecialPageDisplayDao;
import com.bit.module.website.service.SpecialPageDisplayService;
import com.bit.util.CommonUntil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bit.common.Const.ID_LENGTH;
import static com.bit.common.cmsenum.cmsEnum.UNDEL_FLAG;
import static com.bit.common.cmsenum.cmsEnum.USING_FLAG;

/**
 * @description: 特殊页面展示接口
 * @author: liyang
 * @date: 2019-05-09
 **/
@Service
public class SpecialPageDisplayServiceImpl implements SpecialPageDisplayService {

    /**
     * 特殊栏目数据库操作
     */
    @Autowired
    private SpecialPageDisplayDao specialPageDisplayDao;

    /**
     * 通用工具类
     */
    @Autowired
    private CommonUntil commonUntil;

    /**
     * 查询领导介绍
     * @author liyang
     * @date 2019-05-09
     * @param categoryId : 栏目ID
     * @return : BaseVo
    */
    @Override
    public BaseVo getLeaderIntroductionByCategoryId(Long categoryId) {

        //设置查询条件
        PortalOaLeader portalOaLeader = new PortalOaLeader();
        portalOaLeader.setCategoryId(categoryId);
        portalOaLeader.setStatus(USING_FLAG.getCode());
        portalOaLeader.setDelStatus(UNDEL_FLAG.getCode());

        //查询
        List<PortalOaLeader> portalOaLeaderList = specialPageDisplayDao.getLeaderIntroductionByCategoryIdSql(portalOaLeader);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalOaLeaderList);

        return baseVo;
    }

    /**
     * 查询领导明细
     * @author liyang
     * @date 2019-05-09
     * @param contentId : 内容ID
     * @return : BaseVo
     */
    @Override
    public BaseVo getLeaderDetailById(Long contentId) {

        //设置查询条件
        PortalOaLeader portalOaLeader = new PortalOaLeader();
        portalOaLeader.setId(contentId);
        portalOaLeader.setStatus(USING_FLAG.getCode());

        //查询
        PortalOaLeader pol = specialPageDisplayDao.getLeaderDetailByIdSql(portalOaLeader);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pol);

        return baseVo;
    }

    /**
     * 党建组织导航下栏目展示
     * @author liyang
     * @date 2019-05-09
     * @param categoryId : 内容ID
     * @return : BaseVo
     */
    @Override
    public BaseVo getPbOrgNavigationContend(Long categoryId) {

        ProtalPbOrg pol = specialPageDisplayDao.getPbOrgNavigationContendSql(categoryId);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pol);

        return baseVo;

    }

    /**
     * 党建组织导航下栏目展示
     * @author liyang
     * @date 2019-05-09
     * @param pageSize : 显示条数
     * @param pageNum : 显示页数
     * @return : BaseVo
     */
    @Override
    public BaseVo getLeaderImage(Integer pageSize, Integer pageNum) {

        PortalPbLeaderVO portalPbLeader = new PortalPbLeaderVO();
        portalPbLeader.setPageNum(pageNum);
        portalPbLeader.setPageSize(pageSize);
        portalPbLeader.setStatus(USING_FLAG.getCode());
        portalPbLeader.setDelStatus(UNDEL_FLAG.getCode());

        //分页查询
        PageHelper.startPage(portalPbLeader.getPageNum(), portalPbLeader.getPageSize());

        List<PortalPbLeader> pblList = specialPageDisplayDao.getLeaderImageSql(portalPbLeader);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pblList);

        return baseVo;
    }

    /**
     * 查询办事指南导航
     * @author liyang
     * @date 2019-05-10
     * @param navigationId : 导航ID
     * @return : BaseVo
     */
    @Override
    public BaseVo getGuidanceNavigation(Long navigationId) {

        //查询栏目和子栏目
        Long InitCategoryId = 0L;
        PortalCategory portalCategory = new PortalCategory();
        portalCategory.setNavigationId(navigationId);
        portalCategory.setId(InitCategoryId);

        //算出一级栏目
        Integer idLength = ID_LENGTH;
        List<PortalCategory> portalCategoryReturn = commonUntil.checkDownCategory(portalCategory,idLength);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalCategoryReturn);

        return baseVo;
    }

    /**
     * 办事指南导航获取一级栏目下所有内容分页
     * @author liyang
     * @date 2019-05-10
     * @param navigationId : 导航ID
     * @param categoryId : 栏目ID
     * @param serviceTypeVo : 内容分页
     * @return : BaseVo
     */
    @Override
    public BaseVo getGuidanceAllContents(Long navigationId, Long categoryId, ServiceTypeVO serviceTypeVo) {

        //设置查询条件
        serviceTypeVo.setStatus(USING_FLAG.getCode());
        serviceTypeVo.setCategoryId(categoryId);

        //分页查询
        PageHelper.startPage(serviceTypeVo.getPageNum(), serviceTypeVo.getPageSize());
        List<ServiceType> serviceTypeList = specialPageDisplayDao.getAllContentByCategoryIdSql(serviceTypeVo);
        PageInfo<ServiceType> pageInfo = new PageInfo<ServiceType>(serviceTypeList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 根据二级栏目ID获取指定指南下内容(分页)
     * @author liyang
     * @date 2019-05-13
     * @param navigationId : 导航ID
     * @param categoryId : 栏目ID
     * @param serviceTypeVo : 内容分页
     * @return : BaseVo
     */
    @Override
    public BaseVo getGuidanceContents(Long navigationId, Long categoryId, ServiceTypeVO serviceTypeVo) {

        //设置查询条件
        serviceTypeVo.setStatus(USING_FLAG.getCode());
        serviceTypeVo.setCategoryId(categoryId);

        //分页查询
        PageHelper.startPage(serviceTypeVo.getPageNum(), serviceTypeVo.getPageSize());
        List<ServiceType> serviceTypeList = specialPageDisplayDao.getContentByCategoryIdSql(serviceTypeVo);
        PageInfo<ServiceType> pageInfo = new PageInfo<ServiceType>(serviceTypeList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 办事指南根据ID获取内容明细
     * @author liyang
     * @date 2019-05-13
     * @param serviceType : 查询条件
     * @return : BaseVo
     */
    @Override
    public BaseVo getGuidanceContentDetail(ServiceType serviceType) {

        serviceType.setStatus(USING_FLAG.getCode());
        ServiceType serviceTypeReturn = specialPageDisplayDao.getGuidanceContentDetailSql(serviceType);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(serviceTypeReturn);
        return baseVo;
    }

    /**
     * 首页获取办事指南前N条（常用>二级栏目>二级栏目排序）
     * @author liyang
     * @date 2019-05-13
     * @param pageSize : 查询数目
     * @return : BaseVo
     */
    @Override
    public BaseVo getGuidanceContentForHomePage(Integer pageSize) {
        Integer status = UNDEL_FLAG.getCode();
        List<ServiceType> serviceTypeList = specialPageDisplayDao.getGuidanceContentForHomePageSql(status,pageSize);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(serviceTypeList);

        return baseVo;
    }

    /**
     * 魅力杨柳青展示，根据栏目ID获取内容
     * @author liyang
     * @date 2019-05-29
     * @param categoryId : 栏目ID
     * @return : BaseVo
     */
    @Override
    public BaseVo getGlamourShow(Long categoryId) {
        ProtalPbOrg pol = specialPageDisplayDao.getGlamourShowSql(categoryId);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pol);

        return baseVo;

    }


}
