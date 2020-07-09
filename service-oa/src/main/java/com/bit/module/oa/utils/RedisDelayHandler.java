package com.bit.module.oa.utils;

import com.bit.module.oa.vo.redis.RedisDelay;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/4/15 14:35
 */
@Component
@Slf4j
public class RedisDelayHandler {

    @Autowired
    private RedisTemplate redisTemplate;

    public void setDelayZSet(String redisKey, Long id, Date triggerAt) {
        RedisDelay delay = new RedisDelay();
        delay.setId(id);
        delay.setTriggerAt(triggerAt);
        doRedisDelay(redisKey, delay);
    }

    /**
     * 发送redis队列
     * @param delay
     */
    private void doRedisDelay(String redisKey, RedisDelay delay) {
        log.info("设置redis延时会议消息 : {}", delay);
        // 使用zset作为队列, 使用以秒为单位的时间戳(毫秒的长度超过了zset的score长度, 会出现精度丢失的问题)
        redisTemplate.opsForZSet().add(redisKey, delay, delay.getTriggerAt().toInstant().getEpochSecond());
    }
}
