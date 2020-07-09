package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.UserRelVolStation;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-22 14:53
 */
public interface UserRelVolStationService {
    /**
     * 新增用户与志愿者站点关系
     * @param userRelVolStation
     * @return
     */
    BaseVo add(UserRelVolStation userRelVolStation);

    /**
     * 根据用户id查询记录
     * @param userId
     * @return
     */
    BaseVo queryByUserId(Long userId);

    /**
     * 根据用户筛选站点id
     * @return
     */
    List<Long> findByUser();

    /**
     * 根据服务站ID查询服务站管理员
     * @author liyang
     * @date 2019-04-04
     * @param messageTemplate : 组织ID集合 和 人员类型
     */
    BaseVo getAdminUserByStationOrgIds(MessageTemplate messageTemplate);

    /**
     * 获取所有志愿者管理员
     * @author liyang
     * @date 2019-04-10
     * @return : List<Long> : userIds
     */
    List<Long> getAllUserIdForVolOrg();
    /**
     * 根据服务站id查询所有人
     * @param stationId
     * @return
     */
    List<Long> queryAllUserBystationId(Long stationId);
    /**
     * 根据主键删除记录
     * @param id
     * @return
     */
    BaseVo delRelation(Long id);
}
