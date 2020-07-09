package com.bit.job.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author chenduo
 * @create 2019-01-28 15:10
 */
@FeignClient(value = "service-vol")
public interface VolunteerFeign {
    /**
     * 更新志愿者 活动 站点 数据
     */
    @PostMapping(value = "/job/dailyVolJob")
    void dailyVolJob();

    /**
     * 更新志愿者等级
     */
    @PostMapping(value = "/job/dailyUpdateVolunteerLevel")
    void dailyUpdateVolunteerLevel();
}
