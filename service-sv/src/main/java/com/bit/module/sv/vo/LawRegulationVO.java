package com.bit.module.sv.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/7/31 13:41
 */
@Data
public class LawRegulationVO implements Serializable {
    private Long id;

    private String lawName;

    private String title1;

    private String content1;

    private String title2;

    private String content2;

    private String title3;

    private String content3;
}
