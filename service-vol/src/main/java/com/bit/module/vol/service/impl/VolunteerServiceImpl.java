package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.UserInfo;
import com.bit.base.dto.VolunteerInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.common.consts.RedisKey;
import com.bit.common.enumerate.CampaignStatusEnum;
import com.bit.core.utils.CacheUtil;
import com.bit.module.vol.bean.*;
import com.bit.module.vol.dao.CampaignDao;
import com.bit.module.vol.dao.CampaignVolunteerRecordDao;
import com.bit.module.vol.dao.StationDao;
import com.bit.module.vol.dao.VolunteerDao;
import com.bit.module.vol.service.VolunteerService;
import com.bit.module.vol.vo.VolunteerVO;
import com.bit.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

/**
 * @author chenduo
 * @create 2019-03-05 14:03
 */
@Service("volunteerService")
public class VolunteerServiceImpl extends BaseService implements VolunteerService {
    @Autowired
    private VolunteerDao volunteerDao;
    @Autowired
    private CampaignVolunteerRecordDao campaignVolunteerRecordDao;
    @Autowired
    private CampaignDao campaignDao;
    @Autowired
    private StationDao stationDao;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private VolUtil volUtil;
    @Autowired
    private CampaignUtil campaignUtil;

    /**
     * 添加志愿者信息
     * @param volunteer
     */
    @Override
    @Transactional
    public BaseVo add(Volunteer volunteer) {

        Volunteer vi = volUtil.getVolunteerInfo();
        Long stationId = vi.getStationId();
        if (volunteer.getRealName().length()>10){
            BaseVo baseVo = new BaseVo();
            baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            baseVo.setData(null);
            baseVo.setMsg("真实姓名长度过长");
            return baseVo;
        }
        if (volunteer.getWeChatId().length()>50){
            BaseVo baseVo = new BaseVo();
            baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            baseVo.setData(null);
            baseVo.setMsg("微信号长度过长");
            return baseVo;
        }
        if (volunteer.getHobby().length()>50){
            BaseVo baseVo = new BaseVo();
            baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            baseVo.setData(null);
            baseVo.setMsg("特长爱好长度过长");
            return baseVo;
        }
        if (volunteer.getNativePlace().length()>50){
            BaseVo baseVo = new BaseVo();
            baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            baseVo.setData(null);
            baseVo.setMsg("籍贯长度过长");
            return baseVo;
        }
        if (volunteer.getLivingPlace().length()>50){
            BaseVo baseVo = new BaseVo();
            baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            baseVo.setData(null);
            baseVo.setMsg("现居住地长度过长");
            return baseVo;
        }
        if (volunteer.getCompany().length()>50){
            BaseVo baseVo = new BaseVo();
            baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            baseVo.setData(null);
            baseVo.setMsg("工作学习单位长度过长");
            return baseVo;
        }

        Station station = new Station();
        if (stationId!=null){
            station = stationDao.findById(stationId);
        }else {
            throwBusinessException("没选择站点");
        }
        List<Integer> longs = new ArrayList<>();
        List<String> stringList = volunteerDao.queryBynewSerialNumber(station.getStationCode());
        if (CollectionUtils.isNotEmpty(stringList)){
            for (String s : stringList) {
                String str = s.substring(s.length()-4,s.length());
                longs.add(Integer.parseInt(str));
            }
            int max = Collections.max(longs);
            max = max +1;
            String code = CodeUtil.getCode(station,max);
            volunteer.setSerialNumber(code);
        }else {
            String code = CodeUtil.getCode(station,1);
            volunteer.setSerialNumber(code);
        }

        if (volunteer.getVolunteerStatus()==null){
            volunteer.setVolunteerStatus(Const.VOLUNTEER_STATUS_ACTIVE);
        }

        volunteer.setVolunteerSyncho(Const.VOLUNTEER_SYNCHO_NO);
        try {
            volunteer.setBirthday(DateUtil.parse(volunteer.getBirth() + " 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        volunteer.setCampaignCount(Const.INIT_COUNT);
        volunteer.setCampaignHour(Const.INIT_HOUR);
        volunteer.setDonateMoney(Const.INIT_MONEY);
        volunteer.setServiceLevel(Const.INIT_LEVEL);
        volunteer.setCreateTime(new Date());
        volunteer.setStationId(stationId);
        volunteer.setPoint(Const.INIT_POINT);
        volunteer.setVersion(Const.INIT_VERSION);
        volunteer.setVolunteerImage(Const.INIT_VOLUNTEER_IMAGE);
        volunteer.setInitHour(Const.INIT_HOUR);
        volunteer.setInitMoney(Const.INIT_MONEY);
        volunteerDao.add(volunteer);
        return new BaseVo();
    }


    /**
     * 单查志愿者信息
     * @param id
     */
    @Override
    public BaseVo reflectById(Long id) {
        Volunteer volunteer = volunteerDao.findById(id);
        Station byId = stationDao.findById(volunteer.getStationId());
        if (byId!=null){
            volunteer.setStationName(byId.getStationName());
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(volunteer);
        return baseVo;
    }
    /**
     * 更新志愿者信息
     * @param volunteer
     */
    @Override
    @Transactional
    public BaseVo update(Volunteer volunteer) {
        Long id = volunteer.getId();
        Volunteer byId = volunteerDao.findById(id);
        if (byId==null){
            throwBusinessException("此人不存在");
        }
        Volunteer obj = new Volunteer();
        BeanUtils.copyProperties(volunteer,obj);
        obj.setVersion(byId.getVersion());
        if (StringUtil.isNotEmpty(obj.getBirth())){
            try {
                obj.setBirthday(DateUtil.parse(obj.getBirth() + " 00:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        volunteerDao.update(obj);
        return new BaseVo();
    }

    /**
     * 分页查询
     * @param volunteerVO
     * @return
     */
    @Override
    public BaseVo listPage(VolunteerVO volunteerVO) {
        Volunteer vi = volUtil.getVolunteerInfo();
        Long stationId = vi.getStationId();
        //查询出子站点和自己
        List<Station> subStation = stationDao.findSubStation(stationId.toString());
        List<Long> stations = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subStation)){
            for (Station station : subStation) {
                stations.add(station.getId());
            }
            volunteerVO.setChildStationList(stations);
        }
        volunteerVO.setStationId(null);
        PageHelper.startPage(volunteerVO.getPageNum(), volunteerVO.getPageSize());
        List<Volunteer> volunteerList = volunteerDao.listPage(volunteerVO);

        PageInfo<Volunteer> pageInfo = new PageInfo<>(volunteerList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * app用查询
     * @param volunteerVO
     * @return
     */
    @Override
    public BaseVo nlistPage(VolunteerVO volunteerVO) {
        PageHelper.startPage(volunteerVO.getPageNum(), volunteerVO.getPageSize());
        List<Volunteer> volunteerList = volunteerDao.listPage(volunteerVO);
        PageInfo<Volunteer> pageInfo = new PageInfo<>(volunteerList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 志愿者参加过的活动
     * @param volunteerVO
     * @return
     */
    @Override
    public BaseVo queryCampaignByVolunteer(VolunteerVO volunteerVO) {
        Volunteer byId = volunteerDao.findById(volunteerVO.getId());
        if (byId==null){
            throwBusinessException("志愿者不存在");
        }
        List<VolunteerCampaign> volunteerCampaignList = new ArrayList<>();
        // 查询志愿者参加活动的记录

        PageHelper.startPage(volunteerVO.getPageNum(), volunteerVO.getPageSize());
        List<Long> campaignIds = new ArrayList<>();
        CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
        obj.setVolunteerId(volunteerVO.getId());
        obj.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
        obj.setRecordStatus(Const.CAMPAIGN_RECORD_STATUS_ACTIVE);
        obj.setCampaignTheme(volunteerVO.getCampaignTheme());
        List<CampaignVolunteerRecord> byParam = campaignVolunteerRecordDao.findByParam(obj);
        for (CampaignVolunteerRecord campaignVolunteerRecord : byParam) {
            campaignIds.add(campaignVolunteerRecord.getCampaignId());
        }

        for (Long campaignId : campaignIds) {
            VolunteerCampaign volunteerCampaign = new VolunteerCampaign();
            Campaign campaign = campaignDao.queryById(campaignId);
            volunteerCampaign.setCampaignTheme(campaign.getCampaignTheme());
            Map<String, Object> map = campaignUtil.getStartOrEndTime(campaign.getBeginDate(), campaign.getFinishDate());
            volunteerCampaign.setStartTime((String) map.get("startTime"));
            volunteerCampaign.setEndTime((String) map.get("endTime"));
            volunteerCampaign.setStartDate((Integer) map.get("startDate"));
            volunteerCampaign.setEndDate((Integer) map.get("endDate"));
            // 根据志愿者id和活动id计算时长和捐款
            Calculate calculate = campaignVolunteerRecordDao.countTimeAndMoneyByVoIdAndCamId(volunteerVO.getId(), campaignId);
            BigDecimal money = calculate.getMoney();
            BigDecimal hour = calculate.getHour();

            if (money.equals(new BigDecimal(0)) || money==null){
                volunteerCampaign.setDonateMoney(new BigDecimal(0));
            }
            volunteerCampaign.setDonateMoney(money);

            if (hour.equals(new BigDecimal(0)) || hour==null){
                volunteerCampaign.setServiceHour(new BigDecimal(0));
            }
            volunteerCampaign.setServiceHour(hour);

            volunteerCampaignList.add(volunteerCampaign);
        }
        PageInfo<VolunteerCampaign> pageInfo = new PageInfo<VolunteerCampaign>(volunteerCampaignList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    @Override
    public void exportToExcel(String realName,String cardId,BigDecimal lhour,BigDecimal rhour,String stationName,Integer serviceLevel,Integer volunteerStatus, HttpServletResponse response) {
        Volunteer v = new Volunteer();
        v.setRealName(realName);
        v.setCardId(cardId);
        v.setLhour(lhour);
        v.setRhour(rhour);
        v.setServiceLevel(serviceLevel);
        v.setStationName(stationName);
        v.setVolunteerStatus(volunteerStatus);

        List<Long> stationIds = new ArrayList<>();

        List<Volunteer> volunteerList = this.listPageForExcel(v);
        for (Volunteer volunteer : volunteerList) {
            stationIds.add(volunteer.getStationId());
        }
        List<Station> stationList = stationDao.batchSelectByIds(stationIds);

        List<Dict> peopleList =  volUtil.getDict("people");
        List<Dict> educationList =  volUtil.getDict("education");
        List<Dict> politicalList =  volUtil.getDict("political");
        List<Dict> healthList =  volUtil.getDict("health");
        List<Dict> experienceList =  volUtil.getDict("experience");

        List<VolunteerExcel> volunteerExcelList = new ArrayList<>();
        for (int i=0;i < volunteerList.size();i++) {
            VolunteerExcel volunteerExcel = new VolunteerExcel();
            Volunteer volunteer = volunteerList.get(i);
            BeanUtils.copyProperties(volunteer,volunteerExcel);
            //设置性别
            if (volunteer.getSex().equals(1)){
                volunteerExcel.setSex("男");
            }
            if (volunteer.getSex().equals(2)){
                volunteerExcel.setSex("女");
            }
            //设置出生日期
            if (volunteer.getBirthday()!=null){
                volunteerExcel.setBirthday(DateUtil.date2String(volunteer.getBirthday(),DateUtil.DatePattern.YYYYMMDD.getValue()));
            }

            //设置序号
            volunteerExcel.setNum(i+1);

            //设置服务站名称
            for (Station station : stationList){
                if (volunteer.getStationId()!=null && volunteer.getStationId().equals(station.getId())){
                    volunteerExcel.setStationName(station.getStationName());
                    break;
                }
            }

            //设置民族
            for (Dict dict : peopleList){
                if (volunteer.getPeople()!=null && volunteer.getPeople().toString().equals(dict.getDictCode())){
                    volunteerExcel.setPeople(dict.getDictName());
                    break;
                }
            }
            //设置教育程度
            for (Dict dict : educationList){
                if (volunteer.getEducation()!=null && volunteer.getEducation().toString().equals(dict.getDictCode())){
                    volunteerExcel.setEducation(dict.getDictName());
                    break;
                }
            }
            //设置政治面貌
            for (Dict dict : politicalList){
                if (volunteer.getPolitical()!=null && volunteer.getPolitical().toString().equals(dict.getDictCode())){
                    volunteerExcel.setPolitical(dict.getDictName());
                    break;
                }
            }
            //设置健康状况
            for (Dict dict : healthList){
                if (volunteer.getHealth()!=null && volunteer.getHealth().toString().equals(dict.getDictCode())){
                    volunteerExcel.setHealth(dict.getDictName());
                    break;
                }
            }
            //设置志愿者经验
            for (Dict dict : experienceList){
                if (volunteer.getExperience()!=null && volunteer.getExperience().toString().equals(dict.getDictCode())){
                    volunteerExcel.setExperience(dict.getDictName());
                    break;
                }
            }
            //设置志愿服务类别
            if (volunteer.getServiceType()!=null){
                String serviceType = volUtil.getServiceType(volunteer.getServiceType());
                volunteerExcel.setServiceType(serviceType);
            }
            //设置服务时间
            if (volunteer.getServiceTime()!=null){
                String serviceTime =volUtil.getServiceTime(volunteer.getServiceTime());
                volunteerExcel.setServiceTime(serviceTime);
            }

            volunteerExcelList.add(volunteerExcel);
        }
        try {
            String fileName = "志愿者列表_"+ DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
            fileName = new String(fileName.getBytes("UTF-8"), "UTF-8")+".xls";
            response.setHeader("content-type","application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);

            //导出操作
            ExcelUtil.exportExcel(volunteerExcelList,null,"志愿者列表",VolunteerExcel.class, fileName,response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    /**
     * 设置业务信息
     * @param volunteer
     * @return
     */
    @Override
    @Transactional
    public BaseVo setBusinessInfo(Volunteer volunteer) {

        //从token中获取数据
        UserInfo userInfo = getCurrentUserInfo();

        if(userInfo == null){
            throwBusinessException("获取用户信息失败");
        }

        //获取姓名
        volunteer.setRealName(userInfo.getRealName());

        //身份证号
        volunteer.setCardId(userInfo.getIdcard());

        //手机号
        volunteer.setMobile(userInfo.getMobile());

        //志愿者是否启用
        volunteer.setVolunteerStatus(Const.VOLUNTEER_STATUS_ACTIVE);

        //记录创建时间
        volunteer.setCreateTime(new Date());

        //获取编号
        Station station = new Station();
        Long stationId = volunteer.getStationId();
        if (stationId!=null){
            station = stationDao.findById(stationId);
        }else {
            throwBusinessException("没选择站点");
        }

        List<Integer> longs = new ArrayList<>();
        List<String> stringList = volunteerDao.queryBynewSerialNumber(station.getStationCode());
        if (CollectionUtils.isNotEmpty(stringList)){
            for (String s : stringList) {
                String str = s.substring(s.length()-4,s.length());
                longs.add(Integer.parseInt(str));
            }
            int max = Collections.max(longs);
            max = max +1;
            String code = CodeUtil.getCode(station,max);
            volunteer.setSerialNumber(code);
        }else {
            String code = CodeUtil.getCode(station,1);
            volunteer.setSerialNumber(code);
        }

        //将明细插入数据库
        volunteerDao.add(volunteer);
        return new BaseVo();
    }

    /**
     * app签到接口
     * @param campaignId
     * @return
     */
    @Override
    @Transactional
    public BaseVo sign(Long campaignId) {
        Volunteer vi = volUtil.getVolunteerInfo();

        Campaign temp = campaignDao.queryById(campaignId);
        if (temp==null){
            throwBusinessException("该活动不存在");
        }

        if (temp.getCampaignStatus().equals(CampaignStatusEnum.CAMPAIGN_STATUS_CANCELED.getCode())){
            throwBusinessException("该活动已取消");
        }

        Integer st = campaignUtil.setCampaignStatusWithDate(temp.getBeginDate(), temp.getFinishDate());
        //根据当前时间判断活动状态 未开始 结束 已取消 和 默认状态 都不能签到
        if (st>1){
            throwBusinessException("不能签到");
        }

        //判断这个人是否报名
        CampaignVolunteerRecord check = new CampaignVolunteerRecord();
        check.setVolunteerId(vi.getId());
        check.setCampaignId(campaignId);
        check.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_NO);
        check.setRecordStatus(Const.VOLUNTEER_STATUS_ENROLL);
        List<CampaignVolunteerRecord> recordVolunteerCampaign = campaignVolunteerRecordDao.findByParam(check);
        //没报名 爆出异常
        if (recordVolunteerCampaign ==null || recordVolunteerCampaign.size()<=0){
            throwBusinessException("没有报名记录");
        }else {
            //报名了 继续
            //判断这个人是否已经签到
            CampaignVolunteerRecord checksign = new CampaignVolunteerRecord();
            checksign.setVolunteerId(vi.getId());
            checksign.setCampaignId(campaignId);
            checksign.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
            List<CampaignVolunteerRecord> signedVolunteerCampaign = campaignVolunteerRecordDao.findSignedVolunteerCampaign(checksign);
            if (CollectionUtils.isNotEmpty(signedVolunteerCampaign)){
                throwBusinessException("不能重复签到");
            }
            //没签到 就更新记录
            Long recordId = recordVolunteerCampaign.get(0).getId();
            Integer version = recordVolunteerCampaign.get(0).getVersion();
            CampaignVolunteerRecord campaignVolunteerRecord = new CampaignVolunteerRecord();

            campaignVolunteerRecord.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
            //签到只更新活动时长
            campaignVolunteerRecord.setCampaignHour(temp.getCampaignHour());
            campaignVolunteerRecord.setSignTime(new Date());
            campaignVolunteerRecord.setVersion(version);
            campaignVolunteerRecord.setId(recordId);
            campaignVolunteerRecord.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
            //更新记录表
            campaignVolunteerRecordDao.update(campaignVolunteerRecord);

            //签到时更新志愿者服务时长

            Volunteer byId = volunteerDao.findById(vi.getId());
			Volunteer vv = new Volunteer();
			vv.setId(vi.getId());
			vv.setCampaignHour(byId.getCampaignHour().add(temp.getCampaignHour()));
			vv.setCampaignCount(byId.getCampaignCount()+1);
			vv.setPoint(byId.getPoint().add(temp.getCampaignHour()));
			volunteerDao.update(vv);

            //签到时更新活动表
            CalculateParam cal = new CalculateParam();
            cal.setCampaignId(campaignId);
            //根据活动id查询 记录表 得到时长 金额 次数
            Calculate camCal = campaignVolunteerRecordDao.countTimeAndMoneyAndNumber(cal);
            if (camCal.getHour()==null){
                camCal.setHour(new BigDecimal(0));
            }
            if (camCal.getMoney()==null){
                camCal.setMoney(new BigDecimal(0));
            }
            //查询报名人数
            CampaignVolunteerRecord obj1 = new CampaignVolunteerRecord();
            obj1.setRecordStatus(Const.VOLUNTEER_STATUS_ENROLL);
            List<CampaignVolunteerRecord> byParam1 = campaignVolunteerRecordDao.findByParam(obj1);
            //查询签到人数
            CampaignVolunteerRecord obj2 = new CampaignVolunteerRecord();
            obj2.setSignStatus(Const.CAMPAIGN_RECORD_SIGN_STATUS_YES);
            obj2.setRecordStatus(Const.VOLUNTEER_STATUS_ENROLL);
            List<CampaignVolunteerRecord> byParam2 = campaignVolunteerRecordDao.findByParam(obj2);
            if (camCal!=null){
                Campaign cap = new Campaign();
                cap.setId(campaignId);
//                cap.setCampaignDonateMoney(temp.getCampaignDonateMoney().add(camCal.getMoney()));
				cap.setCampaignDonateMoney(camCal.getMoney());
                cap.setCampaignAllHour(camCal.getHour());
                cap.setSignNumber(byParam2 == null? 0 : byParam2.size());
                cap.setEnrollNumber(byParam1 == null? 0 : byParam1.size());
                cap.setVersion(temp.getVersion());
                campaignDao.update(cap);
            }
        }

        return new BaseVo();
    }
    /**
     * 根据身份证获取志愿者信息
     * @param cardId
     * @return
     */
    @Override
    public VolunteerInfo getInfo(String cardId) {
        Volunteer volunteer = volunteerDao.queryByCardId(cardId);
        if (volunteer != null) {
            Station station = stationDao.findById(volunteer.getStationId());
            VolunteerInfo obj = new VolunteerInfo();
            obj.setStationId(volunteer.getStationId());
            obj.setVolunteerId(volunteer.getId());
            if (station != null) {
                obj.setStationLevel(station.getStationLevel());
                obj.setStationName(station.getStationName());
            }
            return obj;
        }
        return null;
    }
    /**
     * 判断身份证是否重复
     * @param cardId
     * @return
     */
    @Override
    public BaseVo distinctCardId(String cardId) {
        BaseVo baseVo = new BaseVo();
        Volunteer volunteer = volunteerDao.queryByCardId(cardId);
        if (volunteer==null){
            baseVo.setData(true);
        }else {
            baseVo.setData(false);
        }

        return baseVo;
    }
    /**
     * 根据身份证和手机号校验是否重复
     * @param verifyParam
     * @return
     */
    @Override
    public BaseVo distinctMobileAndCardId(VerifyParam verifyParam) {
        BaseVo baseVo = new BaseVo();
        int result = volunteerDao.verifyCardIdAndMobile(verifyParam);
        if (verifyParam.getId()!=null){
            if (result>1){
                baseVo.setData(false);
            }
            Volunteer volunteer = new Volunteer();
            volunteer.setCardId(verifyParam.getCardId());
            volunteer.setMobile(verifyParam.getMobile());
            List<Volunteer> byParam = volunteerDao.findByParam(volunteer);
            if (byParam!=null){
                if (byParam.size()==1){
                    if (byParam.get(0).getId().equals(verifyParam.getId())){
                        baseVo.setData(true);
                    }
                }
                if (byParam.size()>1){
                    for (Volunteer volunteer1 : byParam) {
                        if (volunteer1.getId().equals(verifyParam.getId())){
                            baseVo.setData(false);
                        }
                    }
                }
            }else {
                baseVo.setData(true);
            }
        }else {
            if (result>=1){
                baseVo.setData(false);
            }else {
                baseVo.setData(true);
            }
        }
        return baseVo;
    }

    /**
     * 根据手机号和身份证号获取志愿者业务表中信息
     * @return
     */
    @Override
    public BaseVo getVolunteerBusInessInfo() {
        // 从token 中获取业务信息
        UserInfo userInfo = getCurrentUserInfo();

        //插入身份证号和手机号
        VerifyParam verifyParam = new VerifyParam();
        verifyParam.setCardId(userInfo.getIdcard());
        verifyParam.setMobile(userInfo.getMobile());

        // 检测是否存在
        BaseVo baseVo = this.checkMobileAndCardId(verifyParam);

        return baseVo;
    }

    /**
     * 校验身份证号和手机号是否重复
     * @author liyang
     * @date 2019-05-29
     * @param verifyParam :
     * @return : BaseVo
    */
    private BaseVo checkMobileAndCardId(VerifyParam verifyParam) {
        BaseVo baseVo = new BaseVo();
        int result = volunteerDao.verifyCardId(verifyParam);

        if(result>0){
            baseVo.setData(false);
        }else {
            baseVo.setData(true);
        }

        return baseVo;
    }

    /**
     * 获取所有志愿者ID
     * @author liyang
     * @date 2019-04-09
     */
    @Override
    public List<Long> getAllVolUserIds() {
        Volunteer volunteer = new Volunteer();

        //设置状态为启用
        volunteer.setVolunteerStatus(Const.VOLUNTEER_STATUS_INACTIVE);

        List<Long> userIds = volunteerDao.getAllVolUserIdsSql(volunteer);

        return userIds;
    }

    /**
     * 排行榜
     * @return
     */
    @Override
    public BaseVo board(VolunteerVO volunteerVO) {
        List<Board> volunteerList = new ArrayList<>();

        Object o = cacheUtil.get(RedisKey.VOL_BOARD.getKey());
        String s = JSONArray.toJSONString(o);
        volunteerList = JSONArray.parseArray(s, Board.class);
        if (volunteerList == null || volunteerList.size()==0){
            PageHelper.startPage(volunteerVO.getPageNum(),volunteerVO.getPageSize());
            volunteerList = volunteerDao.board(volunteerVO);
            cacheUtil.set(RedisKey.VOL_BOARD.getKey(),volunteerList);
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(volunteerList);

        return baseVo;
    }

    /**
     * 加锁更新积分
     * @param volunteer
     */
    @Override
    public void updatePointLock(Volunteer volunteer,BigDecimal point) {
        Integer version = volunteer.getVersion();
        if (version==null){
            version = 0;
        }
        volunteer.setVersion(version);
        volunteer.setPoint(volunteer.getPoint().subtract(point));
        volunteerDao.update(volunteer);
    }
    /**
     * app端更新志愿者信息
     * @param volunteer
     * @return
     */
    @Override
    @Transactional
    public BaseVo appUpdate(Volunteer volunteer) {
        Volunteer vi = volUtil.getVolunteerInfo();
        Long id = vi.getId();
        Volunteer byId = volunteerDao.findById(id);
        if (byId==null){
            throwBusinessException("此人不存在");
        }
        Volunteer obj = new Volunteer();
        BeanUtils.copyProperties(volunteer,obj);
        obj.setVersion(byId.getVersion());
        if (StringUtil.isNotEmpty(obj.getBirth())){
            try {
                obj.setBirthday(DateUtil.parse(obj.getBirth() + " 00:00:00"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        volunteerDao.update(obj);
        return new BaseVo();
    }
    /**
     * app端单查志愿者
     * @return
     */
    @Override
    public BaseVo appReflect() {
        Volunteer vi = volUtil.getVolunteerInfo();
        Volunteer byId = volunteerDao.findById(vi.getId());
        VolunteerDict volunteerDict = new VolunteerDict();
        BeanUtils.copyProperties(byId,volunteerDict);
        volunteerDict.setDictPeople(volUtil.getDictName("people",byId.getPeople()));
        volunteerDict.setDicteducation(volUtil.getDictName("education",byId.getEducation()));
        volunteerDict.setDicthealth(volUtil.getDictName("health",byId.getHealth()));
        volunteerDict.setDictmarriage(volUtil.getDictName("marriage",byId.getMarriage()));
        volunteerDict.setDictpolitical(volUtil.getDictName("political",byId.getPolitical()));
        if (StringUtil.isNotEmpty(byId.getServiceType())){
            volunteerDict.setDictservicetype(volUtil.getServiceType(byId.getServiceType().toString()));
        }else {
            volunteerDict.setDictservicetype("");
        }

        volunteerDict.setDictexperience(volUtil.getDictName("experience",byId.getExperience()));
        if (StringUtil.isNotEmpty(byId.getServiceTime())){
            volunteerDict.setDictservicetime(volUtil.getServiceTime(byId.getServiceTime()));
        }else {
            volunteerDict.setDictservicetime("");
        }

        BaseVo baseVo = new BaseVo();
        baseVo.setData(volunteerDict);
        return baseVo;
    }
    /**
     * 查询给生成excel专用
     * @param volunteer
     * @return
     */
    @Override
    public List<Volunteer> listPageForExcel(Volunteer volunteer) {
        Volunteer vi = volUtil.getVolunteerInfo();
        Long stationId = vi.getStationId();
        //查询出子站点和自己
        List<Station> subStation = stationDao.findSubStation(stationId.toString());
        List<Long> stations = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(subStation)){
            for (Station station : subStation) {
                stations.add(station.getId());
            }
            volunteer.setChildStationList(stations);
        }
        volunteer.setStationId(null);
        List<Volunteer> volunteerList = volunteerDao.listPageForExcel(volunteer);

        return volunteerList;
    }
    /**
     * 根据站点id分页查询志愿者
     * @return
     */
    @Override
    public BaseVo listPageVolByStationId(VolunteerVO volunteerVO) {
        PageHelper.startPage(volunteerVO.getPageNum(), volunteerVO.getPageSize());
        List<Volunteer> volunteerList = volunteerDao.listPage(volunteerVO);

        PageInfo<Volunteer> pageInfo = new PageInfo<>(volunteerList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * 根据站点id查询志愿者记录
     * @param volunteer
     * @return
     */
    @Override
    public BaseVo queryVolunteer(Volunteer volunteer) {
        List<Volunteer> volunteerList = volunteerDao.findByParam(volunteer);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(volunteerList);
        return baseVo;
    }


}
