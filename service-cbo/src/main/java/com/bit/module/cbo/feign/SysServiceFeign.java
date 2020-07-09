package com.bit.module.cbo.feign;

import com.bit.base.dto.OaOrganization;
import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.vo.Dict;
import com.bit.module.cbo.vo.DictVO;
import com.bit.module.cbo.vo.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author chenduo
 * @create 2019-01-21 16:01
 */
@FeignClient(value = "service-sys")
public interface SysServiceFeign {

    /**
     * 批量查询社区信息
     * @param ids
     * @return
     */
    @RequestMapping(value = "/oaDepartment/batchSelectByIds",method = RequestMethod.POST)
    List<OaOrganization> batchSelectOaDepartmentByIds(@RequestBody List<Long> ids);

    /**
     * 根据手机号查询居委会账号
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/user/getUserByMobile",method = RequestMethod.POST)
	User getUserByMobile(@RequestBody String mobile);

    /**
     * 社区使用 更新居委会账号密码
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/updateOrgPwd",method = RequestMethod.POST)
    Boolean updateOrgPwd(@RequestBody User user);

    /**
     * 根据模块查询字典
     * @param module
     * @return
     */
    @RequestMapping(value = "/dict/findByModule/{module}",method = RequestMethod.GET)
    BaseVo findByModule(@PathVariable(value = "module") String module);

    /**
     * 根据模块和codes查询，用于批量查询
     * @param dictVO
     * @return
     */
    @RequestMapping(value = "/dict/findByModules",method = RequestMethod.POST)
    BaseVo findByModules(@RequestBody Dict dict);

    /**
     * 获取社区信息
     * @return
     */
    @RequestMapping(value = "/oaDepartment/getCommunity",method = RequestMethod.POST)
    BaseVo getCommunity();

    /**
     * 获取社区管理员
     * @param orgId
     * @return
     */
    @RequestMapping(value = "/user/getUserByCboDep/{orgId}",method = RequestMethod.GET)
    List<User> getUserByCboDep(@PathVariable(value = "orgId") Long orgId);
}
