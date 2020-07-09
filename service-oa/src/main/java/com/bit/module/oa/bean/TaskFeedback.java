package com.bit.module.oa.bean;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * 任务反馈
 *
 * @autor xiaoyu.fang
 * @date 2019/2/14 17:15
 */
@Data
public class TaskFeedback implements Serializable {
    /**
     * ID
     */
    private Long id;
    /**
     * 任务ID
     */
    private Long taskId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 反馈内容
     */
    @NotEmpty(message = "反馈内容不能为空")
    @Length(max = 200, message = "反馈意见不能超过200字")
    private String content;
    /**
     * 1:操作记录；
     * 2：用户反馈
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 附件id集合，英文逗号分隔
     */
    private String attactIds;
    /**
     * 终止原因
     */
    private String reason;

}
