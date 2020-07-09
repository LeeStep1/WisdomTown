package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Role;
import com.bit.module.system.bean.RoleRelResource;
import com.bit.module.system.vo.RoleVO;

import java.util.List;

public interface RoleService {

    /**
     * 根据条件查询Role
     * @param roleVO
     * @return
     */
    BaseVo findByConditionPage(RoleVO roleVO);

    /**
     * 查询所有Role
     * @param sorter 排序字符串
     * @return
     */
    List<Role> findAll(String sorter);

    /**
     * 通过主键查询单个Role
     * @param id
     * @return
     */
    Role findById(Long id);

    /**
     * 保存Role
     * @param role
     */
    void add(Role role);

    /**
     * 更新Role
     * @param role
     */
    void update(Role role);

    /**
     * 删除Role
     * @param id
     */
    void delete(Long id);

    /**
     * 校验角色名称
     * @param role
     */
    BaseVo checkRoleName(Role role);

    /**
     * 校验是否能删除  角色是否有被用或者使用
     * @param id
     */
    BaseVo checkRoleNexus(Long id);

    /**
     * 设置资源权限回显
     * @param id
     * @return
     */
    BaseVo findResourcesByRoleId(Long id);

    /**
     * 根据应用id 查询角色
     * @param appId
     * @return
     */
    BaseVo findRoleByApp(Integer appId);

    /**
     * 校验角色编号唯一性
     * @param role
     * @return
     */
    BaseVo checkRoleCodeUnique(Role role);
}
