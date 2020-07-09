package com.bit.module.pb.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description :
 * @Date ： 2018/12/26 17:57
 */
@Data
public class PersonalMonthlyPartyDueVO implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String sex;
    /**
     * 党组织id
     */
    private Long orgId;
    /**
     * 党组织
     */
    private String orgName;
    /**
     * 党员党费月度统计
     */
    private List<MonthlyPartyDueDetailVO> partyDues;
}
