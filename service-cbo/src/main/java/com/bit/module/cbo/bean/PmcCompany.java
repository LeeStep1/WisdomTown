package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @description: 物业公司
 * @author: liyang
 * @date: 2019-07-18
 **/
@Data
public class PmcCompany {

    /**
     * 物业公司主键ID
     */
    private Long id;

    /**
     * 物业公司名称
     */
    private String companyName;

    /**
     * 状态 0停用 1启用
     */
    private Integer status;

    /**
     * 数据创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 数据创建人
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新者id
     */
    private Long updateUserId;
}
