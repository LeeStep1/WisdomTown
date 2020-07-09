package com.bit.ms.feign;

import com.bit.base.vo.BaseVo;
import com.bit.ms.bean.Message;
import com.bit.ms.bean.OrgAndName;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author chenduo
 * @create 2019-02-18 15:11
 */
@FeignClient(value = "service-sys", fallback=SysServiceHystrix.class)
public interface SysServiceFeign {

   /* @RequestMapping(value = "/message/add",method = RequestMethod.POST)
    BaseVo add(@RequestBody Message message);*/

    @RequestMapping(value = "/notice/queryUserByAppIdOrgIdsName",method = RequestMethod.POST)
    BaseVo queryUserByAppIdOrgIdsName(@RequestBody OrgAndName orgAndName);

    @RequestMapping(value = "/user/verifyToken",method = RequestMethod.POST)
    Boolean verifyToken(@RequestParam("token") String token,@RequestParam("tid") Integer tid);
}
