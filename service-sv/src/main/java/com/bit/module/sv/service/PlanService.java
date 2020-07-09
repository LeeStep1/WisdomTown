package com.bit.module.sv.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.vo.PlanVO;

public interface PlanService {
    BaseVo addPlan(PlanVO planVO);

    BaseVo listPlans(PlanVO planVO);

    BaseVo deleteById(Long id);

    BaseVo modifyById(PlanVO planVO);

    BaseVo selectById(Long id);
}
