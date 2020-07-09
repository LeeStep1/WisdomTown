package com.bit.module.system.feign;



import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-12 14:38
 */
@FeignClient(value = "service-vol")
public interface StationServiceFeign {

    @RequestMapping(value = "/station/reflect/{id}",method = RequestMethod.GET)
    BaseVo reflectById(@PathVariable(value = "id") Long id);

    @RequestMapping(value = "/station/findAllStation",method = RequestMethod.POST)
    BaseVo findAllStation();

    @RequestMapping(value = "/station/batchSelectByStationIds",method = RequestMethod.POST)
    BaseVo batchSelectByStationIds(@RequestBody List<Long> stationIds);
}
