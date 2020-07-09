package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.bit.base.dto.OaOrganization;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.ApplyGuideEumn;
import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.config.ExtendTypeMapConfig;
import com.bit.module.cbo.dao.ResidentApplyBaseDao;
import com.bit.module.cbo.dao.ResidentApplyGuideDao;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.service.ResidentApplyBaseService;
import com.bit.module.cbo.vo.*;

import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.DateUtil;
import com.bit.utils.ExcelHandler;
import com.bit.utils.SpringUtil;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

import static com.bit.common.enumerate.ApplyGuideEumn.*;
import static com.bit.common.enumerate.ResidentApplyBaseEnum.*;
import static com.bit.soft.push.msEnum.MessageTemplateEnum.CBO_REMIND_RESIDENT_APPLY_PASS;
import static com.bit.soft.push.msEnum.MessageTemplateEnum.CBO_REMIND_RESIDENT_APPLY_TERMINALTION;

/**
 * @description: 台账管理相关实现类
 * @author: liyang
 * @date: 2019-08-09
 **/
@Service
public class ResidentApplyBaseServiceImpl extends BaseService implements ResidentApplyBaseService {

    private static final Logger logger = LoggerFactory.getLogger(ResidentApplyBaseServiceImpl.class);

    /**
     * 台账相关dao
     */
    @Autowired
    private ResidentApplyBaseDao residentApplyBaseDao;

    /**
     * 办事指南相关dao
     */
    @Autowired
    private ResidentApplyGuideDao residentApplyGuideDao;

    /**
     * sys服务相关查询
     */
    @Autowired
    private SysServiceFeign sysServiceFeign;

    @Autowired
	private FileServiceFeign fileServiceFeign;

    @Autowired
    private SendMqPushUtil sendMqPushUtil;

    @Autowired
    private ExtendTypeMapConfig type;


    /**
     * 新增台账申请
     * @author liyang
     * @date 2019-08-09
     * @param residentApplyBase : 新增明细
     * @return : BaseVo
     */
    @Override
    public BaseVo add(ResidentApplyBase residentApplyBase) {

        UserInfo userInfo = getCurrentUserInfo();

        //申请状态：1进行中 2,待完善，3已办结，4 已终止
        residentApplyBase.setApplyStatus(ApplyGuideEumn.APPLY_STATUS_USING.getCode());

        //社区ID
        residentApplyBase.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());

        //是否生成人员名单 1有，0无  新建时未生成
        residentApplyBase.setGenerateRoster(GENERATEROSTER_FALSE.getCode());

        //创建时间
        residentApplyBase.setCreateTime(new Date());

        //创建人
        residentApplyBase.setCreateUserId(userInfo.getId());

        //异常校验去重
        ResidentApplyBaseVO residentApplyBaseTemp = new ResidentApplyBaseVO();
        residentApplyBaseTemp.setResidentId(residentApplyBase.getResidentId());
        residentApplyBaseTemp.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        residentApplyBaseTemp.setCategoryId(residentApplyBase.getCategoryId());
        residentApplyBaseTemp.setServiceId(residentApplyBase.getServiceId());
        residentApplyBaseTemp.setApplyStatus(ApplyGuideEumn.APPLY_STATUS_USING.getCode());
        List<ResidentApplyBase> residentApplyBaseList = residentApplyBaseDao.findAllSql(residentApplyBaseTemp);
        if(residentApplyBaseList.size()>Const.COUNT){
            throwBusinessException("该居民已提交申请！");
        }

        residentApplyBaseDao.add(residentApplyBase);

