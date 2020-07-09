package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.User;
import com.bit.module.system.vo.UserTemplateVO;
import com.bit.module.system.vo.UserVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author liuyancheng
 * @create 2019-01-16 13:06
 */
public interface TemplateService {

    /**
     * 导出用户
     * @param user
     * @param response
     * @return
     */
    void exportUser(String realName,String idcard,String mobile,Integer appId,Integer status,Integer createType, HttpServletResponse response);


    /**
     * 下载模板
     */
    void downloadTemplate(HttpServletResponse response);
    /**
     * 用户导入excel
     * @param file
     * @return
     */
    void importUserExcel(MultipartFile file,HttpServletResponse response);
}
