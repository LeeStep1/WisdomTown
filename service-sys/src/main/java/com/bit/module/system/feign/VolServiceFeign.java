package com.bit.module.system.feign;


import com.bit.base.dto.VolunteerInfo;
import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author chenduo
 * @create 2019-03-12 14:38
 */
@FeignClient(value = "service-vol")
public interface VolServiceFeign {

    @RequestMapping(value = "/voInfo/getInfo",method = RequestMethod.POST)
    VolunteerInfo getInfo(@RequestBody String cardId);

    @RequestMapping(value = "/level/reflect/{id}",method = RequestMethod.GET)
    BaseVo volLevelReflect(@PathVariable("id") Long id);

    @RequestMapping(value = "/partner/reflect/{id}",method = RequestMethod.GET)
    BaseVo volPartnerOrgReflect(@PathVariable("id") Long id);


}
