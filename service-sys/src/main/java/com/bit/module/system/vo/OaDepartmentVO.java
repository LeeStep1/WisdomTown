package com.bit.module.system.vo;

import java.util.Date;
import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * OaDepartment
 * @author generator
 */
@Data
public class OaDepartmentVO extends BasePageVo{

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
     * 上一级
     */
	private String upLevel;
    /**
     * 组织编码
     */
    private String deptCode;
    /**
     * 组织描述
     */
    private String deptDescribe;
    /**
     * 创建时间
     */
    private Date createTime;

	//columns END

}


