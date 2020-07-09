package com.bit.module.pb.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.PartyDueStatusEnum;

import com.bit.module.pb.bean.*;
import com.bit.module.pb.dao.*;
import com.bit.module.pb.feign.SysServiceFeign;
import com.bit.module.pb.service.RelationshipTransferService;
import com.bit.module.pb.vo.PartyDueVO;
import com.bit.module.pb.vo.partyMember.*;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.support.PushUtil;
import com.bit.utils.ObjectUtil;
import com.bit.utils.RadixUtil;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * RelationshipTransfer的Service实现类
 *
 * @author codeGenerator
 */
@Service("relationshipTransferService")
public class RelationshipTransferServiceImpl extends BaseService implements RelationshipTransferService {

    @Autowired
    private RelationshipTransferDao relationshipTransferDao;

    @Autowired
    private PartyMemberDao partyMemberDao;

    @Autowired
    private ExServicemanDao exServicemanDao;

    @Autowired
    private DoneDao doneDao;

    @Autowired
    private OrganizationDao organizationDao;

  /*  @Autowired
    private PushUtil pushUtil;*/

    @Autowired
    private SysServiceFeign sysServiceFeign;

    @Autowired
	private PartyDueDao partyDueDao;

    @Autowired
    private SendMqPushUtil sendMqPushUtil;

    @Override
    public BaseVo queryPage(PartyMemberQuery partyMemberQuery, Integer type) {
        String orgId = getCurrentUserInfo().getPbOrgId();
        partyMemberQuery.setOrgId(Long.valueOf(orgId));
        partyMemberQuery.setTransferType(type);
        List<RelationshipTransferPageVO> list = new ArrayList<>();
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        if (type == 3) {
            list = relationshipTransferDao.findByConditionPage(partyMemberQuery);
        } else {
            list = relationshipTransferDao.receiveTransferPage(partyMemberQuery);
        }
        PageInfo<RelationshipTransferPageVO> pageInfo = new PageInfo<RelationshipTransferPageVO>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 镇党委 返回审核通过的申请
     * 基层 返回该层的申请 + 支部的提交申请 + 党员转移接收
     * 支部 返回该层的申请
     *
     * @param partyMemberQuery
     * @return
     */
    @Override
    public BaseVo findByConditionPage(PartyMemberQuery partyMemberQuery) {
        // 组织级别
        int level = RadixUtil.getLevel(getCurrentUserInfo().getPbOrgId(), true);
        if (partyMemberQuery.getOrgId() == null) {
            Long orgId = Long.parseLong(getCurrentUserInfo().getPbOrgId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(orgId.toString()));
            partyMemberQuery.setOrgId(orgId);
            partyMemberQuery.setOrgMaxId(orgMaxId);
        }
        List<RelationshipTransferPageVO> list;
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        if (level == 2) {
            list = relationshipTransferDao.findByConditionPage2(partyMemberQuery);
        } else {
            if (level == 1)
                partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.PASSED.getValue());
            list = relationshipTransferDao.findByConditionPage(partyMemberQuery);
        }
        PageInfo<RelationshipTransferPageVO> pageInfo = new PageInfo<RelationshipTransferPageVO>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public BaseVo queryAuditRelationship(PartyMemberQuery partyMemberQuery) {
        Long orgId = Long.parseLong(getCurrentUserInfo().getPbOrgId());
        Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(orgId.toString()));
        partyMemberQuery.setOrgId(orgId);
        partyMemberQuery.setOrgMaxId(orgMaxId);
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        List<RelationshipTransferPageVO> list = relationshipTransferDao.queryAuditRelationship(partyMemberQuery);
        PageInfo<RelationshipTransferPageVO> pageInfo = new PageInfo<RelationshipTransferPageVO>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * TI 分页
     *
     * @param partyMemberQuery
     * @return
     */
    @Override
    public BaseVo findByTransferPage(PartyMemberQuery partyMemberQuery) {
        Long orgId = Long.valueOf(getCurrentUserInfo().getPbOrgId());
        Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(getCurrentUserInfo().getPbOrgId()));
        partyMemberQuery.setOrgId(orgId);
        partyMemberQuery.setOrgMaxId(orgMaxId);
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        List<RelationshipTransferPageVO> list = relationshipTransferDao.findByTransferPage(partyMemberQuery);
        PageInfo<RelationshipTransferPageVO> pageInfo = new PageInfo<RelationshipTransferPageVO>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public List<RelationshipTransferInfo> findByIdCard(String idCard) {
        return relationshipTransferDao.findByIdCard(idCard);
    }

    /**
     * 通过主键查询单个RelationshipTransfer
     *
     * @param id
     * @return
     */
    @Override
    public RelationshipTransferVO findById(Long id) {
        return relationshipTransferDao.findById(id);
    }

    /**
     * 保存RelationshipTransfer
     *
     * @param partyMemberParams
     */
    @Override
    public void add(PartyMemberParams partyMemberParams) {
        RelationshipTransfer relationshipTransfer = builderRelationshipTransfer(partyMemberParams);
        // 回填用户信息
        UserInfo userInfo = getCurrentUserInfo();
        relationshipTransfer.setUserId(userInfo.getId());
        relationshipTransfer.setUserOrgId(Long.valueOf(userInfo.getPbOrgId()));
        // 转内
        if (partyMemberParams.getOrgId() != null) {
            Organization to_organization = organizationDao.findById(partyMemberParams.getOrgId());
            if (to_organization != null) {
                relationshipTransfer.setToOrgId(to_organization.getId());
                relationshipTransfer.setToOrgName(to_organization.getName());
            }
        } else {
            relationshipTransfer.setFromOrgId(relationshipTransfer.getModification().getFromOrgId());
            relationshipTransfer.setFromOrgName(relationshipTransfer.getModification().getFromOrgName());
            relationshipTransfer.setToOrgName(partyMemberParams.getOrgName());
        }
        // 其它属性
        relationshipTransfer.setDeadline(partyMemberParams.getDeadline());
        relationshipTransfer.setAttach(partyMemberParams.getAttach());
        if (partyMemberParams.getId() == null) {
            relationshipTransfer.setOutTime(new Date());
            relationshipTransfer.setInTime(new Date());
            relationshipTransfer.setInsertTime(new Date());
            relationshipTransfer.setApproveOrgId(getCurrentUserInfo().getPbOrgId());
            relationshipTransfer.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.DRAFT.getValue());
            List<RelationshipTransferInfo> list = relationshipTransferDao.findByIdCards(relationshipTransfer.getModification().getIdCard());
            if (list != null && list.size() > 0) {
                throw new BusinessException("该党员的申请已存在");
            }
            relationshipTransferDao.add(relationshipTransfer);
        } else {
            relationshipTransfer.setId(partyMemberParams.getId());
            relationshipTransferDao.update(relationshipTransfer);
        }
    }

