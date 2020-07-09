package com.bit.job.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Description :
 * @Date ： 2019/3/21 18:32
 */
@FeignClient(value = "service-oa")
public interface OaFeign {
    @PostMapping(value = "/meeting/start")
    BaseVo startMeeting(@RequestBody Long id);

    @PostMapping(value = "/meeting/end")
    BaseVo endMeeting(@RequestBody Long id);

    @PostMapping(value = "/inspect/end")
    BaseVo endInspect(@RequestBody Long id);

    @PostMapping(value = "/meeting/invalid")
    BaseVo invalidMeeting(@RequestBody Long id);

    @PostMapping(value = "/vehicleApplication/handle/invalid")
    BaseVo invalidVehicleApplication(@RequestBody Long id);
}
