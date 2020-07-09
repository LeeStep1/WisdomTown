package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * Identity
 * @author liqi
 */
@Data
public class IdentityVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 身份名称
     */	
	private String name;
    /**
     * 身份描述
     */	
	private String remark;
    /**
     * 应用id
     */	
	private Integer appId;
    /**
     * 默认身份 0默认  1反之
     */
	private Integer acquiesce;
    /**
     * 临时字段 应用name
     */
	private String appName;
    /**
     * 临时字段 角色的名称用逗号分开
     */
	private String roleNames;
	//columns END

}


