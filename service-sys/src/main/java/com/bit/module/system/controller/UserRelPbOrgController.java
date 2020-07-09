package com.bit.module.system.controller;

import com.bit.module.system.bean.User;
import com.bit.module.system.service.UserRelPbOrgService;
import com.bit.module.system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-16 13:25
 */
@RestController
@RequestMapping("/userRelPbOrg")
public class UserRelPbOrgController {

    @Autowired
    private UserRelPbOrgService userRelPbOrgService;
    /**
     *
     * 功能描述: 根据党组织id查询所有用户id
     * @author: chenduo
     * @description:
     * @param:
     * @return:
     * @date: 2019-04-16 13:34
     */
    @GetMapping("/queryAllUserByPbOrgId/{pbOrgId}")
    public List<Long> queryAllUserByPbOrgId(@PathVariable(value = "pbOrgId")String pbOrgId){
        return userRelPbOrgService.queryAllUserByPbOrgId(pbOrgId);
    }

    /**
     * 根据组织IDS获取用户ID
     * @param pbOrgIds
     * @return
     */
    @PostMapping("/queryAllUserByPbOrgIds")
    public List<Long> queryAllUserByPbOrgIds(@RequestBody List<String> pbOrgIds) {
        return userRelPbOrgService.queryUserIdByOrgids(pbOrgIds);
    }

    /**
     * 批量查询党建管理员的身份证
     * @param orgIds
     * @return
     */
    @PostMapping("/findUserIdCardByOrgIds")
    public List<String> findUserIdCardByOrgIds(@RequestBody List<Long> orgIds){
        return userRelPbOrgService.findUserIdCardByOrgIds(orgIds);
    }

    /**
     * 根据党组织id查询用户身份证
     * @param pbOrgId
     * @return
     */
    @GetMapping("/queryUserIdCardByOrgId/{pbOrgId}")
    public List<String> queryUserIdCardByOrgId(@PathVariable(value = "pbOrgId")Long pbOrgId){
        return userRelPbOrgService.queryUserIdCardByOrgId(pbOrgId);
    }

    /**
     * 批量查询党建管理员的信息
     * @param orgIds
     * @return
     */
    @PostMapping("/findUserByOrgIds")
    public List<UserVO> findUserByOrgIds(@RequestBody List<Long> orgIds){
        return userRelPbOrgService.findUserByOrgIds(orgIds);
    }

}
