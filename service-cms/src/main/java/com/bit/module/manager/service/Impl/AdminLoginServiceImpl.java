package com.bit.module.manager.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.manager.bean.AdminLogin;
import com.bit.module.manager.bean.PageResult;
import com.bit.module.manager.bean.PortalUser;
import com.bit.module.manager.dao.PortalUserDao;
import com.bit.module.manager.service.AdminLoginService;
import com.bit.module.manager.vo.PortalUserVo;
import com.bit.module.manager.vo.RefreshTokenVO;
//import com.bit.module.mqCore.MqBean.UserMessage;
import com.bit.util.MongoUtil;
import com.bit.util.StringRandom;
import com.bit.core.utils.CacheUtil;
import com.bit.utils.MD5Util;
import com.bit.utils.UUIDUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bit.common.cmsenum.cmsEnum.USING_FLAG;


/**
 * @description:
 * @author: chenduo
 * @create: 2019-05-06 15:44
 */
@Service("adminLoginService")
public class AdminLoginServiceImpl extends BaseService implements AdminLoginService{
    @Autowired
    private CacheUtil cacheUtil;
    @Value("${atToken.expire}")
    private String atTokenExpire;
    @Value("${rtToken.expire}")
    private String rtTokenExpire;
    @Autowired
    private MongoUtil mongoUtil;

    @Autowired
    private PortalUserDao portalUserDao;

    /**
     * admin登陆
     * @param adminLogin
     * @return
     */
    @Override
    public BaseVo adminLogin(AdminLogin adminLogin) {
        String username = adminLogin.getUserName();
        String password = adminLogin.getPassWord();

        if (username.equals("admin") && password.equals("123456")){
            UserInfo userInfo = new UserInfo();
            String token = UUIDUtil.getUUID();
            userInfo.setToken(token);
            userInfo.setIdcard("120101199008190073");
            userInfo.setId(0L);
            userInfo.setMobile("13011399819");
            userInfo.setUsername(adminLogin.getUserName());
            userInfo.setTid(adminLogin.getTerminalId());
            userInfo.setRealName("管理员");
            String userJson = JSON.toJSONString(userInfo);
            Integer tid = null;
            if (adminLogin.getTerminalId().equals(Const.OA_DOOR)){
                tid = Const.OA_DOOR;
            }
            if (adminLogin.getTerminalId().equals(Const.PB_DOOR)){
                tid = Const.PB_DOOR;
            }

            cacheUtil.set(Const.TOKEN_PREFIX+ tid+":"+token, userJson,Long.valueOf(atTokenExpire));

            //rt token 失效时间为7天
            String rtToken = UUIDUtil.getUUID();
            RefreshTokenVO refreshTokenVO = new RefreshTokenVO();
            refreshTokenVO.setUserInfo(userInfo);
            refreshTokenVO.setAtKey(Const.TOKEN_PREFIX+tid+":"+token);
            String rtJson = JSON.toJSONString(refreshTokenVO);
            cacheUtil.set(Const.REFRESHTOKEN_TOKEN_PREFIX + adminLogin.getTerminalId() + ":" + rtToken, rtJson, Long.valueOf(rtTokenExpire));

            Map map = new HashMap<>();
            map.put("token", token);
            map.put("refreshToken", rtToken);
            map.put("id",0L);
            map.put("username",adminLogin.getUserName());
            map.put("realName","管理员");
            BaseVo baseVo = new BaseVo();
            baseVo.setData(map);
            return baseVo;
        }else {
            throw new BusinessException("登陆失败");
        }
    }

    /**
     * admin登出
     * @return
     */
    @Override
    public BaseVo adminLogout() {

        //获取token
        UserInfo userInfo = getCurrentUserInfo();
        String token = userInfo.getToken();
        String terminalId = userInfo.getTid().toString();

        cacheUtil.del("token:"+terminalId+":"+token);
        return new BaseVo();

    }

    /**
     * mongo分页测试
     * @return
     */
/*    @Override
    public BaseVo mongotest() {
        Query query = new Query();
        Query total = new Query();
        Criteria criteria = Criteria.where("msgType").is(2);
        Sort.Order order = new Sort.Order(Sort.Direction.DESC,"recTime");
        PageResult<UserMessage> message = mongoUtil.listPage(query, total, criteria, order, 1, 50, "message", UserMessage.class);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(message);
        return baseVo;
    }*/

    /**
     * 用户新增
     * @author liyang
     * @date 2019-06-11
     * @param user : 用户详情
     * @return : BaseVo
    */
    @Override
    public BaseVo add(PortalUser user) {

        //随机密码盐
        String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);

        //密码和盐=新密码  md5加密新密码
        String password= MD5Util.compute(Const.RESET_PASSWORD + salt);
        user.setPassword(password);
        user.setSalt(salt);

        //状态  0 启用  1 停用
        user.setStatus(USING_FLAG.getCode());

        //新增时间
        user.setInsertTime(new Date());

        //修改时间
        user.setUpdateTime(new Date());
        portalUserDao.addNew(user);
        return successVo();
    }

    /**
     * 用户列表展示
     * @author liyang
     * @date 2019-06-11
     * @return : BaseVo
    */
    @Override
    public BaseVo findAll(PortalUserVo portalUserVo) {
        PageHelper.startPage(portalUserVo.getPageNum(), portalUserVo.getPageSize());
        List<PortalUser> portalUserLIst = portalUserDao.findAll();
        PageInfo<PortalUser> pageInfo = new PageInfo<PortalUser>(portalUserLIst);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public BaseVo update(PortalUser portalUser) {

        //如果是修改密码
        if(portalUser.getPassword() !=null && !("").equals(portalUser.getPassword())){

            //随机密码盐
            String salt = StringRandom.getStringRandom(Const.RANDOM_PASSWORD_SALT);

            //密码和盐=新密码  md5加密新密码
            String password= MD5Util.compute(portalUser.getPassword() + salt);
            portalUser.setPassword(password);
            portalUser.setSalt(salt);
        }

        portalUser.setUpdateTime(new Date());
        portalUserDao.update(portalUser);

        return successVo();
    }

    /**
     * 删除用户
     * @author liyang
     * @date 2019-06-11
     * @param id : 用户ID
     * @return : BaseVo
    */
    @Override
    public BaseVo delete(Long id) {
        portalUserDao.delete(id);
        return successVo();
    }
}
