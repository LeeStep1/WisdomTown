package com.bit.job.handler;


import com.bit.job.feign.PbDueFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 定时任务更新会议状态 和 签到率 和 上传率
 * @author chenduo
 * @create 2019-01-28 15:07
 */
@Component
@JobHandler(value = "dailyCalculateJobHandler")
public class DailyCalculateJobHandler extends IJobHandler {
    @Autowired
    private PbDueFeign pbDueFeign;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("每日更新记录状态和签到率");
//        pbDueFeign.calculate();
        pbDueFeign.dailyUpdateConferenceStatus();
        pbDueFeign.dailyUpdateConferenceSignRate();
        pbDueFeign.dailyUpdateConferenceUploadRate();
        return ReturnT.SUCCESS;
    }
}
