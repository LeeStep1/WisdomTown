package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.PartnerOrg;
import com.bit.module.vol.service.PartnerOrgService;
import com.bit.module.vol.vo.PartnerOrgVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenduo
 * @create 2019-03-19 13:27
 */
@RestController
@RequestMapping("/partner")
public class PartnerOrgController {

    @Autowired
    private PartnerOrgService partnerOrgService;

    /**
     * 添加共建单位审核记录
     * @param partnerOrg
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody PartnerOrg partnerOrg,HttpServletRequest request){
        return partnerOrgService.add(partnerOrg,request);
    }
    /**
     * 更新共建单位
     * @param partnerOrg
     * @return
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody PartnerOrg partnerOrg, HttpServletRequest request){
        return partnerOrgService.update(partnerOrg,request);
    }

    /**
     * 反显记录
     * @param id
     * @return
     */
    @GetMapping("/reflect/{id}")
    public BaseVo reflect(@PathVariable(value = "id")Long id){
        return partnerOrgService.reflectById(id);
    }

    /**
     * 分页查询审核记录
     * @param partnerOrgVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PartnerOrgVO partnerOrgVO){
        return partnerOrgService.listPage(partnerOrgVO);
    }

    /**
     * 获取资料
     * @param id
     * @return
     */
    @GetMapping("/data/{id}")
    public BaseVo getdataById(@PathVariable(value = "id") Long id){
        return partnerOrgService.getdataById(id);
    }


    @GetMapping("/getTest")
    public BaseVo getTest(){
        return partnerOrgService.getTest();
    }

}
