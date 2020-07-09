package com.bit.module.manager.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalPbLeader;
import com.bit.module.manager.bean.ProtalPbOrg;
import com.bit.module.manager.dao.ProtalPbOrgDao;
import com.bit.module.manager.service.ProtalPbOrgService;
import com.bit.module.manager.vo.ProtalPbOrgVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.bit.common.cmsenum.cmsEnum.UNDEL_FLAG;

/**
 * ProtalPbOrg的Service实现类
 * @author liuyancheng
 *
 */
@Service("protalPbOrgService")
public class ProtalPbOrgServiceImpl extends BaseService implements ProtalPbOrgService{
	
	private static final Logger logger = LoggerFactory.getLogger(ProtalPbOrgServiceImpl.class);
	
	@Autowired
	private ProtalPbOrgDao protalPbOrgDao;
	
	/**
	 * 根据条件查询ProtalPbOrg
	 * @param protalPbOrgVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(ProtalPbOrgVO protalPbOrgVO){
		PageHelper.startPage(protalPbOrgVO.getPageNum(), protalPbOrgVO.getPageSize());
		List<ProtalPbOrg> list = protalPbOrgDao.findByConditionPage(protalPbOrgVO);
		PageInfo<ProtalPbOrg> pageInfo = new PageInfo<ProtalPbOrg>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有ProtalPbOrg
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<ProtalPbOrg> findAll(String sorter){
		return protalPbOrgDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个ProtalPbOrg
	 * @param id
	 * @return
	 */
	@Override
	public ProtalPbOrg findById(Long id){
		return protalPbOrgDao.findById(id);
	}
	
	/**
	 * 新增党建组织下栏目
	 * @param protalPbOrg
	 */
	@Override
	public void add(ProtalPbOrg protalPbOrg){
		UserInfo userInfo = getCurrentUserInfo();

		protalPbOrg.setOperationUserId(userInfo.getId());
		protalPbOrg.setOperationUserName(userInfo.getRealName());

		//判断ID是否存在，ID存在为修改，不存在为修改
		if(protalPbOrg.getId() != null && !("").equals(protalPbOrg.getId())){
			this.update(protalPbOrg);
		}else {
			protalPbOrgDao.add(protalPbOrg);
		}

	}
	/**
	 * 更新ProtalPbOrg
	 * @param protalPbOrg
	 */
	@Override
	public void update(ProtalPbOrg protalPbOrg){
		UserInfo userInfo = getCurrentUserInfo();
		protalPbOrg.setOperationUserId(userInfo.getId());
		protalPbOrg.setOperationUserName(userInfo.getRealName());

		protalPbOrgDao.update(protalPbOrg);
	}
	/**
	 * 删除ProtalPbOrg
	 * @param id
	 */
	@Override
	public void delete(Long id){
		protalPbOrgDao.delete(id);
	}

	/**
	 * 党建组织导航下栏目展示
	 * @author liyang
	 * @date 2019-05-24
	 * @param navigationId : 所属导航
	 * @param categoryId : 所属栏目
	 * @return : BaseVo
	 */
    @Override
    public BaseVo getPbOrgNavigationContend(Long navigationId,Long categoryId) {

		ProtalPbOrg pol = protalPbOrgDao.getPbOrgNavigationContendSql(navigationId,categoryId);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(pol);

		return baseVo;
    }

	/**
	 * @description: 领导班子头像展示
	 * @author: liyang
	 * @date: 2019-05-07
	 **/
	@Override
	public BaseVo getLeaderImage() {
		PortalPbLeader portalPbLeader = new PortalPbLeader();
		portalPbLeader.setDelStatus(UNDEL_FLAG.getCode());

		List<PortalPbLeader> pblList = protalPbOrgDao.getLeaderImageSql(portalPbLeader);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(pblList);

		return baseVo;
	}
}
