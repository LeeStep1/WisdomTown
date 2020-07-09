package com.bit.job.handler;

import com.bit.job.feign.PartyDueFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@JobHandler(value = "partyDueJobHandler")
public class PartyDueJobHandler extends IJobHandler {

    @Autowired
    private PartyDueFeign partyDueFeign;

    @Override
    public ReturnT<String> execute(String str) throws Exception {
        XxlJobLogger.log("生成党员党费档案");
        partyDueFeign.generatePartyDueMonthly();
        return ReturnT.SUCCESS;
    }
}
