package com.bit.module.sv.vo;

import com.bit.module.sv.bean.IdName;
import com.bit.module.sv.bean.RectificationNotice;
import com.bit.module.sv.bean.Task;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ChecklistNoticeVO implements Serializable {

    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空")
    private Long id;

    /**
     * 排查开始时间
     */
    private Date checkStartAt;

    /**
     * 排查结束时间
     */
    private Date checkEndAt;

    /**
     * 巡检人员
     */
    private List<IdName> checkInspectors;

    /**
     * 巡检项目详情
     */
    private List<Task.DetailVO> items;

    /**
     * 巡检排查结果(1：通过，0：不通过)
     */
    private Integer result;

    /**
     * 巡检结果描述
     */
    private RectificationNotice.DescriptionVO description;

    /**
     * 整改单ID
     */
    private Long rectifyId;

    /**
     * 整改类型（1：现场整改，2：限期整改）
     */
    private Integer type;

    /**
     * 整改期限
     */
    private Date deadline;

    /**
     * 整改要求
     */
    private String demand;


    /**
     * 现场整改结果
     */
    private RectificationNotice.DescriptionVO rectifyResult;

    /**
     * 违反条例
     */
    private List<IdName> violationRegulations;

    /**
     * 整改依据
     */
    private List<IdName> rectificationBasis;

    private static final long serialVersionUID = 1L;
}