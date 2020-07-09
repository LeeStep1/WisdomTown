package com.bit.module.manager.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * ProtalPbOrg
 * @author generator
 */
@Data
public class ProtalPbOrgVO extends BasePageVo{

	//columns START

    /**
     * 主键ID
     */	
	private Long id;
    /**
     * 内容
     */	
	private String content;
    /**
     * 栏目ID
     */	
	private Long categoryId;
    /**
     * 所属导航
     */
    private Long navigationId;
    /**
     * 操作人
     */	
	private Long operationUserId;
    /**
     * 操作人姓名
     */	
	private String operationUserName;

	//columns END

}


