package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * UserRelIdentity
 * @author liqi
 */
@Data
public class UserRelIdentityVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 用户id
     */	
	private Long userId;
    /**
     * 角色id
     */	
	private Long identityId;

	//columns END

}


