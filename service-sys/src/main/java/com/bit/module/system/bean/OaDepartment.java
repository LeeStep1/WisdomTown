package com.bit.module.system.bean;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * OaDepartment
 * @author generator
 */
@Data
public class OaDepartment {

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 名称
     */	
	private String name;
    /**
     * 顺序
     */	
	private Integer sort;
    /**
     * 组织编码
     */
	private String deptCode;
    /**
     * 组织描述
     */
	private String deptDescribe;
    /**
     * 上级
     */
	private String upName;
    /**
     * 创建时间
     */
	private Date createTime;

	//columns END

    /**
     * 下级明细
     */
    private List<OaDepartment> oaDepartmentList;

}


