package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.module.cbo.bean.CivilizationBehavior;
import com.bit.module.cbo.dao.CivilizationBehaviorDao;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.service.CivilizationBehaviorService;
import com.bit.module.cbo.vo.CivilizationBehaviorPageVO;
import com.bit.module.cbo.vo.FileInfo;
import com.bit.module.cbo.vo.FileInfoVO;
import com.bit.module.cbo.vo.User;

import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import static com.bit.common.enumerate.CivilizationBehaviorEumn.*;
import static com.bit.soft.push.msEnum.MessageTemplateEnum.CBO_REMIND_CIVILIZED_BEHAVIOR_TO_PMC;
import static com.bit.soft.push.msEnum.MessageTemplateEnum.CBO_REMIND_CIVILIZED_BEHAVIOR_TO_RESIDENT;
import static com.bit.soft.push.msEnum.MessageTemplateEnum.CBO_TASK_CIVILIZED_BEHAVIOR;

/**
 * @description: 文明行为相关实现
 * @author: liyang
 * @date: 2019-08-31
 **/
@Service
public class CivilizationBehaviorServiceImpl extends BaseService implements CivilizationBehaviorService {

    /**
     * 文明行为相关dao
     */
    @Autowired
    private CivilizationBehaviorDao civilizationBehaviorDao;

    /**
     * 文件微服务
     */
    @Autowired
    private FileServiceFeign fileServiceFeign;


    /**
     * sys相关服务
     */
    @Autowired
    private SysServiceFeign sysServiceFeign;

    @Autowired
    private SendMqPushUtil sendMqPushUtil;


    /**
     * 新增文明行为
     * @author liyang
     * @date 2019-08-31
     * @param civilizationBehavior : 文明行为详情
     * @return : BaseVo
    */
    @Override
    public BaseVo add(CivilizationBehavior civilizationBehavior) {

        UserInfo userInfo = getCurrentUserInfo();

        Date now = new Date();

        //创建人ID
        civilizationBehavior.setCreateUserId(userInfo.getId());

        //创建人名称
        civilizationBehavior.setCreateUserName(userInfo.getRealName());

        //创建人名称
        civilizationBehavior.setCreateTime(now);

        //状态:1：待处理，2：处理中:3：已处理 新增默认为待处理
        civilizationBehavior.setStatus(STATUS_PENDING.getCode());

        //手机号
        civilizationBehavior.setCreateUserMobile(userInfo.getMobile());

        //修改人ID（默认新增人）
        civilizationBehavior.setUpdateUserId(userInfo.getId());

        //修改时间（默认新增时间）
        civilizationBehavior.setUpdateTime(now);

        civilizationBehaviorDao.add(civilizationBehavior);

        //确保成功了再推送
        if(!Const.COUNT.equals(civilizationBehavior.getId())){
            this.sendTask(civilizationBehavior);
        }

        return successVo();
    }

