package com.bit.module.oa.vo.vehicleApplication;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/1/17 17:17
 */
@Data
@ExcelTarget("handleVehicleApplicationExportVO")
public class HandleVehicleApplicationExportVO implements Serializable {

    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;
    /**
     * 申请单号
     */
    @Excel(name = "申请单号", width = 20, orderNum = "2")
    private String applyNo;
    /**
     * 申请人
     */
    @Excel(name = "申请人", width = 12, orderNum = "3")
    private String userName;
    /**
     * 申请科室名称
     */
    @Excel(name = "申请科室", width = 12, orderNum = "4")
    private String orgName;

    @Excel(name = "派车人", width = 12, orderNum = "5")
    private String assignerName;
    /**
     * 乘车人数
     */
    @Excel(name = "乘车人数", width = 12, orderNum = "6")
    private Integer passengerNum;
    /**
     * 用车性质 0其它 1会议 2应急 3接待 4招商 5迎检
     */
    @Excel(name = "用车性质", width = 12, orderNum = "7")
    private String nature;
    /**
     * 用车用途 1公务用车 2租赁
     */
    @Excel(name = "用车用途", width = 12, orderNum = "8")
    private String usage;
    /**
     * 始发地
     */
    @Excel(name = "始发地", width = 12, orderNum = "9")
    private String origin;
    /**
     * 目的地
     */
    @Excel(name = "目的地", width = 12, orderNum = "10")
    private String terminal;
    /**
     * 申请用车时间
     */
    @Excel(name = "申请用车时间", width = 25, orderNum = "11")
    private String planTime;
    /**
     * 申请时间
     */
    @Excel(name = "申请时间", width = 25, orderNum = "12", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date applyTime;
    /**
     * 实际用车时间
     */
    @Excel(name = "实际用车时间", width = 25, orderNum = "13")
    private String realTime;
    /**
     * 派车时间
     */
    @Excel(name = "派车时间", width = 25, orderNum = "14", exportFormat = "yyyy-MM-dd HH:mm:ss")
    private Date dispatchTime;
    /**
     * 驾驶员列表，英文逗号分隔，每项的格式为：[驾驶员名称]([驾驶员联系电话])
     */
    @Excel(name = "司机", width = 30, orderNum = "15")
    private String drivers;
    /**
     * 用车事由
     */
    @Excel(name = "用车事由", width = 50, orderNum = "16")
    private String applyReason;
    /**
     * 备注
     */
    @Excel(name = "备注", width = 50, orderNum = "17")
    private String remark;
}
