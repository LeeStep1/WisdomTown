package com.bit.module.sv.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

@Data
public class ProjectPageQuery extends BasePageVo {

    /**
    * 项目名称
    */
    private String name;

    /**
    * 项目状态(进度)(0：待审批，1：进行中，2：已暂停，3：已完成)
    */
    private Integer status;

    /**
    * 建设单位ID
    */
    private Long buildingUnitId;

    /**
    * 工程类别（1：城建，2：市政，3：拆迁，4：土整）
    */
    private Integer category;

    /**
    * 数据来源（3：安监，4：环保，6：城建）
    */
    private Integer source;

    private static final long serialVersionUID = 1L;
}