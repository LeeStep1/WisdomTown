package com.bit.module.website.dao;

import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.bean.PortalNavigation;
import com.bit.module.manager.vo.PortalCategoryVO;
import com.bit.module.manager.vo.PortalContentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 页面展示接口
 * @author: liyang
 * @date: 2019-05-07
 **/
public interface PageDisplayDao {

    /**
     * 根据站点获得所有导航
     * @author liyang
     * @date 2019-05-07
     * @param navigation : 站点详情
     * @return : List<Navigation> 导航集合
    */
    List<PortalNavigation> getAllNavigationSql(@Param("navigation") PortalNavigation navigation);

    /**
     * 首页多个导航栏同时展示
     * @author liyang
     * @date 2019-05-08
     * @param portalCategoryVO : 栏目ID集合 和 站点ID
     * @return : List<PortalContent>
    */
    List<PortalContent> getContentListByCategoryIdListSql(@Param("portalCategoryVO") PortalCategoryVO portalCategoryVO);

    /**
     * 根据导航ID和栏目获取单个下内容明细
     * @author liyang
     * @date 2019-05-08
     * @param portalContentVO : 栏目ID 和 站点ID
     * @return : List<PortalContent>
     */
    List<PortalContent> getContentListByCategoryIdSql(@Param("portalContentVO") PortalContentVO portalContentVO);

    /**
     * 根据栏目ID和内容ID获取内容明细
     * @author liyang
     * @date 2019-05-09
     * @param portalContent : 栏目ID 站点ID  内容ID
     * @return : PortalContent
    */
    PortalContent getContentDetailByContentIdSql(@Param("portalContent") PortalContent portalContent);

    /**
     * 根据根据栏目ID 查询栏目下的推荐内容
     * @author liyang
     * @date 2019-05-21
     * @param portalContentVO : 栏目ID,内容ID,查询条数
     * @return : BaseVo
     */
    List<PortalContent> getRecommendContentList(@Param("portalContentVO") PortalContentVO portalContentVO);
}
