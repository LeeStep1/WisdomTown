package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.consts.RedisKey;
import com.bit.core.utils.CacheUtil;

import com.bit.module.vol.bean.*;
import com.bit.module.vol.dao.*;
import com.bit.module.vol.feign.SysServiceFeign;
import com.bit.module.vol.service.JobService;
import com.bit.module.vol.vo.VolunteerVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.CampaignUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-14 13:22
 */
@Service("jobService")
public class JobServiceImpl extends BaseService implements JobService {

    @Autowired
    private CampaignDao campaignDao;
    @Autowired
    private VolunteerDao volunteerDao;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private CampaignVolunteerRecordDao campaignVolunteerRecordDao;
    @Autowired
    private LevelRegulationDao levelRegulationDao;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private CampaignUtil campaignUtil;
    @Autowired
    private SysServiceFeign sysServiceFeign;

    @Autowired
    private SendMqPushUtil sendMqPushUtil;



    /**
     * 更新活动状态
     */
    @Override
    @Transactional
    public void dailyUpdateCamapignStatus() {
        //得到所有 未开始 和 进行中的活动
        List<Campaign> allPassed = campaignDao.findAllPassed();
        List<Campaign> campaignListForUpdate = new ArrayList<>();
        if (allPassed!=null && allPassed.size()>0){
            for (Campaign campaign : allPassed) {
                Date beginDate = campaign.getBeginDate();
                Date finsihDate = campaign.getFinishDate();
                Campaign obj = new Campaign();
                Integer st = campaignUtil.setCampaignStatusWithDate(beginDate,finsihDate);
                obj.setId(campaign.getId());
                obj.setCampaignStatus(st);
                obj.setVersion(campaign.getVersion());
                campaignListForUpdate.add(obj);
            }
            if (CollectionUtils.isNotEmpty(campaignListForUpdate)){
				//截取更新list 1000条一更新
				if (campaignListForUpdate.size()<1000){
					//批量更新志愿者信息
					campaignDao.batchUpdateCampaignStatus(campaignListForUpdate);
				}else {
					int len = 1000;
					int size = campaignListForUpdate.size();
					int count = size % 1000==0?size /1000:size /1000+1;
					for (int i = 0;i<count;i++){
						//截取1000条数据
						List<Campaign> sublist = campaignListForUpdate.subList(i*len,((i + 1) * len > size ? size : len * (i + 1)));
						campaignDao.batchUpdateCampaignStatus(sublist);
					}
				}
			}
        }
    }
    /**
     * 更新志愿者信息
     */
    @Override
    @Transactional
    public void dailuUpdateVolunteer() {
        Volunteer obj = new Volunteer();
        obj.setVolunteerStatus(Const.VOLUNTEER_STATUS_ACTIVE);
        //得到所有激活的志愿者
        List<Volunteer> allActiveVolunteer = volunteerDao.findAllActiveVolunteer(obj);

        List<Volunteer> voListForUpdate = new ArrayList<>();
        if (allActiveVolunteer !=null && allActiveVolunteer.size()>0){
            for (Volunteer volunteer : allActiveVolunteer) {
                int allnumber = 0;
                BigDecimal allmoney = new BigDecimal(0);
                BigDecimal allhour = new BigDecimal(0);
                CampaignVolunteerRecord temp = new CampaignVolunteerRecord();
                temp.setVolunteerId(volunteer.getId());
                temp.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
                temp.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
                //得到所有启用的活动记录 而且是签到的
                List<CampaignVolunteerRecord> volunteerCampaign = campaignVolunteerRecordDao.findSignedVolunteerCampaign(temp);
                if (volunteerCampaign!=null && volunteerCampaign.size()>0){
                    allnumber = volunteerCampaign.size();
                    for (CampaignVolunteerRecord campaignVolunteerRecord : volunteerCampaign) {
                        if (campaignVolunteerRecord.getCampaignHour()==null){
                            allhour = allhour.add(new BigDecimal(0));
                        }else {
                            allhour = allhour.add(campaignVolunteerRecord.getCampaignHour());
                        }
                        if (campaignVolunteerRecord.getDonateMoney()==null){
                            allmoney = allmoney.add(new BigDecimal(0));
                        }else {
                            allmoney = allmoney.add(campaignVolunteerRecord.getDonateMoney());
                        }
                    }

                    Volunteer vv = new Volunteer();
                    vv.setId(volunteer.getId());
                    //设置志愿者活动次数
                    vv.setCampaignCount(allnumber);
                    //设置志愿者活动时长
                    vv.setCampaignHour(allhour.add(volunteer.getInitHour()==null ? new BigDecimal(0): volunteer.getInitHour()));
                    //设置志愿者捐款金额
                    vv.setDonateMoney(allmoney.add(volunteer.getDonateMoney()==null ? new BigDecimal(0): volunteer.getInitMoney()));
                    vv.setVersion(volunteer.getVersion());
                    voListForUpdate.add(vv);
                }
            }
        }
        if (CollectionUtils.isNotEmpty(voListForUpdate)){
            //截取更新list 1000条一更新
            if (voListForUpdate.size()<1000){
                //批量更新志愿者信息
                volunteerDao.batchUpdate(voListForUpdate);
            }else {
                int len = 1000;
                int size = voListForUpdate.size();
                int count = size % 1000==0?size /1000:size /1000+1;
                for (int i = 0;i<count;i++){
                    //截取1000条数据
                    List<Volunteer> sublist = voListForUpdate.subList(i*len,((i + 1) * len > size ? size : len * (i + 1)));
                    volunteerDao.batchUpdate(sublist);
                }
            }
        }

    }
    /**
     * 更新站点信息
     */
    @Override
    @Transactional
    public void dailyUpdateStation() {
        //得到所有启用的站点
        List<Station> all = stationDao.findAll();
        List<Station> stationListForUpdate = new ArrayList<>();
        if (all!=null && all.size()>0){
            for (Station station : all) {
                //总活动次数
                int count = 0;
                //总人数
                int number = 0;
                BigDecimal allmoney = new BigDecimal(0);
                BigDecimal allhour = new BigDecimal(0);

                CampaignVolunteerRecord temp = new CampaignVolunteerRecord();
                temp.setStationId(station.getId());
                temp.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
                List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(temp);
                if (byParam!=null && byParam.size()>0){
                    for (CampaignVolunteerRecord campaignVolunteerRecord : byParam) {
                        if (campaignVolunteerRecord.getCampaignHour()==null){
                            allhour = allhour.add(new BigDecimal(0));
                        }else {
                            allhour = allhour.add(campaignVolunteerRecord.getCampaignHour());
                        }
                        if (campaignVolunteerRecord.getDonateMoney()==null){
                            allmoney = allmoney.add(new BigDecimal(0));
                        }else {
                            allmoney = allmoney.add(campaignVolunteerRecord.getDonateMoney());
                        }
                    }
                }
                //查询活动表计算站点活动次数
                int c = campaignDao.countCampaignByStation(station.getId());
                count = count + c;
                //查询志愿者表计算站点下人数
                int n = volunteerDao.countMan(station.getId());
                number = number + n;

                //更新站点
                Station s = new Station();
                s.setId(station.getId());
                //设置服务站人数
                s.setStationNumber(number);
                //设置活动次数
                s.setStationCampaignCount(count);
                //设置活动时长
                s.setStationCampaignHour(allhour);
                //设置捐款金额
                s.setStationDonateMoney(allmoney);
                stationListForUpdate.add(s);
            }
        }
        if (CollectionUtils.isNotEmpty(stationListForUpdate)){
			//截取更新list 1000条一更新
			if (stationListForUpdate.size()<1000){
				//批量更新志愿者信息
				stationDao.batchUpdate(stationListForUpdate);
			}else {
				int len = 1000;
				int size = stationListForUpdate.size();
				int count = size % 1000==0?size /1000:size /1000+1;
				for (int i = 0;i<count;i++){
					//截取1000条数据
					List<Station> sublist = stationListForUpdate.subList(i*len,((i + 1) * len > size ? size : len * (i + 1)));
					stationDao.batchUpdate(sublist);
				}
			}
		}
    }
    /**
     * 更新志愿者等级
     */
    @Override
    @Transactional
    public void dailyUpdateVolunteerLevel() {
        LevelRegulation level1 = levelRegulationDao.queryByLevel(1);
        LevelRegulation level2 = levelRegulationDao.queryByLevel(2);
        LevelRegulation level3 = levelRegulationDao.queryByLevel(3);
        LevelRegulation level4 = levelRegulationDao.queryByLevel(4);
        LevelRegulation level5 = levelRegulationDao.queryByLevel(5);

        Volunteer obj = new Volunteer();
        obj.setVolunteerStatus(Const.VOLUNTEER_STATUS_ACTIVE);
        //得到所有激活的志愿者信息
        List<Volunteer> allActiveVolunteer = volunteerDao.findAllActiveVolunteer(obj);
        List<Volunteer> volunteerListForLevelUp = new ArrayList<>();
        int flag = 0;//0是默认 1是服务时长 2是捐款
        if (allActiveVolunteer!=null && allActiveVolunteer.size()>0){
            for (Volunteer volunteer : allActiveVolunteer) {
                Volunteer temp = new Volunteer();
                temp.setId(volunteer.getId());
                temp.setCardId(volunteer.getCardId());
                //等级1
                if (volunteer.getCampaignHour().compareTo(level1.getServiceTime()) == 1 && volunteer.getServiceLevel() < 1){
                    temp.setServiceLevel(1);
                    flag = 1;
                }
                if (volunteer.getDonateMoney().compareTo(level1.getDonationAmount())== 1 && volunteer.getServiceLevel() < 1){
                    temp.setServiceLevel(1);
                    flag = 2;
                }
                //等级2
                if (volunteer.getCampaignHour().compareTo(level2.getServiceTime())== 1 && volunteer.getServiceLevel() < 2){
                    temp.setServiceLevel(2);
                    flag = 1;
                }
                if (volunteer.getDonateMoney().compareTo(level2.getDonationAmount())== 1 && volunteer.getServiceLevel() < 2){
                    temp.setServiceLevel(2);
                    flag = 2;
                }
                //等级3
                if (volunteer.getCampaignHour().compareTo(level3.getServiceTime())== 1 && volunteer.getServiceLevel() < 3){
                    temp.setServiceLevel(3);
                    flag = 1;
                }
                if (volunteer.getDonateMoney().compareTo(level3.getDonationAmount())== 1 && volunteer.getServiceLevel() < 3){
                    temp.setServiceLevel(3);
                    flag = 2;
                }
                //等级4
                if (volunteer.getCampaignHour().compareTo(level4.getServiceTime())== 1 && volunteer.getServiceLevel() < 4){
                    temp.setServiceLevel(4);
                    flag = 1;
                }
                if (volunteer.getDonateMoney().compareTo(level4.getDonationAmount())== 1 && volunteer.getServiceLevel() < 4){
                    temp.setServiceLevel(4);
                    flag = 2;
                }
                //等级5
                if (volunteer.getCampaignHour().compareTo(level5.getServiceTime())== 1 && volunteer.getServiceLevel() < 5){
                    temp.setServiceLevel(5);
                    flag = 1;
                }
                if (volunteer.getDonateMoney().compareTo(level5.getDonationAmount())== 1 && volunteer.getServiceLevel() < 5){
                    temp.setServiceLevel(5);
                    flag = 2;
                }
                if (temp.getServiceLevel()!=null){
                    temp.setCampaignHour(volunteer.getCampaignHour());
                    temp.setDonateMoney(volunteer.getDonateMoney());
                    temp.setFlag(flag);
                    volunteerListForLevelUp.add(temp);
                }

            }
            for (Volunteer volunteer : volunteerListForLevelUp) {
              /*  MqMessage mqMessage = new MqMessage();
                mqMessage.setAppId(Const.APPID_VOL);
                mqMessage.setTemplateId(Long.valueOf(MessageTemplateEnum.VOL_LEVEL_UP.getId()));*/
                BaseVo b1 = sysServiceFeign.queryUserByIdcard(volunteer.getCardId());
                String s1 = JSON.toJSONString(b1.getData());
                User user = JSON.parseObject(s1,User.class);
                if (user!=null){
                    Long std = user.getId();
                    Long[] targetId = new Long[]{std};
                  /*
                    mqMessage.setTargetId(targetId);
                    mqMessage.setTargetType(TargetTypeEnum.USER.getCode());
                    mqMessage.setVersion(0);*/
                    String[] params =null;
                    if (volunteer.getFlag() == 1){
                        params = new String[] {"服务时长",volunteer.getServiceLevel().toString()};
                       // mqMessage.setParams(params);
                    }
                    if (volunteer.getFlag() == 2){
                        params =  new String[] {"捐款金额",volunteer.getServiceLevel().toString()};
                       // mqMessage.setParams(params);
                    }


                    Calendar calendar = Calendar.getInstance();//日历对象
                    calendar.setTime(new Date());//设置当前日期

                    String yearStr = calendar.get(Calendar.YEAR)+"";//获取年份
                    int month = calendar.get(Calendar.MONTH) + 1;//获取月份
                    String monthStr = month < 10 ? "0" + month : month + "";
                    int day = calendar.get(Calendar.DATE);//获取日
                    String pushtime = yearStr+"-"+monthStr+"-"+day+" "+"09:00:00";
                   /* mqMessage.setPushTime(pushtime);
                    mqMessage.setTargetUserType(TargetTypeEnum.EXTERNALUSER.getCode()+"");
                    mqMessage.setCreater("系统");
                    //发送到web端
                    mqMessageUtil.assembleMqMessage(rabbitTemplate,mqMessage);*/

                    MqSendMessage mqSendMessage = AppPushMessageUtil.pushOrgMessageByAlias(MessageTemplateEnum.VOL_LEVEL_UP,targetId,params,"系统",new Date(),pushtime);
                    sendMqPushUtil.sendMqMessage(mqSendMessage);
                }
            }
        }


    }
    /**
     * 更新排行榜
     */
    @Override
    public void dailyUpdateBoard() {
        VolunteerVO volunteerVO = new VolunteerVO();
        PageHelper.startPage(volunteerVO.getPageNum(),volunteerVO.getPageSize());
        List<Board> volunteerList = volunteerDao.board(volunteerVO);
        cacheUtil.set(RedisKey.VOL_BOARD.getKey(),volunteerList);

    }

