package com.bit.module.manager.bean;

import lombok.Data;

/**
 * PortalPbLeader
 * @author liuyancheng
 */
@Data
public class PortalPbLeader{

	//columns START

    /**
     * 主键id
     */	
	private Long id;

    /**
     * 排序
     */
	private Integer rank;

    /**
     * 姓名
     */	
	private String name;
    /**
     * 图片路径
     */	
	private String headImgUrl;
    /**
     * 头像ID
     */	
	private Long headImgId;
    /**
     * 是否启用   0 启用 1停用
     */	
	private Integer status;
    /**
     * 栏目ID
     */	
	private Long categoryId;
    /**
     * 操作人
     */	
	private Long operationUserId;
    /**
     * 操作人姓名
     */	
	private String operationUserName;

    /**
     * 是否删除 0 未删除  1 已删除
     */
	private Integer delStatus;

	//columns END

}


