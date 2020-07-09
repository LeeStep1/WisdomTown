package com.bit.job.handler;

import com.bit.job.feign.PartyDueFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @Description :
 * @Date ： 2019/1/1 16:09
 */
@Component
@JobHandler(value = "monthlyPartyDueJobHandler")
public class MonthlyPartyDueJobHandler extends IJobHandler {
    @Autowired
    private PartyDueFeign partyDueFeign;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("生成单位党费档案");
        LocalDate now = LocalDate.now();
        int year = StringUtils.isBlank(s) ? now.getYear() : Integer.parseInt(s.split(":")[0]);
        int month = StringUtils.isBlank(s) ? now.getMonthValue() : Integer.parseInt(s.split(":")[1]);
        partyDueFeign.generateMonthlyPartyDueForSubordinates(year, month);
        return ReturnT.SUCCESS;
    }
}
