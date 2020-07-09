package com.bit.module.system.bean;

import java.util.Date;
import lombok.Data;

/**
 * UserResource
 * @author liuyancheng
 */
@Data
public class UserResource {

	//columns START

    /**
     * 主键id
     */	
	private Long id;
    /**
     * 用户id
     */	
	private Long userId;
    /**
     * 资源id
     */	
	private Long resourceId;

	//columns END

}


