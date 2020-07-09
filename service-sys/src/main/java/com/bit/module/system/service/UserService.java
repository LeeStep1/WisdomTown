package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.User;
import com.bit.module.system.vo.*;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


public interface UserService {

    /**
     * 登陆
     *
     * @param userLoginVO
     * @return
     */
     BaseVo login(UserLoginVO userLoginVO, HttpServletRequest request);

    /**
     * 登出
     * @return
     */
    BaseVo logout();

    /**
     * @param userVO
     * @return
     */
    BaseVo list(UserVO userVO);

    /**
     * 分页查询
     *
     * @param userVO
     * @return
     */
    BaseVo findByConditionPage(UserVO userVO);

    /**
     * 查看
     *
     * @param id
     * @return
     */
    User findById(Long id);

    /**
     * 保存
     *
     * @param userVO
     */
    void add(UserVO userVO);
	/**
	 * 批量更新设置身份
	 * @param userMultiSetVO
	 * @return
	 */
    void multiSet(UserMultiSetVO userMultiSetVO);

    /**
     * 修改
     *
     * @param userVO
     */
    void update(UserVO userVO);

    /**
     * 停用启用用户
     *
     * @param user
     */
    void switchUser(User user);

    /**
     * 重置密码
     *
     * @param id
     */
    void resetPassword(Long id);

    /**
     * 业务 查看
     *
     * @param id
     * @return
     */
    User findRealById(Long id);

    /**
     * 校验username唯一
     *
     * @param userVO
     * @return
     */
    BaseVo checkUsername(UserVO userVO);



    /**
     * 验证用户是否是党员
     *
     * @param partyMemberVO
     * @return
     */
    BaseVo verifyUser(PartyMemberVO partyMemberVO);



    /**
     * 批量查询应用与用户信息
     * @param userVO appid
     * @param userVO uids
     * @return
     */
    BaseVo batchFindByAppIdAndIds(UserMultiListVO userVO);

    /**
     * 刷新token
     *
     * @param refreshTokenVO
     * @return
     */
    BaseVo refreshToken(RefreshTokenVO refreshTokenVO);

    /**
     * 查询所有
     *
     * @return
     */
    List<User> findAll();

    /**
     * 修改密码
     *
     * @param userVO
     * @return
     */
    BaseVo updatePwd(UserVO userVO);

    /**
     * 根据部门ids 查询用户
     *
     * @param userVO
     * @return
     */
    BaseVo findUserListByDepartmentIds(UserVO userVO);

    /**
     * 部门为2的用户
     *
     * @return
     */
    BaseVo findByAppId();

    /**
     * 获取用户详情，包括部门等信息
     *
     * @return
     */
    BaseVo userInfo(HttpServletRequest request);

    /**
     * 验证token
     * @param token
     * @param tid
     * @return
     */
    Boolean verifyToken(String token,Integer tid);

    /**
     * 校验手机号唯一性
     * @param userVO
     * @return
     */
    BaseVo checkMobileUnique(UserVO userVO);

    /**
     * 校验身份证号唯一性
     * @param userVO
     * @return
     */
    BaseVo checkIdCardUnique(UserVO userVO);

    /**
     * 登录用户授权
     * @param appId
     * @return
     */
    BaseVo setUserAuthorization(Long appId);

    /**
     * 根据验证码修改密码
     * @param userVO
     * @return
     */
    BaseVo updatePwdByVCode(UserVO userVO);

    /**
     * 志愿者用户注册
     * @param userVO 用户详情
     * @return
     */
    BaseVo fastRegisterUser(UserVO userVO);

    /**
     * 校验身份证和手机号是否存在
     * @param userVO
     * @return
     */
    BaseVo checkIdCardOrMobileUnique(UserVO userVO);

    /**
     * 获取政务所有用户列表
     * @return
     */
    BaseVo getOaDepartUsers();
    /**
     * 根据身份证查询
     * @return
     */
    BaseVo queryUserByIdcard(String idcard);

    /**
     * 批量查询志愿者 根据身份证号
     * @param idlist
     * @return
     */
    List<Long> batchSelectByCardId(List<String> idlist);

    /**
     * 党建app登录
     * @return
     */
    BaseVo appPbLogin(UserLoginVO userLoginVO, HttpServletRequest request);

    /**
     * 党建app注册
     * @return
     */
    BaseVo appPbRegister(User user);

    /**
     * 党建app注册 验证身份证
     * @return
     */
    BaseVo appPbVerifyIdCard(User user);

    /**
     * 党建app注册 验证手机号
     * @return
     */
    BaseVo appPbVerifyMobile(User user);

    /**
     * 切换当前身份
     * @return
     */
    BaseVo switchIdentiy(Long identyId);

    /**
     * 党建app 验证短信验证码
     * @param user
     * @return
     */
    BaseVo appPbVerifySmsCode(User user);

	/**
	 * 社区使用 根据手机号查询居委会账号
	 * @param mobile
	 * @return
	 */
	User getUserByMobile(String mobile);

	/**
	 * 社区使用 更新居委会账号密码
	 * @param user
	 * @return
	 */
	Boolean updateOrgPwd(User user);

	/**
	 * 根据社区id查询社区管理员
	 * @param orgId
	 * @return
	 */
	List<User> getUserByCboDep(Long orgId);

	/**
	 * 编辑用户
	 * @param userVO
	 * @return
	 */
	BaseVo edit(UserVO userVO);

	/**
	 * 配置
	 * @param userVO
	 * @return
	 */
	BaseVo config(UserVO userVO);

	/**
	 * 配置返显
	 * @return
	 */
	BaseVo configReflect(Long userId);
}
