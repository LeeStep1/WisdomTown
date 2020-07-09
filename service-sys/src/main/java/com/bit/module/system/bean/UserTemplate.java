package com.bit.module.system.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import cn.afterturn.easypoi.handler.inter.IExcelModel;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Objects;

/**
 * @author liuyancheng
 * @create 2019-01-16 11:14
 */
@ExcelTarget("UserTemplate")
@Data
public class UserTemplate implements IExcelModel {
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
    @Pattern(regexp = "^([\\u4e00-\\u9fa5]{1,20}|[a-zA-Z\\.\\s]{1,20})$", message = "真实姓名输入有误")
    private String realName;
    /**
     * 身份证号
     */
    @Excel(name = "身份证号",orderNum = "2")
    @Pattern(regexp = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|\"(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)", message = "身份证输入不合法")
    private String idCard;
    /**
     * 手机号
     */
    @Excel(name = "手机号",orderNum = "3")
    @Pattern(regexp = "^1[3456789]\\d{9}$", message = "手机号输入有误")
    private String mobile;
    /**
     * 停用启用
     */
    @Excel(name = "账号状态",replace = {"已删除_0", "可用_1"}, orderNum = "4")
    @Pattern(regexp = "^[0-1]$", message = "账号状态输入有误") //字符串类型使用
    private String status;
    /**
     * 创建类型
     */
    @Excel(name = "账号创建类型",replace = {"平台创建_1", "注册_2"},orderNum = "5")
    @Pattern(regexp = "^[1-2]$", message = "账号创建类型输入有误") //字符串类型使用
    private String createType;

    private String errorMsg;

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTemplate that = (UserTemplate) o;
        return Objects.equals(userName, that.userName) &&
                Objects.equals(realName, that.realName) &&
                Objects.equals(idCard, that.idCard) &&
                Objects.equals(mobile, that.mobile);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userName, realName, idCard, mobile);
    }
}
