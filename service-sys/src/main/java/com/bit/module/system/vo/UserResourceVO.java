package com.bit.module.system.vo;

import java.util.Date;
import java.util.List;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * UserResource
 * @author liuyancheng
 */
@Data
public class UserResourceVO extends BasePageVo{

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
    /**
     * 资源id集合
     */
	private List<Long> resourceIds;

	//columns END

}


