package com.bit.module.vol.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-01-28 19:33
 */
@Data
public class FileInfoVO {
    private Long id;

    private String fileName;

    private String path;

    private String suffix;

    private Long fileSize;

    private List<Long> fileIds;

}
