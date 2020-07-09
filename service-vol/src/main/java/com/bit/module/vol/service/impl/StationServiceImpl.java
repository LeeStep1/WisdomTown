package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.vol.bean.*;
import com.bit.module.vol.dao.CampaignDao;
import com.bit.module.vol.dao.StationDao;
import com.bit.module.vol.dao.VolunteerDao;
import com.bit.module.vol.feign.FileServiceFeign;
import com.bit.module.vol.feign.SysServiceFeign;
import com.bit.module.vol.service.StationService;
import com.bit.module.vol.vo.CampaignVO;
import com.bit.module.vol.vo.FileInfoVO;
import com.bit.module.vol.vo.StationVO;
import com.bit.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-05 9:04
 */
@Service("stationService")
public class StationServiceImpl extends BaseService implements StationService {

    @Autowired
    private StationDao stationDao;
    @Autowired
    private VolunteerDao volunteerDao;
    @Autowired
    private CampaignDao campaignDao;
    @Autowired
    private FileServiceFeign fileServiceFeign;
    @Autowired
    private VolUtil volUtil;
    @Autowired
    private CodeUtil codeUtil;
    @Autowired
    private SysServiceFeign sysServiceFeign;


    /**
     * 添加站点
     * @param station
     */
    @Override
    @Transactional
    public BaseVo add(Station station) {
        Long upid = station.getUpid();
        List<Station> all = stationDao.findAll();
        if (all.size()==0){
            station.setId(100L);
            //确定服务站code
            String stationCode = CodeUtil.getStationCode("Z", 1);
            station.setStationCode(stationCode);
            station.setStationLevel(1);
        }else if (upid==null){
//            Long topStation = stationDao.findTopStation();
//            Long id = topStation+1;
//            //确定服务站code
//            Station byId = stationDao.findById(topStation);
//            String ss = byId.getStationCode().substring(1,byId.getStationCode().length());
//            Integer num = Integer.valueOf(ss);
//            String stationCode = CodeUtil.getStationCode("Z", num);
//            station.setId(id);
//            station.setStationCode(stationCode);
//            //确定服务站等级
//            int level = RadixUtil.getlevel(id.toString());
//            station.setStationLevel(level);
            throwBusinessException("上级机构必选");
        }else {
            //根据上级查询下级
            List<Long> ids = stationDao.getMaxIds(upid.toString());
            Long max = null;
            //如果只有一个结果说明是顶级下面第一个二级站点
            if (ids.size()==1){
                for (Long id : ids) {
                    String s = id.toString()+Const.STATION_FIRST_CODE;
                    max=Long.parseLong(s);
                    station.setId(max);
                }
            }else {
                List<Long> idss = new ArrayList<>();
                for (Long id : ids) {
                    idss.add(id);
                }
                Collections.sort(idss);
                Long id = idss.get(idss.size() - 1);
                max = id+1;
                station.setId(max);
            }
            //确定服务站等级
            int level = RadixUtil.getlevel(max.toString());
            station.setStationLevel(level);
            //设置服务站编号
            String stationCode = codeUtil.genStationCode(station);
            station.setStationCode(stationCode);
        }

        Integer count = stationDao.countSameName(station.getStationName());
        if (count>0){
            throwBusinessException("服务站名称重复");
        }
        station.setStationCampaignCount(Const.INIT_CAMPAIGN_NUMBER);
        station.setStationCampaignHour(Const.INIT_HOUR);
        station.setStationDonateMoney(Const.INIT_MONEY);
        station.setStationNumber(Const.INIT_NUMBER);
        station.setStationStatus(Const.STATION_STATUS_ACTIVE);
        station.setCreateTime(new Date());
        //如果是共建单位则站点类型为0
        if (station.getPartnerOrgType().equals(Const.PARTENER_ORG_TYPE_YES)){
            station.setPartnerOrgType(Const.PARTENER_ORG_TYPE_YES);
        }
        //如果不是共建单位
        if (station.getPartnerOrgType().equals(Const.PARTENER_ORG_TYPE_NO)){
            station.setPartnerOrgType(Const.PARTENER_ORG_TYPE_NO);
        }
        stationDao.add(station);
        return new BaseVo();
    }



    /**
     * 反显站点
     * @param id
     * @return
     */
    @Override
    public Station reflectById(Long id) {
        Station station = stationDao.findById(id);
        if (station.getStationLevel()!=1){
            String str = station.getId().toString();
            str = str.substring(0,str.length()-3);
            station.setUpid(Long.parseLong(str));
        }

        return station;
    }

