package com.bit.file.model;

import lombok.Data;

import java.util.List;

/**
 * 文件列表返回
 * @author liy
 */
@Data
public class FileInfoReturn {

    /**
     *  返回的文件明细
     */
    private List<FileInfo> fileInfoList;

    /**
     *  所有文件类型
     */
    private List<String> fileTypeList;
}
