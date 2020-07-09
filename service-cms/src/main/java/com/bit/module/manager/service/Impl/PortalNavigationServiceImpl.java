package com.bit.module.manager.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.manager.bean.PortalCategory;
import com.bit.module.manager.bean.PortalNavigation;
import com.bit.module.manager.dao.PortalNavigationDao;
import com.bit.module.manager.service.PortalNavigationService;
import com.bit.module.manager.vo.PortalNavigationParamsVO;
import com.bit.module.manager.vo.PortalNavigationVO;
import com.bit.util.CommonUntil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bit.common.Const.ID_LENGTH;
import static com.bit.common.cmsenum.cmsEnum.NAVIGATION_TABLE;
import static com.bit.common.cmsenum.cmsEnum.NORMAL_CATEGORY;

/**
 * PortalNavigation的Service实现类
 * @author liuyancheng
 *
 */
@Service("portalNavigationService")
public class PortalNavigationServiceImpl extends BaseService implements PortalNavigationService{
	
	private static final Logger logger = LoggerFactory.getLogger(PortalNavigationServiceImpl.class);
	
	@Autowired
	private PortalNavigationDao portalNavigationDao;

	/**
	 * 通用工具类
	 */
	@Autowired
	private CommonUntil commonUntil;
	
	/**
	 * 根据条件查询PortalNavigation
	 * @param portalNavigationVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(PortalNavigationVO portalNavigationVO){
		PageHelper.startPage(portalNavigationVO.getPageNum(), portalNavigationVO.getPageSize());
		List<PortalNavigation> list = portalNavigationDao.findByConditionPage(portalNavigationVO);
		PageInfo<PortalNavigation> pageInfo = new PageInfo<PortalNavigation>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有PortalNavigation
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<PortalNavigation> findAll(String sorter){
		return portalNavigationDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个PortalNavigation
	 * @param id
	 * @return
	 */
	@Override
	public PortalNavigation findById(Long id){
		return portalNavigationDao.findById(id);
	}
	
	/**
	 * 保存PortalNavigation
	 * @param portalNavigation
	 */
	@Override
	@Transactional
	public BaseVo add(PortalNavigation portalNavigation){

		//当前站点的最大ID
		Long id = portalNavigationDao.getMaxId(NAVIGATION_TABLE.getInfo(),portalNavigation.getStationId());

		//如果是当前站点下第一名 ID为 站点ID+01
		if(id.equals(0L)){
			portalNavigation.setId(Long.valueOf(portalNavigation.getStationId()+"01"));
		}else {
			portalNavigation.setId(id+1L);
		}

		UserInfo userInfo = getCurrentUserInfo();
		portalNavigation.setOperationUserId(userInfo.getId());
		portalNavigation.setOperationUserName(userInfo.getRealName());
		portalNavigation.setStatus(Const.STATION_STATUS_NORMAL);

		//先按名称去重
		PortalNavigation pn = portalNavigationDao.findByName(portalNavigation);

		BaseVo baseVo = new BaseVo();
		if(pn != null){
			throw new BusinessException("该导航在此站点下已存在！");
		}else {
			portalNavigationDao.add(portalNavigation);
			return baseVo;
		}


	}
	/**
	 * 更新PortalNavigation
	 * @param portalNavigation
	 */
	@Override
	@Transactional
	public BaseVo update(PortalNavigation portalNavigation){

		//先按名称去重
		PortalNavigation pn = portalNavigationDao.findByName(portalNavigation);

		BaseVo baseVo = new BaseVo();
		if(pn != null){
			throw new BusinessException("该导航在此站点下已存在！");
		}else {
			portalNavigationDao.update(portalNavigation);
			return baseVo;
		}

	}
	/**
	 * 删除PortalNavigation
	 * @param id
	 */
	@Override
	@Transactional
	public void delete(Long id){
		portalNavigationDao.delete(id);
	}
	/**
	 * 查询id
	 * @param portalNavigation
	 * @return
	 */
	@Override
	public BaseVo queryId(PortalNavigation portalNavigation) {
		BaseVo baseVo = new BaseVo();
		PortalNavigationParamsVO portalNavigationVO = new PortalNavigationParamsVO();
		portalNavigationVO.setStationId(portalNavigation.getStationId());
		portalNavigationVO.setOrderBy("id desc");
		List<PortalNavigation> list = portalNavigationDao.findByParam(portalNavigationVO);
		if (list.size() > 0){
			PortalNavigation navigation = list.get(0);
			baseVo.setData(navigation.getId() + 1);
			return baseVo;
		}
		return baseVo;
	}

