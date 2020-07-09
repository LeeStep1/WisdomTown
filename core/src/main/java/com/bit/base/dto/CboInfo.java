package com.bit.base.dto;

import lombok.Data;

import java.util.List;

/**
 * @Description: 社区模块的扩展字段
 * @Author: liyujun
 * @Date: 2019-07-16
 **/
@Data
public class CboInfo {

    /**
     * 自己所有的小区的实体
     */
    private List<Community> communities;

    /**
     * 当前小区
     */
    private Community currentCommunity;

    /**
     * 当前的社区
     */
    private OaOrganization currentCboOrg;

    /**
     * 自己所有的社区的实体
     */
    private List<OaOrganization> cboOrgs;

    /**
     * 角色
     * 物业类型下： 2维修工 1管理员
     * 居民类型下：1:居民 2：游客(当房屋认证通过后，将此值改变为1)
     */
    private Integer identity;

    /**
     * 人员类型：1社区 2.物业 3居民 0社区办
     */
    private Integer userType;


}
