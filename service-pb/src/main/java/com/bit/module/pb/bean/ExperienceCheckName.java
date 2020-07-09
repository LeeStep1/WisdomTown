package com.bit.module.pb.bean;

import lombok.Data;

import java.util.List;

@Data
public class ExperienceCheckName {
    /**
     * 会议id
     */
    private Long conferenceId;
    /**
     * 文件名集合
     */
    private String fileName;
}
