package com.bit.module.pb.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.vo.partyMember.*;

import java.util.List;

/**
 * RelationshipTransfer的Service
 *
 * @author codeGenerator
 */
public interface RelationshipTransferService {

    /**
     * 获取本组织申请的列表
     *
     * @param partyMemberQuery
     * @return
     */
    BaseVo queryPage(PartyMemberQuery partyMemberQuery, Integer type);

    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param partyMemberQuery
     * @return
     */
    BaseVo findByConditionPage(PartyMemberQuery partyMemberQuery);

    /**
     * 获取待审核的申请
     *
     * @param partyMemberQuery
     * @return
     */
    BaseVo queryAuditRelationship(PartyMemberQuery partyMemberQuery);

    /**
     * 镇内组织关系转移
     *
     * @param partyMemberQuery
     * @return
     */
    BaseVo findByTransferPage(PartyMemberQuery partyMemberQuery);

    /**
     * 根据身份证获取转移信息
     *
     * @param idCard
     * @return
     */
    List<RelationshipTransferInfo> findByIdCard(String idCard);

    /**
     * 通过主键查询单个RelationshipTransfer
     *
     * @param id
     * @return
     */
    RelationshipTransferVO findById(Long id);

    /**
     * 保存RelationshipTransfer
     *
     * @param partyMemberParams
     */
    void add(PartyMemberParams partyMemberParams);

    /**
     * 审核流程
     *
     * @param id
     * @param status
     */
    void updateFlow(Long id, Integer status, String reason);

    /**
     * 删除RelationshipTransfer
     *
     * @param id
     */
    void delete(Long id);

}
