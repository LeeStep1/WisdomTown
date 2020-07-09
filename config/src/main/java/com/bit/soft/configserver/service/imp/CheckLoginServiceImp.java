package com.bit.soft.configserver.service.imp;

import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.soft.configserver.bean.User;
import com.bit.soft.configserver.dao.UserDao;
import com.bit.soft.configserver.service.CheckLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service()
public class CheckLoginServiceImp extends BaseService implements CheckLoginService{

    @Autowired
    private UserDao userDao;

    @Value("${login.name}")
    private String name;

    @Value("${login.password}")
    private String password;

    /**
     * 校验登录用户
     * @param user 用户信息
     * @return
     */
    @Override
    public BaseVo checkLogin(User user) {

        //密码MD5转换
        String md5Password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
        user.setPassword(md5Password);

        //校验用户名密码
        Integer count = userDao.checkUserSql(user);
        if(count>0){
            BaseVo baseVo = new BaseVo();
            baseVo.setMsg("1");
            return baseVo;
        }else {
            throw new BusinessException("用户名或密码错误！");
        }

    }

    /**
     * 插入一个用户
     */
    @Override
    public BaseVo insertUser(){

        //读取配置文件中用户名和密码
        User user = new User();
        user.setUserName(name);
        user.setPassword(DigestUtils.md5DigestAsHex(password.getBytes()));

        //插入数据库
        userDao.insertUserSql(user);

        BaseVo baseVo = new BaseVo();
        baseVo.setMsg("1");
        return baseVo;

    }
}
