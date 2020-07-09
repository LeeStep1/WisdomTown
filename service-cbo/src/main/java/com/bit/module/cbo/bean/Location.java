package com.bit.module.cbo.bean;

import lombok.Data;

import java.util.Date;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/18 13:16
 **/
@Data
public class Location {
	/**
	 * id
	 */
	private Long id;
	/**
	 * 名称
	 */
	private String addressName;
	/**
	 * 地址编码   例如：一级/二级/
	 */
	private String addressCode;
	/**
	 * 父id
	 */
	private Long fid;
	/**
	 * 小区ID
	 */
	private Long communityId;
	/**
	 * 数据的类型：1 楼栋，2单元，3楼层，4 房屋
	 */
	private Integer addressType;
	/**
	 * 数据的创建时间
	 */
	private Date createTime;
	/**
	 * 数据的创建人
	 */
	private Long createUserId;
	/**
	 * 数据的更新时间
	 */
	private Date updateTime;
	/**
	 * 数据的更新人id
	 */
	private Long updateUserId;
	/**
	 * 信息扩展字段  如楼层名称后加（户数）
	 */
	private String extendParam;
	/**
	 * 房屋面积
	 */
	private String square;
	/**
	 * 是否出租 0-否 1-是
	 */
	private Integer rent;
	/**
	 * 地址全称
	 */
	private String fullName;

}
