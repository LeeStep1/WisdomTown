package com.bit.job.handler;

import com.bit.job.feign.VolunteerFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 每日更新志愿者等级和发送推送
 */
@Component
@JobHandler(value = "volunteerLevelHandler")
public class VolunteerLevelHandler extends IJobHandler {
    @Autowired
    private VolunteerFeign volunteerFeign;


    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("每日更新志愿者等级和发送推送");
        volunteerFeign.dailyUpdateVolunteerLevel();

        return ReturnT.SUCCESS;
    }
}
