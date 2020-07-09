package com.bit.module.pb.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2019/1/31 14:18
 */
@Data
public class PartyDueBatchVO implements Serializable {
    private List<Integer> ids;

    private Boolean isPaid;
}
