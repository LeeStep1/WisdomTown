package com.bit.module.vol.controller;

import com.bit.base.dto.VolunteerInfo;
import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.VerifyParam;
import com.bit.module.vol.bean.Volunteer;
import com.bit.module.vol.service.VolunteerService;
import com.bit.module.vol.vo.VolunteerVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

/**
 * @author chenduo
 * @create 2019-03-05 12:00
 */
@RestController
@RequestMapping("/voInfo")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;
    @Value("${spring.rabbitmq.host}")
    private String path;

    
    /**
     * 添加志愿者信息
     * @param volunteer
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Volunteer volunteer){
        return volunteerService.add(volunteer);
    }
    /**
     * 单查志愿者信息
     * @param id
     */
    @GetMapping("/reflect/{id}")
    public BaseVo reflectById(@PathVariable(value = "id") Long id){
        return volunteerService.reflectById(id);
    }

    /**
     * 更新志愿者信息
     * @param volunteer
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody Volunteer volunteer){
        return volunteerService.update(volunteer);
    }


    /**
     * 分页查询
     * @param volunteerVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody VolunteerVO volunteerVO){
        return volunteerService.listPage(volunteerVO);
    }

    /**
     * app用查询
     * @param volunteerVO
     * @return
     */
    @PostMapping("/nlistPage")
    public BaseVo nlistPage(@RequestBody VolunteerVO volunteerVO){
        return volunteerService.nlistPage(volunteerVO);
    }

    /**
     * 根据志愿者id查询志愿者参加过的活动
     * @param volunteerVO
     * @return
     */
    @PostMapping("/queryCampaignByVolunteer")
    public BaseVo queryCampaignByVolunteer(@RequestBody VolunteerVO volunteerVO){
        return volunteerService.queryCampaignByVolunteer(volunteerVO);
    }
    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    @PostMapping("/exportToExcel")
    public void exportToExcel(@RequestParam(value = "realName",required = false) String realName,
                              @RequestParam(value = "cardId",required = false)String cardId,
                              @RequestParam(value = "lhour",required = false)BigDecimal lhour,
                              @RequestParam(value = "rhour",required = false)BigDecimal rhour,
                              @RequestParam(value = "stationName",required = false) String stationName,
                              @RequestParam(value = "serviceLevel",required = false) Integer serviceLevel,
                              @RequestParam(value = "volunteerStatus",required = false) Integer volunteerStatus,
                              HttpServletResponse response){
        volunteerService.exportToExcel(realName,cardId,lhour,rhour,stationName,serviceLevel,volunteerStatus,response);
    }


    /**
     * 根据身份证获取志愿者信息
     * @param cardId
     * @return
     */
    @PostMapping("/getInfo")
    public VolunteerInfo getInfo(@RequestBody String cardId){
        return volunteerService.getInfo(cardId);
    }

    /**
     * 判断身份证是否重复
     * @param cardId
     * @return
     */
    @PostMapping("/distinctCardId")
    public BaseVo distinctCardId(@RequestBody String cardId){
        return volunteerService.distinctCardId(cardId);
    }

    /**
     * 根据身份证和手机号校验是否重复
     * @param verifyParam
     * @return
     */
    @PostMapping("/distinctMobileAndCardId")
    public BaseVo distinctMobileAndCardId(@RequestBody VerifyParam verifyParam){
        return volunteerService.distinctMobileAndCardId(verifyParam);
    }
    /**
     * 排行榜
     * @return
     */
    @PostMapping("/board")
    public BaseVo board(@RequestBody VolunteerVO volunteerVO){
        return volunteerService.board(volunteerVO);
    }

    /**
     * app端更新志愿者信息
     * @param volunteer
     * @return
     */
    @PostMapping("/appUpdate")
    public BaseVo appUpdate(@RequestBody Volunteer volunteer){
        return volunteerService.appUpdate(volunteer);
    }

    /**
     * app端单查志愿者
     * @return
     */
    @GetMapping("/appReflect")
    public BaseVo appReflect(){
        return volunteerService.appReflect();
    }

    @PostMapping("/getRabbitMQHost")
    public BaseVo getRabbitMQHost(){
        BaseVo baseVo = new BaseVo();
        baseVo.setData(path);
        return baseVo;
    }

}
