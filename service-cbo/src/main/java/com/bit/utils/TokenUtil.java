package com.bit.utils;

import com.bit.core.utils.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: token 相关处理工具类
 * @author: liyang
 * @date: 2019-07-22
 **/
@Component
public class TokenUtil {

    /**
     * 内存处理相关工具
     */
    @Autowired
    private CacheUtil cacheUtil;

    /**
     * 删除登录token
     * @author liyang
     * @date 2019-07-22
     * @param token : 用户token
    */
    public void delToken(String token){

        cacheUtil.del(token);

    }

}
