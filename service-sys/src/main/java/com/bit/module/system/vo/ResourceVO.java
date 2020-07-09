package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * Resource
 * @author liqi
 */
@Data
public class ResourceVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * name
     */	
	private String name;
    /**
     * 0菜单1按钮
     */	
	private Integer resType;
    /**
     * url
     */	
	private String url;
    /**
     * 子系统id
     */	
	private Integer terminalId;
    /**
     * 应用id
     */	
	private Integer appId;
    /**
     * 父id
     */	
	private Long pid;
    /**
     * 1：显示 0：不显示
     */	
	private Integer display;
    /**
     * 接口
     */	
	private String api;
    /**
     * 图标
     */
    private String icon;
    /**
     * 顺序
     */
    private Integer sort;
	//columns END
    private Integer identityId ;
}


