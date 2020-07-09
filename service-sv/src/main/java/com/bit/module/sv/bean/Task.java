package com.bit.module.sv.bean;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class Task implements Serializable {
    /**
	* 任务ID
	*/
    private Long id;

    /**
	* 所属计划ID
	*/
    private Long planId;

    /**
	* 所属计划名称
	*/
    private String planName;

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
    private String unitAddress;

    /**
	* 计划开始时间
	*/
    private Date startAt;

    /**
	* 计划结束时间
	*/
    private Date endAt;

    /**
	* 创建时间
	*/
    private Date createAt;

    /**
	* 更新时间
	*/
    private Date updateAt;

    /**
	* 计划巡检人员
	*/
    private List<IdName> inspectors;

    /**
     * 排查项目
     */
    private List<DetailVO> items;

    //----------------------------------- 排查详情 ----------------------------------------------------------------------
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
     * 排查人员
     */
    private List<IdName> checkInspectors;

    /**
     * 复查单的任务ID
     */
    private Long refId;

    /**
     * 描述说明
     */
    private RectificationNotice.DescriptionVO description;

    /**
     * 违反条例
     */
    private List<IdName> violationRegulations;

    /**
     * 来源(appId)
     */
    private Integer source;

    private static final long serialVersionUID = 1L;

    @Data
    public static class DetailVO extends IdName {
        private List<ResultVO> children;
    }

    @Data
    public static class ResultVO extends IdName{
        private Integer result;
        private String remark;
        private List<String> images;
    }
}