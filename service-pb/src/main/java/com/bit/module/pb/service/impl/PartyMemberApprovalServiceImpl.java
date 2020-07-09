package com.bit.module.pb.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.dao.*;
import com.bit.module.pb.feign.SysServiceFeign;
import com.bit.module.pb.service.PartyMemberApprovalService;
import com.bit.module.pb.vo.partyMember.PartyMemberApprovalVO;
import com.bit.module.pb.vo.partyMember.PartyMemberPageVO;
import com.bit.module.pb.vo.partyMember.PartyMemberParams;
import com.bit.module.pb.vo.partyMember.PartyMemberQuery;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.support.PushUtil;
import com.bit.utils.RadixUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * PartyMemberApproval的Service实现类
 *
 * @author codeGenerator
 */
@Slf4j
@Service("partyMemberApprovalService")
public class PartyMemberApprovalServiceImpl extends BaseService implements PartyMemberApprovalService {

    @Autowired
    private PartyMemberApprovalDao partyMemberApprovalDao;

    @Autowired
    private OrganizationDao organizationDao;

    @Autowired
    private PartyMemberDao partyMemberDao;

    @Autowired
    private ExServicemanDao exServicemanDao;

    @Autowired
    private DoneDao doneDao;

  /*  @Autowired
    private PushUtil pushUtil;*/

    @Autowired
    private SysServiceFeign sysServiceFeign;

    @Autowired
    private SendMqPushUtil sendMqPushUtil;

