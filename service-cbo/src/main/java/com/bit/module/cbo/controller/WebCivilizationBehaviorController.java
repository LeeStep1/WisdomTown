package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.CivilizationBehavior;
import com.bit.module.cbo.service.CivilizationBehaviorService;
import com.bit.module.cbo.vo.CivilizationBehaviorPageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @description: 文明行为相关controller
 * @author: liyang
 * @date: 2019-08-31
 **/
@RestController
@RequestMapping("/web/WebCivilizationBehavior")
public class WebCivilizationBehaviorController {

    /**
     * 文明行为相关service
     */
    @Autowired
    private CivilizationBehaviorService civilizationBehaviorService;

    /**
     * 文明行为上报列表
     * @author liyang
     * @date 2019-08-31
     * @param civilizationBehaviorPageVO : 查询详情
     * @return : BaseVo
    */
    @PostMapping("/findAll")
    public BaseVo findAll(@RequestBody CivilizationBehaviorPageVO civilizationBehaviorPageVO){
        return civilizationBehaviorService.findAll(civilizationBehaviorPageVO);
    }

    /**
     * 修改文明行为状态
     * @author liyang
     * @date 2019-09-02
     * @param civilizationBehavior : 修改详情
     * @return : BaseVo
    */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody CivilizationBehavior civilizationBehavior){
        return civilizationBehaviorService.modify(civilizationBehavior);
    }

    /**
     * 删除文明行为明细
     * @author liyang
     * @date 2019-09-02
     * @param id : id
     * @return : BaseVo
     */
    @GetMapping("/detail/{id}")
    public BaseVo detail(@PathVariable(value = "id") Long id){
        return civilizationBehaviorService.detail(id);
    }

    /**
     * 刪除上報記錄
     * @author liyang
     * @date 2019-09-02
     * @param id : id
     * @return : BaseVo
     */
    @DeleteMapping("delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id){
        return civilizationBehaviorService.delete(id);
    }

}
