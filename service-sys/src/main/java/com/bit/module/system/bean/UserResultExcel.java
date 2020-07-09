package com.bit.module.system.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 用户导入回执
 */
@Data
public class UserResultExcel {
    /**
     * 用户名
     */
    @Excel(name = "账号名称",orderNum = "0")
    @NotEmpty
    private String userName;
    /**
     * 真实姓名
     */
    @Excel(name = "姓名",orderNum = "1")
    private String realName;
    /**
     * 身份证号
     */
    @Excel(name = "身份证号",orderNum = "2")
    private String idCard;
    /**
     * 手机号
     */
    @Excel(name = "手机号",orderNum = "3")
    private String mobile;
    /**
     * 结果
     */
    @Excel(name = "执行结果",orderNum = "4")
    private String result;

    /**
     * 原因
     */
    @Excel(name = "错误原因",orderNum = "5")
    private String reason;
}
