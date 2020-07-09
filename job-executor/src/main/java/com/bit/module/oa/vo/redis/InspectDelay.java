package com.bit.module.oa.vo.redis;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/3/22 11:14
 */
@Data
public class InspectDelay extends RedisDelay {
    private static final long serialVersionUID = 7608275535331747713L;
    private Long id;

    private Date startTime;

    private Date endTime;
}
