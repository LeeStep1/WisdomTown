package com.bit.module.pb.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.PartyMemberTypeEnum;
import com.bit.common.enumerate.PartyOrgLevelEnum;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.dao.ExServicemanDao;
import com.bit.module.pb.dao.OrganizationDao;
import com.bit.module.pb.dao.PartyMemberDao;
import com.bit.module.pb.feign.FileServiceFeign;
import com.bit.module.pb.feign.SysServiceFeign;
import com.bit.module.pb.service.PartyMemberService;
import com.bit.module.pb.vo.FileInfoVO;
import com.bit.module.pb.vo.OrganizationInfoVO;
import com.bit.module.pb.vo.UserVO;
import com.bit.module.pb.vo.partyMember.*;
import com.bit.utils.ObjectUtil;
import com.bit.utils.RadixUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PartyMember的Service实现类
 *
 * @author codeGenerator
 */
@Slf4j
@Service("partyMemberService")
public class PartyMemberServiceImpl extends BaseService implements PartyMemberService {

    /**
     * 限制条数
     */
    private static final int POINTSDATALIMIT = 1000;

    @Autowired
    private PartyMemberDao partyMemberDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private ExServicemanDao exServicemanDao;

    @Autowired
    private SysServiceFeign sysServiceFeign;

    @Autowired
    private FileServiceFeign fileServiceFeign;

