package com.bit.module.oa.vo.meeting;

import com.bit.module.oa.bean.MeetingExecutor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/3/6 14:30
 */

@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingExecutorPageVO extends MeetingExecutor {
    private String meetingTitle;
    private Date startTime;
    private Date endTime;
}
