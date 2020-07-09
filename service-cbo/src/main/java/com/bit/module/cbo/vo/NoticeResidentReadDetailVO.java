package com.bit.module.cbo.vo;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/8 20:04
 **/
@Data
public class NoticeResidentReadDetailVO {
	/**
	 * 记录id
	 */
	private Long id;
	/**
	 * 通知id
	 */
	private Long noticeId;
	/**
	 * 居民id
	 */
	private Long residentId;
	/**
	 * 居民名称
	 */
	private String residentName;
	/**
	 * 手机号
	 */
	private String mobile;
	/**
	 * 是否联系：1联系，0未联系
	 */
	private Integer connectionStatus;
	/**
	 * 是否已读  0 - 未读   1 - 已读
	 */
	private Integer readStatus;

	/**
	 * 用户阅读的实际操作时间
	 */
	private Date readTime;
}
