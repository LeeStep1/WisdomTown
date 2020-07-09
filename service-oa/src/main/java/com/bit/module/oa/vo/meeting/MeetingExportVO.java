package com.bit.module.oa.vo.meeting;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.bit.module.oa.enums.MeetingStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/3/7 15:31
 */
@Data
@ExcelTarget("meetingExportVO")
public class MeetingExportVO implements Serializable {
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;

    /**
     * 会议主题
     */
    @Excel(name = "会议主题", width = 15, orderNum = "2")
    private String title;

    /**
     * 会议地点
     */
    @Excel(name = "会议地点", width = 15, orderNum = "3")
    private String roomName;

    /**
     * 开始时间
     */
    @Excel(name = "会议开始时间", width = 25, orderNum = "4")
    private Date startTime;
    /**
     * 结束时间
     */
    @Excel(name = "会议结束时间", width = 25, orderNum = "5")
    private Date endTime;
    /**
     * 状态 0未发布 1未执行 2执行中 3已完成 4已终止 5已结束
     */
    @Excel(name = "任务状态", width = 15, orderNum = "6")
    private Integer status;

    public String getStatus() {
        return MeetingStatusEnum.getDescriptionByKey(status);
    }

    public String getStartTime() {
        if (this.startTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(this.startTime);
    }

    public String getEndTime() {
        if (this.endTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(this.endTime);
    }
}
