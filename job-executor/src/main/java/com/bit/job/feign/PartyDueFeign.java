package com.bit.job.feign;

import com.bit.base.vo.BaseVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-pb")
public interface PartyDueFeign {

    @GetMapping(value = "/partyDue/generate")
    BaseVo generatePartyDueMonthly();

    @GetMapping(value = "/monthlyPartyDue/subOrganization/{year}/{month}/generate")
    BaseVo generateMonthlyPartyDueForSubordinates(@PathVariable Integer year, @PathVariable Integer month);
}
