package com.bit.module.system.dao;

import com.bit.module.system.bean.MapCount;
import com.bit.module.system.bean.PushBinding;
import com.bit.module.system.bean.User;
import com.bit.module.system.bean.UserTemplate;
import com.bit.module.system.vo.UserAndDepVO;
import com.bit.module.system.vo.UserLoginVO;
import com.bit.module.system.vo.UserTemplateVO;
import com.bit.module.system.vo.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * User管理的Dao
 * @author 
 *
 */
@Repository
public interface UserDao {

	/**
	 * 根据条件查询User
	 * @param userVO
	 * @return
	 */
	List<User> findByConditionPage(UserVO userVO);

    /**
     * 根据条件查询User
     * @param user
     * @return
     */
    List<User> listPageForExcel(User user);

	/**
	 * 查询所有User
	 * @return
	 */
	List<User> findAll(@Param(value = "sorter") String sorter);

	/**
	 * 通过主键查询单个User
	 * @param id	 	 
	 * @return
	 */
	User findById(@Param(value = "id") Long id);

	/**
	 * 保存User
	 * @param user
	 */
	void add(User user);

	/**
	 * 保存User新方法
	 * @param userVO
	 */
	void addNew(UserVO userVO);

	/**
	 * 更新User
	 * @param userVO
	 */
	void update(UserVO userVO);

	/**
	 * 删除User
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);


	/**
	 * 根据用户名查询用户信息
	 * @param username
	 * @return
	 */
	User findByUsername(@Param(value = "username") String username);

	/**
	 * 停止启动
	 * @param user
	 */
	void switchUser(User user);

	/**
	 * 重置密码
	 * @param user
	 */
	void resetPassword(User user);

	/**
	 * 校验用户名唯一
	 * @param username
	 * @return
	 */
	int findCountByUsername(@Param(value = "username") String username);

	/**
	 * 按id批量查询
	 * @param map
	 * @return
	 */
	List<UserTemplate> batchFindByIds(@Param("params") Map<String,Object> map);

	/**
	 * 批量查询应用与用户信息
	 * @param ids
	 * @param appId
	 * @return
	 */
	List<User> batchFindByAppIdAndIds(@Param(value = "ids") List<Long> ids,@Param(value = "appId") Integer appId);

	/**
	 * 查询所有User
	 * @return
	 */
	List<UserTemplate> findAllByExcel(UserTemplateVO userTemplateVO);

	/**
	 * 批量插入数据
	 * @param passedUserList
	 */
	void batchAdd(@Param(value = "passedUserList") List<User> passedUserList);

	/**
	 * 根据传入的参数查询用户是否存在
	 * @param user
	 * @return
	 */
	User findByParam(User user);

	/**
	 * 根据部门ids 查询用户
	 * @param map
	 * @return
	 */
	List<User> findByDepartmentIds(@Param("params") Map<String,Object> map);

	/**
	 * 部门为2的用户
	 * @return
	 */
	List<UserAndDepVO> findByAppId();

	/**
	 * 批量按id查询
	 * @param ids
	 * @return
	 */
	List<User> batchSelect(@Param(value = "ids")List<Long> ids);

	/**
	 * 校验手机号唯一性
	 * @param userVO
	 * @return
	 */
	int checkMobile(UserVO userVO);

	/**
	 * 校验身份证号唯一性
	 * @param userVO
	 * @return
	 */
	int checkIdCard(UserVO userVO);

	/**
	 * 校验用户名唯一性
	 * @param userVO
	 * @return
	 */
	int checkUsername(UserVO userVO);

	/**
	 * 根据UserLoginVO参数查询user对象
	 * @param userLoginVO
	 * @return
	 */
	User findByUserLoginVO(UserLoginVO userLoginVO);

	/**
	 * 验证登录用户绑定客户端是否需要修改
	 * @param userLoginVO
	 * @return
	 */
	List<PushBinding> checkUserBindingSql(@Param("user") UserLoginVO userLoginVO);

	/**
	 * 修改用户绑定值
	 * @param userLoginVO
	 */
	void updatePushBindingSql(@Param("user") UserLoginVO userLoginVO);

	/**
	 * 增加用户设备绑定信息
	 * @param userLoginVO
	 */
	void insertPushBindingSql(@Param("user") UserLoginVO userLoginVO);

	/**
	 * 根据手机号修改密码
	 * @param userVO
	 */
	void updateUserForVolSql(@Param("user") UserVO userVO);
	/**
	 * 根据身份证查询
	 * @param idcard
	 */
	User findByIdCard(@Param("idcard")String idcard);

	/**
	 * 批量查询志愿者 根据身份证号
	 * @param idlist
	 * @return
	 */
	List<Long> batchSelectByCardId(@Param(value = "idlist")List<String> idlist);

    /**
     * 批量查询身份证数量
     * @param idcards
     * @return
     */
	List<MapCount> selectCountByIdcards(@Param(value = "idcards") List<String> idcards);

    /**
     * 批量查询手机号数量
     * @param mobiles
     * @return
     */
	List<MapCount> selectCountByMobiles(@Param(value = "mobiles") List<String> mobiles);
    /**
     * 批量查询用户名数量
     * @param usernames
     * @return
     */
	List<MapCount> selectCountByUserNames(@Param(value = "usernames") List<String> usernames);

	/**
	 * 社区使用 根据手机号查询居委会账号
	 * @param mobile
	 * @return
	 */
	User getUserByMobile(@Param(value = "mobile")String mobile);

	/**
	 * 社区使用 更新居委会账号密码
	 * @param userVO
	 * @return
	 */
	int updateOrgPwd(UserVO userVO);

	/**
	 * 批量设置用户状态
	 * @param ids
	 */
	void batchUpdateUserStatus(@Param(value = "ids")List<Long> ids,@Param(value = "status") Integer status);
}
