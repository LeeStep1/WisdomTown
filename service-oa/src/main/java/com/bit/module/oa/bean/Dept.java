package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description : 部门
 * @Date ： 2019/2/15 15:04
 */
@Data
public class Dept implements Serializable {
    private Long id;

    private String name;
}
