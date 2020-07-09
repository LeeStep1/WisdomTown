package com.bit.module.oa.vo.task;

import com.bit.base.vo.BasePageVo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @autor xiaoyu.fang
 * @date 2019/2/13 14:02
 */
@Data
public class TaskQuery extends BasePageVo implements Serializable {
    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空", groups = {TaskQuery.FindChildren.class})
    private Long taskId;
    /**
     * 名称
     */
    private String taskName;
    /**
     * 状态(1：进行中、2：已结束、3：已终止)
     */
    private Integer taskStatus;
    /**
     * 类型
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
     * 负责人
     */
    private Long principalId;
    /**
     * 分配人
     */
    private Long assignerId;
    /**
     * 执行人ID或抄送人ID
     */
    private Long ownerId;
    /**
     * 上级节点
     */
    private Long superiorId;
    /**
     * 终止原因
     */
    private String reason;
    /**
     * 已结束
     */
    @JsonIgnore
    private Integer overStatus;
    /**
     * 进心中
     */
    @JsonIgnore
    private Integer ingStatus;

    public interface FindChildren {
    }

}
