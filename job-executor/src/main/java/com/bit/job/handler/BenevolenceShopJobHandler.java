package com.bit.job.handler;

import com.bit.job.feign.BenevolenceShopFeign;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author liuyancheng
 * @create 2019-04-04 8:56
 */
@Component
@JobHandler(value = "benevolenceShopJobHandler")
public class BenevolenceShopJobHandler extends IJobHandler {

    @Autowired
    private BenevolenceShopFeign benevolenceShopFeign;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        XxlJobLogger.log("每日更新商品统计的积分和总兑换的数量");
        benevolenceShopFeign.timingIntegral();
        return ReturnT.SUCCESS;
    }
}
