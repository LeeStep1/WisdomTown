package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.AdminLogin;
import com.bit.module.manager.bean.PortalUser;
import com.bit.module.manager.service.AdminLoginService;
import com.bit.module.manager.vo.PortalUserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 15:37
 */
@RestController
@RequestMapping("/manager")
public class AdminController {

    @Autowired
    private AdminLoginService adminLoginService;
    /**
     * admin登陆
     * @param adminLogin
     * @return
     */
    @PostMapping(value = "/login")
    public BaseVo adminLogin(@RequestBody AdminLogin adminLogin){
        return adminLoginService.adminLogin(adminLogin);
    }

    /**
     * admin登出
     * @return
     */
    @GetMapping(value = "/logout")
    public BaseVo adminLogout(){
        return adminLoginService.adminLogout();
    }

    /**
     * mongo分页测试
     * @return
     */
  /*  @PostMapping(value = "/mongo")
    public BaseVo mongotest(){
        return adminLoginService.mongotest();
    }*/

    /**
     * 用户新增
     * @author liyang
     * @date 2019-06-11
     * @param user : 用户详情
     * @return : BaseVo
    */
    @PostMapping("/add")
    public BaseVo addUser(@Valid @RequestBody PortalUser user){
        return adminLoginService.add(user);
    }

    /**
     * 用户列表展示
     * @author liyang
     * @date 2019-06-11
     * @return : BaseVo
     */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody PortalUserVo portalUserVo){
        return adminLoginService.findAll(portalUserVo);
    }

    /**
     * 修改用户
     * @author liyang
     * @date 2019-06-11
     * @return : BaseVo
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody PortalUser portalUser){
        return adminLoginService.update(portalUser);
    }

    /**
     * 删除用户
     * @author liyang
     * @date 2019-06-11
     * @return : BaseVo
     */
    @DeleteMapping("/delete/{id}")
    public BaseVo update(@PathVariable Long id){
        return adminLoginService.delete(id);
    }

}
