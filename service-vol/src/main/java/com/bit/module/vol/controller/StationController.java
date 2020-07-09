package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Station;
import com.bit.module.vol.service.CampaignService;
import com.bit.module.vol.service.StationService;
import com.bit.module.vol.service.VolunteerService;
import com.bit.module.vol.vo.StationVO;
import com.bit.module.vol.vo.VolunteerVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-05 10:57
 */
@RestController
@RequestMapping("/station")
public class StationController {

    @Autowired
    private VolunteerService volunteerService;
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private StationService stationService;

    /**
     * 添加站点
     * @param station
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Station station){

        return stationService.add(station);
    }

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
     * 更改记录状态
     * @param station
     * @return
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody Station station){
        stationService.update(station);
        return new BaseVo();
    }

    /**
     * 根据站点id查询志愿者记录
     * @param volunteerVO
     * @return
     */
    @PostMapping("/queryVolunteer")
    public BaseVo queryVolunteerByStationId(@RequestBody VolunteerVO volunteerVO){
        return volunteerService.listPageVolByStationId(volunteerVO);
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

    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    @PostMapping("/exportToExcel")
    public void exportToExcel(@RequestParam(value = "stationName",required = false) String stationName,
                              @RequestParam(value = "firstChargeMan",required = false) String firstChargeMan,
                              @RequestParam(value = "firstChargeManMobile",required = false) String firstChargeManMobile,
                              @RequestParam(value = "stationStatus",required = false) Integer stationStatus,
                              @RequestParam(value = "partnerOrgType",required = false) Integer partnerOrgType,
                              HttpServletResponse response,HttpServletRequest request,
                              @RequestParam(value = "type",required = true) Integer type){
        stationService.exportToExcel(stationName,firstChargeMan,firstChargeManMobile,stationStatus,partnerOrgType,response,request,type);
    }

    /**
     * 生成服务站树
     * @param stationId
     * @return
     */
    @GetMapping("/tree/{stationId}")
    public List<Station> tree(@PathVariable(value = "stationId")Long stationId){
        return stationService.tree(stationId);
    }

    /**
     * 生成服务站树
     * @param stationId
     * @return
     */
    @GetMapping("/childTree/{stationId}")
    public List<Station> childTree(@PathVariable(value = "stationId")Long stationId){
        return stationService.childTree(stationId);
    }

    /**
     * 判断机构编号是否重复
     * @param code
     * @return
     */
    @PostMapping("/countSameCode")
    public Integer countSameCode(@RequestBody String code){
        return stationService.countSameCode(code);
    }

    /**
     * 根据志愿者id查询服务站信息
     * @param volunteerId
     * @return
     */
    @GetMapping("/queryStation/{volunteerId}")
    public BaseVo queryStationByVolunteerId(@PathVariable(value = "volunteerId")Long volunteerId){
        return stationService.queryStationByVolunteerId(volunteerId);
    }

    /**
     * 查询所有的站点
     * @return
     */
    @PostMapping("/findAllStation")
    public BaseVo findAllStation(){
        return stationService.findAllStation();
    }

    /**
     * 查询站点资料
     * @param stationId
     * @return
     */
    @GetMapping("/data/{stationId}")
    public BaseVo data(@PathVariable(value = "stationId")Long stationId){
        return stationService.data(stationId);
    }
    /**
     * 批量查询服务站
     * @param stationIds
     * @return
     */
    @PostMapping("/batchSelectByStationIds")
    public BaseVo batchSelectByStationIds(@RequestBody List<Long> stationIds){
        return stationService.batchSelectByStationIds(stationIds);
    }
    /**
     * 停用单位
     * @param id
     * @return
     */
    @GetMapping("/deactivate/{id}")
    public BaseVo deactivate(@PathVariable(value = "id") Long id){
        return stationService.deactivate(id);
    }

    /**
     * 查询顶级站点
     * @return
     */
    @PostMapping("/findTopStation")
    public Long findTopStation(){
        return stationService.findTopStation();
    }

    /**
     * 查询等级为1的机构
     * @return
     */
    @PostMapping("/findTopStationDetail")
    public BaseVo findTopStationDetail(){
        return stationService.findTopStationDetail();
    }
}
