package com.bit.job.handler;


import com.bit.job.feign.SvFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 定时任务 检查3天后到期的巡检任务、复查任务，给执勤人员发送到期提醒
 * @author decai.liu
 * @create 2019-08-09
 */
@Component
@JobHandler(value = "taskExpirePushJobHandler")
public class TaskExpirePushJobHandler extends IJobHandler {

    @Autowired
    private SvFeign svFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("每日更新检查3天后到期的巡检任务及复查任务并发送消息推送...");
        svFeign.dailyPushExpireTask();
        svFeign.dailyPushExpireReviewTask();
        return ReturnT.SUCCESS;
    }
}
