package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.OaOrganization;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.dao.*;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.vo.FileInfo;
import com.bit.module.cbo.vo.FileInfoVO;
import com.bit.module.cbo.service.CommunityService;
import com.bit.module.cbo.vo.AppCommunityVO;
import com.bit.module.cbo.vo.CommunityVO;
import com.bit.utils.StringUtil;
import com.bit.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 19:38
 **/
@Service("communityService")
public class CommunityServiceImpl extends BaseService implements CommunityService {

	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private PmcStaffRelCommunityDao pmcStaffRelCommunityDao;
	@Autowired
	private LocationDao locationDao;
	@Autowired
	private PmcStaffDao pmcStaffDao;
	@Autowired
	private TokenUtil tokenUtil;
	@Autowired
	private ResidentRelLocationDao residentRelLocationDao;

	/**
	 * 文件管理服务
	 */
	@Autowired
	private FileServiceFeign fileServiceFeign;


	/**
	 * 批量根据社区id查询小区
	 * @param orgIds
	 * @return
	 */
	@Override
	public List<Community> batchSelectByOrgIds(List<Long> orgIds) {
		if (CollectionUtils.isNotEmpty(orgIds)){
			return communityDao.batchSelectByOrgIds(orgIds);
		}
		return null;
	}
	/**
	 * app端展示所有的小区 不需要分页
	 * @return
	 */
	@Override
	public BaseVo appListPage() {
		UserInfo userInfo = getCurrentUserInfo();
		Long residentId = userInfo.getId();
		List<Long> orgIds = new ArrayList<>();
		List<CommunityVO> communityModelVOS = communityDao.appListPage(residentId);
		if (CollectionUtils.isNotEmpty(communityModelVOS)){
			for (CommunityVO communityModelVO : communityModelVOS) {
				orgIds.add(communityModelVO.getOrgId());
				if (communityModelVO.getAddressId()==null){
					communityModelVO.setStatus(Const.APP_COMMUNITY_VERIFY_STATUS_NO);
				}else {
					communityModelVO.setStatus(Const.APP_COMMUNITY_VERIFY_STATUS_YES);
				}
			}
			if (CollectionUtils.isNotEmpty(orgIds)){
				//调用feign 批量查询社区名称
				List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(orgIds);
				if (CollectionUtils.isNotEmpty(oaOrganizationList)){
					for (OaOrganization oaOrganization : oaOrganizationList) {
						for (CommunityVO communityModelVO : communityModelVOS){
							if (communityModelVO.getOrgId().equals(oaOrganization.getId())){
								communityModelVO.setOrgName(oaOrganization.getName());
							}
						}
					}
				}
			}
		}
		//结果集去重
		List<CommunityVO> collect = removeListDuplicateObject(communityModelVOS);
//		List<CommunityModelVO> collect = communityModelVOS.stream().filter(distinctByKey(CommunityModelVO::getId))
//				.collect(Collectors.toList());

		List<AppCommunityVO> appCommunityVOList = new ArrayList<>();
		for (CommunityVO communityModelVO : collect){
			AppCommunityVO appCommunityVO = new AppCommunityVO();
			BeanUtils.copyProperties(communityModelVO,appCommunityVO);
			appCommunityVOList.add(appCommunityVO);
		}
		BaseVo baseVo = new BaseVo();
		baseVo.setData(appCommunityVOList);
		return baseVo;
	}

	/**
	 * list实体集合去重 根据社区id
	 * @param list
	 * @return
	 */
	private static List<CommunityVO> removeListDuplicateObject(List<CommunityVO> list) {
		for (int i = 0; i < list.size(); i++) {
			for (int j=list.size()-1;j>i;j--){
				if(list.get(i).getId().equals(list.get(j).getId())) {
					list.remove(j); //去除重复的元素
				}
			}
		}
		return list;
	}

	/**
	 * 去重使用 函数式接口 T -> bollean
	 * @param keyExtractor
	 * @param <T>
	 * @return
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
		ConcurrentHashMap<Object, Boolean> map = new ConcurrentHashMap<>(16);
		return t -> map.putIfAbsent(keyExtractor.apply(t),Boolean.TRUE) == null;
	}

	/**
	 * 新增小区
	 * @author liyang
	 * @date 2019-07-18
	 * @param community : 新增小区详情
	 * @return : BaseVo
	 */
	@Override
	public BaseVo add(Community community) {

		UserInfo userInfo = getCurrentUserInfo();

		//获取当前时间
		Date now = new Date();

		//数据的创建时间
		community.setCreateTime(now);

		//数据的创建者ID
		community.setCreateUserId(userInfo.getId());

		//数据的更新时间
		community.setUpdateTime(now);

		//数据的更新者ID
		community.setUpdateUserId(userInfo.getId());

		//小区名称校验
		Integer count = communityDao.countByNameSql(community.getCommunityName());
		if(count.equals(Const.COUNT)){
			communityDao.add(community);
		}else {
			throwBusinessException("小区名称重复，请重新输入！");
		}

		return successVo();
	}

