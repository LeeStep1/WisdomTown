package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * UserRelAppVO
 * @author liqi
 */
@Data
public class UserRelAppVO extends BasePageVo{

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
     * dictId
     */	
	private Integer appId;

	//columns END

}


