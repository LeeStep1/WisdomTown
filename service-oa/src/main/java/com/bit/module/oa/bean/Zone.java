package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/2/13 16:17
 */
@Data
public class Zone implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 名称
     */
    private String name;

    /**
     * 负责人姓名
     */
    private String principalName;

    /**
     * 状态，0停用 1启用
     */
    private Integer status;
}
