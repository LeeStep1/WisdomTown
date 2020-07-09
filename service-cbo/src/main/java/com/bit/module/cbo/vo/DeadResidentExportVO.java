package com.bit.module.cbo.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: 死亡居民信息
 * @author: liyang
 * @date: 2019-07-22
 **/
@Data
@ExcelTarget("DeadResidentExportVO")
public class DeadResidentExportVO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 序号
     */
    @Excel(name = "序号", width = 12, orderNum = "1")
    private Long orderId;

    /**
     * 死者名称
     */
    @Excel(name = "死者姓名", width = 12, orderNum = "2")
    private String name;

    /**
     * 证件类型名称 证件类型1 身份证，2士官证、3护照、4港澳通行证
     */
    @Excel(name = "证件类型", width = 12, orderNum = "3")
    private String cardTypeString;

    /**
     * 证件号
     */
    @Excel(name = "证件号码", width = 24, orderNum = "4")
    private String cardNum;

    /**
     * 性别  1男2女
     */
    @Excel(name = "性别", width = 12, orderNum = "5")
    private String sex;

    /**
     * 年龄
     */
    @Excel(name = "年龄", width = 12, orderNum = "6")
    private Integer age;

    /**
     * 逗号分隔
     * 人群类型 1残疾、2居家养老、3失独、4低保、5其他
     */
    private String type;

    /**
     * 逗号分隔
     * 人群类型名称 1残疾、2居家养老、3失独、4低保、5其他
     */
    @Excel(name = "人群类型", width = 20, orderNum = "7")
    private String typeName;

    /**
     * 所属社区的名字
     */
    @Excel(name = "所属社区", width = 12, orderNum = "8")
    private String oaOrgName;

    /**
     * 房屋的详细地址
     */
    @Excel(name = "房屋信息", width = 24, orderNum = "9")
    private String locationName;

    /**
     * 住房的身份：1业主、2家属、3租客
     */
    @Excel(name = "身份", width = 12, orderNum = "10")
    private String identity;

    /**
     * yyyy-MM-DD 死亡日期
     */
    @Excel(name = "死亡日期", width = 12, orderNum = "11")
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private String deadTime;

}
