package com.bit.officialdoc.fallback;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.service.UserService;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/22 16:45
 */
@Component
public class UserHystrixFallback implements UserService {
    @Override
    public BaseVo findById(Long id) {
        return new BaseVo();
    }

    @Override
    public BaseVo listByAppIdAndIds(Map<String, Object> params) {
        return new BaseVo();
    }

    @Override
    public BaseVo usersByDepIds(Map<String, Object> params) {
        return new BaseVo();
    }
}
