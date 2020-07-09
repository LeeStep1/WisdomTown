package com.bit.module.pb.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.vo.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-16 13:18
 */
@FeignClient(value = "service-sys")
public interface SysServiceFeign {

    @RequestMapping(value = "/userRelPbOrg/queryAllUserByPbOrgId/{pbOrgId}",method = RequestMethod.GET)
    List<Long> queryAllUserByPbOrgId(@PathVariable(value = "pbOrgId")String pbOrgId);


    @RequestMapping(value = "/userRelPbOrg/queryAllUserByPbOrgIds", method = RequestMethod.POST)
    List<Long> queryAllUserByPbOrgIds(@RequestBody List<String> pbOrgIds);

    /**
     * 批量查询志愿者 根据身份证号
     * @param idlist
     * @return
     */
    @RequestMapping(value = "/user/batchSelectByCardId",method = RequestMethod.POST)
    List<Long> batchSelectByCardId(@RequestBody List<String> idlist);

    /**
     * 根据身份id查询身份信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/identity/query/{id}",method = RequestMethod.GET)
    BaseVo findById(@PathVariable(value = "id") Long id);

    /**
     * 批量查询党建管理员的身份证
     * @param orgIds
     * @return
     */
    @RequestMapping(value = "/userRelPbOrg/findUserIdCardByOrgIds",method = RequestMethod.POST)
    List<String> findUserIdCardByOrgIds(@RequestBody List<Long> orgIds);
    /**
     * 根据党组织id查询用户身份证
     * @param pbOrgId
     * @return
     */
    @RequestMapping(value = "/userRelPbOrg/queryUserIdCardByOrgId/{pbOrgId}",method = RequestMethod.GET)
    List<String> queryUserIdCardByOrgId(@PathVariable(value = "pbOrgId")Long pbOrgId);

    /**
     * 批量查询党建管理员的信息
     * @param orgIds
     * @return
     */
    @RequestMapping(value = "/userRelPbOrg/findUserByOrgIds",method = RequestMethod.POST)
    List<UserVO> findUserByOrgIds(@RequestBody List<Long> orgIds);

    /**
     * 字典表按模块名查询
     * @param module
     * @return
     */
    @RequestMapping(value = "/dict/findByModule/{module}",method = RequestMethod.GET)
    BaseVo findByModule(@PathVariable("module") String module);


}
