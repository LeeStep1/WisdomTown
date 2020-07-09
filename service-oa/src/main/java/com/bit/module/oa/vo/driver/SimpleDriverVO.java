package com.bit.module.oa.vo.driver;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/10 11:02
 */
@Data
public class SimpleDriverVO implements Serializable {
    private Long id;

    private String name;

    private String mobile;

    private Integer status;
}