    /**
     * 更新活动数据
     */
    @Override
    @Transactional
    public void dailyUpdateCamapignData() {
        List<Campaign> byConditionPage = campaignDao.findAllPassed();
        List<Campaign> campaignListForUpdate = new ArrayList<>();
        for (Campaign campaign : byConditionPage) {
            CalculateParam param = new CalculateParam();
            param.setCampaignId(campaign.getId());
            //查询时间总和 捐款总额 和人数总数
            Calculate calculate = campaignVolunteerRecordDao.countTimeAndMoneyAndNumber(param);

            Integer signNumber = campaignVolunteerRecordDao.countSignNumberByCampaignId(campaign.getId());
            Integer enrollNumber = campaignVolunteerRecordDao.countEnrollNumberByCampaignId(campaign.getId());
            //更新活动
            Campaign obj = new Campaign();
            obj.setId(campaign.getId());
            //设置报名人数
            obj.setEnrollNumber(enrollNumber);
            //设置签到人数
            obj.setSignNumber(signNumber);
            //设置捐款数
            obj.setCampaignDonateMoney(calculate.getMoney());
            //设置活动时长
            obj.setCampaignAllHour(calculate.getHour());
            obj.setVersion(campaign.getVersion());
            campaignListForUpdate.add(obj);
        }
        if (CollectionUtils.isNotEmpty(campaignListForUpdate)){
			//截取更新list 1000条一更新
			if (campaignListForUpdate.size()<1000){
				//批量更新志愿者信息
				campaignDao.batchUpdate(campaignListForUpdate);
			}else {
				int len = 1000;
				int size = campaignListForUpdate.size();
				int count = size % 1000==0?size /1000:size /1000+1;
				for (int i = 0;i<count;i++){
					//截取1000条数据
					List<Campaign> sublist = campaignListForUpdate.subList(i*len,((i + 1) * len > size ? size : len * (i + 1)));
					campaignDao.batchUpdate(sublist);
				}
			}
		}
    }
}
