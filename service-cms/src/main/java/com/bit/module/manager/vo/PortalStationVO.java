package com.bit.module.manager.vo;

import java.util.Date;
import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * PortalStation
 * @author generator
 */
@Data
public class PortalStationVO extends BasePageVo{

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

	//columns END

}


