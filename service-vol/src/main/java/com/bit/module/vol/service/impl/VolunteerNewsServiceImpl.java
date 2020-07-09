package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.ApplyLogCode;
import com.bit.common.BaseConst;
import com.bit.common.Const;
import com.bit.module.applylogs.bean.ApplyLogs;
import com.bit.module.applylogs.repository.ApplyRepository;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.module.vol.bean.FileInfo;
import com.bit.module.vol.bean.Station;
import com.bit.module.vol.bean.VolNews;
import com.bit.module.vol.dao.StationDao;
import com.bit.module.vol.dao.VolunteerNewsDao;
import com.bit.module.vol.feign.FileServiceFeign;
import com.bit.module.vol.service.StationService;
import com.bit.module.vol.service.VolunteerNewsService;
import com.bit.module.vol.vo.FileInfoVO;
import com.bit.module.vol.vo.VolNewsVo;
import com.bit.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 志愿者风采相关实现
 * @author Liy
 */
@Service
public class VolunteerNewsServiceImpl extends BaseService implements VolunteerNewsService {

    /**
     * 站点数据库管理
     */
    @Autowired
    private StationDao stationDao;

    /**
     * 志愿者风采数据库管理
     */
    @Autowired
    private VolunteerNewsDao volunteerNewsDao;

    /**
     * 服务站实现类
     */
    @Autowired
    private StationService stationService;

    /**
     * 文件服务
     */
    @Autowired
    private FileServiceFeign fileServiceFeign;

    /**
     * 日志操作
     */
    @Autowired
    private ApplyRepository applyRepository;
    /**
     * 推送工具类
     */
    @Autowired
    private SendMqPushUtil sendMqPushUtil;


    /**
     * 保存志愿者风采
     * @param volNews
     * @return
     * @author Liy
     */
    @Override
    @Transactional
    public BaseVo saveVolNews(VolNews volNews) {
        UserInfo userInfo = getCurrentUserInfo();

        //获取姓名和ID
        volNews.setCreateStationName(userInfo.getRealName());
        volNews.setCreateUserId(userInfo.getId());

        //获取服务站ID
        volNews.setCreateStationId(userInfo.getVolunteerInfo().getStationId());

        //先判断是新增还是修改
        if(volNews.getId() == null || volNews.getId().equals("")){
            //不存在ID说明新增
            volNews.setOperationType(Const.NEWS_SAVE);
            this.createVolNews(volNews);
            return successVo();
        }else{
            //存在ID 说明修改
            this.modifyVolNewsById(volNews);
            return successVo();
        }
    }

    /**
     * 提交志愿者风采信息
     * @param volNews
     * @return
     * @author Liy
     */
    @Override
    @Transactional
    public BaseVo commitVolNews(VolNews volNews) {
        UserInfo userInfo = getCurrentUserInfo();
        VolNews volNewsNew = new VolNews();

        //先判断是否存在ID
        if(volNews.getId()== null || volNews.getId().equals("")){
            //不存在说明是创建并提交
            volNews.setOperationType(Const.NEWS_COMMIT);
            volNewsNew = this.createVolNews(volNews);
            volNewsNew.setStatus(Const.NEWS_STATUS_NOT_DEL);

        }else {
            //存在说明是直接提交
            //判断是否是一级单位  一级变为已通过  二级变为待审核
            List<Station> childTree = stationService.childTreeExcludeSelf(userInfo.getVolunteerInfo().getStationId());
            if(childTree.isEmpty()){
                volNews.setAuditState(Const.NEWS_AUDIT_STATUS_CHECK_PEDING);
            }else{
                volNews.setAuditState(Const.NEWS_AUDIT_STATUS_PASSED);
            }
            volNews.setUpdateTime(DateUtil.getCurrentDate());
            volunteerNewsDao.update(volNews);
            volNewsNew.setId(volNews.getId());
            volNewsNew.setStatus(Const.NEWS_STATUS_NOT_DEL);
        }

        //获取明细

        volNewsNew = volunteerNewsDao.getNewsDetailsSql(volNewsNew);

        //推送消息
        sendToMq(volNewsNew);

        return successVo();
    }

    /**
     * 消息推送
     * @author liyang
     * @date 2019-04-18
     * @param volNewsNew : 志愿者风采明细
    */
    public void sendToMq(VolNews volNewsNew) {

        //推送人员
        Long[] targetId = {volNewsNew.getApplyStationId()};

        //作者
        Station station = stationService.reflectById(volNewsNew.getCreateStationId());

        //模板参数
        String[] params = {station.getStationName()};

        MqSendMessage mqSendMessageTask= AppPushMessageUtil.pushOrgTaskByAlias(MessageTemplateEnum.VOL_VOLUNTEER_NEWS_COMMIT,targetId,params,volNewsNew.getId(),volNewsNew.getVersion(),station.getStationName(),new Date(),null);
        sendMqPushUtil.sendMqMessage(mqSendMessageTask);

    }

