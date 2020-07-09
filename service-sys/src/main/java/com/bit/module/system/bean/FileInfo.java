package com.bit.module.system.bean;

import lombok.Data;

/**
 * @author chenduo
 * @create 2019-02-14 13:47
 */
@Data
public class FileInfo {

    /**
     * id
     */
    private Long id;
    /**
     * 路径
     */
    private String path;
    /**
     * 后缀
     */
    private String suffix;
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件大小
     */
    private Long fileSize;
}
