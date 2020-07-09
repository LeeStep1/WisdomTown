package com.bit.module.oa.vo.meeting;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Description :
 * @Date ： 2019/3/6 14:22
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeetingExecutorVO extends BasePageVo {
    /**
     *
     */
    private Long id;

    /**
     * 会议id
     */
    private Long meetingId;

    /**
     * 参与者id
     */
    private Long participantId;

    /**
     * 参与者名称
     */
    private String participantName;

    /**
     * 部门id
     */
    private Long depId;

    /**
     * 部门名称
     */
    private String depName;

    /**
     * 签到时间（不为空则已签到）
     */
    private Date checkInAt;

    /**
     * 签到类型，1扫码签到 2手动录入（若未签到为空）
     */
    private Integer checkInType;

    /**
     * 是否迟到（0：false；1：true）
     */
    private Boolean late;

    /**
     * 创建时间
     */
    private Date createAt;
}
