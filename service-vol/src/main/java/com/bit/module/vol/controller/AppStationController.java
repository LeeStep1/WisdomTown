package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Station;
import com.bit.module.vol.bean.Volunteer;
import com.bit.module.vol.service.CampaignService;
import com.bit.module.vol.service.StationService;
import com.bit.module.vol.service.VolunteerService;
import com.bit.module.vol.vo.StationVO;
import com.bit.module.vol.vo.VolunteerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenduo
 * @create 2019-03-12 10:42
 */
@RestController
@RequestMapping("/nstation")
public class AppStationController {
    @Autowired
    private StationService stationService;
    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private CampaignService campaignService;

    /**
     * 反显站点
     * @param id
     * @return
     */
    @GetMapping("/reflect/{id}")
    public BaseVo reflectById(@PathVariable(value = "id") Long id){
        Station station = stationService.reflectById(id);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(station);
        return baseVo;
    }
    /**
     * 分页查询
     * @param stationVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody StationVO stationVO){
        return stationService.listPage(stationVO);
    }

    /**
     * 根据站点id查询志愿者记录
     * @param volunteer
     * @return
     */
    @PostMapping("/queryVolunteer")
    public BaseVo queryVolunteerByStationId(@RequestBody Volunteer volunteer){
        return volunteerService.queryVolunteer(volunteer);
    }




    /**
     * 根据机构id 查询活动
     * @param stationVO
     * @return
     */
    @PostMapping("/queryStationCamapaign")
    public BaseVo queryStationCamapaign(@RequestBody StationVO stationVO){
        return campaignService.queryStationCampaign(stationVO);
    }
}
