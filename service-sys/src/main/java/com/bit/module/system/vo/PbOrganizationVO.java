package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

import java.util.Date;

/**
 * PbOrganization
 * @author liqi
 */
@Data
public class PbOrganizationVO extends BasePageVo{

	//columns START

    /**
     * id
     */	
	private Long id;
    /**
     * 代码
     */	
	private String pcode;
    /**
     * 名称
     */	
	private String name;
    /**
     * 顺序
     */	
	private Integer sort;
    /**
     * 是否审批机关，0否 1是
     */	
	private Integer isApprovalAuz;
    /**
     * 状态，0否 1是
     */	
	private Integer status;
    /**
     * 组织类型
     */	
	private Integer orgType;
    /**
     * 组织描述
     */	
	private String orgDesc;

    /**
     * 临时字段---父名称
     */
    private String pname;
    /**
     * 临时字段---父id
     */
    private String strPid;
    /**
     * 创建时间
     */
    private Date createTime;
	//columns END

    private String idStr;
}


