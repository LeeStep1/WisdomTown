package com.bit.module.vol.dao;


import com.bit.module.vol.bean.Calculate;
import com.bit.module.vol.bean.CalculateParam;
import com.bit.module.vol.bean.CampaignVolunteerRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-03-04 16:21
 */
@Repository
public interface CampaignVolunteerRecordDao {

    void add(CampaignVolunteerRecord campaignVolunteerRecord);

    List<CampaignVolunteerRecord> findByParam(CampaignVolunteerRecord campaignVolunteerRecord);

    List<CampaignVolunteerRecord> findVolunteerCampaign(CampaignVolunteerRecord campaignVolunteerRecord);

    Calculate countTimeAndMoneyByVoIdAndCamId(@Param(value = "volunteerId")Long volunteerId, @Param(value = "campaignId")Long campaignId);

    Calculate countTimeAndMoneyAndNumber(CalculateParam calculateParam);

    Calculate countTimeAndMoneyByCampaignId(@Param(value = "campaignId")Long campaignId);

    void batchDelete(@Param(value = "ids")List<Long> ids);

    void batchUpdateInactive(@Param(value = "ids")List<Long> ids);

    Integer countByCampaignId(@Param(value = "campaignId")Long campaignId);

    Integer countByCampaignIdAndVolunteerId(@Param(value = "campaignId")Long campaignId,@Param(value = "volunteerId")Long volunteerId);

    void update(CampaignVolunteerRecord campaignVolunteerRecord);

    Integer countEnrollNumberByCampaignId(@Param(value = "campaignId")Long campaignId);

    Integer countSignNumberByCampaignId(@Param(value = "campaignId")Long campaignId);

    /**
     * 得到这个人的签到的活动记录
     * @param campaignVolunteerRecord
     * @return
     */
    List<CampaignVolunteerRecord> findSignedVolunteerCampaign(CampaignVolunteerRecord campaignVolunteerRecord);



    List<CampaignVolunteerRecord> batchSelectByRecord(@Param(value = "searchList")List<CampaignVolunteerRecord> searchList);
    /**
     * 批量更新记录 根据活动id 志愿者id 和version
     * @param recordList
     */
    void batchUpdateRecordMoney(@Param(value = "recordList")List<CampaignVolunteerRecord> recordList);

}
