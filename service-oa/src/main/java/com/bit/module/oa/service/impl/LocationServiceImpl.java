package com.bit.module.oa.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.module.oa.bean.Executor;
import com.bit.module.oa.bean.Location;
import com.bit.module.oa.dao.ExecutorDao;
import com.bit.module.oa.dao.LocationDao;
import com.bit.module.oa.service.LocationService;
import com.bit.module.oa.vo.location.LocationQO;
import com.bit.module.oa.vo.location.LocationUserVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Description :
 * @Date ： 2019/2/15 10:42
 */
@Service("locationService")
@Transactional
public class LocationServiceImpl extends BaseService implements LocationService {

    private static final Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private ExecutorDao executorDao;

    @Override
    public List<LocationUserVO> findLocationList(List<Long> executeIds) {
        List<Location> locations = locationDao.findByExecuteIds(executeIds);
        return transform(locations);
    }

    @Override
    public void add(LocationQO locationQO) {
        UserInfo currentUserInfo = getCurrentUserInfo();
        Executor toCheck = executorDao.findByInspectIdAndExecutorId(locationQO.getInspectId(), currentUserInfo.getId());
        if (toCheck == null) {
            logger.error("巡检单不存在 : {}", locationQO);
            throw new BusinessException("巡检单不存在");
        }
        Location location = new Location();
        location.setExecuteId(toCheck.getId());
        location.setLat(locationQO.getLat());
        location.setLng(locationQO.getLng());
        location.setUserId(currentUserInfo.getId());
        location.setCreateAt(new Date());
        locationDao.add(location);
    }


    private List<LocationUserVO> transform(List<Location> locations) {
        Map<Long, List<Location>> collect = new HashMap<>();
        for (Location location : locations) {
            if (collect.get(location.getUserId()) == null) {
                List<Location> locationList = new ArrayList<>();
                collect.put(location.getUserId(), locationList);
            }
            collect.get(location.getUserId()).add(location);
        }
        List<LocationUserVO> locationUserVOS = new ArrayList<>();
        for (Map.Entry<Long, List<Location>> entry : collect.entrySet()) {
            LocationUserVO userVO = new LocationUserVO();
            userVO.setUserId(entry.getKey());
            userVO.setLocations(entry.getValue());
            locationUserVOS.add(userVO);
        }
        return locationUserVOS;
    }
}
