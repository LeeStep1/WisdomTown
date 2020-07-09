package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Location;
import com.bit.module.oa.service.LocationService;
import com.bit.module.oa.vo.location.LocationQO;
import com.bit.module.oa.vo.location.LocationUserVO;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Location的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/location")
public class LocationController {
	private static final Logger logger = LoggerFactory.getLogger(LocationController.class);
	@Autowired
	private LocationService locationService;


    /**
     * 根据主键ID查询Location
     *
     * @param locationQO
     * @return
     */
    @PostMapping("/query")
    public BaseVo query(@RequestBody LocationQO locationQO) {

        List<LocationUserVO> locationList = locationService
                .findLocationList(locationQO.getExecuteIds());
        BaseVo baseVo = new BaseVo();
		baseVo.setData(locationList);
		return baseVo;
    }

    /**
     * 根据主键ID删除Location
     *
     * @param locationQO
     * @return
     */
    @PostMapping("/mark")
    public BaseVo mark(@RequestBody LocationQO locationQO) {
        CheckUtil.notNull(locationQO.getLat());
        CheckUtil.notNull(locationQO.getLng());
        CheckUtil.notNull(locationQO.getInspectId());

        locationService.add(locationQO);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

}
