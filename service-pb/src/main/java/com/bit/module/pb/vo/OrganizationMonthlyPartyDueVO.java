package com.bit.module.pb.vo;

import com.bit.module.pb.bean.MonthlyPartyDue;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2018/12/24 21:04
 */
@Data
public class OrganizationMonthlyPartyDueVO implements Serializable {
    private String orgId;

    private String orgName;

    private List<MonthlyPartyDue> monthlyPartyDues;
}
