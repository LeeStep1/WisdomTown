package com.bit.module.system.service;

import com.bit.module.system.bean.User;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-14 16:57
 */
public interface OrgUserService {

    List<User> getPbUser(Long orgId);

    List<User> getOaUser(Long orgId);

    List<User> getPbUserByName(List<String> orgIds,String name);

    List<User> getOaUserByName(List<Long> orgIds,String name);

    List<User> getVolUser(Long orgId);
}

