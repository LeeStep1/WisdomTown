package com.bit.module.sv.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Item implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 父节点ID
     */
    private Long parentId;

    /**
     * 项目名称
     */
    private String name;

    /**
     * 排查内容
     */
    private String content;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 执法依据,正文ID集合
     */
    private List<Integer> regulations;

    /**
     * 来源(appId)
     */
    private Integer source;
}