    /**
     * 回填党员转移信息
     *
     * @param partyMemberParams
     * @return
     */
    private RelationshipTransfer builderRelationshipTransfer(PartyMemberParams partyMemberParams) {
        RelationshipTransfer relationshipTransfer = new RelationshipTransfer();
        PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
        // 内转或内转外
        if (partyMemberParams.getMemberId() != null) {
            PartyMember partyMember = partyMemberDao.findByCondition(partyMemberParams.getMemberId(), null);
            BeanUtils.copyProperties(partyMember, partyMemberSummary);
            partyMemberSummary.setId(partyMemberParams.getMemberId());
            partyMemberSummary.setFromOrgId(partyMember.getOrgId());
            partyMemberSummary.setFromOrgName(partyMember.getOrgName());

            relationshipTransfer.setFromOrgId(partyMember.getOrgId());
            relationshipTransfer.setFromOrgName(partyMember.getOrgName());
        } else {
            // 外转内
            BeanUtils.copyProperties(partyMemberParams, partyMemberSummary);
            relationshipTransfer.setFromOrgName(partyMemberParams.getFromOrgName());
            partyMemberSummary.setId(null);
        }
        // 是否是退伍军人
        ExServiceman exServiceman = new ExServiceman();
        exServiceman.setOrigin(partyMemberParams.getOrigin());
        exServiceman.setOriginalTroops(partyMemberParams.getOriginalTroops());
        exServiceman.setRetireTime(partyMemberParams.getRetireTime());
        exServiceman.setIsSelfEmployment(partyMemberParams.getIsSelfEmployment());
        exServiceman.setRelTransferTime(partyMemberParams.getRelTransferTime());
        if (exServiceman.getOrigin() != null && exServiceman.getOriginalTroops() != null) {
            exServiceman.setOrgName(partyMemberParams.getOrgName());
            partyMemberSummary.setExServiceman(exServiceman);
            relationshipTransfer.setIsExServiceman(1);
        }
        relationshipTransfer.setModification(partyMemberSummary);
        return relationshipTransfer;
    }

