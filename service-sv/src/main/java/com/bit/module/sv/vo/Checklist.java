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
public class Checklist implements Serializable {
    /**
	* 任务ID
	*/
    @NotNull(message = "任务ID不能为空")
    private Long id;

    /**
     * 巡检结果(1：通过，0：不通过)
     */
    private Integer result;


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
	* 巡检详情
	*/
    private List<Task.DetailVO> items;

    /**
     * 巡检结果描述
     */
    private RectificationNotice.DescriptionVO description;

    /**
     * 违反条例
     */
    private List<IdName> violationRegulations;

    private static final long serialVersionUID = 1L;
}