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
public class ExportVO implements Serializable {

    /**
     * 任务ID
     */
    @NotNull(message = "任务ID不能为空")
    private Long id;

    /**
     * 巡检目标单位
     */
    private String unitName;

    /**
     * 街道
     */
    private String street;

    /**
     * 巡检地址
     */
    private String address;

    /**
     * 负责人姓名
     */
    private String principalName;

    /**
     * 负责人联系方式
     */
    private String principalPhone;

    /**
     * 巡检排查结果(1：通过，0：不通过)
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
     * 巡检项目详情
     */
    private List<Task.DetailVO> items;

    /**
     * 整改期限
     */
    private Date deadline;

    /**
     * 整改要求
     */
    private String demand;

    /**
     * 违法行为
     */
    private RectificationNotice.DescriptionVO injuria;

    /**
     * 整改通知单创建时间
     */
    private Date createAt;

    /**
     * 复查结果(1：通过，0：不通过)
     */
    private Integer reviewResult;

    /**
     * 复查备注
     */
    private RectificationNotice.DescriptionVO reviewRemark;

    private static final long serialVersionUID = 1L;
}