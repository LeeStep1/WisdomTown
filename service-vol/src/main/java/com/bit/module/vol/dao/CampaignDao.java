package com.bit.module.vol.dao;



import com.bit.module.vol.bean.Campaign;
import com.bit.module.vol.bean.CampaignApp;
import com.bit.module.vol.bean.CampaignPage;
import com.bit.module.vol.vo.CampaignVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-04 14:55
 */
@Repository
public interface CampaignDao {
    /**
     * 添加活动
     * @param campaign
     */
    void add(Campaign campaign);

    /**
     * 分页查询活动
     * @param campaignVO
     * @return
     */
    List<CampaignPage> findByConditionPage(CampaignVO campaignVO);

    /**
     * 查询审核记录
     * @param campaignVO
     * @return
     */
    List<CampaignPage> findAudit(CampaignVO campaignVO);

    /**
     * 单查记录
     * @param id
     * @return
     */
    Campaign queryById(@Param(value = "id") Long id);

    /**
     * app查询活动记录分页
     * @param campaignVO
     * @return
     */
    List<CampaignApp> appCampaignlistPage(CampaignVO campaignVO);

    /**
     * 删除记录
     * @param id
     */
    void deleteById(@Param(value = "id") Long id);

    /**
     * 根据参数查询活动记录
     * @param campaign
     * @return
     */
    List<Campaign> findByParam(Campaign campaign);

    /**
     * 根据站点id和主题查询记录
     * @param campaign
     * @return
     */
    List<Campaign> findByStationIdAndTheme(Campaign campaign);

    /**
     * 更改记录
     * @param campaign
     */
    void update(Campaign campaign);

    /**
     * app端我的活动记录查询分页
     * @param campaignVO
     * @return
     */
    List<CampaignApp> appMyCampaignlistPage(CampaignVO campaignVO);

    /**
     * 查询前三个记录
     * @param campaignVO
     * @return
     */
    List<Campaign> findTopThree(CampaignVO campaignVO);

    /**
     * 查询通过审核的记录不分页
     * @return
     */
    List<Campaign> findAllPassed();

    /**
     * 根据站点计算活动数量
     * @param stationId
     * @return
     */
    Integer countCampaignByStation(@Param(value = "stationId") Long stationId);

    /**
     * excel专用的查询记录不分页
     * @param campaign
     * @return
     */
    List<CampaignPage> findByConditionPageForExcel(Campaign campaign);

    /**
     * 批量更新
     * @param campaignList
     */
    void batchUpdate(@Param(value = "campaignList")List<Campaign> campaignList);

    /**
     * 批量更新活动状态
     * @param campaignList
     */
    void batchUpdateCampaignStatus(@Param(value = "campaignList")List<Campaign> campaignList);
}
