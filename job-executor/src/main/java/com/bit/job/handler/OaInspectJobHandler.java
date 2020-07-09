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
@JobHandler(value = "oaInspectJobHandler")
public class OaInspectJobHandler extends IJobHandler {

    @Autowired
    private OaFeign oaFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String INSPECT_END_ZSET = "z_inspect_end";

    @Override
    public ReturnT<String> execute(String str) throws Exception {
        Instant queryTime = new Date().toInstant();
        long now = queryTime.getEpochSecond();
        XxlJobLogger.log("扫描redis巡检时间:{}", queryTime);
        Long endInspectSize = redisTemplate.opsForZSet().size(INSPECT_END_ZSET);
        XxlJobLogger.log("结束巡检数量:{}", endInspectSize);
        if (endInspectSize != null && endInspectSize > 0) {
            endInspect(now);
        }
        return ReturnT.SUCCESS;
    }

    private void endInspect(long now) {
        ZSetOperations zSet = redisTemplate.opsForZSet();
        // 每5秒执行一次
        Set<RedisDelay> inspects = zSet.rangeByScore(INSPECT_END_ZSET, 0, now);
        if (CollectionUtils.isEmpty(inspects)) {
            return;
        }
        XxlJobLogger.log("结束巡检列表 : {}", inspects);
        // 考虑到实际应用场景，5秒内同时修改多个巡检的概率较低，故直接for循环调用
        inspects.forEach(i -> oaFeign.endInspect(i.getId()));
        zSet.removeRangeByScore(INSPECT_END_ZSET, 0, now);
    }
}
