package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.Location;
import com.bit.module.cbo.vo.LocationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface LocationDao {
	/**
	 * 根据id查询房屋信息
	 * @param id
	 * @return
	 */
	Location getLocationById(@Param(value = "id") Long id);

	/**
	 * 批量查询房屋信息
	 * @param ids
	 * @return
	 */
	List<Location> batchSelectByIds(@Param(value = "ids") List<Long> ids);

	/**
	 * 查询 楼 单元 房屋 等
	 * @return
	 */
	List<Location> getLocationByFId(LocationVO locationModelVO);

	/**
	 * 楼栋 单元 楼层 房屋新增
	 * @param location
	 * @return
	 */
	int add(Location location);

	/**
	 * 更新
	 * @param location
	 * @return
	 */
	int update(Location location);

	/**
	 * 多参数查询
	 * @param location
	 * @return
	 */
	List<Location> findByParam(Location location);

	/**
	 * 批量新增
	 * @param list
	 * @return
	 */
	int batchAddLocation(List<Location> list);

	/**
	 * 批量更新addresscode
	 * @param locationList
	 * @return
	 */
	int batchUpdateAddressCode(@Param(value = "locationList") List<Location> locationList);

	/**
	 * 根据id删除
	 * @param id
	 * @return
	 */
	int delById(@Param(value = "id") Long id);

	/**
	 * 根据addressCode删除
	 * @param addressCode
	 * @return
	 */
	int delByAddressCode(@Param(value = "addressCode") String addressCode);

	/**
	 * 根据addresscode查询记录
	 * @return
	 */
	List<Location> getRecordsByLikeAddressCode(@Param(value = "addressCode") String addressCode);

	/**
	 * 批量查询信息 根据类型过滤 按id顺序排列
	 * @param unitCodes
	 * @Param type
	 * @return
	 */
	List<Location> batchSelectByUnitCodesAndType(@Param(value = "unitCodes") List<String> unitCodes, @Param(value = "type") Integer type);

	/**
	 * 根据父id和地址类型查询符合条件的数量
	 * @param fid
	 * @param addressType
	 * @return
	 */
	Integer locationCount(@Param(value = "fid") Long fid, @Param(value = "addressType") Integer addressType);

	/**
	 * 根据小区id查询记录
	 * @param communityId
	 * @return
	 */
	List<Location> getAll(@Param(value = "communityId") Long communityId);

	/**
	 * 批量更新房屋记录
	 * @param updateList
	 * @return
	 */
	int batchUpdateFullName(@Param(value = "updateList") List<Location> updateList);

	/**
	 * 根据
	 * @param addressCode
	 * @param id
	 * @return
	 */
	int countRoomLikeAddressCodeAndId(@Param(value = "addressCode")String addressCode,@Param(value = "id") Long id);

	/**
	 * 替换小区名称
	 * @param updateList
	 * @return
	 */
	int updateFullName(@Param(value = "updateList")List<Location> updateList);
}
