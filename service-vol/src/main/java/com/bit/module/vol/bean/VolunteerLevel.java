package com.bit.module.vol.bean;

import com.bit.module.applylogs.bean.ApplyLogs;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-20 15:18
 */
@Data
public class VolunteerLevel {

    /**
     * id
     */
    private Long id;
    /**
     * 编号
     */
    private String serialNumber;
    /**
     * 姓名
     */
    private String realName;
    /**
     * 性别
     */
    private Integer sex;
    /**
     * 身份证号
     */
    private String cardId;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 民族
     */
    private Integer people;
    /**
     * 教育程度
     */
    private Integer education;
    /**
     * 出生日期
     */
    private Date birthday;
    /**
     * 健康状况
     */
    private Integer health;
    /**
     * 婚姻状况
     */
    private Integer marriage;
    /**
     * 志愿者经验
     */
    private Integer experience;
    /**
     * 政治面貌
     */
    private Integer political;
    /**
     * 志愿服务类别
     */
    private String serviceType;
    /**
     * 服务时间
     */
    private String serviceTime;
    /**
     * 特长爱好
     */
    private String hobby;
    /**
     * 籍贯
     */
    private String nativePlace;
    /**
     * 居住地
     */
    private String livingPlace;
    /**
     * 工作学习单位
     */
    private String company;

    /**
     * 站点id
     */
    private Long stationId;
    /**
     * 微信号
     */
    private String weChatId;
    /**
     * 活动时长
     */
    private BigDecimal campaignHour;
    /**
     * 捐款金额
     */
    private BigDecimal donateMoney;
    /**
     * 活动次数
     */
    private Integer campaignCount;
    /**
     * 积分
     */
    private Integer point;
    /**
     * 志愿等级
     */
    private Integer serviceLevel;
    /**
     * 审核id
     */
    private Long auditid;
    /**
     * 志愿者id
     */
    private Long volunteerId;
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
    private Date auditcreateTime;
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
     * 审批站点名称
     */
    private String auditStationName;
    /**
     * 类型列表
     */
    private List<String> typelist;
    /**
     * 日志列表
     */
    private List<ApplyLogs> applyLogs;



}
