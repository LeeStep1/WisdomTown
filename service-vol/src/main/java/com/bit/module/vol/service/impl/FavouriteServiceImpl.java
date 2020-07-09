package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.consts.RedisKey;
import com.bit.core.utils.CacheUtil;
import com.bit.module.vol.bean.*;
import com.bit.module.vol.dao.CampaignDao;
import com.bit.module.vol.dao.CampaignVolunteerRecordDao;
import com.bit.module.vol.dao.FavouriteDao;
import com.bit.module.vol.feign.FileServiceFeign;
import com.bit.module.vol.service.FavouriteService;
import com.bit.module.vol.vo.FavouriteVO;
import com.bit.utils.CampaignUtil;
import com.bit.utils.TypeUtil;
import com.bit.utils.VolUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-03-08 15:30
 */
@Service("/favouriteService")
public class FavouriteServiceImpl extends BaseService implements FavouriteService {
    @Autowired
    private FavouriteDao favouriteDao;
    @Autowired
    private CampaignDao campaignDao;
    @Autowired
    private CampaignVolunteerRecordDao campaignVolunteerRecordDao;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private FileServiceFeign fileServiceFeign;
    @Autowired
    private VolUtil volUtil;
    @Autowired
    private TypeUtil typeUtil;
    @Autowired
    private CampaignUtil campaignUtil;

    /**
     * 添加收藏
     * @param favourite
     * @return
     */
    @Override
    public BaseVo add(Favourite favourite) {
        Volunteer vi = volUtil.getVolunteerInfo();
        favourite.setVolunteerId(vi.getId());
        favouriteDao.add(favourite);
        return new BaseVo();
    }
    /**
     * 分页查询收藏
     * @param favouriteVO
     * @return
     */
    @Override
    public BaseVo listPage(FavouriteVO favouriteVO) {
        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId = vi.getId();
        favouriteVO.setVolunteerId(userId);
        PageHelper.startPage(favouriteVO.getPageNum(),favouriteVO.getPageSize());
        List<Favourite> favourites = favouriteDao.listPage(favouriteVO);
        List<Favourite> resultList = new ArrayList<>();
        for (Favourite favourite : favourites) {
            Campaign campaign = campaignDao.queryById(favourite.getCampaignId());
            if (campaign!=null){
                BeanUtils.copyProperties(campaign,favourite);
                //处理活动类型
                List<String> campaignTypeList = typeUtil.getType(campaign.getCampaignType());
                favourite.setCampaignTypeList(campaignTypeList);

                String key = RedisKey.VOL_ENROLL.getKey()+favourite.getId();
                Boolean flag = cacheUtil.hasKey(key);
                if (flag){
                    Long l = cacheUtil.sGetSetSize(key);
                    //因为key中值有个默认-1的值 所有要-1
                    favourite.setEnrollNumber(l.intValue()-1);
                }else {
                    int num = campaignVolunteerRecordDao.countByCampaignId(favourite.getId());
                    favourite.setEnrollNumber(num);
                }

                CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
                obj.setCampaignId(favourite.getCampaignId());
                obj.setVolunteerId(favouriteVO.getVolunteerId());
                List<CampaignVolunteerRecord> volunteerCampaign = campaignVolunteerRecordDao.findVolunteerCampaign(obj);
                if (volunteerCampaign!=null && volunteerCampaign.size()== 1){
                    favourite.setSignStatus(volunteerCampaign.get(0).getSignStatus());
                }
                if (campaign.getCampaignImage()!=null){
                    // 查询图片
                    BaseVo b1 = fileServiceFeign.findById(campaign.getCampaignImage());
                    String s = JSON.toJSONString(b1.getData());
                    FileInfo fileInfo = JSON.parseObject(s,FileInfo.class);
                    if (fileInfo==null){
                        throwBusinessException("文件不存在");
                    }
                    favourite.setFileInfo(fileInfo);
                }
                favourite.setCampaignStatus(campaignUtil.setCampaignStatusWithDate(campaign.getBeginDate(),campaign.getFinishDate()));

                Map<String,Object> result = campaignUtil.getStartOrEndTime(campaign.getBeginDate(),campaign.getFinishDate());
                favourite.setStartDate((Integer) result.get("startDate"));
                favourite.setEndDate((Integer) result.get("endDate"));
                favourite.setStartTime((String) result.get("startTime"));
                favourite.setEndTime((String) result.get("endTime"));
                resultList.add(favourite);
            }

        }
        PageInfo<Favourite> pageInfo = new PageInfo<Favourite>(resultList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * 取消收藏
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseVo cancelFavouriteById(Long id) {
        Volunteer vi = volUtil.getVolunteerInfo();
        Long userId = vi.getId();
        Campaign obj = campaignDao.queryById(id);
        if (obj==null){
            throwBusinessException("该活动不存在");
        }
        Favourite favourite = new Favourite();
        favourite.setCampaignId(id);
        favourite.setVolunteerId(userId);
        favouriteDao.delete(favourite);
        return new BaseVo();
    }

}
