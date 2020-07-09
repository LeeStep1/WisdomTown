package com.bit.job.handler;

import com.bit.job.feign.VolunteerFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description: 每日更新志愿者和活动和站点信息
 * @author: chenduo
 * @create: 2019-04-03 13:24
 */
@Component
@JobHandler(value = "volunteerCampaignStationJobHandler")
public class VolunteerCampaignStationJobHandler extends IJobHandler {

    @Autowired
    private VolunteerFeign volunteerFeign;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("每日更新志愿者和活动和站点信息");
        volunteerFeign.dailyVolJob();

        return ReturnT.SUCCESS;
    }
}