    @Override
    @Transactional
    public void updateFlow(Long id, Integer status, String reason) {
        RelationshipTransferVO relationshipTransfer = relationshipTransferDao.findById(id);
        if (relationshipTransfer != null) {
            switch (ApprovalStatusEnum.getByValue(status)) {
                case AUDIT:
                    // 审核中（提交）
                    audit(relationshipTransfer);
                    break;
                case RETURN:
                    // 退回
                    sentBack(relationshipTransfer, reason);
                    break;
                case PASSED:
                    // 通过
                    passed(relationshipTransfer);
                    break;
                case RECEIVE:
                    // 待接收
                    receive(relationshipTransfer);
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 删除RelationshipTransfer
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        relationshipTransferDao.delete(id);
    }

    /**
     * 审核（表单提交）
     *
     * @param relationshipTransfer
     */
    private void audit(RelationshipTransferVO relationshipTransfer) {
        PartyMemberQuery partyMemberQuery = new PartyMemberQuery();
        String nextOrgid = null;
        try {
            nextOrgid = getNextOrgid(relationshipTransfer.getUserOrgId().toString());
        } catch (ReturnOrgIdException e) {
            nextOrgid = e.getMessage();
        }
        //20191107添加校验党员身份证
        //如果原组织id是null 就是接收党员
        if (StringUtil.isEmpty(relationshipTransfer.getFromOrgId())){
            RelationshipTransferVO byId = relationshipTransferDao.findById(relationshipTransfer.getId());
            if (byId!=null){
                String card = byId.getModification().getIdCard().replace("\"","");
                PartyMember byIdCard = partyMemberDao.findByIdCard(card);
                if (byIdCard!=null){
                	//如果党员的组织id是空的 就是转出的党员又回来了 需要过滤掉
                    if (StringUtil.isNotEmpty(byIdCard.getOrgId())){
                        throwBusinessException("身份证重复");
                    }
                }
            }else {
                throwBusinessException("关系转移记录不存在");
            }
        }

        partyMemberQuery.setApproveOrgId(nextOrgid);
        partyMemberQuery.setId(relationshipTransfer.getId());
        partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.AUDIT.getValue());
        partyMemberQuery.setStatuses(Arrays.asList(new Integer[]{PartyMemberApprovalServiceImpl.ApprovalStatusEnum.DRAFT.getValue()
                , PartyMemberApprovalServiceImpl.ApprovalStatusEnum.RETURN.getValue()}));
        int result = relationshipTransferDao.updateByStatus(partyMemberQuery);
        if (result > 0) {
            relationshipTransfer.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.AUDIT.getValue());
            relationshipTransfer.setApproveOrgId(nextOrgid);
            disposeRecords(relationshipTransfer, "提交申请", null);
        } else {
            throw new BusinessException("表单提交失败");
        }
    }

    /**
     * 获取下级组织ID
     *
     * @param orgId
     * @return
     */
    private String getNextOrgid(String orgId) throws ReturnOrgIdException {
        Organization nextOrg = organizationDao.findDirectSuperiorById(orgId, true);
        if (nextOrg != null) {
            int level = RadixUtil.getLevel(nextOrg.getId(), true);
            if (level == 1) {
                throw new ReturnOrgIdException(nextOrg.getId());
            }
            if (nextOrg.getIsApprovalAuz() == 0 && level > 1) {
                getNextOrgid(nextOrg.getId());
            }
            throw new ReturnOrgIdException(nextOrg.getId());
        }
        return orgId;
    }

    /**
     * 回退
     *
     * @param relationshipTransfer
     * @param reason
     */
    private void sentBack(RelationshipTransferVO relationshipTransfer, String reason) {
        PartyMemberQuery partyMemberQuery = new PartyMemberQuery();
        String lastOrgId = null;
        try {
            lastOrgId = getLastOrgId(relationshipTransfer.getUserOrgId().toString(), relationshipTransfer.getApproveOrgId());
        } catch (ReturnOrgIdException e) {
            lastOrgId = e.getMessage();
        }
        if (relationshipTransfer.getUserOrgId().toString().equals(lastOrgId)) {
            String topOrgId = RadixUtil.getTopOrgId(relationshipTransfer.getUserOrgId().toString());
            partyMemberQuery.setApproveOrgId(topOrgId);
            partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.RETURN.getValue());
            partyMemberQuery.setChangeTime(new Date());

            relationshipTransfer.setApproveOrgId(relationshipTransfer.getUserOrgId().toString());
        } else {
            partyMemberQuery.setApproveOrgId(lastOrgId);
            partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.AUDIT.getValue());

