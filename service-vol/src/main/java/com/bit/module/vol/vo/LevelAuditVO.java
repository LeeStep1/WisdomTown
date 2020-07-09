package com.bit.module.vol.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-03-20 13:32
 */
@Data
public class LevelAuditVO extends BasePageVo{
    /**
     * id
     */
    private Long id;
    /**
     * 志愿者id
     */
    private Long volunteerId;
    /**
     * 申请等级
     */
    private Integer applyLevel;
    /**
     * 审核状态  0-审核中下级审批 1-审核中上级审批 2-已通过 3-已退回
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
     * 数据锁
     */
    private Integer version;
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
    /**
     * 站点名称
     */
    private String stationName;
    /**
     * 原先等级
     */
    private Integer oldLevel;
}
