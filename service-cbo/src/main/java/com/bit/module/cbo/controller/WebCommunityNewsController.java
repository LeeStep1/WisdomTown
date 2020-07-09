package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.CommunityNews;
import com.bit.module.cbo.service.CommunityNewsService;
import com.bit.module.cbo.vo.CommunityNewsPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description: 社区新闻相关Controller
 * @author: liyang
 * @date: 2019-08-29
 **/
@RestController
@RequestMapping("/web/WebCommunityNews")
public class WebCommunityNewsController {

    /**
     * 新闻相关Service
     */
    @Autowired
    private CommunityNewsService communityNewsService;

    /**
     * 新增社区新闻
     * @author liyang
     * @date 2019-08-29
     * @param communityNews : 新增详情
     * @return BaseVo
    */
    @PostMapping("/add")
    public BaseVo add(@RequestBody @Valid CommunityNews communityNews){

        return communityNewsService.add(communityNews);

    }

    /**
     * 修改社区风采
     * @author liyang
     * @date 2019-08-30
     * @param communityNews : 修改详情
     * @return : BaseVo
    */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody @Valid CommunityNews communityNews){
        return communityNewsService.modify(communityNews);
    }

    /**
     * 删除一条风采
     * @author liyang
     * @date 2019-08-30
     * @param id : 要删除的ID
     * @return : BaseVo
    */
    @DeleteMapping("/delete/{id}")
    public BaseVo delete(@PathVariable("id") Long id){
        return communityNewsService.delete(id);
    }

    /**
     * 查询社区风采列表
     * @author liyang
     * @date 2019-08-30
     * @param communityNewsPageVO : 查询条件
     * @return : BaseVo
    */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody CommunityNewsPageVO communityNewsPageVO){
        return communityNewsService.findAll(communityNewsPageVO);
    }

    /**
     * 查询明细
     * @author liyang
     * @date 2019-08-31
     * @param id :  id
     * @return : BaseVo
    */
    @GetMapping("/detail/{id}")
    public BaseVo detail(@PathVariable(value = "id") Long id){
        return communityNewsService.detail(id);
    }

}
