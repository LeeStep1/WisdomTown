package com.bit.module.oa.vo.task;

import com.bit.module.oa.bean.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务的WEB端和APP端列表
 *
 * @autor xiaoyu.fang
 * @date 2019/2/13 14:03
 */
@Data
public class SimpleTask implements Serializable{
    /**
     * 对应表ID
     */
    private Long id;
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 负责人名称
     */
    private String taskPrincipalName;
    /**
     * 状态(1：进行中、2：已结束、3：已终止)
     */
    private Integer taskStatus;
    /**
     * 任务类别(1：通用任务、2：计划任务)
     */
    private Integer taskType;
    /**
     * 开始时间
     */
    private Date taskStartAt;
    /**
     * 结束时间
     */
    private Date taskEndAt;
    /**
     * 上级ID
     */
    private Long superiorId;
    /**
     * 用户身份，1:负责人；2：分配人；3：执行人；4：抄送人
     */
    private Integer userIdentity;
    /**
     * 所在层级，
     * 0：父级；
     * 1：子级；
     */
    @JsonIgnore
    private Integer tier;

    /**
     * 负责人ID
     */
    @JsonIgnore
    private Long taskPrincipalId;
    /**
     * 执行人
     */
    @JsonIgnore
    private List<Task.Consumer> executor;
    /**
     * 分配人ID
     */
    @JsonIgnore
    private Long assignerId;

    /**
     * 分组数量
     */
    @JsonIgnore
    private Integer num;
}
