package com.bit.module.pb.bean;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-16 17:05
 */
@Data
public class StudyMemberDetail {
    /**
     * 学习计划id
     */
    private Long id;
    /**
     * 党员姓名
     */
    private String name;

    /**
     * 学习情况 0-已学习  1-未学习
     */
    private Integer studySituation;
    /**
     * 学习人员类型 0-必学人员 1-自学人员
     */
    private Integer userType;
    /**
     * 学习时间
     */
    private Integer studyTime;
    /**
     * 党员id
     */
    private Long memberId;
    /**
     * 学习时长字符串
     */
    private String strTime;

}
