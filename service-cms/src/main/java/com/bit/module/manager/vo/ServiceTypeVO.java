package com.bit.module.manager.vo;

import com.bit.base.vo.BasePageVo;
import lombok.Data;

/**
 * ServiceType
 * @author generator
 */
@Data
public class ServiceTypeVO extends BasePageVo{

	//columns START

    /**
     * 主键ID
     */	
	private Long id;
    /**
     * 标题
     */	
	private String title;
    /**
     * 启用状态 0 未启用  1 启用
     */	
	private Integer status;

    /**
     * 删除状态 0 正常  1 已删除
     */
    private Integer delStatus;

    /**
     * 排名
     */	
	private Integer rank;
    /**
     * 所属栏目ID
     */	
	private Long categoryId;
    /**
     * 申请条件
     */	
	private String applicationRequire;
    /**
     * 申请材料
     */	
	private String applicationMaterials;
    /**
     * 办理流程
     */	
	private String handlingProcess;
    /**
     * 常见问题
     */	
	private String frequentlyAskedQuestions;
    /**
     * 操作人
     */	
	private Long operationUserId;
    /**
     * 操作人姓名
     */	
	private String operationUserName;
    /**
     * 文件明细,json结构
     */	
	private String filesDetail;

	//columns END

}


