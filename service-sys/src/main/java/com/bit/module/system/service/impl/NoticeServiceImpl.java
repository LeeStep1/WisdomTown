package com.bit.module.system.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.SysConst;
import com.bit.common.consts.ApplicationTypeEnum;
import com.bit.module.system.ApplyLogs.bean.PageMongo;
import com.bit.module.system.ApplyLogs.bean.PageMongoInfo;
import com.bit.module.system.ApplyLogs.repository.ApplyRepository;
import com.bit.module.system.bean.*;
import com.bit.module.system.dao.DictDao;
import com.bit.module.system.dao.NoticeDao;
import com.bit.module.system.dao.OaDepartmentDao;
import com.bit.module.system.dao.UserDao;
import com.bit.module.system.feign.FileServiceFeign;
import com.bit.module.system.feign.StationServiceFeign;
import com.bit.module.system.service.*;
import com.bit.module.system.vo.NoticeVO;
import com.bit.soft.push.common.MqBaseConst;
import com.bit.soft.push.msEnum.MsgTypeEnum;
import com.bit.soft.push.msEnum.TargetTypeEnum;
import com.bit.soft.push.payload.MqNoticeMessage;
import com.bit.soft.push.payload.UserMessage;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.DateUtil;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-14 10:20
 */
@Service("noticeService")
public class NoticeServiceImpl extends BaseService implements NoticeService {

    @Autowired
    private NoticeDao noticeDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private DictDao dictDao;
    @Autowired
    private OrgService orgService;
    @Autowired
    private OrgUserService orgUserService;
    @Autowired
    private FileServiceFeign fileServiceFeign;
    @Autowired
    private StationServiceFeign stationServiceFeign;
    @Autowired
    private UserRelVolStationService userRelVolStationService;
    @Autowired
    private ApplyRepository applyRepository;
   /* @Autowired
    private RabbitTemplate rabbitTemplate;*/
    @Autowired
    private OaDepartmentDao oaDepartmentDao;
   /* @Autowired
    private MqNoticeMessageUtil mqNoticeMessageUtil;*/
    @Autowired
    private PbOrganizationService pbOrganizationService;
    @Autowired
    private SendMqPushUtil sendMqPushUtil;


    /**
     * 添加通知公告 或 直接发布
     *
     * @param notice
     * @return
     */
    @Override
    @Transactional
    public BaseVo add(Notice notice) {
        if (StringUtil.isEmpty(notice.getTid())) {
            throwBusinessException("请选择接入端");
        }
        List<FileInfo> fileInfoList = notice.getFileInfoList();
        String fileIds = "";
        if (fileInfoList != null && fileInfoList.size() > 0) {
            for (FileInfo fileInfo : fileInfoList) {
                fileIds += fileInfo.getId();
                fileIds += ",";
            }
            fileIds = fileIds.substring(0, fileIds.length() - 1);
            notice.setFileId(fileIds);
        }
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            throwBusinessException("请重新登录");
        }
        Long authorId = userInfo.getId();
        notice.setAuthorId(authorId);
        if (notice.getNoticeStatus().equals(SysConst.NOTICE_STATUS_RELEASED)) {
            //通知
            if (notice.getNoticeType().equals(MsgTypeEnum.MSG_TYPE_MESSAGE.getCode())) {
                //通知不对接收人做判断
            } else if (notice.getNoticeType().equals(MsgTypeEnum.MSG_TYPE_ANNOUNCEMENT.getCode())) {
                //公告不需要接收人
                notice.setUserIdList(null);
            }

        }

        //插入通知公告表
        noticeDao.add(notice);
        noticeMessageSend(notice, userInfo.getRealName());

