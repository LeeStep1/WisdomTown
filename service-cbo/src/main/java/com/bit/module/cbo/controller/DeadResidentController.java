package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.DeadResident;
import com.bit.module.cbo.service.DeadResidentService;
import com.bit.module.cbo.vo.DeadResidentVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 死亡居民信息相关controller
 * @author: liyang
 * @date: 2019-07-22
 **/
@RestController
@RequestMapping(value = "/deadResident")
public class DeadResidentController {

    /**
     * 死亡居民信息相关service
     */
    @Autowired
    private DeadResidentService deadResidentService;

    /**
     * 新增死亡居民信息
     * @author liyang
     * @date 2019-07-22
     * @param deadResident : 新增详情
     * @return : BaseVo
    */
    @PostMapping("/add")
    public BaseVo add(@RequestBody DeadResident deadResident){

        return deadResidentService.add(deadResident);

    }

    /**
     * 修改死亡居民信息
     * @author liyang
     * @date 2019-07-23
     * @param deadResident : 修改详情
     * @return : BaseVo
    */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody DeadResident deadResident){
        return deadResidentService.modify(deadResident);
    }

    /**
     * 返回死亡居民信息列表接口
     * @author liyang
     * @date 2019-07-23
     * @param deadResidentVO : 查询条件
     * @return : BaseVo
    */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody DeadResidentVO deadResidentVO){
        return deadResidentService.findAll(deadResidentVO);
    }

    /**
     * 批量导出死亡居民信息
     * @author liyang
     * @date 2019-12-06
     * @param deadResident : 查询条件
     * @return : response
    */
    @PostMapping("/exportToExcel")
    public void exportToExcel(@RequestBody DeadResident deadResident,HttpServletResponse response){
        deadResidentService.exportToExcel(deadResident,response);
    }

    /**
     * 返回死亡信息详情
     * @author liyang
     * @date 2019-07-23
     * @param id : id
     * @return : BaseVo
    */
    @GetMapping("/detail/{id}")
    public BaseVo detail(@PathVariable(value = "id") Long id){
        return deadResidentService.detail(id);
    }

    /**
     * 删除死亡居民信息
     * @author liyang
     * @date 2019-07-23
     * @param id : 信息id
     * @return : BaseVo
    */
    @DeleteMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id){
        return deadResidentService.delete(id);
    }
}
