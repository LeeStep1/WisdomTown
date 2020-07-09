package com.bit.module.cbo.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Dict
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DictVO extends BasePageVo{

    /**
     * id
     */
    private Long id;
    /**
     * 模块/表单/类型
     */
    private String module;
    /**
     * key
     */
    private String dictCode;
    /**
     * 字符值
     */
    private String dictName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 描述
     */
    private String remark;
    /**
     * 用于批量
     */
    private List<String> codes;
}


