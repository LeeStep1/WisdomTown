package com.bit.module.oa.vo.executor;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.bit.module.oa.enums.ApplicationStatusEnum;
import com.bit.module.oa.enums.CheckInStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/2/21 20:20
 */
@Data
@ExcelTarget("executorExportVO")
public class ExecutorExportVO implements Serializable {
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;
    /**
     * 巡检名称
     */
    @Excel(name = "巡检名称", width = 15, orderNum = "2")
    private String inspectName;
    /**
     * 巡检开始时间
     */
    @Excel(name = "巡检开始时间", width = 25, orderNum = "3")
    private Date inspectStartTime;
    /**
     * 巡检结束时间
     */
    @Excel(name = "巡检结束时间", width = 25, orderNum = "4")
    private Date inspectEndTime;
    /**
     * 签到状态 0缺卡 1正常 2补卡 3补卡成功 4补卡失败
     */
    @Excel(name = "签到状态", width = 15, orderNum = "5")
    private Integer checkInStatus;
    /**
     * 补卡状态 0未审批 1通过 2驳回
     */
    @Excel(name = "补卡状态", width = 15, orderNum = "11")
    private Integer applyStatus;

    public String getInspectStartTime() {
        if (this.inspectStartTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(this.inspectStartTime);
    }

    public String getInspectEndTime() {
        if (this.inspectEndTime == null) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(this.inspectEndTime);
    }

    public String getCheckInStatus() {
        if (this.checkInStatus == null) {
            return "";
        }
        return CheckInStatusEnum.getByKey(this.checkInStatus);
    }

    public String getApplyStatus() {
        if (this.applyStatus == null) {
            return "";
        }
        return ApplicationStatusEnum.getByKey(this.applyStatus);
    }
}
