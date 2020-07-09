package com.bit.module.vol.bean;

import lombok.Data;

import java.io.Serializable;

@Data
public class FileInfo implements Serializable {

    private Long id;

    private String fileName;

    private String path;

    private String suffix;

    private Long fileSize;
    /**
     * 文件类型 0-学习计划 1-学习心得 2-会议图片
     */
    private Integer type;

}
