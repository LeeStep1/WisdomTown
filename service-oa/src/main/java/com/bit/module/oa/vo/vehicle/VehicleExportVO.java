package com.bit.module.oa.vo.vehicle;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/17 14:40
 */
@Data
@ExcelTarget("vehicleExportVO")
public class VehicleExportVO implements Serializable {
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;
    /**
     * 车牌号
     */
    @Excel(name = "车牌号", width = 12, orderNum = "2")
    private String plateNo;
    /**
     * 品牌
     */
    @Excel(name = "品牌", width = 12, orderNum = "3")
    private String brand;
    /**
     * 车型
     */
    @Excel(name = "车型", width = 12, orderNum = "4")
    private String vehicleType;
    /**
     * 载客量
     */
    @Excel(name = "载客量", width = 12, orderNum = "5")
    private Integer seatingCapacity;
    /**
     * 发动机号
     */
    @Excel(name = "发动机号", width = 17, orderNum = "6")
    private String engineNo;
    /**
     * 车架号
     */
    @Excel(name = "车架号", width = 22, orderNum = "7")
    private String vin;
    /**
     * 颜色
     */
    @Excel(name = "颜色", width = 12, orderNum = "8")
    private String color;
    /**
     * 动力
     */
    @Excel(name = "动力", width = 12, orderNum = "9")
    private String power;
    /**
     * 号牌类型
     */
    @Excel(name = "号牌类型", width = 12, orderNum = "10")
    private String plateType;
}