    /**
     * 文明行为上报列表
     * @author liyang
     * @date 2019-08-31
     * @param civilizationBehaviorPageVO : 查询详情
     * @return : BaseVo
     */
    @Override
    public BaseVo findAll(CivilizationBehaviorPageVO civilizationBehaviorPageVO) {
        UserInfo userInfo = getCurrentUserInfo();

        //判断是否是社区办
        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG)){
            //插入社区ID
            civilizationBehaviorPageVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        //如果是物业或者居民，只能看自己提交的
        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_RESIDENT) || userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_PMC) ){
            civilizationBehaviorPageVO.setCreateUserId(userInfo.getId());
        }

        //查询列表
        PageHelper.startPage(civilizationBehaviorPageVO.getPageNum(),civilizationBehaviorPageVO.getPageSize());
        List<CivilizationBehavior> civilizationBehaviorList = civilizationBehaviorDao.findAll(civilizationBehaviorPageVO);
        PageInfo<CivilizationBehavior> civilizationBehaviorPageInfo = new PageInfo<>(civilizationBehaviorList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(civilizationBehaviorPageInfo);

        return baseVo;
    }

    /**
     * 修改文明行为状态
     * @author liyang
     * @date 2019-09-02
     * @param civilizationBehavior : 修改详情
     * @return : BaseVo
     */
    @Override
    public BaseVo modify(CivilizationBehavior civilizationBehavior) {

        UserInfo userInfo = getCurrentUserInfo();
        
        //20190927 添加比较version
		//先查询文明上报详细信息
		CivilizationBehavior detail = civilizationBehaviorDao.selectByPrimaryKey(civilizationBehavior.getId());
		if (detail.getVersion()!=civilizationBehavior.getVersion()){
			throwBusinessException("此条数据已经被处理");
		}


		//处理时异常校验
        if(civilizationBehavior.getStatus().equals(STATUS_BEING_PROCESSED.getCode())){
            CivilizationBehavior cb = civilizationBehaviorDao.selectByIdAndStatus(civilizationBehavior.getId(),civilizationBehavior.getStatus());
            if(cb != null){
                throwBusinessException("此条上报信息已处理！");
            }
        }

        Date now = new Date();
        civilizationBehavior.setVersion(detail.getVersion());

        //如果是已反馈 增加反馈状态
        if(civilizationBehavior.getStatus().equals(STATUS_PROCESSED.getCode())){
            civilizationBehavior.setFeedbackUserId(userInfo.getId());
            civilizationBehavior.setFeedbackUserName(userInfo.getRealName());
            civilizationBehavior.setFeedbackTime(now);


        }
        civilizationBehavior.setUpdateUserId(userInfo.getId());
        civilizationBehavior.setUpdateTime(now);
        civilizationBehaviorDao.updateByPrimaryKeySelective(civilizationBehavior);
        //发送消息放在最后，数据库与消息保持原子性
        if(civilizationBehavior.getStatus().equals(STATUS_PROCESSED.getCode())){
            this.sendMessage(detail);
        }

        return successVo();
    }

    /**
     * 查询文明行为明细
     * @author liyang
     * @date 2019-09-02
     * @param id : id
     * @return : BaseVo
     */
    @Override
    public BaseVo detail(Long id) {

        //根据ID查询明细
        CivilizationBehavior civilizationBehavior = civilizationBehaviorDao.selectByPrimaryKey(id);

        BaseVo baseVo = new BaseVo();

        if(civilizationBehavior != null){
            //获取图片地址
            String filesStr = civilizationBehavior.getPicIds();
            String[] ss = filesStr.split(",");
            List<String> filesStringList = Arrays.asList(ss);
            List<Long> ids = new ArrayList<>();
            filesStringList.forEach(idsString -> ids.add(Long.valueOf(idsString)));
            FileInfoVO fileInfoVO = new FileInfoVO();
            fileInfoVO.setFileIds(ids);
            BaseVo baseFile = fileServiceFeign.findByIds(fileInfoVO);
            String fileInfoVOStr = JSON.toJSONString(baseFile.getData());
            List<FileInfo> fileInfoList = JSONArray.parseArray(fileInfoVOStr,FileInfo.class);
            List<String> picAddress = new ArrayList<>();
            for (FileInfo fileInfo : fileInfoList){
                picAddress.add(fileInfo.getPath());
            }

            //插入图片
            civilizationBehavior.setPicAddress(picAddress);

            baseVo.setData(civilizationBehavior);
        }else{
            baseVo.setCode(ResultCode.RECORD_ALREADY_DELETED.getCode());
            baseVo.setMsg(ResultCode.RECORD_ALREADY_DELETED.getInfo());
        }

        return baseVo;
    }

    /**
     * 刪除上報記錄
     * @author liyang
     * @date 2019-09-02
     * @param id : id
     * @return : BaseVo
     */
    @Override
    public BaseVo delete(Long id) {

        civilizationBehaviorDao.deleteByPrimaryKey(id);
        return successVo();
    }

    /**
     * 文明行为反馈消息推送
     * @author liyang
     * @date 2019-09-06
     * @param civilizationBehavior : 反馈详情
     * @return : null
    */
    public void sendMessage(CivilizationBehavior civilizationBehavior){

        //查询推送APP是物业还是居民
        if(civilizationBehavior.getDataType().equals(DATE_TYPE_PMC.getCode())){

            //获取参数
            String[] params = {REMIND_MESSAGE_CBO_CATEGORY.getInfo()};

            Long[] userIdArrays = {civilizationBehavior.getCreateUserId()};

            //发送消息
            MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(CBO_REMIND_CIVILIZED_BEHAVIOR_TO_PMC,
                    userIdArrays,
                    params,
                    null,
                    civilizationBehavior.getCreateTime(),
                    null);
            sendMqPushUtil.sendMqMessage(mqSendMessage);

        }else if(civilizationBehavior.getDataType().equals(DATE_TYPE_RESIDENT.getCode())){

            //获取参数
            String[] params = {REMIND_MESSAGE_CBO_CATEGORY.getInfo()};

            Long[] userIdArrays = {civilizationBehavior.getCreateUserId()};

            //发送消息
            MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(CBO_REMIND_CIVILIZED_BEHAVIOR_TO_RESIDENT,
                    userIdArrays,
                    params,
                    null,
                    civilizationBehavior.getCreateTime(),
                    null);
            sendMqPushUtil.sendMqMessage(mqSendMessage);
        }
    }

    /**
     * 推送文明行为待办
     * @author liyang
     * @date 2019-09-06
     * @param civilizationBehavior : 推送详情
    */
    public void sendTask(CivilizationBehavior civilizationBehavior){

        //获取参数
        String[] params = {TASK_MESSAGE_CBO_CATEGORY.getInfo(),civilizationBehavior.getCommunityName()};

        //获取服务站管理员
        List<User> userList = sysServiceFeign.getUserByCboDep(civilizationBehavior.getOrgId());
        List<Long> userIdList = new ArrayList<>();
        userList.forEach(user -> userIdList.add(user.getId()));
        Long[] userIdArrays = new Long[userIdList.size()];
        userIdList.toArray(userIdArrays);

        //发送消息
        MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserTaskByAlias(CBO_TASK_CIVILIZED_BEHAVIOR,
                userIdArrays,
                params,
                civilizationBehavior.getId(),
                civilizationBehavior.getVersion(),
                civilizationBehavior.getCreateUserName(),
                civilizationBehavior.getCreateTime(),
                null);
        sendMqPushUtil.sendMqMessage(mqSendMessage);
    }

}
