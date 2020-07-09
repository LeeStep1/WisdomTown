package com.bit.module.system.service.impl;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.*;
import com.bit.module.system.dao.UserDao;
import com.bit.module.system.dao.UserRelOaDepDao;
import com.bit.module.system.dao.UserRelPbOrgDao;
import com.bit.module.system.dao.UserRelVolStationDao;
import com.bit.module.system.service.OrgUserService;
import com.bit.module.system.vo.UserRelOaDepVO;
import com.bit.module.system.vo.UserRelPbOrgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-14 16:59
 */
@Service("orgUserService")
public class OrgUserServiceImpl implements OrgUserService {
    @Autowired
    private UserRelPbOrgDao userRelPbOrgDao;
    @Autowired
    private UserRelOaDepDao userRelOaDepDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserRelVolStationDao userRelVolStationDao;

    /**
     * 根据组织id查询党建用户
     * @param orgId
     * @return
     */
    @Override
    public List<User> getPbUser(Long orgId) {
        UserRelPbOrg userRelPbOrg = new UserRelPbOrg();
        userRelPbOrg.setPborgId(orgId);
        List<UserRelPbOrg> byConditionPage = userRelPbOrgDao.findAllByParam(userRelPbOrg);
        List<Long> userIds = new ArrayList<>();
        for (UserRelPbOrg us : byConditionPage) {
            userIds.add(us.getUserId());
        }
        List<User> userList = new ArrayList<>();
        if (userIds.size()>0){
            userList = userDao.batchSelect(userIds);
            return userList;
        }


        return userList;
    }

    /**
     * 根据组织id查询政务用户
     * @param orgId
     * @return
     */
    @Override
    public List<User> getOaUser(Long orgId) {
        UserRelOaDep userRelOaDep = new UserRelOaDep();
        userRelOaDep.setDepId(orgId.intValue());
        List<UserRelOaDep> byConditionPage = userRelOaDepDao.findAllByParam(userRelOaDep);
        List<Long> userIds = new ArrayList<>();
        for (UserRelOaDep us : byConditionPage) {
            userIds.add(us.getUserId());
        }
        List<User> userList = new ArrayList<>();
        if (userIds.size()>0){
            userList = userDao.batchSelect(userIds);
            return userList;
        }
        return userList;
    }


    /**
     * 根据组织id 姓名 查询党建用户
     * @param orgIds,name
     * @return
     */
    @Override
    public List<User> getPbUserByName(List<String> orgIds, String name) {
        OrgAndName orgAndName = new OrgAndName();
        orgAndName.setPbOrgIds(orgIds);
        orgAndName.setName(name);
        List<User> userList = userRelPbOrgDao.getPbUserByName(orgAndName);
        return userList;
    }
    /**
     * 根据组织id  姓名 查询政务用户
     * @param orgIds,name
     * @return
     */
    @Override
    public List<User> getOaUserByName(List<Long> orgIds, String name) {
        OrgAndName orgAndName = new OrgAndName();
        orgAndName.setOrgIds(orgIds);
        orgAndName.setName(name);
        List<User> userList = userRelOaDepDao.getOaUserByName(orgAndName);
        return userList;
    }
    /**
     * 根据组织id  姓名 查询志愿者用户
     * @param orgId
     * @return
     */
    @Override
    public List<User> getVolUser(Long orgId) {
        UserRelVolStation userRelVolStation = new UserRelVolStation();
        userRelVolStation.setStationId(orgId);
        List<UserRelVolStation> all = userRelVolStationDao.findAllByParam(userRelVolStation);

        List<Long> userIds = new ArrayList<>();
        for (UserRelVolStation us : all) {
            userIds.add(us.getUserId());
        }
        List<User> userList = new ArrayList<>();
        if (userIds.size()>0){
            userList = userDao.batchSelect(userIds);
            return userList;
        }
        return userList;
    }
}
