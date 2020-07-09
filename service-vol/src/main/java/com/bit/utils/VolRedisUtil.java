package com.bit.utils;

import com.bit.base.exception.BusinessException;
import com.bit.core.utils.CacheUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 志愿者模块的redis 操作工具类
 * @author mifei
 * @create 2019-03-14 15:51
 **/
@Component
public class VolRedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private CacheUtil cachecUtil;

    /**
     * @param key1
     * @param size
     * @param value
     * @return 2已重复 1超出容量 0成功
     */
    private  String checkSetSize(String key1, int size, String value) {
        size = size-1;//初始set时已经放入一个了,所以这里要减1
        String argv1 = size + "";
        String argv2 = value;
        //定义key
        List<String> keyList = new ArrayList();
        keyList.add(key1);
        String result = cachecUtil.executeLuaForVol(key1, argv1, argv2);
        return result;
    }

    /**
     *
     * @param key1 redis的key
     * @param size 活动人数
     * @param value 志愿者id
     * @return
     */
    public  String checkCampaignVols(String key1, int size, String value) {
        String result = checkSetSize(key1, size, value);
        if ("2".equals(result)){
            throw new BusinessException("请务重复报名");
        }else if ("1".equals(result)){
            throw new BusinessException("报名人数已满");
        }else if ("3".equals(result)){
            throw new BusinessException("活动不存在");
        }
        return result;
    }

    /**
     * 新建活动并设
     * @param key1
     * @param time
     * @return
     */
    public  String createCampaign(String key1,long time) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key1,-1);//新建一条,其实是多余的
        Set<Object> resultSet = redisTemplate.opsForSet().members(key1);
        redisTemplate.expire(key1, time, TimeUnit.SECONDS);
        return null;
    }
}
