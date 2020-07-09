package com.bit.utils;

import com.bit.common.Const;
import com.bit.common.enumerate.ConferenceStatusEnum;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class StatusUtil {
    /**
     * 根据会议的时间设置会议状态
     * @param startTime
     * @param endTime
     * @return
     */
    public Integer setConferenceStatus(Date startTime,Date endTime){
        Date now = new Date();

        if (now.before(startTime)){
            return ConferenceStatusEnum.STATUS_NOT_START.getCode();
        }
        if (now.before(endTime) && now.after(startTime)){
            return ConferenceStatusEnum.STATUS_RUNNING.getCode();
        }
        if (now.after(endTime)){
            return ConferenceStatusEnum.STATUS_END.getCode();
        }
        return null;
    }
}
