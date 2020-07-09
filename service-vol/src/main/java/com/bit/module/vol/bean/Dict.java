package com.bit.module.vol.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * Dict实体类
 * @author zhangjie
 * @date 2018-12-28
 */
@Data
public class Dict implements Serializable {

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
     * 字典名字
     */
    private String dictName;
    /**
     * 排序
     */	
	private Integer sort;
    /**
     * 描述
     */
	private  String remark;

}