    /**
     * 分页查询志愿者风采信息
     * @param volNewsVo
     * @return
     */
    @Override
    public BaseVo listPage(VolNewsVo volNewsVo) {

        List<VolNews> volNewsListRetrun = new ArrayList<>();

        UserInfo userInfo = getCurrentUserInfo();
        volNewsVo.setCreateStationId(userInfo.getVolunteerInfo().getStationId());

        //先获服务站所有下级(不包括自己)
        List<Long> stationIds = new ArrayList<>();
        List<Station> childTree = stationService.childTreeExcludeSelf(volNewsVo.getCreateStationId());

        //存在下级查询直接查询全部(不包含下级草稿)
        if(!childTree.isEmpty()){
            for (Station station : childTree) {
                stationIds.add(station.getId());
            }

            //添加自己的服务站
            stationIds.add(userInfo.getVolunteerInfo().getStationId());
            volNewsVo.setStationIds(stationIds);

            //设置文章状态 0 未删除  1 已删除
            volNewsVo.setStatus(Const.NEWS_STATUS_NOT_DEL);

            //设置审批状态(查询时不等于此状态)
            volNewsVo.setAuditStateDraft(Const.NEWS_AUDIT_STATUS_DRAFT);

            //根据服务站ID 查询
            PageHelper.startPage(volNewsVo.getPageNum(),volNewsVo.getPageSize());
            volNewsListRetrun = volunteerNewsDao.getChildNewsSql(volNewsVo);

            PageInfo<VolNews> pageInfo = new PageInfo<>(volNewsListRetrun);
            BaseVo baseVo = new BaseVo();
            baseVo.setData(pageInfo);

            return baseVo;
        }else {

            //不存在说明是最下级 只查询自己的
            volNewsListRetrun = getSelfNews(volNewsVo);
          
            PageInfo<VolNews> pageInfo = new PageInfo<>(volNewsListRetrun);
            BaseVo baseVo = new BaseVo();
            baseVo.setData(pageInfo);
            return baseVo;

        }

    }

    /**
     * 查询本站的新闻
     * @param volNewsVo
     * @return
     */
    public List<VolNews> getSelfNews(VolNewsVo volNewsVo) {

        //设置服务站
        List<Long> stationIds = new ArrayList<>();
        stationIds.add(volNewsVo.getCreateStationId());
        volNewsVo.setStationIds(stationIds);

        //设置文章状态 0 未删除  1 已删除
        volNewsVo.setStatus(Const.NEWS_STATUS_NOT_DEL);

        //根据服务站ID 查询
        PageHelper.startPage(volNewsVo.getPageNum(),volNewsVo.getPageSize());
        List<VolNews> volNewsListRetrun = volunteerNewsDao.getAllNewsSql(volNewsVo);

        return volNewsListRetrun;
    }

    /**
     * 审核文章
     * @param volNews
     * @return
     */
    @Override
    @Transactional
    public BaseVo auditNews(VolNews volNews) {
        //根据ID 先获取现有数据库文章 获得锁
        volNews.setStatus(Const.NEWS_STATUS_NOT_DEL);
        VolNews volNewsNew = volunteerNewsDao.getNewsVersionByIdSql(volNews);

        //设置修改状态
        volNewsNew.setAuditState(Const.NEWS_AUDIT_STATUS_PASSED);

        //查询人物名称
        UserInfo userInfo = getCurrentUserInfo();

        //设置修改人
        volNewsNew.setUpdateUserId(userInfo.getId());

        //根据乐观锁修改
        volunteerNewsDao.auditNewsByIdSql(volNewsNew);

        //记录日志
        Integer terminalId = userInfo.getTid();
        ApplyLogs applyLogs = new ApplyLogs(terminalId,
                BaseConst.AUDIT_LOG_TYPE_VOL_NEWS,
                volNewsNew.getId(),
                volNewsNew.getUpdateUserId(),
                userInfo.getRealName(),
                ApplyLogCode.AUDIT_NEWS.getInfo(),
                new Date(),
                ApplyLogCode.AUDIT_NEWS.getCode(),
                DateUtil.format(new Date()),
                ApplyLogCode.AUDIT_PASS.getInfo(),"");

        applyRepository.addApplyLogs(applyLogs);

        return successVo();
    }

