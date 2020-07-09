package com.bit.module.pb.dao;

import com.bit.module.pb.bean.PartyMemberApproval;
import com.bit.module.pb.vo.partyMember.PartyMemberApprovalVO;
import com.bit.module.pb.vo.partyMember.PartyMemberQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PartyMemberApproval管理的Dao
 *
 * @author
 */
@Repository
public interface PartyMemberApprovalDao {
    /**
     * 根据条件查询PartyMemberApproval
     *
     * @param partyMemberQuery
     * @return
     */
    List<PartyMemberApproval> findByConditionPage(PartyMemberQuery partyMemberQuery);

    /**
     * 通过主键查询单个PartyMemberApproval
     *
     * @param id
     * @return
     */
    PartyMemberApprovalVO findById(@Param(value = "id") Long id);

    /**
     * 保存PartyMemberApproval
     *
     * @param partyMemberApproval
     */
    void add(PartyMemberApproval partyMemberApproval);

    /**
     * 更新PartyMemberApproval
     *
     * @param partyMemberApproval
     */
    void update(PartyMemberApproval partyMemberApproval);

    /**
     * 更改状态
     *
     * @param partyMemberQuery
     */
    int updateByStatus(PartyMemberQuery partyMemberQuery);

    /**
     * 删除PartyMemberApproval
     *
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     * 查询是否提交申请
     *
     * @param memberId
     * @param type
     * @return
     */
    PartyMemberApproval findRecord(@Param(value = "memberId") Long memberId, @Param(value = "type") Integer type);

    /**
     * 获取停用原因
     *
     * @param memberId
     * @return
     */
    PartyMemberApproval findOutreason(@Param(value = "memberId") Long memberId, @Param(value = "type") Integer type);

    /**
     * 根据ID 获取申请信息
     *
     * @param idCard
     * @return
     */
    List<PartyMemberApproval> findByIdCard(@Param(value = "idCard") String idCard);

}
