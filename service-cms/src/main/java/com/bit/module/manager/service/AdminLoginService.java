package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.AdminLogin;
import com.bit.module.manager.bean.PortalUser;
import com.bit.module.manager.vo.PortalUserVo;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 15:44
 */
public interface AdminLoginService {
    /**
     * admin登陆
     * @param adminLogin
     * @return
     */
    BaseVo adminLogin(AdminLogin adminLogin);

    /**
     * admin登出
     * @return
     */
    BaseVo adminLogout();

    /**
     * mongo分页测试
     * @return
     */
   // BaseVo mongotest();

    /**
     * 用户新增
     * @return
     */
    BaseVo add(PortalUser user);

    /**
     * 用户列表展示
     * @return
     */
    BaseVo findAll(PortalUserVo portalUserVo);

    /**
     * 修改用户信息
     * @return
     */
    BaseVo update(PortalUser portalUser);

    /**
     * 删除用户
     * @return
     */
    BaseVo delete(Long id);
}
