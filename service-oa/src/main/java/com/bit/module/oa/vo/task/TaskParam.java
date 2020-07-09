package com.bit.module.oa.vo.task;

import com.bit.module.oa.bean.Task;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 新增任务
 *
 * @autor xiaoyu.fang
 * @date 2019/2/13 14:22
 */
@Data
public class TaskParam implements Serializable {
    /**
     * 任务编号
     */
    private String taskNo;
    /**
     * 任务名称
     */
    @NotNull
    private String taskName;
    /**
     * 负责人ID
     */
    private Long taskPrincipalId;
    /**
     * 任务类别(1：通用任务、2：计划任务)
     */
    @NotNull
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
     * 分配人ID
     */
    private Long assignerId;
    /**
     * 正文
     */
    private String content;
    /**
     * 是否有上级，有则为上级ID，没有则为0
     */
    private Long superiorId;
    /**
     * 执行人
     */
    private List<Long> executor;
    /**
     * 抄送人
     */
    private List<Long> cc;
    /**
     * 附件id集合，英文逗号分隔
     */
    private String attactIds;

}
