package com.bit.officialdoc.entity;

import lombok.Data;

import java.util.Date;

/**
 * 个人文件夹
 * @author terry.jiang[taoj555@163.com] on 2019-01-15.
 */
@Data
public class CustomFolder {

    /**
     *
     */
    private Long id;

    /**
     * 所有者id
     */
    private Long ownerId;

    /**
     * 文件夹名称
     */
    private String name;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     *
     */
    private String remark;
}
