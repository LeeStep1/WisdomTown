package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.User;
import com.bit.module.system.service.TemplateService;
import com.bit.module.system.vo.UserTemplateVO;
import com.bit.module.system.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuyancheng
 * @create 2019-01-16 13:06
 */
@RestController
@RequestMapping("/template")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    /**
     * 用户批量导出
     * @param user
     * @param response
     * @return
     */
    @PostMapping("/exportUser")
    public void exportUser(@RequestParam(value = "realName",required = false)String realName,
                           @RequestParam(value = "idcard",required = false)String idcard,
                           @RequestParam(value = "mobile",required = false)String mobile,
                           @RequestParam(value = "appId",required = false)Integer appId,
                           @RequestParam(value = "status",required = false)Integer status,
                           @RequestParam(value = "createType",required = false)Integer createType,
                           HttpServletResponse response){
        templateService.exportUser(realName,idcard,mobile,appId,status,createType,response);
    }

    /**
     * 用户导入用户excel
     * @param file
     * @return
     */
    @PostMapping("/importUserExcel")
    public void importUserExcel(@RequestParam MultipartFile file,HttpServletResponse response){
        templateService.importUserExcel(file,response);
    }

    /**
     * 用户批量导入下载模板
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response){
        templateService.downloadTemplate(response);
    }
}
