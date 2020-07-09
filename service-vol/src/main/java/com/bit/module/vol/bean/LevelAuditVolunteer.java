package com.bit.module.vol.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-20 21:29
 */
@Data
public class LevelAuditVolunteer {
    /**
     * id
     */
    private Long id;
    /**
     * 志愿者id
     */
    private Long volunteerId;
    /**
     * 编号
     */
    private String serialNumber;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 志愿等级
     */
    private Integer serviceLevel;
    /**
     * 申请等级
     */
    private Integer applyLevel;
    /**
     * 审核状态  0-审核中 1-已通过 2-已退回
     */
    private Integer auditStatus;
    /**
     * 审核时间
     */
    private Date createTime;
    /**
     * 申请人id
     */
    private Long createUserId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新人id
     */
    private Long updateUserId;
    /**
     * 审批的服务站id
     */
    private Long auditStationId;
    /**
     * 所属的服务站id
     */
    private Long belongStationId;
    /**
     * 服务站名称
     */
    private String auditStationName;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 身份证
     */
    private String cardId;

}