        return successVo();
    }

    /**
     * 获取办事指南所有类别
     * @author liyang
     * @date 2019-08-12
     * @param status : 状态 0 停用 1启用 2 草稿
     * @return : BaseVo
     */
    @Override
    public BaseVo getGuide(Integer status) {

        List<ResidentApplyGuide> residentApplyGuideList = residentApplyBaseDao.getGuideSql(status);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(residentApplyGuideList);
        return baseVo;
    }

    /**
     * 根据办事指南类别获取所有启用的事项
     * @author liyang
     * @date 2019-08-12
     * @param type : 数据类别:1 类别，0事项
     * @param pid : 办事指南类别ID
     * @param status : 状态 0 停用 1启用 2 草稿
     * @return : BaseVo
     */
    @Override
    public BaseVo getGuideItems(Integer type, Long pid,Integer status) {

        List<ResidentApplyGuide> residentApplyGuideList = residentApplyBaseDao.getGuideItemsSql(type,pid,status);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(residentApplyGuideList);
        return baseVo;
    }

    /**
     * 获取办事指南台账列表
     * @author liyang
     * @date 2019-08-12
     * @param residentApplyBaseVO : 查询条件
     * @return : BaseVo
     */
    @Override
    public BaseVo findAll(ResidentApplyBaseVO residentApplyBaseVO) {
        UserInfo userInfo = getCurrentUserInfo();

        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //获取社区ID
            residentApplyBaseVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        //分页查询
        PageHelper.startPage(residentApplyBaseVO.getPageNum(),residentApplyBaseVO.getPageSize());
        List<ResidentApplyBase> residentApplyBaseList = residentApplyBaseDao.findAllSql(residentApplyBaseVO);

        if(residentApplyBaseList.size() == Const.COUNT){
            PageInfo<ResidentApplyBase> pageInfo = new PageInfo<>(residentApplyBaseList);
            BaseVo baseVo = new BaseVo();
            baseVo.setData(pageInfo);
            return baseVo;
        }

        Map<Long,OaOrganization> oaOrganizationMap = new HashMap<>();

        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){

            //如果是社区办
            List<Long> ids = new ArrayList<>();
            residentApplyBaseList.forEach(base->ids.add(base.getOrgId()));
            List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

            //转换Map
            oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

        }else{
            residentApplyBaseList.forEach(ra->ra.setOrgName(userInfo.getCboInfo().getCboOrgs().get(0).getName()));
        }

        //获取所有办事指南
        List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findAllGuide();

        //办事指南分类汇总
        Map<Long,ResidentApplyGuide> residentApplyGuideMap = residentApplyGuideList.stream().collect(Collectors.toMap(ResidentApplyGuide::getId, residentApplyGuide -> residentApplyGuide));

        //循环插入办事指南类型名称
        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            for(ResidentApplyBase residentApplyBase : residentApplyBaseList){
                residentApplyBase.setCategoryName(residentApplyGuideMap.get(residentApplyBase.getCategoryId()).getName());
                residentApplyBase.setServiceName(residentApplyGuideMap.get(residentApplyBase.getServiceId()).getName());
                String guideName = residentApplyGuideMap.get(residentApplyBase.getCategoryId()).getName() + "-" +  residentApplyGuideMap.get(residentApplyBase.getServiceId()).getName();
                residentApplyBase.setGuideName(guideName);
            }
        }else {

            //社区办需要获取社区名称
            for(ResidentApplyBase residentApplyBase : residentApplyBaseList){
                residentApplyBase.setCategoryName(residentApplyGuideMap.get(residentApplyBase.getCategoryId()).getName());
                residentApplyBase.setServiceName(residentApplyGuideMap.get(residentApplyBase.getServiceId()).getName());
                String guideName = residentApplyGuideMap.get(residentApplyBase.getCategoryId()).getName() + "-" +  residentApplyGuideMap.get(residentApplyBase.getServiceId()).getName();
                residentApplyBase.setGuideName(guideName);
                residentApplyBase.setOrgName(oaOrganizationMap.get(residentApplyBase.getOrgId()).getName());
            }
        }


        PageInfo<ResidentApplyBase> pageInfo = new PageInfo<>(residentApplyBaseList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }

    /**
     * 根据ID查询台账明细
     * @author liyang
     * @date 2019-08-12
     * @param id : 台账ID
     * @return : BaseVo
    */
    @Override
    public BaseVo detail(Long id) {

        //获取台账基本信息和办事人信息
        ResidentApplyBaseVO residentApplyBaseVO = residentApplyBaseDao.findDetailById(id);

        //获取所有办事指南
        List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findAllGuide();

        //办事指南分类汇总
        Map<Long,ResidentApplyGuide> residentApplyGuideMap = residentApplyGuideList.stream().collect(Collectors.toMap(ResidentApplyGuide::getId, residentApplyGuide -> residentApplyGuide));
        String serviceName = residentApplyGuideMap.get(residentApplyBaseVO.getServiceId()).getName();
        String categoryName =  residentApplyGuideMap.get(residentApplyBaseVO.getCategoryId()).getName();
        String guideName = residentApplyGuideMap.get(residentApplyBaseVO.getCategoryId()).getName() + "-" +  residentApplyGuideMap.get(residentApplyBaseVO.getServiceId()).getName();
        residentApplyBaseVO.setGuideName(guideName);
        residentApplyBaseVO.setCategoryName(categoryName);
        residentApplyBaseVO.setServiceName(serviceName);

        //获取办理进度
        ResidentApplyGuideItems residentApplyGuideItems = new ResidentApplyGuideItems();
        residentApplyGuideItems.setGuideId(residentApplyBaseVO.getServiceId());
        residentApplyGuideItems.setType(APPLY_ITEMS_TYPE_HANDLING.getCode());

        //查询办理进度明细
        List<ResidentApplyProgressVO> residentApplyProgressVOList = this.getItemHandleDetails(residentApplyGuideItems,id);
        residentApplyBaseVO.setResidentApplyProgressVOList(residentApplyProgressVOList);


        //todo 优化下面 判断是否存在补充业务信息 这段代码  先不提交
//        if(residentApplyBaseVO.getExtend().equals(APPLY_EXTEND_TRUE.getCode())){
//            Map<Integer,String> extendTypeMap = type.getType();
//            String extendTypeClass = extendTypeMap.get(residentApplyBaseVO.getExtendType());
//
//            AbstractExtendType abstractExtendType = (AbstractExtendType)SpringUtil.getBean(extendTypeClass);
//            residentApplyBaseVO.setExtendTypeBase(abstractExtendType.getExtendTypeBase(id));
//
//        }

        //判断是否存在补充业务信息
        if(residentApplyBaseVO.getExtend().equals(APPLY_EXTEND_TRUE.getCode())){

            //存在补充信息 判断是什么补充信息 扩展信息类型：1 低保申请、2 居家养老 3 残疾人 4 特殊扶助
            if(residentApplyBaseVO.getExtendType().equals(BASE_APPLY_LIVING_ALLOWANCES.getCode())){

                //如果是低保申请，获取低保申请详情
                ResidentApplyBasicLivingAllowances resReturn = this.getBasicLivingAllowancesByApplyId(id);
                residentApplyBaseVO.setResidentApplyBasicLivingAllowances(resReturn);
            }else if(residentApplyBaseVO.getExtendType().equals(BASE_RESIDENT_APPLY_HOME_CARE.getCode())){

                //如果是居家养老申请
                ResidentApplyHomeCare resCareReturn = this.getResidentApplyHomeCareByApplyId(id);
                residentApplyBaseVO.setResidentApplyHomeCare(resCareReturn);
            }else if (residentApplyBaseVO.getExtendType().equals(BASE_APPLY_DISABLE.getCode())){
            	//残疾人
				ResidentApplyDisabledIndividuals resDisableReturn = this.getResidentApplyDisabledIndividualsByApplyId(id);
				residentApplyBaseVO.setResidentApplyDisabledIndividuals(resDisableReturn);
			}else if (residentApplyBaseVO.getExtendType().equals(BASE_RESIDENT_SPECIAL_SUPPORT.getCode())){
            	//特殊扶助
				ResidentApplySpecialSupport resSupportReturn = this.getResidentApplySpecialSupportByApplyId(id);
				residentApplyBaseVO.setResidentApplySpecialSupport(resSupportReturn);
			}
        }
        if (StringUtil.isNotEmpty(residentApplyBaseVO.getAttachedIds())){
			//查询附件信息
			List<Long> fileIds = Arrays.asList(residentApplyBaseVO.getAttachedIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(fileIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
				String s = JSON.toJSONString(byIds.getData());
				List<FileInfo> fileInfos = JSONArray.parseArray(s,FileInfo.class);
				residentApplyBaseVO.setFileInfos(fileInfos);
			}
		}

        BaseVo baseVo = new BaseVo();
        baseVo.setData(residentApplyBaseVO);
        return baseVo;
    }

    /**
     * 台账办理步揍展示
     * @author liyang
     * @date 2019-08-13
     * @param applyId : 申请台账ID
     * @return : BaseVo
     */
    @Override
    public BaseVo itemsStep(Long applyId) {

        //先获取申请表所属事项
        ResidentApplyBaseVO residentApplyBaseVO = residentApplyBaseDao.findDetailById(applyId);

        //从事项中获取办理流程
        ResidentApplyGuideItems residentApplyGuideItems = new ResidentApplyGuideItems();
        residentApplyGuideItems.setType(APPLY_ITEMS_TYPE_HANDLING.getCode());
        residentApplyGuideItems.setGuideId(residentApplyBaseVO.getServiceId());
        List<ResidentApplyProgressVO> residentApplyProgressVOList = this.getItemHandleDetails(residentApplyGuideItems,applyId);

        residentApplyBaseVO.setResidentApplyProgressVOList(residentApplyProgressVOList);
		if (StringUtil.isNotEmpty(residentApplyBaseVO.getAttachedIds())){
			//查询附件
			List<Long> fileIds = Arrays.asList(residentApplyBaseVO.getAttachedIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(fileIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
                String s = JSON.toJSONString(byIds.getData());
                List<FileInfo> fileInfos = JSONArray.parseArray(s,FileInfo.class);
                residentApplyBaseVO.setFileInfos(fileInfos);
            }
		}

        BaseVo baseVo = new BaseVo();
        baseVo.setData(residentApplyBaseVO);

        return baseVo;
    }

    /**
     * 审核办理流程
     * @author liyang
     * @date 2019-08-13
     * @param residentApplyProgress : 修改的明细
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo auditItems(ResidentApplyProgress residentApplyProgress) {

        UserInfo userInfo = getCurrentUserInfo();

        //异常处理  判断是否有已经存在的该业务信息 如果存在直接返回
        Integer count = residentApplyBaseDao.getHandleProcess(residentApplyProgress.getGuideItemId(),residentApplyProgress.getApplyId());
        if(count > Const.COUNT){
            throwBusinessException("节点状态已反馈！");
        }

        //先按顺序获取整个流程
        ResidentApplyGuideItems residentApplyGuideItems = new ResidentApplyGuideItems();
        residentApplyGuideItems.setType(APPLY_ITEMS_TYPE_HANDLING.getCode());
        residentApplyGuideItems.setGuideId(residentApplyProgress.getGuideId());
        List<ResidentApplyProgressVO> residentApplyProgressVOList = residentApplyGuideDao.queryItemsByParmSql(residentApplyGuideItems);

        //修改该流程状态(通过或者拒绝)
        residentApplyProgress.setFeedbackTime(new Date());
        residentApplyProgress.setFeedbackUserId(userInfo.getId());
        residentApplyBaseDao.addHandleProcess(residentApplyProgress);

        //获取台账申请人
        ResidentApplyBaseVO base = residentApplyBaseDao.findDetailById(residentApplyProgress.getApplyId());

        //获取所有办事指南
        List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findAllGuide();

        //办事指南分类汇总
        Map<Long,ResidentApplyGuide> residentApplyGuideMap = residentApplyGuideList.stream().collect(Collectors.toMap(ResidentApplyGuide::getId, residentApplyGuide -> residentApplyGuide));

        //循环插入办事指南类型名称
        base.setCategoryName(residentApplyGuideMap.get(base.getCategoryId()).getName());
        base.setServiceName(residentApplyGuideMap.get(base.getServiceId()).getName());

        //判断是否是拒绝，如果是拒绝状态为终止  修改台账状态 直接返回
        if(residentApplyProgress.getStepStatus().equals(AUDIT_STATUS_FAIL.getCode())){

            //将台账状态变为已终止
            ResidentApplyBase residentApplyBase = new ResidentApplyBase();
            residentApplyBase.setApplyStatus(APPLY_STATUS_DISABLE.getCode());
            residentApplyBase.setId(residentApplyProgress.getApplyId());
            residentApplyBaseDao.modify(residentApplyBase);


            ResidentApplyProgressVO pro = new ResidentApplyProgressVO();
            pro.setRejectReason(residentApplyProgress.getRejectReason());
            List<ResidentApplyProgressVO> proList = new ArrayList<>();
            proList.add(pro);
            base.setResidentApplyProgressVOList(proList);

            //发送消息
            sendMessage(base,CBO_REMIND_RESIDENT_APPLY_TERMINALTION.getId());

            return successVo();
        }

        //获取该事项最后一个流程ID
        Long lastHandleId = residentApplyProgressVOList.get(residentApplyProgressVOList.size() - 1).getGuideItemId();

        //判断此流程是否为最后一个流程
        if(residentApplyProgress.getGuideItemId().equals(lastHandleId)){

            //如果是最后一个流程,再判断是否有业务信息,根据业务信息不同修改为不同的状态
            ResidentApplyGuideVO residentApplyGuideVO = residentApplyGuideDao.queryByIdSql(residentApplyProgress.getGuideId());
            if(residentApplyGuideVO.getExtend().equals(APPLY_EXTEND_TRUE.getCode())){
                ResidentApplyBase residentApplyBase = new ResidentApplyBase();

                //插入待完善状态
                residentApplyBase.setApplyStatus(APPLY_STATUS_UNCOMMPLETE.getCode());
                residentApplyBase.setId(residentApplyProgress.getApplyId());
                residentApplyBaseDao.modify(residentApplyBase);

            }else {

                //插入已办结状态
                ResidentApplyBase residentApplyBase = new ResidentApplyBase();
                residentApplyBase.setApplyStatus(APPLY_STATUS_COMPLETE.getCode());
                residentApplyBase.setId(residentApplyProgress.getApplyId());

                //判断是否有补充业务  如果没有补充业务 将转换服务名单字段也一并修改
                if(residentApplyProgress.getExtend().equals(APPLY_EXTEND_FALSE.getCode())){
                    residentApplyBase.setGenerateRoster(GENERATEROSTER_NONEED.getCode());
                }
                residentApplyBaseDao.modify(residentApplyBase);
            }

            //发送消息
            sendMessage(base,CBO_REMIND_RESIDENT_APPLY_PASS.getId());
        }

        return successVo();
    }

    /**
     * 发送办事指南消息
     * @author liyang
     * @date 2019-09-05
     * @param base : 申请详情
     * @return : null
    */
    private void sendMessage(ResidentApplyBaseVO base,int templateId) {

        Long[] userIdArrays = {base.getResidentId()};

        //拒绝
        if(templateId == CBO_REMIND_RESIDENT_APPLY_TERMINALTION.getId()){

            //获取参数 事项名称 拒绝原因
            logger.debug("params是 " + base.getServiceName());
            String[] params = {base.getServiceName(),base.getResidentApplyProgressVOList().get(Const.COUNT).getRejectReason()};

            //发送消息
            MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(CBO_REMIND_RESIDENT_APPLY_TERMINALTION,
                                                        userIdArrays,
                                                        params,
                                                        null,
                                                        base.getCreateTime(),
                                                        null);
            sendMqPushUtil.sendMqMessage(mqSendMessage);
        }else if(templateId == CBO_REMIND_RESIDENT_APPLY_PASS.getId()){
            //所有都通过
            //获取参数 事项名称
            logger.debug("params是 " + base.getServiceName());
            String[] params = {base.getServiceName()};

            //发送消息
            MqSendMessage mqSendMessage = AppPushMessageUtil.pushUserMessageByAlias(CBO_REMIND_RESIDENT_APPLY_PASS,
                                                      userIdArrays,
                                                      params,
                                                      null,
                                                      base.getCreateTime(),
                                                      null);
            sendMqPushUtil.sendMqMessage(mqSendMessage);
        }

    }

    /**
     * 台账补充业务信息
     * @author liyang
     * @date 2019-08-14
     * @param serviceInformationVO : 业务信息详情
     * @param extendType : 关联的扩展信息关联字典表中扩展信息类型：1 低保申请、2 居家养老
     * @return : BaseVo
     */
    @Override
    public BaseVo addServiceInformation(ServiceInformationVO serviceInformationVO,Integer extendType) {
        UserInfo userInfo = getCurrentUserInfo();

        //todo 优化下面多 if else的代码  先不提交
//        Map<Integer,String> extendTypeMap = type.getType();
//        String extendTypeClass = extendTypeMap.get(extendType);
//        AbstractExtendType abstractExtendType = (AbstractExtendType) SpringUtil.getBean(extendTypeClass);
//        try {
//            abstractExtendType.addExtendInfo(serviceInformationVO.getResidentApplyBasicLivingAllowances(),userInfo,serviceInformationVO.getApplyId());
//        }catch (Exception ex){
//            throwBusinessException(ex.getMessage());
//        }


        //判断是插入哪个业务信息表
        if(extendType.equals(RESIDENT_APPLY_BASE_LIVING_ALLOWANCES.getCode())){

            //异常判断，如果有人已经完善了该信息 直接返回
            Integer count = residentApplyBaseDao.getBasicLivingAllowancesCountByApplyId(serviceInformationVO.getApplyId());
            if(count > Const.COUNT){
                throwBusinessException("该业务信息已完善！");
            }

            //插入低保信息表
            ResidentApplyBasicLivingAllowances rabla = serviceInformationVO.getResidentApplyBasicLivingAllowances();
            Date now = new Date();
            rabla.setCreateTime(now);
            rabla.setCreateUserId(userInfo.getId());
            rabla.setUpdateTime(now);
            rabla.setUpdateUserId(userInfo.getId());
            rabla.setReleaseTime(now);
            residentApplyBaseDao.addBasicLivingAllowances(rabla);

        }else if(extendType.equals(RESIDENT_APPLY_HOME_CARE.getCode())){

            //异常判断，如果有人已经完善了该信息 直接返回
            Integer count = residentApplyBaseDao.getHomeCareCountByApplyId(serviceInformationVO.getApplyId());
            if(count > Const.COUNT){
                throwBusinessException("该业务信息已完善！");
            }

            //插入居民养老表
            ResidentApplyHomeCare residentApplyHomeCare = serviceInformationVO.getResidentApplyHomeCare();
            Date now = new Date();
            residentApplyHomeCare.setCreateTime(now);
            residentApplyHomeCare.setCreateUserId(userInfo.getId());
            residentApplyHomeCare.setUpdateTime(now);
            residentApplyHomeCare.setUpdateUserId(userInfo.getId());
            residentApplyBaseDao.addHomeCare(residentApplyHomeCare);

        }else if (extendType.equals(RESIDENT_APPLY_DISABLED_INDIVIDUALS.getCode())){
            //残疾人申请
            //异常判断，如果有人已经完善了该信息 直接返回
            Integer count = residentApplyBaseDao.getDisabledCountByApplyId(serviceInformationVO.getApplyId());
            if(count > Const.COUNT){
                throwBusinessException("该业务信息已完善！");
            }
            ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals = serviceInformationVO.getResidentApplyDisabledIndividuals();
            Date now = new Date();
            residentApplyDisabledIndividuals.setCreateTime(now);
            residentApplyDisabledIndividuals.setCreateUserId(userInfo.getId());
            residentApplyDisabledIndividuals.setUpdateTime(now);
            residentApplyDisabledIndividuals.setUpdateUserId(userInfo.getId());
            residentApplyBaseDao.addDisable(residentApplyDisabledIndividuals);

        }else if (extendType.equals(RESIDENT_APPLY_SPECIAL_SUPPORT.getCode())){
            //特殊扶助申请
            //异常判断，如果有人已经完善了该信息 直接返回
            Integer count = residentApplyBaseDao.getSpecialSupportCountByApplyId(serviceInformationVO.getApplyId());
            if(count > Const.COUNT){
                throwBusinessException("该业务信息已完善！");
            }
            ResidentApplySpecialSupport residentApplySpecialSupport = serviceInformationVO.getResidentApplySpecialSupport();
            Date now = new Date();
            residentApplySpecialSupport.setCreateTime(now);
            residentApplySpecialSupport.setCreateUserId(userInfo.getId());
            residentApplySpecialSupport.setUpdateTime(now);
            residentApplySpecialSupport.setUpdateUserId(userInfo.getId());
            residentApplyBaseDao.addSpecialSupport(residentApplySpecialSupport);
        }

        //插入成功后修改该台账状态
        ResidentApplyBase residentApplyBase = new ResidentApplyBase();
        residentApplyBase.setApplyStatus(APPLY_STATUS_COMPLETE.getCode());
        residentApplyBase.setId(serviceInformationVO.getApplyId());
        residentApplyBase.setAttachedIds(serviceInformationVO.getAttachedIds());
        residentApplyBaseDao.modify(residentApplyBase);

        return successVo();
    }

	@Override
	@Transactional
	public BaseVo updateApplyBaseInfo(ResidentApplyBaseVO residentApplyBaseVO) {
    	ResidentApplyBase residentApplyBase = new ResidentApplyBase();
		residentApplyBase.setId(residentApplyBaseVO.getId());
		residentApplyBase.setAttachedIds(residentApplyBaseVO.getAttachedIds());
		residentApplyBaseDao.modify(residentApplyBase);
		return successVo();
	}

	/**
	 * 返显台账的补充业务信息的附件
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo reflectBase(Long id) {
		BaseVo baseVo = new BaseVo();
		ResidentApplyBaseVO detailById = residentApplyBaseDao.findDetailById(id);
		String file = detailById.getAttachedIds();
		if (StringUtil.isNotEmpty(file)){
			//查询附件
			List<Long> fileIds = Arrays.asList(file.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(fileIds);
			BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
			if (byIds.getData()!=null){
				String s = JSON.toJSONString(byIds.getData());
				List<FileInfo> fileInfos = JSONArray.parseArray(s,FileInfo.class);
				baseVo.setData(fileInfos);
			}
		}
		return baseVo;
	}

    /**
     * 导出办事指南列表
     * @author liyang
     * @date 2019-12-09
     * @param residentApplyBase : 过滤条件
     */
    @Override
    public void exportToExcel(ResidentApplyBase residentApplyBase, HttpServletResponse response) {
        UserInfo userInfo = getCurrentUserInfo();

        String name = "办事台账";

        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //获取社区ID
            residentApplyBase.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        //分页查询
        List<ResidentApplyBaseExcelVO> residentApplyBaseList = residentApplyBaseDao.exportToExcel(residentApplyBase);

        if(residentApplyBaseList.size() == Const.COUNT){
            String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
            ExcelHandler.exportExcel(residentApplyBaseList,fileName,name,ResidentApplyBaseExcelVO.class,response);
        }

        Map<Long,OaOrganization> oaOrganizationMap = new HashMap<>();

        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){

            //如果是社区办
            List<Long> ids = new ArrayList<>();
            residentApplyBaseList.forEach(base->ids.add(base.getOrgId()));
            List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

            //转换Map
            oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

        }else {
            String orgName = userInfo.getCboInfo().getCboOrgs().get(0).getName();
            residentApplyBaseList.forEach(base->base.setOrgName(orgName));
        }

        //获取所有办事指南
        List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findAllGuide();

        //办事指南分类汇总
        Map<Long,ResidentApplyGuide> residentApplyGuideMap = residentApplyGuideList.stream().collect(Collectors.toMap(ResidentApplyGuide::getId, residentApplyGuide -> residentApplyGuide));

        //循环插入办事指南类型名称
        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            for(ResidentApplyBaseExcelVO residentApplyBaseTemp : residentApplyBaseList){
                residentApplyBaseTemp.setCategoryName(residentApplyGuideMap.get(residentApplyBaseTemp.getCategoryId()).getName());
                residentApplyBaseTemp.setServiceName(residentApplyGuideMap.get(residentApplyBaseTemp.getServiceId()).getName());
                String guideName = residentApplyGuideMap.get(residentApplyBaseTemp.getCategoryId()).getName() + "-" +  residentApplyGuideMap.get(residentApplyBaseTemp.getServiceId()).getName();
                residentApplyBaseTemp.setGuideName(guideName);
            }
        }else {

            //社区办需要获取社区名称
            for(ResidentApplyBaseExcelVO residentApplyBaseTemp : residentApplyBaseList){
                residentApplyBaseTemp.setCategoryName(residentApplyGuideMap.get(residentApplyBaseTemp.getCategoryId()).getName());
                residentApplyBaseTemp.setServiceName(residentApplyGuideMap.get(residentApplyBaseTemp.getServiceId()).getName());
                String guideName = residentApplyGuideMap.get(residentApplyBaseTemp.getCategoryId()).getName() + "-" +  residentApplyGuideMap.get(residentApplyBaseTemp.getServiceId()).getName();
                residentApplyBaseTemp.setGuideName(guideName);
                residentApplyBaseTemp.setOrgName(oaOrganizationMap.get(residentApplyBaseTemp.getOrgId()).getName());
            }
        }

        //导出excel
        String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        ExcelHandler.exportExcel(residentApplyBaseList,fileName,name,ResidentApplyBaseExcelVO.class,response);

    }

    /**
     * 根据dictCode和module 获取字典表相应字段名称
     * @author liyang
     * @date 2019-08-13
     * @param dictList : 字典表集合
     * @param dictCode : 模块值
     * @return : String
    */
    private String getDictNameByDictCodeAndModule(List<Dict> dictList,String dictCode){
        Map<String,Dict> dictMap = dictList.stream().collect(Collectors.toMap(Dict::getDictCode,dict -> dict));
        String dictName = dictMap.get(dictCode).getDictName();

        return dictName;
    }

    /**
     * 根据申请ID查询居家养老业务明细
     * @author liyang
     * @date 2019-08-13
     * @param id : 申请ID
     * @return : ResidentApplyHomeCare 明细
    */
    public ResidentApplyHomeCare getResidentApplyHomeCareByApplyId(Long id){
        ResidentApplyHomeCare resCare = new ResidentApplyHomeCare();
        resCare.setApplyId(id);
        ResidentApplyHomeCare resCareReturn = residentApplyGuideDao.queryResidentHomeCareByParmSql(resCare);

        if(resCareReturn != null){
            List<String> codes = new ArrayList<>();

            //居家养老户口类别
            codes.add(Const.RESIDENCE_TYPE);

            //待遇类别
            codes.add(Const.TREATMENT_TYPE);

            //评估等级
            codes.add(Const.BASE_LEVEL);
            Dict dict = new Dict();
            dict.setModules(codes);

            // 查询字典表
            Object object = sysServiceFeign.findByModules(dict).getData();
            Map<String, List<Dict>> map = new HashMap<String, List<Dict>>();
            String ss = JSON.toJSONString(object);
            Gson gson = new Gson();
            map = gson.fromJson(ss,map.getClass());

            //获取户口类别
            List<Dict> residenceTypeDictList = JSON.parseArray(JSON.toJSONString(map.get(Const.RESIDENCE_TYPE)),Dict.class);
            String residenceTypeName = this.getDictNameByDictCodeAndModule(residenceTypeDictList,resCareReturn.getResidenceType());
            resCareReturn.setResidenceTypeName(residenceTypeName);

            //获取待遇类别名称
            List<Dict> treatmentTypeDictList = JSON.parseArray(JSON.toJSONString(map.get(Const.TREATMENT_TYPE)),Dict.class);
            String treatmentTypeName = this.getDictNameByDictCodeAndModule(treatmentTypeDictList,resCareReturn.getTreatmentType());
            resCareReturn.setTreatmentTypeName(treatmentTypeName);

            //获取评估等级名称
            List<Dict> levelNameDictList = JSON.parseArray(JSON.toJSONString(map.get(Const.TREATMENT_TYPE)),Dict.class);
            String levelName = this.getDictNameByDictCodeAndModule(levelNameDictList,resCareReturn.getLevel());
            resCareReturn.setLevelName(levelName);
        }

        return resCareReturn;
    }

	/**
	 * 根据id查询残疾人业务明细
	 * @param id
	 * @return
	 */
    public ResidentApplyDisabledIndividuals getResidentApplyDisabledIndividualsByApplyId(Long id){
    	ResidentApplyDisabledIndividuals res = new ResidentApplyDisabledIndividuals();
    	res.setApplyId(id);
		ResidentApplyDisabledIndividuals resDisableReturn = residentApplyGuideDao.queryResidentDisableByParmSql(res);

		return resDisableReturn;
	}
    /**
     * 根据id查询特殊扶助业务明细
     * @param id
     * @return
     */
	public ResidentApplySpecialSupport getResidentApplySpecialSupportByApplyId(Long id){
        ResidentApplySpecialSupport res = new ResidentApplySpecialSupport();
        res.setApplyId(id);
        ResidentApplySpecialSupport resSpecial = residentApplyGuideDao.queryResidentApplySpecialSupportByParmSql(res);
        return resSpecial;
    }

    /**
     * 根据申请ID获取低保业务信息
     * @author liyang
     * @date 2019-08-13
     * @param id : 申请ID
     * @return : ResidentApplyBasicLivingAllowances ：低保业务信息明细
    */
    public ResidentApplyBasicLivingAllowances getBasicLivingAllowancesByApplyId(Long id){
        ResidentApplyBasicLivingAllowances res = new ResidentApplyBasicLivingAllowances();
        res.setApplyId(id);
        ResidentApplyBasicLivingAllowances resReturn = residentApplyGuideDao.queryAllowancesByParmSql(res);
        return resReturn;
    }

    /**
     * 查询台账所属流程明细
     * @author liyang
     * @date 2019-08-13
     * @param residentApplyGuideItems : 事件对应流程明细
     * @param applyId : 台账明细
     * @return : List<ResidentApplyProgressVO> : 台账所属流程明细
    */
    public List<ResidentApplyProgressVO> getItemHandleDetails(ResidentApplyGuideItems residentApplyGuideItems,Long applyId){

        //查询办事指南对应流程步揍
        List<ResidentApplyProgressVO> residentApplyProgressVOList = residentApplyGuideDao.queryItemsByParmSql(residentApplyGuideItems);

        //查看该申请所有完成流程项
        List<ResidentApplyProgress> residentApplyProgressList = residentApplyBaseDao.queryHandleItemsByApplyIdSql(applyId);

        //合并流程
        if(residentApplyProgressList.size()>Const.COUNT){
            Map<Long,ResidentApplyProgress> progressMap = residentApplyProgressList.stream().collect(Collectors.toMap(ResidentApplyProgress::getGuideItemId,residentApplyProgress -> residentApplyProgress));
            for (ResidentApplyProgressVO residentApplyProgressVO : residentApplyProgressVOList){
                ResidentApplyProgress residentApplyProgressTemp = progressMap.get(residentApplyProgressVO.getGuideItemId());

                //在审核表中能找到的插入相应状态
                if(residentApplyProgressTemp != null){

                    //后台审核的录入的时间
                    residentApplyProgressVO.setFeedbackTime(residentApplyProgressTemp.getFeedbackTime());

                    //反馈人的id
                    residentApplyProgressVO.setFeedbackUserId(residentApplyProgressTemp.getFeedbackUserId());

                    //每步装态：0未审核，1通过，2未通过
                    residentApplyProgressVO.setStepStatus(residentApplyProgressTemp.getStepStatus());

                    //拒绝原因
                    residentApplyProgressVO.setRejectReason(residentApplyProgressTemp.getRejectReason());
                }else {

                    //在审核表中找不到的插入未审核状态
                    residentApplyProgressVO.setStepStatus(Const.COUNT);
                }
            }

        }else {

            //插入未审核状态
            residentApplyProgressVOList.forEach(respro ->respro.setStepStatus(Const.COUNT));
        }

        return residentApplyProgressVOList;
    }
}
