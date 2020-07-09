package com.bit.module.pb.bean;

import lombok.Data;

@Data
public class ExperienceFile {
    /**
     * 会议id
     */
    private Long conferenceId;
    /**
     * 文件信息
     */
    private FileInfo fileInfo;
}
