package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.PmcStaff;
import com.bit.module.cbo.service.PmcStaffService;
import com.bit.module.cbo.vo.PmcStaffVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 物业公司员工相关controller
 * @author: liyang
 * @date: 2019-07-18
 **/
@RestController
@RequestMapping("/pmcStaff")
public class PmcStaffController {

    @Autowired
    private PmcStaffService pmcStaffService;

    /**
     * 新增物业人员
     * @author liyang
     * @date 2019-07-19
     * @param pmcStaff : 物业员工信息
     * @return : BaseVo
    */
    @PostMapping("/add")
    public BaseVo add(@RequestBody PmcStaff pmcStaff){
        return pmcStaffService.add(pmcStaff);
    }

    /**
     * 修改物业公司员工信息
     * @author liyang
     * @date 2019-07-19
     * @param pmcStaff : 修改详情
     * @return : BaseVo
    */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody PmcStaff pmcStaff){
        return pmcStaffService.modify(pmcStaff);
    }

    /**
     * 返回物业人员列表
     * @author liyang
     * @date 2019-07-22
     * @param pmcStaffVO : 查询条件
     * @return : BaseVo
    */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody PmcStaffVO pmcStaffVO){
        return pmcStaffService.findAll(pmcStaffVO);
    }

    /**
     * 物业人员停用/启用
     * @author liyang
     * @date 2019-07-22
     * @param id : 要修改的ID
     * @param status : 要修改的状态
     * @return : BaseVo
    */
    @GetMapping("/modifyFlg/{id}/{status}")
    public BaseVo modifyFlg(@PathVariable(value = "id") Long id,
                            @PathVariable(value = "status") Integer status){

        return pmcStaffService.modifyFlg(id,status);
    }

}
