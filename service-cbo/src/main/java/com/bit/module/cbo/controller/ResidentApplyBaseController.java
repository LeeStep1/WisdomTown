package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.ResidentApplyBase;
import com.bit.module.cbo.bean.ResidentApplyProgress;
import com.bit.module.cbo.service.ResidentApplyBaseService;
import com.bit.module.cbo.vo.ResidentApplyBaseVO;
import com.bit.module.cbo.vo.ServiceInformationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 台账管理相关controller
 * @author: liyang
 * @date: 2019-08-09
 **/
@RestController
@RequestMapping("/residentApplyBase")
public class ResidentApplyBaseController {

    /**
     * 台账相关service
     */
    @Autowired
    private ResidentApplyBaseService residentApplyBaseService;

    /**
     * 新增台账申请
     * @author liyang
     * @date 2019-08-09
     * @param residentApplyBase : 新增明细
     * @return : BaseVo
    */
    @PostMapping("/add")
    public BaseVo add(@RequestBody ResidentApplyBase residentApplyBase){
        return residentApplyBaseService.add(residentApplyBase);
    }

    /**
     * 获取办事指南所有类别
     * @author liyang
     * @date 2019-08-12
     * @return : BaseVo
    */
    @GetMapping("/findGuide/{status}")
    public BaseVo findGuide(@PathVariable(value = "status") Integer status){
        return residentApplyBaseService.getGuide(status);
    }

    /**
     * 根据办事指南类别获取所有启用的事项
     * @author liyang
     * @date 2019-08-12
     * @param type : 数据类别:1 类别，0事项
     * @param pid : 办事指南类别ID
     * @param status : 状态 0 停用 1启用 2草稿
     * @return : BaseVo
    */
    @GetMapping("/findGuideItems/{type}/{pid}/{status}")
    public BaseVo findGuideItems(@PathVariable(value = "type") Integer type,
                                @PathVariable(value = "pid") Long pid,
                                @PathVariable(value = "status") Integer status){
        return residentApplyBaseService.getGuideItems(type,pid,status);
    }

    /**
     * 获取办事指南台账列表
     * @author liyang
     * @date 2019-08-12
     * @param residentApplyBaseVO : 查询条件
     * @return : BaseVo
    */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody ResidentApplyBaseVO residentApplyBaseVO){
        return residentApplyBaseService.findAll(residentApplyBaseVO);
    }

    /**
     * 根据ID查询台账明细
     * @author liyang
     * @date 2019-08-12
     * @param id : id
     * @return : BaseVo
    */
    @GetMapping("/detail/{id}")
    public BaseVo detail(@PathVariable(value = "id") Long id){
        return residentApplyBaseService.detail(id);
    }

    /**
     * 台账办理步揍展示
     * @author liyang
     * @date 2019-08-13
     * @param applyId : 申请台账ID
     * @return : BaseVo
    */
    @GetMapping("/itemsStep/{applyId}")
    public BaseVo itemsStep(@PathVariable(value = "applyId") Long applyId){
        return residentApplyBaseService.itemsStep(applyId);
    }

    /**
     * 审核办理流程
     * @author liyang
     * @date 2019-08-13
     * @param residentApplyProgress : 修改的明细
     * @return : BaseVo
    */
    @PostMapping("/auditItems")
    public BaseVo auditItems(@RequestBody ResidentApplyProgress residentApplyProgress){
        return residentApplyBaseService.auditItems(residentApplyProgress);
    }

    /**
     * 台账补充业务信息
     * @author liyang
     * @date 2019-08-14
     * @param extendType : 关联的扩展信息关联字典表中扩展信息类型：1 低保申请、2 居家养老 3、残疾人申请 4、特殊扶助
     * @param serviceInformationVO : 业务信息详情
     * @return : BaseVo
    */
    @PostMapping("/supplementaryInformation/{extendType}")
    public BaseVo supplementaryInformation(@RequestBody ServiceInformationVO serviceInformationVO,
                                           @PathVariable Integer extendType){
        return residentApplyBaseService.addServiceInformation(serviceInformationVO,extendType);
    }

    /**
     * 台账进度反馈 修改附件 接口
     * @param residentApplyBaseVO
     * @return
     */
    @PostMapping("/updateApplyBaseInfo")
    public BaseVo updateApplyBaseInfo(@RequestBody ResidentApplyBaseVO residentApplyBaseVO){
		return residentApplyBaseService.updateApplyBaseInfo(residentApplyBaseVO);
    }

	/**
	 * 返显台账的补充业务信息的附件
	 * @param id
	 * @return
	 */
    @GetMapping("/reflectBase/{id}")
    public BaseVo reflectBase(@PathVariable(value = "id")Long id){
        return residentApplyBaseService.reflectBase(id);
    }

    /**
     * 导出办事指南列表
     * @author liyang
     * @date 2019-12-09
     * @param residentApplyBase : 过滤条件
    */
    @PostMapping("/exportToExcel")
    public void exportToExcel(@RequestBody ResidentApplyBase residentApplyBase, HttpServletResponse response){
        residentApplyBaseService.exportToExcel(residentApplyBase,response);
    }
}

