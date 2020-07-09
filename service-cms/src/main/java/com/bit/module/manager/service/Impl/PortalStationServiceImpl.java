package com.bit.module.manager.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.manager.bean.PortalCategory;
import com.bit.module.manager.bean.PortalNavigation;
import com.bit.module.manager.bean.PortalStation;
import com.bit.module.manager.dao.PortalCategoryDao;
import com.bit.module.manager.dao.PortalNavigationDao;
import com.bit.module.manager.dao.PortalStationDao;
import com.bit.module.manager.service.PortalStationService;
import com.bit.module.manager.vo.PortalCategoryVO;
import com.bit.module.manager.vo.PortalNavigationParamsVO;
import com.bit.module.manager.vo.PortalStationVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * PortalStation的Service实现类
 * @author liuyancheng
 *
 */
@Service("portalStationService")
public class PortalStationServiceImpl extends BaseService implements PortalStationService{
	
	private static final Logger logger = LoggerFactory.getLogger(PortalStationServiceImpl.class);
	
	@Autowired
	private PortalStationDao portalStationDao;
	@Autowired
	private PortalNavigationDao portalNavigationDao;
	@Autowired
	private PortalCategoryDao portalCategoryDao;
	
	/**
	 * 根据条件查询PortalStation
	 * @param portalStationVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(PortalStationVO portalStationVO){
		PageHelper.startPage(portalStationVO.getPageNum(), portalStationVO.getPageSize());
		List<PortalStation> list = portalStationDao.findByConditionPage(portalStationVO);
		PageInfo<PortalStation> pageInfo = new PageInfo<PortalStation>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有PortalStation
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<PortalStation> findAll(String sorter){
		return portalStationDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个PortalStation
	 * @param id
	 * @return
	 */
	@Override
	public PortalStation findById(Long id){
		return portalStationDao.findById(id);
	}
	/**
	 * 保存PortalStation
	 * @param portalStation
	 */
	@Override
	@Transactional
	public BaseVo add(PortalStation portalStation){
		UserInfo userInfo = getCurrentUserInfo();
		portalStation.setOperationUserId(userInfo.getId());
		portalStation.setOperationUserName(userInfo.getRealName());
		portalStation.setStatus(Const.STATION_STATUS_NORMAL);

		//先按名称去重
		PortalStation ps = portalStationDao.findByName(portalStation);

		BaseVo baseVo = new BaseVo();
		if(ps != null){
			throw new  BusinessException("该站点已存在！");
		}else {
			portalStationDao.add(portalStation);
			return baseVo;
		}
	}
	/**
	 * 更新PortalStation
	 * @param portalStation
	 */
	@Override
	@Transactional
	public BaseVo update(PortalStation portalStation){

		//先按名称去重
		PortalStation ps = portalStationDao.findByName(portalStation);

		BaseVo baseVo = new BaseVo();
		if(ps != null){
			throw new  BusinessException("该站点已存在！");
		}else {
			portalStationDao.update(portalStation);
			return baseVo;
		}

	}

	/**
	 * 查询树
	 * @return
	 */
	@Override
	public BaseVo queryTree() {
		//1. 先查询所有已启用的站点
		PortalStationVO portalStationVO = new PortalStationVO();
		portalStationVO.setStatus(0);
		List<PortalStation> stationList = portalStationDao.findByParam(portalStationVO);

		//2. 查询所有导航
		for (PortalStation station : stationList) {
			PortalNavigationParamsVO portalNavigationVO = new PortalNavigationParamsVO();
			portalNavigationVO.setStatus(0);
			portalNavigationVO.setStationId(station.getId());
			List<PortalNavigation> navigationList = portalNavigationDao.findByParam(portalNavigationVO);
			station.setSecondMenu(navigationList);
		}

		//3. 查询所有栏目
		List<PortalCategory> removeList = new ArrayList<>();
		for (PortalStation station : stationList) {
			List<PortalNavigation> navigations = station.getSecondMenu();
			for (PortalNavigation navigation : navigations) {
				PortalCategoryVO portalCategoryVO = new PortalCategoryVO();
				portalCategoryVO.setStatus(0);
				portalCategoryVO.setNavigationId(navigation.getId());
				List<PortalCategory> categoryList = portalCategoryDao.findByParam(portalCategoryVO);
				for (PortalCategory portalCategory : categoryList) {
					String idStr = String.valueOf(portalCategory.getId());
					if (idStr.indexOf("18") != -1 && idStr.length() >=4){
						removeList.add(portalCategory);
						continue;
					}
					if (idStr.indexOf("19") != -1 && idStr.length() >=4){
						removeList.add(portalCategory);
						continue;
					}
				}
				categoryList.removeAll(removeList);
				navigation.setSecondMenu(categoryList);
			}
		}
        //4. 查询id为18和19的二级菜单
		List<PortalCategory> list = new ArrayList<>();
		List<PortalCategory> list2 = new ArrayList<>();
		for (PortalStation station : stationList) {
			List<PortalNavigation> navigations = station.getSecondMenu();
			for (PortalNavigation navigation : navigations) {
				List<PortalCategory> secondMenu = navigation.getSecondMenu();
				for (PortalCategory menu : secondMenu) {
					if (menu.getId().equals(18L)){
						list.clear();
						List<PortalCategory> categoryList = portalCategoryDao.findAll("");
						for (PortalCategory portalCategory : categoryList) {
							String idStr = String.valueOf(portalCategory.getId());
							if (idStr.indexOf("18") != -1 && idStr.length() >=4){
								list.add(portalCategory);
							}
						}
						menu.setSecondMenu(list);
					}
					if (menu.getId().equals(19L)){
						list2.clear();
						List<PortalCategory> categoryList = portalCategoryDao.findAll("");
						for (PortalCategory portalCategory : categoryList) {
							String idStr = String.valueOf(portalCategory.getId());
							if (idStr.indexOf("19") != -1 && idStr.length() >=4){
								list2.add(portalCategory);
							}
						}
						menu.setSecondMenu(list2);
					}
				}
			}
		}
		BaseVo baseVo = new BaseVo();
		baseVo.setData(stationList);
		return baseVo;
	}

	@Override
	public BaseVo queryId() {
		BaseVo baseVo = new BaseVo();
		// 按照id倒序排列，取第一条+1
		List<PortalStation> stationList = portalStationDao.findAll("id desc");
		if (stationList.size() > 0){
			PortalStation portalStation = stationList.get(0);
			Long newId = portalStation.getId() + 1;
			baseVo.setData(newId);
		}
		return baseVo;
	}

	/**
	 * 删除PortalStation
	 * @param id
	 */
	@Override
	public void delete(Long id){
		portalStationDao.delete(id);
	}

}
