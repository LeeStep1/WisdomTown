package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 分配人+执行人+抄送人
 *
 * @autor xiaoyu.fang
 * @date 2019/2/14 10:34
 */
@Data
public class TaskSubset implements Serializable {
    /**
     * 对应表ID
     */
    private Long id;
    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 所在层级，
     * 0：父级；
     * 1：子级；
     */
    private Integer tier;
    /**
     * 是否有上级，有则为上级ID，没有则为0
     */
    private Long superiorId;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 执行人ID
     */
    private Long executorId;
    /**
     * 抄送人ID
     */
    private Long ccId;
    /**
     * 分配人ID
     */
    private Long assignerId;
    /**
     * 名称
     */
    private String ownerName;
}
