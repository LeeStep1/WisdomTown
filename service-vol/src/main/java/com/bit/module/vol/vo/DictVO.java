package com.bit.module.vol.vo;

import lombok.Data;

import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-03-26 16:59
 */
@Data
public class DictVO {
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
