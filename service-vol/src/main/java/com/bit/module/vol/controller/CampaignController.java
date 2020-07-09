package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Campaign;
import com.bit.module.vol.service.CampaignService;
import com.bit.module.vol.vo.CampaignVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-04 14:50
 */
@RestController
@RequestMapping("/campaign")
public class CampaignController {

    @Autowired
    private CampaignService campaignService;

    /**
     * 添加活动记录
     * @param campaign
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Campaign campaign){
        return campaignService.add(campaign);
    }

    /**
     * 发布草稿
     * @param id
     * @return
     */
    @GetMapping("/release/{id}")
    public BaseVo release(@PathVariable(value = "id")Long id, HttpServletRequest request){
        return campaignService.release(id,request);
    }

    /**
     * 分页查询审核活动记录
     * @param campaignVO
     * @return
     */
    @PostMapping("/auditlistPage")
    public BaseVo auditlistPage(@RequestBody CampaignVO campaignVO){
        return campaignService.auditlistPage(campaignVO);
    }

    /**
     * 分页查询活动记录
     * @param campaignVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody CampaignVO campaignVO){
        return campaignService.listPage(campaignVO);
    }

    /**
     * 反显单条记录
     * @param id
     * @return
     */
    @GetMapping("/reflect/{id}")
    public BaseVo reflectById(@PathVariable(value = "id") Long id){
        return campaignService.reflectById(id);
    }

    /**
     * 取消活动
     * @param campaignVO
     * @return
     */
    @PostMapping("/cancel")
    public BaseVo cancelById(@RequestBody CampaignVO campaignVO){
        return campaignService.cancelById(campaignVO);
    }
    /**
     * 删除活动
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo deleteById(@PathVariable(value = "id") Long id){
        return campaignService.deleteById(id);
    }


    /**
     * 获取资料
     * @param id
     * @return
     */
    @GetMapping("/data/{id}")
    public BaseVo getdataById(@PathVariable(value = "id") Long id){
        return campaignService.getdataById(id);
    }

    /**
     * 更新活动记录
     * @param campaign
     * @return
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody Campaign campaign){
        return campaignService.update(campaign);
    }
    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    @PostMapping("/exportToExcel")
    public void exportToExcel(@RequestParam(value = "campaignTheme",required = false) String campaignTheme,
                              @RequestParam(value = "campaignStatus",required = false) Integer campaignStatus,
                              @RequestParam(value = "startDate",required = false) Integer startDate,
                              @RequestParam(value = "endDate",required = false) Integer endDate,
                              @RequestParam(value = "startTime",required = false) String startTime,
                              @RequestParam(value = "endTime",required = false) String endTime,
                              @RequestParam(value = "stationName",required = false) String stationName,
                              HttpServletResponse response){
        campaignService.exportToExcel(campaignTheme,campaignStatus,startDate,endDate,startTime,endTime,stationName,response);
    }

    /**
     * 检查签到信息
     * @param campaignVO
     * @return
     */
    @PostMapping("/checkSign")
    public BaseVo checkSign(@RequestBody CampaignVO campaignVO){
        return campaignService.checkSign(campaignVO);
    }

    /**
     * 检查捐款信息
     * @param campaignVO
     * @return
     */
    @PostMapping("/checkDonateMoney")
    public BaseVo checkDonateMoney(@RequestBody CampaignVO campaignVO){
        return campaignService.checkDonateMoney(campaignVO);
    }

    /**
     * 审核活动
     * @param campaign
     * @return
     */
    @PostMapping("/audit")
    public BaseVo audit(@RequestBody Campaign campaign){
        return campaignService.audit(campaign);
    }

    /**
     * 更新捐款信息
     * @param campaignVO
     * @return
     */
    @PostMapping("/updateDonateMoney")
    public BaseVo updateDonateMoney(@RequestBody CampaignVO campaignVO){
        return campaignService.updateDonateMoney(campaignVO);
    }

    /**
     * 导入excel
     * @param file
     * @return
     */
    @PostMapping("/importDonateMoneyExcel/{campaignId}")
    public void importDonateMoneyExcel(@RequestParam MultipartFile file,@PathVariable(value = "campaignId")Long campaignId,HttpServletResponse response) throws IOException {
        campaignService.importDonateMoneyExcel(file,campaignId,response);
    }
    /**
     * 查询该活动下的报名人
     * @param campaignId
     * @return
     */
    @GetMapping("/queryEnrollId/{campaignId}")
    public List<Long> queryEnrollId(@PathVariable(value = "campaignId")Long campaignId){
        return campaignService.queryEnrollId(campaignId);
    }
    /**
     * 检查该活动是否取消
     * @param campaignId
     * @return
     */
    @GetMapping("/checkCampaignStatusById/{campaignId}")
    public Boolean checkCampaignStatusById(@PathVariable(value = "campaignId")Long campaignId){
        return campaignService.checkCampaignStatusById(campaignId);
    }

    /**
     * 下载模板
     */
    @PostMapping("/downloadTemplate")
    public void downloadTemplate(HttpServletResponse response) throws IOException {
        campaignService.downloadTemplate(response);
    }


}
