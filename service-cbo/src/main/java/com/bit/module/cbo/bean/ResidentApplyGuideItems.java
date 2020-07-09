package com.bit.module.cbo.bean;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description: 事件详情
 * @author: liyang
 * @date: 2019-08-06
 **/
@Data
public class ResidentApplyGuideItems {

    /**
     * id
     */
    private Long id;

    /**
     * 事件名称
     */
    @NotNull(message = "事项名称不能为空")
    private String name;

    /**
     * 扩展字段
     */
    @NotNull(message = "内容不能为空")
    private String param;

    /**
     * 类型：1办理条件，2材料名称，3办事流程
     */
    @NotNull(message = "类型不能为空")
    private Integer type;

    /**
     * 排序
     */
    private int sort;

    /**
     * 关联所办理事项的t_cbo_apply_guide中type为0事项的数据的id
     */
    @NotNull(message = "所属类别不能为空")
    private Long guideId;

}
