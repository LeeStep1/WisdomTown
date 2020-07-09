package com.bit.module.oa.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.service.impl.TaskServiceImpl;
import com.bit.module.oa.service.impl.UserHystrixFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @autor xiaoyu.fang
 * @date 2019/2/14 9:49
 */
@FeignClient(value = "service-sys", fallback = UserHystrixFallback.class)
public interface UserServiceFeign {

    /**
     * 批量获取用户信息
     *
     * @param params uids  用户ID集合，List<Long> uids
     * @param params appId  appid
     * @return
     */
    @PostMapping("/user/listByAppIdAndIds")
    BaseVo<List<TaskServiceImpl.User>> listByAppIdAndIds(@RequestBody Map<String, Object> params);

}
