package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * OaOrganization
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OaOrganizationVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 代码
     */	
	private String pcode;
    /**
     * 名称
     */	
	private String name;
    /**
     * 顺序
     */	
	private Integer sort;

	//columns END

}


