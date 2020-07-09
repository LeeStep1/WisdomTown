package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * RoleRelRes
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class RoleRelResVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 角色ID
     */	
	private Long roleId;
    /**
     * 资源ID
     */	
	private Long resId;

	//columns END

}


