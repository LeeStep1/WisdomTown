package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-17 18:57
 */
@Data
public class ConferenceExperience {

    /**
     * id
     */
    private Long id;
    /**
     * 党员id
     */
    private Long userId;
    /**
     * 会议id
     */
    private Long conferenceId;
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 上传状态 0-未上传 1-已上传
     */
    private Integer status;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 上传文件json
     */
    private String fileDetail;

}
