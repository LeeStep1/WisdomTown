package com.bit.module.sv.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Date ： 2019/7/31
 */
@Data
public class RequestVO implements Serializable {
    /**
     * ids集合
     */
    private List<Long> ids;
}