        return new BaseVo();
    }

    /**
     * 修改通知公告 或 发布
     *
     * @param notice
     * @return
     */
    @Override
    @Transactional
    public BaseVo update(Notice notice) {
        UserInfo userInfo = getCurrentUserInfo();
        //更新附件
        List<FileInfo> fileInfoList = notice.getFileInfoList();
        String fileIds = "";
        if (fileInfoList != null && fileInfoList.size() > 0) {
            for (FileInfo fileInfo : fileInfoList) {
                fileIds += fileInfo.getId();
                fileIds += ",";
            }
            fileIds = fileIds.substring(0, fileIds.length() - 1);
            notice.setFileId(fileIds);
        }
        //要发布了
        if (notice.getNoticeStatus().equals(SysConst.NOTICE_STATUS_RELEASED)) {

            //通知
            if (notice.getNoticeType().equals(MsgTypeEnum.MSG_TYPE_NOTICE.getCode())) {
                //通知不对接收人做判断
            } else if (notice.getNoticeType().equals(MsgTypeEnum.MSG_TYPE_ANNOUNCEMENT.getCode())) {
                //公告不需要接收人
                notice.setUserIdList(null);
            }


        }
        //更新记录
        noticeDao.update(notice);
        noticeMessageSend(notice, userInfo.getRealName());
        return new BaseVo();
    }


    //公用的发送mq 通知公告方法
    private void noticeMessageSend(Notice notice, String realName) {
        //如果是发布
        if (notice.getNoticeStatus() == SysConst.NOTICE_STATUS_RELEASED) {
            // 插入mq
            MqNoticeMessage mqNoticeMessage = new MqNoticeMessage();
            mqNoticeMessage.setAppId(notice.getAppId().intValue());
            mqNoticeMessage.setCreater(realName);
            mqNoticeMessage.setBusinessId(notice.getId());

            if (notice.getAppId().equals(ApplicationTypeEnum.APPLICATION_PB.getApplicationId().longValue())) {
                OrganizationPb byId = pbOrganizationService.findPbOrgById(notice.getPublishOrgId());
                if (byId != null) {
                    mqNoticeMessage.setCreater(byId.getName());
                }
            }
            else if (notice.getAppId().equals(ApplicationTypeEnum.APPLICATION_PB.getApplicationId().longValue())) {
                OaDepartment byId = oaDepartmentDao.findById(notice.getPublishOrgId());
                if (byId != null) {
                    mqNoticeMessage.setCreater(byId.getName());
                }
            }
            else if (notice.getAppId().equals(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId().longValue())) {
                BaseVo b1 = stationServiceFeign.reflectById(notice.getPublishOrgId());
                if (b1.getData() != null) {
                    String ss = JSON.toJSONString(b1.getData());
                    Station station = JSON.parseObject(ss, Station.class);
                    mqNoticeMessage.setCreater(station.getStationName());
                }
            }
            //通知
            if (notice.getNoticeType().equals(MsgTypeEnum.MSG_TYPE_NOTICE.getCode())) {

                String[] tid = notice.getTid().split(",");
                mqNoticeMessage.setTid(tid);
                if (StringUtil.isNotEmpty(notice.getUserIdList())) {
                    String[] ss = notice.getUserIdList().split(",");
                    List<String> resultList = new ArrayList<>(ss.length);
                    Collections.addAll(resultList, ss);

                    List<Long> longList = new ArrayList<>();
                    for (String str : resultList) {
                        longList.add(Long.valueOf(str));
                    }

                    mqNoticeMessage.setTargetType(TargetTypeEnum.USER.getCode());
                    Long[] targetId = {};
                    Long[] longs = longList.toArray(targetId);
                    mqNoticeMessage.setTargetId(longs);
                } else {
                    //后面改mq工程中的消费端，原来使用MqConstEnum 中的做判断。
                    mqNoticeMessage.setTargetType(TargetTypeEnum.ALL.getCode());
                    //mqNoticeMessage.setTargetType(0);
                }
                mqNoticeMessage.setTitle(notice.getTitle());
                mqNoticeMessage.setMsgTypeName(MsgTypeEnum.MSG_TYPE_NOTICE.getInfo());
                mqNoticeMessage.setMsgType(MsgTypeEnum.MSG_TYPE_NOTICE.getCode());
            }
            //公告
            if (notice.getNoticeType().equals(MsgTypeEnum.MSG_TYPE_ANNOUNCEMENT.getCode())) {
                mqNoticeMessage.setTargetType(TargetTypeEnum.ALL.getCode());
                String[] tid = notice.getTid().split(",");
                mqNoticeMessage.setTid(tid);
                mqNoticeMessage.setTitle(notice.getTitle());
                mqNoticeMessage.setMsgTypeName(MsgTypeEnum.MSG_TYPE_ANNOUNCEMENT.getInfo());
                mqNoticeMessage.setMsgType(MsgTypeEnum.MSG_TYPE_ANNOUNCEMENT.getCode());
            }
           // mqNoticeMessageUtil.assembleMqNoticeMessage(rabbitTemplate, mqNoticeMessage);
            sendMqPushUtil.sendMqNoticeMessage(mqNoticeMessage);
        }
    }

    /**
     * 删除通知公告
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseVo delete(Long id) {
        //删除用户
//        noticeRelUserDao.deleteByNoticeId(id);
        //删除记录
        noticeDao.delete(id);
        return new BaseVo();
    }

    /**
     * 通知管理 公告管理 的 反显通知公告
     *
     * @param id
     * @return
     */
    @Override
    @Transactional
    public BaseVo reflectById(Long id) {

        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            throwBusinessException("请重新登陆");
        }
        NoticeUserFile noticeUserFile = new NoticeUserFile();

        Notice notice = noticeDao.queryById(id);
        User byId = userDao.findById(notice.getAuthorId());
        notice.setAuthorName(byId.getRealName());

        if (notice.getAppId().equals(ApplicationTypeEnum.APPLICATION_PB.getApplicationId().longValue())) {
            OrganizationPb pb = pbOrganizationService.findPbOrgById(notice.getPublishOrgId());
            if (pb != null) {
                notice.setPublishOrgName(pb.getName());
                notice.setPublishOrgStr(pb.getId().toString());
            }
        }
        else if (notice.getAppId().equals(ApplicationTypeEnum.APPLICATION_OA.getApplicationId().longValue())) {
            OaDepartment oa = oaDepartmentDao.findById(notice.getPublishOrgId());
            if (oa != null) {
                notice.setPublishOrgName(oa.getName());
                notice.setPublishOrgStr(oa.getId().toString());
            }
        }
        else if (notice.getAppId().equals(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId().longValue())) {
            BaseVo b1 = stationServiceFeign.reflectById(notice.getPublishOrgId());
            if (b1.getData() != null) {
                String ss = JSON.toJSONString(b1.getData());
                Station station = JSON.parseObject(ss, Station.class);
                notice.setPublishOrgName(station.getStationName());
                notice.setPublishOrgStr(station.getId().toString());
            }
        }

        noticeUserFile.setNotice(notice);

        List<FileInfo> fileInfos = new ArrayList<>();
        //查询附件
        String fileids = notice.getFileId();
        if (StringUtil.isNotEmpty(fileids)) {
            String[] str = fileids.split(",");
            for (String s : str) {
                if (StringUtil.isNotEmpty(s)) {
                    BaseVo b1 = fileServiceFeign.findById(Long.parseLong(s));
                    String ss = JSON.toJSONString(b1.getData());
                    FileInfo fileInfo = JSON.parseObject(ss, FileInfo.class);
                    fileInfos.add(fileInfo);
                }
            }
        }
        noticeUserFile.setFileInfoList(fileInfos);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(noticeUserFile);
        return baseVo;
    }

    /**
     * 单查通知公告 不做状态改变
     *
     * @param id
     * @return
     */
    @Override
    public BaseVo queryById(Long id) {

        NoticeUserFile noticeUserFile = new NoticeUserFile();

        Notice notice = noticeDao.queryById(id);
        User byId = userDao.findById(notice.getAuthorId());
        notice.setAuthorName(byId.getRealName());

        noticeUserFile.setNotice(notice);
        List<FileInfo> fileInfos = new ArrayList<>();
        //查询附件
        String fileids = notice.getFileId();
        if (StringUtil.isNotEmpty(fileids)) {
            String[] str = fileids.split(",");
            for (String s : str) {
                if (StringUtil.isNotEmpty(s)) {
                    BaseVo b1 = fileServiceFeign.findById(Long.parseLong(s));
                    String ss = JSON.toJSONString(b1.getData());
                    FileInfo fileInfo = JSON.parseObject(ss, FileInfo.class);
                    fileInfos.add(fileInfo);
                }
            }
        }
        noticeUserFile.setFileInfoList(fileInfos);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(noticeUserFile);
        return baseVo;
    }

    /**
     * 分页查询通知 公告 待办 已办 消息
     *
     * @param noticeVO
     * @return
     */
    @Override
    public BaseVo listPage(NoticeVO noticeVO) {
        UserInfo userInfo = getCurrentUserInfo();

        Long userId = userInfo.getId();
        noticeVO.setUserId(userId);
        noticeVO.setTid(userInfo.getTid());
        PageMongo<UserMessage> pageMongos = applyRepository.listPage(noticeVO);
        List<UserMessage> userMessages = new ArrayList<>();
        userMessages = pageMongos.getRows();
        int total = pageMongos.getTotalCount().intValue();
        int totalpages = pageMongos.getTotalPage().intValue();
        int pageNum = noticeVO.getPageNum();
        int pageSize = noticeVO.getPageSize();
        List<NoticePage> noticePageList = new ArrayList<>();


        if (userMessages != null && userMessages.size() > 0) {
            for (UserMessage userMessage : userMessages) {
                NoticePage noticePage = new NoticePage();
                noticePage.setMongoId(userMessage.get_id());
                noticePage.setId(userMessage.getBusinessId());
                noticePage.setAppId(userMessage.getAppid().longValue());
                noticePage.setAuthorName(userMessage.getCreater());
                noticePage.setContent(userMessage.getContent());
                noticePage.setNoticeType(userMessage.getMsgType());
                try {
                    noticePage.setCreateTime(DateUtil.parse(userMessage.getRecTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                noticePage.setReaded(userMessage.getStatus());
                noticePage.setCategoryCode(userMessage.getCategoryCode());
                noticePage.setVersion(userMessage.getVersion());
                noticePage.setLevelOneMenu(userMessage.getLevelOneMenu());

                Dict obj = new Dict();
                obj.setModule(SysConst.MODULE_APP);
                obj.setDictCode(userMessage.getAppid().toString());
                Dict byModuleAndDictCode = dictDao.findByModuleAndDictCode(obj);
                noticePage.setSourceName(byModuleAndDictCode.getDictName());
                noticePage.setCategoryName(userMessage.getCategoryName());

                noticePageList.add(noticePage);
            }
        }
        PageMongoInfo<NoticePage> pageInfo = new PageMongoInfo<NoticePage>(noticePageList, 8, total, pageMongos.getTotalPage().intValue(), pageNum, pageSize);

        if (pageNum < totalpages) {
            pageInfo.setHasNextPage(true);
            pageInfo.setNextPage(pageNum + 1 > pageMongos.getTotalPage().intValue() ? 0 : pageNum + 1);
        } else {
            pageInfo.setHasNextPage(false);
        }
        if (pageNum > 1 && pageNum <= total) {
            pageInfo.setHasPreviousPage(true);
            pageInfo.setPrePage(pageNum - 1 == 0 ? 0 : pageNum - 1);
        } else {
            pageInfo.setHasPreviousPage(false);
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }

    /**
     * 分页查询公告 这个用没用
     * @param noticeVO
     * @return
     */
//    @Override
//    public BaseVo anlistPage(NoticeVO noticeVO) {
//        UserInfo userInfo = getCurrentUserInfo();
//        if (userInfo==null){
//            throwBusinessException("请重新登陆");
//        }
//        Long userId = userInfo.getId();
//        noticeVO.setUserId(userId);
//        PageHelper.startPage(noticeVO.getPageNum(),noticeVO.getPageSize());
//
//        List<NoticePage> noticePages = noticeDao.listPage(noticeVO);
//        if (noticePages!=null && noticePages.size()>0){
//            for (NoticePage noticePage : noticePages) {
//                User byId = userDao.findById(noticePage.getAuthorId());
//                if (byId!=null){
//                    noticePage.setAuthorName(byId.getRealName());
//                }
//                Dict obj = new Dict();
//                obj.setModule(SysConst.MODULE_APP);
//                obj.setDictCode(noticePage.getAppId().toString());
//                Dict byModuleAndDictCode = dictDao.findByModuleAndDictCode(obj);
//                noticePage.setSourceName(byModuleAndDictCode.getDictName());
//
//                NoticeRelUser noticeRelUser = new NoticeRelUser();
//                noticeRelUser.setUserId(userId);
//                noticeRelUser.setNoticeId(noticePage.getId());
//                List<NoticeRelUser> noticeRelUserList = noticeRelUserDao.queryByNoticeIdAndUserId(noticeRelUser);
//                if (noticeRelUserList!=null && noticeRelUserList.size()==1){
//                    noticePage.setReaded(noticeRelUserList.get(0).getReaded());
//                }
//                if (noticeRelUserList.size()==0){
//                    noticePage.setReaded(SysConst.IS_READ_NO);
//                }
//            }
//        }
//
//        PageInfo<NoticePage> pageInfo = new PageInfo<>(noticePages);
//        BaseVo baseVo = new BaseVo();
//        baseVo.setData(pageInfo);
//
//        return baseVo;
//    }

    /**
     * 分页查询用户发送的通知公告
     *
     * @param noticeVO
     * @return
     */
    @Override
    public BaseVo userlistPage(NoticeVO noticeVO) {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            throwBusinessException("请重新登陆");
        }
        Long userId = userInfo.getId();
        noticeVO.setAuthorId(userId);
        PageHelper.startPage(noticeVO.getPageNum(), noticeVO.getPageSize());
        List<NoticePage> noticePages = noticeDao.userlistPage(noticeVO);

        PageInfo<NoticePage> pageInfo = new PageInfo<NoticePage>(noticePages);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }

    /**
     * 查询当前用户下的应用
     *
     * @return
     */
    @Override
    public BaseVo getApp() {
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo == null) {
            throwBusinessException("请重新登录");
        }
        Long userId = userInfo.getId();
        List<Dict> app = noticeDao.getApp(userId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(app);
        return baseVo;
    }

    /**
     * 根据应用查询组织结构
     *
     * @param appId
     * @return
     */
    @Override
    public BaseVo queryOrgByAppId(Long appId) {
        BaseVo baseVo = new BaseVo();
        //党建组织
        if (appId.equals( Long.valueOf(ApplicationTypeEnum.APPLICATION_PB.getApplicationId()))) {
            List<Organization> organizationList = orgService.treePbOrg();
            baseVo.setData(organizationList);
        }
        //政务组织
        else if (appId.equals( Long.valueOf(ApplicationTypeEnum.APPLICATION_OA.getApplicationId()))) {
            List<Organization> organizationList = orgService.treeOaOrg();
            baseVo.setData(organizationList);
        }
        //志愿者组织
        else if (appId.equals( Long.valueOf(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId()))) {
            BaseVo b1 = stationServiceFeign.findAllStation();
            String s1 = JSON.toJSONString(b1.getData());
            List<Station> stationList = JSONArray.parseArray(s1, Station.class);
            baseVo.setData(stationList);
        }

        return baseVo;
    }

    /**
     * 根据应用和用户查询组织结构
     *
     * @param appId
     * @return
     */
    @Override
    public BaseVo queryOrgByAppIdUser(Long appId) {
        BaseVo baseVo = new BaseVo();
        //党建组织
        if (appId.equals( Long.valueOf(ApplicationTypeEnum.APPLICATION_PB.getApplicationId()))) {

            List<PbOrganization> pbOrganizations = orgService.queryPbOrg();
            baseVo.setData(pbOrganizations);
        }
        //政务组织
        else if (appId.equals(Long.valueOf(ApplicationTypeEnum.APPLICATION_OA.getApplicationId()))) {

            List<OaDepartment> oaDepartmentList = orgService.queryOaOrg();
            baseVo.setData(oaDepartmentList);
        }
        //志愿者组织
       else if (appId.equals(Long.valueOf(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId()))){
            List<Long> stationIds = userRelVolStationService.findByUser();
            BaseVo b1 = stationServiceFeign.batchSelectByStationIds(stationIds);
            String s1 = JSON.toJSONString(b1.getData());
            List<Station> stationList = JSONArray.parseArray(s1, Station.class);
            baseVo.setData(stationList);
        }
        return baseVo;
    }

    /**
     * 根据应用 组织id 查询人员
     *
     * @param appId
     * @param orgId
     * @return
     */
    @Override
    public BaseVo queryUserByAppId(Long appId, Long orgId) {
        BaseVo baseVo = new BaseVo();
        if (appId .equals( Long.valueOf(ApplicationTypeEnum.APPLICATION_PB.getApplicationId()))) {
            List<User> pbUser = orgUserService.getPbUser(orgId);
            baseVo.setData(pbUser);
        }
        if (appId .equals( Long.valueOf(ApplicationTypeEnum.APPLICATION_OA.getApplicationId()))) {
            List<User> oaUser = orgUserService.getOaUser(orgId);
            baseVo.setData(oaUser);
        }
        //志愿者组织
        if (appId.equals( Long.valueOf(ApplicationTypeEnum.APPLICATION_VOL.getApplicationId()))) {
            List<User> volUser = orgUserService.getVolUser(orgId);
            baseVo.setData(volUser);
        }
        return baseVo;
    }

    /**
     * 根据通知公告类型更改已读状态
     *
     * @param noticeVO
     * @
     */
    @Override
    public BaseVo readAll(NoticeVO noticeVO) {
        UserInfo userInfo = getCurrentUserInfo();
        Long userId = userInfo.getId();
        noticeVO.setUserId(userId);
        List<UserMessage> userMessages = applyRepository.listPageWithoutPage(noticeVO);
        if (CollectionUtils.isNotEmpty(userMessages)) {
            applyRepository.updateMsgStatus(userMessages, MqBaseConst.MONGO_MESSAGE_STATUS_READED);
        }

        return new BaseVo();
    }

    /**
     * 根据用户id 查询最大的公告id
     * @param userId
     * @return
     */
//    @Override
//    public BaseVo getMaxId(Long userId) {
//        NoticeMaxRecord temp = noticeMaxRecordDao.queryByUserId(userId);
//        BaseVo baseVo = new BaseVo();
//        baseVo.setData(temp);
//        return baseVo;
//    }

    /**
     * 根据应用 组织id 姓名 查询人员
     *
     * @param orgAndName
     * @return
     */
    @Override
    public BaseVo queryUserByAppIdOrgIdsName(OrgAndName orgAndName) {
        BaseVo baseVo = new BaseVo();
        Long appId = orgAndName.getAppId();
        if (appId.equals(Long.valueOf(ApplicationTypeEnum.APPLICATION_PB.getApplicationId()))) {
            List<User> pbUser = orgUserService.getPbUserByName(orgAndName.getPbOrgIds(), orgAndName.getName());
            baseVo.setData(pbUser);
        }
        if (appId.equals(Long.valueOf(ApplicationTypeEnum.APPLICATION_OA.getApplicationId()))) {
            List<User> oaUser = orgUserService.getOaUserByName(orgAndName.getOrgIds(), orgAndName.getName());
            baseVo.setData(oaUser);
        }
        return baseVo;
    }

    /**
     * 铃铛的查询
     *
     * @return
     */
    @Override
    public BaseVo listPageForBell() {
        UserInfo userInfo = getCurrentUserInfo();
        Long userId = userInfo.getId();
        Integer tid = userInfo.getTid();
        PageMongo<NoticeBell> noticeBellPageMongo = applyRepository.listPageForBell(userId, tid);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(noticeBellPageMongo);
        return baseVo;
    }

    /**
     * 更新mongo的msgtype
     *
     * @param mongoId
     * @return
     */
    @Override
    public BaseVo updateMsgType(String mongoId) {
        UserMessage um = applyRepository.queryMessage(mongoId);
        //只有是待办消息才更新
       /* if (um.getMsgType().equals(MqBaseConst.MONGO_MESSAGE_MSG_TYPE_WAIT_ALERT)){
            applyRepository.updateMsgType(um, MqBaseConst.MONGO_MESSAGE_MSG_TYPE_ALREADY_ALERT);

        }*/
        if (um.getMsgType().equals(MsgTypeEnum.MSG_TYPE_WAIT.getCode())) {
            applyRepository.updateMsgType(um, MsgTypeEnum.MSG_TYPE_ALREADY.getCode());

        }
        return new BaseVo();
    }

    /**
     * 更新mongo的status
     *
     * @param mongoId
     * @return
     */
    @Override
    public BaseVo updateMongoMsgStatus(String mongoId) {
        UserMessage userMessage = applyRepository.queryMessage(mongoId);
        List<UserMessage> userMessages = new ArrayList<>();
        userMessages.add(userMessage);
        applyRepository.updateStatus(MqBaseConst.MONGO_MESSAGE_STATUS_READED, mongoId);
//        applyRepository.updateMsgStatus(userMessages, MqBaseConst.MONGO_MESSAGE_STATUS_READED);
        return new BaseVo();
    }

    /**
     * 删除mongo的消息
     *
     * @param
     * @return
     */
    @Override
    public BaseVo deleteMongoMessage(String mongoId) {
        applyRepository.deleteMessage(mongoId);
        return new BaseVo();
    }
}
