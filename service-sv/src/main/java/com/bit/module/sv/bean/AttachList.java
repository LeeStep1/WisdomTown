package com.bit.module.sv.bean;

import java.io.Serializable;
import lombok.Data;

@Data
public class AttachList implements Serializable {
    /**
    * ID
    */
    private Long id;

    /**
    * 标题内容
    */
    private String title;

    /**
    * 附件类型（1：pdf，2：excel，3：word，9：其他）
    */
    private Integer type;

    /**
    * 备注
    */
    private String remark;

    /**
    * 父级ID
    */
    private Long parentId;

    private static final long serialVersionUID = 1L;
}