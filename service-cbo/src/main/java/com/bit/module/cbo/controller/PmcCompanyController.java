package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.PmcCompany;
import com.bit.module.cbo.service.PmcCompanyService;
import com.bit.module.cbo.service.PmcService;
import com.bit.module.cbo.service.PmcStaffService;
import com.bit.module.cbo.vo.PmcCompanyVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 物业公司相关controller
 * @author: liyang
 * @date: 2019-07-18
 **/
@RestController
@RequestMapping("/pmcCompany")
public class PmcCompanyController {

    @Autowired
    private PmcCompanyService pmcCompanyService;
    @Autowired
    private PmcService pmcService;

    /**
     * 新增物业公司
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompany : 新增公司详情
     * @return : BaseVo
    */
    @PostMapping("/add")
    public BaseVo addPmcCompany(@RequestBody PmcCompany pmcCompany){
        return pmcCompanyService.add(pmcCompany);
    }

    /**
     * 修改物业公司
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompany : 修改详情
     * @return : BaseVo
    */
    @PostMapping("/modify")
    public BaseVo modifyPmcCompany(@RequestBody PmcCompany pmcCompany){
        return pmcCompanyService.modify(pmcCompany);
    }

    /**
     * 物业公司列表展示(分页)
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompanyVO : 查询条件
     * @return : BaseVo
    */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody PmcCompanyVO pmcCompanyVO){
        return pmcCompanyService.findAll(pmcCompanyVO);
    }

    /**
     * 根据ID删除物业公司
     * @author liyang
     * @date 2019-07-18
     * @param id : 物业公司ID
     * @return : BaseVo
    */
    @DeleteMapping("/delete/{id}")
    public BaseVo deleteById(@PathVariable(value = "id")Long id){
        return pmcCompanyService.deleteById(id);
    }

    /**
     * 获取所有物业公司
     * @author liyang
     * @date 2019-07-18
     * @return : BaseVo
     */
    @PostMapping("/findAllUseCompany")
    public BaseVo findAllUseCompany(@RequestBody PmcCompany pmcCompany){
        return pmcCompanyService.findAllUseCompany(pmcCompany);
    }

    /**
     * 修改物业公司状态
     * @author liyang
     * @date 2019-07-23
     * @param id : 物业公司ID
     * @param status : 即将成为的状态
     * @return : BaseVo
    */
    @GetMapping("/modifyFlg/{id}/{status}")
    public BaseVo modifyFlg(@PathVariable(value = "id") Long id,
                            @PathVariable(value = "status") Integer status){
        return pmcCompanyService.modifyFlg(id,status);
    }

    /**
     * 物业web 删除物业员工
     * @param id
	 * @date 2019-09-04
	 * @author chenduo
     * @return
     */
    @GetMapping("/delPmcStaff/{id}")
    public BaseVo delPmcStaff(@PathVariable(value = "id")Long id){
        return pmcService.delPmcStaff(id);
    }
}
