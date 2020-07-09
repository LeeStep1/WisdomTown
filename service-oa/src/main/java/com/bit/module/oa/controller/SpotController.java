package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Spot;
import com.bit.module.oa.enums.SpotStatusEnum;
import com.bit.module.oa.service.SpotService;
import com.bit.module.oa.vo.spot.SpotDetailVO;
import com.bit.module.oa.vo.spot.SpotVO;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Spot的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/spot")
public class SpotController {
	private static final Logger logger = LoggerFactory.getLogger(SpotController.class);
	@Autowired
	private SpotService spotService;
	

    /**
     * 分页查询Spot列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody SpotVO spotVO) {
    	//分页对象，前台传递的包含查询的参数

        return spotService.findByConditionPage(spotVO);
    }

    /**
     * 根据主键ID查询Spot
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        SpotDetailVO spot = spotService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(spot);
		return baseVo;
    }
    
    /**
     * 新增Spot
     *
     * @param spot Spot实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Spot spot) {
        CheckUtil.notNull(spot.getStatus());
        CheckUtil.notNull(spot.getName());
        CheckUtil.notNull(spot.getLng());
        CheckUtil.notNull(spot.getLat());
        CheckUtil.notNull(spot.getZoneId());

        spotService.add(spot);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Spot
     *
     * @param spot Spot实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody Spot spot) {
        CheckUtil.notNull(spot.getId());
		spotService.update(spot);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 启用签到点
     * @param id
     * @return
     */
    @PostMapping("/{id}/enable")
    public BaseVo enableSpot(@PathVariable("id") Long id) {
        spotService.convertStatus(id, SpotStatusEnum.ENABLED.getKey());
        return new BaseVo();
    }

    /**
     * 停用签到点
     * @param id
     * @return
     */
    @PostMapping("/{id}/disable")
    public BaseVo disableSpot(@PathVariable("id") Long id) {
        spotService.convertStatus(id, SpotStatusEnum.DISABLE.getKey());
        return new BaseVo();
    }

    /**
     * 下拉列表
     * @return
     */
    @GetMapping("/list")
    public BaseVo findAll() {
        List<Spot> spot = spotService.findAll("id");
        return new BaseVo<>(spot);
    }

    /**
     * 删除签到点
     * @param id
     * @return
     */
    @GetMapping("/{id}/delete")
    public BaseVo deleteSpot(@PathVariable("id") Long id) {
        spotService.delete(id);
        return new BaseVo();

    }
}
