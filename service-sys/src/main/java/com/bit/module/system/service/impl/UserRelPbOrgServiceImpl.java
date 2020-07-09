package com.bit.module.system.service.impl;

import com.bit.module.system.bean.User;
import com.bit.module.system.bean.UserRelPbOrg;
import com.bit.module.system.dao.UserRelPbOrgDao;
import com.bit.module.system.service.UserRelPbOrgService;
import com.bit.module.system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-16 13:35
 */
@Service("userRelPbOrgService")
public class UserRelPbOrgServiceImpl implements UserRelPbOrgService {

    @Autowired
    private UserRelPbOrgDao userRelPbOrgDao;
    /**
     *
     * 功能描述: 根据党组织id查询所有用户id
     * @author: chenduo
     * @description:
     * @param:
     * @return:
     * @date: 2019-04-16 13:34
     */
    @Override
    public List<Long> queryAllUserByPbOrgId(String pbOrgId) {
        UserRelPbOrg userRelPbOrg = new UserRelPbOrg();
        userRelPbOrg.setPborgId(Long.valueOf(pbOrgId));
        List<UserRelPbOrg> allByParam = userRelPbOrgDao.findAllByParam(userRelPbOrg);
        List<Long> longlist = new ArrayList<>();
        for (UserRelPbOrg relPbOrg : allByParam) {
            longlist.add(relPbOrg.getUserId());
        }


        return longlist;
    }
    /**
     * 批量查询党建管理员的身份证
     * @param orgIds
     * @return
     */
    @Override
    public List<String> findUserIdCardByOrgIds(List<Long> orgIds) {
        return userRelPbOrgDao.findUserIdCardByOrgIds(orgIds);
    }
    /**
     * 根据党组织id查询用户身份证
     * @param pbOrgId
     * @return
     */
    @Override
    public List<String> queryUserIdCardByOrgId(Long pbOrgId) {
        return userRelPbOrgDao.queryUserIdCardByOrgId(pbOrgId);
    }
    /**
     * 批量查询党建管理员的信息
     * @param orgIds
     * @return
     */
    @Override
    public List<UserVO> findUserByOrgIds(List<Long> orgIds) {
        return userRelPbOrgDao.findUserByOrgIds(orgIds);
    }

    @Override
    public List<Long> queryUserIdByOrgids(List<String> orgIds) {
        return userRelPbOrgDao.queryUserIdByOrgids(orgIds);
    }
}
