package com.bit.module.pb.service;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.vo.partyMember.PartyMemberApprovalVO;
import com.bit.module.pb.vo.partyMember.PartyMemberParams;
import com.bit.module.pb.vo.partyMember.PartyMemberQuery;

/**
 * PartyMemberApproval的Service
 *
 * @author codeGenerator
 */
public interface PartyMemberApprovalService {
    /**
     * 根据条件查询PartyMemberApproval
     *
     * @param partyMemberQuery
     * @return
     */
    BaseVo findByConditionPage(PartyMemberQuery partyMemberQuery);

    /**
     * 获取停用党员信息
     *
     * @param partyMemberQuery
     * @return
     */
    BaseVo findByDisablePage(PartyMemberQuery partyMemberQuery);

    /**
     * 通过主键查询单个PartyMemberApproval
     *
     * @param id
     * @return
     */
    PartyMemberApprovalVO findById(Long id);

    /**
     * 保存PartyMemberApproval
     *
     * @param partyMemberParams
     */
    void add(PartyMemberParams partyMemberParams) throws BusinessException;

    /**
     * 提交党员申请
     *
     * @param id
     */
    void submit(Long id);

    /**
     * 退回申请
     *
     * @param id
     */
    void sendBack(Long id, String reason);

    /**
     * 通过审核
     *
     * @param id
     */
    PartyMemberApprovalVO pass(Long id);

    /**
     * 删除PartyMemberApproval
     *
     * @param id
     */
    void delete(Long id);

}
