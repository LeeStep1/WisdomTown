package com.bit.module.pb.controller;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.service.PartyDueService;
import com.bit.module.pb.service.PartyMemberApprovalService;
import com.bit.module.pb.service.impl.PartyMemberApprovalServiceImpl;
import com.bit.module.pb.vo.partyMember.PartyMemberApprovalVO;
import com.bit.module.pb.vo.partyMember.PartyMemberParams;
import com.bit.module.pb.vo.partyMember.PartyMemberQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * 党员审批接口
 *
 * @author generator
 */
@Slf4j
@RestController
@RequestMapping(value = "/partyMemberApproval")
public class PartyMemberApprovalController {

    @Autowired
    private PartyMemberApprovalService partyMemberApprovalService;

    @Autowired
    private PartyDueService partyDueService;

    /**
     * 新增 党员、启用、停用
     *
     * @param partyMemberParams PartyMemberApproval实体
     * @return
     */
    @PostMapping("/_create")
    public BaseVo add(@Valid @RequestBody PartyMemberParams partyMemberParams) {
        partyMemberApprovalService.add(partyMemberParams);
        return new BaseVo();
    }

    /**
     * 分页查询PartyMemberApproval列表
     */
    @PostMapping("/_query")
    public BaseVo listPage(@RequestBody PartyMemberQuery partyMemberQuery) {
        // 分页对象，前台传递的包含查询的参数
        return partyMemberApprovalService.findByConditionPage(partyMemberQuery);
    }

    /**
     * 查看党员审批信息
     * 根据主键ID查询PartyMemberApproval
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        PartyMemberApprovalVO partyMemberApproval = partyMemberApprovalService.findById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(partyMemberApproval);
        return baseVo;
    }

    /**
     * 提交申请
     *
     * @param param PartyMemberApproval的id
     * @return
     */
    @PostMapping("/_submit")
    public BaseVo submit(@RequestBody Map<String, Object> param) {
        Object id = param.get("id");
        if (id == null) {
            throw new BusinessException("审批信息的ID不能为空");
        }
        partyMemberApprovalService.submit(Long.valueOf(id.toString()));
        return new BaseVo();
    }

    /**
     * 退回
     *
     * @param param
     * @return
     */
    @PostMapping("/_sendBack")
    public BaseVo sendBack(@RequestBody Map<String, Object> param) {
        Object id = param.get("id");
        Object reason = param.get("reason");
        if (id == null) {
            throw new BusinessException("审批信息的ID不能为空");
        }
        partyMemberApprovalService.sendBack(Long.valueOf(id.toString()), reason == null ? null : reason.toString());
        return new BaseVo();
    }

    /**
     * 通过
     *
     * @param param
     * @return
     */
    @Transactional
    @PostMapping("/_pass")
    public BaseVo pass(@RequestBody Map<String, Object> param) {
        Object id = param.get("id");
        if (id == null) {
            throw new BusinessException("审批信息的ID不能为空");
        }
        PartyMemberApprovalVO partyMemberApproval = partyMemberApprovalService.pass(Long.valueOf(id.toString()));
        // 修改党员信息（新增、修改）
        updatePartyMember(partyMemberApproval);
        return new BaseVo();
    }

    /**
     * 根据主键ID删除PartyMemberApproval
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        partyMemberApprovalService.delete(id);
        return new BaseVo();
    }

    /**
     * 【党员申请、启动、停用】
     * 新增、修改党员信息
     *
     * @param partyMemberApproval
     * @return
     */
    private void updatePartyMember(PartyMemberApprovalVO partyMemberApproval) {
        if (partyMemberApproval != null) {
            switch (PartyMemberApprovalServiceImpl.ActionPartyMemberEnum.getByValue(partyMemberApproval.getType())) {
                case ADD:
                    // 新增或转移党员党费
                    partyDueService.movePartyDueThisMonth(partyMemberApproval.getMemberId(), partyMemberApproval.getModification().getOrgId());
                    break;
                case DISABLE:
                    // 停用党员党费
                    partyDueService.stopMemberPartyDueThisMonth(partyMemberApproval.getMemberId());
                    break;
                case ENABLED:
                    // 启用
                    break;
                default:
                    break;
            }
        }
    }

}
