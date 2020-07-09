package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * IdentityRelRole
 * @author liqi
 */
@Data
public class IdentityRelRoleVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 身份id
     */	
	private Long identityId;
    /**
     * 角色id
     */	
	private Long roleId;

	//columns END

}


