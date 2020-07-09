package com.bit.module.manager.bean;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * PortalNavigation
 * @author liuyancheng
 */
@Data
public class PortalNavigation {

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
    private List<PortalCategory> secondMenu;

	//columns END

}


