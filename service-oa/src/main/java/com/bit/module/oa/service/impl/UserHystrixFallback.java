package com.bit.module.oa.service.impl;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.feign.UserServiceFeign;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @autor xiaoyu.fang
 * @date 2019/2/14 9:51
 */
@Component
public class UserHystrixFallback implements UserServiceFeign {

    @Override
    public BaseVo<List<TaskServiceImpl.User>> listByAppIdAndIds(Map<String, Object> params) {
        return new BaseVo<>();
    }

}
