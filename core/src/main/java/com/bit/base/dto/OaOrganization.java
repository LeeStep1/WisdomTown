package com.bit.base.dto;

import lombok.Data;

import java.util.Date;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2019-07-16
 **/
@Data
public class OaOrganization {

    /**
     * ID
     */
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 顺序
     */
    private Integer sort;
    /**
     * 组织编码
     */
    private String deptCode;
    /**
     * 组织描述
     */
    private String deptDescribe;


}
