package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/3/7 11:15
 */
@Data
public class Schedule implements Serializable {
    private Long id;
    /**
     * 日程标题
     */
    private String name;

    /**
     * 日程类型 1会议 2任务
     */
    private Integer type;

    private Date startTime;

    private Date endTime;

    private Long userId;
}
