package com.bit.ms.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-02-16 19:37
 */
@Data
public class Message {

    /**
     * id
     */
    private Long id;
    /**
     * 应用id
     */
    private Long appId;
    /**
     * 标题
     */
    private String title;

    /**
     * 类目
     */
    private Integer categoryId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户id
     */
    private Long userId;
    /**
     * 组织id
     */
    private Long orgId;



}
