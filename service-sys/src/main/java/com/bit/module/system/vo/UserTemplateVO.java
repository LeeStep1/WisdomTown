package com.bit.module.system.vo;

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
@Data
public class UserTemplateVO {
    /**
     * 用户名
     */
    private String username;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 身份证号
     */
    private String idcard;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 停用启用
     */
    private Integer status;
    /**
     * 创建类型
     */
    private Integer createType;
    /**
     * 临时字段---查询字典应用
     */
    private String dictIds;

}
