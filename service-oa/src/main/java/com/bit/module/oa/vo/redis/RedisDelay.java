package com.bit.module.oa.vo.redis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/4/15 14:30
 */
@Data
public class RedisDelay implements Serializable {
    private Long id;
    private Date triggerAt;
}
