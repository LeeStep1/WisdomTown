package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.User;
import com.bit.module.system.service.UserService;
import com.bit.module.system.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RefreshScope
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 登陆
     * @param userLoginVO
     * @return
     */
    @PostMapping(value = "/login")
    public BaseVo login(@RequestBody @Valid UserLoginVO userLoginVO, BindingResult bindingResult, HttpServletRequest request){
        return  userService.login(userLoginVO,request);
    }
    /**
     * 获取用户详情，包括部门等信息
     * @return
     */
    @PostMapping("/userInfo")
    public BaseVo userInfo(HttpServletRequest request){
        return userService.userInfo(request);
    }

    /**
     * 党建app登录
     * @return
     */
    @PostMapping("/appPbLogin")
    public BaseVo appPbLogin(@RequestBody @Valid UserLoginVO userLoginVO,HttpServletRequest request){
        return userService.appPbLogin(userLoginVO, request);
    }

    /**
     * 党建app注册
     * @return
     */
    @PostMapping("/appPbRegister")
    public BaseVo appPbRegister(@RequestBody User user){
        return userService.appPbRegister(user);
    }

    /**
     * 党建app注册 验证身份证
     * @return
     */
    @PostMapping("/appPbVerifyIdCard")
    public BaseVo appPbVerifyIdCard(@RequestBody User user){
        return userService.appPbVerifyIdCard(user);
    }

    /**
     * 党建app注册 验证手机号
     * @return
     */
    @PostMapping("/appPbVerifyMobile")
    public BaseVo appPbVerifyMobile(@RequestBody User user){
        return userService.appPbVerifyMobile(user);
    }

    /**
     * 党建app 验证短信验证码
     * @param user
     * @return
     */
    @PostMapping("/appPbVerifySmsCode")
    public BaseVo appPbVerifySmsCode(@RequestBody User user){
        return userService.appPbVerifySmsCode(user);
    }

    /**
     * 登出
     * @return
     */
    @PostMapping("/logout")
    public BaseVo logout(){
        return userService.logout();
    }



    /**
     * 分页模糊查询
     * @param userVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody UserVO userVO) {
        return userService.findByConditionPage(userVO);
    }

    /**
     * 根据主键ID查询
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        User user = userService.findById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(user);
        return baseVo;
    }

    /**
     * 根据主键ID查询--业务
     * @param id
     * @return
     */
    @GetMapping("/findRealById/{id}")
    public BaseVo findRealById(@PathVariable(value = "id") Long id) {
        User user = userService.findRealById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(user);
        return baseVo;
    }

    /**
     * 新增
     * @param userVO
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody UserVO userVO,BindingResult bindingResult)  {
        userService.add(userVO);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 批量更新设置身份
     * @param userMultiSetVO
     * @return
     */
    @PostMapping("/multiSet")
    public BaseVo multiSet(@RequestBody UserMultiSetVO userMultiSetVO)  {
        userService.multiSet(userMultiSetVO);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改
     * @param userVO
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody UserVO userVO,BindingResult bindingResult) {
        userService.update(userVO);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 停用启用用户
     * @param
     * @return
     */
    @PostMapping("/switchUser")
    public BaseVo switchUser(@RequestBody User user) {
        userService.switchUser(user);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 重置密码
     * @param
     * @return
     */
    @GetMapping("/resetPassword/{id}")
    public BaseVo resetPassword(@PathVariable(value = "id") Long id) {
        userService.resetPassword(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 校验用户名唯一
     * @param
     * @return
     */
    @PostMapping("/checkUsername")
    public BaseVo checkUsername(@RequestBody UserVO userVO) {
        return userService.checkUsername(userVO);
    }

    /**
     * 验证用户是否是党员
     * @param partyMemberVO
     * @return
     * @author liuyancheng
     */
    @PostMapping("/verifyUser")
    public BaseVo verifyUser(@RequestBody PartyMemberVO partyMemberVO){
        return userService.verifyUser(partyMemberVO);
    }


    /**
     * 刷新token
     * @param refreshTokenVO
     * @return
     * @author liuyancheng
     */
    @PostMapping("/refreshToken")
    public BaseVo refreshToken(@RequestBody RefreshTokenVO refreshTokenVO){
        return userService.refreshToken(refreshTokenVO);
    }

    /**
     * 批量查询用户
     * @param userVO
     * @return
     */
    @PostMapping("/listByAppIdAndIds")
    public BaseVo listByAppIdAndIds(@RequestBody UserMultiListVO userVO){
        return userService.batchFindByAppIdAndIds(userVO);
    }

    /**
     * 修改密码
     * @param userVO
     * @return
     */
    @PostMapping("/updatePwd")
    public BaseVo updatePwd(@RequestBody UserVO userVO){
        return userService.updatePwd(userVO);
    }

    /**
     * 根据部门id集合获取用户列表
     * @param userVO
     * @return
     */
    @PostMapping("/usersByOaDepIds")
    public BaseVo usersByDepIds(@RequestBody UserVO userVO){
        return userService.findUserListByDepartmentIds(userVO);
    }

    /**
     * appId为2的用户
     * @return
     */
    @PostMapping("/usersByAppId")
    public BaseVo usersByAppId(){
        return userService.findByAppId();
    }


    /**
     * 验证token
     * @param token
     * @param tid
     * @return
     */
    @PostMapping("/verifyToken")
    public Boolean verifyToken(@RequestParam String token,@RequestParam Integer tid){
        return userService.verifyToken(token,tid);
    }

    /**
     * 校验手机号唯一性
     * @param userVO
     * @return
     */
    @PostMapping("/checkMobileUnique")
    public BaseVo checkMobileUnique(@RequestBody UserVO userVO){
        return userService.checkMobileUnique(userVO);
    }

    /**
     * 校验身份证号唯一性
     * @param userVO
     * @return
     */
    @PostMapping("/checkIdCardUnique")
    public BaseVo checkIdCardUnique(@RequestBody UserVO userVO){
        return userService.checkIdCardUnique(userVO);
    }

    /**
     * 登录用户授权
     * @param appId 授权信息
     * @return
     */
    @PostMapping("/userAuthorization/{appId}")
    public BaseVo userAuthorization(@PathVariable("appId") Long appId){
        return userService.setUserAuthorization(appId);
    }

    /**
     * 根据验证码修改志愿者APP密码
     * @param userVO
     * @return
     */
    @PostMapping("/updatePwdByVCode")
    public BaseVo updatePwdByVCode(@RequestBody UserVO userVO){
        return userService.updatePwdByVCode(userVO);
    }

    /**
     * 志愿者APP注册用户
     * @param userVO
     * @return
     */
    @PostMapping("/fastRegisterUser")
    public BaseVo registerVol(@RequestBody UserVO userVO){
        return userService.fastRegisterUser(userVO);
    }

    /**
     * 校验身份证和手机号是否存在
     * @param userVO
     * @return
     */
    @PostMapping("/checkIdCardOrMobileUnique")
    public BaseVo checkIdCardOrMobileUnique(@RequestBody UserVO userVO){
        return userService.checkIdCardOrMobileUnique(userVO);
    }

    /**
     * 获取政务所有用户列表
     * @return
     */
    @PostMapping("/getOaDepartUsers")
    public BaseVo getOaDepartUsers(){
        return userService.getOaDepartUsers();
    }

    /**
     * 根据身份证查询
     * @return
     */
    @GetMapping("/queryUserByIdcard/{idcard}")
    public BaseVo queryUserByIdcard(@PathVariable(value = "idcard")String idcard){
        return userService.queryUserByIdcard(idcard);
    }

    /**
     * 批量查询志愿者 根据身份证号
     * @param idlist
     * @return
     */
    @PostMapping("/batchSelectByCardId")
    public List<Long> batchSelectByCardId(@RequestBody List<String> idlist){
        return userService.batchSelectByCardId(idlist);
    }

    /**
     * 切换当前身份
     * @return
     */
    @GetMapping("/switchIdentiy/{identyId}")
    public BaseVo switchIdentiy(@PathVariable(value = "identyId") Long identyId){
        return userService.switchIdentiy(identyId);
    }

    /**
     * 社区使用 根据手机号查询居委会账号
     * @param mobile
     * @return
     */
    @PostMapping("/getUserByMobile")
    public User getUserByMobile(@RequestBody String mobile){
		return userService.getUserByMobile(mobile);
    }

    /**
     * 社区使用 更新居委会账号密码
     * @param user
     * @return
     */
    @PostMapping("/updateOrgPwd")
    public Boolean updateOrgPwd(@RequestBody User user){
		return userService.updateOrgPwd(user);
    }

    /**
     * 根据社区id查询社区管理员
     * @param orgId
     * @return
     */
    @GetMapping("/getUserByCboDep/{orgId}")
    public List<User> getUserByCboDep(@PathVariable(value = "orgId") Long orgId){
        return userService.getUserByCboDep(orgId);
    }


    //============================================neo-sys=========================================================

	/**
	 * 编辑用户
	 * @param userVO
	 * @return
	 */
    @PostMapping("/edit")
    public BaseVo edit(@Valid @RequestBody UserVO userVO){
    	return userService.edit(userVO);
	}

	/**
	 * 配置
	 * @param userVO
	 * @return
	 */
	@PostMapping("/config")
	public BaseVo config(@RequestBody UserVO userVO){
		return userService.config(userVO);
	}

	/**
	 * 配置返显
	 * @return
	 */
	@GetMapping("/configReflect/{userId}")
	public BaseVo configReflect(@PathVariable(value = "userId")Long userId){
		return userService.configReflect(userId);
	}

}
