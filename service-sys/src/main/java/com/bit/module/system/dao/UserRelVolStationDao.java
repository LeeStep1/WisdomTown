package com.bit.module.system.dao;

import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.User;
import com.bit.module.system.bean.UserRelVolStation;
import com.bit.module.system.vo.UserRelVolStationVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-22 13:13
 */
@Repository
public interface UserRelVolStationDao {

    void add(UserRelVolStation userRelVolStation);

    void update(UserRelVolStation userRelVolStation);

    List<UserRelVolStation> findByConditionPage(UserRelVolStationVO userRelVolStationVO);

    UserRelVolStation findById(@Param(value = "id")Long id);

    UserRelVolStation findByUserId(@Param(value = "userId")Long userId);

    void delete(@Param(value = "id")Long id);

    void batchAdd(@Param(value = "list")List<UserRelVolStation> list);

    void delByUserId(@Param(value = "userId")Long userId);

    List<Long> findByUser(@Param(value = "userId")Long userId);

    List<UserRelVolStation> findAllByParam(UserRelVolStation userRelVolStation);

    /**
     * 根据服务站ID查询服务站管理员
     * @author liyang
     * @date 2019-04-04
     * @param messageTemplate :组织ID集合 和 人员类型
    */
    List<Long> getAdminUserByStationOrgIdsSql(@Param("messageTemplate") MessageTemplate messageTemplate);

    /**
     * 获取所有志愿者管理员
     * @author liyang
     * @date 2019-04-10
     * @param user : 用户筛选条件
     * @return : List<Long> : userIds
    */
    List<Long> getAllUserIdForVolOrgSql(@Param("user") User user);
    /**
     * 根据服务站id查询所有人
     * @param stationId
     * @return
     */
    List<Long> queryAllUserBystationId(@Param("stationId") Long stationId);
}
