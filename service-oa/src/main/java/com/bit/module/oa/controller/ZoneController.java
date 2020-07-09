package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Zone;
import com.bit.module.oa.enums.ZoneStatusEnum;
import com.bit.module.oa.service.ZoneService;
import com.bit.module.oa.vo.zone.SimpleZoneVO;
import com.bit.module.oa.vo.zone.ZoneQO;
import com.bit.module.oa.vo.zone.ZoneVO;
import com.bit.utils.CheckUtil;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Zone的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/zone")
public class ZoneController {
    private static final Logger logger = LoggerFactory.getLogger(ZoneController.class);
    @Autowired
    private ZoneService zoneService;


    /**
     * 分页查询Zone列表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ZoneVO zoneVO) {
        //分页对象，前台传递的包含查询的参数

        return zoneService.findByConditionPage(zoneVO);
    }

    /**
     * 根据主键ID查询Zone
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Zone zone = zoneService.findById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(zone);
        return baseVo;
    }

    /**
     * 新增Zone
     *
     * @param zone Zone实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Zone zone) {
        CheckUtil.notNull(zone.getStatus());
        CheckUtil.notNull(zone.getName());
        CheckUtil.notNull(zone.getPrincipalName());

        zoneService.add(zone);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 修改Zone
     *
     * @param zone Zone实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody Zone zone) {
        CheckUtil.notNull(zone.getId());
        zoneService.update(zone);
        return new BaseVo();
    }

    /**
     * 启用区域
     * @param id
     * @return
     */
    @PostMapping("/{id}/enable")
    public BaseVo enableZone(@PathVariable("id") Long id) {
        zoneService.convertStatus(id, ZoneStatusEnum.ENABLED.getKey());
        return new BaseVo();
    }

    /**
     * 停用区域
     * @param id
     * @return
     */
    @PostMapping("/{id}/disable")
    public BaseVo disableZone(@PathVariable("id") Long id) {
        zoneService.convertStatus(id, ZoneStatusEnum.DISABLE.getKey());
        return new BaseVo();
    }

    /**
     * 区域下拉列表
     * @return
     */
    @GetMapping("/list")
    public BaseVo findAll() {
        List<SimpleZoneVO> zone = zoneService.findAll("id");
        return new BaseVo<>(zone);
    }

    /**
     * 签到点区域查询
     * @param zone
     * @return
     */
    @PostMapping("/spot/query")
    public BaseVo findZoneSpotVO(@RequestBody ZoneQO zone) {
        return zoneService.findZoneSpotVO(zone);
    }

    /**
     * 删除区域
     * @param id
     * @return
     */
    @GetMapping("/{id}/delete")
    public BaseVo deleteSpot(@PathVariable("id") Long id) {
        zoneService.delete(id);
        return new BaseVo();

    }
}
