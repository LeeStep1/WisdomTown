package com.bit.job.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author decai.liu
 * @create 2019-08-09
 */
@FeignClient(value = "service-sv")
public interface SvFeign {

    /**
     * 每日定时检查3天后过期的巡检任务并发送消息推送
     * @return
     */
    @GetMapping("/tasks/daily-push-expire-task")
    BaseVo dailyPushExpireTask();

    /**
     * 每日定时检查3天后过期的复查任务并发送消息推送
     * @return
     */
    @GetMapping("/tasks/daily-push-expire-review-task")
    BaseVo dailyPushExpireReviewTask();
}