            relationshipTransfer.setApproveOrgId(lastOrgId);
        }
        partyMemberQuery.setId(relationshipTransfer.getId());
        partyMemberQuery.setStatuses(Arrays.asList(new Integer[]{PartyMemberApprovalServiceImpl.ApprovalStatusEnum.AUDIT.getValue(), PartyMemberApprovalServiceImpl.ApprovalStatusEnum.RECEIVE.getValue()}));
        int result = relationshipTransferDao.updateByStatus(partyMemberQuery);
        if (result > 0) {
            relationshipTransfer.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.RETURN.getValue());
            disposeRecords(relationshipTransfer, "退回申请", reason);
        } else {
            throw new BusinessException("表单退回失败");
        }
    }

    public String getLastOrgId(String originalOrgId, String targetOrgId) throws ReturnOrgIdException {
        if (originalOrgId == targetOrgId) {
            throw new ReturnOrgIdException(originalOrgId);
        }
        int level = RadixUtil.getLevel(targetOrgId, true);
        String orgId = RadixUtil.getFixedOrgId(originalOrgId, level + 1);
        if (orgId == null) {
            throw new ReturnOrgIdException(originalOrgId);
        }
        Organization lastOrg = organizationDao.findById(orgId);
        if (lastOrg != null) {
            if (lastOrg.getIsApprovalAuz() == 0 && !lastOrg.getId().equals(originalOrgId)) {
                getLastOrgId(originalOrgId, lastOrg.getId());
            } else {
                throw new ReturnOrgIdException(lastOrg.getId());
            }
        }
        return originalOrgId;
    }

    /**
     * 审核通过
     *
     * @param relationshipTransfer
     */
    private void passed(RelationshipTransferVO relationshipTransfer) {
        PartyMemberQuery partyMemberQuery = new PartyMemberQuery();
        String nextOrgId = null;
        try {
            nextOrgId = getNextOrgid(relationshipTransfer.getApproveOrgId());
        } catch (ReturnOrgIdException e) {
            nextOrgId = e.getMessage();
        }
        // 一级
        if (relationshipTransfer.getApproveOrgId().equals(nextOrgId)) {
            // 如果是内转内、外传内，直接通过
            if (relationshipTransfer.getToOrgId() != null) {
                partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.PASSED.getValue());
                relationshipTransfer.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.PASSED.getValue());
                buildPartyMember(relationshipTransfer, relationshipTransfer.getFromOrgId() == null ? true : false);
            } else {
                relationshipTransfer.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.RECEIVE.getValue());
                partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.RECEIVE.getValue());
            }
        } else {
            partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.AUDIT.getValue());
        }
        partyMemberQuery.setApproveOrgId(nextOrgId);
        partyMemberQuery.setChangeTime(new Date());
        partyMemberQuery.setId(relationshipTransfer.getId());
        partyMemberQuery.setStatuses(Arrays.asList(new Integer[]{PartyMemberApprovalServiceImpl.ApprovalStatusEnum.AUDIT.getValue()}));
        int result = relationshipTransferDao.updateByStatus(partyMemberQuery);
        if (result > 0) {
            relationshipTransfer.setApproveOrgId(nextOrgId);
            disposeRecords(relationshipTransfer, "审核通过", null);
        } else {
            throw new BusinessException("表单审核失败");
        }

		//审批机构是一级的时候 才能做党费转移
		String s = RadixUtil.toFullBinaryString(Long.valueOf(nextOrgId));
        int level = RadixUtil.getlevel(s);
		if (relationshipTransfer.getApproveOrgId().equals(nextOrgId) && level == 1){
			//20191014添加党费转移
			Calendar cal = Calendar.getInstance();
			Integer day = cal.get(Calendar.DATE);
			Integer month = cal.get(Calendar.MONTH) + 1;
			Integer year = cal.get(Calendar.YEAR);
			//党员id
			Long memberId = relationshipTransfer.getMemberId();
			PartyDue obj = new PartyDue();
			//此处为了区分镇外转镇内 新来的党员没有memberId
			if (memberId!=null){
                obj.setMemberId(memberId.intValue());
            }
			//如果是1月 就要找上一个月的党费记录
			if (month.equals(Const.FIRST_MONTH_OF_YEAR)){
				obj.setYear(year-1);
				obj.setMonth(Const.LAST_MONTH_OF_YEAR);
			}else {
				obj.setYear(year);
				obj.setMonth(month-1);
			}
			//此处为了区分镇外转镇内 新来的党员没有memberId
			if (memberId!=null){
				//上个月党费
				List<PartyDue> byCondition = partyDueDao.findByCondition(obj);
				if (CollectionUtils.isNotEmpty(byCondition)){
					if (byCondition.size()>1 || byCondition.size()<1){
						throwBusinessException("党费缴纳记录异常");
					}else {
						//上个月党费
						PartyDue lastmonth = byCondition.get(0);
						//本月党费
						obj.setYear(year);
						obj.setMonth(month);
						List<PartyDue> currentMonth = partyDueDao.findByCondition(obj);
						PartyDue cmonth = currentMonth.get(0);
						//15号之前若党员未交纳党费，则删除党员在A党支部当月的党费记录，同时在B党支部生成一条党费记录
						//若党员已交纳党费，则当月党费记录保留在A党支部，B党支部不生成记录
						//党费记录要找上一个月的
						if (day<=15){
							//本月没交党费
							if (cmonth.getPaidAmount()==null || cmonth.getPaidAmount() == 0){
								//如果toOrgId是空 就是转出镇外 而且没交纳党费的  要删除党费记录
								if (StringUtil.isEmpty(relationshipTransfer.getToOrgId())){
									Long id = cmonth.getId();
									partyDueDao.delete(id);
								}else {
									//删除本月党费记录
									cmonth.setOrgId(relationshipTransfer.getToOrgId());
									cmonth.setOrgName(relationshipTransfer.getToOrgName());
									cmonth.setInsertTime(new Date());
									cmonth.setBase(lastmonth.getBase());
									cmonth.setAmount(lastmonth.getAmount());
									partyDueDao.update(cmonth);
								}
							}
						}else {
							//15号之后不论党员是否交纳党费，则该党员的当月党费记录保留在A党支部，状态为“未交纳”，B党支部不产生党员当月党费记录
							//党费记录要找上一个月的
							PartyDue partyDue = new PartyDue();
							partyDue.setId(byCondition.get(0).getId());
							partyDue.setRemark(PartyDueStatusEnum.NOT_PAY.getInfo());
							partyDueDao.update(partyDue);
						}
					}
				}
			}

		}
    }

    /**
     * 待接收（审核通过）
     *
     * @param relationshipTransfer
     */
    private void receive(RelationshipTransferVO relationshipTransfer) {
        PartyMemberQuery partyMemberQuery = new PartyMemberQuery();
        partyMemberQuery.setId(relationshipTransfer.getId());
        partyMemberQuery.setApproveOrgId(relationshipTransfer.getApproveOrgId());
        partyMemberQuery.setChangeTime(new Date());
        partyMemberQuery.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.PASSED.getValue());
        partyMemberQuery.setStatuses(Arrays.asList(new Integer[]{PartyMemberApprovalServiceImpl.ApprovalStatusEnum.RECEIVE.getValue()}));
        int result = relationshipTransferDao.updateByStatus(partyMemberQuery);
        if (result > 0) {
            relationshipTransfer.setStatus(PartyMemberApprovalServiceImpl.ApprovalStatusEnum.PASSED.getValue());
            relationshipTransfer.setApproveOrgId(relationshipTransfer.getApproveOrgId());
            disposeRecords(relationshipTransfer, "申请通过", null);
            buildPartyMember(relationshipTransfer, true);
        } else {
            throw new BusinessException("表单审核失败");
        }
    }

    /**
     * 新增党员或转移党员
     *
     * @param relationshipTransferVO
     */
    private void buildPartyMember(RelationshipTransferVO relationshipTransferVO, Boolean isExecute) {
        // 镇外转入镇内
        if (relationshipTransferVO.getFromOrgId() == null && relationshipTransferVO.getToOrgId() != null) {
            if (isExecute) {
                PartyMember partyMember = new PartyMember();
                BeanUtils.copyProperties(relationshipTransferVO.getModification(), partyMember);
                partyMember.setInsertTime(new Date());
                partyMember.setOrgId(relationshipTransferVO.getToOrgId());
                PartyMember rollOutPartyMember = partyMemberDao.findByIdCard(partyMember.getIdCard());
                ExServiceman exServiceman = relationshipTransferVO.getModification().getExServiceman();
                if (rollOutPartyMember != null && rollOutPartyMember.getOrgId() != null) {
                    throw new BusinessException("该党员已存在");
                } else if (rollOutPartyMember != null && rollOutPartyMember.getOrgId() == null) {
                    partyMember.setId(rollOutPartyMember.getId());
                    partyMember.setOrgId(relationshipTransferVO.getToOrgId());
                    partyMember.setStatus(PartyMember.judgeStatus(partyMember));
                    partyMemberDao.update(partyMember);
                    if (exServiceman != null) {
                        ExServiceman previous = exServicemanDao.findByIdCard(partyMember.getIdCard());
                        if (previous == null) {
                            exServiceman.setIdCard(partyMember.getIdCard());
                            exServicemanDao.add(exServiceman);
                        } else {
                            exServiceman.setId(previous.getId());
                            exServicemanDao.update(exServiceman);
                        }
                    }
                } else {
                    partyMember.setStatus(PartyMember.judgeStatus(partyMember));
                    partyMemberDao.add(partyMember);
                    if (!ObjectUtil.checkObjAllFieldsIsNull(exServiceman)) {
                        exServiceman.setIdCard(partyMember.getIdCard());
                        exServicemanDao.add(exServiceman);
                    }
                }
            }
        }
        // 镇内转镇外
        if (relationshipTransferVO.getFromOrgId() != null && relationshipTransferVO.getToOrgId() == null) {
            partyMemberDao.updateByTransfer(relationshipTransferVO.getModification().getId(), null,
                    relationshipTransferVO.getFromOrgId(),
                    relationshipTransferVO.getToOrgName());
        }
        // 镇内转镇内
        if (relationshipTransferVO.getToOrgId() != null && relationshipTransferVO.getFromOrgId() != null) {
            partyMemberDao.updateByTransfer(relationshipTransferVO.getModification().getId(),
                    relationshipTransferVO.getToOrgId(),
                    relationshipTransferVO.getFromOrgId(),
                    relationshipTransferVO.getToOrgName());
        }
    }

    /**
     * 回填记录信息
     *
     * @param transferVO
     * @param content
     * @param reason
     */
    private void disposeRecords(RelationshipTransferVO transferVO, String content, String reason) {
        Organization handleOrg = organizationDao.findById(getCurrentUserInfo().getPbOrgId());
        Boolean flag = false;
        Set<Long> allAudits = new HashSet<>();
        switch (PartyMemberApprovalServiceImpl.ApprovalStatusEnum.getByValue(transferVO.getStatus())) {
            case AUDIT:
                Done done = buildDone(transferVO, handleOrg.getName(), reason, content);
                doneDao.add(done);
                List<Long> nextAudits = sysServiceFeign.queryAllUserByPbOrgId(transferVO.getApproveOrgId());
                if (nextAudits != null && nextAudits.size() > 0) {
                    /*pushUtil.sendMessage(transferVO.getId(), getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 1), TargetTypeEnum.USER,
                            nextAudits.toArray(new Long[nextAudits.size()]), new String[]{transferVO.getModification().getName(),
                                    getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 1).getInfo(),
                                    transferVO.getFromOrgName(),
                                    transferVO.getToOrgName()},
                            getCurrentUserInfo().getRealName(), transferVO.getVersion() + 1);*/
                    MqSendMessage mqSendMessageAudit= AppPushMessageUtil.pushUserTaskByAlias( getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 1), nextAudits.toArray(new Long[nextAudits.size()]), new String[]{transferVO.getModification().getName(),
                            getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 1).getInfo(),
                            transferVO.getFromOrgName(),
                            transferVO.getToOrgName()},transferVO.getId(),transferVO.getVersion() + 1,getCurrentUserInfo().getRealName(),new Date(),null);
                    sendMqPushUtil.sendMqMessage(mqSendMessageAudit);
                }
                break;
            case RECEIVE:
                Done done_receive = buildDone(transferVO, handleOrg.getName(), reason, content);
                doneDao.add(done_receive);
                break;
            case PASSED:
                List<String> topOrgId = seriesOrgIds(transferVO.getUserOrgId().toString());
                if (transferVO.getToOrgId() != null) {
                    List<String> rollOutOrgId = seriesOrgIds(transferVO.getToOrgId());
                    if (rollOutOrgId.size() > 0) {
                        topOrgId.addAll(rollOutOrgId);
                    }
                }
                List<String> orgids = organizationDao.seriesOrganizations(topOrgId);
                // 党员转出
                if (transferVO.getToOrgId() == null && transferVO.getFromOrgId() != null) {
                    String localOrgId = transferVO.getModification().getOrgId();
                    if (localOrgId != null)
                        orgids.add(localOrgId);
                }
                if (transferVO.getFromOrgId() != null) {
                    orgids.add(transferVO.getFromOrgId());
                }
                List<Long> actors = sysServiceFeign.queryAllUserByPbOrgIds(orgids);
                allAudits.addAll(new HashSet<>(actors));
               /* pushUtil.sendMessage(transferVO.getId(), getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 3), TargetTypeEnum.USER,
                        allAudits.toArray(new Long[allAudits.size()]), new String[]{transferVO.getModification().getName(), getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 3).getInfo(),
                                transferVO.getFromOrgName(),
                                transferVO.getToOrgName()},
                        getCurrentUserInfo().getRealName(), transferVO.getVersion() + 1);*/

                MqSendMessage mqSendMessagePassed=AppPushMessageUtil.pushUserTaskByAlias( getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 1),  allAudits.toArray(new Long[allAudits.size()]),  new String[]{transferVO.getModification().getName(), getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 3).getInfo(),
                        transferVO.getFromOrgName(),
                        transferVO.getToOrgName()},transferVO.getId(),transferVO.getVersion() + 1,getCurrentUserInfo().getRealName(),new Date(),null);
                sendMqPushUtil.sendMqMessage(mqSendMessagePassed);

                flag = true;
            case RETURN:
                Done done_ = buildDone(transferVO, handleOrg.getName(), reason, content);
                doneDao.add(done_);
                if (!flag) {
                    List<Long> reclaimer = sysServiceFeign.queryAllUserByPbOrgId(transferVO.getApproveOrgId());
                   /* pushUtil.sendMessage(transferVO.getId(), getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 2), TargetTypeEnum.USER,
                            reclaimer.toArray(new Long[reclaimer.size()]), new String[]{transferVO.getModification().getName(),
                                    getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 2).getInfo(),
                                    transferVO.getFromOrgName(),
                                    transferVO.getToOrgName()},
                            getCurrentUserInfo().getRealName(), transferVO.getVersion() + 1);*/
                    MqSendMessage mqSendMessageReturn=AppPushMessageUtil.pushUserTaskByAlias( getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 2),reclaimer.toArray(new Long[reclaimer.size()]),new String[]{transferVO.getModification().getName(),
                            getMessageTemplateEnumCommit(transferVO.getFromOrgId(), transferVO.getToOrgId(), 2).getInfo(),
                            transferVO.getFromOrgName(),
                            transferVO.getToOrgName()},transferVO.getId(),transferVO.getVersion() + 1, getCurrentUserInfo().getRealName(), new Date(),null);
                    sendMqPushUtil.sendMqMessage(mqSendMessageReturn);


                }
                break;
            default:
                break;
        }
    }

    /**
     * 获取一系列组织ID
     *
     * @param orgId
     * @return
     */
    public List<String> seriesOrgIds(String orgId) {
        List<String> orgIds = new ArrayList<>();
        int level = RadixUtil.getLevel(orgId, true);
        for (int i = 1; i <= level; i++) {
            String nextOrgId = RadixUtil.getFixedOrgId(orgId, i);
            orgIds.add(nextOrgId);
        }
        return orgIds;
    }
   //待办消息模板方法
    private MessageTemplateEnum getMessageTemplateEnumCommit(String fromOrgId, String toOrgId, Integer type) {
        MessageTemplateEnum templateEnum = null;
        // 镇内转移
        if (fromOrgId != null && toOrgId != null) {
            // 新增 1
            if (type == 1) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_TOWN_COMMIT;
            }
            // 驳回 2
            if (type == 2) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_TOWN_REJECT;
            }
            // 通过 3
            if (type == 3) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_TOWN_YES;
            }
        }
        // 镇内转镇外
        if (fromOrgId != null && toOrgId == null) {
            // 新增 1
            if (type == 1) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_OUT_COMMIT;
            }
            // 驳回 2
            if (type == 2) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_OUT_REJECT;
            }
            // 通过 3
            if (type == 3) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_OUT_YES;
            }
        }
        // 镇外转镇内
        if (fromOrgId == null && toOrgId != null) {
            // 新增 1
            if (type == 1) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_INTO_COMMIT;
            }
            // 驳回 2
            if (type == 2) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_INTO_REJECT;
            }
            // 通过 3
            if (type == 3) {
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_TRANSFER_INTO_YES;
            }
        }
        return templateEnum;
    }


    private Done buildDone(RelationshipTransferVO transferVO, String orgName, String reason, String content) {
        if (transferVO != null) {
            Done done = new Done();
            done.setOrgId(getCurrentUserInfo().getPbOrgId());
            done.setOrgName(orgName);

            done.setUserId(getCurrentUserInfo().getId());
            done.setReason(transferVO.getStatus() == PartyMemberApprovalServiceImpl.ApprovalStatusEnum.RETURN.getValue() ? reason : null);
            done.setContent(content);
            done.setCorrelationId(transferVO.getId());
            done.setLastHandlerName(orgName);

            // 主题
            done.setTopic(TopicTypeEnum.TRANSFER.getValue());
            if (transferVO.getFromOrgId() == null && transferVO.getToOrgId() != null) {
                done.setAction(ActionTransferEnum.INTOWN.value);
            }
            if (transferVO.getFromOrgId() != null && transferVO.getToOrgId() == null) {
                done.setAction(ActionTransferEnum.OUTSIDE.value);
            }
            if (transferVO.getFromOrgId() != null && transferVO.getToOrgId() != null) {
                done.setAction(ActionTransferEnum.TRANSFER.value);
            }
            done.setSubmitTime(new Date());
            done.setHandleTime(new Date());
            return done;
        }
        return null;
    }

    @AllArgsConstructor
    enum ActionTransferEnum {
        /**
         * 动作 对于党员信息的主题：1新增 2停用 3启用；
         * 对于党员组织关系转移的主题：1转移 2转入 3转出 4转出到镇外 5转入到镇内
         */
        TRANSFER(1, "转移"),

        SHIFTTO(2, "转入"),

        ROLLOUT(3, "转出"),

        OUTSIDE(4, "转出镇外"),

        INTOWN(5, "转入到镇内");

        /**
         * 枚举值
         */
        private int value;
        /**
         * 枚举叙述
         */
        private String phrase;

        public static ActionTransferEnum getByValue(int value) {
            for (ActionTransferEnum actionTransferEnum : values()) {
                if (actionTransferEnum.value == value) {
                    return actionTransferEnum;
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

    @AllArgsConstructor
    public enum ApprovalStatusEnum {
        /**
         * 默认配置
         */
        DRAFT(0, "草稿"),

        AUDIT(1, "审核中"),

        PASSED(2, "已通过"),

        RETURN(3, "已退回"),

        RECEIVE(4, "待接收"),

        UNRECEIVE(5, "未接受");

        /**
         * 枚举值
         */
        private int value;
        /**
         * 枚举叙述
         */
        private String phrase;

        public static ApprovalStatusEnum getByValue(int value) {
            for (ApprovalStatusEnum approvalStatusEnum : values()) {
                if (approvalStatusEnum.value == value) {
                    return approvalStatusEnum;
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

    @AllArgsConstructor
    public enum TopicTypeEnum {
        /**
         * 默认配置
         */
        PARTYMEMBER(1, "党员信息审批"),

        TRANSFER(2, "党员组织关系转移审批");

        /**
         * 枚举值
         */
        private int value;
        /**
         * 枚举叙述
         */
        private String phrase;

        public static TopicTypeEnum getByValue(int value) {
            for (TopicTypeEnum todoTypeEnum : values()) {
                if (todoTypeEnum.value == value) {
                    return todoTypeEnum;
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

    static class ReturnOrgIdException extends RuntimeException {
        ReturnOrgIdException(String message) {
            super(message);
        }
    }

}
