package com.bit.module.oa.vo.vehicleApplication;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/1/17 15:33
 */
@Data
@ExcelTarget("myVehicleApplicationExportVO")
public class MyVehicleApplicationExportVO implements Serializable {

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
/**
 * 用车时间
 */
@Excel(name = "用车时间", width = 25, orderNum = "5")
private String planTime;
/**
 * 申请时间
 */
@Excel(name = "申请时间", width = 25, orderNum = "6", exportFormat = "yyyy-MM-dd HH:mm:ss")
private Date applyTime;
/**
 * 乘车人数
 */
@Excel(name = "乘车人数", width = 12, orderNum = "7")
private Integer passengerNum;
/**
 * 用车性质 0其它 1会议 2应急 3接待 4招商 5迎检
 */
@Excel(name = "用车性质", width = 12, orderNum = "8")
private String nature;
/**
 * 用车用途 1公务用车 2租赁
 */
@Excel(name = "用车用途", width = 12, orderNum = "9")
private String usage;
/**
 * 始发地
 */
@Excel(name = "始发地", width = 12, orderNum = "10")
private String origin;
/**
 * 目的地
 */
@Excel(name = "目的地", width = 12, orderNum = "11")
private String terminal;
/**
 * 用车事由
 */
@Excel(name = "用车事由", width = 50, orderNum = "12")
private String applyReason;
}
