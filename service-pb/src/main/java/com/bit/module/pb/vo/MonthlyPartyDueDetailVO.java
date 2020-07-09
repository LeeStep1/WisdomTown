package com.bit.module.pb.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2018/12/27 13:40
 */
@Data
public class MonthlyPartyDueDetailVO implements Serializable {
    private Long id;

    private Integer year;

    private Integer month;

    private String memberId;

    private String orgId;

    private Integer base;

    private Integer amount;

    private Integer paidAmount;
}
