package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-17 20:23
 */
@Data
public class ConferenceExperienceFile {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 党员id
     */
    private Long userId;
    /**
     * 党员姓名
     */
    private String name;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 上传时间
     */
    private Date uploadTime;
    /**
     * 上传状态 0-未上传 1-已上传
     */
    private Integer status;
    /**
     * 上传文件id
     */
    private Long fileId;
    /**
     * 会议id
     */
    private Long conferenceId;
    /**
     * 上传文件json
     */
    private String fileDetail;
    /**
     * 身份证号
     */
    private String idcard;
}
