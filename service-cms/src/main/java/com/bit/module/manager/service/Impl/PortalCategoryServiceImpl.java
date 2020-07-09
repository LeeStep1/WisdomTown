package com.bit.module.manager.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.ResultCode;
import com.bit.module.manager.bean.BaseTableInfo;
import com.bit.module.manager.bean.PortalCategory;
import com.bit.module.manager.dao.PortalCategoryDao;
import com.bit.module.manager.dao.PortalNavigationDao;
import com.bit.module.manager.service.PortalCategoryService;
import com.bit.module.manager.vo.PortalCategoryResultVO;
import com.bit.util.CommonUntil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.bit.common.Const.DEFAULT_RANK;
import static com.bit.common.Const.ID_LENGTH;
import static com.bit.common.cmsenum.cmsEnum.SERVICETYPE_CATEGORY_TABLE;
import static com.bit.common.cmsenum.cmsEnum.USING_FLAG;

/**
 * PortalCategory的Service实现类
 * @author liuyancheng
 *
 */
@Service("portalCategoryService")
public class PortalCategoryServiceImpl extends BaseService implements PortalCategoryService{
	
	private static final Logger logger = LoggerFactory.getLogger(PortalCategoryServiceImpl.class);
	
	@Autowired
	private PortalCategoryDao portalCategoryDao;
	@Autowired
	private PortalNavigationDao portalNavigationDao;

	/**
	 * 通用工具类
	 */
	@Autowired
	private CommonUntil commonUntil;
	
	/**
	 * 根据条件查询PortalCategory
	 * @param portalCategory
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(PortalCategory portalCategory){
//		List<PortalCategoryResultVO> list = portalCategoryDao.findByConditionPage(portalCategoryVO);
//		// 遍历list把导航id存入list
//		List<Long> ids = new ArrayList<>();
//		for (PortalCategoryResultVO portalCategory : list) {
//			ids.add(portalCategory.getNavigationId());
//		}
//		HashSet h = new HashSet(ids);
//		ids.clear();
//		ids.addAll(h);
//		// 批量查询导航，挨个对比
//		List<PortalNavigation> navigationList = portalNavigationDao.batchById(ids);
//		for (PortalCategoryResultVO portalCategory : list) {
//			for (PortalNavigation portalNavigation : navigationList) {
//				if (portalCategory.getNavigationId().equals(portalNavigation.getId())){
//					portalCategory.setNavigationName(portalNavigation.getNavigationName());
//				}
//			}
//		}
		Integer idLength = ID_LENGTH;
		List<PortalCategoryResultVO> list = portalCategoryDao.findByCategoryListByStationId(portalCategory.getStationId(),idLength);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(list);
		return baseVo;
	}
	/**
	 * 查询所有PortalCategory
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<PortalCategory> findAll(String sorter){
		return portalCategoryDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个PortalCategory
	 * @param id
	 * @return
	 */
	@Override
	public PortalCategory findById(Long id){
		return portalCategoryDao.findById(id);
	}
	
	/**
	 * 保存PortalCategory
	 * @param portalCategory
	 */
	@Override
	public BaseVo add(PortalCategory portalCategory){

		UserInfo userInfo = getCurrentUserInfo();

		//获取当前最大ID
		BaseTableInfo baseTableInfo = commonUntil.getMaxIdAndRank(portalCategory.getParentId(),SERVICETYPE_CATEGORY_TABLE.getInfo(),portalCategory.getNavigationId());

		portalCategory.setId(baseTableInfo.getId());
		portalCategory.setRank(DEFAULT_RANK);

		//操作者
		portalCategory.setOperationUserId(userInfo.getId());
		portalCategory.setOperationUserName(userInfo.getRealName());

		portalCategory.setStatus(USING_FLAG.getCode());

		//先按名称去重
		PortalCategory pc = portalCategoryDao.findByName(portalCategory);

		BaseVo baseVo = new BaseVo();
		if(pc != null){
			throw new BusinessException("该栏目在此导航下已存在！");
		}else {
			portalCategoryDao.add(portalCategory);
			return baseVo;
		}

	}
	/**
	 * 更新PortalCategory
	 * @param portalCategory
	 */
	@Override
	public void update(PortalCategory portalCategory){
		portalCategoryDao.update(portalCategory);
	}
	/**
	 * 删除PortalCategory
	 * @param id
	 */
	@Override
	public void delete(Long id){
		portalCategoryDao.delete(id);
	}

	/**
	 * 新增个人/法人服务类型
	 * @author liyang
	 * @date 2019-05-13
	 * @param portalCategory :
	 * @return : BaseVo
	*/
    @Override
    public BaseVo addServiceType(PortalCategory portalCategory) {

		//取得最新的子栏目ID与排名
		BaseTableInfo baseTableInfo = commonUntil.getMaxIdAndRank(portalCategory.getParentId(),SERVICETYPE_CATEGORY_TABLE.getInfo(),portalCategory.getNavigationId());

		//新增数据库
		portalCategory.setId(baseTableInfo.getId());
		portalCategory.setRank(baseTableInfo.getRank());
		portalCategoryDao.add(portalCategory);

        return successVo();
    }
	/**
	 * 个人服务 or 法人服务类型分页查询
	 * @author chenduo
	 * @param portalCategory
	 * @return
	 */
	@Override
	public BaseVo serviceTypeListPage(PortalCategory portalCategory) {
        List<PortalCategory> portalCategories = portalCategoryDao.serviceTypeListPage(portalCategory);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(portalCategories);
        return baseVo;
	}
    /**
     * 个人服务 or 法人服务 批量更新服务排序
     * @param portalCategoryList
     * @return
     */
    @Override
    @Transactional
    public BaseVo serviceSort(List<PortalCategory> portalCategoryList) {
        BaseVo baseVo = new BaseVo();
        if (portalCategoryList==null || portalCategoryList.size()<=0){
            baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            baseVo.setMsg(ResultCode.PARAMETER_ERROR.getInfo());
            baseVo.setData(null);
            return baseVo;
        }
        List<PortalCategory> list = new ArrayList<>();
        //批量更新排序值
        //过滤参数 有空值的淘汰
        for (PortalCategory portalCategory : portalCategoryList) {
            if (portalCategory.getId()!=null && portalCategory.getRank()!=null){
                list.add(portalCategory);
            }

        }
        portalCategoryDao.batchUpdateRank(list);
        return new BaseVo();
    }

}
