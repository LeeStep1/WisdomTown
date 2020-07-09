package com.bit.module.pb.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-25 14:01
 */
@Data
public class ConferenceRecordVO extends BasePageVo {

    /**
     * id
     */
    private Long id;
    /**
     * 会议id
     */
    private Long conferenceId;
    /**
     * 参会人员id
     */
    private Long userId;
    /**
     * 签到情况 0-已签到 1-未签到
     */
    private Integer signSituation;
    /**
     * 签到时间
     */
    private Date signTime;
    /**
     * 开始时间
     */
    private String beginTime;
    /**
     * 结束时间
     */
    private String endTime;
    /**
     * 主题
     */
    private String theme;
    /**
     * 状态 0-草稿  1-未开始  2-进行中  3-已结束
     */
    private String status;


}
