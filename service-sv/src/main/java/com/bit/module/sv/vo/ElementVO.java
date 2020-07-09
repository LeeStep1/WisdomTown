package com.bit.module.sv.vo;

import com.bit.module.sv.bean.IdName;
import lombok.Data;

import java.util.List;

@Data
public class ElementVO extends IdName {

    /**
     * 备注
     */
    private String remark;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 层级
     */
    private Integer level;

    /**
     * 父节点
     */
    private Long parentId;

    /**
     * 子节点
     */
    private List<ElementVO> children;
}
