package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/2/15 15:06
 */
@Data
public class SimpleUser implements Serializable {
    private Long id;

    private String name;
}
