package com.bit.module.system.bean;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author liuyancheng
 * @create 2019-01-17 16:08
 */
@Data
public class UserTemplateFailed extends UserImportTemplate {

    @Excel(name = "错误信息")
    private String errorMsg;

    public static UserTemplateFailed userTemplate2MemberFailed(UserImportTemplate userTemplate) {
        UserTemplateFailed failed = new UserTemplateFailed();
        failed.setErrorMsg(userTemplate.getErrorMsg());
        failed.setUserName(userTemplate.getUserName());
        failed.setRealName(userTemplate.getRealName());
        failed.setIdCard(userTemplate.getIdCard());
        failed.setPhone(userTemplate.getPhone());
        return failed;
    }

    public static List<UserTemplateFailed> userTemplate2MemberFaileds(List<UserImportTemplate> userTemplates) {
        List<UserTemplateFailed> list = new ArrayList<>();
        for (UserImportTemplate UserTemplate : userTemplates) {
            list.add(userTemplate2MemberFailed(UserTemplate));
        }
        return list;
    }
}
