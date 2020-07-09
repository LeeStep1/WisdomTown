package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-17 11:26
 */
@Data
public class ConferenceMemberDetailVO extends BasePageVo{
    /**
     * 会议id
     */
    private Long id;
    /**
     * 党员姓名
     */
    private String name;
    /**
     * 学习时间
     */
    private Date signTime;
    /**
     * 签到情况 0-未签到 1-已签到
     */
    private Integer signSituation;
    /**
     * 签到状况 0-迟到 1-正常
     */
    private Integer signCondition;
    /**
     * 迟到原因
     */
    private String lateReason;
}
