package com.bit.module.oa.service;

import com.bit.module.oa.vo.location.LocationQO;
import com.bit.module.oa.vo.location.LocationUserVO;

import java.util.List;

/**
 * @Description :
 * @Date ： 2019/2/15 10:34
 */
public interface LocationService {
    List<LocationUserVO> findLocationList(List<Long> executeIds);

    void add(LocationQO location);
}
