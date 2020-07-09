package com.bit.job.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author chenduo
 * @create 2019-01-28 15:10
 */
@FeignClient(value = "service-pb")
public interface PbDueFeign {

    /**
     * 每日定时更新会议状态
     * @return
     */
    @PostMapping("/meeting/dailyUpdateConferenceStatus")
    BaseVo dailyUpdateConferenceStatus();

    /**
     * 每日定时更新会议签到率
     * @return
     */
    @PostMapping("/meeting/dailyUpdateConferenceSignRate")
    BaseVo dailyUpdateConferenceSignRate();

    /**
     * 每日定时更新会议上传率
     * @return
     */
    @PostMapping("/meeting/dailyUpdateConferenceUploadRate")
    BaseVo dailyUpdateConferenceUploadRate();
}
