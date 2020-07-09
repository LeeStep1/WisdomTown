package com.bit.module.pb.bean;

import lombok.Data;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-01-17 10:30
 */
@Data
public class ConferenceAndImages {

    /**
     * 会议详情
     */
    private Conference conference;
    /**
     * 会议附件或图片
     */
    private List<FileInfo> fileInfos;
    /**
     * 签到情况 0-已签到 1-未签到
     */
    private Integer signSituation;

    /**
     * 必学人员
     */
    private List<ConferenceMemberDetail> signMembers;
    /**
     * 自学人员
     */
    private List<ConferenceMemberDetail> notSignMembers;
}
