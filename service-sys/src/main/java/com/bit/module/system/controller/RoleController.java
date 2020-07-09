package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Role;
import com.bit.module.system.service.RoleRelResourceService;
import com.bit.module.system.service.RoleService;
import com.bit.module.system.vo.ResourcesRoleVo;
import com.bit.module.system.vo.RoleVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Role的相关请求
 * @author zhangjie
 * @date 2018-12-27
 */
@RestController
@RequestMapping(value = "/role")
public class RoleController {

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Autowired
    private RoleService roleService;

    @Autowired
    private RoleRelResourceService roleRelResourceService;
    /**
     * 根据条件查询Role，分页查询
     * @param roleVO
     * @return BaseVo
     * @author zhangjie
     * @date 2018-12-27
     */
    @PostMapping("/listPage")
    public BaseVo findByConditionPage(@RequestBody RoleVO roleVO){
        return roleService.findByConditionPage(roleVO);
    }

    /**
     * 查询所有Role
     * @param sorter 排序字符串
     * @return List<Role>
     * @author zhangjie
     * @date 2018-12-27
     */
    @GetMapping("/findAll")
    public BaseVo findAll(@PathVariable("sorter") String sorter){
        BaseVo baseVo = new BaseVo();
        baseVo.setData(roleService.findAll(sorter));
        return baseVo;
    }

    /**
     * 通过主键查询单个Role
     * @param id
     * @return Role
     * @author zhangjie
     * @date 2018-12-27
     */
    @GetMapping("/findById/{id}")
    public BaseVo findById(@PathVariable("id") Long id){
        BaseVo baseVo = new BaseVo();
        baseVo.setData(roleService.findById(id));
        return baseVo;
    }

    /**
     * 保存Role
     * @param role
     * @author zhangjie
     * @date 2018-12-27
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Role role){
        roleService.add(role);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 更新Role
     * @param role
     * @author zhangjie
     * @date 2018-12-27
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody Role role){
        roleService.update(role);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 删除Role
     * @param id
     * @author zhangjie
     * @date 2018-12-27
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable("id") Long id){
        roleService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 校验名字不能重复
     * @param role
     * @author liqi
     * @date 2018-12-27
     */
    @PostMapping("/checkRoleName")
    public BaseVo checkRoleName(@RequestBody Role role){
        return roleService.checkRoleName(role);
    }

    /**
     * 校验是否能删除  角色是否有被用或者使用
     * @param id
     * @return
     */
    @GetMapping("/checkRoleNexus/{id}")
    public BaseVo checkRoleNexus(@PathVariable("id") Long id){
        return roleService.checkRoleNexus(id);
    }

    /**
     * 设置资源权限
     * @param resourcesRoleVo
     * @return
     */
    @PostMapping("/setResources")
    public BaseVo setResources(@RequestBody ResourcesRoleVo resourcesRoleVo){
        return roleRelResourceService.setResources(resourcesRoleVo);
    }

    /**
     * 设置资源权限回显
     * @param id
     * @return
     */
    @GetMapping("/findResourcesByRoleId/{id}")
    public BaseVo findResourcesByRoleId(@PathVariable("id") Long id){
        return roleService.findResourcesByRoleId(id);
    }

    /**
     * 根据应用id 查询角色
     * @param appId
     * @return
     */
    @GetMapping("/findRoleByApp/{appId}")
    public BaseVo findRoleByApp(@PathVariable("appId") Integer appId){
        return roleService.findRoleByApp(appId);
    }

    /**
     * 校验角色编号唯一性
     * @param role
     * @return
     */
    @PostMapping("/checkRoleCodeUnique")
    public BaseVo checkRoleCodeUnique(@RequestBody Role role){
        return roleService.checkRoleCodeUnique(role);
    }
}
