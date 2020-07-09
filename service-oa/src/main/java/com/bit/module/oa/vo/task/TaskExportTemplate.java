package com.bit.module.oa.vo.task;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/2/18 14:42
 */
@Data
@ExcelTarget("taskExportTemplate")
public class TaskExportTemplate implements Serializable {
    /**
     * 序号
     */
    @Excel(name = "序号", width = 8, orderNum = "1")
    private Integer no;
    /**
     * 任务编号
     */
    @Excel(name = "任务单号", width = 18, orderNum = "2")
    private String taskNo;
    /**
     * 任务名称
     */
    @Excel(name = "任务名称", width = 18, orderNum = "3")
    private String taskName;
    /**
     * 负责人名称
     */
    @Excel(name = "负责人", width = 15, orderNum = "4")
    private String taskPrincipalName;
    /**
     * 状态(1：进行中、2：已结束、3：已终止)
     */
    @Excel(name = "任务状态", replace = { "进行中_1", "已结束_2", "已终止_3"}, width = 12, orderNum = "5")
    private Integer taskStatus;
    /**
     * 任务类别(1：通用任务、2：计划任务)
     */
    @Excel(name = "任务类别", replace = { "通用任务_1", "计划任务_2"}, width = 12, orderNum = "6")
    private Integer taskType;
    /**
     * 开始时间
     */
    @Excel(name = "任务开始时间", format = "yyyy-MM-dd HH:mm", width = 18, orderNum = "7")
    private Date taskStartAt;
    /**
     * 结束时间
     */
    @Excel(name = "任务结束时间", format = "yyyy-MM-dd HH:mm", width = 18, orderNum = "8")
    private Date taskEndAt;
}
