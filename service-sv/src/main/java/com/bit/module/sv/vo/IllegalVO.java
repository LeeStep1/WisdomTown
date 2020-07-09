package com.bit.module.sv.vo;

import com.bit.module.sv.bean.IdName;
import com.bit.module.sv.bean.RectificationNotice;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 企业违法违规行为实体
 */
@Data
public class IllegalVO implements Serializable {
    /**
     * 整改通知单ID
     */
    private Long id;

    /**
     * 任务编号
     */
    private String taskNo;

    /**
     * 整改期限
     */
    private Date deadline;

    /**
     * 整改要求
     */
    private String demand;

    /**
     * 创建时间
     */
    private Date createAt;

    /**
     * 整改依据
     */
    private List<IdName> rectificationBasis;

    /**
     * 违法行为
     */
    private RectificationNotice.DescriptionVO injuria;

    /**
     * 违反条例
     */
    private List<IdName> violationRegulations;

    private static final long serialVersionUID = 1L;
}