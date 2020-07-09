package com.bit.module.oa.vo.meeting;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/3/5 14:07
 */
@Data
public class RoomUsing implements Serializable {
    private Long id;

    private String meetingTitle;

    private Long roomId;

    private String roomName;

    private Date startTime;

    private Date endTime;
}
