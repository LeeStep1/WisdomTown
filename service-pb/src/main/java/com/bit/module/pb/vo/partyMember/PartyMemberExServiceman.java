package com.bit.module.pb.vo.partyMember;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/1/2 15:39
 */
@Data
public class PartyMemberExServiceman implements Serializable {

    /**
     * id
     */
    private Long id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 电话
     */
    private String mobile;
    /**
     * 1正式2预备
     */
    private Integer memberType;
    /**
     * 组织id
     */
    private String orgId;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 状态0待完善1正常2停用
     */
    private Integer status;

    /**
     * 原服役部队
     */
    private String originalTroops;
    /**
     * 退役时间
     */
    private Date retireTime;
    /**
     * 是否自主择业，0否1是
     */
    private Integer isSelfEmployment;
}
