package com.bit.module.pb.bean;

import lombok.Data;

import java.util.Date;

/**
 * @author chenduo
 * @create 2019-01-16 10:27
 */
@Data
public class StudyRelFileInfo {
    /**
     * 数据id
     */
    private Long id;
    /**
     * 学习计划或会议id
     */
    private Long studyId;
    /**
     * 文件id
     */
    private Long fileId;
    /**
     * 文件类型 
     */
    private Integer type;



}
