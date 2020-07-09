package com.bit.module.sv.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class HazardList implements Serializable {
    /**
    * ID
    */
    private Long id;

    /**
    * 标题内容
    */
    private String title;

    /**
    * 操作类型（1：勾选，2：输入）
    */
    private Integer type;

    /**
    * 父级ID
    */
    private Long parentId;

    private static final long serialVersionUID = 1L;
}