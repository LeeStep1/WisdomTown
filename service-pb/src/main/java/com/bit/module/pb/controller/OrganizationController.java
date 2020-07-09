package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.bean.OrganizationParam;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.OrganizationVO;
import com.bit.module.pb.vo.PbOrgTreeVO;
import com.bit.module.pb.vo.PbOrganizationVO;
import com.bit.utils.RadixUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Organization的相关请求
 *
 * @author generator
 */
@RestController
@RequestMapping(value = "/organization")
public class OrganizationController {
    private static final Logger logger = LoggerFactory.getLogger(OrganizationController.class);
    @Autowired
    private OrganizationService organizationService;

    /**
     * 分页查询Organization列表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody OrganizationVO organizationVO) {
        //分页对象，前台传递的包含查询的参数
        return organizationService.findByConditionPage(organizationVO);
    }

    /**
     * 根据主键ID查询Organization
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") String id) {

        Organization organization = organizationService.findById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(organization);
        return baseVo;
    }

    /**
     * 新增Organization
     *
     * @param organization Organization实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody Organization organization) {
        organizationService.add(organization);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 修改Organization
     *
     * @param organization Organization实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody Organization organization) {
        organizationService.update(organization);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据主键ID删除Organization
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") String id) {
        organizationService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据id查询组织的直接下级
     *
     * @param organization
     * @return
     */
    @PostMapping("/sub/get")
    public BaseVo getSubOrganizationIgnoreItself(@RequestBody Organization organization) {
        List<OrganizationInfoVO> organizations = organizationService.getDirectSubOrganization(organization.getId());
        BaseVo<List<OrganizationInfoVO>> organizationBaseVo = new BaseVo<>();
        organizationBaseVo.setData(organizations);
        return organizationBaseVo;
    }

    /**
     * 获取全部支部节点
     *
     * @return
     */
    @PostMapping("/branch/get")
    public BaseVo getBranchOrganization() {
        List<OrganizationInfoVO> branchOrg = organizationService.getBranchOrganization();
        return new BaseVo(branchOrg);
    }

    /**
     * 获取下级组织包括自己
     *
     * @param orgId
     * @return
     */
    @GetMapping("/getSubOrgNoIgnoreItself/{orgId}")
    public BaseVo getSubOrgNoIgnoreItself(@PathVariable(value = "orgId") String orgId) {

        return organizationService.getSubOrgNoIgnoreItself(orgId);
    }

