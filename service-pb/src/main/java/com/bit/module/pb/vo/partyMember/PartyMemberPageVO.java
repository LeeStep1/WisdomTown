package com.bit.module.pb.vo.partyMember;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/3/5 16:44
 */
@Data
public class PartyMemberPageVO implements Serializable {

    private Long id;
    /**
     * 党员名称
     */
    private String name;
    /**
     * 党员类型，1正式 2预备
     */
    private Integer memberType;
    /**
     * 联系电话
     */
    private String mobile;
    /**
     * 组织名称[关联获取]
     */
    private String orgName;
    /**
     * 原组织名称
     */
    private String fromOrgName;
    /**
     * 入党时间
     */
    private Date joinTime;
    /**
     * 图片
     */
    private String photo;
    /**
     * 状态，0待完善 1正常 2停用
     */
    private Integer status;
    /**
     * 是否在册党员
     * 0：不在册；
     * 1：在册
     */
    private Integer isExServiceman;
    /**
     * 完成时间
     */
    private Date completeTime;
    /**
     * 停用原因
     * 1：党员去世；
     * 2：停止党籍；
     * 3：党员出党；
     * 4：其它；
     */
    private Integer reason;

}
