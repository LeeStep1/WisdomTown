package com.bit.module.cbo.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description 发起房屋认证前查询居民信息 展示用
 * @Author chenduo
 * @Date 2019/7/18 11:42
 **/
@Data
public class BeforeLocationApplyVO {
	/**
	 * 真实姓名
	 */
	private String realName;
	/**
	 * 证件类型 1 身份证 2士官证 3护照 4港澳通行证
	 */
	private Integer cardType;
	/**
	 * 证件号码
	 */
	private String cardNum;
	/**
	 * 证件照片 文件信息
	 */
	private List<FileInfo> credentialsPhotoFileInfos;
}
