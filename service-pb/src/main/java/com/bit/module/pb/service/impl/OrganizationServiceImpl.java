package com.bit.module.pb.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.PartyOrgLevelEnum;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.bean.OrganizationParam;
import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.dao.OrganizationDao;
import com.bit.module.pb.dao.PartyMemberDao;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.OrganizationVO;
import com.bit.module.pb.vo.PbOrgTreeVO;
import com.bit.module.pb.vo.PbOrganizationVO;
import com.bit.utils.RadixUtil;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Organization的Service实现类
 *
 * @author codeGenerator
 */
@Service("organizationService")
public class OrganizationServiceImpl extends BaseService implements OrganizationService {

    private static final Logger logger = LoggerFactory.getLogger(OrganizationServiceImpl.class);

    @Autowired
    private OrganizationDao organizationDao;
    @Autowired
	private PartyMemberDao partyMemberDao;

    /**
     * 根据条件查询Organization
     *
     * @param organizationVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(OrganizationVO organizationVO) {
        PageHelper.startPage(organizationVO.getPageNum(), organizationVO.getPageSize());
        List<Organization> list = organizationDao.findByConditionPage(organizationVO);
        PageInfo<Organization> pageInfo = new PageInfo<Organization>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有Organization
     *
     * @param sorter 排序字符串
     * @return
     */
    @Override
    public List<Organization> findAll(String sorter) {
        return organizationDao.findAll(sorter);
    }

    @Override
    public Organization findDirectSuperiorById(String orgId) {
        return organizationDao.findDirectSuperiorById(orgId, true);
    }

    /**
     * 通过主键查询单个Organization
     *
     * @param id
     * @return
     */
    @Override
    public Organization findById(String id) {
        return organizationDao.findById(id);
    }

    /**
     * 批量保存Organization
     *
     * @param organizations
     */
    @Override
    public void batchAdd(List<Organization> organizations) {
        //organizationDao.batchAdd(organizations);
    }

    /**
     * 保存Organization
     *
     * @param organization
     */
    @Override
    public void add(Organization organization) {
        organizationDao.add(organization);
    }

    /**
     * 批量更新Organization
     *
     * @param organizations
     */
    @Override
    public void batchUpdate(List<Organization> organizations) {
        //organizationDao.batchUpdate(organizations);
    }

    /**
     * 更新Organization
     *
     * @param organization
     */
    @Override
    public void update(Organization organization) {
        organizationDao.update(organization);
    }

    /**
     * 删除Organization
     *
     * @param ids
     */
    @Override
    public void batchDelete(List<String> ids) {
        organizationDao.batchDelete(ids);
    }

