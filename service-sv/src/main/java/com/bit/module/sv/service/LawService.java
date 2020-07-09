package com.bit.module.sv.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Law;
import com.bit.module.sv.vo.LawVO;

public interface LawService {

    /**
     * 新增法律
     * @param lawVO
     * @return
     */
    void addLaw(LawVO lawVO);

    /**
     * 法律分页查询
     * @param lawVO
     * @return
     */
    BaseVo listLaws(LawVO lawVO);

    /**
     * 删除法律
     * @param id
     */
    void deleteById(Long id);

    /**
     * 更新法律
     * @param law
     */
    void update(Law law);

    /**
     * 查询所有法律
     * @return
     * @param range
     */
    BaseVo allLaws(Integer range);
}
