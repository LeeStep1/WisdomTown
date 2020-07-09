package com.bit.module.system.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.OrganizationPb;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-10 13:29
 */
@FeignClient(value = "service-pb")
public interface PbServiceFeign {
    /**
     * 单查会议信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/meeting/reflect/{id}",method = RequestMethod.GET)
    BaseVo pbConferenceReflect(@PathVariable(value = "id")Long id);
    /**
     * 单查学习计划信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/study/findById/{id}",method = RequestMethod.GET)
    BaseVo pbStudyReflect(@PathVariable(value = "id")Long id);

    /**
     * 单查党组织信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/organization/query/{id}",method = RequestMethod.GET)
    BaseVo query(@PathVariable(value = "id")String id);

    /**
     * 查询下一层级党组织id
     * @param id
     * @param includeItself
     * @return
     */
    @RequestMapping(value = "/organization/findLowerLevelById/{id}/{includeItself}",method = RequestMethod.GET)
    Integer findLowerLevelById(@PathVariable(value = "id")String id,@PathVariable(value = "includeItself")Boolean includeItself);

    /**
     * 按照党组织id查询
     * @param ids
     * @return
     */
    @RequestMapping(value = "/organization/findOrganizationsByIds",method = RequestMethod.POST)
    BaseVo findOrganizationsByIds(@RequestBody List<Long> ids);

    /**
     * 查询所有党组织
     * @return
     */
    @RequestMapping(value = "/organization/queryAll",method = RequestMethod.POST)
    List<OrganizationPb> queryAll();

    /**
     * 根据党组织参数查询
     * @param organizationPb
     * @return
     */
    @RequestMapping(value = "/organization/findByParam",method = RequestMethod.POST)
    List<OrganizationPb> findByParam(OrganizationPb organizationPb);

    /**
     * 查询激活的党组织
     * @param ids
     * @return
     */
    @RequestMapping(value = "/organization/batchSelectByIdsAndActive",method = RequestMethod.POST)
    List<OrganizationPb> batchSelectByIdsAndActive(@RequestBody List<Long> ids);
}
