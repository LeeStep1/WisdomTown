package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.LocationVO;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/19 14:23
 **/
public interface LocationService {
	/**
	 * 根据社区id查询小区
	 * @param orgId
	 * @return
	 */
	BaseVo getCommunityByOrgId(@PathVariable(value = "orgId")Long orgId);
	/**
	 * 根据小区查询楼栋 单元 楼层 房屋
	 * @return
	 */
	BaseVo getLocationByFId(LocationVO locationModelVO);

	/**
	 * 楼栋新增
	 * @param locationModelVO
	 * @return
	 */
	BaseVo addBuilding(LocationVO locationModelVO);

	/**
	 * 单元新增
	 * @param locationModelVO
	 * @return
	 */
	BaseVo addUnit(LocationVO locationModelVO);
	/**
	 * 楼层新增
	 * @param locationModelVO
	 * @return
	 */
	BaseVo addFloor(LocationVO locationModelVO);

	/**
	 * 房屋新增
	 * @param locationModelVO
	 * @return
	 */
	BaseVo addRoom(LocationVO locationModelVO);
	/**
	 * 楼栋 房屋 更新
	 * @param locationModelVO
	 * @return
	 */
	BaseVo updateBuildingOrUnit(LocationVO locationModelVO);

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	BaseVo delById(Long id);

	/**
	 * 复制单元
	 * @param locationModelVO
	 * @return
	 */
	BaseVo copyUnit(LocationVO locationModelVO);

	/**
	 * 查询房间信息
	 * @param roomId
	 * @return
	 */
	BaseVo getLocationInfo(Long roomId);

	/**
	 * app小区树形结构
	 * @param communityId
	 * @return
	 */
	BaseVo appLocationTree(Long communityId);
	/**
	 * 查询小区下所有记录
	 * @param communityId
	 * @return
	 */
	BaseVo appGetAll(Long communityId);

	/**
	 * 查询单元下有多少个房屋
	 * @param id
	 * @return
	 */
	BaseVo countRoomWithinUnit(Long id);
}
