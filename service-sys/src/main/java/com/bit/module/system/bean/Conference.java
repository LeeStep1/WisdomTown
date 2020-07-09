package com.bit.module.system.bean;

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
     * 发布时间
     */
    private Date releaseTime;
    /**
     * 状态：0-草稿  1-未开始  2-进行中  3-已结束
     */
    private Integer status;
    /**
     * 会议心得上传率
     */
    private Integer uploadRate;
    /**
     * 是否通知 0-是  1-否
     */
    private Integer isNotice;
    /**
     * 是否签到 0-是  1-否
     */
    private Integer isSign;
    /**
     * 是否上传心得 0-是  1-否
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
     * 上传图片的id(关联附件表)
     */
    private Long imageId;
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
    private Long pbId;
    /**
     * 党支部name
     */
    private String name;
    /**
     * 党员签到信息 签到情况 0-已签到 1-未签到
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
     * 附件列表
     */
    private List<FileInfo> fileInfos;
}
