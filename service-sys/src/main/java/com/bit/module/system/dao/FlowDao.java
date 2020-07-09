package com.bit.module.system.dao;

import com.bit.module.system.bean.BusinessRelRole;
import com.bit.module.system.bean.BusinessRole;
import com.bit.module.system.bean.RoleIdentityUser;
import com.bit.module.system.bean.User;
import com.bit.module.system.vo.BusinessRoleVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FlowDao {
    /**
     * 根据工作流id 批量查询记录
     */
    List<BusinessRole> batchSelectByFlowId(Integer flowId);

    /**
     * 根据流id查询角色id和角色名称
     * @param flowId
     * @return
     */
    List<BusinessRelRole> queryRoleNamesByFlowId(Integer flowId);

    /**
     * 根据流id删除记录
     * @param flowId
     */
    void batchDeleteByFlowId(Integer flowId);

    /**
     * 根据流id查询记录
     * @param flowId
     * @return
     */
    List<BusinessRole> queryByFlowId(Integer flowId);

    /**
     * 批量添加记录
     * @param businessRoleList
     */
    void batchAdd(@Param(value = "businessRoleList") List<BusinessRole> businessRoleList);

    /**
     * 分页查询记录
     * @param businessRoleVO
     * @return
     */
    List<BusinessRole> listPage(BusinessRoleVO businessRoleVO);

    /**
     * 根据角色查询用户
     * @param roleIdentityUser
     * @return
     */
    List<User> queryUserByRole(RoleIdentityUser roleIdentityUser);

    /**
     * 根据身份查询用户
     * @param roleIdentityUser
     * @return
     */
    List<User> queryUserByIdentity(RoleIdentityUser roleIdentityUser);

    /**
     * 根据角色id的列表批量查询用户
     * @param roleIds
     * @return
     */
    List<User> queryUserByRoleBatch(@Param(value = "roleIds") List<Long> roleIds);
}
