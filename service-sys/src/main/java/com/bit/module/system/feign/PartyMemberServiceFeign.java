package com.bit.module.system.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liuyancheng
 * @create 2019-01-19 13:48
 */
@FeignClient(value = "service-pb")
public interface PartyMemberServiceFeign {

    @RequestMapping(value = "/partyMember/exist",method = RequestMethod.GET)
    BaseVo existMember(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "idCard", required = false) String idCard);

	/**
	 * 身份证+状态查询党员信息
	 * @param idCard
	 * @return
	 */
    @RequestMapping(value = "/partyMember/findByIdCardWithStatus/{idCard}",method = RequestMethod.GET)
    BaseVo findByIdCardWithStatus(@PathVariable(value = "idCard") String idCard);
	/**
	 * 身份证查询党员信息
	 * @param idCard
	 * @return
	 */
    @RequestMapping(value = "/partyMember/findByIdCard/{idCard}",method = RequestMethod.GET)
    BaseVo findByIdCard(@PathVariable(value = "idCard") String idCard);
}
