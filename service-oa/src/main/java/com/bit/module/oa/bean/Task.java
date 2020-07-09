package com.bit.module.oa.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务主表（主要是【我负责的】和【分配给我】）
 *
 * @autor xiaoyu.fang
 * @date 2019/2/13 13:43
 */
@Data
public class Task implements Serializable {
    /**
     * id
     */
    private Long id;
    /**
     * 任务编号
     */
    private String no;
    /**
     * 任务名称
     */
    private String name;
    /**
     * 负责人ID
     */
    private Long principalId;
    /**
     * 负责人名称
     */
    private String principalName;
    /**
     * 状态(1：进行中、2：已结束、3：已终止)
     */
    private Integer status;
    /**
     * 任务类别(1：通用任务、2：计划任务)
     */
    private Integer type;
    /**
     * 开始时间
     */
    private Date startAt;
    /**
     * 结束时间
     */
    private Date endAt;
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
     * 是否有上级，有则为上级ID，没有则为0
     */
    private Long superiorId;
    /**
     * 上级名称
     */
    private String superiorName;
    /**
     * 创建人ID
     */
    private Long userId;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 执行人
     */
    private List<Consumer> executor;
    /**
     * 抄送人
     */
    private List<Consumer> cc;
    /**
     * 附件id集合，英文逗号分隔
     */
    private String attactIds;

    @Data
    public static class Consumer {
        private Long id;
        private String name;
    }
}
