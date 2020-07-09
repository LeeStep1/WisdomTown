package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.bean.OrganizationParam;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.OrganizationVO;
import com.bit.module.pb.vo.PbOrgTreeVO;
import com.bit.module.pb.vo.PbOrganizationVO;

import java.util.List;

/**
 * Organization的Service
 *
 * @author codeGenerator
 */
public interface OrganizationService {
    /**
     * 根据条件查询Organization
     *
     * @param organizationVO
     * @return
     */
    BaseVo findByConditionPage(OrganizationVO organizationVO);

    /**
     * 查询所有Organization
     *
     * @param sorter 排序字符串
     * @return
     */
    List<Organization> findAll(String sorter);

    Organization findDirectSuperiorById(String orgId);

    /**
     * 通过主键查询单个Organization
     *
     * @param id
     * @return
     */
    Organization findById(String id);

    /**
     * 批量保存Organization
     *
     * @param organizations
     */
    void batchAdd(List<Organization> organizations);

    /**
     * 保存Organization
     *
     * @param organization
     */
    void add(Organization organization);

    /**
     * 批量更新Organization
     *
     * @param organizations
     */
    void batchUpdate(List<Organization> organizations);

    /**
     * 更新Organization
     *
     * @param organization
     */
    void update(Organization organization);

    /**
     * 删除Organization
     *
     * @param id
     */
    void delete(String id);

    /**
     * 批量删除Organization
     *
     * @param ids
     */
    void batchDelete(List<String> ids);

    /**
     * 获取下级组织
     *
     * @param orgId
     * @return
     */
    List<OrganizationInfoVO> getDirectSubOrganization(String orgId);

    /**
     * 获取下级组织包括自己
     *
     * @param orgId
     * @return
     */
    BaseVo getSubOrgNoIgnoreItself(String orgId);


    /**
     * web端党组织下拉框获取下级组织包括自己
     *
     * @param orgId
     * @return
     */
    BaseVo getSubOrgNoIgnoreItselfForWeb(String orgId);

    /**
     * 查询所有的机构层级 树形结构
     *
     * @param
     * @return
     */
    List<OrganizationParam> tree();

    /**
     * 根据机构id查询树形结构
     *
     * @param id
     * @return
     */
    List<OrganizationParam> treeById(Long id);

    /**
     * 根据条件查询Organization
     *
     * @param organizationVO
     * @return
     */
    BaseVo sysFindByConditionPage(OrganizationVO organizationVO);

    /**
     * 根据条件查询Organization
     *
     * @param pbOrganizationVO
     * @return
     */
    public void sysAdd(PbOrganizationVO pbOrganizationVO);

    /**
     * 管理平台进行更新
     *
     * @param pbOrganizationVO
     * @return
     */
    public void sysUpdate(PbOrganizationVO pbOrganizationVO);

    /**
     * 查询树
     *
     * @param organizationVO
     * @return
     */
    public List<PbOrgTreeVO> findTreeByParam(OrganizationVO organizationVO);


    /**
     * 校验组织编码
     *
     * @param pbOrganization
     * @return
     */
    BaseVo checkPcode(PbOrganizationVO pbOrganization);

    /**
     * 删除根据二进制
     *
     * @param strId
     * @return
     */
    void sysDelete(String strId);

    /**
     * 根据机构id查询下级机构的数量
     *
     * @param id
     * @param includeItself
     * @return
     */
    Integer findLowerLevelById(String id, Boolean includeItself);


    /**
     * 通过二进制strid查询单个PbOrganization
     *
     * @param strId
     * @return
     */
    BaseVo sysFindById(String strId);

    /**
     * 根据sys的党组织id 查询pb的党组织信息
     *
     * @param ids
     * @return
     */
    BaseVo findOrganizationsByIds(List<Long> ids);

    /**
     * 查询所有党组织
     *
     * @return
     */
    List<Organization> queryAll();

    /**
     * 根据参数查询
     *
     * @param organization
     * @return
     */
    List<Organization> findByParam(Organization organization);

    /**
     * 根据id批量查询党组织
     *
     * @param ids
     * @return
     */
    List<Organization> batchSelectByIdsAndActive(List<Long> ids);

    /**
     * 根据基层ID 获取该基层的支部列表
     *
     * @param orgId
     * @return
     */
    List<Organization> findByBasicUnit(String orgId);

    /**
     * 获取当前用户组织
     *
     * @return
     */
    Organization findByLocalOrgId();

    /**
     * 获取全部支部信息
     *
     * @return
     */
    List<OrganizationInfoVO> getBranchOrganization();

    /**
     * 根据名称获取组织信息
     *
     * @param name
     * @return
     */
    OrganizationInfoVO findByName(String name);

}
