package com.bit.base.dto;

import lombok.Data;

/**
 * @Description:  小区实体
 * @Author: liyujun
 * @Date: 2019-07-16
 **/
@Data
public class Community {

    /**
     * 所属小区的id
     */
    private Long id;
    /**
     * 小区名称
     */
    private String communityName;
    /**
     * 小区名称
     */
    private Long orgId;
    /**
     * '物业公司id'
     */
    private Long pmcCompanyId;
    /**
     * 社区名称
     */
    private String orgName;

}
