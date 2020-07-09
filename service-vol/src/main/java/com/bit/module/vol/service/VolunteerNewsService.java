package com.bit.module.vol.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.VolNews;
import com.bit.module.vol.vo.VolNewsVo;

public interface VolunteerNewsService {

    /**
     * 创建志愿者风采信息
     * @param volNews
     * @return
     */
    BaseVo saveVolNews(VolNews volNews);

    /**
     * 提交志愿者风采信息
     * @param volNews
     * @return
     */
    BaseVo commitVolNews(VolNews volNews);

    /**
     * 分页查询志愿者风采信息
     * @param volNewsVo
     * @return
     */
    BaseVo listPage(VolNewsVo volNewsVo);

    /**
     * 审核文章
     * @param volNews
     * @return
     */
    BaseVo auditNews(VolNews volNews);

    /**
     * 退回申请
     * @param volNews
     * @return
     */
    BaseVo backNews(VolNews volNews);

    /**
     * 查看一条新闻
     * @param volNews
     * @return
     */
    BaseVo getNewsById(VolNews volNews);

    /**
     * 根据ID删除一篇文章
     * @param id
     * @return
     */
    BaseVo delNewsById(Long id);

    /**
     * 获取APP端显示文章列表
     * @param volNewsVo
     * @return
     */
    BaseVo getNewsForAppShow(VolNewsVo volNewsVo);

    /**
     * 获取APP端显示文章明细
     * @param volNews
     * @return
     */
    BaseVo getNewsDetailForApp(VolNews volNews);
}
