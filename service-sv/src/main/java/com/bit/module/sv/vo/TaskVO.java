package com.bit.module.sv.vo;

import com.bit.base.vo.BasePageVo;
import com.bit.module.sv.bean.IdName;
import com.bit.module.sv.bean.Task;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
public class TaskVO extends BasePageVo {
    /**
	* 任务ID
	*/
    @NotNull(message = "任务ID", groups = Modify.class)
    private Long id;

    /**
	* 所属计划ID
	*/
    @NotNull(message = "所属计划ID不能为空", groups = PageSearch.class)
    private Long planId;

    /**
	* 任务编号
	*/
    private String no;

    /**
	* 任务类型
	*/
    private Integer type;

    /**
	* 执行状态
	*/
    private Integer status;

    /**
	* 巡检目标单位Id
	*/
    private Long unitId;

    /**
	* 巡检目标单位
	*/
    private String unitName;

    /**
     * 巡检地址
     */
    private String address;

    /**
	* 计划开始时间
	*/
    private Date startAt;

    /**
	* 计划结束时间
	*/
    private Date endAt;

    /**
	* 排查项目
	*/
    private List<Task.DetailVO> items;

    /**
	* 巡检人员
	*/
    private List<IdName> inspectors;

    /**
     * 来源(appId)
     */
    private Integer source;

    private static final long serialVersionUID = 1L;

    public interface Modify {}
    public interface PageSearch {}
}