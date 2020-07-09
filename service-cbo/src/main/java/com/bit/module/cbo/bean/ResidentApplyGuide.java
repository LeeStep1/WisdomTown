package com.bit.module.cbo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description: 办事指南，类别与事项
 * @author: liyang
 * @date: 2019-08-06
 **/
@Data
public class ResidentApplyGuide {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称
     */
    @NotNull(message = "名称不能为空")
    private String name;

    /**
     * 数据类别:1 类别，0事项
     */
    @NotNull(message = "数据类别:1 类别，0事项 不能为空")
    private Integer type;

    /**
     * 排序
     */
    private int sort;

    /**
     * 是否停用：1 启用，0 停用 2、草稿
     */
    private Integer enable;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 关联的扩展信息关联字典表中扩展信息类型：1、低保申请、2、残疾人申请、3、居家养老
     */
    private Integer extendType;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 是否有业务扩展信息:1有，0无 默认无
     */
    private Integer extend;

    /**
     * 所属类别的事件集合
     */
    private List<ResidentApplyGuide> residentApplyGuideList;

}
