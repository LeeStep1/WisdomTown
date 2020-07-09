package com.bit.module.system.vo;


			import java.util.Date;
		import lombok.Data;

/**
 * 消息模板与接入端关系
 * 
 * @author chenduo
 * @email ${email}
 * @date 2020-04-13 16:41:26
 */
@Data
public class MessageTemplateRelTidVO  {

	/**
	 * 主键id
	 */
	private Long id;
	/**
	 * 模板id
	 */
	private Long templateId;
	/**
	 * 接入端id
	 */
	private String tid;
	/**
	 * 应用id
	 */
	private String appid;
	/**
	 * 是否存储消息，进行已读未读存储，通知公告待办：1：不存，0 存储
	 */
	private Integer store;


}
