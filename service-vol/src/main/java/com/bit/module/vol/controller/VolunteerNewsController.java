package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.VolNews;
import com.bit.module.vol.service.VolunteerNewsService;
import com.bit.module.vol.vo.VolNewsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 志愿者风采接口
 * @author Liy
 */
@RestController
@RequestMapping("/news")
public class VolunteerNewsController {

    /**
     * 志愿者风采实现
     */
    @Autowired
    private VolunteerNewsService volunteerNewsService;

    /**
     * 保存志愿者风采
     * @param volNews
     * @return
     */
    @RequestMapping("/createVolNew")
    public BaseVo createVolunteerNews(@RequestBody VolNews volNews){
        return volunteerNewsService.saveVolNews(volNews);
    }

    /**
     * 发布志愿者风采
     * @param volNews
     * @return
     */
    @RequestMapping("/commitVolunteerNews")
    public BaseVo commitVolunteerNews(@RequestBody VolNews volNews){
        return volunteerNewsService.commitVolNews(volNews);
    }

    /**
     * 分页查询活动记录
     * @param volNewsVo
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody VolNewsVo volNewsVo){
        return volunteerNewsService.listPage(volNewsVo);
    }

    /**
     * 审核文章申请
     * @param volNews
     * @return
     */
    @PostMapping("/auditNews")
    public BaseVo auditNews(@RequestBody VolNews volNews){
        return volunteerNewsService.auditNews(volNews);
    }

    /**
     *  退回文章
     * @param volNews
     * @return
     */
    @PostMapping("/backNews")
    public BaseVo backNews(@RequestBody VolNews volNews){
        return volunteerNewsService.backNews(volNews);
    }

    /**
     *  查看一条新闻
     * @param volNews
     * @return
     */
    @PostMapping("/getNewsById")
    public BaseVo getNewsById(@RequestBody VolNews volNews){
        return volunteerNewsService.getNewsById(volNews);
    }

    /**
     * 根据ID删除一篇文章
     * @param id
     * @return
     */
    @DeleteMapping("/delNewsById/{id}")
    public BaseVo delNewsById(@PathVariable(value = "id") Long id){
        return volunteerNewsService.delNewsById(id);
    }

    /**
     * 获取App端显示的新闻
     * @param volNewsVo
     * @return
     */
    @PostMapping("/getNewsForAppShow")
    public BaseVo getNewsForAppShow(@RequestBody VolNewsVo volNewsVo){
        return volunteerNewsService.getNewsForAppShow(volNewsVo);
    }

    /**
     * APP端查看一条新闻
     * @param volNews
     * @return
     */
    @PostMapping("/getNewsDetailForApp")
    public BaseVo getNewsDetailForApp(@RequestBody VolNews volNews){
        return volunteerNewsService.getNewsDetailForApp(volNews);
    }

}