    /**
     * 根据条件查询PartyMember
     *
     * @param partyMemberQuery
     * @return
     */
    @Override
    public PageInfo findByConditionPage(PartyMemberQuery partyMemberQuery) {
        if (partyMemberQuery.getOrgId() == null) {
            Long orgId = Long.parseLong(getUserInfo().getPbOrgId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(orgId.toString()));
            partyMemberQuery.setOrgId(orgId);
            partyMemberQuery.setOrgMaxId(orgMaxId);
        }
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        List<PartyMemberPageVO> list = partyMemberDao.findByConditionPage(partyMemberQuery);
        PageInfo<PartyMemberPageVO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo findByPMStatusPage(PartyMemberQuery partyMemberQuery) {
        partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.PASSED.getValue());
        List<PartyMemberPageVO> list = new ArrayList<>();
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        if (partyMemberQuery.getStatuses() != null && partyMemberQuery.getStatuses().size() != 0) {
            switch (PMStatusEnum.getByValue(partyMemberQuery.getStatuses().get(0))) {
                case DRAFT:
                case NORMAL:
                    // 正常、待完善
                    list = registeredPartyMembers(partyMemberQuery);
                    getPhoto(list);
                    break;
                case DISABLE:
                    // 停用
                    list = disablePartyMembers(partyMemberQuery);
                    break;
                case EMIGRATION:
                    // 转出
                    list = rollOutPartyMembers(partyMemberQuery);
                    break;
                default:
                    break;
            }
        }
        PageInfo<PartyMemberPageVO> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    private List<PartyMemberPageVO> getPhoto(List<PartyMemberPageVO> list) {
        if (list != null && list.size() > 0) {
            List<Long> fileIds = new ArrayList<>();
            list.parallelStream().forEach(partyMemberPageVO -> {
                try {
                    if (partyMemberPageVO != null && partyMemberPageVO.getPhoto() != null) {
                        Long photo = Long.valueOf(partyMemberPageVO.getPhoto());
                        fileIds.add(photo);
                    }
                } catch (NumberFormatException e) {
                    partyMemberPageVO.setPhoto(null);
                    log.info("非Long......");
                }
            });
            if (fileIds.size() > 0) {
                FileInfoVO fileInfoVO = new FileInfoVO();
                fileInfoVO.setFileIds(fileIds);
                BaseVo baseVo = fileServiceFeign.findByIds(fileInfoVO);
                if (baseVo != null && baseVo.getData() != null) {
                    String str = JSON.toJSONString(baseVo.getData());
                    List<FileInfoVO> fileInfos = JSON.parseArray(str, FileInfoVO.class);
                    if (fileInfos != null && fileInfos.size() > 0) {
                        for (int i = 0; i < fileInfos.size(); i++) {
                            for (int j = 0; j < list.size(); j++) {
                                if (fileInfos.get(i).getId().toString().equals(list.get(j).getPhoto())) {
                                    list.get(j).setPhoto(fileInfos.get(i).getPath());
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<PartyMemberPageVO> registeredPartyMembers(PartyMemberQuery partyMemberQuery) {
        Organization organization = organizationDao.findById(getCurrentUserInfo().getPbOrgId());
        if (organization != null) {
            Long orgId = Long.valueOf(organization.getId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(organization.getId()));
            switch (organization.getOrgType()) {
                case 1: // 镇党委
                case 2: // 党总支
                case 4: // 基层党委
                    if (partyMemberQuery.getOrgId() == null) {
                        partyMemberQuery.setOrgId(orgId);
                        partyMemberQuery.setOrgMaxId(orgMaxId);
                    }
                    break;
                case 3: // 支部
                    if (partyMemberQuery.getOrgId() == null) {
                        partyMemberQuery.setOrgId(orgId);
                        partyMemberQuery.setOrgMaxId(null);
                    }
                    break;
                default:
                    break;
            }
        }
        return partyMemberDao.findByRegisteredPage(partyMemberQuery);
    }

    private List<PartyMemberPageVO> disablePartyMembers(PartyMemberQuery partyMemberQuery) {
        Organization organization = organizationDao.findById(getCurrentUserInfo().getPbOrgId());
        if (organization != null) {
            Long orgId = Long.valueOf(organization.getId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(organization.getId()));
            switch (organization.getOrgType()) {
                case 1: // 镇党委
                case 2: // 党总支
                case 4: // 基层党委
                    if (partyMemberQuery.getOrgId() == null) {
                        partyMemberQuery.setOrgId(orgId);
                        partyMemberQuery.setOrgMaxId(orgMaxId);
                    }
                    break;
                case 3: // 支部
                    if (partyMemberQuery.getOrgId() == null) {
                        partyMemberQuery.setOrgId(orgId);
                        partyMemberQuery.setOrgMaxId(null);
                    }
                    break;
                default:
                    break;
            }
        }
        return partyMemberDao.findByDisablePage(partyMemberQuery);
    }

    private List<PartyMemberPageVO> rollOutPartyMembers(PartyMemberQuery partyMemberQuery) {
        Long orgId = Long.valueOf(getCurrentUserInfo().getPbOrgId());
        Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(getCurrentUserInfo().getPbOrgId()));
        partyMemberQuery.setOrgId(orgId);
        partyMemberQuery.setOrgMaxId(orgMaxId);
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        return partyMemberDao.findByRollOutPage(partyMemberQuery);
    }


    @Override
    public PageInfo findExServicemanPage(PartyMemberQuery partyMemberExServicemanVO) {
        if (partyMemberExServicemanVO.getOrgId() == null) {
            Long orgId = Long.parseLong(getUserInfo().getPbOrgId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(orgId.toString()));
            partyMemberExServicemanVO.setOrgId(orgId);
            partyMemberExServicemanVO.setOrgMaxId(orgMaxId);
        }
        PageHelper.startPage(partyMemberExServicemanVO.getPageNum(), partyMemberExServicemanVO.getPageSize());
        List<PartyMemberExServiceman> list = partyMemberDao.findExServicemanPage(partyMemberExServicemanVO);
        PageInfo<PartyMemberExServiceman> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    /**
     * 查询所有PartyMember
     *
     * @param partyMemberQuery
     * @return
     */
    @Override
    public List<PartyMemberExportVO> findAll(PartyMemberQuery partyMemberQuery) {
        Organization organization = organizationDao.findById(getCurrentUserInfo().getPbOrgId());
        if (organization != null) {
            Long orgId = Long.valueOf(organization.getId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(organization.getId()));
            switch (organization.getOrgType()) {
                case 1: // 镇党委
                case 2: // 党总支
                case 4: // 基层党委
                    if (partyMemberQuery.getOrgId() == null) {
                        partyMemberQuery.setOrgId(orgId);
                        partyMemberQuery.setOrgMaxId(orgMaxId);
                    }
                    break;
                case 3: // 支部
                    if (partyMemberQuery.getOrgId() == null) {
                        partyMemberQuery.setOrgId(orgId);
                        partyMemberQuery.setOrgMaxId(null);
                    }
                    break;
                default:
                    break;
            }
        }
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize()).setOrderByOnly(true);
        return partyMemberDao.findAll(partyMemberQuery);
    }

    @Override
    public List<PartyMemberExportVO> findExServiceman(PartyMemberQuery partyMemberQuery) {
        if (partyMemberQuery.getOrgId() == null) {
            Long orgId = Long.parseLong(getUserInfo().getPbOrgId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(orgId.toString()));
            partyMemberQuery.setOrgId(orgId);
            partyMemberQuery.setOrgMaxId(orgMaxId);
        }
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize()).setOrderByOnly(true);
        return partyMemberDao.findExServiceman(partyMemberQuery);
    }

    /**
     * 通过主键查询单个PartyMember
     *
     * @param id
     * @return
     */
    @Override
    public PartyMember findById(Long id) {
        return partyMemberDao.findByCondition(id, null);
    }

    @Override
    public PartyMember findByIdCard(String idCard) {
        if (idCard == null) {
            idCard = getUserInfo().getIdcard();
        }
        return partyMemberDao.findByCondition(null, idCard);
    }

    @Override
    public List<PartyMember> findByIdCards(List<String> idCards) {
        return partyMemberDao.findByIdCards(idCards);
    }

    /**
     * 根据身份证获取党员信息 查询正常的
     *
     * @param idCard
     * @return
     */
    @Override
    public PartyMember findByIdCardWithStatus(String idCard) {
        if (idCard == null) {
            idCard = getUserInfo().getIdcard();
        }
        return partyMemberDao.findByIdCardAndStatus(idCard);
    }

    /**
     * 三会一课 会议 和 学习计划选择人员专用 搜索人员
     *
     * @param partyMemberSearch
     * @return
     */
    @Override
    public BaseVo getPartyMembersByLikeName(PartyMemberSearch partyMemberSearch) {
        List<PartyMember> partyMembersByLikeName = partyMemberDao.getPartyMembersByLikeName(partyMemberSearch);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(partyMembersByLikeName);
        return baseVo;
    }

    @Override
    public List<PartyMember> findAllLimit(Integer page, Integer size) {

        return partyMemberDao.findAllLimit(page * size, size);
    }

    /**
     * 批量保存PartyMember
     * 根据唯一索引idCard去重
     *
     * @param partyMembers
     */
    @Override
    public void batchAdd(List<PartyMember> partyMembers) {
        int partNum = partyMembers.size() / POINTSDATALIMIT;
        partyMembers.parallelStream().forEach(partyMember -> {
            partyMember.setStatus(PartyMember.judgeStatus(partyMember));
        });
        for (int i = 0; i < partNum; i++) {
            List<PartyMember> partyMemberPart = partyMembers.stream().limit(POINTSDATALIMIT).collect(Collectors.toList());
            partyMemberDao.batchAdd(partyMemberPart);
            partyMembers.subList(0, POINTSDATALIMIT).clear();
        }
        partyMemberDao.batchAdd(partyMembers);
    }

    /**
     * 更新PartyMember
     *
     * @param partyMemberParams
     */
    @Override
    @Transactional
    public void update(PartyMemberParams partyMemberParams) {
        PartyMember partyMember = new PartyMember();
        BeanUtils.copyProperties(partyMemberParams, partyMember);
        partyMember.setStatus(PartyMember.judgeStatus(partyMember));
        partyMember.setOrgId(null);
        partyMember.setName(null);
        partyMember.setIdCard(null);
        partyMemberDao.update(partyMember);
        // 退伍信息
        ExServiceman exServiceman = new ExServiceman();
        exServiceman.setOrigin(partyMemberParams.getOrigin());
        exServiceman.setOriginalTroops(partyMemberParams.getOriginalTroops());
        exServiceman.setRetireTime(partyMemberParams.getRetireTime());
        exServiceman.setIsSelfEmployment(partyMemberParams.getIsSelfEmployment());
        exServiceman.setRelTransferTime(partyMemberParams.getRelTransferTime());
        if (!ObjectUtil.checkObjAllFieldsIsNull(exServiceman)) {
            exServiceman.setOrgName(partyMemberParams.getOrgName());
            exServiceman.setIdCard(partyMemberParams.getIdCard());
            exServicemanDao.updateByIdCard(exServiceman);
        }
    }

    /**
     * 完善信息PartyMember
     *
     * @param partyMemberParams
     */
    @Override
    public void perfect(PartyMemberParams partyMemberParams) {
        PartyMember partyMember = new PartyMember();
        BeanUtils.copyProperties(partyMemberParams, partyMember);
        partyMember.setStatus(PartyMember.judgeStatus(partyMember));
        partyMember.setOrgId(null);
        partyMemberDao.update(partyMember);
    }

    @Override
    public Boolean existPartyMember(String name, String idCard) {
        return partyMemberDao.countPartyMemberByNameAndIdCard(name, idCard) > 0;
    }

    @Override
    public PartyMember findByIdCardAndInside(String idCard) {
        return partyMemberDao.findByIdCardAndStatus(idCard);
    }

    @Override
    public BaseVo successVo() {
        return super.successVo();
    }

    @Override
    public UserInfo getUserInfo() {
        return getCurrentUserInfo();
    }

    /**
     * 根据党组织id查询党员信息
     *
     * @param orgId
     * @return
     */
    @Override
    public BaseVo findMembersByOrgId(String orgId) {
        List<PartyMember> membersByOrgId = partyMemberDao.findMembersByOrgId(Long.valueOf(orgId));
        BaseVo baseVo = new BaseVo();
        baseVo.setData(membersByOrgId);
        return baseVo;
    }

    /*
    * 若身份为T1或T2级管理员，通讯录展示该级与下级管理员联系方式
若身份为T3级管理员，通讯录展示该支部党员和该支部管理员联系方式
若身份为党员，通讯录展示该党员所在组织其他党员的联系方式*/

    /*
    * 管理员：
    * 镇党委及基层党委/总支：显示本组织其他管理员及下级组织所有管理员的联系方式；
      党支部：显示本组织其他管理员及所有党员的联系方式。
    * 党员：
    * 显示本组织所有党员的通讯录信息
    * 2019-06-26 林睿涵说党员要查出自己机构下的党员和管理员
    * */


    /**
     * 2019-06-27 林睿涵说新的通讯录需求
     * 1、镇党委：显示本组织其他管理员、机关党委管理员、企业服务中心党委、村党委/总支、部分村支部（直属镇党委）、社区党委/总支、部分社区支部（直属社区党建工作部）；
     2、机关党委：显示本组织其他管理员、所有机关党支部管理员；
     3、企业服务中心党委：显示本组织其他管理员、所有企业党支部管理员；
     4、村党委/总支、社区党委/总支：显示本组织其他管理员、直接下级党支部管理员；
     5、所有党支部：显示本组织其他管理员及所有党员的联系方式。
     注意：社区党建工作部无需在通讯录中显示。
     *
     */

    /**
     * 通讯录
     *
     * @return
     */
    @Override
    public BaseVo addressBook() {
        UserInfo userInfo = getCurrentUserInfo();

        List<PartyMemberType> partyMemberTypeList = new ArrayList<>();

        List<PartyMemberType> partyMembers = new ArrayList<>();
        //当前用户的组织id
        String pbOrgId = userInfo.getPbOrgId();

        Long identityId = userInfo.getCurrentIdentity();
        //查询当前身份
        BaseVo byId = sysServiceFeign.findById(identityId);
        if (byId.getData() != null) {
            String s = JSON.toJSONString(byId.getData());
            Identity identity = JSON.parseObject(s, Identity.class);

            //党员身份
            //如果是党员 本组织下所有的党员信息
            if (identity.getAcquiesce().equals(PartyMemberTypeEnum.MEMBER.getCode())) {
                List<PartyMember> partyMemberList = partyMemberDao.findMembersByOrgId(Long.valueOf(pbOrgId));
                for (PartyMember partyMember : partyMemberList) {
                    PartyMemberType partyMemberType = new PartyMemberType();
                    BeanUtils.copyProperties(partyMember, partyMemberType);
                    partyMemberType.setType(PartyMemberTypeEnum.MEMBER.getCode());
                    partyMemberTypeList.add(partyMemberType);
                }
                //得出管理员
                List<Long> subIds = new ArrayList<>();
                subIds.add(Long.valueOf(pbOrgId));

                List<Long> feignOrgIds = new ArrayList<>();


                //调用feign 查询user_rel_pborg 和 user表
                List<UserVO> adminUserList = sysServiceFeign.findUserByOrgIds(subIds);

                partyMemberTypeList = commonPartyMemberTypeList(adminUserList, feignOrgIds, partyMembers, partyMemberTypeList, userInfo.getIdcard());

            }
            //如果是镇党委 显示本组织其他管理员、
            // 机关党委管理员、 type 4
            // 企业服务中心党委、 type 4
            // 村党委/总支、 type 4 and 2
            // 部分村支部（直属镇党委）、  type 3
            // 社区党委/总支、 type 4 and 2
            // 部分社区支部（直属社区党建工作部）type 3

            //管理员身份
            if (identity.getAcquiesce().equals(PartyMemberTypeEnum.ADMIN.getCode())) {

                List<Long> subIds = new ArrayList<>();
                //查询用户组织信息
                Organization org = organizationDao.findById(pbOrgId);
                Integer type = org.getOrgType();
                switch (type) {
                    case 1:
                        //镇党委
                        //查询出直属下级
                        subIds.add(Long.valueOf(pbOrgId));
                        List<OrganizationInfoVO> organizationInfoVOS1 = organizationDao.findDirectSubordinatesInfoById(pbOrgId);
                        if (organizationInfoVOS1 != null && organizationInfoVOS1.size() > 0) {
                            for (OrganizationInfoVO organizationInfoVO : organizationInfoVOS1) {
                                //如果type是5 取5 下面直属2 3 4 机构
                                if (organizationInfoVO.getOrgType().equals(PartyOrgLevelEnum.LEVEL_FIVE.getCode())) {
                                    //因为5下面都是 2 3 4 机构 所有直接全取了
                                    List<OrganizationInfoVO> organizationInfoVOS1sub = organizationDao.findDirectSubordinatesInfoById(organizationInfoVO.getId());
                                    if (organizationInfoVOS1sub != null && organizationInfoVOS1sub.size() > 0) {
                                        for (OrganizationInfoVO infoVO : organizationInfoVOS1sub) {
                                            subIds.add(Long.valueOf(infoVO.getId()));
                                        }
                                    }
                                } else {
                                    subIds.add(Long.valueOf(organizationInfoVO.getId()));
                                }
                            }
                            //根据组织id 查询 管理员
                            List<Long> feignOrgIds1 = new ArrayList<>();
                            List<UserVO> adminUserList1 = sysServiceFeign.findUserByOrgIds(subIds);
                            partyMemberTypeList = commonPartyMemberTypeList(adminUserList1, feignOrgIds1, partyMembers, partyMemberTypeList, userInfo.getIdcard());

                        }
                        break;
                    case 2:
                        //查询直属下级和本级的管理员
                        List<OrganizationInfoVO> organizationInfoVOS2 = organizationDao.findDirectSubordinatesInfoById(pbOrgId);
                        if (organizationInfoVOS2 != null && organizationInfoVOS2.size() > 0) {
                            //添加本级机构
                            subIds.add(Long.valueOf(pbOrgId));
                            for (OrganizationInfoVO organizationInfoVO : organizationInfoVOS2) {
                                //查询 type是3 机构
                                if (organizationInfoVO.getOrgType().equals(PartyOrgLevelEnum.LEVEL_THREE.getCode())) {
                                    subIds.add(Long.valueOf(organizationInfoVO.getId()));
                                }
                            }
                            //根据组织id 查询 管理员
                            List<Long> feignOrgIds2 = new ArrayList<>();
                            //调用feign 查询user_rel_pborg 和 user表
                            List<UserVO> adminUserList2 = sysServiceFeign.findUserByOrgIds(subIds);
                            partyMemberTypeList = commonPartyMemberTypeList(adminUserList2, feignOrgIds2, partyMembers, partyMemberTypeList, userInfo.getIdcard());

                        }
                        break;
                    case 3:
                        //查询本组织管理员和党员
                        //先插党员
                        List<PartyMember> membersByOrgId = partyMemberDao.findMembersByOrgId(Long.valueOf(pbOrgId));
                        if (membersByOrgId != null && membersByOrgId.size() > 0) {
                            for (PartyMember partyMember : membersByOrgId) {
                                PartyMemberType partyMemberType = new PartyMemberType();
                                BeanUtils.copyProperties(partyMember, partyMemberType);
                                partyMemberType.setType(PartyMemberTypeEnum.MEMBER.getCode());
                                partyMemberTypeList.add(partyMemberType);
                            }
                        }
                        //党员添加去除自己的过滤
                        partyMemberTypeList = removeMyself(partyMemberTypeList, userInfo.getIdcard());

                        //再查管理员
                        List<Long> adminOrgIds3 = new ArrayList<>();
                        adminOrgIds3.add(Long.valueOf(pbOrgId));
                        List<Long> feignOrgIds3 = new ArrayList<>();
                        //调用feign 查询user_rel_pborg 和 user表
                        List<UserVO> adminUserList3 = sysServiceFeign.findUserByOrgIds(adminOrgIds3);
                        partyMemberTypeList = commonPartyMemberTypeList(adminUserList3, feignOrgIds3, partyMembers, partyMemberTypeList, userInfo.getIdcard());


                        break;
                    case 4:
                        //查询直属下级和本级的管理员
                        List<OrganizationInfoVO> organizationInfoVOS4 = organizationDao.findDirectSubordinatesInfoById(pbOrgId);
                        if (organizationInfoVOS4 != null && organizationInfoVOS4.size() > 0) {
                            //添加本级机构
                            subIds.add(Long.valueOf(pbOrgId));
                            for (OrganizationInfoVO organizationInfoVO : organizationInfoVOS4) {
                                //查询 type是3 机构
                                if (organizationInfoVO.getOrgType().equals(PartyOrgLevelEnum.LEVEL_THREE.getCode())) {
                                    subIds.add(Long.valueOf(organizationInfoVO.getId()));
                                }
                            }
                            //根据组织id 查询 管理员
//                            List<Long> adminOrgIds4 = new ArrayList<>();
//                            adminOrgIds4.add(Long.valueOf(pbOrgId));

                            List<Long> feignOrgIds4 = new ArrayList<>();
                            //调用feign 查询user_rel_pborg 和 user表
                            List<UserVO> adminUserList4 = sysServiceFeign.findUserByOrgIds(subIds);
                            partyMemberTypeList = commonPartyMemberTypeList(adminUserList4, feignOrgIds4, partyMembers, partyMemberTypeList, userInfo.getIdcard());

                        }
                        break;
                    default:
                        break;
                }
            }
        }

        BaseVo baseVo = new BaseVo();
        baseVo.setData(partyMemberTypeList);
        return baseVo;
    }

    /**
     * 通讯录党员去掉自己
     *
     * @param partyMembers
     * @param idcard
     * @return
     */
    private List<PartyMemberType> removeMyself(List<PartyMemberType> partyMembers, String idcard) {
        Iterator iter = partyMembers.iterator();
        while (iter.hasNext()) {
            PartyMemberType partyMemberType = (PartyMemberType) iter.next();
            if (partyMemberType.getIdCard().equals(idcard)) {
                iter.remove();
            }
        }
        return partyMembers;
    }

    /**
     * 通讯录管理员去掉自己
     *
     * @param adminUserList
     * @param feignOrgIds
     * @param partyMembers
     * @param partyMemberTypeList
     * @param idcard
     * @return
     */
    private List<PartyMemberType> commonPartyMemberTypeList(List<UserVO> adminUserList,
                                                            List<Long> feignOrgIds,
                                                            List<PartyMemberType> partyMembers,
                                                            List<PartyMemberType> partyMemberTypeList,
                                                            String idcard) {
        if (adminUserList != null && adminUserList.size() > 0) {
            for (UserVO user : adminUserList) {
                //去除自己
                if (!user.getIdcard().equals(idcard)) {
                    feignOrgIds.add(user.getPbOrgId());
                    PartyMemberType partyMemberType = new PartyMemberType();
                    partyMemberType.setId(user.getId());
                    partyMemberType.setIdCard(user.getIdcard());
                    partyMemberType.setName(user.getRealName());
                    partyMemberType.setMobile(user.getMobile());
                    partyMemberType.setType(PartyMemberTypeEnum.ADMIN.getCode());
                    partyMemberType.setOrgId(user.getPbOrgId().toString());
                    partyMembers.add(partyMemberType);
                }
            }

            if (feignOrgIds != null && feignOrgIds.size() > 0) {
                //批量获得党组织名称
                List<Organization> organizations = organizationDao.batchSelectByIds(feignOrgIds);
                if (organizations != null && organizations.size() > 0) {
                    for (int i = 0; i < partyMembers.size(); i++) {
                        Long pbid = Long.valueOf(partyMembers.get(i).getOrgId());
                        for (int j = 0; j < organizations.size(); j++) {
                            if (pbid.equals(Long.valueOf(organizations.get(j).getId()))) {
                                partyMembers.get(i).setOrgName(organizations.get(j).getName());
                            }
                        }
                    }
                }
                partyMemberTypeList.addAll(partyMembers);
            }
        }
        return partyMemberTypeList;
    }

    @Override
    public BaseVo queryContacts(String orgId) {
        if (StringUtils.isEmpty(orgId)) {
            // 如果为空，获取当前用户的组织ID
            orgId = getUserInfo().getPbOrgId();
        }
        int level = RadixUtil.getLevel(orgId, true);
        List<Contacts> contactsList = new ArrayList<>();
        List<String> orgIds = new ArrayList<>();
        if (level < 3) {
            // 若身份为T1或T2级管理员，通讯录展示该级与下级管理员联系方式
            List<Organization> organizationList = organizationDao.findSubordinatesById(orgId, true);
            orgIds = organizationList.parallelStream().map(Organization::getId).collect(Collectors.toList());
        } else {
            // 若身份为T3级管理员，通讯录展示该支部党员和该支部管理员联系方式 || 若身份为党员，通讯录展示该党员所在组织其他党员的联系方式
            orgIds.add(orgId);
        }
        if (orgIds.size() > 0) {
            // 获取党员信息列表
            List<PartyMember> partyMemberList = partyMemberDao.findContacts(orgIds);
            if (partyMemberList.size() > 0) {
                for (String id : orgIds) {
                    Contacts contacts = new Contacts();
                    List<Contacts.PartyMember> partyMembers = new ArrayList<>();
                    partyMemberList.stream().forEach(partyMember -> {
                        if (partyMember.getOrgId().equals(id)) {
                            Contacts.PartyMember contact_partymember = new Contacts.PartyMember();
                            contacts.setOrgId(id);
                            contacts.setOrgName(partyMember.getOrgName());
                            contact_partymember.setName(partyMember.getName());
                            contact_partymember.setMobile(partyMember.getMobile());
                            partyMembers.add(contact_partymember);
                        }
                    });
                    if (partyMembers.size() > 0) {
                        contacts.setPartyMemberList(partyMembers);
                        contactsList.add(contacts);
                    }
                }
            }
        }
        return new BaseVo(contactsList);
    }

    /**
     * 删除PartyMember
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        PartyMember partyMember = partyMemberDao.findByCondition(id, null);
        partyMemberDao.delete(id);
        exServicemanDao.deleteByIdCard(partyMember.getIdCard());
    }

    @Override
    public List<DisablePartyExportVO> findDisableParty(PartyMemberQuery partyMemberQuery) {
        Organization organization = organizationDao.findById(getCurrentUserInfo().getPbOrgId());
        if (organization != null) {
            Long orgId = Long.valueOf(organization.getId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(organization.getId()));
            switch (organization.getOrgType()) {
                case 1: // 镇党委
                case 2: // 党总支
                case 4: // 基层党委
                    if (partyMemberQuery.getOrgId() == null) {
                        partyMemberQuery.setOrgId(orgId);
                        partyMemberQuery.setOrgMaxId(orgMaxId);
                    }
                    break;
                case 3: // 支部
                    if (partyMemberQuery.getOrgId() == null) {
                        partyMemberQuery.setOrgId(orgId);
                        partyMemberQuery.setOrgMaxId(null);
                    }
                    break;
                default:
                    break;
            }
        }
        partyMemberQuery.setStatus(PMStatusEnum.DISABLE.getValue());
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize()).setOrderByOnly(true);
        List<DisablePartyExportVO> list = partyMemberDao.findDisableParty(partyMemberQuery);
        return list;
    }

    @Override
    public List<TransferPartyMemberExportVO> findTransferPartyMember(PartyMemberQuery partyMemberQuery) {
        Long orgId = Long.valueOf(getCurrentUserInfo().getPbOrgId());
        Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(getCurrentUserInfo().getPbOrgId()));
        partyMemberQuery.setOrgId(orgId);
        partyMemberQuery.setOrgMaxId(orgMaxId);
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize()).setOrderByOnly(true);
        return partyMemberDao.findByRollOutExport(partyMemberQuery);
    }


    @AllArgsConstructor
    public enum PMStatusEnum {
        /**
         * 默认配置
         */
        DRAFT(0, "待完善"),

        NORMAL(1, "正常"),

        DISABLE(2, "停用"),

        EMIGRATION(3, "转出");

        /**
         * 枚举值
         */
        private int value;
        /**
         * 枚举叙述
         */
        private String phrase;

        public static PMStatusEnum getByValue(int value) {
            for (PMStatusEnum pmStatusEnum : values()) {
                if (pmStatusEnum.value == value) {
                    return pmStatusEnum;
                }
            }
            return null;
        }

        public int getValue() {
            return value;
        }

        public String getPhrase() {
            return phrase;
        }
    }

}