    /**
     * 分页查询
     * @param stationVO
     * @return
     */
    @Override
    public BaseVo listPage(StationVO stationVO) {
        PageHelper.startPage(stationVO.getPageNum(),stationVO.getPageSize());
        List<Station> stationList = stationDao.listPage(stationVO);
        PageInfo<Station> pageInfo = new PageInfo<Station>(stationList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }
    /**
     * 更改记录状态
     * @param station
     * @return
     */
    @Override
    @Transactional
    public BaseVo update(Station station) {
        stationDao.update(station);
        return new BaseVo();
    }
    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    @Override
    public void exportToExcel(String stationName,
                              String firstChargeMan,
                              String firstChargeManMobile,
                              Integer stationStatus,
                              Integer partnerOrgType,
                              HttpServletResponse response,
                              HttpServletRequest request,
                              Integer type) {

        Station s = new Station();
        s.setStationName(stationName);
        s.setFirstChargeMan(firstChargeMan);
        s.setFirstChargeManMobile(firstChargeManMobile);
        s.setPartnerOrgType(partnerOrgType);
        s.setStationStatus(stationStatus);

        List<Station> stationList = stationDao.listPageForExcel(s);
        List<StationExcel> stationExcelList = new ArrayList<>();
        List<StationPartExcel> stationPartExcelList = new ArrayList<>();
        int num = 1;
        //导出服务站
        if (type == 1){
            for (Station station : stationList) {
                StationExcel stationExcel = new StationExcel();
                BeanUtils.copyProperties(station,stationExcel);

                //设置序号
                stationExcel.setNum(num);
                if (station.getStationStatus().equals(Const.STATION_STATUS_ACTIVE)){
                    stationExcel.setStatus("正常");
                }
                if (station.getStationStatus().equals(Const.STATION_STATUS_INACTIVE)){
                    stationExcel.setStatus("停用");
                }
                //设置站点类型
                stationExcel.setStationType(volUtil.getStationType(station.getStationType().toString()));
                stationExcelList.add(stationExcel);
                num = num+1;
            }
        }
        //导出共建单位
        if (type == 2){
            for (Station station : stationList) {
                StationPartExcel stationPartExcel = new StationPartExcel();
                BeanUtils.copyProperties(station,stationPartExcel);

                //设置序号
                stationPartExcel.setNum(num);
                if (station.getStationStatus().equals(Const.STATION_STATUS_ACTIVE)){
                    stationPartExcel.setStatus("正常");
                }
                if (station.getStationStatus().equals(Const.STATION_STATUS_INACTIVE)){
                    stationPartExcel.setStatus("停用");
                }

                stationPartExcelList.add(stationPartExcel);
                num = num+1;
            }
        }

        try {
            String fileName = "";
            //导出服务站
            if (type == 1){

                fileName = "站点列表_"+ DateUtil.date2String(new Date(),DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
                fileName = new String(fileName.getBytes("UTF-8"), "UTF-8")+".xls";
                response.setHeader("content-type","application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                //导出操作
                ExcelUtil.exportExcel(stationExcelList,null,"站点列表",StationExcel.class, fileName,response);
            }
            //导出共建单位
            if (type == 2){
                fileName = "共建单位列表_"+ DateUtil.date2String(new Date(),DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
                fileName = new String(fileName.getBytes("UTF-8"), "UTF-8")+".xls";
                response.setHeader("content-type","application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
                //导出操作
                ExcelUtil.exportExcel(stationPartExcelList,null,"共建单位列表",StationPartExcel.class, fileName,response);
            }



        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
    /**
     * 生成服务站树
     * @param stationId
     * @return
     */
    @Override
    public List<Station> tree(Long stationId) {
        Station s = stationDao.findById(stationId);
        int level = RadixUtil.getlevel(s.getId().toString());
        String s1 = s.getId().toString().substring(0,level*3);
        List<Station> stationList = stationDao.findSubStation(s1);

        //根节点
        List<Station> rootList = new ArrayList<>();
        for (Station station : stationList) {
            if (station.getStationLevel().equals(s.getStationLevel())){
                rootList.add(station);
            }
        }
        for (Station station : rootList) {
            station.setChildStationList(getChild(stationList,station.getId().toString(),station.getStationLevel()));
        }

        return rootList;
    }

    /**
     * 子节点
     * @param stationId
     * @return
     */
    @Override
    public List<Station> childTree(Long stationId) {
        List<Station> childList = new ArrayList<>();
        childList = stationDao.findSubStation(stationId.toString());
        return childList;
    }

    /**
     * 判断机构编号是否重复
     * @param code
     * @return
     */
    @Override
    public Integer countSameCode(String code) {
        return stationDao.countSameCode(code);
    }
    /**
     * 根据志愿者id查询服务站信息
     * @param volunteerId
     * @return
     */
    @Override
    public BaseVo queryStationByVolunteerId(Long volunteerId) {
        Volunteer byId = volunteerDao.findById(volunteerId);
        if (byId==null){
            throwBusinessException("改用户不存在");
        }
        Long stationId = byId.getStationId();
        Station station = stationDao.findById(stationId);
        StationApp stationApp = new StationApp();
        stationApp.setStation(station);

        CampaignVolunteerRecord obj = new CampaignVolunteerRecord();
        obj.setStationId(station.getId());
        //查询前三个
        CampaignVO campaignVO = new CampaignVO();
        campaignVO.setPageNum(1);
        campaignVO.setPageSize(3);
        List<Campaign> campaignList = campaignDao.findTopThree(campaignVO);

        stationApp.setCampaignList(campaignList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(stationApp);
        return baseVo;
    }
    /**
     * 查询所有的站点
     * @return
     */
    @Override
    public BaseVo findAllStation() {
        List<Station> all = stationDao.findAll();
        BaseVo baseVo = new BaseVo();
        baseVo.setData(all);
        return baseVo;
    }

    private List<Station> getChild(List<Station> list,String levelId,Integer level){
        List<Station> obj = new ArrayList<>();
        for (Station organizationParam : list){
            int l = organizationParam.getStationLevel();
            int y=level+1;
            //如果层级等于下一层级
            if (l==y){
                String str = levelId.substring(0, level * 3);
                String pbstr = organizationParam.getId().toString().substring(0, level * 3);
                if (str.equals(pbstr)){
                    obj.add(organizationParam);
                }
            }
        }
        for (Station organizationParam : obj){
            organizationParam.setChildStationList(getChild(list,organizationParam.getId().toString(),organizationParam.getStationLevel()));
        }
        if(obj.size()==0){
            return  null;
        }
        return obj;
    }

    /**
     * 查询下级服务站 不包含自己
     * @param stationId
     * @return
     */
    @Override
    public List<Station> childTreeExcludeSelf(Long stationId) {
        List<Station> childList = stationDao.findSubStationExcludeSelfSql(stationId.toString());
        return childList;
    }
    /**
     * 查询站点资料
     * @param stationId
     * @return
     */
    @Override
    public BaseVo data(Long stationId) {
        Station byId = stationDao.findById(stationId);
        if (byId==null){
            throwBusinessException("这个站点不存在");
        }
        String informationFile = byId.getInformationFile();
        List<Long> fileids = new ArrayList<>();
        List<FileInfo> fileInfoList = new ArrayList<>();
        if (StringUtil.isNotEmpty(informationFile)){
            String[] str = informationFile.split(",");
            for (String s : str) {
                fileids.add(Long.parseLong(s));
            }
            FileInfoVO fileInfoVO = new FileInfoVO();
            fileInfoVO.setFileIds(fileids);
            BaseVo b1 = fileServiceFeign.findByIds(fileInfoVO);
            String s1 = JSON.toJSONString(b1.getData());
            fileInfoList = JSON.parseArray(s1,FileInfo.class);

        }

        BaseVo baseVo = new BaseVo();
        baseVo.setData(fileInfoList);
        return baseVo;
    }
    /**
     * 批量查询服务站
     * @param stationIds
     * @return
     */
    @Override
    public BaseVo batchSelectByStationIds(List<Long> stationIds) {
        List<Station> stationList = stationDao.batchSelectByStationIds(stationIds);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(stationList);
        return baseVo;
    }
    /**
     * 停用服务站
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseVo deactivate(Long id) {
        Station byId = stationDao.findById(id);
        if (byId==null){
            throwBusinessException("该站点不存在");
        }
        Volunteer vol = new Volunteer();
        vol.setStationId(id);
        vol.setVolunteerStatus(Const.VOLUNTEER_STATUS_ACTIVE);
        List<Volunteer> byParam = volunteerDao.findByParam(vol);
        if (byParam!=null && byParam.size()>0){
            throwBusinessException("该站点还存在志愿者不能停用");
        }
        List<Long> userList = sysServiceFeign.queryAllUserBystationId(id);
        if (userList.size()>0){
            throwBusinessException("该站点还存在管理员不能停用");
        }
        Station station = new Station();
        station.setStationStatus(Const.STATION_STATUS_INACTIVE);
        station.setId(id);
        stationDao.update(station);
        return successVo();
    }
    /**
     * 查询顶级站点
     * @return
     */
    @Override
    public Long findTopStation() {
        return stationDao.findTopStation();
    }
    /**
     * 查询顶级站点信息
     * @return
     */
    @Override
    public BaseVo findTopStationDetail() {
        Station station = new Station();
        station.setStationLevel(1);
        List<Station> param = stationDao.findParam(station);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(param);
        return baseVo;
    }


}
