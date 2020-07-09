package com.bit.module.oa.vo.task;

import com.bit.module.oa.bean.Task;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务的WEB端和APP端详细信息
 *
 * @autor xiaoyu.fang
 * @date 2019/2/13 14:14
 */
@Data
public class FullTask implements Serializable {

    /**
     * id
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
     * 负责人ID
     */
    private Long taskPrincipalId;
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
     * 分配人ID
     */
    private Long assignerId;
    /**
     * 分配人名称
     */
    private String assignerName;
    /**
     * 正文
     */
    private String content;
    /**
     * 终止原因
     */
    private String reason;
    /**
     * 是否有上级，有则为上级ID，没有则为0
     */
    private Long superiorId;
    /**
     * 上级名称
     */
    private String superiorName;
    /**
     * 执行人
     */
    private List<Task.Consumer> executor;
    /**
     * 抄送人
     */
    private List<Task.Consumer> cc;
    /**
     * 附件id集合，英文逗号分隔
     */
    private String attactIds;
    /**
     * 用户身份，1:负责人；2：分配人；3：执行人；4：抄送人
     */
    private Integer userIdentity;

}
