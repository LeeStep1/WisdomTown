package com.bit.module.pb.dao;

import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.OrganizationVO;
import com.bit.module.pb.vo.PbOrganizationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Organization管理的Dao
 *
 * @author
 */
@Repository
public interface OrganizationDao {
    /**
     * 根据条件查询Organization
     *
     * @param organizationVO
     * @return
     */
    List<Organization> findByConditionPage(OrganizationVO organizationVO);

    /**
     * 查询所有Organization
     *
     * @return
     */
    List<Organization> findAll(@Param(value = "sorter") String sorter);

    /**
     * 通过主键查询单个Organization
     *
     * @param id
     * @return
     */
    Organization findById(@Param(value = "id") String id);
    /**
     * 批量保存Organization
     * @param organizations
     */
    //void batchAdd(List<Organization> organizations);

    /**
     * 保存Organization
     *
     * @param organization
     */
    void add(Organization organization);
    /**
     * 批量更新Organization
     * @param organizations
     */
    //void batchUpdate(List<Organization> organizations);

    /**
     * 更新Organization
     *
     * @param organization
     */
    void update(Organization organization);

    /**
     * 删除Organization
     *
     * @param ids
     */
    void batchDelete(List<String> ids);

    /**
     * 删除Organization
     *
     * @param id
     */
    void delete(@Param(value = "id") String id);

    /**
     * 查询所有基层单位
     *
     * @return
     */
    List<Organization> findAllGrassRootsUnits();

    /**
     * 根据id查询组织的下级
     *
     * @return
     */
    List<Organization> findSubordinatesById(@Param(value = "id") String id, @Param(value = "includeItself") Boolean includeItself);

    /**
     * web端党组织下拉框根据id查询组织的下级
     *
     * @return
     */
    List<Organization> findSubordinatesByIdForWeb(@Param(value = "id") String id, @Param(value = "includeItself") Boolean includeItself);

    /**
     * 根据id查询组织的直接下级
     *
     * @param id
     * @return
     */
    List<OrganizationInfoVO> findDirectSubordinatesInfoById(@Param(value = "id") String id);

    /**
     * 根据id查询直属上级组织
     *
     * @param id
     * @param isApprovalAuz
     * @return
     */
    Organization findDirectSuperiorById(@Param(value = "id") String id, @Param("isApprovalAuz") Boolean isApprovalAuz);

    /**
     * 在组织下添加子组织
     *
     * @param organization
     */
    void nestedAdd(Organization organization);

    /**
     * 级联删除
     *
     * @param id
     */
    void cascadeDelete(@Param(value = "id") String id);

    /**
     * 查询顶级机构
     *
     * @return
     */
    Long findTopOrg();

    /**
     * 添加根节点
     *
     * @return
     */

    void addRootNode(Organization organization);

    /**
     * 添加子节点
     *
     * @return
     */
    void addChildNode(PbOrganizationVO pbOrganizationVO);


    /**
     * 根据pcode 查询统计
     *
     * @param pCode
     * @return
     */
    int findCountByPcode(@Param(value = "pCode") String pCode);

    /**
     * 根据机构id查询下级机构的数量
     *
     * @param id
     * @param includeItself
     * @return
     */
    int findLowerLevelById(@Param(value = "id") String id, @Param(value = "includeItself") Boolean includeItself);

    /**
     * 查询父亲对象
     *
     * @param id
     * @return
     */
    PbOrganizationVO findSubObjById(Long id);

    /**
     * 根据sys的党组织id 查询pb的党组织信息
     *
     * @param ids
     * @return
     */
    List<Organization> batchSelectByIds(@Param(value = "ids") List<Long> ids);

    /**
     * 根据大于党组织id查询党组织信息
     *
     * @param orgId
     * @return
     */
    List<Organization> findOrgByBiggerThanOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 根据参数查询
     *
     * @param organization
     * @return
     */
    List<Organization> findByParam(Organization organization);

    /**
     * 根据sys的党组织id 查询pb的党组织信息
     *
     * @param ids
     * @return
     */
    List<Organization> batchSelectByIdsAndActive(@Param(value = "ids") List<Long> ids);

    /**
     * 系统管理分页查询党组织
     *
     * @param organizationVO
     * @return
     */
    List<PbOrganizationVO> sysFindByConditionPage(OrganizationVO organizationVO);

    /**
     * 获取基层下面的支部列表
     *
     * @param orgId
     * @param maxOrgId
     * @return
     */
    List<Organization> findByBasicUnit(@Param(value = "orgId") String orgId, @Param(value = "maxOrgId") String maxOrgId);


    OrganizationInfoVO findByName(@Param(value = "name") String name);

    /**
     * 获取一系列组织ID
     *
     * @param orgIds
     * @return
     */
    List<String> seriesOrganizations(@Param(value = "orgIds") List<String> orgIds);
}