	/**
	 * 根据站点获取导航树
	 * @author liyang
	 * @date 2019-05-14
	 * @param stationId : 站点ID
	 * @return : BaseVo
	 */
    @Override
    public BaseVo getNavigationTreeByStationId(Long stationId) {
		PortalNavigationVO portalNavigationVO = new PortalNavigationVO();
		portalNavigationVO.setStationId(stationId);

    	//查询站点下所有导航
		List<PortalNavigation> portalNavigationList = portalNavigationDao.findByConditionPage(portalNavigationVO);

		//初始导航
		Long InitCategoryId = 0L;
		PortalCategory portalCategory = new PortalCategory();
		portalCategory.setId(InitCategoryId);

		//获取导航下所有栏目
		for(PortalNavigation pn : portalNavigationList){
			portalCategory.setNavigationId(pn.getId());
			List<PortalCategory> portalCategoryListTemp = commonUntil.checkDownCategory(portalCategory,ID_LENGTH);
			pn.setSecondMenu(portalCategoryListTemp);
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalNavigationList);

		return baseVo;
    }

	/**
	 * 获取导航树(内容发布时使用，不包含特殊)
	 * @author liyang
	 * @date 2019-06-03
	 * @param stationId : 站点ID
	 * @return : BaseVo
	 */
    @Override
    public BaseVo getNavigationTreeExSpecial(Long stationId) {

		PortalNavigationVO portalNavigationVO = new PortalNavigationVO();
		portalNavigationVO.setStationId(stationId);

		//查询站点下所有导航
		List<PortalNavigation> portalNavigationList = portalNavigationDao.findByConditionPage(portalNavigationVO);

		List<Long> navigationIdList = new ArrayList<>();
		portalNavigationList.forEach(portalNavigation->{navigationIdList.add(portalNavigation.getId());});

		//转换成map
		Map<Long,PortalNavigation> portalNavigationMap = portalNavigationList.stream().collect(Collectors.toMap(PortalNavigation::getId,portalNavigation -> portalNavigation));

		//正常栏目
		Integer categoryType = NORMAL_CATEGORY.getCode();

		//获取指定导航下所有正常栏目
		List<PortalCategory> portalCategoryList = portalNavigationDao.getNavigationTreeExSpecialSql(navigationIdList,categoryType);

		//根据导航ID进行聚合
		Map<Long,List<PortalCategory>> portalCategoryMap = portalCategoryList.stream().collect(Collectors.groupingBy(PortalCategory::getNavigationId));

		List<PortalNavigation> portalNavigations = new ArrayList<>();
		for (Map.Entry<Long, List<PortalCategory>> map : portalCategoryMap.entrySet()) {
			PortalNavigation pn = new PortalNavigation();

			//导航ID
			pn.setId(map.getKey());

			//导航名称
			pn.setNavigationName(portalNavigationMap.get(map.getKey()).getNavigationName());

			//所属栏目
			pn.setStationId(portalNavigationMap.get(map.getKey()).getStationId());

			//下属栏目ID
			pn.setSecondMenu(map.getValue());
			portalNavigations.add(pn);
 		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalNavigations);

		return baseVo;
    }
}
