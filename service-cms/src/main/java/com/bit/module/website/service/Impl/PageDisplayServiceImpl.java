package com.bit.module.website.service.Impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.bean.PortalNavigation;
import com.bit.module.manager.vo.PortalCategoryVO;
import com.bit.module.manager.vo.PortalContentVO;
import com.bit.module.website.dao.PageDisplayDao;
import com.bit.module.website.service.PageDisplayService;
import com.bit.util.FastDFSClient;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bit.common.cmsenum.cmsEnum.*;

/**
 * @description:  常规页面展示通用接口
 * @author: liyang
 * @date: 2019-05-07
 **/
@Service
public class PageDisplayServiceImpl extends BaseService implements PageDisplayService {

    @Autowired
    private PageDisplayDao pageDisplayDao;

    @Autowired
    private FastDFSClient fastDFSClient;

    /**
     * 根据站点获取所有导航
     * @author liyang
     * @date 2019-05-07
     * @param stationId : 站点ID
     * @return : BaseVo
    */
    @Override
    public BaseVo getAllNavigation(Long stationId) {
        PortalNavigation navigation = new PortalNavigation();
        navigation.setId(stationId);

        //状态  0 启用 1停用
        navigation.setStatus(USING_FLAG.getCode());

        //查询
        List<PortalNavigation> navigationList = pageDisplayDao.getAllNavigationSql(navigation);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(navigationList);

        return baseVo;
    }

    /**
     * 首页多个导航栏同时展示
     * @author liyang
     * @date 2019-05-08
     * @param portalCategoryVO ：栏目明细以及分页
     * @return : BaseVo
    */
    @Override
    public BaseVo getContentListByCategoryIdList(PortalCategoryVO portalCategoryVO) {

        //插入站点查询条件
        portalCategoryVO.setStatus(USING_FLAG.getCode());

        //批量查询
        PageHelper.startPage(portalCategoryVO.getPageNum(), portalCategoryVO.getPageSize());
        List<PortalContent> portalContentList = pageDisplayDao.getContentListByCategoryIdListSql(portalCategoryVO);

        //根据栏目名称分组
        Map<Long,List<PortalContent>> portalCategoryMap = portalContentList.stream().collect(Collectors.groupingBy(PortalContent::getCategoryId));

//        for (Map.Entry<Long, List<PortalContent>> m : portalCategoryMap.entrySet()) {
//            System.out.println("key:" + m.getKey() + " value:" + m.getValue());
//        }


        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalCategoryMap);
        return baseVo;
    }

    /**
     * 根据导航ID和栏目获取单个下内容明细
     * @author liyang
     * @date 2019-05-08
     * @param portalContentVO : 栏目明细以及分页
     * @return : BaseVo
    */
    @Override
    public BaseVo getContentListByCategoryId(PortalContentVO portalContentVO) {

        //插入站点查询条件
        portalContentVO.setStatus(USING_FLAG.getCode());
        portalContentVO.setPublishStatus(PUBLISH_FLAG.getCode());

        //分页查询
        PageHelper.startPage(portalContentVO.getPageNum(), portalContentVO.getPageSize());
        List<PortalContent> portalContentList = pageDisplayDao.getContentListByCategoryIdSql(portalContentVO);
        PageInfo<PortalContent> pageInfo = new PageInfo<PortalContent>(portalContentList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 根据栏目ID和内容ID获取内容明细
     * @author liyang
     * @date 2019-05-09
     * @param portalContent：栏目条件
     * @return  BaseVo
     */
    @Override
    public BaseVo getContentDetailByContentId(PortalContent portalContent) {

        //插入站点查询条件
        portalContent.setStatus(USING_FLAG.getCode());
        portalContent.setPublishStatus(PUBLISH_FLAG.getCode());

        //查询
        PortalContent pc = pageDisplayDao.getContentDetailByContentIdSql(portalContent);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pc);

        return baseVo;
    }

    /**
     * 根据根据栏目ID 查询栏目下的推荐内容
     * @author liyang
     * @date 2019-05-21
     * @param stationId : 站点ID
     * @param portalContentVO : 栏目ID,内容ID,查询条数
     * @return : BaseVo
     */
    @Override
    public BaseVo getRecommendContentList(Long stationId, PortalContentVO portalContentVO) {

        portalContentVO.setStatus(UNDEL_FLAG.getCode());
        portalContentVO.setPublishStatus(PUBLISH_FLAG.getCode());

        //随机查询不包含指定ID的指定栏目推荐
        List<PortalContent> portalContentList = pageDisplayDao.getRecommendContentList(portalContentVO);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalContentList);

        return baseVo;
    }


}
