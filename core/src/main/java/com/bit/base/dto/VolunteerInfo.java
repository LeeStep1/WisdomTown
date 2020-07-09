package com.bit.base.dto;

import lombok.Data;

/**
 * @Description:志愿者信息
 * @Author: mifei
 * @Date: 2019-03-12
 **/
@Data
public class VolunteerInfo {
	/**
	 * 志愿者id
	 */
	private Long volunteerId;
	/**
	 * 志愿者所属站点id
	 */
	private Long stationId;
	/**
	 * 志愿者所属站点名称
	 */
	private String stationName;
	/**
	 * 志愿者所属站点等级
	 */
    private Integer stationLevel;

}
