package com.bit.module.pb.vo.partyMember;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/3/5 16:00
 */
@Data
public class RelationshipTransferInfo implements Serializable {
    /**
     * 原组织名称
     */
    private String fromOrgName;
    /**
     * 组织名称
     */
    private String orgName;
    /**
     * 完成时间
     */
    private Date completeTime;

}
