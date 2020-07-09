package com.bit.module.system.importConfig;


import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.bit.module.system.bean.User;
import com.bit.module.system.bean.UserImportTemplate;
import com.bit.module.system.bean.UserTemplate;
import com.bit.module.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @author liuyancheng
 * @create 2019-01-17 15:01
 */
@Component
public class ImportUserExcelVerifyConfig implements IExcelVerifyHandler<UserImportTemplate> {

    @Autowired
    private UserDao userDao;

    @Override
    public ExcelVerifyHandlerResult verifyHandler(UserImportTemplate userTemplate) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult();

        //这里开始校验
        //1.先校验用户名是否重复
        User param = new User();
        param.setUsername(userTemplate.getUserName());
        User userByUserName = userDao.findByParam(param);
        if (userByUserName != null){
            result.setMsg("该用户已存在");
            result.setSuccess(false);
            return result;
        }
        //2.在校验身份证号是否存在
        param = new User();
        param.setIdcard(userTemplate.getIdCard());
        User userByIdCard = userDao.findByParam(param);
        if (userByIdCard != null){
            result.setMsg("该用户已存在");
            result.setSuccess(false);
            return result;
        }

        result.setSuccess(true);
        return result;
    }
}
