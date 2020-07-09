package com.bit.module.manager.vo;

import java.util.Date;
import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * PortalNavigation
 * @author generator
 */
@Data
public class PortalNavigationVO extends BasePageVo{

	//columns START

    /**
     * id主键 
     */	
	private Long id;
    /**
     * 导航名称
     */	
	private String navigationName;
    /**
     * 状态 0 启用  1  停用
     */	
	private Integer status;
    /**
     * 所属导航
     */	
	private Long stationId;
    /**
     * 操作人ID
     */	
	private Long operationUserId;
    /**
     * 操作人姓名
     */	
	private String operationUserName;

	//columns END

}


