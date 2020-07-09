package com.bit.module.website.feign;

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
@FeignClient(value = "service-file")
public interface FileFeign {

    @RequestMapping(value ="/findById/{fileId}",method = RequestMethod.GET)
    public BaseVo findById(@PathVariable("fileId") Long fileId);
}
