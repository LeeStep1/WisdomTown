package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.pb.bean.FileInfo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * StudyVO
 * @author zhangjie
 * @date 2019-1-12
 */
@Data
public class StudyVO extends BasePageVo {

    /**
     * id
     */
    private Long id;
    /**
     * 计划主题
     */
    private String theme;
    /**
     * 计划内容
     */
    private String content;

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
     * 是否发布  0-草稿  1-已发布
     */
    private Integer isRelease;
    /**
     * 党组织id
     */
    private Long pborgId;
    /**
     * 发布时间
     */
    private Date releaseTime;

    /**
     * 附件id
     */
    private Long fileId;
    /**
     * 附件信息集合
     */
    private List<FileInfo> fileInfos;
    /**
     * 开始时间
     */
    private String startTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 党支部本身和下属id集合
     */
    private List<Long> idList;
    /**
     * 党支部下属id集合 不包含自己
     */
    private List<Long> noidList;
    /**
     * 发布状态 0-过滤草稿 1-不过滤草稿
     */
    private Integer condition;
    /**
     * 党员id
     */
    private Long userId;
    /**
     * 组织id
     */
    private Long pbId;
    /**
     * 是不是自己 0-不是 1-是
     */
    private Integer isMeFlag;
    /**
     * 学习人员
     */
    private String attendPerson;
    /**
     * 登陆用户的类型 0- 党员 1-管理员
     */
    private Integer loginType;
}
