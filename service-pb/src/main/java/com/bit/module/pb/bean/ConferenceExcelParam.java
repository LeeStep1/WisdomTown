package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-08 15:55
 */
@Data
public class ConferenceExcelParam {

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
    private String startTime;
    /**
     * 会议持续时间
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
     * 状态：0-草稿  1-未开始  2-进行中  3-已结束  为自己使用
     */
    private Integer status;
    /**
     * 会议类型 1 - 党员大会 2- 支部委员会 3- 党小组会 4 -党课
     */
    private Integer conferenceType;
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
    private String signRate;
    /**
     * 自选动作
     */
    private String optionalAction;

    /**
     * 搜索开始时间
     */
    private String beginTime;
    /**
     * 搜索结束时间
     */
    private String endTime;
    /**
     * 搜索党支部id
     */
    private Long pbId;
    /**
     * 党支部本身和下属id集合
     */
    private List<Long> idList;
    /**
     * 党支部下属id集合 不包含自己
     */
    private List<Long> noidList;
    /**
     * 是不是自己 0-不是 1-是
     */
    private Integer isMeFlag;
    /**
     * 用来过滤 草稿状态 0-过滤 1-不过滤
     */
    private Integer condition;
    /**
     * 党员签到信息 签到情况 0-已签到 1-未签到
     */
    private Integer signSituation;
    /**
     * 当前用户id
     */
    private Long userId;

    /**
     * 登陆用户的类型
     */
    private Integer createType;
    /**
     * 子部门
     */
    private List<Long> childOrg;
    /**
     * 自己的部门
     */
    private Long ownId;
    /**
     * 当前时间
     */
    private Date nowDate;
}