	/**
	 * 修改小区
	 * @author liyang
	 * @date 2019-07-19
	 * @param community : 修改的详情
	 * @return : BaseVo
	 */
	@Override
	@Transactional
	public BaseVo modify(Community community) {
		Long communityId = community.getId();
		Community communityById = communityDao.getCommunityById(communityId);
		if (communityById==null){
			throwBusinessException("该小区不存在");
		}

		UserInfo userInfo = getCurrentUserInfo();

		//获取修改人ID
		community.setUpdateUserId(userInfo.getId());

		//获取修改时间
		community.setUpdateTime(new Date());

		//名称去重
		Integer count = communityDao.modifyCountByNameSql(community.getCommunityName(),community.getId());
		if(count.equals(Const.COUNT)){
			communityDao.modify(community);
		}else {
			throwBusinessException("小区名称重复，请重新输入！");
		}
		//20190917 添加物业人员信息关联处理
		if (!communityById.getPmcCompanyId().equals(community.getPmcCompanyId())){
			//如果数据库的物业公司id和传进来的参数不同 进行物业人员关联的处理
			PmcStaffRelCommunity temp = new PmcStaffRelCommunity();
			temp.setCommunityId(communityId);
			List<PmcStaffRelCommunity> pmcStaffRelCommunityList = pmcStaffRelCommunityDao.findByParam(temp);
			List<Long> staffIds = new ArrayList<>();
			List<Long> ids = new ArrayList<>();
			if (CollectionUtils.isNotEmpty(pmcStaffRelCommunityList)){
				for (PmcStaffRelCommunity pmcStaffRelCommunity : pmcStaffRelCommunityList) {
					staffIds.add(pmcStaffRelCommunity.getStaffId());
					ids.add(pmcStaffRelCommunity.getId());
				}
				//批量删除物业人员关联关系
				pmcStaffRelCommunityDao.delByIds(ids);
				//批量查询物业人员的信息
				List<PmcStaff> pmcStaffs = pmcStaffDao.batchSelectByIds(staffIds);
				//删除token
				if (CollectionUtils.isNotEmpty(pmcStaffs)){
					for (PmcStaff pmcStaff : pmcStaffs) {
						if (StringUtil.isNotEmpty(pmcStaff.getToken())){
							tokenUtil.delToken(pmcStaff.getToken());
						}
					}
				}
			}
			//将新的物业公司的关系重新建立
//			Long companyId = community.getPmcCompanyId();
//			if (companyId!=null){
//				PmcStaff ps = new PmcStaff();
//				ps.setCompanyId(companyId);
//				List<PmcStaff> byParam = pmcStaffDao.findByParam(ps);
//				List<Long> stffs = new ArrayList<>();
//				List<PmcStaffRelCommunity> relCommunityList = new ArrayList<>();
//				if (CollectionUtils.isNotEmpty(byParam)){
//					for (PmcStaff pmcStaff : byParam) {
//						PmcStaffRelCommunity relCommunity = new PmcStaffRelCommunity();
//						relCommunity.setStaffId(pmcStaff.getId());
//						relCommunity.setCommunityId(communityId);
//						relCommunity.setOrgId(communityById.getOrgId());
//						relCommunityList.add(relCommunity);
//						stffs.add(pmcStaff.getId());
//					}
//					pmcStaffRelCommunityDao.add(relCommunityList);
//					//批量查询物业人员的信息
//					List<PmcStaff> pmcStaffs = pmcStaffDao.batchSelectByIds(stffs);
//					//删除token
//					if (CollectionUtils.isNotEmpty(pmcStaffs)){
//						for (PmcStaff pmcStaff : pmcStaffs) {
//							if (StringUtil.isNotEmpty(pmcStaff.getToken())){
//								tokenUntil.delToken(pmcStaff.getToken());
//							}
//						}
//					}
//				}
//			}
		}


		//20190917 添加更新房间信息表的小区名称
		String oldCommunityName = communityById.getCommunityName();
		String newCommunityName = community.getCommunityName();
		Location obj = new Location();
		obj.setCommunityId(communityId);
		List<Location> byParam = locationDao.findByParam(obj);
		List<Location> updateList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(byParam)){
			for (Location location : byParam) {
				Location lo = new Location();
				lo.setId(location.getId());
				lo.setFullName(location.getFullName().replace(oldCommunityName,newCommunityName));
				updateList.add(lo);
			}
			int i = locationDao.updateFullName(updateList);
			if (i<=0){
				throwBusinessException("批量更新小区名称失败");
			}
		}

