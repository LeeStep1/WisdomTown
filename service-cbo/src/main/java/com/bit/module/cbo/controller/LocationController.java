package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.service.LocationService;
import com.bit.module.cbo.vo.LocationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/19 14:11
 **/
@RestController
@RequestMapping("/location")
public class LocationController {

	@Autowired
	private LocationService locationService;

	/**
	 * 根据社区id查询小区
	 * @param orgId
	 * @return
	 */
	@GetMapping("/getCommunityByOrgId/{orgId}")
	public BaseVo getCommunityByOrgId(@PathVariable(value = "orgId")Long orgId){
		return locationService.getCommunityByOrgId(orgId);
	}

	/**
	 * 根据小区查询楼栋 单元 楼层 房屋
	 * @return
	 */
	@PostMapping("/getLocationByFId")
	public BaseVo getLocationByFId(@RequestBody LocationVO locationModelVO){
		return locationService.getLocationByFId(locationModelVO);
	}

	/**
	 * 楼栋新增
	 * @param locationModelVO
	 * @return
	 */
	@PostMapping("/addBuilding")
	public BaseVo addBuilding(@RequestBody LocationVO locationModelVO){
		return locationService.addBuilding(locationModelVO);
	}

	/**
	 * 单元新增
	 * @param locationModelVO
	 * @return
	 */
	@PostMapping("/addUnit")
	public BaseVo addUnit(@RequestBody LocationVO locationModelVO){
		return locationService.addUnit(locationModelVO);
	}

	/**
	 * 楼层新增
	 * @param locationModelVO
	 * @return
	 */
	@PostMapping("/addFloor")
	public BaseVo addFloor(@RequestBody LocationVO locationModelVO){
		return locationService.addFloor(locationModelVO);
	}
	/**
	 * 房屋新增
	 * @param locationModelVO
	 * @return
	 */
	@PostMapping("/addRoom")
	public BaseVo addRoom(@RequestBody LocationVO locationModelVO){
		return locationService.addRoom(locationModelVO);
	}

	/**
	 * 楼栋 房屋 更新
	 * @param locationModelVO
	 * @return
	 */
	@PostMapping("/updateBuildingOrRoom")
	public BaseVo updateBuilding(@RequestBody LocationVO locationModelVO){
		return locationService.updateBuildingOrUnit(locationModelVO);
	}

	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@GetMapping("/delById/{id}")
	public BaseVo delById(@PathVariable(value = "id")Long id){
		return locationService.delById(id);
	}

	/**
	 * 复制单元
	 * @param locationModelVO
	 * @return
	 */
	@PostMapping("/copyUnit")
	public BaseVo copyUnit(@RequestBody LocationVO locationModelVO){
		return locationService.copyUnit(locationModelVO);
	}

	/**
	 * 查询房间信息
	 * @param roomId
	 * @return
	 */
	@GetMapping("/getLocationInfo/{roomId}")
	public BaseVo getLocationInfo(@PathVariable(value = "roomId")Long roomId){
		return locationService.getLocationInfo(roomId);
	}

	/**
	 * app小区树形结构
	 * @param comunityId
	 * @return
	 */
	@GetMapping("/appLocationTree/{comunityId}")
	public BaseVo appLocationTree(@PathVariable(value = "comunityId")Long comunityId){
		return locationService.appLocationTree(comunityId);
	}

	/**
	 * 查询小区下所有记录
	 * @param communityId
	 * @return
	 */
	@GetMapping("/appGetAll/{comunityId}")
	public BaseVo appGetAll(@PathVariable(value = "communityId")Long communityId){
		return locationService.appGetAll(communityId);
	}

	/**
	 * 查询单元下有多少个房屋
	 * @param id
	 * @return
	 */
	@GetMapping("/countRoomWithinUnit/{id}")
	public BaseVo countRoomWithinUnit(@PathVariable(value = "id")Long id){
		return locationService.countRoomWithinUnit(id);
	}
}
