package com.bit.module.cbo.vo;

import com.bit.module.cbo.bean.Resident;
import lombok.Data;

import java.util.List;

/**
 * @Description 房间信息展示用
 * @Author chenduo
 * @Date 2019/7/24 15:34
 **/
@Data
public class LocationInfoVO {
	/**
	 * id
	 */
	private Long id;

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
	/**
	 * 业主信息
	 */
	private List<Resident> ownerList;
	/**
	 * 家属信息
	 */
	private List<Resident> relativeList;
	/**
	 * 租户信息
	 */
	private List<Resident> rentList;
}
