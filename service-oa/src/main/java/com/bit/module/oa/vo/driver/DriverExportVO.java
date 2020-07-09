package com.bit.module.oa.vo.driver;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/17 14:22
 */
@Data
@ExcelTarget("driverExportVO")
public class DriverExportVO implements Serializable {
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long id;
    /**
     * 姓名
     */
    @Excel(name = "姓名", width = 15, orderNum = "2")
    private String name;
    /**
     * 驾照等级
     */
    @Excel(name = "驾照等级", width = 15, orderNum = "3")
    private String drivingClass;
    /**
     * 驾龄 1一年及一年以下 2一到三年 3三到五年 4五到十年 5十年以上
     */
    @Excel(name = "驾龄", width = 15, orderNum = "4")
    private Integer drivingExperience;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话", width = 15, orderNum = "5")
    private String mobile;
    /**
     * 年龄
     */
    @Excel(name = "年龄", width = 15, orderNum = "6")
    private Integer age;
    /**
     * 性别
     */
    @Excel(name = "性别", width = 15, orderNum = "7")
    private String sex;
    /**
     * 身高，单位cm
     */
    @Excel(name = "身高", width = 15, orderNum = "8")
    private Integer height;
    /**
     * 体重，单位kg
     */
    @Excel(name = "体重", width = 15, orderNum = "9")
    private String weight;
    /**
     * 健康状况
     */
    @Excel(name = "健康状况", width = 15, orderNum = "10")
    private String health;
    /**
     * 左眼视力
     */
    @Excel(name = "左眼视力", width = 15, orderNum = "11")
    private String leftVision;
    /**
     * 右眼视力
     */
    @Excel(name = "右眼视力", width = 15, orderNum = "12")
    private String rightVision;
}
