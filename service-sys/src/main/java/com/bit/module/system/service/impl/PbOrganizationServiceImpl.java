package com.bit.module.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.SysConst;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.OrganizationPb;
import com.bit.module.system.bean.PbOrganization;
import com.bit.module.system.bean.User;
import com.bit.module.system.dao.PbOrganizationDao;
import com.bit.module.system.dao.UserRelPbOrgDao;
import com.bit.module.system.feign.PbServiceFeign;
import com.bit.module.system.service.PbOrganizationService;
import com.bit.module.system.vo.PbOrganizationVO;
import com.bit.utils.RadixUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * PbOrganization的Service实现类
 * @author liqi
 *
 */
@Service("pbOrganizationService")
public class PbOrganizationServiceImpl extends BaseService implements PbOrganizationService {
	
	private static final Logger logger = LoggerFactory.getLogger(PbOrganizationServiceImpl.class);
	
	@Autowired
	private PbOrganizationDao pbOrganizationDao;
	@Autowired
	private UserRelPbOrgDao userRelPbOrgDao;
	@Autowired
    private PbServiceFeign pbServiceFeign;
    @Autowired
    private PbOrganizationService pbOrganizationService;
	/**
	 * 根据条件查询PbOrganization
	 * @param pbOrganizationVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(PbOrganizationVO pbOrganizationVO){
		PageHelper.startPage(pbOrganizationVO.getPageNum(), pbOrganizationVO.getPageSize());
        if (StringUtils.isNotEmpty(pbOrganizationVO.getPcode())){
            pbOrganizationVO.setOrderBy("p_code");
            pbOrganizationVO.setOrder("asc");
        }else {
            pbOrganizationVO.setOrderBy("create_time");
            pbOrganizationVO.setOrder("desc");
        }
		List<PbOrganization> list = pbOrganizationDao.findByConditionPage(pbOrganizationVO);
        for (PbOrganization pbOrganization : list) {
            String strid = RadixUtil.toFullBinaryString(Long.valueOf(pbOrganization.getId()));
            pbOrganization.setStrId(strid);
        }
		PageInfo<PbOrganization> pageInfo = new PageInfo<PbOrganization>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 查询所有PbOrganization
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<PbOrganization> findAll(String sorter){
		return pbOrganizationDao.findAll(sorter);
	}

	/**
	 * 通过二进制strid查询单个PbOrganization
	 * @param strId
	 * @return
	 */
	@Override
	public BaseVo findById(String strId){
	    Long id= RadixUtil.toLong(strId);
        OrganizationPb pbOrgById = pbOrganizationService.findPbOrgById(id);
        PbOrganization pbOrganization = new PbOrganization();
        BeanUtils.copyProperties(pbOrgById,pbOrganization);
        pbOrganization.setPcode(pbOrgById.getPCode());

        pbOrganization.setStrId(strId);
        //判断一下等级，如果该对象是顶级，则不再查询上一级
        if (RadixUtil.getlevel(strId) != 1){
            PbOrganization subObj= pbOrganizationDao.findSubObjById(id);
            String subStrId = RadixUtil.toFullBinaryString(Long.valueOf(subObj.getId()));
            pbOrganization.setStrPid(subStrId);
            pbOrganization.setPid(Long.valueOf(subObj.getId()));
            pbOrganization.setPname(subObj.getName());
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pbOrganization);
        return baseVo;
	}
	
	/**
	 * 保存PbOrganization
	 * @param pbOrganization
	 */
	/*@Override
	@Transactional
	public void add(PbOrganization pbOrganization){
	    //创建时间
        pbOrganization.setCreateTime(new Date());
        if (pbOrganization.getStrPid()==null){
            List<PbOrganization> pbOrganizations=pbOrganizationDao.findRootPbOrgList();
            if (pbOrganizations.size()==0){
                pbOrganization.setId(RadixUtil.toLong(SysConst.BINARY));
                pbOrganizationDao.add(pbOrganization);
            }else {
                pbOrganizationDao.addRootNode(pbOrganization);
            }
        }else {
            String strPid = pbOrganization.getStrPid();
            Long pid = RadixUtil.toLong(strPid);
            pbOrganization.setPid(pid);
            pbOrganizationDao.addChildNode(pbOrganization);
        }
	}*/

	/**
	 * 更新PbOrganization
	 * @param pbOrganization
	 */
	/*@Override
	@Transactional
	public void update(PbOrganization pbOrganization){
        String strId = pbOrganization.getStrId();
        Long id = RadixUtil.toLong(strId);
        pbOrganization.setId(id);
        pbOrganizationDao.update(pbOrganization);
	}*/

	/**
	 * 删除PbOrganization
	 * @param ids
	 */
	/*@Override
	@Transactional
	public void batchDelete(List<Long> ids){
		pbOrganizationDao.batchDelete(ids);
	}*/

	/**
	 * 查询党政组织如果有下级组织不能删除  如果被用户用了也不能删除
	 * @param strId
	 * @return
	 */
	@Override
	public BaseVo checkNexusByPbId(String strId) {
		BaseVo baseVo = new BaseVo();
        Long id = RadixUtil.toLong(strId);
		int userCount = userRelPbOrgDao.findCountByPbId(id);
        int pbCount = pbServiceFeign.findLowerLevelById(String.valueOf(id),true);
        Map<String, Object> map = new HashMap<>();
        map.put("userCount",userCount);
        map.put("pbCount",pbCount);
        baseVo.setData(map);
		return baseVo;
	}

