package com.bit.module.system.vo;

import lombok.Data;

import java.util.Date;

/**
 * @author liuyancheng
 * @create 2019-02-22 13:50
 */
@Data
public class OaDepartmentResultVO {
    /**
     * id
     */
    private Long id;
    /**
     * 组织编码
     */
    private String pcode;
    /**
     * 组织名称
     */
    private String name;
    /**
     * 组织描述
     */
    private String orgDesc;
    /**
     * 上级
     */
    private String strPid;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 上级名称
     */
    private String upName;
    /**
     * 创建时间
     */
    private Date createTime;
}
