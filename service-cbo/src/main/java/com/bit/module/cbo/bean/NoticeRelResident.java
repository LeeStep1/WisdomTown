package com.bit.module.cbo.bean;

import lombok.Data;

import java.util.Date;

/**
 * @Description 通知与居民关联关系表
 * @Author chenduo
 * @Date 2019/8/5 9:35
 **/
@Data
public class NoticeRelResident {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 关联的通知的id
	 */
	private Long noticeId;
	/**
	 * 居民的id
	 */
	private Long residentId;
	/**
	 * 是否已读  0 - 未读   1 - 已读
	 */
	private Integer readStatus;
	/**
	 * 用户阅读的实际操作时间
	 */
	private Date readTime;
	/**
	 * 是否联系： 0未联系 1联系
	 */
	private Integer connectionStatus;
	/**
	 * 联系的时间
	 */
	private Date connectionTime;
	/**
	 * 联系人的id
	 */
	private Long connectionUserId;
}
