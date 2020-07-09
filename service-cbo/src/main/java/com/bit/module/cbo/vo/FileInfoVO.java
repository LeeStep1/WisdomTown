package com.bit.module.cbo.vo;

import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-01-28 19:33
 */
public class FileInfoVO {
    private Long id;

    private String fileName;

    private String path;

    private String suffix;

    private Long fileSize;

    //文件类型集合
    private List<String> fileTypeList;

    private List<Long> fileIds;

    public List<Long> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<Long> fileIds) {
        this.fileIds = fileIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public List<String> getFileTypeList() {
        return fileTypeList;
    }

    public void setFileTypeList(List<String> fileTypeList) {
        this.fileTypeList = fileTypeList;
    }
}