    /**
     * 判断机构层级
     *
     * @param id
     * @return
     */
    @GetMapping("/level/{id}")
    public BaseVo level(@PathVariable(value = "id") Long id) {
        String str = RadixUtil.toFullBinaryString(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(RadixUtil.getlevel(str));
        return baseVo;
    }

    /**
     * 查询所有的机构层级 树形结构
     *
     * @param
     * @return
     */
    @GetMapping("/tree")
    public BaseVo tree() {
        BaseVo baseVo = new BaseVo();
        List<OrganizationParam> tree = organizationService.tree();
        baseVo.setData(tree);
        return baseVo;
    }

    /**
     * 根据机构id查询树形结构
     *
     * @param id
     * @return
     */
    @GetMapping("/tree/{id}")
    public BaseVo treeById(@PathVariable(value = "id") Long id) {
        BaseVo baseVo = new BaseVo();

        List<OrganizationParam> tree = organizationService.treeById(id);
        baseVo.setData(tree);
        return baseVo;
    }


    /**
     * 分页查询PbOrganization列表
     *
     * @param organizationVO (pageSize,pageNum,pCode,name,orgType)
     * @return
     */
    @PostMapping("/sysListPage")
    public BaseVo syslistPage(@RequestBody OrganizationVO organizationVO) {
        return organizationService.sysFindByConditionPage(organizationVO);
    }


    /**
     * 新增PbOrganization
     *
     * @param pbOrganizationVO 实体
     * @return
     */
    @PostMapping("/sysAdd")
    public BaseVo add(@Valid @RequestBody PbOrganizationVO pbOrganizationVO) {
        organizationService.sysAdd(pbOrganizationVO);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }


    /**
     * 修改PbOrganization
     *
     * @param pbOrganizationVO PbOrganization实体
     * @return
     */
    @PostMapping("/sysModify")
    public BaseVo sysModify(@Valid @RequestBody PbOrganizationVO pbOrganizationVO) {
        organizationService.sysUpdate(pbOrganizationVO);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }


    /**
     * 查询树 ---党建组织
     *
     * @param organizationVO
     * @return
     */
    @PostMapping("/sysFindTreeByParam")
    public BaseVo sysFindTreeByParam(@RequestBody OrganizationVO organizationVO) {
        List<PbOrgTreeVO> list = organizationService.findTreeByParam(organizationVO);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(list);
        return baseVo;
    }


    /**
     * 校验 组织编码
     *
     * @param pbOrganization
     * @return
     */
    @PostMapping("/sysCheckPcode")
    public BaseVo checkPcode(@RequestBody PbOrganizationVO pbOrganization) {
        return organizationService.checkPcode(pbOrganization);
    }


    /**
     * 根据主键ID删除PbOrganization
     *
     * @param strId
     * @return
     */
    @GetMapping("/sysDelete/{strId}")
    public BaseVo sysDelete(@PathVariable(value = "strId") String strId) {
        organizationService.sysDelete(strId);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    @GetMapping("/sysFindById/{strId}")
    public BaseVo sysFindById(@PathVariable(value = "strId") String strId) {
        return organizationService.sysFindById(strId);
    }

    /**
     * 根据机构id查询下级机构的数量
     *
     * @param id
     * @param includeItself
     * @return
     */
    @GetMapping("/findLowerLevelById/{id}/{includeItself}")
    public Integer findLowerLevelById(@PathVariable(value = "id") String id, @PathVariable(value = "includeItself") Boolean includeItself) {
        return organizationService.findLowerLevelById(id, includeItself);
    }

    /**
     * 根据sys的党组织id 查询pb的党组织信息
     *
     * @param ids
     * @return
     */
    @PostMapping("/findOrganizationsByIds")
    public BaseVo findOrganizationsByIds(@RequestBody List<Long> ids) {
        return organizationService.findOrganizationsByIds(ids);

    }

    /**
     * 查询所有党组织
     *
     * @return
     */
    @PostMapping("/queryAll")
    public List<Organization> queryAll() {
        return organizationService.queryAll();
    }

    /**
     * 根据参数查询
     *
     * @param organization
     * @return
     */
    @PostMapping("/findByParam")
    public List<Organization> findByParam(@RequestBody Organization organization) {
        return organizationService.findByParam(organization);
    }

    /**
     * 根据id批量查询党组织
     *
     * @param ids
     * @return
     */
    @PostMapping("/batchSelectByIdsAndActive")
    public List<Organization> batchSelectByIdsAndActive(@RequestBody List<Long> ids) {
        return organizationService.batchSelectByIdsAndActive(ids);
    }

    /**
     * 根据基层获取支部列表
     *
     * @param orgId
     * @return
     */
    @PostMapping("/findByBasicUnit")
    public BaseVo findByBasicUnit(@RequestParam(value = "orgId", required = false) String orgId) {
        List<Organization> organizations = organizationService.findByBasicUnit(orgId);
        // 支部
        if (organizations.size() == 0) {
            Organization organization = organizationService.findByLocalOrgId();
            organizations.add(organization);
        }
        return new BaseVo(organizations);
    }

    /**
     * web端党组织下拉框获取下级组织包括自己
     *
     * @param orgId
     * @return
     */
    @GetMapping("/getSubOrgNoIgnoreItselfForWeb/{orgId}")
    public BaseVo getSubOrgNoIgnoreItselfForWeb(@PathVariable(value = "orgId") String orgId) {

        return organizationService.getSubOrgNoIgnoreItselfForWeb(orgId);
    }

}
