package com.bit.file.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chenduo
 * @create 2019-03-01 15:59
 */
@FeignClient(value = "service-cms")
public interface CmsFeign {
    @RequestMapping(value = "/meeting/pbDelFile/{fileId}",method = RequestMethod.GET)
    void pbDelFile(@PathVariable("fileId") Long fileId);
}
