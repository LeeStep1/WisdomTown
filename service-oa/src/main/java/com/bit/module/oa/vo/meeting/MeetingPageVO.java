package com.bit.module.oa.vo.meeting;

import com.bit.module.oa.bean.Meeting;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description :
 * @Date ： 2019/3/5 14:40
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingPageVO extends Meeting {
    private String roomName;
}
