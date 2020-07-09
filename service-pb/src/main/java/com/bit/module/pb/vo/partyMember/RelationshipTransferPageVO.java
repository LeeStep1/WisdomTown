package com.bit.module.pb.vo.partyMember;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/3/4 17:40
 */
@Data
public class RelationshipTransferPageVO implements Serializable {
    /**
     * 表ID
     */
    private Long id;
    /**
     * 党员名称
     */
    private String name;
    /**
     * 原组织名称
     */
    private String fromOrgName;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 1正式2预备
     */
    private Integer memberType;
    /**
     * 状态，0草稿 1审核中 2已通过 3已退回 4等待接收（镇外） 5确认接收（镇外）6未接受（镇外）
     */
    private Integer status;
    /**
     * 是否退伍军人
     * 1：是；
     * 0：否
     */
    private Integer isExServiceman;
    /**
     * 待审批组织ID
     */
    private String approveOrgId;

    private Date insertTime;

}
