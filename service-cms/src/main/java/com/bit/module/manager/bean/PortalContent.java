package com.bit.module.manager.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * PortalContent
 * @author liuyancheng
 */
@Data
public class PortalContent {

	//columns START

    /**
     * 主键ID
     */	
	private Long id;
    /**
     * 内容名称
     */	
	private String contentName;
    /**
     * 标题
     */	
	private String title;
    /**
     * 封面图片ID
     */	
	private Long coverId;
    /**
     * 封面路径
     */	
	private String coverUrl;
    /**
     * 视频ID
     */	
	private Long videoId;
    /**
     * 视频路径
     */	
	private String videoUrl;
    /**
     * 内容
     */	
	private String content;
    /**
     * 发布状态 0 未发布 1 已发布
     */	
	private Integer publishStatus;
    /**
     * 栏目ID
     */	
	private Long categoryId;

    /**
     * 栏目名称
     */
    private String categoryName;

    /**
     * 站点ID
     */	
	private Long stationId;

    /**
     * 站点名称
     */
    private String stationName;
    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date publishTime;
    /**
     * 创建人ID
     */	
	private Long operationUserId;
    /**
     * 操作人姓名
     */	
	private String operationUserName;

    /**
     * 内容状态 0 正常 1 已删除
     */	
	private Integer status;
    /**
     * 副标题
     */
    private String subTitle;
    /**
     * 发布方
     */
    private String publisher;

    /**
     * 发布时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

	//columns END

}


