package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-16 20:32
 */
@Data
public class Conference {

    /**
     * id
     */
    private Long id;
    /**
     * 会议主题
     */
    private String theme;
    /**
     * 会议地点
     */
    private String place;
    /**
     * 会议时间
     */
    private Date startTime;
    /**
     * 会议持续时间 按小时计算
     */
    private Integer lastTime;
    /**
     * 会议提前签到时间
     */
    private Integer aheadTime;
    /**
     * 结束时间
     */
    private Date endTime;
    /**
     * 发布时间
     */
    private Date releaseTime;
    /**
     * 状态：0-草稿  1-未开始  2-进行中  3-已结束 4-已取消
     */
    private Integer status;
    /**
     * 会议心得上传率
     */
    private Integer uploadRate;
    /**
     * 是否通知 0-否  1-是
     */
    private Integer isNotice;
    /**
     * 是否需要签到 0- 否 1-是
     */
    private Integer isSign;
    /**
     * 是否上传心得 0-否  1-是
     */
    private Integer isUploadExperience;
    /**
     * 学习计划id
     */
    private Long studyId;

    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建用户id
     */
    private Long createUserId;
    /**
     * 更新时间
     */
    private Date updateTime;
    /**
     * 更新用户id
     */
    private Long updateUserId;
    /**
     * 资料附件
     */
    private List<FileInfo> attachDataFile;
    /**
     * 到课率
     */
    private Integer signRate;
    /**
     * 自选动作
     */
    private String optionalAction;
    /**
     * 党支部id
     */
    private String pbId;
    /**
     * 党支部name
     */
    private String name;
    /**
     * 党员签到信息 签到情况 0-未签到 1-已签到
     */
    private Integer signSituation;
    /**
     * 党组织id  组织表用
     */
    private Long orgId;
    /**
     * 学习计划名称
     */
    private String studyTheme;

    /**
     * 创建者用户名
     */
    private String createUserName;
    /**
     * 创建者真实姓名
     */
    private String createRealName;
    /**
     * 更新者用户名
     */
    private String updateUserName;
    /**
     * 更新者真实姓名
     */
    private String updateRealName;
    /**
     * 锁
     */
    private Integer version;
    /**
     * 附件列表 或 学习计划附件列表
     */
    private List<FileInfo> fileInfos;
    /**
     * 是否是自己发布的 0-是 1-不是
     */
    private Integer match;
    /**
     * 与会人员姓名
     */
    private String attendPerson;
    /**
     * 取消原因
     */
    private String cancelReaseon;
    
    /**
     * 会议类型 1 - 党员大会 2- 支部委员会 3- 党小组会 4 -党课
     */
    private Integer conferenceType;
    /**
     * 结束时间 页面展示使用
     */
    private String end;
    /**
     * 提前签到时间 页面展示使用
     */
    private Double atime;
    /**
     * 参会人员参数
     */
    private List<Long> memberIds;
    /**
     * 党员信息和组织信息 页面返显用
     */
    private List<PartyMemberOrgName> partyMemberOrgNameList;
    /**
     * 上传资料的附件id
     */
    private Long uploadDataFileId;
}
