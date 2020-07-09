package com.bit.module.vol.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Dict;
import com.bit.module.vol.vo.DictVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-19 9:12
 */

@FeignClient(value = "service-sys")
public interface SysServiceFeign {
    /**
     * 字典表按模块名查询
     * @param module
     * @return
     */
    @RequestMapping(value = "/dict/findByModule/{module}",method = RequestMethod.GET)
    BaseVo findByModule(@PathVariable("module") String module);

    /**
     * 用户志愿者站点表 按照用户id查询
     * @param userId
     * @return
     */
    @RequestMapping(value = "/userRelVolStation/queryByUserId/{userId}",method = RequestMethod.GET)
    BaseVo queryByUserId(@PathVariable(value = "userId") Long userId);

    /**
     * 批量查询字典
     * @param dictVO
     * @return
     */
    @RequestMapping(value = "/dict/findByModuleAndCodes",method = RequestMethod.POST)
    BaseVo findByModuleAndCodes(@RequestBody DictVO dictVO);
    /**
     * 根据模块和code查询，用于回显
     * @param dict
     * @return
     */
    @RequestMapping(value = "/dict/findByModuleAndCode",method = RequestMethod.POST)
    BaseVo findByModuleAndCode(@RequestBody Dict dict);

    /**
     * 根据身份证查询用户
     * @param idcard
     * @return
     */
    @RequestMapping(value = "/user/queryUserByIdcard/{idcard}",method = RequestMethod.GET)
    BaseVo queryUserByIdcard(@PathVariable(value = "idcard") String idcard);

    /**
     * 用户志愿者站点表 按照服务站id查询
     * @param stationId
     * @return
     */
    @RequestMapping(value = "/userRelVolStation/queryAllUserBystationId/{stationId}",method = RequestMethod.GET)
    List<Long> queryAllUserBystationId(@PathVariable(value = "stationId") Long stationId);

    /**
     * 批量查询志愿者 根据身份证号
     * @param idlist
     * @return
     */
    @RequestMapping(value = "/user/batchSelectByCardId",method = RequestMethod.POST)
    List<Long> batchSelectByCardId(@RequestBody List<String> idlist);

}
