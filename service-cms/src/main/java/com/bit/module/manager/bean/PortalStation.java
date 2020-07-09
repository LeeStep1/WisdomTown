package com.bit.module.manager.bean;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * PortalStation
 * @author liuyancheng
 */
@Data
public class PortalStation {

	//columns START

    /**
     * 主键ID
     */	
	private Long id;
    /**
     * 站点名称
     */	
	private String stationName;
    /**
     * 状态 0 启用  1  停用
     */	
	private Integer status;
    /**
     * 创建人ID
     */	
	private Long operationUserId;
    /**
     * 操作人姓名
     */	
	private String operationUserName;
	private List<PortalNavigation> secondMenu;

	//columns END

}


