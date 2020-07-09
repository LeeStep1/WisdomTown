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
@JobHandler(value = "oaMeetingJobHandler")
public class OaMeetingJobHandler extends IJobHandler {

    @Autowired
    private OaFeign oaFeign;

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String MEETING_START_ZSET = "z_meeting_start";

    private static final String MEETING_END_ZSET = "z_meeting_end";

    public static final String MEETING_REJECT_ZSET = "z_meeting_reject";

    @Override
    public ReturnT<String> execute(String str) throws Exception {

        Instant queryTime = new Date().toInstant();
        long now = queryTime.getEpochSecond();
        XxlJobLogger.log("扫描redis会议记录时间 : {}", queryTime);

        Long startMeetingSize = redisTemplate.opsForZSet().size(MEETING_START_ZSET);
        XxlJobLogger.log("待开始会议数量:{}", startMeetingSize);
        if (startMeetingSize != null && startMeetingSize > 0) {
            startMeeting(now);
        }

        Long endMeetingSize = redisTemplate.opsForZSet().size(MEETING_END_ZSET);
        XxlJobLogger.log("待结束会议数量:{}", endMeetingSize);
        if (endMeetingSize != null && endMeetingSize > 0) {
            endMeeting(now);
        }

        Long rejectMeetingSize = redisTemplate.opsForZSet().size(MEETING_REJECT_ZSET);
        XxlJobLogger.log("待审核会议数量:{}", rejectMeetingSize);
        if (rejectMeetingSize != null && rejectMeetingSize > 0) {
            invalidMeeting(now);
        }
        return ReturnT.SUCCESS;
    }

    private void invalidMeeting(long now) {
        ZSetOperations zSet = redisTemplate.opsForZSet();
        // 每5秒执行一次
        Set<RedisDelay> meetings = findMeetingByRedisZSetAndTime(now, zSet, MEETING_REJECT_ZSET);
        if (CollectionUtils.isEmpty(meetings)) {
            return;
        }

        XxlJobLogger.log("会议失效 : {}", meetings);
        // 考虑到实际应用场景，5秒内同时修改多个会议的概率较低，故直接for循环调用
        meetings.forEach(meeting -> oaFeign.invalidMeeting(meeting.getId()));
        zSet.removeRangeByScore(MEETING_REJECT_ZSET, 0, now);
    }

    private void endMeeting(long now) {
        ZSetOperations zSet = redisTemplate.opsForZSet();
        // 每5秒执行一次
        Set<RedisDelay> meetings = findMeetingByRedisZSetAndTime(now, zSet, MEETING_END_ZSET);
        if (CollectionUtils.isEmpty(meetings)) {
            return;
        }

        XxlJobLogger.log("结束会议列表 : {}", meetings);
        // 考虑到实际应用场景，5秒内同时修改多个会议的概率较低，故直接for循环调用
        meetings.forEach(meeting -> oaFeign.endMeeting(meeting.getId()));
        zSet.removeRangeByScore(MEETING_END_ZSET, 0, now);
    }

    private void startMeeting(long now) {
        ZSetOperations zSet = redisTemplate.opsForZSet();
        // 每5秒执行一次
        Set<RedisDelay> meetings = findMeetingByRedisZSetAndTime(now, zSet, MEETING_START_ZSET);
        if (CollectionUtils.isEmpty(meetings)) {
            return;
        }

        XxlJobLogger.log("开始会议列表 : {}", meetings);
        // 考虑到实际应用场景，5秒内同时修改多个会议的概率较低，故直接for循环调用
        meetings.forEach(meeting -> oaFeign.startMeeting(meeting.getId()));
        zSet.removeRangeByScore(MEETING_START_ZSET, 0, now);
    }

    private Set findMeetingByRedisZSetAndTime(long now, ZSetOperations zSet, String meetingStartZset) {
        return zSet.rangeByScore(meetingStartZset, 0, now);
    }
}
