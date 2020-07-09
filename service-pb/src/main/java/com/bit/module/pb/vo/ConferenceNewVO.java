package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-17 8:30
 */
@Data
public class ConferenceNewVO {
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
}