    /**
     * 退回申请
     * @param volNews
     * @return
     */
    @Override
    @Transactional
    public BaseVo backNews(VolNews volNews) {
        //根据ID 先获取现有数据库文章 获得锁
        volNews.setStatus(Const.NEWS_STATUS_NOT_DEL);
        VolNews volNewsNew = volunteerNewsDao.getNewsVersionByIdSql(volNews);

        //设置修改状态
        volNewsNew.setAuditState(Const.NEWS_AUDIT_STATUS_REJECTED);
        volNewsNew.setBackReason(volNews.getBackReason());
        volNewsNew.setApplyStationId(volNews.getApplyStationId());

        //查询人物名称
        UserInfo userInfo = getCurrentUserInfo();

        //根据乐观锁修改
        volunteerNewsDao.auditNewsByIdSql(volNewsNew);

        //记录日志
        Integer terminalId = userInfo.getTid();
        ApplyLogs applyLogs = new ApplyLogs(terminalId,
                BaseConst.BACK_LOG_TYPE_VOL_NEWS,
                volNewsNew.getId(),
                volNewsNew.getUpdateUserId(),
                userInfo.getRealName(),
                ApplyLogCode.BACK_NEWS.getInfo(),
                new Date(),
                ApplyLogCode.BACK_NEWS.getCode(),
                DateUtil.format(new Date()),
                ApplyLogCode.AUDIT_BACK.getInfo(),volNews.getBackReason());
        applyRepository.addApplyLogs(applyLogs);


        return successVo();

    }

    /**
     * 查看一篇文章
     * @param volNews
     * @return
     */
    @Override
    public BaseVo getNewsById(VolNews volNews) {
        //获取文章明细
        volNews.setStatus(Const.NEWS_STATUS_NOT_DEL);
        VolNewsVo volNewsVoRturn = volunteerNewsDao.getNewsContextByIdSql(volNews);

        if(volNewsVoRturn == null){
            throwBusinessException("无数据！");
        }

        //通过图片ID获取图片
        BaseVo baseVo = fileServiceFeign.findById(volNewsVoRturn.getImgId());
        if (baseVo.getData()!=null){
            String fileInfoStr = JSON.toJSONString(baseVo.getData());
            FileInfo fileInfo = JSON.parseObject(fileInfoStr,FileInfo.class);
            //将图片地址插入后返回
            volNewsVoRturn.setImgPath(fileInfo.getPath());
        }

        baseVo.setData(volNewsVoRturn);
        return baseVo;
    }

    /**
     * 根据ID 删除文章
     * @param id
     * @return
     */
    @Override
    public BaseVo delNewsById(Long id){
        VolNews volNews = new VolNews();

        //设置删除状态
        volNews.setStatus(Const.NEWS_STATUS_DEL);
        volNews.setId(id);

        volunteerNewsDao.delNewsByIdSql(volNews);

        return successVo();

    }

