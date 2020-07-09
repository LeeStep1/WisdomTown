package com.bit.module.oa.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.vo.user.BusinessRolePage;
import com.bit.module.oa.vo.user.UserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @Description :
 * @Date ： 2019/1/25 15:02
 */
@FeignClient(value = "service-sys")
public interface SysServiceFeign {
    @RequestMapping(value = "/user/listByAppIdAndIds", method = RequestMethod.POST)
    BaseVo<List<UserVO>> listByAppIdAndIds(@RequestBody Map userVO);

    @PostMapping(value = "/oaDepartment/getAll")
    BaseVo<List<UserVO>> getAll();

    @GetMapping("/user/query/{id}")
    BaseVo<UserVO> queryByUserId(@PathVariable(value = "id") Long id);

    @PostMapping("/flow/queryUserByRoleBatch")
    BaseVo<List<UserVO>> queryUserByRoleBatch(@RequestBody List<Long> roleIds);

    @GetMapping("/flow/reflect/{flowId}")
    BaseVo<BusinessRolePage> reflectByFlowId(@PathVariable(value = "flowId") Integer flowId);
}
