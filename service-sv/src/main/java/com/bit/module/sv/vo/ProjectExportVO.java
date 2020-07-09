package com.bit.module.sv.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectExportVO implements Serializable {

    @Excel(name = "工程名称", width = 30, orderNum = "0")
    private String name;

    @Excel(name = "建设单位", width = 20, orderNum = "1")
    private String buildingUnit;

    @Excel(name = "所在地址", width = 30, orderNum = "2")
    private String address;

    @Excel(name = "合同开工日期", format = "yyyy-MM-dd", width = 12, orderNum = "3")
    private Date startAt;

    @Excel(name = "合同竣工日期", format = "yyyy-MM-dd", width = 12, orderNum = "4")
    private Date endAt;

    @Excel(name = "状态", replace = {"待审批_0", "进行中_1", "已暂停_2", "已完成_3"}, width = 10, orderNum = "5")
    private Integer status;

    @Excel(name = "工程类别", replace = {"城建_1", "市政_2", "拆迁_3", "土整_4"}, width = 10, orderNum = "6")
    private Integer category;

    @Excel(name = "最新提交时间", format = "yyyy-MM-dd", width = 12, orderNum = "7")
    private Date updateAt;

    private static final long serialVersionUID = 1L;
}