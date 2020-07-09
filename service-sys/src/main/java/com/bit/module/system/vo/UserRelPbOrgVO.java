package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * UserRelPbOrg
 * @author generator
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserRelPbOrgVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * userId
     */	
	private Long userId;
    /**
     * pborgId
     */	
	private Long pborgId;

	//columns END
    private String orgId;

}


