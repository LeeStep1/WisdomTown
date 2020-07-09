package com.bit.module.sv.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Plan implements Serializable {
    /**
     * 计划ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 任务类型
     */
    private Integer type;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 计划开始时间
     */
    private Date startAt;

    /**
     * 计划结束时间
     */
    private Date endAt;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    /**
     * 巡检目标单位
     */
    private List<IdName> units;

    /**
     * 排查项目
     */
    private List<Task.DetailVO> items;

    /**
     * 巡检人员
     */
    private List<IdName> inspectors;


    /**
     * 任务总数量
     */
    private Integer totalTaskNum;

    /**
     * 已完成任务数量
     */
    private Integer completedTaskNum;

    /**
     * 来源(appId)
     */
    private Integer source;

    private static final long serialVersionUID = 1L;
}