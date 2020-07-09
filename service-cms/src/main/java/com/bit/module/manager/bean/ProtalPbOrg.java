package com.bit.module.manager.bean;

import lombok.Data;

/**
 * ProtalPbOrg
 * @author liuyancheng
 */
@Data
public class ProtalPbOrg {

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


