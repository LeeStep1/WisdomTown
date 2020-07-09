package com.bit.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.OaOrganization;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.enumerate.LocationAddressTypeEnum;
import com.bit.module.cbo.bean.Community;
import com.bit.module.cbo.bean.Location;
import com.bit.module.cbo.bean.PmcStaffRelCommunity;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.LocationDao;
import com.bit.module.cbo.dao.PmcStaffRelCommunityDao;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/18 13:39
 **/
@Component
public class CommonUtil extends BaseService{
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private PmcStaffRelCommunityDao pmcStaffRelCommunityDao;
	@Autowired
	private FileServiceFeign fileServiceFeign;

	public String getAddressStructure(String addressCode,String communityName){
		StringBuffer structure = new StringBuffer();
		String building = "";
		String unit = "";
		String floor = "";
		String room = "";
		if (StringUtil.isEmpty(addressCode)){
			throwBusinessException("地址code为空");
		}
		List<Long> addressIds = Arrays.asList(addressCode.split("/")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
		List<Location> locations = locationDao.batchSelectByIds(addressIds);
		if (CollectionUtils.isNotEmpty(locations)){
			for (Location location : locations) {
				int address_type=LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode();
				if (location.getAddressType().equals(address_type)){
					room = location.getFullName();
				}
				structure.append(building).append(unit).append(floor).append(room);
			}
		}
		return structure.toString();

	}


	public ResidentRelLocationVO getAddressNames(String addressCode,Long communityId,ResidentRelLocationVO residentRelLocationVO){
		StringBuffer structure = new StringBuffer();
		String building = "";
		String unit = "";
		String floor = "";
		String room = "";
		if (StringUtil.isEmpty(addressCode)){
			throwBusinessException("地址code为空");
		}
		//查询小区
		Community communityById = communityDao.getCommunityById(communityId);
		if (communityById!=null){
			structure.append(communityById.getCommunityName()).append("/");
		}


		List<Long> addressIds = Arrays.asList(addressCode.split("/")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
		List<Location> locations = locationDao.batchSelectByIds(addressIds);
		if (CollectionUtils.isNotEmpty(locations)){
			for (Location location : locations) {
				if (location.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_BUILDING.getCode())){
					building = location.getAddressName();
					residentRelLocationVO.setBuildingName(building);
					residentRelLocationVO.setBuildingId(location.getId());
				}
				else if (location.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_UNIT.getCode())){
					unit = location.getAddressName();
					residentRelLocationVO.setUnitName(unit);
					residentRelLocationVO.setUnitId(location.getId());
				}
				else if (location.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_FLOOR.getCode())){
					floor = location.getAddressName();
					residentRelLocationVO.setFloorName(floor);
					residentRelLocationVO.setFloorId(location.getId());
				}
				else if (location.getAddressType().equals(LocationAddressTypeEnum.LOCATION_ADDRESS_TYPE_ROOM.getCode())){
					room = location.getAddressName();
					residentRelLocationVO.setRoomName(room);
					residentRelLocationVO.setRoomId(location.getId());
				}
			}
		}
		return residentRelLocationVO;
	}

	/**
	 * 处理居民扩展信息
	 * @param extendType
	 * @return
	 */
	public String getExtendTypes(String extendType, List<Dict> dicts){
		StringBuffer sb = new StringBuffer();
		if (StringUtil.isNotEmpty(extendType)){
			String[] ss = extendType.split(",");
			for (String s : ss) {
				for (Dict dict : dicts) {
					if (s.equals(dict.getDictCode())){
						sb.append(dict.getDictName());
						sb.append(",");
						break;
					}
				}
			}
			String result = sb.toString().substring(0,sb.toString().length()-1);
			return result;
		}else {
			return "";
		}

	}

	/**
	 * 设置社区名称
	 * @param orgId
	 * @param oaOrganizations
	 * @return
	 */
	public String getOrgName(Long orgId, List<OaOrganization> oaOrganizations){
		if (CollectionUtils.isNotEmpty(oaOrganizations)){
			for (OaOrganization oaOrganization : oaOrganizations) {
				if (oaOrganization.getId().equals(orgId)){
					return oaOrganization.getName();
				}
			}
		}
		return "";
	}

	/**
	 * 获得所有的社区
	 * @return
	 */
	public List<OaOrganization> getOaOrganizationList(){
		List<OaOrganization> oaOrganizationList = new ArrayList<>();
		BaseVo community = sysServiceFeign.getCommunity();
		if (community.getData()!=null){
			String ss = JSON.toJSONString(community.getData());
			oaOrganizationList = JSON.parseArray(ss, OaOrganization.class);
		}
		return oaOrganizationList;
	}

	/**
	 * 截取房间全称最后一个“ ”后面的部分
	 * @param fullName
	 * @return
	 */
	public String resetFullName(String fullName,String replaceName,int index){
		String ss[] = fullName.split("/");
		ss[index] = replaceName;
		StringBuilder result = new StringBuilder(50);
		for (int i=0;i<ss.length;i++){
			if (StringUtil.isNotEmpty(ss[i])){
				result.append(ss[i]);
				result.append("/");
			}
		}
		result.substring(0,result.length()-1);
		return result.toString();
	}

	/**
	 * 根据社区id查询物业人员的id
	 * @param communityId
	 * @return
	 */
	public Long[] queryPmcStaffByCommunityId(Long communityId){
		PmcStaffRelCommunity rel = new PmcStaffRelCommunity();
		rel.setCommunityId(communityId);
		List<PmcStaffRelCommunity> byParam = pmcStaffRelCommunityDao.findByParam(rel);
		List<Long> pmcIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(byParam)){
			for (PmcStaffRelCommunity pmcStaffRelCommunity : byParam) {
				pmcIds.add(pmcStaffRelCommunity.getStaffId());
			}
		}
		Long[] targetId = new Long[byParam.size()];
		pmcIds.toArray(targetId);
		return targetId;
	}

	/**
	 * 根据社区id查询社区管理员id
	 * @param orgId
	 * @return
	 */
	public Long[] queryOrgAdminByOrgId(Long orgId){
		List<User> userByCboDep = sysServiceFeign.getUserByCboDep(orgId);
		List<Long> adminIds = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(userByCboDep)){
			for (User user : userByCboDep) {
				adminIds.add(user.getId());
			}
		}
		Long[] targetId = new Long[adminIds.size()];
		adminIds.toArray(targetId);
		return targetId;
	}

	/**
	 * 处理房间信息 去掉/和楼层
	 * @param addressStructure
	 * @return
	 */
	public String ignoreFloorAddressName(String addressStructure){
		String[] temp = addressStructure.split("/");
		String addressName = temp[0] + temp[1] + temp[2] + temp[4];
		return addressName;
	}

	/**
	 * 根据dictCode和module 获取字典表相应字段名称
	 * @author liyang
	 * @date 2019-08-13
	 * @param dictList : 字典表集合
	 * @param dictCode : 模块值
	 * @return : String
	 */
	public String getDictNameByDictCodeAndModule(List<Dict> dictList,String dictCode){
		Map<String,Dict> dictMap = dictList.stream().collect(Collectors.toMap(Dict::getDictCode, dict -> dict));
		String dictName = dictMap.get(dictCode).getDictName();

		return dictName;
	}

	/**
	 * 查询附件信息
	 * @param fileId
	 * @return
	 */
	public List<FileInfo> getFileInfos(String fileId){
		if (StringUtil.isNotEmpty(fileId)){
			//查询附件
			List<Long> fileIds = Arrays.asList(fileId.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(fileIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
				String s = JSON.toJSONString(byIds.getData());
				List<FileInfo> fileInfos = JSONArray.parseArray(s,FileInfo.class);
				return fileInfos;
			}
		}
		return null;
	}
}
