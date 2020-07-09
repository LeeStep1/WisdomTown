package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.ResidentApplyGuide;
import com.bit.module.cbo.bean.ResidentApplyGuideItems;
import com.bit.module.cbo.service.ResidentApplyGuideService;
import com.bit.module.cbo.vo.ResidentApplyGuideVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description: 办事指南，类别与事项 相关controller
 * @author: liyang
 * @date: 2019-08-06
 **/
@RestController
@RequestMapping("/residentApplyGuide")
public class ResidentApplyGuideController {

    /**
     * 办事指南，类别与事项 相关Service
     */
    @Autowired
    private ResidentApplyGuideService residentApplyGuideService;

    /**
     * 增加 办事指南类别和事项
     * @author liyang
     * @date 2019-08-06
     * @param residentApplyGuideVO :  办事指南类别和事件详情
     * @return : BaseVo
    */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody ResidentApplyGuideVO residentApplyGuideVO){
        return residentApplyGuideService.add(residentApplyGuideVO);
    }

    /**
     * 办事指南类别和事项排序
     * @param residentApplyGuideList 办事指南类别和事项新的顺序
     * @return
     */
    @PostMapping("/sortGuid")
    public BaseVo sortGuid(@RequestBody List<ResidentApplyGuide> residentApplyGuideList){
        return residentApplyGuideService.sortGuid(residentApplyGuideList);
    }

    /**
     * 获得类别和事项列表
     * @author liyang
     * @date 2019-08-07
     * @param residentApplyGuide : 查询条件
     * @return : BaseVo
    */
    @PostMapping("/findGuide")
    public BaseVo findGuide(@RequestBody ResidentApplyGuide residentApplyGuide){
        return residentApplyGuideService.findGuide(residentApplyGuide);
    }

    /**
     * 修改办事指南类别和事件
     * @author liyang
     * @date 2019-08-07
     * @param residentApplyGuideVO : 修改详情
     * @return : BaseVo
    */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody ResidentApplyGuideVO residentApplyGuideVO){
        return residentApplyGuideService.modify(residentApplyGuideVO);
    }

    /**
     * 修改办事指南类别和事项
     * @author liyang
     * @date 2019-08-07
     * @param id : 办事指南类别和事项ID
     * @param type : 数据类别:1 类别，0事项
     * @param enable : 是否停用：1 启用，0 停用
     * @return : BaseVo
    */
    @PutMapping("/modifyFlg/{id}/{type}/{enable}")
    public BaseVo modifyFlg(@PathVariable(value = "id") Long id,
                            @PathVariable(value = "type" ) Integer type,
                            @PathVariable(value = "enable" ) Integer enable){
        return residentApplyGuideService.modifyFlg(id,type,enable);
    }

    /**
     * 根据ID查询办事指南明细
     * @author liyang
     * @date 2019-08-08
     * @param id : id
     * @param type : 数据类别:1 类别，0事项
     * @return : BaseVo
    */
    @GetMapping("/queryId/{id}/{type}")
    public BaseVo queryId(@PathVariable(value = "id") Long id,
                          @PathVariable(value = "type") Integer type){

        return residentApplyGuideService.queryId(id,type);
    }

    /**
     * 查询含有补充业务的类别和事项
     * @author liyang
     * @date 2019-08-19
     * @return : BaseVo
    */
    @GetMapping("/queryGuideRoster")
    public BaseVo queryGuideRoster(){
        return residentApplyGuideService.queryGuideRoster();
    }
}