		return successVo();
	}

	/**
	 * 查询小区列表（分页）
	 * @author liyang
	 * @date 2019-07-19
	 * @param communityVO : 查询条件
	 * @return : BaseVo
	 */
	@Override
	public BaseVo findAll(CommunityVO communityVO) {

		//分页查询
		PageHelper.startPage(communityVO.getPageNum(), communityVO.getPageSize());
		List<Community> communityList = communityDao.findAllSql(communityVO);

		if(communityList.size() != Const.COUNT){
			//查询所有社区
			List<Long> orgIds = new ArrayList<>();
			communityList.forEach(community->orgIds.add(community.getOrgId()));
			List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(orgIds);

			//转成map
			Map<Long,OaOrganization> oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId, OaOrganization->OaOrganization));

			//拼装数据
			for(Community community : communityList){
				community.setOrgName(oaOrganizationMap.get(community.getOrgId()).getName());
			}
		}

		PageInfo<Community> pageInfo = new PageInfo<Community>(communityList);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);

		return baseVo;
	}

	/**
	 * 根据ID 删除小区
	 * @author liyang
	 * @date 2019-07-19
	 * @param id : 小区ID
	 * @return : BaseVo
	 */
	@Override
	public BaseVo delete(Long id) {

		//检测小区是否可以删除(生成楼栋  单元  不能删除)
		Integer count = communityDao.locationCountByCommunityIdSql(id);
		if(count.equals(Const.COUNT)){

			//生成小区物业关联关系不能删除
			Integer staffcount = pmcStaffRelCommunityDao.staffByCommunityCount(id);
			if(staffcount.equals(Const.COUNT)){
				//20191015 添加如果有小区居民不能删除
				ResidentRelLocation rel = new ResidentRelLocation();
				rel.setCommunityId(id);
				List<ResidentRelLocation> byParam = residentRelLocationDao.findByParam(rel);
				if (CollectionUtils.isNotEmpty(byParam)){
					throwBusinessException("此小区存在居民关联信息，不允许删除！");
				}
				//可以删除
				communityDao.delete(id);
			}else {
				throwBusinessException("此小区存在物业人员关联信息，不允许删除！");
			}
		}else {
			throwBusinessException("此小区信息已生成房屋信息，不允许删除！");
		}

		return successVo();
	}

	/**
	 * 根据ID查询明细
	 * @author liyang
	 * @date 2019-07-22
	 * @param id : 查询的明细
	 * @return : BaseVo
	 */
    @Override
    public BaseVo findById(Long id) {
    	//根据ID 查询明细
		Community community = communityDao.getCommunityDetailById(id);

		List<Long> orgId = new ArrayList<>();
		orgId.add(community.getOrgId());
		List<OaOrganization> oaOrganizations = sysServiceFeign.batchSelectOaDepartmentByIds(orgId);
		if(oaOrganizations.size()>0){
			community.setOrgName(oaOrganizations.get(0).getName());
		}

		if(community.getAttachmentIds()!= null && !community.getAttachmentIds().trim().equals("")){
			//查询文件
			FileInfoVO fileInfoVO = new FileInfoVO();
			List<Long> ids = new ArrayList<>();
			String[] idsString = community.getAttachmentIds().split(",");
			for(String idString : idsString){
				ids.add(Long.valueOf(idString));
			}
			fileInfoVO.setFileIds(ids);

			BaseVo fileBaseVo = fileServiceFeign.findByIds(fileInfoVO);
			String fileString = JSON.toJSONString(fileBaseVo.getData());
			List<FileInfo> fileInfos = JSON.parseArray(fileString,FileInfo.class);

			community.setFileInfoList(fileInfos);
		}


		BaseVo baseVo = new BaseVo();
		baseVo.setData(community);

        return baseVo;
    }

    /**
     * 获取物业公司关联小区
     * @author liyang
     * @date 2019-07-23
     * @param id : 物业公司ID
     * @return : BaseVo
    */
	@Override
	public BaseVo findCommunityByCompanyId(Long id) {

		List<Community> communityList = communityDao.getCommunityByCompanyId(id);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(communityList);

		return baseVo;
	}
}
