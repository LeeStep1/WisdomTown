package com.bit.module.sv.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.sv.bean.IdName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class PlanVO extends BasePageVo {
    /**
     * 计划ID
     */
    @NotNull(message = "计划ID不能为空", groups = Modify.class)
    private Long id;

    /**
     * 名称
     */
    @NotBlank(message = "名称不能为空", groups = Add.class)
    private String name;

    /**
     * 任务类型
     */
    @NotNull(message = "类型不能为空", groups = Add.class)
    private Integer type;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 计划开始时间
     */
    @NotNull(message = "开始日期不能为空", groups = Add.class)
    private Date startAt;

    /**
     * 计划结束时间
     */
    @NotNull(message = "结束日期不能为空", groups = Add.class)
    private Date endAt;

    /**
     * 巡检目标单位
     */
    @NotEmpty(message = "巡检目标单位不能为空", groups = Add.class)
    private List<IdName> units;

    /**
     * 排查项目
     */
    @NotEmpty(message = "排查项目不能为空", groups = Add.class)
    private List<ElementVO> items;

    /**
     * 巡检人员
     */
    @NotEmpty(message = "巡检人员不能为空", groups = Add.class)
    private List<IdName> inspectors;

    /**
     * 来源(appId)
     */
    private Integer source;

    private static final long serialVersionUID = 1L;

    public interface Add {

    }

    public interface Modify {

    }
}