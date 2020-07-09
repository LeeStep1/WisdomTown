package com.bit.module.system.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


/**
 * @author chenduo
 * @create 2019-01-21 16:01
 */
@FeignClient(value = "service-file")
public interface FileServiceFeign {




    @RequestMapping(value = "/findById/{fileId}",method = RequestMethod.GET)
    BaseVo findById(@PathVariable("fileId") Long fileId);

}
