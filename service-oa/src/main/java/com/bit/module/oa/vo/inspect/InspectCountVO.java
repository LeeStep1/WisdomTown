package com.bit.module.oa.vo.inspect;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/4/26 13:59
 */
@Data
public class InspectCountVO implements Serializable {
    private Integer allInspect;

    private Integer completedInspect;

    private Integer risk;

    private Integer application;
}
