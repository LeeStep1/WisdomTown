package com.bit.module.pb.service;

import com.bit.base.dto.UserInfo;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.bean.PartyMemberSearch;
import com.bit.module.pb.vo.partyMember.*;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * PartyMember的Service
 *
 * @author codeGenerator
 */
public interface PartyMemberService {

    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberVO
     * @return
     */
    PageInfo findByConditionPage(PartyMemberQuery partyMemberVO);

    /**
     * 党员状态分页
     *
     * @param partyMemberVO
     * @return
     */
    PageInfo findByPMStatusPage(PartyMemberQuery partyMemberVO);

    /**
     * 查询退伍党员
     *
     * @param partyMemberQuery
     * @return
     */
    PageInfo findExServicemanPage(PartyMemberQuery partyMemberQuery);

    /**
     * 查询所有PartyMember
     *
     * @param partyMember
     * @return
     */
    List<PartyMemberExportVO> findAll(PartyMemberQuery partyMember);

    /**
     * 导出退伍党员
     *
     * @param partyMemberQuery
     * @return
     */
    List<PartyMemberExportVO> findExServiceman(PartyMemberQuery partyMemberQuery);

    /**
     * 通过主键查询单个PartyMember
     *
     * @param id
     * @return
     */
    PartyMember findById(Long id);

    /**
     * 根据身份证获取党员信息
     *
     * @param idCard
     * @return
     */
    PartyMember findByIdCard(String idCard);

    /**
     * 批量保存PartyMember
     *
     * @param partyMembers
     */
    void batchAdd(List<PartyMember> partyMembers);

    /**
     * 更新PartyMember
     *
     * @param partyMember
     */
    void update(PartyMemberParams partyMember);

    /**
     * 完善PartyMember
     *
     * @param partyMember
     */
    void perfect(PartyMemberParams partyMember);

    /**
     * 删除PartyMember
     *
     * @param id
     */
    void delete(Long id);

    /**
     * 是否存在党员，存在返回true，否则返回false
     *
     * @param name
     * @param idCard
     * @return
     */
    Boolean existPartyMember(String name, String idCard);

    /**
     * 根据身份证查镇内党员
     *
     * @param idCard
     * @return
     */
    PartyMember findByIdCardAndInside(String idCard);

    /**
     * 获取当前用户信息
     *
     * @return
     */
    UserInfo getUserInfo();

    /**
     * 根据党组织id查询党员信息
     *
     * @param orgId
     * @return
     */
    BaseVo findMembersByOrgId(String orgId);

    /**
     * 通讯录
     *
     * @return
     */
    BaseVo addressBook();

    /**
     * 获取通讯录
     *
     * @param orgId
     * @return
     */
    BaseVo queryContacts(String orgId);

    /**
     * 根据身份证集合获取党员信息，导入党费时用到
     *
     * @param idCards
     * @return
     */
    List<PartyMember> findByIdCards(List<String> idCards);

    /**
     * 根据身份证获取党员信息 查询正常的
     *
     * @param idCard
     * @return
     */
    PartyMember findByIdCardWithStatus(String idCard);

    /**
     * 三会一课 会议 和 学习计划选择人员专用 搜索人员
     *
     * @param partyMemberSearch
     * @return
     */
    BaseVo getPartyMembersByLikeName(PartyMemberSearch partyMemberSearch);

    List<PartyMember> findAllLimit(Integer page, Integer size);

    /**
     * 导出停用党员
     *
     * @param partyMemberQuery
     * @return
     */
    List<DisablePartyExportVO> findDisableParty(PartyMemberQuery partyMemberQuery);

    /**
     * 导出转出党员
     *
     * @param partyMemberQuery
     * @return
     */
    List<TransferPartyMemberExportVO> findTransferPartyMember(PartyMemberQuery partyMemberQuery);

}
