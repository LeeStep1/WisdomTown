package com.bit.module.cbo.vo;

import lombok.Data;

import java.util.List;

/**
 * @Description 房屋结构树展示用
 * @Author chenduo
 * @Date 2019/7/25 10:57
 **/
@Data
public class LocationTreeVO {
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
	 * 数据的类型：1 楼栋，2单元，3楼层，4 房屋
	 */
	private Integer addressType;
	/**
	 * 子节点
	 */
	private List<LocationTreeVO> childLocations;
	/**
	 * 父id
	 */
	private Long fid;
}
