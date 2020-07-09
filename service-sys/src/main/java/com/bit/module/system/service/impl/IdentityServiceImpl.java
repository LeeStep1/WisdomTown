package com.bit.module.system.service.impl;

import com.bit.base.exception.CheckException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Identity;
import com.bit.module.system.bean.IdentityRelRole;
import com.bit.module.system.dao.IdentityDao;
import com.bit.module.system.dao.IdentityRelRoleDao;
import com.bit.module.system.dao.ResourceDao;
import com.bit.module.system.dao.UserRelIdentityDao;
import com.bit.module.system.service.IdentityService;
import com.bit.module.system.vo.IdentityVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Identity的Service实现类
 * @author liqi
 *
 */
@Service("identityService")
public class IdentityServiceImpl extends BaseService implements IdentityService {
	
	private static final Logger logger = LoggerFactory.getLogger(IdentityServiceImpl.class);
	
	@Autowired
	private IdentityDao identityDao;
	@Autowired
	private IdentityRelRoleDao identityRelRoleDao;
	@Autowired
	private UserRelIdentityDao userRelIdentityDao;
	@Autowired
	private ResourceDao resourceDao;

	/**
	 * 根据条件查询Identity
	 * @param identityVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(IdentityVO identityVO){
		PageHelper.startPage(identityVO.getPageNum(), identityVO.getPageSize());
		List<Identity> list = identityDao.findByConditionPage(identityVO);
		PageInfo<Identity> pageInfo = new PageInfo<Identity>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 根据条件查询Identity 业务分页
	 * @param identityVO
	 * @return
	 */
	@Override
	public BaseVo findRealPage(IdentityVO identityVO) {
		PageHelper.startPage(identityVO.getPageNum(), identityVO.getPageSize());
		identityVO.setOrderBy("a.id desc");
		List<Identity> list = identityDao.findRealPage(identityVO);
		PageInfo<Identity> pageInfo = new PageInfo<Identity>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 查询所有Identity
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<Identity> findAll(String sorter){
		return identityDao.findAll(sorter);
	}

	/**
	 * 通过主键查询单个Identity
	 * @param id
	 * @return
	 */
	@Override
	public Identity findById(Long id){
		return identityDao.findById(id);
	}

	/**
	 * 根据主键ID查询Identity  业务查询
	 * @param id
	 * @return
	 */
	@Override
	public Identity findBykey(Long id) {
        Identity identity = identityDao.findById(id);
        if (null!=identity){
            //根据身份id查询身份和角色中间表
            List<IdentityRelRole> identityRelRoles=identityRelRoleDao.findByIdentityId(identity.getId());
            identity.setIdentityRelRoles(identityRelRoles);
        }
        return identity;
	}

	/**
	 * 校验删除  大于0不能删除  ---查询和身份有关系的数据
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo checkCountNexus(Long id) {
		int count=userRelIdentityDao.findCountByIdentityId(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(count);
        return baseVo;
	}

    /**
     * 根据appID查询identityList
     * @param appId
     * @return
     */
    @Override
    public List<Identity> findIdentListByAppId(Long appId) {
        return identityDao.findIdentListByAppId(appId,null);
    }

    /**
     * 根据条件查询所有
     * @param identity
     * @return
     */
    @Override
    public List<Identity> findAllByParam(Identity identity) {
        return identityDao.findAllByParam(identity);
    }

	/**
	 * 校验默认身份  --一个应用下只有一个默认身份
	 * @param identity
	 * @return
	 */
	@Override
	public BaseVo checkAcquiesce(Identity identity) {
        BaseVo baseVo = new BaseVo();
        Boolean flag;
        //更新校验
        if (identity.getId()!=null){
            //原数据（旧数据）
            Identity byId = identityDao.findById(identity.getId());
            Integer appIdOld = byId.getAppId();
            Integer acquiesceOld = byId.getAcquiesce();
            if (appIdOld.equals(identity.getAppId())&&acquiesceOld.equals(identity.getAcquiesce())){
                flag=true;
            }else {
                int count = identityDao.findByAppIdAndAcqu(identity);
				if (count>0){
					flag=false;
				}else {
					flag=true;
				}
            }
        }else {
         //保存校验
            int count=identityDao.findByAppIdAndAcqu(identity);
            if (count>0){
                flag=false;
            }else {
                flag=true;
            }
        }
        baseVo.setData(flag);
        return baseVo;
	}

	/**
	 * 根据APPId查询默认身份
	 * @param appId
	 * @return
	 */
	@Override
	public Identity getDefaultIdentityByAppId(Long appId) {
		//设置默认身份标识
		int acquiesce = 1;

		//查询默认身份
		Identity identity = identityDao.getDefaultIdentityByAppIdSql(appId,acquiesce);

		return identity;
	}

	@Override
	public BaseVo checkNameUnique(Identity identity) {
		BaseVo baseVo = new BaseVo();
		Boolean flag;
		//更新校验
		if (identity.getId() != null) {
			//原数据（旧数据）
			Identity byId = identityDao.findById(identity.getId());
			Integer appIdOld = byId.getAppId();
			String oldName = byId.getName();
			if (appIdOld.equals(identity.getAppId()) && oldName.equals(identity.getName())) {
				flag = true;
			} else {
				int count = identityDao.findByAppIdAndName(identity);
				if (count > 0) {
					flag = false;
				} else {
					flag = true;
				}
			}
		} else {
			//保存校验
			int count = identityDao.findByAppIdAndName(identity);
			if (count > 0) {
				flag = false;
			} else {
				flag = true;
			}
		}
		baseVo.setData(flag);
		return baseVo;
	}
    /**
	 * 保存Identity
	 * @param identity
	 */
	@Override
    @Transactional
	public void add(Identity identity){
        List<IdentityRelRole> identityRelRoles = identity.getIdentityRelRoles();
        //添加身份时，角色必选,不选则身份添加失败，返回参数不能为空
        if (identityRelRoles != null && identityRelRoles.size() > 0){
			identityDao.add(identity);
            for (IdentityRelRole identityRelRole : identityRelRoles) {
                identityRelRole.setIdentityId(identity.getId());
            }
			//保存中间表s
			identityRelRoleDao.batchAdd(identityRelRoles);
        }else {
        	throw new CheckException("角色不能为空");
		}
	}

	/**
	 * 更新Identity
	 * @param identity
	 */
	@Override
    @Transactional
	public void update(Identity identity){
		List<IdentityRelRole> identityRelRoles = identity.getIdentityRelRoles();
		if (identityRelRoles != null && identityRelRoles.size() > 0){
			//删除中间表
			identityRelRoleDao.delByIndentityId(identity.getId());
			for (IdentityRelRole identityRelRole : identityRelRoles) {
				identityRelRole.setIdentityId(identity.getId());
			}
			//从新插入中间表
			identityRelRoleDao.batchAdd(identityRelRoles);
			//更新主表
			identityDao.update(identity);
		}else {
			throw new CheckException("角色不能为空");
		}
	}

	/**
	 * 删除Identity
	 * @param ids
	 */
	@Override
    @Transactional
	public void batchDelete(List<Long> ids){
		identityDao.batchDelete(ids);
	}

	/**
	 * 删除Identity
	 * @param id
	 */
	@Override
    @Transactional
	public void delete(Long id){
		identityDao.delete(id);
        identityRelRoleDao.delByIndentityId(id);
    }

}
