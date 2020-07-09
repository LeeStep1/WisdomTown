package com.bit.officialdoc.service;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.fallback.UserHystrixFallback;
import com.bit.officialdoc.service.impl.OfficialDocServiceImpl;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/22 16:27
 */
@FeignClient(value = "service-sys", fallback = UserHystrixFallback.class)
public interface UserService {

    /**
     * 获取用户信息
     *
     * @param id
     * @return
     */
    @GetMapping("/user/query/{id}")
    BaseVo findById(Long id);

    /**
     * 批量获取用户信息
     *
     * @param params uids  用户ID集合，List<Long> uids
     * @param params appId  appid
     * @return
     */
    @PostMapping("/user/listByAppIdAndIds")
    BaseVo<List<OfficialDocServiceImpl.User>> listByAppIdAndIds(@RequestBody Map<String, Object> params);

    /**
     * 根据部门id集合获取用户列表
     *
     * @param params depIds
     * @return
     */
    @PostMapping("/user/usersByOaDepIds")
    BaseVo usersByDepIds(@RequestBody Map<String, Object> params);

}
