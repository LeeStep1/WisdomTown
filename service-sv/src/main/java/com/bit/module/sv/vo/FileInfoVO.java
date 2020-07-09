package com.bit.module.sv.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author decai.liu
 * @create 2019-08-30
 */
@Data
public class FileInfoVO implements Serializable {

    /**
     * 附件项ID
     */
    private Long id;

    /**
     * 附件名称
     */
    private String fileName;

    /**
     * 附件路径
     */
    private String path;

    /**
     * 附件后缀
     */
    private String suffix;

    /**
     * 附件大小
     */
    private Long fileSize;

    /**
     * 附件ID
     */
    private Long fileId;

}
