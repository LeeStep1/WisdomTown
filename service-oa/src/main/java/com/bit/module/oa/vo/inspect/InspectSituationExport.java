package com.bit.module.oa.vo.inspect;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.bit.module.oa.enums.InspectTypeEnum;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/21 21:30
 */
@Data
@ExcelTarget("inspectSituationExport")
public class InspectSituationExport {
    /**
     * id
     */
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;
    /**
     * 巡检单号
     */
    @Excel(name = "任务单号", width = 15, orderNum = "2")
    private String no;
    /**
     * 巡检名称
     */
    @Excel(name = "任务名称", width = 15, orderNum = "3")
    private String name;
    /**
     * 类型 1智能巡检 2轨迹巡检
     */
    @Excel(name = "巡检类型", width = 15, orderNum = "4")
    private Integer type;
    /**
     * 巡检开始时间
     */
    @Excel(name = "巡检开始时间", width = 15, orderNum = "5")
    private Date startTime;
    /**
     * 巡检结束时间
     */
    @Excel(name = "巡检结束时间", width = 15, orderNum = "6")
    private Date endTime;
    /**
     * 最后上报时间
     */
    @Excel(name = "最后上报时间", width = 15, orderNum = "7")
    private Date lastReportAt;


    public String getType() {
        return InspectTypeEnum.getByKey(type).getDescription();
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

    public String getLastReportAt() {
        if (this.lastReportAt == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(this.lastReportAt);
    }
}