    /**
     * 获取APP端展示新闻
     * @return
     */
    @Override
    public BaseVo getNewsForAppShow(VolNewsVo volNewsVo) {
        //设置查询属性 审核状态和删除状态
        volNewsVo.setAuditState(Const.NEWS_AUDIT_STATUS_PASSED);
        volNewsVo.setStatus(Const.NEWS_STATUS_NOT_DEL);

        //获取所有站点已经通过的项目
        PageHelper.startPage(volNewsVo.getPageNum(),volNewsVo.getPageSize());
        List<VolNews> volNewsList =  volunteerNewsDao.getNewsForAppShowSql(volNewsVo);

        //获取图片集合
        List<Long> imgIdList = new ArrayList<>();
        volNewsList.forEach(volNews -> {imgIdList.add(volNews.getImgId());});

        //获取所有图片地址
        FileInfoVO fileInfoVO = new FileInfoVO();
        fileInfoVO.setFileIds(imgIdList);
        BaseVo baseFiles = fileServiceFeign.findByIds(fileInfoVO);
        String fileInfoStr = JSON.toJSONString(baseFiles.getData());
        List<FileInfo> fileInfos = JSONArray.parseArray( fileInfoStr, FileInfo.class);

        //循环插入
        Map<Long, FileInfo> fileInfoMap = fileInfos.stream().collect(Collectors.toMap(FileInfo::getId, FileInfo -> FileInfo));
        for(VolNews volNews: volNewsList){
            FileInfo fileInfo = fileInfoMap.get(volNews.getImgId());
            String imagePath = fileInfo.getPath();
            volNews.setImgPath(imagePath);
        }
        PageInfo<VolNews> pageInfo = new PageInfo<>(volNewsList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * APP端查看文章详情
     * @param volNews
     * @return
     */
    @Override
    public BaseVo getNewsDetailForApp(VolNews volNews) {

        //获取文章明细
        volNews.setStatus(Const.NEWS_STATUS_NOT_DEL);
        VolNewsVo volNewsVoRturn = volunteerNewsDao.getNewsContextByIdSql(volNews);

        //通过图片ID获取图片
        BaseVo baseVo = fileServiceFeign.findById(volNewsVoRturn.getImgId());
        String fileInfoStr = JSON.toJSONString(baseVo.getData());
        FileInfo fileInfo = JSON.parseObject(fileInfoStr,FileInfo.class);

        //增加一次阅读次数
        VolNews volNewsUpdate = new VolNews();
        volNewsUpdate.setId(volNews.getId());
        volNewsUpdate.setBrowseCount(volNewsVoRturn.getBrowseCount()+1);
        volunteerNewsDao.update(volNewsUpdate);

        //将图片地址插入后返回
        volNewsVoRturn.setImgPath(fileInfo.getPath());

        baseVo.setData(volNewsVoRturn);
        return baseVo;

    }

    /**
     * 创建志愿者风采
     * @param volNews
     * @return
     * @author Liy
     */
    public VolNews createVolNews(VolNews volNews) {
        UserInfo userInfo = getCurrentUserInfo();
        volNews.setCreateStationId(userInfo.getVolunteerInfo().getStationId());

        //检测是否有上级单位
        Boolean b = getIsSuperSation(volNews.getCreateStationId());
        if(b){
            //检测操作类型
            if(volNews.getOperationType().equals(Const.NEWS_SAVE)){
                //如果操作类型是保存 变成 草稿
                volNews.setAuditState(Const.NEWS_AUDIT_STATUS_DRAFT);
            }else {
                //如果是发布 变成 审核中
                volNews.setAuditState(Const.NEWS_AUDIT_STATUS_CHECK_PEDING);
            }

        }else{
            //检测操作类型
            if(volNews.getOperationType().equals(Const.NEWS_SAVE)){
                //如果操作类型是保存 变成待审核
                volNews.setAuditState(Const.NEWS_AUDIT_STATUS_DEPLOYING);
            }else {
                //如果是发布 变成 已通过
                volNews.setAuditState(Const.NEWS_AUDIT_STATUS_PASSED);
            }
        }

        //获取创建时间
        Date date = DateUtil.getCurrentDate();
        volNews.setCreateTime(date);
        volNews.setUpdateTime(date);

        //创建时  创建人与修改人一致
        volNews.setUpdateUserId(userInfo.getId());
        volNews.setCreateUserId(userInfo.getId());

        //获取服务站名称
        Station station = stationDao.findById(userInfo.getVolunteerInfo().getStationId());
        volNews.setCreateStationName(station.getStationName());

        //获取上级服务站ID
        Station stationUp = getSuperSation(volNews.getCreateStationId());
        volNews.setApplyStationId(stationUp.getId());

        //存入数据库
        volunteerNewsDao.add(volNews);

        //发布记录日志
        if(volNews.getOperationType().equals(Const.NEWS_COMMIT)){

            //查询人物名称
            String terminalId = userInfo.getTid().toString();
            ApplyLogs applyLogs = new ApplyLogs(Integer.parseInt(terminalId),
                    BaseConst.DEPLOYING_LOG_TYPE_VOL_NEWS,
                    volNews.getId(),
                    userInfo.getId(),
                    userInfo.getRealName(),
                    ApplyLogCode.DEPELOYING_NEWS.getInfo(),
                    new Date(),
                    ApplyLogCode.DEPELOYING_NEWS.getCode(),
                    DateUtil.format(new Date()),
                    ApplyLogCode.DEPELOYING_NEWS.getInfo(),"");

            applyRepository.addApplyLogs(applyLogs);
        }

        return volNews;

    }

    /**
     * 根据ID修改支援者风采
     * @param volNews
     * @return
     */
    public void modifyVolNewsById(VolNews volNews) {
        volNews.setUpdateTime(DateUtil.getCurrentDate());
        volunteerNewsDao.update(volNews);
    }

    /**
     * 根据ID检测是否有上级单位
     * @param id
     * @return false 不存在  true 存在
     */
    public Boolean getIsSuperSation(Long id) {
        //去除最后三位
        String idTemp = String.valueOf(id);
        String superId = idTemp.substring(0,idTemp.length()-3);

        //看是否存在，如果存在去查询，如果不存在说明为顶级
        if(superId.equals("")){
            return false;
        }else{
            //检测数据库中是否存在
            Station station = stationDao.findById(Long.parseLong(superId));

            if(station == null){
                return false;
            }else {
                return true;
            }
        }
    }

    public Station getSuperSation(Long id) {
        //去除最后三位
        String idTemp = String.valueOf(id);
        String superId = idTemp.substring(0,idTemp.length()-3);

        //看是否存在，如果存在去查询，如果不存在直接返回
        if(superId.equals("")){
            Station station = new Station();
            station.setId(id);
            return station;
        }else{
            //检测数据库中是否存在
            Station station = stationDao.findById(Long.parseLong(superId));

            if(station == null){
                return null;
            }else {
                return station;
            }
        }
    }


}
