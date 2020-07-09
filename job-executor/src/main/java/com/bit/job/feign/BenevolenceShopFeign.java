package com.bit.job.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author liuyancheng
 * @create 2019-04-04 8:52
 */
@FeignClient(value = "service-vol")
public interface BenevolenceShopFeign {
    /**
     * 定时查询商品统计的积分和总兑换的数量
     */
    @GetMapping(value = "/productExchangeAudit/timingIntegral")
    void timingIntegral();
}
