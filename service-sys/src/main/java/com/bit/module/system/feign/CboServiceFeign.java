package com.bit.module.system.feign;

import com.bit.base.dto.Community;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "service-cbo")
public interface CboServiceFeign {
	/**
	 * 批量查询小区信息
	 * @param orgIds
	 * @return
	 */
	@RequestMapping(value = "/community/batchSelectByOrgIds",method = RequestMethod.POST)
	List<Community> batchSelectByOrgIds(@RequestBody List<Long> orgIds);
}
