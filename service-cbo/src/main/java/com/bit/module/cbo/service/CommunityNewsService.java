package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.CommunityNews;
import com.bit.module.cbo.vo.CommunityNewsPageVO;

/**
 * @description: 社区新闻相关Service
 * @author: liyang
 * @date: 2019-08-29
 **/
public interface CommunityNewsService {

    /**
     * 新增社区新闻
     * @author liyang
     * @date 2019-08-29
     * @param communityNews : 新增详情
     * @return BaseVo
     */
    BaseVo add(CommunityNews communityNews);

    /**
     * 修改社区风采
     * @author liyang
     * @date 2019-08-30
     * @param communityNews : 修改详情
     * @return : BaseVo
     */
    BaseVo modify(CommunityNews communityNews);

    /**
     * 删除一条风采
     * @author liyang
     * @date 2019-08-30
     * @param id : 要删除的ID
     * @return : BaseVo
     */
    BaseVo delete(Long id);

    /**
     * 查询社区风采列表
     * @author liyang
     * @date 2019-08-30
     * @param communityNewsPageVO : 查询明细
     * @return : BaseVo
    */
    BaseVo findAll(CommunityNewsPageVO communityNewsPageVO);

    /**
     * 查询明细
     * @author liyang
     * @date 2019-08-31
     * @param id :  id
     * @return : BaseVo
     */
    BaseVo detail(Long id);

}
