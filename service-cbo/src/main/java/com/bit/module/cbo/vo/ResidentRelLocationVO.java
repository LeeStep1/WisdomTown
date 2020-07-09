package com.bit.module.cbo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Description 居民住房关系 展示用
 * @Author chenduo
 * @Date 2019/7/20 11:47
 **/
@Data
public class ResidentRelLocationVO {

	/**
	 * id
	 */
	private Long id;
	/**
	 * 居民id
	 */
	private Long residentId;
	/**
	 * 社区id
	 */
	private Long orgId;
	/**
	 * 小区id
	 */
	private Long communityId;
	/**
	 * 房屋id
	 */
	private Long addressId;
	/**
	 * 1-业主 2-家属 3-租客
	 */
	private Integer identityType;
	/**
	 * 地址结构
	 */
	private String structure;
	/**
	 * 地址结构代码
	 */
	private String addressCode;
	/**
	 * 社区名称
	 */
	private String communityName;
	/**
	 * 楼栋id
	 */
	private Long buildingId;
	/**
	 * 楼栋名称
	 */
	private String buildingName;
	/**
	 * 单元id
	 */
	private Long unitId;
	/**
	 * 单元名称
	 */
	private String unitName;
	/**
	 * 楼层id
	 */
	private Long floorId;
	/**
	 * 楼层名称
	 */
	private String floorName;
	/**
	 * 房间id
	 */
	private Long roomId;
	/**
	 * 房间名称
	 */
	private String roomName;
	/**
	 * 社区id集合
	 */
	private List<Long> orgIds;
	/**
	 * 住房地址全名
	 */
	private String addressFullName;
}
