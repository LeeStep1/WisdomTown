package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.service.CommunityNewsService;
import com.bit.module.cbo.vo.CommunityNewsPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.bit.common.enumerate.CommunityNewsEumn.STATUS_PUBLISH;

/**
 * @description: APP新闻相关controller
 * @author: liyang
 * @date: 2019-08-31
 **/
@RestController
@RequestMapping("/app/AppCommunityNews")
public class AppCommunityNewsController {

    /**
     * 社区风采相关service
     */
    @Autowired
    private CommunityNewsService communityNewsService;

    /**
     * 查询社区风采列表
     * @author liyang
     * @date 2019-08-31
     * @param communityNewsPageVO : 查询条件
     * @return : BaseVo
    */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody CommunityNewsPageVO communityNewsPageVO){
        communityNewsPageVO.setStatus(STATUS_PUBLISH.getCode());
        return communityNewsService.findAll(communityNewsPageVO);
    }

    /**
     * 查询明细
     * @author liyang
     * @date 2019-08-31
     * @param id : ID
     * @return : BaseVo
    */
    @GetMapping("/detail/{id}")
    public BaseVo detail(@PathVariable Long id){
        return communityNewsService.detail(id);
    }
}
