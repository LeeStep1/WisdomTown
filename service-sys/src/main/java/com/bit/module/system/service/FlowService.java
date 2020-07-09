package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.BusinessRole;
import com.bit.module.system.bean.RoleIdentityUser;
import com.bit.module.system.vo.BusinessRoleVO;

import java.util.List;

public interface FlowService {
    /**
     * 新增记录
     * @param businessRole
     * @return
     */
    BaseVo add(BusinessRole businessRole);

    /**
     * 更新记录
     * @param businessRole
     * @return
     */
    BaseVo update(BusinessRole businessRole);

    /**
     * 返显记录
     * @param flowId
     * @return
     */
    BaseVo reflectByFlowId(Integer flowId);

    /**
     * 分页查询
     * @param businessRoleVO
     * @return
     */
    BaseVo listPage(BusinessRoleVO businessRoleVO);

    /**
     * 根据角色id的列表批量查询用户
     * @param roleIds
     * @return
     */
    BaseVo queryUserByRoleBatch(List<Long> roleIds);
}
