package com.bit.soft.configserver.service;

import com.bit.base.vo.BaseVo;
import com.bit.soft.configserver.bean.User;
import org.springframework.stereotype.Repository;

/**
 * 登录service
 */
@Repository
public interface CheckLoginService {

    /**
     * 登录用户校验
     * @param user
     * @return BaseVo
     */
    BaseVo checkLogin(User user);

    /**
     * 增加用户
     * @return BaseVo
     */
    BaseVo insertUser();
}
