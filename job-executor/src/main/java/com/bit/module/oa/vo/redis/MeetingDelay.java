package com.bit.module.oa.vo.redis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/3/22 11:14
 */
@Data
public class MeetingDelay extends RedisDelay {
    private Long id;

    private Date startTime;

    private Date endTime;
}
