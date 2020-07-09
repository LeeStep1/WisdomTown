package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.OaOrganization;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.PmcStaffRoleTypeEnum;
import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.PhoneBookDao;
import com.bit.module.cbo.dao.PmcStaffRelCommunityDao;
import com.bit.module.cbo.dao.ResidentRelLocationDao;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.service.PhoneBookService;
import com.bit.module.cbo.vo.*;
import com.bit.utils.CommonUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/5 14:35
 **/
@Service("phoneBookService")
public class PhoneBookServiceImpl extends BaseService implements PhoneBookService {

	@Autowired
	private PhoneBookDao phoneBookDao;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private CommonUtil commonUtil;
	@Autowired
	private CommunityDao communityDao;
	@Autowired
	private PmcStaffRelCommunityDao pmcStaffRelCommunityDao;
	@Autowired
	private ResidentRelLocationDao residentRelLocationDao;

	/**
	 * 查询当前用户所在社区的电话
	 * @return
	 */
	@Override
	public BaseVo getOrgTelInfoByToken() {
		//当前社区id
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		PhoneBookVO phoneBookVO = new PhoneBookVO();
		phoneBookVO.setOrgId(orgId);
		phoneBookVO.setType(Const.TYPE_PHONE_BOOK_ORG);
		List<PhoneBook> phoneBooks = phoneBookDao.getOrgTelInfoByToken(phoneBookVO);
//		PhoneBook orgTelInfoByToken = phoneBookDao.getOrgTelInfoByToken(phoneBookVO);
		PhoneBook orgTelInfoByToken = new PhoneBook();
		if (CollectionUtils.isNotEmpty(phoneBooks)){
			orgTelInfoByToken = phoneBooks.get(0);
		}
		PhoneBookOrgVO result = new PhoneBookOrgVO();
		result.setOrgId(orgId);
		if (orgTelInfoByToken!=null){
			result.setTelOne(orgTelInfoByToken.getTelOne());
			result.setTelTwo(orgTelInfoByToken.getTelTwo());
			result.setCommunityId(orgTelInfoByToken.getCommunityId());
			result.setId(orgTelInfoByToken.getId());
			result.setType(orgTelInfoByToken.getType());
		}



		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		if (CollectionUtils.isNotEmpty(oaOrganizationList)){
			for (OaOrganization oaOrganization : oaOrganizationList) {
				if (oaOrganization.getId().equals(result.getOrgId())){
					result.setOrgName(oaOrganization.getName());
					break;
				}
			}
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(result);
		return baseVo;
	}
	/**
	 * web端分页查询
	 * @return
	 */
	@Override
	public BaseVo webListPage(PhoneBookVO phoneBookVO) {
		//判断是不是社区办
		Integer userType = getCurrentUserInfo().getCboInfo().getUserType();
		if (userType.equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			//是社区办
			if (phoneBookVO.getOrgId()==null){
				//如果没选社区
				List<Long> orgIds = new ArrayList<>();
				List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
				if (CollectionUtils.isNotEmpty(oaOrganizationList)){
					for (OaOrganization oaOrganization : oaOrganizationList) {
						orgIds.add(oaOrganization.getId());
				}
					phoneBookVO.setOrgIds(orgIds);
				}
			}
		}else {
			//不是社区办
			Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
			phoneBookVO.setOrgId(orgId);
		}
		PageHelper.startPage(phoneBookVO.getPageNum(),phoneBookVO.getPageSize());
		phoneBookVO.setType(Const.TYPE_PHONE_BOOK_PMC);
		List<PhoneBookCommunityCompanyVO> phoneBookCommunityCompanyVOS = phoneBookDao.webListPage(phoneBookVO);
		PageInfo<PhoneBookCommunityCompanyVO> pageInfo = new PageInfo<>(phoneBookCommunityCompanyVOS);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * web端小区物业电话修改提前校验
	 * @return
	 */
	@Override
	public BaseVo checkCommunityTelExist(PhoneBookVO phoneBookVO) {
		//当前社区id
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		BaseVo baseVo = new BaseVo();
		PhoneBookVO temp = new PhoneBookVO();
		temp.setOrgId(orgId);
		temp.setCommunityId(phoneBookVO.getCommunityId());
		temp.setType(phoneBookVO.getType());
		List<PhoneBook> phoneBooks = phoneBookDao.getOrgTelInfoByToken(temp);
//		PhoneBook orgTelInfoByToken = phoneBookDao.getOrgTelInfoByToken(temp);
		if (CollectionUtils.isNotEmpty(phoneBooks)){
			PhoneBook orgTelInfoByToken = phoneBooks.get(0);
			baseVo.setData(orgTelInfoByToken);
		}
		return baseVo;
	}
	/**
	 * 新增数据
	 * @param phoneBook
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo add(PhoneBook phoneBook) {
		int add = phoneBookDao.add(phoneBook);
		if (add<=0){
			throwBusinessException("添加记录失败");
		}
		return successVo();
	}
	/**
	 * 修改数据
	 * @param phoneBook
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo modify(PhoneBook phoneBook) {
		PhoneBook temp = new PhoneBook();
		temp.setId(phoneBook.getId());
		temp.setTelOne(phoneBook.getTelOne());
		temp.setTelTwo(phoneBook.getTelTwo()==null?"":phoneBook.getTelTwo());
		int modify = phoneBookDao.modify(temp);
		if (modify<=0){
			throwBusinessException("修改记录失败");
		}
		return successVo();
	}
	/**
	 * 居民app通讯录
	 * @return
	 */
	@Override
	public BaseVo appResidentBook() {
		AppOrgPmcTelInfo appOrgPmcTelInfo = new AppOrgPmcTelInfo();

		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		//当前用户所有的社区
		List<OaOrganization> cboOrgs = getCurrentUserInfo().getCboInfo().getCboOrgs();
		List<Long> allorgIds = new ArrayList<>();
		for (OaOrganization cboOrg : cboOrgs) {
			allorgIds.add(cboOrg.getId());
		}
		if (CollectionUtils.isNotEmpty(allorgIds)){
			AppOrgPmcTelInfo obj1 = new AppOrgPmcTelInfo();
			obj1.setType(Const.TYPE_PHONE_BOOK_ORG);
			obj1.setOrgIds(allorgIds);
			//查询社区电话
			List<PhoneBookCommunityCompanyVO> telInfoByOrgIds = phoneBookDao.getTelInfoByOrgIds(obj1);
			//设置社区名称
			for (PhoneBookCommunityCompanyVO telInfoByOrgId : telInfoByOrgIds) {
				telInfoByOrgId.setOrgName(commonUtil.getOrgName(telInfoByOrgId.getOrgId(),oaOrganizationList));
			}
			appOrgPmcTelInfo.setAppResidentOrgTelInfos(telInfoByOrgIds);
		}
		List<Long> communityIds = new ArrayList<>();
		//批量查询小区
		if (CollectionUtils.isNotEmpty(allorgIds)){
			//查询认证过房屋的小区
			Long residentId = getCurrentUserInfo().getId();
			ResidentRelLocationVO temp = new ResidentRelLocationVO();
			temp.setResidentId(residentId);
			temp.setOrgIds(allorgIds);
			List<ResidentRelLocation> byParam = residentRelLocationDao.findByParamBatchOrgIds(temp);
			if (CollectionUtils.isNotEmpty(byParam)){
				for (ResidentRelLocation residentRelLocation : byParam) {
					communityIds.add(residentRelLocation.getCommunityId());
				}
			}
//			List<Community> communities = communityDao.batchSelectByOrgIds(allorgIds);
//			for (Community community1 : communities) {
//				communityIds.add(community1.getId());
//			}
			if (CollectionUtils.isNotEmpty(communityIds)){
				AppOrgPmcTelInfo obj2 = new AppOrgPmcTelInfo();
				obj2.setType(Const.TYPE_PHONE_BOOK_PMC);
				obj2.setCommunityIds(communityIds);
				//查询小区物业电话
				List<PhoneBookCommunityCompanyVO> telInfoByCommunityIds = phoneBookDao.getTelInfoByCommunityIds(obj2);
				appOrgPmcTelInfo.setAppResidentCommunityPmcTelInfos(telInfoByCommunityIds);
			}
		}
		BaseVo baseVo = new BaseVo();
		baseVo.setData(appOrgPmcTelInfo);


		return baseVo;
	}
	/**
	 * 居委会app通讯录 物业
	 * @return
	 */
	@Override
	public BaseVo appOrgBookWithPmc() {
		AppOrgPmcTelInfo appOrgPmcTelInfo = new AppOrgPmcTelInfo();
		//当前社区的id
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		List<Community> communityByOrgId = communityDao.getCommunityByOrgId(orgId);
		if (CollectionUtils.isEmpty(communityByOrgId)){
			throwBusinessException("该社区下没有小区");
		}

		List<Long> communityIds = new ArrayList<>();
		for (Community community : communityByOrgId) {
			communityIds.add(community.getId());
		}

		if (CollectionUtils.isNotEmpty(communityIds)){
			AppOrgPmcTelInfo obj2 = new AppOrgPmcTelInfo();
			obj2.setType(Const.TYPE_PHONE_BOOK_PMC);
			obj2.setCommunityIds(communityIds);
			//查询小区物业电话
			List<PhoneBookCommunityCompanyVO> telInfoByCommunityIds = phoneBookDao.getTelInfoByCommunityIds(obj2);
			appOrgPmcTelInfo.setAppOrgWithPmcTelInfos(telInfoByCommunityIds);
		}
		BaseVo baseVo = new BaseVo();
		baseVo.setData(appOrgPmcTelInfo);
		return baseVo;
	}
	/**
	 * 居委会app通讯录 社区
	 * @return
	 */
	@Override
	public BaseVo appOrgBookWithCommunity() {
		AppOrgPmcTelInfo appOrgPmcTelInfo = new AppOrgPmcTelInfo();
		//当前社区的id
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		Long userId = getCurrentUserInfo().getId();

		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();

		List<Long> allorgIds = new ArrayList<>();
		allorgIds.add(orgId);

		AppOrgPmcTelInfo obj1 = new AppOrgPmcTelInfo();
		obj1.setType(Const.TYPE_PHONE_BOOK_ORG);
		obj1.setOrgIds(allorgIds);
		//查询社区电话
		List<PhoneBookCommunityCompanyVO> telInfoByOrgIds = phoneBookDao.getTelInfoByOrgIds(obj1);
		for (PhoneBookCommunityCompanyVO telInfoByOrgId : telInfoByOrgIds) {
			telInfoByOrgId.setOrgName(commonUtil.getOrgName(telInfoByOrgId.getOrgId(),oaOrganizationList));
		}
		appOrgPmcTelInfo.setAppOrgWithOrgTelInfos(telInfoByOrgIds);

		//调用feign查询管理员
		List<User> userByCboDep = sysServiceFeign.getUserByCboDep(orgId);
		//去除调当期登录账号
		Iterator iter = userByCboDep.iterator();
		while (iter.hasNext()){
			User user = (User) iter.next();
			if (user.getId().equals(userId)){
				iter.remove();
			}
		}
		appOrgPmcTelInfo.setAdminUserList(userByCboDep);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(appOrgPmcTelInfo);
		return baseVo;
	}
	/**
	 * 物业app通讯录
	 * @return
	 */
	@Override
	public BaseVo appPmcBook() {
		//当前用户的id
		Long staffid = getCurrentUserInfo().getId();
		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		AppOrgPmcTelInfo appOrgPmcTelInfo = new AppOrgPmcTelInfo();

		List<Long> orgIds = new ArrayList<>();
		List<Long> communityIds = new ArrayList<>();
		//查询这个人所负责的社区
		PmcStaffRelCommunity temp = new PmcStaffRelCommunity();
		temp.setStaffId(staffid);
		List<PmcStaffRelCommunity> byParam = pmcStaffRelCommunityDao.findByParam(temp);
		if (CollectionUtils.isEmpty(byParam)){
			throwBusinessException("此人没有小区关联");
		}
		for (PmcStaffRelCommunity pmcStaffRelCommunity : byParam) {
			orgIds.add(pmcStaffRelCommunity.getOrgId());
			communityIds.add(pmcStaffRelCommunity.getCommunityId());
		}

		if (CollectionUtils.isNotEmpty(orgIds)){
			AppOrgPmcTelInfo obj1 = new AppOrgPmcTelInfo();
			obj1.setType(Const.TYPE_PHONE_BOOK_ORG);
			obj1.setOrgIds(orgIds);
			//查询社区电话
			List<PhoneBookCommunityCompanyVO> telInfoByOrgIds = phoneBookDao.getTelInfoByOrgIds(obj1);
			for (PhoneBookCommunityCompanyVO telInfoByOrgId : telInfoByOrgIds) {
				telInfoByOrgId.setOrgName(commonUtil.getOrgName(telInfoByOrgId.getOrgId(),oaOrganizationList));
			}
			appOrgPmcTelInfo.setAppPmcOrgTelInfos(telInfoByOrgIds);
		}

		if (CollectionUtils.isNotEmpty(communityIds)){
			AppOrgPmcTelInfo obj2 = new AppOrgPmcTelInfo();
			obj2.setType(Const.TYPE_PHONE_BOOK_PMC);
			obj2.setCommunityIds(communityIds);
			//查询小区电话
			List<PhoneBookCommunityCompanyVO> telInfoByOrgIds = phoneBookDao.getTelInfoByCommunityIds(obj2);
			appOrgPmcTelInfo.setAppPmcCommunityPmcTelInfos(telInfoByOrgIds);

			//查询小区物业人员管理员信息
			List<PmcStaff> staffInfosByCommunityIds = pmcStaffRelCommunityDao.getStaffInfosByCommunityIds(communityIds, PmcStaffRoleTypeEnum.PROPERTY_MANAGEMENT_ROLE_TYPE_ADMIN.getCode(),staffid);
			appOrgPmcTelInfo.setAppPmcStaffAdminList(staffInfosByCommunityIds);

			//查询小区物业人员维修员信息
			List<PmcStaff> staffInfosByCommunityIds1 = pmcStaffRelCommunityDao.getStaffInfosByCommunityIds(communityIds, PmcStaffRoleTypeEnum.PROPERTY_MANAGEMENT_ROLE_TYPE_REPAIR.getCode(),staffid);
			appOrgPmcTelInfo.setAppPmcStaffRepaireList(staffInfosByCommunityIds1);

		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(appOrgPmcTelInfo);

		return baseVo;
	}
	/**
	 * web端 社区办	查询社区电话
	 * @param phoneBook
	 * @return
	 */
	@Override
	public BaseVo getOrgTelInfo(PhoneBook phoneBook) {
		List<Long> orgIds = new ArrayList<>();
		//所有的社区
		List<OaOrganization> oaOrganizationList = commonUtil.getOaOrganizationList();
		Long orgId = phoneBook.getOrgId();
		if (orgId!=null){
			orgIds.add(orgId);
		}else {
			if (CollectionUtils.isNotEmpty(oaOrganizationList)){
				for (OaOrganization oaOrganization : oaOrganizationList) {
					orgIds.add(oaOrganization.getId());
				}
			}
		}

		PhoneBookVO phoneBookVO = new PhoneBookVO();
		phoneBookVO.setOrgIds(orgIds);
		phoneBookVO.setType(Const.TYPE_PHONE_BOOK_ORG);

		List<PhoneBookCommunityCompanyVO> telInfoByOrgIds = phoneBookDao.getTelsByOrgIds(phoneBookVO);
		if (CollectionUtils.isNotEmpty(telInfoByOrgIds)){
			for (PhoneBookCommunityCompanyVO telInfoByOrgId : telInfoByOrgIds) {
				for (OaOrganization oaOrganization : oaOrganizationList){
					if (telInfoByOrgId.getOrgId().equals(oaOrganization.getId())){
						telInfoByOrgId.setOrgName(oaOrganization.getName());
						break;
					}
				}
			}
		}else {
			PhoneBookCommunityCompanyVO phoneBookCommunityCompanyVO = new PhoneBookCommunityCompanyVO();

			for (OaOrganization oaOrganization : oaOrganizationList) {
				for (Long ll : orgIds) {
					if (ll.equals(oaOrganization.getId())) {
						phoneBookCommunityCompanyVO.setOrgName(oaOrganization.getName());
						telInfoByOrgIds.add(phoneBookCommunityCompanyVO);
						break;
					}
				}
			}
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(telInfoByOrgIds);
		return baseVo;
	}
}
