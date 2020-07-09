package com.bit.module.website.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.FileInfo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.vo.PortalCategoryVO;
import com.bit.module.manager.vo.PortalContentVO;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @description: 页面展示接口
 * @author: liyang
 * @date: 2019-05-07
 **/
public interface PageDisplayService {

    /**
     * 根据站点获取所有导航
     * @author liyang
     * @date 2019-05-07
     * @param stationId : 站点id
     * @return : BaseVo
    */
    BaseVo getAllNavigation(Long stationId);

    /**
     * 首页多个导航栏同时展示
     * @author liyang
     * @date 2019-05-08
     * @param portalCategoryVO ：栏目明细以及分页
     * @return : BaseVo
    */
    BaseVo getContentListByCategoryIdList(PortalCategoryVO portalCategoryVO);

    /**
     * 根据导航ID和栏目获取单个下内容明细
     * @author liyang
     * @date 2019-05-08
     * @param portalContentVO ：栏目明细以及分页
     * @return : BaseVo
     */
    BaseVo getContentListByCategoryId(PortalContentVO portalContentVO);

    /**
     * 根据栏目ID和内容ID获取内容明细
     * @author liyang
     * @date 2019-05-09
     * @param portalContent：栏目条件
     * @return  BaseVo
     */
    BaseVo getContentDetailByContentId(PortalContent portalContent);

    /**
     * 根据根据栏目ID 查询栏目下的推荐内容
     * @author liyang
     * @date 2019-05-21
     * @param stationId : 站点ID
     * @param portalContentVO : 栏目ID,内容ID,查询条数
     * @return : BaseVo
     */
    BaseVo getRecommendContentList(Long stationId,PortalContentVO portalContentVO);


}