    /**
     * 根据条件查询PartyMemberApproval
     *
     * @param partyMemberQuery
     * @return
     */
    @Override
    public BaseVo findByConditionPage(PartyMemberQuery partyMemberQuery) {
        partyMemberQuery.setApproveOrgId(getCurrentUserInfo().getPbOrgId());
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        List<PartyMemberApproval> list = partyMemberApprovalDao.findByConditionPage(partyMemberQuery);
        PageInfo<PartyMemberApproval> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public BaseVo findByDisablePage(PartyMemberQuery partyMemberQuery) {
        if (partyMemberQuery.getOrgId() == null) {
            Long orgId = Long.parseLong(getCurrentUserInfo().getPbOrgId());
            Long orgMaxId = Long.parseLong(RadixUtil.getLevelEndID(orgId.toString()));
            partyMemberQuery.setOrgId(orgId);
            partyMemberQuery.setOrgMaxId(orgMaxId);
        }
        PageHelper.startPage(partyMemberQuery.getPageNum(), partyMemberQuery.getPageSize());
        List<PartyMemberPageVO> list = partyMemberDao.findByDisablePage(partyMemberQuery);
        PageInfo<PartyMemberPageVO> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 通过主键查询单个PartyMemberApproval
     *
     * @param id
     * @return
     */
    @Override
    public PartyMemberApprovalVO findById(Long id) {
        return partyMemberApprovalDao.findById(id);
    }

    /**
     * 新增或更新
     * PartyMemberApproval
     *
     * @param partyMemberParams
     */
    @Override
    public void add(PartyMemberParams partyMemberParams) throws BusinessException {
        Organization organization = organizationDao.findById(getCurrentUserInfo().getPbOrgId());
        if (organization != null && organization.getIsApprovalAuz() == 0) {
            throw new BusinessException("该账号所在的组织不能申请");
        }
        PartyMemberApproval partyMemberApproval = buildPartyMemberApproval(partyMemberParams);
        partyMemberApproval.setType(ActionPartyMemberEnum.ADD.value);
        // 启用
        if (partyMemberParams.getMemberId() != null && partyMemberParams.getReason() == null) {
            partyMemberApproval.setType(ActionPartyMemberEnum.ENABLED.value);
            partyMemberApproval.setRemark(partyMemberParams.getRemark());
        }
        // 停用
        if (partyMemberParams.getMemberId() != null && partyMemberParams.getReason() != null) {
            partyMemberApproval.setType(ActionPartyMemberEnum.DISABLE.value);
            partyMemberApproval.setReason(partyMemberParams.getReason());
            partyMemberApproval.setRemark(partyMemberParams.getRemark());
        }
        if (partyMemberParams.getAttach() != null && partyMemberParams.getAttach().size() > 0) {
            partyMemberApproval.setAttach(partyMemberParams.getAttach());
        } else {
            partyMemberApproval.setAttach(new ArrayList<>());
        }
        // 回填用户信息
        UserInfo userInfo = getCurrentUserInfo();
        partyMemberApproval.setUserId(userInfo.getId());
        partyMemberApproval.setUserOrgId(userInfo.getPbOrgId().toString());
        if (partyMemberParams.getId() == null) {
            partyMemberApproval.setStatus(ApprovalStatusEnum.DRAFT.value);
            partyMemberApproval.setInsertTime(new Date());
            partyMemberApproval.setApproveOrgId(getCurrentUserInfo().getPbOrgId());
            List<PartyMemberApproval> list = partyMemberApprovalDao.findByIdCard(partyMemberApproval.getModification().getIdCard());
            if (list != null && list.size() > 0) {
                throw new BusinessException("该党员的申请已存在");
            }
            partyMemberApprovalDao.add(partyMemberApproval);
        } else {
            partyMemberApproval.setId(partyMemberParams.getId());
            partyMemberApprovalDao.update(partyMemberApproval);
        }
    }


    @Override
    @Transactional
    public void submit(Long id) {
        // 获取党员审核信息
        PartyMemberApprovalVO approval = partyMemberApprovalDao.findById(id);
        //20191107添加校验党员身份证
        //只校验新增党员的情况
        if (approval.getType().equals(ActionPartyMemberEnum.ADD.value)){
            String card = approval.getIdCard().replace("\"","");
            PartyMember byIdCard = partyMemberDao.findByIdCard(card);
            if (byIdCard!=null){
                throwBusinessException("身份证重复");
            }
        }
        PartyMemberQuery query = new PartyMemberQuery();
        query.setId(approval.getId());
        query.setStatus(ApprovalStatusEnum.AUDIT.getValue());
        String nextOrgId = null;
        try {
            nextOrgId = getNextOrgId(approval.getOrgId().toString());
        } catch (RelationshipTransferServiceImpl.ReturnOrgIdException e) {
            nextOrgId = e.getMessage();
        }
        query.setApproveOrgId(nextOrgId);
        query.setStatuses(Arrays.asList(new Integer[]{ApprovalStatusEnum.DRAFT.getValue(), ApprovalStatusEnum.RETURN.getValue()}));
        int result = partyMemberApprovalDao.updateByStatus(query);
        if (result > 0) {
            approval.setApproveOrgId(nextOrgId);
            approval.setStatus(ApprovalStatusEnum.AUDIT.value);
            disposeRecords(approval, "提交申请", null);
        } else {
            throw new BusinessException("表单提交失败");
        }
    }

    private String getNextOrgId(String orgId) throws RelationshipTransferServiceImpl.ReturnOrgIdException {
        Organization nextOrg = organizationDao.findDirectSuperiorById(orgId, true);
        if (nextOrg != null) {
            int level = RadixUtil.getLevel(nextOrg.getId(), true);
            if (level == 1) {
                throw new RelationshipTransferServiceImpl.ReturnOrgIdException(nextOrg.getId());
            }
            if (nextOrg.getIsApprovalAuz() == 0 && level > 1) {
                getNextOrgId(nextOrg.getId());
            }
            throw new RelationshipTransferServiceImpl.ReturnOrgIdException(nextOrg.getId());
        }
        return orgId;
    }

    @Override
    @Transactional
    public void sendBack(Long id, String reason) {
        PartyMemberApprovalVO approval = partyMemberApprovalDao.findById(id);
        PartyMemberQuery query = new PartyMemberQuery();
        String lastOrgId = null;
        try {
            lastOrgId = getLastOrgId(approval.getOrgId().toString(), approval.getApproveOrgId());
        } catch (RelationshipTransferServiceImpl.ReturnOrgIdException e) {
            lastOrgId = e.getMessage();
        }
        query.setId(approval.getId());
        if (approval.getOrgId().toString().equals(lastOrgId)) {
            String topOrgId = RadixUtil.getTopOrgId(approval.getOrgId().toString());
            query.setApproveOrgId(topOrgId);
            query.setStatus(ApprovalStatusEnum.RETURN.getValue());
            query.setChangeTime(new Date());

            approval.setApproveOrgId(lastOrgId);
        } else {
            query.setApproveOrgId(lastOrgId);
            query.setStatus(ApprovalStatusEnum.AUDIT.getValue());

            approval.setApproveOrgId(lastOrgId);
        }
        query.setStatuses(Arrays.asList(new Integer[]{ApprovalStatusEnum.AUDIT.getValue()}));
        int result = partyMemberApprovalDao.updateByStatus(query);
        if (result > 0) {
            approval.setStatus(ApprovalStatusEnum.RETURN.value);
            disposeRecords(approval, "申请退回", reason);
        } else {
            throw new BusinessException("表单退回失败");
        }
    }

    /**
     * 退回，获取下级节点
     *
     * @param originalOrgId
     * @param targetOrgId
     * @return
     */
    private String getLastOrgId(String originalOrgId, String targetOrgId) throws RelationshipTransferServiceImpl.ReturnOrgIdException {
        if (originalOrgId == targetOrgId)
            throw new RelationshipTransferServiceImpl.ReturnOrgIdException(originalOrgId);
        int level = RadixUtil.getLevel(targetOrgId, true);
        String orgId = RadixUtil.getFixedOrgId(originalOrgId, level + 1);
        if (orgId == null)
            throw new RelationshipTransferServiceImpl.ReturnOrgIdException(originalOrgId);
        Organization lastOrg = organizationDao.findById(orgId);
        if (lastOrg != null) {
            if (lastOrg.getIsApprovalAuz() == 0 && !lastOrg.getId().equals(originalOrgId)) {
                getLastOrgId(originalOrgId, lastOrg.getId());
            } else {
                throw new RelationshipTransferServiceImpl.ReturnOrgIdException(lastOrg.getId());
            }
        }
        return originalOrgId;
    }


    @Override
    @Transactional
    public PartyMemberApprovalVO pass(Long id) {
        PartyMemberApprovalVO approval = partyMemberApprovalDao.findById(id);
        if (!getCurrentUserInfo().getPbOrgId().equals(approval.getApproveOrgId())) {
            throw new BusinessException("非审核组织");
        }
        String approvalOrgId = null;
        try {
            approvalOrgId = getNextOrgId(approval.getApproveOrgId());
        } catch (RelationshipTransferServiceImpl.ReturnOrgIdException e) {
            approvalOrgId = e.getMessage();
        }
        PartyMemberQuery query = new PartyMemberQuery();
        query.setId(approval.getId());
        if (RadixUtil.getLevel(approvalOrgId, true) == 1) {
            query.setStatus(ApprovalStatusEnum.PASSED.getValue());
        } else {
            query.setStatus(ApprovalStatusEnum.AUDIT.getValue());
        }
        query.setApproveOrgId(approvalOrgId);
        query.setStatuses(Arrays.asList(new Integer[]{ApprovalStatusEnum.AUDIT.getValue()}));
        query.setChangeTime(new Date());
        int result = partyMemberApprovalDao.updateByStatus(query);
        if (result > 0) {
            approval.setStatus(query.getStatus());
            approval.setApproveOrgId(approvalOrgId);
            disposeRecords(approval, "申请通过", null);
            // 更改党员信息
            buildPartyMember(approval);
        } else {
            throw new BusinessException("表单审核失败");
        }
        return approval;
    }

    /**
     * 删除PartyMemberApproval
     *
     * @param id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        PartyMemberApprovalVO partyMemberApproval = partyMemberApprovalDao.findById(id);
        Integer status = partyMemberApproval.getStatus();
        if (ApprovalStatusEnum.DRAFT.getValue() == status) {
            partyMemberApprovalDao.delete(id);
        } else {
            throw new BusinessException("该申请已提交，不能删除");
        }
    }

    /**
     * 回填记录信息
     *
     * @param approval
     * @param content
     * @param reason
     */
    private void disposeRecords(PartyMemberApprovalVO approval, String content, String reason) {
        Boolean flag = false;
        switch (ApprovalStatusEnum.getByValue(approval.getStatus())) {
            case AUDIT:
                Organization localOrganization = organizationDao.findById(String.valueOf(approval.getOrgId()));
                Done done = buildDone(approval, localOrganization == null ? null : localOrganization.getName(), reason, content);
                doneDao.add(done);
                // 获取上级组织的ID
                List<Long> nextAudits = sysServiceFeign.queryAllUserByPbOrgId(approval.getApproveOrgId());
                /*pushUtil.sendMessage(approval.getId(), getMessageTemplateCommit(approval.getType()), TargetTypeEnum.USER,
                        nextAudits.toArray(new Long[nextAudits.size()]),
                        new String[]{approval.getModification().getName(), getMessageTemplateCommit(approval.getType()).getInfo(), approval.getModification().getOrgName()},
                        getCurrentUserInfo().getRealName(), approval.getVersion() + 1);*/

                MqSendMessage mqSendMessage= AppPushMessageUtil.pushUserTaskByAlias(getMessageTemplateCommit(approval.getType()), nextAudits.toArray(new Long[nextAudits.size()]),   new String[]{approval.getModification().getName(), getMessageTemplateCommit(approval.getType()).getInfo(), approval.getModification().getOrgName()},approval.getId(),approval.getVersion() + 1, getCurrentUserInfo().getRealName(),new Date(),null);
                sendMqPushUtil.sendMqMessage(mqSendMessage);
                break;
            case PASSED:
                Set<Long> audits = new HashSet<>();
                int level = RadixUtil.getLevel(getCurrentUserInfo().getPbOrgId(), true);
                if (level == 1) {
                    // 审核通过
                    List<String> auditUserIds = seriesOrgIds(approval.getOrgId().toString());
                    String targetOrgId = approval.getModification().getOrgId();
                    if (targetOrgId != null) {
                        auditUserIds.add(targetOrgId);
                    }
                    List<String> orgids = organizationDao.seriesOrganizations(auditUserIds);
                    List<Long> noticeUserIds = sysServiceFeign.queryAllUserByPbOrgIds(orgids);
                    audits.addAll(new HashSet<>(noticeUserIds));
                } else {
                    // 审核中
                    List<Long> topAudits = sysServiceFeign.queryAllUserByPbOrgId(approval.getApproveOrgId());
                    audits.addAll(new HashSet<>(topAudits));
                }
                // 消息推送
               /* pushUtil.sendMessage(approval.getId(), getMessageTemplatePass(approval.getType()), TargetTypeEnum.USER,
                        audits.toArray(new Long[audits.size()]),
                        new String[]{approval.getModification().getName(), getMessageTemplatePass(approval.getType()).getInfo(), approval.getModification().getOrgName()},
                        getCurrentUserInfo().getRealName(), approval.getVersion() + 1);*/

                MqSendMessage mqSendMessageAduit= AppPushMessageUtil.pushUserMessageByAlias(getMessageTemplatePass(approval.getType()), audits.toArray(new Long[audits.size()]),new String[]{approval.getModification().getName(), getMessageTemplatePass(approval.getType()).getInfo(), approval.getModification().getOrgName()},getCurrentUserInfo().getRealName(),new Date(),null);
                sendMqPushUtil.sendMqMessage(mqSendMessageAduit);
                flag = true;
            case RETURN:
                if (!flag) {
                    List<Long> lastAudits = sysServiceFeign.queryAllUserByPbOrgId(approval.getApproveOrgId());
                   /* pushUtil.sendMessage(approval.getId(), getMessageTemplateReject(approval.getType()), TargetTypeEnum.USER,
                            lastAudits.toArray(new Long[lastAudits.size()]),
                            new String[]{approval.getModification().getName(), getMessageTemplateReject(approval.getType()).getInfo(), approval.getModification().getOrgName()},
                            getCurrentUserInfo().getRealName(), approval.getVersion() + 1);*/

                    MqSendMessage mqSendMessageAduitReject=AppPushMessageUtil.pushUserTaskByAlias(getMessageTemplateReject(approval.getType()), lastAudits.toArray(new Long[lastAudits.size()]),new String[]{approval.getModification().getName(), getMessageTemplateReject(approval.getType()).getInfo(), approval.getModification().getOrgName()},approval.getId(),approval.getVersion() + 1,getCurrentUserInfo().getRealName(),new Date(),null );
                    sendMqPushUtil.sendMqMessage(mqSendMessageAduitReject);
                }

                Organization handleOrg = organizationDao.findById(getCurrentUserInfo().getPbOrgId());
                Done done_ = buildDone(approval, handleOrg.getName(), reason, content);
                doneDao.add(done_);
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
    //选取党建待办任务消息的模板
    private MessageTemplateEnum getMessageTemplateCommit(Integer type) {
        MessageTemplateEnum templateEnum = null;
        switch (type) {
            case 1:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_ADD_COMMIT;
                break;
            case 2:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_DISABLE_COMMIT;
                break;
            case 3:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_ENABLED_COMMIT;
                break;
            default:
                break;
        }
        return templateEnum;
    }

    private MessageTemplateEnum getMessageTemplateReject(Integer type) {
        MessageTemplateEnum templateEnum = null;
        switch (type) {
            case 1:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_ADD_REJECT;
                break;
            case 2:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_DISABLE_REJECT;
                break;
            case 3:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_ENABLED_REJECT;
                break;
        }
        return templateEnum;
    }

    private MessageTemplateEnum getMessageTemplatePass(Integer type) {
        MessageTemplateEnum templateEnum = null;
        switch (type) {
            case 1:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_ADD_YES;
                break;
            case 2:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_DISABLE_YES;
                break;
            case 3:
                templateEnum = MessageTemplateEnum.PB_PARTYMEMBER_APPROVAL_ENABLED_YES;
                break;
        }
        return templateEnum;
    }

    private Done buildDone(PartyMemberApprovalVO partyMemberApproval, String orgName, String reason, String content) {
        if (partyMemberApproval != null) {
            Done done = new Done();
            done.setOrgId(getCurrentUserInfo().getPbOrgId());
            done.setOrgName(orgName);

            done.setUserId(getCurrentUserInfo().getId());
            done.setReason(partyMemberApproval.getStatus() == ApprovalStatusEnum.RETURN.getValue() ? reason : null);
            done.setContent(content);
            done.setCorrelationId(partyMemberApproval.getId());
            done.setLastHandlerName(orgName);

            // 主题
            done.setTopic(RelationshipTransferServiceImpl.TopicTypeEnum.PARTYMEMBER.getValue());
            done.setAction(partyMemberApproval.getType());

            done.setSubmitTime(new Date());
            done.setHandleTime(new Date());
            return done;
        }
        return null;
    }

    /**
     * 回填党员申请信息
     *
     * @param partyMemberParams
     * @return
     */
    private PartyMemberApproval buildPartyMemberApproval(PartyMemberParams partyMemberParams) {
        PartyMemberApproval partyMemberApproval = new PartyMemberApproval();
        PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
        // 启动或停用
        if (partyMemberParams.getMemberId() != null) {
            PartyMember partyMember = partyMemberDao.findByCondition(partyMemberParams.getMemberId(), null);
            BeanUtils.copyProperties(partyMember, partyMemberSummary);
            partyMemberSummary.setId(partyMemberParams.getMemberId());
            partyMemberSummary.setOrgId(partyMember.getOrgId());
        } else {
            // 新增
            BeanUtils.copyProperties(partyMemberParams, partyMemberSummary);
            partyMemberSummary.setId(null);
            partyMemberSummary.setOrgId(partyMemberParams.getOrgId());
        }
        Organization organization = organizationDao.findById(partyMemberSummary.getOrgId());
        partyMemberSummary.setOrgName(organization.getName());
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
        }
        partyMemberApproval.setModification(partyMemberSummary);
        return partyMemberApproval;
    }

    /**
     * 新增党员或启用、停用党员
     *
     * @param partyMemberParams
     */
    private void buildPartyMember(PartyMemberApprovalVO partyMemberParams) {
        switch (ActionPartyMemberEnum.getByValue(partyMemberParams.getType())) {
            case ADD:
                PartyMember partyMember = new PartyMember();
                BeanUtils.copyProperties(partyMemberParams.getModification(), partyMember);
                partyMember.setInsertTime(new Date());
                partyMember.setStatus(PartyMember.judgeStatus(partyMember));
                partyMemberDao.add(partyMember);
                ExServiceman exServiceman = partyMemberParams.getModification().getExServiceman();
                if (exServiceman != null) {
                    if (exServiceman.getOrigin() != null) {
                        ExServiceman exServiceman1 = new ExServiceman();
                        BeanUtils.copyProperties(exServiceman, exServiceman1);
                        exServiceman1.setIdCard(partyMember.getIdCard());
                        exServicemanDao.add(exServiceman1);
                    }
                }
                partyMemberParams.setMemberId(partyMember.getId());
                break;
            case DISABLE:
                // 状态，0待完善 1正常 2停用
                partyMemberDao.updateByStatus(partyMemberParams.getMemberId(), PartyMemberServiceImpl.PMStatusEnum.DISABLE.getValue(), partyMemberParams.getReason());
                break;
            case ENABLED:
                partyMemberDao.updateByStatus(partyMemberParams.getMemberId(), PartyMemberServiceImpl.PMStatusEnum.NORMAL.getValue(), null);
                break;
        }
    }

    @AllArgsConstructor
    public enum ActionPartyMemberEnum {
        // 1新增 2停用 3启用；

        ADD(1, "新增"),

        DISABLE(2, "停用"),

        ENABLED(3, "启用");

        /**
         * 枚举值
         */
        private int value;
        /**
         * 枚举叙述
         */
        private String phrase;

        public static ActionPartyMemberEnum getByValue(int value) {
            for (ActionPartyMemberEnum actionPartyMemberEnum : values()) {
                if (actionPartyMemberEnum.value == value) {
                    return actionPartyMemberEnum;
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
    enum ApprovalStatusEnum {
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

}
