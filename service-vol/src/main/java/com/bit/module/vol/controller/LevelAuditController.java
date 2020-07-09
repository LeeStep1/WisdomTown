package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.LevelAudit;
import com.bit.module.vol.service.LevelAuditService;
import com.bit.module.vol.vo.LevelAuditVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenduo
 * @create 2019-03-20 13:33
 */
@RestController
@RequestMapping("/level")
public class LevelAuditController {

    @Autowired
    private LevelAuditService levelAuditService;

    /**
     * 新增审核记录
     * @param levelAudit
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody LevelAudit levelAudit,HttpServletRequest request){
        return levelAuditService.add(levelAudit,request);
    }
    /**
     * 镇团委更新审核记录
     * @param levelAudit
     * @return
     */
    @PostMapping("/zupdate")
    public BaseVo zupdate(@RequestBody LevelAudit levelAudit,HttpServletRequest request){
        return levelAuditService.zupdate(levelAudit,request);
    }

    /**
     * 服务站更新审核记录
     * @param levelAudit
     * @return
     */
    @PostMapping("/fupdate")
    public BaseVo fupdate(@RequestBody LevelAudit levelAudit,HttpServletRequest request){
        return levelAuditService.fupdate(levelAudit,request);
    }

    /**
     * 反显记录
     * @param id
     * @return
     */
    @GetMapping("/reflect/{id}")
    public BaseVo reflect(@PathVariable(value = "id")Long id,HttpServletRequest request){
        return levelAuditService.reflect(id,request);
    }

    /**
     * 镇团委审核记录分页查询
     * @param levelAuditVO
     * @return
     */
    @PostMapping("/zlistPage")
    public BaseVo zlistPage(@RequestBody LevelAuditVO levelAuditVO){
        return levelAuditService.zlistPage(levelAuditVO);
    }

    /**
     * 服务站审核记录分页查询
     * @param levelAuditVO
     * @return
     */
    @PostMapping("/flistPage")
    public BaseVo flistPage(@RequestBody LevelAuditVO levelAuditVO){
        return levelAuditService.flistPage(levelAuditVO);
    }

    /**
     * app查询自身星级
     * @return
     */
    @GetMapping("/starlevel")
    public BaseVo starlevel(){
        return levelAuditService.starlevel();
    }

    /**
     * 审核记录
     * @param levelAudit
     * @return
     */
    @PostMapping("/log")
    public BaseVo log(@RequestBody LevelAudit levelAudit){
        return levelAuditService.log(levelAudit);
    }



}
