package com.bit.module.cbo.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.FileInfoVO;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

    @RequestMapping(value = "/findByIds",method = RequestMethod.POST)
    BaseVo findByIds(@RequestBody FileInfoVO fileInfoVO);

}
