package com.bit.module.syslog.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * vol微服务调用
 * @author liyang
 * @date 2019-04-09
*/
@FeignClient(value = "service-vol")
public interface VolServiceFeign {

    /**
     * 获取所有志愿者ID
     * @author liyang
     * @date 2019-04-09
     */
    @RequestMapping(value = "/user/getAllVolUserIds", method = RequestMethod.GET)
    List<Long> getAllVolUserIds();

    @RequestMapping(value = "/campaign/queryEnrollId/{campaignId}", method = RequestMethod.GET)
    List<Long> queryEnrollId(@PathVariable(value = "campaignId") Long campaignId);

    @RequestMapping(value = "/campaign/checkCampaignStatusById/{id}", method = RequestMethod.GET)
    Boolean checkCampaignStatusById(@PathVariable(value = "id") Long id);

    @RequestMapping(value = "/station/findTopStation", method = RequestMethod.POST)
    Long findTopStation();
}
