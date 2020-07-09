package com.bit.module.pb.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description :
 * @Date ： 2019/1/7 16:25
 */
@Data
public class OrganizationInfoVO implements Serializable {
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * 组织类型 1-镇党委 2-党总支 3-党支部 4-基层党委 5-社区党建工作部
     */
    private Integer orgType;
}
