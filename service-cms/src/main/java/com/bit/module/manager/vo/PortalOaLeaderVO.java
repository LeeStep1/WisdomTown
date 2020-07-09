package com.bit.module.manager.vo;

import java.util.Date;
import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * PortalOaLeader
 * @author generator
 */
@Data
public class PortalOaLeaderVO extends BasePageVo{

	//columns START

    /**
     * 主键ID
     */	
	private Long id;
    /**
     * 排名
     */	
	private Integer rank;
    /**
     * 职务ID
     */	
	private Integer dutyCode;
    /**
     * 姓名
     */	
	private String name;
    /**
     * 职务介绍
     */	
	private String dutyDetail;
    /**
     * 个人简介
     */	
	private String individualResume;
    /**
     * 照片
     */	
	private Long imgId;
    /**
     * 图片地址
     */	
	private String imgUrl;
    /**
     * 启用状态  0 启用  1 停用
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

	//columns END

}


