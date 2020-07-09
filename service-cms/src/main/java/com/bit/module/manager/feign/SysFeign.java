package com.bit.module.manager.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @description: sys服务feign
 * @author: liyang
 * @date: 2019-05-15
 **/
@FeignClient(value = "service-sys")
public interface SysFeign {

    @RequestMapping(value = "dict/findByModule/{module}",method = RequestMethod.GET)
    BaseVo findByModule(@PathVariable(value = "module") String module);
}
