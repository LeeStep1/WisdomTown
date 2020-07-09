package com.bit.module.system.bean;


import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 用户excel模板
 */
@Data
public class UserExcel {
    /**
     * 用户名
     */
    @Excel(name = "账户名称",orderNum = "0")
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
}
