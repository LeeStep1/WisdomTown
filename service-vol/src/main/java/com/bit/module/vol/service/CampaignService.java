package com.bit.module.vol.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.Campaign;
import com.bit.module.vol.bean.CampaignPage;
import com.bit.module.vol.vo.CampaignVO;
import com.bit.module.vol.vo.StationVO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-04 14:52
 */
public interface CampaignService {
    /**
     * 添加活动记录
     * @param campaign
     * @return
     */
    BaseVo add(Campaign campaign);
    /**
     * 发布草稿
     * @param id
     * @return
     */
    BaseVo release(Long id,HttpServletRequest request);
    /**
     * 分页查询活动记录
     * @param campaignVO
     * @return
     */
    BaseVo listPage(CampaignVO campaignVO);
    /**
     * 反显单条记录
     * @param id
     * @return
     */
    BaseVo reflectById(Long id);
    /**
     * 取消活动
     * @param campaignVO
     * @return
     */
    BaseVo cancelById(CampaignVO campaignVO);
    /**
     * 删除活动
     * @param id
     * @return
     */
    BaseVo deleteById(Long id);
    /**
     * 根据机构id 查询活动
     * @param stationVO
     * @return
     */
    BaseVo queryStationCampaign(StationVO stationVO);
    /**
     * 获取资料
     * @param id
     * @return
     */
    BaseVo getdataById(Long id);
    /**
     * 更新活动
     * @param campaign
     * @return
     */
    BaseVo update(Campaign campaign);
    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    void exportToExcel(String campaignTheme,
                       Integer campaignStatus,
                       Integer startDate,
                       Integer endDate,
                       String startTime,
                       String endTime,
                       String stationName,
                       HttpServletResponse response);
    /**
     * 检查签到信息
     * @param campaignVO
     * @return
     */
    BaseVo checkSign(CampaignVO campaignVO);
    /**
     * 检查捐款信息
     * @param campaignVO
     * @return
     */
    BaseVo checkDonateMoney(CampaignVO campaignVO);
    /**
     * 审核活动
     * @param campaign
     * @return
     */
    BaseVo audit(Campaign campaign);
    /**
     * app使用查询活动详情
     * @param id
     * @return
     */
    BaseVo appReflect(Long id);
    /**
     * 分页查询审核活动记录
     * @param campaignVO
     * @return
     */
    BaseVo auditlistPage(CampaignVO campaignVO);
    /**
     * app端我的活动列表
     * @param campaignVO
     * @return
     */
    BaseVo appCampaignlistPage(CampaignVO campaignVO);
    /**
     * app端报名
     * @param campaignId
     * @return
     */
    BaseVo appEnroll(Long campaignId);
    /**
     * app端取消报名
     * @param campaignId
     * @return
     */
    BaseVo appUnEnroll(Long campaignId);
    /**
     * app端我的活动
     * @param campaignVO
     * @return
     */
    BaseVo appMyCampaignlistPage(CampaignVO campaignVO);
    /**
     * app端查询活动记录
     * @param campaignId
     * @return
     */
    BaseVo appLog(Long campaignId);
    /**
     * app端查询报名人数
     * @param campaignId
     * @return
     */
    BaseVo appCheckSign(Long campaignId);
    /**
     * 更新捐款信息
     * @param campaignVO
     * @return
     */
    BaseVo updateDonateMoney(CampaignVO campaignVO);

    /**
     * 查询为生成excel专用
     * @param campaign
     * @return
     */
    List<CampaignPage> listPageForExcel(Campaign campaign);
    /**
     * 导入excel
     * @param file
     * @return
     */
    void importDonateMoneyExcel(MultipartFile file,Long campaignId,HttpServletResponse response) throws IOException;
    /**
     * 查询该活动下的报名人
     * @param campaignId
     * @return
     */
    List<Long> queryEnrollId(Long campaignId);
    /**
     * 检查该活动是否取消
     * @param campaignId
     * @return
     */
    Boolean checkCampaignStatusById(Long campaignId);
    /**
     * 下载模板
     */
    void downloadTemplate(HttpServletResponse response) throws IOException;

}
