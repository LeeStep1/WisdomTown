package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.BusinessRole;
import com.bit.module.system.service.FlowService;
import com.bit.module.system.vo.BusinessRoleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flow")
public class FlowController {

    @Autowired
    private FlowService flowService;

    /**
     * 新增记录
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody BusinessRole businessRole){
        return new BaseVo();
    }

    /**
     * 编辑记录
     * @param businessRole
     * @return
     */
    @PutMapping("/update")
    public BaseVo update(@RequestBody BusinessRole businessRole){
        return flowService.update(businessRole);
    }

    /**
     * 返显记录
     * @param flowId
     * @return
     */
    @GetMapping("/reflect/{flowId}")
    public BaseVo reflectByFlowId(@PathVariable(value = "flowId")Integer flowId){
        return flowService.reflectByFlowId(flowId);
    }

    /**
     * 分页查询
     * @param businessRoleVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody BusinessRoleVO businessRoleVO){
        return flowService.listPage(businessRoleVO);
    }



    /**
     * 根据角色id的列表批量查询用户
     * @param roleIds
     * @return
     */
    @PostMapping("/queryUserByRoleBatch")
    public BaseVo queryUserByRoleBatach(@RequestBody List<Long> roleIds){
        return flowService.queryUserByRoleBatch(roleIds);
    }

}
