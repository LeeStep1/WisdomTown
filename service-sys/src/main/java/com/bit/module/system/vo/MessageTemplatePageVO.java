package com.bit.module.system.vo;

import com.bit.base.vo.BasePageVo;
import java.util.Date;
import lombok.Data;

/**
 * 消息模板
 * 
 * @author chenduo
 * @email ${email}
 * @date 2020-04-13 16:31:39
 */
@Data
public class MessageTemplatePageVO extends BasePageVo {

	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 内容
	 */
	private String context;
	/**
	 * 类目(对应dict中的_category中code字段)
	 */
	private String category;
	/**
	 * 类型 1消息2待办3通知4公告5已办
	 */
	private Integer msgType;
	/**
	 * web端跳转url
	 */
	private String url;
	/**
	 * appid
	 */
	private Integer appId;
	/**
	 * 用户类型 1内部用户2外部0所有（user表用户）,4自有的业务表用户
	 */
	private Integer userType;
	/**
	 * app跳转url
	 */
	private String appUrl;
	/**
	 * 是否需要跳转 0、需要  1、不需要(暂时未用到)
	 */
	private Integer jumpFlag;
	/**
	 * 一级菜单(暂时未用到)
	 */
	private Integer levelOneMenu;
	/**
	 * 新增的字段，对应新增的表t_sys_message_category表中的id
	 */
	private Long categoryId;


}
