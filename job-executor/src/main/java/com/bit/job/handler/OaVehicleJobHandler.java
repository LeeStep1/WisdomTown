package com.bit.job.handler;

import com.bit.job.feign.OaFeign;
import com.bit.module.oa.vo.redis.RedisDelay;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Component
@JobHandler(value = "oaVehicleJobHandler")
public class OaVehicleJobHandler extends IJobHandler {

    @Autowired
    private OaFeign oaFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    public static final String VEHICLE_REJECT_ZSET = "z_vehicle_reject";

    @Override
    public ReturnT<String> execute(String str) throws Exception {
        Instant queryTime = new Date().toInstant();
        long now = queryTime.getEpochSecond();
        XxlJobLogger.log("扫描redis车辆申请时间:{}", queryTime);
        Long invalidVehicleApplicationSize = redisTemplate.opsForZSet().size(VEHICLE_REJECT_ZSET);
        XxlJobLogger.log("结束车辆申请数量:{}", invalidVehicleApplicationSize);
        if (invalidVehicleApplicationSize != null && invalidVehicleApplicationSize > 0) {
            invalid(now);
        }
        return ReturnT.SUCCESS;
    }

    private void invalid(long now) {
        ZSetOperations zSet = redisTemplate.opsForZSet();
        // 每5秒执行一次
        Set<RedisDelay> vehicleApplications = zSet.rangeByScore(VEHICLE_REJECT_ZSET, 0, now);
        if (CollectionUtils.isEmpty(vehicleApplications)) {
            return;
        }
        XxlJobLogger.log("失效车辆申请列表 : {}", vehicleApplications);
        // 考虑到实际应用场景，5秒内同时修改多个巡检的概率较低，故直接for循环调用
        vehicleApplications.forEach(i -> oaFeign.invalidVehicleApplication(i.getId()));
        zSet.removeRangeByScore(VEHICLE_REJECT_ZSET, 0, now);
    }
}
