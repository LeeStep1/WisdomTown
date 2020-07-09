package com.bit.module.pb.dao;

import com.bit.module.pb.bean.RelationshipTransfer;
import com.bit.module.pb.vo.partyMember.PartyMemberQuery;
import com.bit.module.pb.vo.partyMember.RelationshipTransferInfo;
import com.bit.module.pb.vo.partyMember.RelationshipTransferPageVO;
import com.bit.module.pb.vo.partyMember.RelationshipTransferVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * RelationshipTransfer管理的Dao
 *
 * @author
 */
@Repository
public interface RelationshipTransferDao {
    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param partyMemberQuery
     * @return
     */
    List<RelationshipTransferPageVO> findByConditionPage(PartyMemberQuery partyMemberQuery);


    List<RelationshipTransferPageVO> receiveTransferPage(PartyMemberQuery partyMemberQuery);

    /**
     * 根据条件查询RelationshipTransfer
     *
     * @param partyMemberQuery
     * @return
     */
    List<RelationshipTransferPageVO> findByConditionPage2(PartyMemberQuery partyMemberQuery);

    /**
     * 镇内组织关系转移
     *
     * @param partyMemberQuery
     * @return
     */
    List<RelationshipTransferPageVO> findByTransferPage(PartyMemberQuery partyMemberQuery);

    /**
     * 根据身份证获取转移信息
     *
     * @param idCard
     * @return
     */
    List<RelationshipTransferInfo> findByIdCard(@Param("idCard") String idCard);

    /**
     * 通过主键查询单个RelationshipTransfer
     *
     * @param id
     * @return
     */
    RelationshipTransferVO findById(@Param(value = "id") Long id);

    /**
     * 根据党员ID获取
     *
     * @param memberId
     * @return
     */
    RelationshipTransfer findRecord(@Param(value = "memberId") Long memberId, @Param(value = "status") Integer status);

    /**
     * 保存RelationshipTransfer
     *
     * @param relationshipTransfer
     */
    void add(RelationshipTransfer relationshipTransfer);

    /**
     * 更新RelationshipTransfer
     *
     * @param relationshipTransfer
     */
    void update(RelationshipTransfer relationshipTransfer);

    /**
     * 更新状态
     *
     * @param partyMemberQuery
     * @return
     */
    int updateByStatus(PartyMemberQuery partyMemberQuery);

    /**
     * 删除RelationshipTransfer
     *
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     * 获取待审核的申请
     *
     * @param partyMemberQuery
     * @return
     */
    List<RelationshipTransferPageVO> queryAuditRelationship(PartyMemberQuery partyMemberQuery);

    /**
     * @param idCard
     * @return
     */
    List<RelationshipTransferInfo> findByIdCards(@Param("idCard") String idCard);

}
