package com.bit.module.system.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.SysConst;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.User;
import com.bit.module.system.bean.UserRelVolStation;
import com.bit.module.system.dao.UserRelVolStationDao;
import com.bit.module.system.service.UserRelVolStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-22 14:53
 */
@Service("userRelVolStationService")
public class UserRelVolStaionServiceImpl extends BaseService implements UserRelVolStationService {

    @Autowired
    private UserRelVolStationDao userRelVolStationDao;
    /**
     * 新增用户与志愿者站点关系
     * @param userRelVolStation
     * @return
     */
    @Override
    @Transactional
    public BaseVo add(UserRelVolStation userRelVolStation) {
        userRelVolStationDao.add(userRelVolStation);
        return new BaseVo();
    }
    /**
     * 根据用户id查询记录
     * @param userId
     * @return
     */
    @Override
    public BaseVo queryByUserId(Long userId) {
        UserRelVolStation byUserId = userRelVolStationDao.findByUserId(userId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(byUserId);
        return baseVo;
    }
    /**
     * 根据用户筛选站点id
     * @return
     */
    @Override
    public List<Long> findByUser() {
        UserInfo userInfo = getCurrentUserInfo();
        Long userId = userInfo.getId();
        return userRelVolStationDao.findByUser(userId);
    }

    /**
     * 根据服务站ID查询服务站管理员
     * @author liyang
     * @date 2019-04-04
     * @param messageTemplate : 组织ID集合 和 人员类型
     */
    @Override
    public BaseVo getAdminUserByStationOrgIds(MessageTemplate messageTemplate) {
        List<Long> userIds = userRelVolStationDao.getAdminUserByStationOrgIdsSql(messageTemplate);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(userIds);
        return baseVo;
    }

    /**
     * 获取所有志愿者管理员
     * @author liyang
     * @date 2019-04-10
     * @return : List<Long> : userIds
     */
    @Override
    public List<Long> getAllUserIdForVolOrg() {

        User user = new User();

        //设置筛选条件为用户可用
        user.setStatus(SysConst.USER_STATUS);

        List<Long> userIds = userRelVolStationDao.getAllUserIdForVolOrgSql(user);

        return userIds;
    }
    /**
     * 根据服务站id查询所有人
     * @param stationId
     * @return
     */
    @Override
    public List<Long> queryAllUserBystationId(Long stationId) {
        return userRelVolStationDao.queryAllUserBystationId(stationId);
    }
    /**
     * 根据主键删除记录
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseVo delRelation(Long id) {
        userRelVolStationDao.delete(id);
        return successVo();
    }
}
