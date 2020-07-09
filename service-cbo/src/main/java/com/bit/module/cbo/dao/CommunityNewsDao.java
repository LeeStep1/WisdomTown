package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.CommunityNews;
import com.bit.module.cbo.vo.CommunityNewsPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 社区新闻相关dao
 * @author: liyang
 * @date: 2019-08-29
 **/
public interface CommunityNewsDao {

    /**
     * 新增社区风采
     * @param communityNews 社区风采详情
     */
    void add(CommunityNews communityNews);

    /**
     * 根据ID删除内容
     * @param id ID
     * @return
     */
    int deleteByPrimaryKey(Long id);

    /**
     * 查询明细
     * @param id 查询ID
     * @return
     */
    CommunityNews selectByPrimaryKey(Long id);

    /**
     * 修改社区风采
     * @param communityNews 修改详情
     * @return
     */
    int modify(CommunityNews communityNews);

    /**
     * 查询社区风采列表
     * @param communityNewsPageVO 查询明细
     * @return
     */
    List<CommunityNews> findAll(@Param("news") CommunityNewsPageVO communityNewsPageVO);
}
