package com.bit.soft.configserver.dao;

import com.bit.soft.configserver.bean.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserDao {

    /**
     * 校验登录用户
     * @param user
     * @return
     */
    Integer checkUserSql(@Param("user") User user);

    /**
     * 插入新用户
     * @param user
     */
    void insertUserSql(@Param("user") User user);
}
