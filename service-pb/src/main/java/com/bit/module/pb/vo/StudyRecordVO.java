package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-25 14:12
 */
@Data
public class StudyRecordVO extends BasePageVo{

    /**
     * 学习计划id
     */
    private Long studyId;
    /**
     * 人员id
     */
    private Long userId;
    /**
     * 学习情况 0-已学习  1-未学习
     */
    private Integer studySituation;
    /**
     * 学习时间
     */
    private String studyTime;
    /**
     * 学习人员类型 0-必学人员 1-自学人员
     */
    private Integer userType;
    /**
     * 搜索开始时间
     */
    private String beginTime;
    /**
     * 搜索结束时间
     */
    private String endTime;

    /**
     * 主题
     */
    private String theme;
}
