package com.bit.module.sv.vo;

import com.bit.module.sv.bean.IdName;
import com.bit.module.sv.bean.RectificationNotice;
import com.bit.module.sv.bean.Task;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ReviewChecklist implements Serializable {
    /**
     * 复查单ID
     */
    @NotNull(message = "复查单ID不能为空", groups = Modify.class)
    private Long id;

    /**
     * 所属任务ID
     */
    @NotNull(message = "所属任务ID不能为空", groups = Add.class)
    private Long refId;

    /**
     * 计划复查开始时间
     */
    @NotNull(message = "计划复查开始时间不能为空", groups = Add.class)
    private Date startAt;

    /**
     * 计划复查结束时间
     */
    @NotNull(message = "计划复查结束时间不能为空", groups = Add.class)
    private Date endAt;

    /**
     * 实际复查开始时间
     */
    private Date checkStartAt;

    /**
     * 实际复查结束时间
     */
    private Date checkEndAt;

    /**
     * 复查结果(1：通过，0：不通过)
     */
    private Integer result;

    /**
     * 结果描述
     */
    private RectificationNotice.DescriptionVO description;

    /**
     * 计划巡检人员
     */
    @NotEmpty(message = "计划巡检人员不能为空", groups = Add.class)
    private List<IdName> inspectors;

    /**
     * 复查项目
     */
    @NotEmpty(message = "复查项目不能为空", groups = Add.class)
    private List<Task.DetailVO> items;

    /**
     * 实际巡检人员
     */
    private List<IdName> checkInspectors;

    /**
     * 违反条例
     */
    private List<IdName> violationRegulations;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 更新时间
     */
    private Date updateAt;

    private static final long serialVersionUID = 1L;

    public interface Add {
    }

    public interface Modify {
    }
}