    /**
     * 根据id查询子节点集合 list
     * @param strId
     * @return
     */
    @Override
    public BaseVo findChildListByPid(String strId) {
        Long id = RadixUtil.toLong(strId);
        List<PbOrganization> list=pbOrganizationDao.findChildListByPid(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(list);
        return baseVo;
    }

    /**
     * 根据参数查询
     * @param pbOrganization
     * @return
     */
    @Override
    public List<PbOrganization> findAllByParam(PbOrganization pbOrganization) {
        List<PbOrganization> allByParam = pbOrganizationDao.findAllByParam(pbOrganization);
        return allByParam;
    }

    /**
     * 查询组织树
     * @param pbOrganization
     * @return
     */
   /* @Override
    public List<PbOrganization> findTreeByParam(PbOrganization pbOrganization) {
        List<PbOrganization> allByParam = pbOrganizationDao.findAllByParam(pbOrganization);
        for (PbOrganization organization : allByParam) {
            String string = RadixUtil.toFullBinaryString(organization.getId());
            organization.setStrId(string);
            int level = RadixUtil.getlevel(string);
            organization.setLevel(level);
        }
        List<PbOrganization> rootlist = new ArrayList<>();
        for (PbOrganization organization : allByParam) {
            if (1 == organization.getLevel()) {
                rootlist.add(organization);
            }
        }
        for (PbOrganization organization : rootlist) {
            organization.setChildList(getChild(allByParam,organization.getStrId(),organization.getLevel()));
        }
        return rootlist;
    }*/

    /**
     * 校验组织编码
     * @param pbOrganization
     * @return
     */
   /* @Override
    public BaseVo checkPcode(PbOrganizationVO pbOrganization) {
        BaseVo baseVo = new BaseVo();
        Boolean flag ;
        if (pbOrganization.getIdStr() == null) {
            int count = pbOrganizationDao.findCountByPcode(pbOrganization.getPcode());
            if (count > 0) {
                flag = false;
            } else {
                flag = true;
            }
        } else {
            String strId = pbOrganization.getIdStr();
            Long id = Long.parseLong(strId);
            OrganizationPb byId = pbOrganizationService.findPbOrgById(id);
            String pcode = byId.getPCode();
            if (pcode.equals(pbOrganization.getPcode())) {
                flag = true;
            } else {
                int count = pbOrganizationDao.findCountByPcode(pbOrganization.getPcode());
                if (count > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        }
        baseVo.setData(flag);
        return baseVo;
    }*/

    /**
     * 批量查询党建组织下所有用户
     * @author liyang
     * @date 2019-04-04
     * @param targetIds :  组织ID集合
    */
    @Override
    public BaseVo getAllUserIdsByPbOrgIds(Long[] targetIds) {

        //集合转换
        List<Long> targetIdList = new ArrayList<>();
        for (Long targetId : targetIds){
            targetIdList.add(targetId);
        }

        //获取用户
        List<Long> userIdList = pbOrganizationDao.getAllUserIdsByPbOrgIdsSql(targetIdList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(userIdList);

        return baseVo;
    }

    /**
     * 批量查询指定组织指定方式注册用户
     * @author liyang
     * @date 2019-04-04
     * @param messageTemplate : 组织ID集合与人员类型
     */
    @Override
    public BaseVo getUserIdsByOrgIds(MessageTemplate messageTemplate) {

        //获取用户
        List<Long> userIdList = pbOrganizationDao.getUserIdsByOrgIdsSql(messageTemplate);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(userIdList);

        return baseVo;
    }

    /**
     * 获取党建组织下所有用户
     * @author liyang
     * @date 2019-04-09
     * @return : List<Long> ：用户ID集合
     */
    @Override
    public List<Long> getAllUserIdsForPbOrg() {

        User user = new User();

        //设置筛选条件为用户可用
        user.setStatus(SysConst.USER_STATUS);

        //获取用户
        List<Long> userIdList = pbOrganizationDao.getAllUserIdsForPbOrgSql(user);
        return userIdList;
    }

    /**
     * 根据组织id查询党组织信息 feign调用
     * @param id
     * @return
     */
    @Override
    public OrganizationPb findPbOrgById(Long id) {
        BaseVo b1 = pbServiceFeign.query(id + "");
        String s = JSON.toJSONString(b1.getData());
        OrganizationPb organizationPb = JSON.parseObject(s, OrganizationPb.class);
        return organizationPb;
    }

    /**
     * 递归查询
     * @param allByParam  所有数据
     * @param id 二进制的string——id
     * @param i 等级
     * @return
     */
    private List<PbOrganization> getChild( List<PbOrganization> allByParam,String id,int i) {
        List<PbOrganization> pbOrganizations = new ArrayList<>();
        for (PbOrganization pbOrganization : allByParam) {
            int y=i+1;
            if (pbOrganization.getLevel()==y){
                String str = id.substring(0, i * 8);
                String pbstr = pbOrganization.getStrId().substring(0, i * 8);
                if (str.equals(pbstr)){
                    pbOrganizations.add(pbOrganization);
                }
            }
        }
        for (PbOrganization pbOrganization : pbOrganizations) {
            pbOrganization.setChildList(getChild(allByParam,pbOrganization.getStrId(),pbOrganization.getLevel()));
        }
		if(pbOrganizations.size()==0){
        	return  null;
		}
        return pbOrganizations;
    }

    /**
	 * 删除PbOrganization
	 * @param strId
	 */
	/*@Override
	@Transactional
	public void delete(String strId){
        Long id = RadixUtil.toLong(strId);
        pbOrganizationDao.delete(id);
	}*/



}
