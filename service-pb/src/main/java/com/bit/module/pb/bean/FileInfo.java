package com.bit.module.pb.bean;

import lombok.Data;

@Data
public class FileInfo {

    private Long id;

    private String fileName;

    private String path;

    private String suffix;

    private Long fileSize;
    /**
     * 文件类型 1-学习计划附件 2-会议附件
     */
    private Integer type;

}
