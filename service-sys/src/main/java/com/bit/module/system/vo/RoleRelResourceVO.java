package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * RoleRelResource
 * @author liqi
 */
@Data
public class RoleRelResourceVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 角色id
     */	
	private Long roleId;
    /**
     * 资源id
     */	
	private Long resourceId;

	//columns END

}


