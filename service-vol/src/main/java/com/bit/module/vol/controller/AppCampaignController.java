package com.bit.module.vol.controller;


import com.bit.base.vo.BaseVo;
import com.bit.module.vol.service.CampaignService;
import com.bit.module.vol.service.VolunteerService;
import com.bit.module.vol.vo.CampaignVO;
import com.bit.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-03-12 11:17
 */
@RestController
@RequestMapping("/ncampaign")
public class AppCampaignController {
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private VolunteerService volunteerService;

    /**
     * app使用查询活动详情
     * @param id
     * @return
     */
    @GetMapping("/reflect/{id}")
    public BaseVo appReflect(@PathVariable(value = "id") Long id){
        return campaignService.appReflect(id);
    }

    /**
     * app端活动列表
     * @param campaignVO
     * @return
     */
    @PostMapping("/campaignlistPage")
    public BaseVo appCampaignlistPage(@RequestBody CampaignVO campaignVO){
        return campaignService.appCampaignlistPage(campaignVO);
    }

    /**
     * app端报名
     * @param campaignId
     * @return
     */
    @GetMapping("/enroll/{campaignId}")
    public BaseVo appEnroll(@PathVariable(value = "campaignId") Long campaignId){
        return campaignService.appEnroll(campaignId);
    }

    /**
     * app端取消报名
     * @param campaignId
     * @return
     */
    @GetMapping("/unEnroll/{campaignId}")
    public BaseVo appUnEnroll(@PathVariable(value = "campaignId") Long campaignId){
        return campaignService.appUnEnroll(campaignId);
    }

    /**
     * app端我的活动
     * @param campaignVO
     * @return
     */
    @PostMapping("/myCampaignlistPage")
    public BaseVo appMyCampaignlistPage(@RequestBody CampaignVO campaignVO){
        return campaignService.appMyCampaignlistPage(campaignVO);
    }

    /**
     * app端查询活动记录
     * @param campaignId
     * @return
     */
    @GetMapping("/log/{campaignId}")
    public BaseVo appLog(@PathVariable(value = "campaignId") Long campaignId){
        return campaignService.appLog(campaignId);
    }

    /**
     * app端查询报名人数
     * @param campaignId
     * @return
     */
    @GetMapping("/checkEnroll/{campaignId}")
    public BaseVo appCheckSign(@PathVariable(value = "campaignId") Long campaignId){
        return campaignService.appCheckSign(campaignId);
    }

    /**
     * app签到接口
     * @param campaignId
     * @return
     */
    @GetMapping("/sign/{campaignId}")
    public BaseVo sign(@PathVariable(value = "campaignId") Long campaignId){
        return volunteerService.sign(campaignId);
    }

    /**
     * 返回系统时间
     * @return
     */
    @PostMapping("/getServerDate")
    public BaseVo getServerDate(){
        Map<String,String> map = new HashMap<>();
        String s = DateUtil.format(new Date());
        map.put("date",s);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(map);

        return baseVo;
    }

}
