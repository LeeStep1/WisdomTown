package com.bit.module.system.service;


import com.bit.module.system.bean.User;
import com.bit.module.system.vo.UserVO;

import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-16 13:33
 */
public interface UserRelPbOrgService {
    /**
     *
     * 功能描述: 根据党组织id查询所有用户id
     * @author: chenduo
     * @description:
     * @param:
     * @return:
     * @date: 2019-04-16 13:34
     */
    List<Long> queryAllUserByPbOrgId(String pbOrgId);
    /**
     * 批量查询党建管理员的身份证
     * @param orgIds
     * @return
     */
    List<String> findUserIdCardByOrgIds(List<Long> orgIds);
    /**
     * 根据党组织id查询用户身份证
     * @param pbOrgId
     * @return
     */
    List<String> queryUserIdCardByOrgId(Long pbOrgId);

    /**
     * 批量查询党建管理员的信息
     * @param orgIds
     * @return
     */
    List<UserVO> findUserByOrgIds(List<Long> orgIds);

    /**
     * 根据组织IDS获取用户ID
     * @param orgIds
     * @return
     */
    List<Long> queryUserIdByOrgids(List<String> orgIds);

}
