package com.bit.module.pb.dao;

import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.bean.PartyMemberCount;
import com.bit.module.pb.bean.PartyMemberOrgName;
import com.bit.module.pb.bean.PartyMemberSearch;
import com.bit.module.pb.vo.partyMember.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * PartyMember管理的Dao
 *
 * @author
 */
@Repository
public interface PartyMemberDao {
    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    List<PartyMemberPageVO> findByConditionPage(PartyMemberQuery partyMemberVO);

    /**
     * 在册党员
     *
     * @param partyMemberQuery
     * @return
     */
    List<PartyMemberPageVO> findByRegisteredPage(PartyMemberQuery partyMemberQuery);

    /**
     * 获取已停用的党员信息
     *
     * @param partyMemberQuery
     * @return
     */
    List<PartyMemberPageVO> findByDisablePage(PartyMemberQuery partyMemberQuery);

    /**
     * 导出停用党员信息
     *
     * @param partyMemberQuery
     * @return
     */
    List<DisablePartyExportVO> findDisableParty(PartyMemberQuery partyMemberQuery);

    /**
     * 获取退伍党员信息分页
     *
     * @param partyMemberExServiceman
     * @return
     */
    List<PartyMemberExServiceman> findExServicemanPage(PartyMemberQuery partyMemberExServiceman);

    /**
     * 查询所有PartyMember
     *
     * @return
     */
    List<PartyMemberExportVO> findAll(PartyMemberQuery partyMember);

    /**
     * 通过主键查询单个PartyMember
     *
     * @param id
     * @return
     */
    PartyMember findByCondition(@Param(value = "id") Long id, @Param(value = "idCard") String idCard);

    /**
     * 批量保存PartyMember
     *
     * @param partyMembers
     */
    void batchAdd(List<PartyMember> partyMembers);

    /**
     * 保存PartyMember
     *
     * @param partyMember
     */
    void add(PartyMember partyMember);

    /**
     * 更新PartyMember
     *
     * @param partyMember
     */
    void update(PartyMember partyMember);

    /**
     * 更改状态
     *
     * @param id
     * @param status
     */
    void updateByStatus(@Param(value = "id") Long id, @Param(value = "status") Integer status, @Param(value = "reason") Integer reason);

    /**
     * 党员转移更改状态
     *
     * @param id
     * @param orgId
     */
    void updateByTransfer(@Param(value = "id") Long id,
                          @Param(value = "orgId") String orgId,
                          @Param(value = "previousOrgId") String previousOrgId,
                          @Param(value = "currentOrgName") String currentOrgName);

    /**
     * 删除PartyMember
     *
     * @param id
     */
    void delete(@Param(value = "id") Long id);

    /**
     * 根据组织ID查询已转出的党员记录
     *
     * @param orgId
     * @return
     */
    List<PartyMember> findRollOutsByOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 根据组织ID查询停用的党员记录
     *
     * @param orgId
     * @return
     */
    List<PartyMember> findDisableMembersByOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 导出退伍军人
     *
     * @param partyMemberQuery
     * @return
     */
    List<PartyMemberExportVO> findExServiceman(PartyMemberQuery partyMemberQuery);

    Integer countPartyMemberByNameAndIdCard(@Param(value = "name") String name, @Param(value = "idCard") String idCard);

    /**
     * 根据身份证和状态查询党员信息
     *
     * @param idCard
     * @return
     */
    PartyMember findByIdCardAndStatus(@Param(value = "idCard") String idCard);

    /**
     * 根据组织id计算组织人数
     *
     * @param partyMemberCount
     * @return
     */
    Integer countByOrgId(PartyMemberCount partyMemberCount);

    /**
     * 根据id查询身份证
     *
     * @param pmIds
     * @return
     */
    List<String> batchSelectByIds(@Param(value = "pmIds") List<Long> pmIds);

    /**
     * 根据orgId查询党员
     *
     * @param orgId
     * @return
     */
    List<Long> findMemberIdsByOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 根据用户id批量查询用户
     *
     * @param pmIds
     * @return
     */
    List<PartyMember> queryUserbatchSelectByIds(@Param(value = "pmIds") List<Long> pmIds);

    /**
     * 根据组织id批量查询党员
     *
     * @param orgIds
     * @return
     */
    List<PartyMember> batchFindMemberByOrgIds(@Param(value = "orgIds") List<Long> orgIds);

    /**
     * 根据orgIds集合查询党员
     *
     * @param orgIds
     * @return
     */
    List<Long> findMemberIdsByOrgIds(@Param(value = "orgIds") List<Long> orgIds);

    /**
     * 根据党员id集合 查询党员信息和党组织信息
     *
     * @param ids
     * @return
     */
    List<PartyMemberOrgName> findDetailByMemberIds(@Param(value = "ids") List<Long> ids);

    /**
     * 根据党组织id查询党员信息
     *
     * @param orgId
     * @return
     */
    List<PartyMember> findMembersByOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 根据大于党组织id查询党员信息
     *
     * @param orgId
     * @return
     */
    List<PartyMember> findMembersByBiggerThanOrgId(@Param(value = "orgId") Long orgId);

    /**
     * 根据身份证批量查询党员信息
     *
     * @param idcards
     * @return
     */
    List<PartyMember> batchFindMemberDetailByIdCard(@Param(value = "idcards") List<String> idcards);

    /**
     * 根据组织ID获取党员的联系信息
     *
     * @param orgIds
     * @return
     */
    List<PartyMember> findContacts(@Param(value = "orgIds") List<String> orgIds);

    /**
     * 根据身份证集合获取多个党员信息
     *
     * @param idCards
     * @return
     */
    List<PartyMember> findByIdCards(List<String> idCards);

    /**
     * 三会一课 会议 和 学习计划选择人员专用 搜索人员
     *
     * @param partyMemberSearch
     * @return
     */
    List<PartyMember> getPartyMembersByLikeName(PartyMemberSearch partyMemberSearch);

    /**
     * 批量导入，分段获取党员信息
     *
     * @param start
     * @param end
     * @return
     */
    List<PartyMember> findAllLimit(@Param(value = "start") Integer start, @Param(value = "end") Integer end);

    /**
     * 根据身份证获取转出党员信息
     *
     * @param idCard
     * @return
     */
    PartyMember findByIdCard(@Param(value = "idCard") String idCard);

    /**
     * 党员转出
     *
     * @param partyMemberQuery
     * @return
     */
    List<PartyMemberPageVO> findByRollOutPage(PartyMemberQuery partyMemberQuery);

    /**
     * 导出转出党员
     * @param partyMemberQuery
     * @return
     */
    List<TransferPartyMemberExportVO> findByRollOutExport(PartyMemberQuery partyMemberQuery);

}
