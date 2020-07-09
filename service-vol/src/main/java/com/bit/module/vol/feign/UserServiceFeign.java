package com.bit.module.vol.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.AuthorUser;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * 用户微服务
 * @author Liy
 * @create 2019-03-08 16:01
 */
@FeignClient(value = "service-sys")
public interface UserServiceFeign {

    /**
     * 用户授权
     * @param authorUser
     * @return
     */
    @PostMapping("/user/userAuthorization")
    BaseVo userAuthorization(@RequestBody AuthorUser authorUser);

    /**
     * 根据APPID查询身份
     * @param appId
     * @return
     */
    @PostMapping("/identity/defaultIdentityByAppId/{appId}")
    BaseVo defaultIdentityByAppId(@PathVariable(value = "appId") Long appId);


}
