package com.bit.module.pb.controller;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.service.PartyMemberService;
import com.bit.module.pb.service.RelationshipTransferService;
import com.bit.module.pb.service.impl.RelationshipTransferServiceImpl;
import com.bit.module.pb.vo.partyMember.PartyMemberParams;
import com.bit.module.pb.vo.partyMember.PartyMemberQuery;
import com.bit.module.pb.vo.partyMember.RelationshipTransferInfo;
import com.bit.module.pb.vo.partyMember.RelationshipTransferVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 组织关系转移接口
 *
 * @author generator
 */
@Slf4j
@RestController
@RequestMapping(value = "/relationshipTransfer")
public class RelationshipTransferController {

    @Autowired
    private RelationshipTransferService relationshipTransferService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private PartyMemberService partyMemberService;

    /**
     * 党员转移分页
     *
     * @param partyMemberQuery
     * @return
     */
    @Deprecated
    @PostMapping("/_query")
    public BaseVo query(@RequestBody PartyMemberQuery partyMemberQuery) {
        List<Organization> organizationList = organizationService.findByBasicUnit(null);
        if (organizationList.size() == 0) {
            partyMemberQuery.setOrgId(Long.parseLong(partyMemberService.getUserInfo().getPbOrgId()));
        }
        return relationshipTransferService.findByConditionPage(partyMemberQuery);
    }

    /**
     * 获取组织创建的党员申请分页
     *
     * @param partyMemberQuery
     * @param type
     * @return
     */
    @PostMapping("/query")
    public BaseVo queryRelationship(@RequestBody PartyMemberQuery partyMemberQuery, @RequestParam(value = "type") Integer type) {
        return relationshipTransferService.queryPage(partyMemberQuery, type);
    }


    /**
     * 获取待审核的申请
     *
     * @param partyMemberQuery
     * @return
     */
    @PostMapping("/roll/query")
    public BaseVo queryRoll(@RequestBody PartyMemberQuery partyMemberQuery) {
        return relationshipTransferService.queryAuditRelationship(partyMemberQuery);
    }

    /**
     * 党员转移分页
     *
     * @param type
     * @param partyMemberQuery
     * @return
     */
    @PostMapping("/{type}/_query")
    public BaseVo query(@PathVariable(value = "type") Integer type, @RequestBody PartyMemberQuery partyMemberQuery) {
        partyMemberQuery.setTransferType(type);
        return relationshipTransferService.findByTransferPage(partyMemberQuery);
    }

    /**
     * 根据主键ID查询RelationshipTransfer
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        RelationshipTransferVO relationshipTransfer = relationshipTransferService.findById(id);
        relationshipTransfer.getModification().setOrgName(relationshipTransfer.getToOrgName());
        BaseVo baseVo = new BaseVo();
        baseVo.setData(relationshipTransfer);
        return baseVo;
    }

    /**
     * 新增RelationshipTransfer
     *
     * @param partyMemberParams RelationshipTransfer实体
     * @return
     */
    @PostMapping("/_create")
    public BaseVo add(@Valid @RequestBody PartyMemberParams partyMemberParams) {
        relationshipTransferService.add(partyMemberParams);
        return new BaseVo();
    }

    /**
     * 提交申请
     *
     * @param params
     * @return
     */
    @PostMapping("/_submit")
    public BaseVo submit(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        if (id == null) {
            throw new BusinessException("缺少ID参数");
        }
        // 更改状态
        relationshipTransferService.updateFlow(Long.valueOf(id.toString()),
                RelationshipTransferServiceImpl.ApprovalStatusEnum.AUDIT.getValue(), "提交申请");
        return new BaseVo();
    }

    /**
     * 审核通过
     *
     * @param params
     * @return
     */
    @Transactional
    @PostMapping("/_pass")
    public BaseVo pass(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        if (id == null) {
            throw new BusinessException("缺少ID参数");
        }
        // 更新流程状态
        relationshipTransferService.updateFlow(Long.valueOf(id.toString()),
                RelationshipTransferServiceImpl.ApprovalStatusEnum.PASSED.getValue(), null);
        return new BaseVo();
    }

    /**
     * 接收
     *
     * @param params
     * @return
     */
    @Transactional
    @PostMapping("/_receive")
    public BaseVo receive(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        if (id == null) {
            throw new BusinessException("缺少ID参数");
        }
        // 更新流程状态
        relationshipTransferService.updateFlow(Long.valueOf(id.toString()),
                RelationshipTransferServiceImpl.ApprovalStatusEnum.RECEIVE.getValue(), null);
        return new BaseVo();
    }

    /**
     * 退回申请
     *
     * @param params
     * @return
     */
    @Transactional
    @PostMapping("/_sendBack")
    public BaseVo sendBack(@RequestBody Map<String, Object> params) {
        Object id = params.get("id");
        Object reason = params.get("reason");
        if (id == null) {
            throw new BusinessException("缺少ID参数");
        }
        if (reason == null) {
            throw new BusinessException("缺少原因参数");
        }
        relationshipTransferService.updateFlow(Long.valueOf(id.toString()),
                RelationshipTransferServiceImpl.ApprovalStatusEnum.RETURN.getValue(), reason == null ? null : reason.toString());
        return new BaseVo();
    }

    /**
     * 根据主键ID删除RelationshipTransfer
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        relationshipTransferService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据身份证获取转移信息
     *
     * @param idCard
     * @return
     */
    @GetMapping("/idCard/{idCard}/_query")
    public BaseVo findByIdCard(@Validated @PathVariable(value = "idCard") String idCard) {
        List<RelationshipTransferInfo> relationshipTransferInfoList = relationshipTransferService.findByIdCard(idCard);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(relationshipTransferInfoList);
        return baseVo;
    }

}