    /**
     * 根据id查询组织的直接下级
     *
     * @param orgId
     * @return
     */
    @Override
    public List<OrganizationInfoVO> getDirectSubOrganization(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            List<Organization> all = organizationDao.findAll(null);
            return all.stream().map(org -> {
                OrganizationInfoVO organizationInfoVO = new OrganizationInfoVO();
                organizationInfoVO.setId(org.getId());
                organizationInfoVO.setName(org.getName());
                organizationInfoVO.setOrgType(org.getOrgType());
                return organizationInfoVO;
            }).collect(Collectors.toList());
        }
        return organizationDao.findDirectSubordinatesInfoById(orgId);
    }

    /**
     * 获取下级组织包括自己
     *
     * @param orgId
     * @return
     */
    @Override
    public BaseVo getSubOrgNoIgnoreItself(String orgId) {
        List<Organization> organizationList = organizationDao.findSubordinatesById(orgId, true);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(organizationList);
        return baseVo;
    }

    /**
     * web端党组织下拉框获取下级组织包括自己
     *
     * @param orgId
     * @return
     */
    @Override
    public BaseVo getSubOrgNoIgnoreItselfForWeb(String orgId) {
        List<Organization> organizationList = new ArrayList<>();

        //查询用户组织信息
        Organization org = organizationDao.findById(orgId);
        Integer type = org.getOrgType();
        switch (type) {
            case 1:
                // type == 1 找所有的机构 除了type 是 5 和 1 但是要把5下面的找出来
                Organization param = new Organization();
                param.setStatus(1);
                List<Organization> all = organizationDao.findByParam(param);
                if (all != null && all.size() > 0) {
                    for (Organization organization : all) {
                        //去除orgType是1的 和 orgType是5的
                        if (!organization.getOrgType().equals(PartyOrgLevelEnum.LEVEL_ONE.getCode()) && !organization.getOrgType().equals(PartyOrgLevelEnum.LEVEL_FIVE.getCode())) {
                            organizationList.add(organization);
                        }
                    }
                }
                break;
            case 2:
                //添加自己的机构
                organizationList.add(org);
                //type == 2 or 4 找出直属下级
                List<OrganizationInfoVO> directSubordinatesInfoById = organizationDao.findDirectSubordinatesInfoById(orgId);
                if (directSubordinatesInfoById != null && directSubordinatesInfoById.size() > 0) {
                    for (OrganizationInfoVO organizationInfoVO : directSubordinatesInfoById) {
                        Organization obj = new Organization();
                        BeanUtils.copyProperties(organizationInfoVO, obj);
                        organizationList.add(obj);
                    }
                }
                break;
            case 4:
                //添加自己的机构
                organizationList.add(org);
                //type == 2 or 4 找出直属下级
                List<OrganizationInfoVO> directSubordinatesInfoById2 = organizationDao.findDirectSubordinatesInfoById(orgId);
                if (directSubordinatesInfoById2 != null && directSubordinatesInfoById2.size() > 0) {
                    for (OrganizationInfoVO organizationInfoVO : directSubordinatesInfoById2) {
                        Organization obj = new Organization();
                        BeanUtils.copyProperties(organizationInfoVO, obj);
                        organizationList.add(obj);
                    }
                }
                break;
            default:
                break;
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(organizationList);
        return baseVo;
    }

    /**
     * 查询所有的机构层级 树形结构
     *
     * @param
     * @return
     */
    @Override
    public List<OrganizationParam> tree() {
        //查询所有的机构
        List<Organization> organizationList = organizationDao.findAll(null);

        List<OrganizationParam> organizationParamList = new ArrayList<>();
        for (Organization obj : organizationList) {
            OrganizationParam param = new OrganizationParam();
            BeanUtils.copyProperties(obj, param);
            organizationParamList.add(param);
        }
        //根节点
        List<OrganizationParam> rootList = new ArrayList<>();
        for (OrganizationParam o : organizationParamList) {
            String str = RadixUtil.toFullBinaryString(Long.parseLong(o.getId()));
            o.setStrId(str);
            int level = RadixUtil.getlevel(str);
            o.setLevel(level);
            if (level == 1) {
                rootList.add(o);
            }
        }
        for (OrganizationParam organization : rootList) {
            organization.setChildList(getChild(organizationParamList, organization.getStrId(), organization.getLevel()));
        }
        return rootList;
    }

    /**
     * 根据机构id查询树形结构
     *
     * @param id
     * @return
     */
    @Override
    public List<OrganizationParam> treeById(Long id) {
        //传入参数的二进制id
        String s = RadixUtil.toFullBinaryString(id);
        int l = RadixUtil.getlevel(s);

        //查询传入参数机构下的机构
        List<Organization> organizationList = organizationDao.findSubordinatesById(id.toString(), true);
        List<OrganizationParam> organizationParamList = new ArrayList<>();
        for (Organization obj : organizationList) {
            OrganizationParam param = new OrganizationParam();
            BeanUtils.copyProperties(obj, param);
            organizationParamList.add(param);
        }
        //根节点
        List<OrganizationParam> rootList = new ArrayList<>();
        for (OrganizationParam o : organizationParamList) {
            String str = RadixUtil.toFullBinaryString(Long.parseLong(o.getId()));
            o.setStrId(str);
            int level = RadixUtil.getlevel(str);
            o.setLevel(level);
            if (level == l) {
                rootList.add(o);
            }
        }
        for (OrganizationParam organization : rootList) {
            organization.setChildList(getChild(organizationParamList, organization.getStrId(), organization.getLevel()));
        }
        return rootList;
    }

    private List<OrganizationParam> getChild(List<OrganizationParam> list, String strid, Integer level) {
        List<OrganizationParam> obj = new ArrayList<>();
        for (OrganizationParam organizationParam : list) {
            String name = organizationParam.getName();
            String s = organizationParam.getStrId();
            int l = organizationParam.getLevel();
            int y = level + 1;

//            if (l >= y){
//            	//上一级的二进制id
//			String str = strid.substring(0, level * 8);
//			//当前节点的二进制id
//			String pbstr = organizationParam.getStrId().substring(0, level * 8);
//			if (str.equals(pbstr)){
//				obj.add(organizationParam);
//
//			}
//
//            }

            //如果层级等于下一层级
            if (l == y) {
                //上一级的二进制id
                String str = strid.substring(0, level * 8);
                //当前节点的二进制id
                String pbstr = organizationParam.getStrId().substring(0, level * 8);
                if (str.equals(pbstr)) {
                    obj.add(organizationParam);
                }

            }
        }
        for (OrganizationParam organizationParam : obj) {
            organizationParam.setChildList(getChild(list, organizationParam.getStrId(), organizationParam.getLevel()));
        }
        if (obj.size() == 0) {
            return null;
        }
        return obj;
    }

//	@Override
//	public List<OrganizationParam> tree(Long id) {
//		//查询所有的机构
//		List<Organization> organizationList = organizationDao.findAll(null);
//
//		List<OrganizationParam> organizationParamList = new ArrayList<>();
//		for (Organization obj : organizationList) {
//			OrganizationParam param = new OrganizationParam();
//			BeanUtils.copyProperties(obj,param);
//			organizationParamList.add(param);
//		}
//		//根节点
//		List<OrganizationParam> rootList = new ArrayList<>();
//		for (OrganizationParam o : organizationParamList){
//			String str = RadixUtil.toFullBinaryString(Long.parseLong(o.getId()));
//			o.setStrId(str);
//			int level = RadixUtil.getlevel(str);
//			o.setLevel(level);
//			if (level == 1){
//				rootList.add(o);
//			}
//		}
//		for (OrganizationParam organization : rootList) {
//			organization.setChildList(repeat(organizationParamList,RadixUtil.toFullBinaryString(Long.parseLong(organization.getId()))));
//		}
//
//		//调用递归方法
//		return rootList;
//	}


    private List<OrganizationParam> repeat(List<OrganizationParam> all, String op) {
        List<OrganizationParam> childlist = new ArrayList<>();
        for (OrganizationParam organizationParam : all) {
            //获得根节点的层级
            int level = RadixUtil.getlevel(op);
            String str = RadixUtil.toFullBinaryString(Long.parseLong(organizationParam.getId()));
            if (str.contains(op)) {
                childlist.add(organizationParam);
                organizationParam.setChildList(childlist);
            }

        }
        for (OrganizationParam organizationParam : childlist) {
            repeat(childlist, RadixUtil.toFullBinaryString(Long.parseLong(organizationParam.getId())));
        }
        if (childlist.size() == 0) {
            return null;
        }
        return childlist;

    }

    /**
     * 删除Organization
     *
     * @param id
     */
    @Override
    public void delete(String id) {
        organizationDao.delete(id);
    }

    /**
     * 根据条件查询Organization
     *
     * @param organizationVO
     * @return
     */
    @Override
    public BaseVo sysFindByConditionPage(OrganizationVO organizationVO) {
        PageHelper.startPage(organizationVO.getPageNum(), organizationVO.getPageSize());
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(organizationVO.getPCode())) {
            organizationVO.setOrderBy("p_code");
            organizationVO.setOrder("asc");
        } else {
            organizationVO.setOrderBy("create_time");
            organizationVO.setOrder("desc");
        }

        List<PbOrganizationVO> listVo = organizationDao.sysFindByConditionPage(organizationVO);
        for (PbOrganizationVO pbOrganization : listVo) {
            pbOrganization.setStrId(RadixUtil.toFullBinaryString(Long.valueOf(pbOrganization.getId())));

        }
        PageInfo<PbOrganizationVO> pageInfo = new PageInfo<PbOrganizationVO>(listVo);
		/*List<Organization> list = organizationDao.findByConditionPage(organizationVO);
		List<PbOrganizationVO> listVo=new ArrayList<PbOrganizationVO>();
		for (Organization pbOrganization : list) {
            PbOrganizationVO a=new PbOrganizationVO();
            BeanUtils.copyProperties(pbOrganization,a);
            //a.setId(Long.valueOf(pbOrganization.getId()));
			String strid = RadixUtil.toFullBinaryString(Long.valueOf(pbOrganization.getId()));
			a.setStrId(strid);
			a.setPcode(pbOrganization.getPCode());
			//a.setIdStr(a.getId()+"");
			listVo.add(a);
		}
		PageInfo<PbOrganizationVO> pageInfo = new PageInfo<PbOrganizationVO>(listVo);*/
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }


    /**
     * 根据条件查询Organization
     *
     * @param pbOrganizationVO
     * @return
     */
    @Override
    @Transactional
    public void sysAdd(PbOrganizationVO pbOrganizationVO) {

        pbOrganizationVO.setCreateTime(new Date());
        //如果没选上级组织 strPid ==null
        if (pbOrganizationVO.getStrPid() == null) {
            Organization a = new Organization();
            //将公共赋值方法提取出来
            pbOrganizationVO.setId(String.valueOf(RadixUtil.toLong(Const.BINARY)));
            BeanUtils.copyProperties(pbOrganizationVO, a);
            a.setPCode(pbOrganizationVO.getPcode());
            //如果顶级组织为0
            if (organizationDao.findTopOrg() == 0) {
                //pbOrganizationVO.setId(RadixUtil.toLong(Const.BINARY));
                organizationDao.add(a);
            } else {
                organizationDao.addRootNode(a);
            }
        } else {
            String strPid = pbOrganizationVO.getStrPid();
            Long pid = RadixUtil.toLong(strPid);
            pbOrganizationVO.setPid(pid);
            organizationDao.addChildNode(pbOrganizationVO);
        }
    }

    /**
     * 管理平台进行更新
     *
     * @param pbOrganizationVO
     * @return
     */
    @Override
    public void sysUpdate(PbOrganizationVO pbOrganizationVO) {
        Organization a = new Organization();
        Long id = RadixUtil.toLong(pbOrganizationVO.getStrId());
        pbOrganizationVO.setId(String.valueOf(id));
        BeanUtils.copyProperties(pbOrganizationVO, a);
        a.setId(pbOrganizationVO.getId());
        a.setPCode(pbOrganizationVO.getPcode());
        a.setOrgDesc(pbOrganizationVO.getOrgDesc());
        a.setOrgType(pbOrganizationVO.getOrgType());
        a.setName(pbOrganizationVO.getName());
        organizationDao.update(a);
    }

    /**
     * 查询树
     *
     * @param organizationVO
     * @return
     */
    @Override
    public List<PbOrgTreeVO> findTreeByParam(OrganizationVO organizationVO) {
        organizationVO.setPageSize(Integer.MAX_VALUE);
        List<Organization> allByParam = organizationDao.findByConditionPage(organizationVO);
        List<PbOrgTreeVO> list = new ArrayList<>(allByParam.size());
        for (Organization organization : allByParam) {
            PbOrgTreeVO vo = new PbOrgTreeVO();
            BeanUtils.copyProperties(organization, vo);
            String string = RadixUtil.toFullBinaryString(Long.valueOf(organization.getId()));
            int level = RadixUtil.getlevel(string);
            vo.setLevel(level);
            //vo.setId(Long.valueOf(organization.getId()));
            vo.setStrId(RadixUtil.toFullBinaryString(Long.valueOf(organization.getId())));
            vo.setPcode(organization.getPCode());
            //vo.setIdStr(organization.getId());
            list.add(vo);
        }
        List<PbOrgTreeVO> rootlist = new ArrayList<>();
        for (PbOrgTreeVO organization : list) {
            if (1 == organization.getLevel()) {
                rootlist.add(organization);
            }
        }
        for (PbOrgTreeVO organization : rootlist) {
            organization.setChildList(getChild(list, organization.getStrId(), organization.getLevel()));
        }
        return rootlist;
    }


    /**
     * 校验组织编码
     *
     * @param pbOrganization
     * @return
     */
    @Override
    public BaseVo checkPcode(PbOrganizationVO pbOrganization) {
        BaseVo baseVo = new BaseVo();
        Boolean flag;
        if (pbOrganization.getId() == null) {
            int count = organizationDao.findCountByPcode(pbOrganization.getPcode());
            if (count > 0) {
                flag = false;
            } else {
                flag = true;
            }
        } else {
            Organization byId = organizationDao.findById(pbOrganization.getId());
            String pcode = byId.getPCode();
            if (pcode.equals(pbOrganization.getPcode())) {
                flag = true;
            } else {
                int count = organizationDao.findCountByPcode(pbOrganization.getPcode());
                if (count > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        }
        baseVo.setData(flag);
        return baseVo;
    }

    /**
     * 删除根据二进制
     *
     * @param strId
     * @return
     */
    @Override
	@Transactional
    public void sysDelete(String strId) {
        Long id = RadixUtil.toLong(strId);
        //添加校验该党组织下是否有党员的校验
		List<PartyMember> membersByOrgId = partyMemberDao.findMembersByOrgId(id);
		if (membersByOrgId!=null && membersByOrgId.size()>0){
			throwBusinessException("该党组织下存在党员不能删除");
		}
		organizationDao.delete(String.valueOf(id));

    }


    /**
     * 递归查询
     *
     * @param allByParam 所有数据
     * @param id         二进制的string——id
     * @param i          等级
     * @return
     */
    private List<PbOrgTreeVO> getChild(List<PbOrgTreeVO> allByParam, String id, int i) {
        List<PbOrgTreeVO> pbOrganizations = new ArrayList<>();
        for (PbOrgTreeVO pbOrganization : allByParam) {
            int y = i + 1;
            if (pbOrganization.getLevel() == y) {
                String str = id.substring(0, i * 8);
                String pbstr = pbOrganization.getStrId().substring(0, i * 8);
                if (str.equals(pbstr)) {
                    pbOrganizations.add(pbOrganization);
                }
            }
        }
        for (PbOrgTreeVO pbOrganization : pbOrganizations) {
            pbOrganization.setChildList(getChild(allByParam, pbOrganization.getStrId(), pbOrganization.getLevel()));
        }
        if (pbOrganizations.size() == 0) {
            return null;
        }
        return pbOrganizations;
    }

    /**
     * 根据机构id查询下级机构的数量
     *
     * @param id
     * @param includeItself
     * @return
     */
    @Override
    public Integer findLowerLevelById(String id, Boolean includeItself) {
        return organizationDao.findLowerLevelById(id, includeItself);
    }

    /**
     * 通过二进制strid查询单个PbOrganization
     *
     * @param strId
     * @return
     */
    @Override
    public BaseVo sysFindById(String strId) {
        Long id = RadixUtil.toLong(strId);
        Organization pbOrganization = organizationDao.findById(String.valueOf(id));
        PbOrganizationVO vo = new PbOrganizationVO();
        BeanUtils.copyProperties(pbOrganization, vo);
        //vo.setId(Long.valueOf(pbOrganization.getId()));
        vo.setId(String.valueOf(pbOrganization.getId()));
        vo.setPcode(pbOrganization.getPCode());
        vo.setStrId(strId);
        //判断一下等级，如果该对象是顶级，则不再查询上一级
        if (RadixUtil.getlevel(strId) != 1) {
            PbOrganizationVO subObj = organizationDao.findSubObjById(id);
            //String subStrId = RadixUtil.toFullBinaryString(subObj.getId());
            String subStrId = RadixUtil.toFullBinaryString(Long.valueOf(subObj.getId()));
            vo.setStrPid(subStrId);
            //vo.setPid(subObj.getId());
            vo.setPname(subObj.getName());
        }
        //vo.setIdStr(pbOrganization.getId());
        BaseVo baseVo = new BaseVo();
        baseVo.setData(vo);
        return baseVo;
    }

    /**
     * 根据sys的党组织id 查询pb的党组织信息
     *
     * @param ids
     * @return
     */
    @Override
    public BaseVo findOrganizationsByIds(List<Long> ids) {
        List<Organization> organizations = organizationDao.batchSelectByIds(ids);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(organizations);
        return baseVo;
    }

    /**
     * 查询所有党组织
     *
     * @return
     */
    @Override
    public List<Organization> queryAll() {
        List<Organization> all = organizationDao.findAll(null);
        return all;
    }

    /**
     * 根据参数查询
     *
     * @param organization
     * @return
     */
    @Override
    public List<Organization> findByParam(Organization organization) {
        List<Organization> all = organizationDao.findByParam(organization);
        return all;
    }

    /**
     * 根据组织id查询激活的党组织
     *
     * @param ids
     * @return
     */
    @Override
    public List<Organization> batchSelectByIdsAndActive(List<Long> ids) {
        List<Organization> organizations = organizationDao.batchSelectByIdsAndActive(ids);
        return organizations;
    }
    /**
     * 根据基层获取支部列表
     *
     * @param orgId
     * @return
     */
    @Override
    public List<Organization> findByBasicUnit(String orgId) {
        if (StringUtil.isEmpty(orgId)) {
            orgId = getCurrentUserInfo().getPbOrgId();
        }
        // 获取值的最大范围
        String maxOrgId = RadixUtil.getLevelEndID(orgId);
        // 获取该基层的支部列表
        List<Organization> organizations = organizationDao.findByBasicUnit(orgId, maxOrgId);
//        if (organizations.size() > 0) {
//            // 基层的级别
//            int basic_level = RadixUtil.getLevel(orgId, true);
//            List<Organization> organizationList = new ArrayList<>();
//            List<Organization> next_organizationList = new ArrayList<>();
//            // 判断级别
//            organizations.stream().forEach(organization -> {
//                int level = RadixUtil.getLevel(organization.getId(), true);
//                // 下一级支部
//                if (level - basic_level == 1) {
//                    organizationList.add(organization);
//                }
//                // 下下一级支部
//                if (level - basic_level == 2) {
//                    next_organizationList.add(organization);
//                }
//            });
//            if (next_organizationList.size() > 0) {
//                organizationList.stream().forEach(organization -> {
//                    String orgId_ = organization.getId();
//                    String maxOrgId_ = RadixUtil.getLevelEndID(organization.getId());
//                    next_organizationList.stream().forEach(organization1 -> {
//                        Long next_orgId_ = Long.valueOf(organization1.getId());
//                        if (Long.valueOf(orgId_) < next_orgId_ && next_orgId_ < Long.valueOf(maxOrgId_)) {
//                            // 移除上级
//                            organizations.remove(organization);
//                        }
//                    });
//                });
//            }
//        }
        return organizations;
    }

    /**
     * 查询当前用户所在的党组织
     * @return
     */
    @Override
    public Organization findByLocalOrgId() {
        return organizationDao.findById(getCurrentUserInfo().getPbOrgId());
    }
	/**
	 * 获取全部支部节点
	 *
	 * @return
	 */
    @Override
    public List<OrganizationInfoVO> getBranchOrganization() {
        String orgId = getCurrentUserInfo().getPbOrgId();
        List<Organization> all = organizationDao.findAll(null);
        List<OrganizationInfoVO> organizationInfoVOS = new ArrayList<>();
        all.stream().forEach(org -> {
            if (org.getOrgType() == 3 && !org.getId().equals(orgId)) {
                OrganizationInfoVO organizationInfoVO = new OrganizationInfoVO();
                organizationInfoVO.setId(org.getId());
                organizationInfoVO.setName(org.getName());
                organizationInfoVO.setOrgType(org.getOrgType());
                organizationInfoVOS.add(organizationInfoVO);
            }
        });
        return organizationInfoVOS;
    }
	/**
	 * 根据名称获取组织信息
	 *
	 * @param name
	 * @return
	 */
    @Override
    public OrganizationInfoVO findByName(String name) {
        return organizationDao.findByName(name);
    }

}
