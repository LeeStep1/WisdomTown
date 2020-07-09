package com.bit.module.cbo.service.Impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.LocationAddressTypeEnum;
import com.bit.common.enumerate.ResidentIdentityEnum;
import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.LocationApplyDao;
import com.bit.module.cbo.dao.LocationDao;
import com.bit.module.cbo.dao.ResidentRelLocationDao;
import com.bit.module.cbo.vo.*;
import com.bit.module.cbo.service.LocationService;
import com.bit.utils.CommonUtil;
import com.bit.utils.DateUtil;
import com.bit.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.*;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/19 14:24
 **/
@Service("locationService")
public class LocationServiceImpl extends BaseService implements LocationService {
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private ResidentRelLocationDao residentRelLocationDao;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private LocationApplyDao locationApplyDao;
	/**
	 * 根据社区id查询小区
	 * @param orgId
	 * @return
	 */
	@Override
	public BaseVo getCommunityByOrgId(Long orgId) {
		List<Community> communityByOrgId = communityDao.getCommunityByOrgId(orgId);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(communityByOrgId);
		return baseVo;
	}
	/**
	 * 根据小区查询楼栋 单元 楼层 房屋 1 楼栋，2单元，3楼层，4 房屋
	 * @return
	 */
	@Override
	public BaseVo getLocationByFId(LocationVO locationModelVO) {
		List<LocationVO> locationVOS = new ArrayList<>();
		List<Location> locationByCommunityId = locationDao.getLocationByFId(locationModelVO);
		if (CollectionUtils.isNotEmpty(locationByCommunityId)){
			for (Location location : locationByCommunityId) {
				LocationVO locationVO = new LocationVO();
				BeanUtils.copyProperties(location,locationVO);
				if (location.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode())){
					//单元找楼层和户数
					Integer floornum = locationByCommunityId.size();
					Integer roomnum = locationDao.locationCount(location.getId(), LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
					locationVO.setFloorNum(floornum);
					locationVO.setRoomNum(roomnum);
				}
				locationVOS.add(locationVO);
			}
		}


		BaseVo baseVo = new BaseVo();
		baseVo.setData(locationVOS);
		return baseVo;
	}
	/**
	 * 楼栋新增
	 * @param locationModelVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo addBuilding(LocationVO locationModelVO) {
		Long userId = getCurrentUserInfo().getId();
		Location location = new Location();
		location.setCommunityId(locationModelVO.getCommunityId());
		//校验楼栋名称
		Location temp = new Location();
		temp.setAddressName(locationModelVO.getAddressName());
		temp.setCommunityId(locationModelVO.getCommunityId());
		temp.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode());
		List<Location> byParam = locationDao.findByParam(temp);
		if (CollectionUtils.isNotEmpty(byParam)){
			throwBusinessException("楼栋名字已存在");
		}
		location.setAddressName(locationModelVO.getAddressName());
		location.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode());
		location.setCreateTime(new Date());
		location.setCreateUserId(userId);
		location.setFid(0L);
		Community communityById = communityDao.getCommunityById(locationModelVO.getCommunityId());
		if (communityById!=null){
			location.setFullName(communityById.getCommunityName()+"/"+ locationModelVO.getAddressName()+"/");
		}
		int add = locationDao.add(location);
		if (add<=0){
			throwBusinessException("楼栋添加失败");
		}
		Location lc = new Location();
		lc.setId(location.getId());
		lc.setAddressCode(location.getId()+"/");
		int update = locationDao.update(lc);
		if (update<=0){
			throwBusinessException("楼栋更新失败");
		}
		return successVo();
	}
	/**
	 * 单元新增
	 * @param locationModelVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo addUnit(LocationVO locationModelVO) {
		Long userId = getCurrentUserInfo().getId();

		Integer floornum = locationModelVO.getFloorNum();
		Integer roomnum = locationModelVO.getRoomNum();
		if (floornum>50||roomnum>50){
			throwBusinessException("楼层和户数不能大于50");
		}
		Location location = new Location();
		location.setCommunityId(locationModelVO.getCommunityId());
		//校验单元名称
		Location temp = new Location();
		temp.setAddressName(locationModelVO.getAddressName());
		temp.setCommunityId(locationModelVO.getCommunityId());
		temp.setFid(locationModelVO.getFid());
		temp.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_UNIT.getCode());
		List<Location> byParam = locationDao.findByParam(temp);
		if (CollectionUtils.isNotEmpty(byParam)){
			throwBusinessException("单元名字已存在");
		}
		location.setAddressName(locationModelVO.getAddressName());
		location.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_UNIT.getCode());
		location.setCreateTime(new Date());
		location.setCreateUserId(userId);
		location.setFid(locationModelVO.getFid());
		//查询楼栋
		String unitName = "";
		Location building = locationDao.getLocationById(locationModelVO.getFid());
		if (building!=null){
			unitName = building.getFullName() + locationModelVO.getAddressName() + "/";
			location.setFullName(unitName);
		}



		location.setExtendParam(floornum.toString());

		int add = locationDao.add(location);
		if (add<=0){
			throwBusinessException("单元添加失败");
		}
		//查询父机构 楼栋
		Location father = locationDao.getLocationById(locationModelVO.getFid());
		String fathercode = father.getAddressCode();
		//更新自己的addressCode -- 单元
		Location lc = new Location();
		lc.setId(location.getId());
		lc.setAddressCode(fathercode + location.getId()+"/");
		lc.setExtendParam(floornum.toString());
		int update = locationDao.update(lc);
		if (update<=0){
			throwBusinessException("单元更新失败");
		}
		//更新父机构 -- 楼栋
		//查询楼栋下有多少单元
		Location ll = new Location();
		ll.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_UNIT.getCode());
		ll.setFid(father.getId());
		List<Location> unitcount = locationDao.findByParam(ll);
		if (CollectionUtils.isEmpty(unitcount)){
			father.setExtendParam("1");
		}else {
			father.setExtendParam(String.valueOf(unitcount.size()+1));
		}
		int fat = locationDao.update(father);
		if (fat<=0){
			throwBusinessException("父楼栋更新失败");
		}
		//这是单元id
		String unitcode = fathercode + location.getId()+"/";

		//开始插入楼层
		//先查询原先有的楼层 找出原来有的记录 名称自增
		String floorName = "";
		Location temp2 = new Location();
		temp2.setAddressName(locationModelVO.getAddressName());
		temp2.setCommunityId(locationModelVO.getCommunityId());
		temp2.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode());
		temp2.setFid(location.getId());
		List<Location> byParam2 = locationDao.findByParam(temp2);
		//用来接收楼层的数据
		List<Location> floorList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(byParam2)){
			int start = byParam2.size();
			for (int i = 0;i<floornum;i++){
				Location floor = new Location();
				floor.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode());
				floor.setFid(location.getId());
				floor.setCommunityId(locationModelVO.getCommunityId());
				floor.setCreateTime(new Date());
				floor.setCreateUserId(userId);
				floor.setExtendParam(roomnum.toString());
				int name = start+i+1;
				floorName = unitName+name+"/";
				floor.setAddressName(name+"");
				floor.setAddressCode(unitcode);
				floor.setFullName(floorName);
				floorList.add(floor);
			}
		}else {
			//批量新增楼层
			for (int i = 0;i<floornum;i++){
				Location floor = new Location();
				floor.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode());
				floor.setFid(location.getId());
				floor.setCommunityId(locationModelVO.getCommunityId());
				floor.setCreateTime(new Date());
				floor.setCreateUserId(userId);
				floor.setExtendParam(roomnum.toString());
				int name = i+1;
				floorName = unitName+name+"/";
				floor.setAddressName(name+"");
				floor.setAddressCode(unitcode);
				floor.setFullName(floorName);
				floorList.add(floor);
			}
		}
		int floor = locationDao.batchAddLocation(floorList);
		if (floor<=0){
			throwBusinessException("批量新增楼层失败");
		}
		//遍历楼层 设置code
		List<Long> floorIds = new ArrayList<>();
		List<Location> floorUpdateList = new ArrayList<>();
		for (Location location1 : floorList) {
			floorIds.add(location1.getId());
			Location foo = new Location();
			foo.setId(location1.getId());
			foo.setAddressCode(location1.getAddressCode()+location1.getId()+"/");
			foo.setFullName(location1.getFullName());
			floorUpdateList.add(foo);
		}

		int floors = locationDao.batchUpdateAddressCode(floorUpdateList);
		if (floors<=0){
			throwBusinessException("批量更新楼层addresscode失败");
		}

		//开始插入房屋
		String roomName = "";
		List<Location> roomList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(byParam2)){
			int start = byParam2.size();
			for (int y = 0;y < floorUpdateList.size();y++){
				for (int x = 0;x < roomnum;x++){
					Location room = new Location();
					room.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
					room.setFid(floorUpdateList.get(y).getId());
					room.setCommunityId(locationModelVO.getCommunityId());
					room.setCreateTime(new Date());
					room.setCreateUserId(userId);
					roomName = floorUpdateList.get(y).getFullName() + getRoomNumber((start+y+1),x+1)+"/";
					room.setAddressName(getRoomNumber((start+y+1),x+1));
					room.setAddressCode(floorUpdateList.get(y).getAddressCode());
					room.setFullName(roomName);
					room.setSquare(Const.DEFAULT_SQUARE);
					room.setRent(Const.IS_RENT_NO);
					roomList.add(room);
				}
			}
		}else {
			for (int y = 0;y < floorUpdateList.size();y++){
				for (int x = 0;x < roomnum;x++){
					Location room = new Location();
					room.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
					room.setFid(floorUpdateList.get(y).getId());
					room.setCommunityId(locationModelVO.getCommunityId());
					room.setCreateTime(new Date());
					room.setCreateUserId(userId);
					roomName = floorUpdateList.get(y).getFullName() + getRoomNumber(y+1,x+1)+"/";
					room.setAddressName(getRoomNumber(y+1,x+1));
					room.setAddressCode(floorUpdateList.get(y).getAddressCode());
					room.setFullName(roomName);
					room.setSquare(Const.DEFAULT_SQUARE);
					room.setRent(Const.IS_RENT_NO);
					roomList.add(room);
				}
			}
		}
		int room = locationDao.batchAddLocation(roomList);
		if (room<=0){
			throwBusinessException("批量新增房屋失败");
		}
		//遍历楼层 设置code
		List<Long> roomIds = new ArrayList<>();
		List<Location> roomUpdateList = new ArrayList<>();
		for (Location location2 : roomList) {
			roomIds.add(location2.getId());
			Location roo = new Location();
			roo.setId(location2.getId());
			roo.setAddressCode(location2.getAddressCode()+location2.getId()+"/");
			roomUpdateList.add(roo);
		}

		int rooms = locationDao.batchUpdateAddressCode(roomUpdateList);
		if (rooms<=0){
			throwBusinessException("批量更新房屋addresscode失败");
		}

		return successVo();
	}
	/**
	 * 楼层新增
	 * @param locationModelVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo addFloor(LocationVO locationModelVO) {
		Long userId = getCurrentUserInfo().getId();
		//查询出父机构 -- 单元
		Location father = locationDao.getLocationById(locationModelVO.getFid());

		Integer roomnum = locationModelVO.getRoomNum();

		//先查询原先有的楼层 找出原来有的记录 名称自增
		Location temp = new Location();
		temp.setAddressName(locationModelVO.getAddressName());
		temp.setCommunityId(locationModelVO.getCommunityId());
		temp.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode());
		temp.setFid(locationModelVO.getFid());
		List<Location> byParam = locationDao.findByParam(temp);
		if (CollectionUtils.isNotEmpty(byParam)){
			throwBusinessException("楼层名称重复");
		}

		Location floor = new Location();
		floor.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode());
		floor.setFid(locationModelVO.getFid());
		floor.setCommunityId(locationModelVO.getCommunityId());
		floor.setCreateTime(new Date());
		floor.setCreateUserId(userId);
		floor.setExtendParam(roomnum.toString());
		floor.setAddressCode(father.getAddressCode());



		Location param = new Location();
		param.setCommunityId(locationModelVO.getCommunityId());
		param.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode());
		param.setFid(locationModelVO.getFid());
		List<Location> byParams = locationDao.findByParam(param);
		String floorName = "";

		if (CollectionUtils.isNotEmpty(byParams)){
//			int start = byParams.size();
//			floor.setAddressName((start+1)+"");
			floor.setAddressName(locationModelVO.getAddressName());
//			floorName = father.getFullName()+"/"+(start+1);
			floorName = father.getFullName()+locationModelVO.getAddressName()+"/";
		}else {
			floor.setAddressName("1");
			floorName = father.getFullName()+"/"+1;
		}
		floor.setFullName(floorName);
		int flooradd = locationDao.add(floor);
		if (flooradd<=0){
			throwBusinessException("新增楼层失败");
		}
		//更新楼层 设置code
		Location foo = new Location();
		foo.setId(floor.getId());
		foo.setAddressCode(floor.getAddressCode()+floor.getId()+"/");

		int floors = locationDao.update(foo);
		if (floors<=0){
			throwBusinessException("更新楼层addresscode失败");
		}
		//更新单元的exparam
		//查询单元下有多少层
		Location ll = new Location();
		ll.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode());
		ll.setFid(father.getId());
		List<Location> floorcount = locationDao.findByParam(ll);
		Location lls = new Location();
		if (CollectionUtils.isEmpty(floorcount)){
			lls.setExtendParam("1");
		}else {
			lls.setExtendParam(String.valueOf(floorcount.size()));
		}
		lls.setId(father.getId());
		int floorupdate = locationDao.update(lls);
		if (floorupdate<=0){
			throwBusinessException("单元extendparam更新失败");
		}


		//开始插入房屋
		List<Location> roomList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(byParams)){
			int start = byParams.size();
			for (int x = 0;x < roomnum;x++){
				Location room = new Location();
				room.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
				room.setFid(floor.getId());
				room.setCommunityId(locationModelVO.getCommunityId());
				room.setCreateTime(new Date());
				room.setCreateUserId(userId);
//				room.setAddressName(getRoomNumber((start+1),(x+1)));
				room.setAddressName(getRoomNumber(Integer.valueOf(locationModelVO.getAddressName()),(x+1)));
				room.setAddressCode(floor.getAddressCode());
				room.setFullName(floorName +room.getAddressName()+"/");
				room.setSquare(Const.DEFAULT_SQUARE);
				room.setRent(Const.IS_RENT_NO);
				roomList.add(room);
			}
		}else {
			for (int x = 0;x < roomnum;x++){
				Location room = new Location();
				room.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
				room.setFid(floor.getId());
				room.setCommunityId(locationModelVO.getCommunityId());
				room.setCreateTime(new Date());
				room.setCreateUserId(userId);
				room.setAddressName(getRoomNumber(1,x+1));
				room.setAddressCode(floor.getAddressCode());
				room.setFullName(floorName +room.getAddressName()+"/");
				room.setSquare(Const.DEFAULT_SQUARE);
				room.setRent(Const.IS_RENT_NO);
				roomList.add(room);
			}
		}
		int room = locationDao.batchAddLocation(roomList);
		if (room<=0){
			throwBusinessException("批量新增房屋失败");
		}
		//遍历楼层 设置code
		List<Location> roomUpdateList = new ArrayList<>();
		for (Location location2 : roomList) {
			Location roo = new Location();
			roo.setId(location2.getId());
			roo.setAddressCode(floor.getAddressCode()+floor.getId()+"/"+location2.getId()+"/");
			roomUpdateList.add(roo);
		}

		int rooms = locationDao.batchUpdateAddressCode(roomUpdateList);
		if (rooms<=0){
			throwBusinessException("批量更新房屋addresscode失败");
		}

		return successVo();
	}
	/**
	 * 房屋新增
	 * @param locationModelVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo addRoom(LocationVO locationModelVO) {
		Long userId = getCurrentUserInfo().getId();
		Location location = new Location();
		BeanUtils.copyProperties(locationModelVO,location);



		//查询楼层信息
		Location father = locationDao.getLocationById(locationModelVO.getFid());
		if (father==null){
			throwBusinessException("父机构异常");
		}
		String addressName = getRoomNumber(Integer.valueOf(father.getAddressName()),Integer.valueOf(locationModelVO.getAddressName()));
		Location temp = new Location();
		temp.setFid(locationModelVO.getFid());
		temp.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
		temp.setAddressName(addressName);

		List<Location> byParam = locationDao.findByParam(temp);
		if (CollectionUtils.isNotEmpty(byParam)){
			throwBusinessException("房间名称重复");
		}

		String roomName = father.getFullName()  + addressName + "/";
		location.setAddressName(addressName);
		location.setFullName(roomName);
		location.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
		location.setCreateTime(new Date());
		location.setCreateUserId(userId);
		location.setAddressCode(father.getAddressCode());
		location.setSquare(StringUtil.isEmpty(locationModelVO.getSquare())? Const.DEFAULT_SQUARE : locationModelVO.getSquare());
		location.setRent(locationModelVO.getRent() == null ? Const.IS_RENT_NO : locationModelVO.getRent());
		int add = locationDao.add(location);
		if (add<=0){
			throwBusinessException("添加房屋失败");
		}
		//更新房屋code
		Location temp1 = new Location();
		temp1.setId(location.getId());
		temp1.setAddressCode(father.getAddressCode()+location.getId()+"/");
		int updateroom = locationDao.update(temp1);
		if (updateroom<=0){
			throwBusinessException("房屋code更新失败");
		}


		//更新楼层信息
		Location obj = new Location();

		Location ll = new Location();
		ll.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
		ll.setFid(father.getId());
		List<Location> byParam1 = locationDao.findByParam(ll);
		if (CollectionUtils.isNotEmpty(byParam1)){
			obj.setExtendParam(String.valueOf(byParam1.size()));
		}else {
			obj.setExtendParam("1");
		}

		obj.setId(father.getId());
		int update = locationDao.update(obj);
		if (update<=0){
			throwBusinessException("楼层信息更新失败");
		}
		return successVo();
	}
	/**
	 * 楼栋 房屋 更新
	 * @param locationModelVO
	 * @return
	 */
	@Override
	public BaseVo updateBuildingOrUnit(LocationVO locationModelVO) {
		//查询当前更新的数据是不是房屋
		Location locationById = locationDao.getLocationById(locationModelVO.getId());
		if (locationById==null){
			throwBusinessException("要修改的信息不存在");
		}
		//校验单元名称
		Location temp = new Location();
		temp.setAddressName(locationModelVO.getAddressName());
		temp.setCommunityId(locationModelVO.getCommunityId());
		temp.setAddressType(locationModelVO.getAddressType());
		temp.setFid(locationModelVO.getFid());
		List<Location> byParam = locationDao.findByParam(temp);
		if (CollectionUtils.isNotEmpty(byParam)){
			if (locationModelVO.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode())){
				throwBusinessException("楼栋名字已存在");
			}
			if (locationModelVO.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_UNIT.getCode())){
				throwBusinessException("单元名字已存在");
			}
			if (locationModelVO.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode())){
				throwBusinessException("房间名字已存在");
			}
		}
		Location location = new Location();
		location.setId(locationModelVO.getId());
		//如果是房屋要加楼层号
		if (locationById.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode())){
			Location father = locationDao.getLocationById(locationById.getFid());
			if (father!=null){
				String addressname = getRoomNumber(Integer.valueOf(father.getAddressName()),Integer.valueOf(locationModelVO.getAddressName()));
//				String addressname = father.getAddressName()+ locationModelVO.getAddressName();
				if (!addressname.equals(locationById.getAddressName())){
					Location tt = new Location();
					tt.setFid(locationModelVO.getFid());
					tt.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
					tt.setAddressName(addressname);

					List<Location> byParams = locationDao.findByParam(tt);
					if (CollectionUtils.isNotEmpty(byParams)){
						throwBusinessException("房间名称重复");
					}
//					location.setAddressName(father.getAddressName()+ locationModelVO.getAddressName());
					location.setAddressName(addressname);
				}
				location.setSquare(locationModelVO.getSquare());
				location.setRent(locationModelVO.getRent());
//				location.setFullName(father.getFullName()+"/"+locationModelVO.getAddressName());
				location.setFullName(father.getFullName()+addressname+"/");
			}
		}else {
			Location tts = new Location();
			if (locationById.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode())){
				tts.setCommunityId(locationById.getCommunityId());
			}else {
				tts.setFid(locationModelVO.getFid());
			}
			tts.setAddressType(locationModelVO.getAddressType());
			tts.setAddressName(locationModelVO.getAddressName());

			List<Location> byParams = locationDao.findByParam(tts);
			if (CollectionUtils.isNotEmpty(byParams)){
				throwBusinessException("名称重复");
			}
			location.setAddressName(locationModelVO.getAddressName());
			if (locationModelVO.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode())){
				//如果是楼栋
				Community communityById = communityDao.getCommunityById(locationModelVO.getCommunityId());
				if (communityById!=null){
					location.setFullName(communityById.getCommunityName()+"/"+locationModelVO.getAddressName());
				}
			}else {
				Location father = locationDao.getLocationById(locationById.getFid());
				if (father!=null){
					location.setFullName(father.getFullName()+locationModelVO.getAddressName());
				}
			}
		}
		int update = locationDao.update(location);
		if (update<=0){
			throwBusinessException("名称更新失败");
		}
		//批量更新的集合
		List<Location> updateList = new ArrayList<>();
		//如果不是房屋 批量更新下级的所有名称
		if (!locationById.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode()) &&
				!locationById.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode())){
			//查询它下面所有的记录
			List<Location> recordsByLikeAddressCode = locationDao.getRecordsByLikeAddressCode(locationById.getAddressCode());
			//如果要更新的记录是楼栋
			if (locationById.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode())){
				if (CollectionUtils.isNotEmpty(recordsByLikeAddressCode)){
					for (Location location1 : recordsByLikeAddressCode) {
						//split字符串做替换
						Location t1 = new Location();
						t1.setId(location1.getId());
						t1.setFullName(commonUtil.resetFullName(location1.getFullName(),locationModelVO.getAddressName(),1));
						updateList.add(t1);
					}
				}
			}else if (locationById.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_UNIT.getCode())){
				//如果要更新的记录是单元
				if (CollectionUtils.isNotEmpty(recordsByLikeAddressCode)){
					for (Location location1 : recordsByLikeAddressCode) {
						//split字符串做替换
						Location t1 = new Location();
						t1.setId(location1.getId());
						t1.setFullName(commonUtil.resetFullName(location1.getFullName(),locationModelVO.getAddressName(),2));
						updateList.add(t1);
					}
				}
			}
			//开始批量更新
			if (CollectionUtils.isNotEmpty(updateList)){
				int i = locationDao.batchUpdateFullName(updateList);
				if (i<=0){
					throwBusinessException("批量更新房屋信息名称失败");
				}
			}
		}

		return successVo();
	}
	/**
	 * 删除
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo delById(Long id) {
		Location obj = locationDao.getLocationById(id);
		if (obj==null){
			throwBusinessException("数据不存在");
		}
		List<Location> roomsByLikeAddressCode = locationDao.getRecordsByLikeAddressCode(obj.getAddressCode());
		if (CollectionUtils.isNotEmpty(roomsByLikeAddressCode)){
			List<Long> roomIds = new ArrayList<>();
			for (Location location : roomsByLikeAddressCode) {
				roomIds.add(location.getId());
			}
			//查询申请记录 如果有申请记录不能删除
			LocationApplyVO locationApplyVO = new LocationApplyVO();
			locationApplyVO.setAddressId(id);
			List<LocationApply> byParam = locationApplyDao.findByParam(locationApplyVO);
			//查询房屋与居民的关系 如果有记录不能删除
			List<ResidentRelLocation> residentRelLocations = residentRelLocationDao.batchSelectRecordByAddressId(roomIds);
			if (CollectionUtils.isNotEmpty(residentRelLocations)){
				if (obj.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode())){
					throwBusinessException("此房屋存在关联住户，不允许删除");
				}
				if (obj.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode())){
					throwBusinessException("此楼层存在关联住户，不允许删除");
				}
				if (obj.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_UNIT.getCode())){
					throwBusinessException("此单元存在关联住户，不允许删除");
				}
				if (obj.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode())){
					throwBusinessException("此楼栋存在关联住户，不允许删除");
				}
			}else if (CollectionUtils.isNotEmpty(byParam)){
				throwBusinessException("此房屋存在关联住户，不允许删除");
			}else {
				int i = locationDao.delByAddressCode(obj.getAddressCode());
				if (i<=0){
					throwBusinessException("删除失败");
				}
//				//对id的父机构进行刷新
//				if (obj.getFid()!=null){
//					Location father = locationDao.getLocationById(obj.getFid());
//					Location param = new Location();
//					param.setAddressType(father.getAddressType());
//					param.setCommunityId(father.getCommunityId());
//					param.setFid(father.getId());
//					List<Location> byParam = locationDao.findByParam(param);
//					if (CollectionUtils.isNotEmpty(byParam)){
//						Location update = new Location();
//						update.setId(father.getId());
//						update.setExtendParam(String.valueOf(byParam.size()));
//						int update1 = locationDao.update(update);
//						if (update1<=0){
//							throwBusinessException("父机构刷新失败");
//						}
//					}
//				}
			}
		}
		return successVo();
	}
	/**
	 * 复制单元
	 * @param locationModelVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo copyUnit(LocationVO locationModelVO) {
		Long userId = getCurrentUserInfo().getId();
		//查询选中单元的楼层数  房间数
		Long locationId = locationModelVO.getId();

		//添加单元
		//查询出父机构 -- 楼栋
		Location father = locationDao.getLocationById(locationModelVO.getFid());

		Location newUnit = new Location();
		newUnit.setFid(locationModelVO.getFid());
		newUnit.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_UNIT.getCode());
		newUnit.setAddressName(locationModelVO.getAddressName()+"-副本");
		newUnit.setCommunityId(locationModelVO.getCommunityId());
		newUnit.setCreateUserId(userId);
		newUnit.setCreateTime(new Date());
		newUnit.setFullName(father.getFullName()+ locationModelVO.getAddressName()+"-副本" +"/");
		int add = locationDao.add(newUnit);
		if (add<=0){
			throwBusinessException("新增单元失败");
		}
		String unitcode = father.getAddressCode()+newUnit.getId()+"/";
		Location upd = new Location();
		upd.setId(newUnit.getId());
		upd.setAddressCode(unitcode);
		int update = locationDao.update(upd);
		if (update<=0){
			throwBusinessException("更新单元失败");
		}
		//添加新的楼层
		//查询出选中的单元的楼层
		List<Location> newfloors = new ArrayList<>();

		LocationVO lm = new LocationVO();
		lm.setFid(locationId);
		lm.setCommunityId(locationModelVO.getCommunityId());
		lm.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode());
		//旧的楼层
		List<Location> locationByFId = locationDao.getLocationByFId(lm);
		for (Location location : locationByFId) {
			Location obj = new Location();
			BeanUtils.copyProperties(location,obj);
			obj.setId(null);
			obj.setAddressName(location.getAddressName());
			obj.setFullName(location.getFullName());
			obj.setCreateTime(new Date());
			obj.setCreateUserId(userId);
			obj.setFid(newUnit.getId());
			newfloors.add(obj);
		}
		int i = locationDao.batchAddLocation(newfloors);
		if (i<=0){
			throwBusinessException("批量新增楼层失败");
		}
		//更新楼层
		List<Location> floorUpdateList = new ArrayList<>();
		for (Location newfloor : newfloors) {
			Location fupd = new Location();
			fupd.setId(newfloor.getId());
			fupd.setAddressCode(unitcode+newfloor.getId()+"/");
			floorUpdateList.add(fupd);
		}
		int l = locationDao.batchUpdateAddressCode(floorUpdateList);
		if (l<=0){
			throwBusinessException("批量更新楼层失败");
		}


		List<Location> nrooms = new ArrayList<>();
		//查询出选中的单元的房间
		//遍历旧的楼层
		for (int x =0;x<locationByFId.size();x++){
			//查询每层的房间
			LocationVO lms = new LocationVO();
			lms.setFid(locationByFId.get(x).getId());
			lms.setAddressType(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode());
			lms.setCommunityId(locationByFId.get(x).getCommunityId());
			List<Location> locationByFId1 = locationDao.getLocationByFId(lms);
			if (CollectionUtils.isNotEmpty(locationByFId1)){
				for (int y = 0;y<locationByFId1.size();y++) {
					Location room = new Location();
					BeanUtils.copyProperties(locationByFId1.get(y),room);
					room.setId(null);
					room.setAddressName(locationByFId1.get(y).getAddressName());
					room.setFullName(newfloors.get(x).getFullName()+locationByFId1.get(y).getAddressName()+"/");
					room.setFid(newfloors.get(x).getId());
					room.setAddressCode(floorUpdateList.get(x).getAddressCode());
					room.setRent(null);
					nrooms.add(room);
				}
			}
		}
		int rr = locationDao.batchAddLocation(nrooms);
		if (rr<=0){
			throwBusinessException("批量新增房屋失败");
		}
		List<Location> roomUpdateList = new ArrayList<>();
		for (Location nroom : nrooms) {
			Location updroom = new Location();
			updroom.setId(nroom.getId());
			updroom.setAddressCode(nroom.getAddressCode()+nroom.getId()+"/");
			roomUpdateList.add(updroom);
		}
		int uu = locationDao.batchUpdateAddressCode(roomUpdateList);
		if (uu<=0){
			throwBusinessException("批量更新房屋失败");
		}

		return successVo();
	}
	/**
	 * 查询房间信息
	 * @param roomId
	 * @return
	 */
	@Override
	public BaseVo getLocationInfo(Long roomId) {
		Location locationById = locationDao.getLocationById(roomId);
		if (locationById==null){
			throwBusinessException("房间不存在");
		}
		LocationInfoVO locationInfoVO = new LocationInfoVO();
		BeanUtils.copyProperties(locationById,locationInfoVO);
		locationInfoVO.setFullName(commonUtil.ignoreFloorAddressName(locationById.getFullName()));
		//业主集合
		List<Resident> ownerList = new ArrayList<>();
		//家属集合
		List<Resident> relativeList = new ArrayList<>();
		//租户集合
		List<Resident> rentList = new ArrayList<>();

		//查询住户
		ResidentRelLocationVO residentRelLocationModelVO = new ResidentRelLocationVO();
		residentRelLocationModelVO.setAddressId(roomId);
		List<ResidentVO> residentInfoByAddressId = residentRelLocationDao.getResidentInfoByAddressId(residentRelLocationModelVO);
		if (CollectionUtils.isNotEmpty(residentInfoByAddressId)){
			for (ResidentVO residentVO : residentInfoByAddressId) {
				if (residentVO.getIdentityType().equals(ResidentIdentityEnum.RESIDENT_IDENTITY_OWNER.getCode())){
					Resident owner = new Resident();
					BeanUtils.copyProperties(residentVO,owner);
					ownerList.add(owner);
				}
				if (residentVO.getIdentityType().equals(ResidentIdentityEnum.RESIDENT_IDENTITY_FAMILY_MEMBER.getCode())){
					Resident relatives = new Resident();
					BeanUtils.copyProperties(residentVO,relatives);
					relativeList.add(relatives);
				}
				if (residentVO.getIdentityType().equals(ResidentIdentityEnum.RESIDENT_IDENTITY_RENT.getCode())){
					Resident rent = new Resident();
					BeanUtils.copyProperties(residentVO,rent);
					rentList.add(rent);
				}
			}
		}
		//排序
		ownerList = sort(ownerList);
		relativeList = sort(relativeList);
		rentList = sort(rentList);

		locationInfoVO.setOwnerList(ownerList);
		locationInfoVO.setRelativeList(relativeList);
		locationInfoVO.setRentList(rentList);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(locationInfoVO);
		return baseVo;
	}

	/**
	 * 对集合排序
	 * @param list
	 * @return
	 */
	private List<Resident> sort(List<Resident> list){
		Collections.sort(list, new Comparator<Resident>() {
			@Override
			public int compare(Resident o1, Resident o2) {
				try {
					Date t1 = DateUtil.string2Date(o1.getBirthday(),DateUtil.DatePattern.YYYYMMDD.getValue());
					Date t2 = DateUtil.string2Date(o2.getBirthday(),DateUtil.DatePattern.YYYYMMDD.getValue());
					// 这是由大向小排序   如果要由小向大转换比较符号就可以
					if (t1.getTime() < t2.getTime()) {
						return 1;
					} else if (t1.getTime() > t2.getTime()) {
						return -1;
					} else {
						return 0;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return 0;
			}
		});
		return list;
	}
	/**
	 * app小区树形结构
	 * @param communityId
	 * @return
	 */
	@Override
	public BaseVo appLocationTree(Long communityId) {
		Community communityById = communityDao.getCommunityById(communityId);
		if (communityById==null){
			throwBusinessException("小区不存在");
		}
		List<LocationTreeVO> locationVOList = new ArrayList<>();
		//查询这个小区的记录
		Location buildingparam = new Location();
		buildingparam.setCommunityId(communityId);
		List<Location> buildings = locationDao.findByParam(buildingparam);
		if (CollectionUtils.isNotEmpty(buildings)){
			for (Location building : buildings) {
				LocationTreeVO locationTreeVO = new LocationTreeVO();
				BeanUtils.copyProperties(building,locationTreeVO);
				locationVOList.add(locationTreeVO);
			}

		}
		List<LocationTreeVO> rootList = new ArrayList<>();
		for (LocationTreeVO locationVO : locationVOList) {
			if (locationVO.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode())){
				rootList.add(locationVO);
			}
		}
		for (LocationTreeVO locationVO : rootList) {
			locationVO.setChildLocations(getTree(locationVOList,locationVO.getId()));
		}
		BaseVo baseVo = new BaseVo();
		baseVo.setData(rootList);
		return baseVo;
	}
	/**
	 * 查询小区下所有记录
	 * @param communityId
	 * @return
	 */
	@Override
	public BaseVo appGetAll(Long communityId) {
		List<Location> all = locationDao.getAll(communityId);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(all);
		return baseVo;
	}
	/**
	 * 查询单元下有多少个房屋
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo countRoomWithinUnit(Long id) {
		Location locationById = locationDao.getLocationById(id);
		if (locationById==null){
			throwBusinessException("该单元不存在");
		}
		String addressCode = locationById.getAddressCode();
		if (StringUtil.isEmpty(addressCode)){
			throwBusinessException("数据异常地址编码为空");
		}
		int i = locationDao.countRoomLikeAddressCodeAndId(addressCode, id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(i);
		return baseVo;
	}

	/**
	 * 递归生成树
	 * @param buildingVOs
	 * @param id
	 * @return
	 */
	private List<LocationTreeVO> getTree(List<LocationTreeVO> buildingVOs,Long id){
		List<LocationTreeVO> obj = new ArrayList<>();
		for (LocationTreeVO buildingVO : buildingVOs) {
			if (buildingVO.getFid().equals(id)){
				obj.add(buildingVO);
			}
		}
		for (LocationTreeVO locationVO : obj) {
			locationVO.setChildLocations(getTree(buildingVOs,locationVO.getId()));
		}
		if (obj.size() == 0) {
			return null;
		}
		return obj;
	}

	/**
	 * 生成房间号
	 * @param floor
	 * @param roomnum
	 * @return
	 */
	private String getRoomNumber(Integer floor,Integer roomnum){
		String s = floor.toString()+String.format("%02d",roomnum);
		return s;
	}

}
