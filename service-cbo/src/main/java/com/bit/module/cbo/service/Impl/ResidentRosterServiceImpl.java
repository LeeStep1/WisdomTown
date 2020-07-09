package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.OaOrganization;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.bean.ResidentApplyLog;
import com.bit.common.enumerate.ResidentApplyBaseEnum;
import com.bit.common.enumerate.ResidentApplyTypeEumn;
import com.bit.common.enumerate.ResidentRosterEnum;
import com.bit.module.cbo.bean.*;
import com.bit.module.cbo.dao.ResidentApplyBaseDao;
import com.bit.module.cbo.dao.ResidentApplyGuideDao;
import com.bit.module.cbo.dao.ResidentRosterDao;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.service.ResidentRosterService;
import com.bit.module.cbo.vo.*;
import com.bit.utils.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.ipc.netty.http.server.HttpServerResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.stream.Collectors;

import static com.bit.common.enumerate.ResidentApplyBaseEnum.*;
import static com.bit.common.enumerate.ResidentRosterEnum.DATA_TYPE_CONVERT;
import static com.bit.common.enumerate.ResidentRosterEnum.DATA_TYPE_INSERT;

/**
 * @description: 服务名单相关实现
 * @author: liyang
 * @date: 2019-08-14
 **/
@Service
public class ResidentRosterServiceImpl extends BaseService implements ResidentRosterService {

    /**
     * 服务名单相关dao
     */
    @Autowired
    private ResidentRosterDao residentRosterDao;

    /**
     * 补充业务相关dao
     */
    @Autowired
    private ResidentApplyBaseDao residentApplyBaseDao;

    /**
     * 办事指南相关dao
     */
    @Autowired
    private ResidentApplyGuideDao residentApplyGuideDao;

    @Autowired
	private ResidentApplyLogUtil residentApplyLogUtil;
    @Autowired
	private CommonUtil commonUtil;


    /**
     * 台账相关实现
     */
    @Autowired
    private ResidentApplyBaseServiceImpl residentApplyBaseService;


    /**
     * sys服务相关查询
     */
    @Autowired
    private SysServiceFeign sysServiceFeign;


    /**
     * 增加居民低保服务名单
     * @author liyang
     * @date 2019-08-14
     * @param dataType : 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     * @param residentBasicLivingRosterVO : 居民服务名单详情
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo addBasicLivingRoster(Integer dataType,ResidentBasicLivingRosterVO residentBasicLivingRosterVO) {
        UserInfo userInfo = getCurrentUserInfo();
        Date now = new Date();

        //社区ID
        residentBasicLivingRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());

        //数据创建时间
        residentBasicLivingRosterVO.setCreateTime(now);

        //创建人ID
        residentBasicLivingRosterVO.setCreateUserId(userInfo.getId());

        //更新时间
        residentBasicLivingRosterVO.setUpdateTime(now);

        //更新人id
        residentBasicLivingRosterVO.setUpdateUserId(userInfo.getId());

		//设置状态
		residentBasicLivingRosterVO.setStatus(Const.STATUS_ACTIVE);

        //判断  证件类型  证件号码  事项  有效期内 四项不能重复
        Integer count =residentRosterDao.findConvertLivingRoster(residentBasicLivingRosterVO);
        if(count > Const.COUNT){
            throwBusinessException("添加失败，服务名单中已存在此居民此类型服务信息！");
        }

        //如果是手动创建 需要再次插入相关补充信息表
        if(dataType.equals(DATA_TYPE_INSERT.getCode())){
            manuallyAddBasicLiving(residentBasicLivingRosterVO, userInfo, now);

        }else if(dataType.equals(DATA_TYPE_CONVERT.getCode())){
            LedgerCoverBasicLiving(residentBasicLivingRosterVO, userInfo, now);
        }

        return successVo();
    }

    /**
     * 台账转换低保信息
     * @author liyang
     * @date 2020-06-12
     * @param residentBasicLivingRosterVO : 台账信息
     * @param userInfo : 操作人详情
     * @param now : 操作时间
     * @return : null
    */
    private void LedgerCoverBasicLiving(ResidentBasicLivingRosterVO residentBasicLivingRosterVO, UserInfo userInfo, Date now) {
        //先根据applyId查询apply_base表
        ResidentApplyBaseVO detailById = residentApplyBaseDao.findDetailById(residentBasicLivingRosterVO.getApplyId());
        BeanUtils.copyProperties(detailById,residentBasicLivingRosterVO);
        //社区ID
        residentBasicLivingRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        //数据创建时间
        residentBasicLivingRosterVO.setCreateTime(now);
        //申请记录的日期
//			residentBasicLivingRosterVO.setApplyTime(now);
        //创建人ID
        residentBasicLivingRosterVO.setCreateUserId(userInfo.getId());
        //更新时间
        residentBasicLivingRosterVO.setUpdateTime(now);
        //更新人id
        residentBasicLivingRosterVO.setUpdateUserId(userInfo.getId());
        //设置状态
        residentBasicLivingRosterVO.setStatus(Const.STATUS_ACTIVE);
        residentBasicLivingRosterVO.setDataType(DATA_TYPE_CONVERT.getCode());

        //如果是转换的，先获取具体补充信息,然后插入服务表
        ResidentApplyBasicLivingAllowances resReturn = residentApplyBaseService.getBasicLivingAllowancesByApplyId(residentBasicLivingRosterVO.getApplyId());
        residentBasicLivingRosterVO.setResidentApplyBasicLivingAllowances(resReturn);
        residentRosterDao.addBasicLivingRoster(residentBasicLivingRosterVO);

        // 需要在业务信息中增加服务名单字段
        ResidentApplyBasicLivingAllowances rabla = new ResidentApplyBasicLivingAllowances();
        rabla.setUpdateTime(now);
        rabla.setUpdateUserId(userInfo.getId());
        rabla.setRosterId(residentBasicLivingRosterVO.getId());
        rabla.setApplyId(residentBasicLivingRosterVO.getApplyId());
        residentRosterDao.modifyBasicLiving(rabla);

        //将台账的转换字段修改
        ResidentApplyBase residentApplyBase = new ResidentApplyBase();
        residentApplyBase.setGenerateRoster(GENERATEROSTER_TRUE.getCode());
        residentApplyBase.setId(residentBasicLivingRosterVO.getApplyId());
        residentApplyBaseDao.modify(residentApplyBase);
    }

    /**
     * 手动创建低保服务名单
     * @author liyang
     * @date 2020-06-12
     * @param residentBasicLivingRosterVO : 添加详情
     * @param userInfo : 添加人详情
     * @param now : 添加日期
     * @return : null
    */
    private void manuallyAddBasicLiving(ResidentBasicLivingRosterVO residentBasicLivingRosterVO, UserInfo userInfo, Date now) {

        //先增加居民服务表
        residentRosterDao.addBasicLivingRoster(residentBasicLivingRosterVO);

        //再插入居民低保业务信息表
        ResidentApplyBasicLivingAllowances rabla = residentBasicLivingRosterVO.getResidentApplyBasicLivingAllowances();
        rabla.setRosterId(residentBasicLivingRosterVO.getId());
        rabla.setCreateTime(now);
        rabla.setCreateUserId(userInfo.getId());
        rabla.setUpdateTime(now);
        rabla.setUpdateUserId(userInfo.getId());
        residentApplyBaseDao.addBasicLivingAllowances(rabla);
    }

    /**
     * 增加居家养老服务名单
     * @author liyang
     * @date 2019-08-14
     * @param dataType : 数据来源类型：1 申请通过后手动生成的名单，0：管理员手动创建的服务名单
     * @param residentHomeCareRosterVO : 居民养老服务名单详情
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo addHomeCareRoster(Integer dataType,ResidentHomeCareRosterVO residentHomeCareRosterVO) {

        UserInfo userInfo = getCurrentUserInfo();
        Date now = new Date();

        //社区ID
        residentHomeCareRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());

        //数据创建时间
        residentHomeCareRosterVO.setCreateTime(now);

        //申请记录的日期
//        residentHomeCareRosterVO.setApplyTime(now);

        //创建人ID
        residentHomeCareRosterVO.setCreateUserId(userInfo.getId());

        //更新时间
        residentHomeCareRosterVO.setUpdateTime(now);

        //更新人id
        residentHomeCareRosterVO.setUpdateUserId(userInfo.getId());

        //设置状态
		residentHomeCareRosterVO.setStatus(Const.STATUS_ACTIVE);

        //判断是否已经存在  证件类型  证件号码  事项  三项不能重复
        Integer count = residentRosterDao.findConvertHomeCareRoster(residentHomeCareRosterVO);
        if(count>Const.COUNT){
            throwBusinessException("添加失败，服务名单中已存在此居民此类型服务信息！");
        }

        //如果是手动创建 需要再次插入相关补充信息表
        if(dataType.equals(DATA_TYPE_INSERT.getCode())){
            manualAddHomeCare(residentHomeCareRosterVO, userInfo, now);

        }else if(dataType.equals(DATA_TYPE_CONVERT.getCode())){
            ledgerConvertHomeCare(residentHomeCareRosterVO, userInfo, now);
        }

        return successVo();
    }

    /**
     * 台账居家养老转换
     * @author liyang
     * @date 2020-06-12
     * @param residentHomeCareRosterVO : 居家养老明细
     * @param userInfo : 操作人详情
     * @param now : 操作时间
    */
    private void ledgerConvertHomeCare(ResidentHomeCareRosterVO residentHomeCareRosterVO, UserInfo userInfo, Date now) {
        //先根据applyId查询apply_base表
        ResidentApplyBaseVO detailById = residentApplyBaseDao.findDetailById(residentHomeCareRosterVO.getApplyId());
        BeanUtils.copyProperties(detailById,residentHomeCareRosterVO);
        //社区ID
        residentHomeCareRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        //数据创建时间
        residentHomeCareRosterVO.setCreateTime(now);
        //申请记录的日期
//			residentHomeCareRosterVO.setApplyTime(now);
        //创建人ID
        residentHomeCareRosterVO.setCreateUserId(userInfo.getId());
        //更新时间
        residentHomeCareRosterVO.setUpdateTime(now);
        //更新人id
        residentHomeCareRosterVO.setUpdateUserId(userInfo.getId());
        //设置状态
        residentHomeCareRosterVO.setStatus(Const.STATUS_ACTIVE);
        residentHomeCareRosterVO.setDataType(DATA_TYPE_CONVERT.getCode());

        //如果是转换的，先获取具体补充信息,然后插入服务表
        ResidentApplyHomeCare resCare = residentApplyBaseService.getResidentApplyHomeCareByApplyId(residentHomeCareRosterVO.getApplyId());
        residentHomeCareRosterVO.setResidentApplyHomeCare(resCare);
        residentHomeCareRosterVO.setResidenceType(resCare.getResidenceType());
        residentHomeCareRosterVO.setTreatmentType(resCare.getTreatmentType());
        residentHomeCareRosterVO.setLevel(resCare.getLevel());
        residentRosterDao.addHomeCareRoster(residentHomeCareRosterVO);

        //需要在业务信息中增加服务名单字段
        ResidentApplyHomeCare residentApplyHomeCare = new ResidentApplyHomeCare();
        residentApplyHomeCare.setUpdateTime(now);
        residentApplyHomeCare.setUpdateUserId(userInfo.getId());
        residentApplyHomeCare.setRosterId(residentHomeCareRosterVO.getId());
        residentApplyHomeCare.setApplyId(residentHomeCareRosterVO.getApplyId());
        residentRosterDao.modifyHomeCare(residentApplyHomeCare);

        //将台账的转换字段修改
        ResidentApplyBase residentApplyBase = new ResidentApplyBase();
        residentApplyBase.setGenerateRoster(GENERATEROSTER_TRUE.getCode());
        residentApplyBase.setId(residentHomeCareRosterVO.getApplyId());
        residentApplyBaseDao.modify(residentApplyBase);
    }

    /**
     * 手动创建居家养老服务名单
     * @author liyang
     * @date 2020-06-12
     * @param residentHomeCareRosterVO : 居家养老明细
     * @param userInfo : 操作人
     * @param now : 操作时间
    */
    private void manualAddHomeCare(ResidentHomeCareRosterVO residentHomeCareRosterVO, UserInfo userInfo, Date now) {

        //先增加居民服务表
        residentRosterDao.addHomeCareRoster(residentHomeCareRosterVO);

        //插入居民养老表
        ResidentApplyHomeCare residentApplyHomeCare = residentHomeCareRosterVO.getResidentApplyHomeCare();
        residentApplyHomeCare.setRosterId(residentHomeCareRosterVO.getId());
        residentApplyHomeCare.setCreateTime(now);
        residentApplyHomeCare.setCreateUserId(userInfo.getId());
        residentApplyHomeCare.setUpdateTime(now);
        residentApplyHomeCare.setUpdateUserId(userInfo.getId());
        residentApplyBaseDao.addHomeCare(residentApplyHomeCare);
    }

    /**
     * 增加居民残疾人申请服务名单
     * @param dataType
     * @param residentDisableIndividualRosterVO
     * @return
     */
    @Override
	@Transactional
    public BaseVo addDisabledIndividuals(Integer dataType, ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO) {
    	Long userId = getCurrentUserInfo().getId();
    	Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
    	Date now = new Date();
		//社区ID
		residentDisableIndividualRosterVO.setOrgId(orgId);

		//数据创建时间
		residentDisableIndividualRosterVO.setCreateTime(now);

		//申请记录的日期
//		residentDisableIndividualRosterVO.setApplyTime(now);

		//创建人ID
		residentDisableIndividualRosterVO.setCreateUserId(userId);

		//更新时间
		residentDisableIndividualRosterVO.setUpdateTime(now);

		//更新人id
		residentDisableIndividualRosterVO.setUpdateUserId(userId);

		//设置状态
		residentDisableIndividualRosterVO.setStatus(Const.STATUS_ACTIVE);

        //判断是否已经存在  证件类型  证件号码  事项  三项不能重复
        Integer count = residentRosterDao.findConvertDisableRoster(residentDisableIndividualRosterVO);
        if(count>Const.COUNT){
            throwBusinessException("添加失败，服务名单中已存在此居民此类型服务信息！");
        }

		//如果是手动创建 需要再次插入相关补充信息表
		if(dataType.equals(DATA_TYPE_INSERT.getCode())){
            manualAddDisabledinidual(residentDisableIndividualRosterVO, userId, now);

        }else if(dataType.equals(DATA_TYPE_CONVERT.getCode())){
            ledgerConvertDisabledIndividual(residentDisableIndividualRosterVO, userId, orgId, now);
        }
        return successVo();
    }

    /**
     * 台账转换残疾人服务名单
     * @author liyang
     * @date 2020-06-12
     * @param residentDisableIndividualRosterVO : 台账详情
     * @param userId : 操作人ID
     * @param orgId : 社区ID
     * @param now : 操作时间
    */
    private void ledgerConvertDisabledIndividual(ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO, Long userId, Long orgId, Date now) {
        //先根据applyId查询apply_base表
        ResidentApplyBaseVO detailById = residentApplyBaseDao.findDetailById(residentDisableIndividualRosterVO.getApplyId());
        BeanUtils.copyProperties(detailById,residentDisableIndividualRosterVO);
        //社区ID
        residentDisableIndividualRosterVO.setOrgId(orgId);
        //数据创建时间
        residentDisableIndividualRosterVO.setCreateTime(now);
        //申请记录的日期
//			residentDisableIndividualRosterVO.setApplyTime(now);
        //创建人ID
        residentDisableIndividualRosterVO.setCreateUserId(userId);
        //更新时间
        residentDisableIndividualRosterVO.setUpdateTime(now);
        //更新人id
        residentDisableIndividualRosterVO.setUpdateUserId(userId);
        //设置状态
        residentDisableIndividualRosterVO.setStatus(Const.STATUS_ACTIVE);
        residentDisableIndividualRosterVO.setDataType(DATA_TYPE_CONVERT.getCode());

        //如果是转换的，先获取具体补充信息,然后插入服务表
        ResidentApplyDisabledIndividuals resReturn = residentApplyBaseService.getResidentApplyDisabledIndividualsByApplyId(residentDisableIndividualRosterVO.getId());
        residentDisableIndividualRosterVO.setResidentApplyDisabledIndividuals(resReturn);
        //将业务扩展信息的数据赋值给服务名单表里
        residentDisableIndividualRosterVO.setDisabilityCategory(resReturn.getDisabilityCategory());
        residentDisableIndividualRosterVO.setDisabilityLevel(resReturn.getDisabilityLevel());
        residentDisableIndividualRosterVO.setLivingAble(resReturn.getLivingAble());
        residentDisableIndividualRosterVO.setLivingIssuanceDate(resReturn.getLivingIssuanceDate());
        residentRosterDao.addDisableRoster(residentDisableIndividualRosterVO);

        //修改残疾人台账表的rosterId
        ResidentApplyDisabledIndividuals temp = new ResidentApplyDisabledIndividuals();
        temp.setUpdateTime(now);
        temp.setUpdateUserId(userId);
        temp.setRosterId(residentDisableIndividualRosterVO.getId());
        temp.setApplyId(residentDisableIndividualRosterVO.getApplyId());
        temp.setLivingIssuanceDate(residentDisableIndividualRosterVO.getLivingIssuanceDate());
        residentRosterDao.modifyDisable(temp);

        //将台账的转换字段修改
        ResidentApplyBase residentApplyBase = new ResidentApplyBase();
        residentApplyBase.setGenerateRoster(GENERATEROSTER_TRUE.getCode());
        residentApplyBase.setId(residentDisableIndividualRosterVO.getApplyId());
        residentApplyBaseDao.modify(residentApplyBase);
    }

    /**
     * 手动创建残疾人申请
     * @author liyang
     * @date 2020-06-12
     * @param residentDisableIndividualRosterVO : 残疾人申请服务名单
     * @param userId : 用户ID
     * @param now : 创建时间
    */
    private void manualAddDisabledinidual(ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO, Long userId, Date now) {
        ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals = residentDisableIndividualRosterVO.getResidentApplyDisabledIndividuals();

        residentDisableIndividualRosterVO.setDisabilityCategory(residentApplyDisabledIndividuals.getDisabilityCategory());
        residentDisableIndividualRosterVO.setDisabilityLevel(residentApplyDisabledIndividuals.getDisabilityLevel());
        residentDisableIndividualRosterVO.setLivingAble(residentApplyDisabledIndividuals.getLivingAble());
        //先增加居民服务表
        residentRosterDao.addDisableRoster(residentDisableIndividualRosterVO);

        //插入居民残疾扩展信息表

        residentApplyDisabledIndividuals.setRosterId(residentDisableIndividualRosterVO.getId());
        residentApplyDisabledIndividuals.setCreateTime(now);
        residentApplyDisabledIndividuals.setCreateUserId(userId);
        residentApplyDisabledIndividuals.setUpdateTime(now);
        residentApplyDisabledIndividuals.setUpdateUserId(userId);
        residentApplyBaseDao.addDisable(residentApplyDisabledIndividuals);
    }

    /**
	 * 增加居民特殊扶助申请服务名单
	 * @param dataType
	 * @param residentSpecialSupportRosterVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo addSpecialSupport(Integer dataType, ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO) {
		Long userId = getCurrentUserInfo().getId();
		Long orgId = getCurrentUserInfo().getCboInfo().getCurrentCboOrg().getId();
		Date now = new Date();
		//社区ID
		residentSpecialSupportRosterVO.setOrgId(orgId);

		//数据创建时间
		residentSpecialSupportRosterVO.setCreateTime(now);

		//申请记录的日期
//		residentSpecialSupportRosterVO.setApplyTime(now);

		//创建人ID
		residentSpecialSupportRosterVO.setCreateUserId(userId);

		//更新时间
		residentSpecialSupportRosterVO.setUpdateTime(now);

		//更新人id
		residentSpecialSupportRosterVO.setUpdateUserId(userId);

		//设置状态
		residentSpecialSupportRosterVO.setStatus(Const.STATUS_ACTIVE);

        //判断是否已经存在  证件类型  证件号码  事项  三项不能重复
        Integer count = residentRosterDao.findConvertSpecialSupportRoster(residentSpecialSupportRosterVO);
        if(count>Const.COUNT){
            throwBusinessException("添加失败，服务名单中已存在此居民此类型服务信息！");
        }

		//如果是手动创建 需要再次插入相关补充信息表
		if(dataType.equals(DATA_TYPE_INSERT.getCode())){
            manualAddSpecialSupport(residentSpecialSupportRosterVO, userId, now);
        }else if(dataType.equals(DATA_TYPE_CONVERT.getCode())){
            ledgerConvertSpecialSupport(residentSpecialSupportRosterVO, userId, orgId, now);
        }
		return successVo();
	}

	/**
	 * 台账转换特殊扶持服务名单
	 * @author liyang
	 * @date 2020-06-12
	 * @param residentSpecialSupportRosterVO : 特殊扶持详情明细
	 * @param userId : 操作人ID
     * @param orgId : 所属社区ID
     * @param now : 操作时间
	*/
    private void ledgerConvertSpecialSupport(ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO, Long userId, Long orgId, Date now) {
        //先根据applyId查询apply_base表
        ResidentApplyBaseVO detailById = residentApplyBaseDao.findDetailById(residentSpecialSupportRosterVO.getApplyId());
        BeanUtils.copyProperties(detailById,residentSpecialSupportRosterVO);
        //社区ID
        residentSpecialSupportRosterVO.setOrgId(orgId);
        //数据创建时间
        residentSpecialSupportRosterVO.setCreateTime(now);
        //申请记录的日期
//			residentSpecialSupportRosterVO.setApplyTime(now);
        //创建人ID
        residentSpecialSupportRosterVO.setCreateUserId(userId);
        //更新时间
        residentSpecialSupportRosterVO.setUpdateTime(now);
        //更新人id
        residentSpecialSupportRosterVO.setUpdateUserId(userId);
        //设置状态
        residentSpecialSupportRosterVO.setStatus(Const.STATUS_ACTIVE);
        residentSpecialSupportRosterVO.setDataType(DATA_TYPE_CONVERT.getCode());


        //如果是转换的，先获取具体补充信息,然后插入服务表
        ResidentApplySpecialSupport resReturn = residentApplyBaseService.getResidentApplySpecialSupportByApplyId(residentSpecialSupportRosterVO.getId());
        residentSpecialSupportRosterVO.setResidentApplySpecialSupport(resReturn);
        //将业务扩展信息的数据赋值给服务名单表里
        residentSpecialSupportRosterVO.setSupportType(resReturn.getSupportType());
        residentRosterDao.addSpecialSupportRoster(residentSpecialSupportRosterVO);

        //修改特殊扶助人台账表的rosterId
        ResidentApplySpecialSupport temp = new ResidentApplySpecialSupport();
        temp.setUpdateTime(now);
        temp.setUpdateUserId(userId);
        temp.setRosterId(residentSpecialSupportRosterVO.getId());
        temp.setApplyId(residentSpecialSupportRosterVO.getApplyId());
        residentRosterDao.modifySpecialSupport(temp);

        //将台账的转换字段修改
        ResidentApplyBase residentApplyBase = new ResidentApplyBase();
        residentApplyBase.setGenerateRoster(GENERATEROSTER_TRUE.getCode());
        residentApplyBase.setId(residentSpecialSupportRosterVO.getApplyId());
        residentApplyBaseDao.modify(residentApplyBase);
    }

    /**
     * 手动添加特殊扶持服务名单
     * @author liyang
     * @date 2020-06-12
     * @param residentSpecialSupportRosterVO : 特殊扶持详情
     * @param userId : 操作人ID
     * @param now : 操作时间
    */
    private void manualAddSpecialSupport(ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO, Long userId, Date now) {
        ResidentApplySpecialSupport residentApplySpecialSupport = residentSpecialSupportRosterVO.getResidentApplySpecialSupport();
        residentSpecialSupportRosterVO.setSupportType(residentApplySpecialSupport.getSupportType());
        //先增加居民服务表
        residentRosterDao.addSpecialSupportRoster(residentSpecialSupportRosterVO);

        //插入居民特殊扶助扩展信息表

        residentApplySpecialSupport.setRosterId(residentSpecialSupportRosterVO.getId());
        residentApplySpecialSupport.setCreateTime(now);
        residentApplySpecialSupport.setCreateUserId(userId);
        residentApplySpecialSupport.setUpdateTime(now);
        residentApplySpecialSupport.setUpdateUserId(userId);
        residentApplyBaseDao.addSpecialSupport(residentApplySpecialSupport);
    }

    /**
     * 根据事项查询服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param pageNum : 第几页
     * @param pageSize : 每页几条
     * @return : BaseVo
     */
    @Override
    public BaseVo findAllRoster(Long serviceId,Integer pageNum,Integer pageSize) {
        UserInfo userInfo = getCurrentUserInfo();

        //根据事项ID 获取事项所属业务信息
        ResidentApplyGuideVO residentApplyGuideVO = residentApplyGuideDao.queryByIdSql(serviceId);

        //根据业务信息查询不同的服务表  1 低保申请、2 居家养老
        if(residentApplyGuideVO.getExtendType().equals(GUIDE_EXTEND_TYPE_LIVING.getCode())){
            ResidentBasicLivingRosterVO residentBasicLivingRosterVO = new ResidentBasicLivingRosterVO();
            residentBasicLivingRosterVO.setPageNum(pageNum);
            residentBasicLivingRosterVO.setPageSize(pageSize);
            residentBasicLivingRosterVO.setServiceId(serviceId);

            if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
                //获取社区ID
                residentBasicLivingRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
            }

            PageHelper.startPage(residentBasicLivingRosterVO.getPageNum(),residentBasicLivingRosterVO.getPageSize());
            List<ResidentBasicLivingRosterVO> residentBasicLivingRosterVOList = residentRosterDao.findAllBasicLivingRoster(residentBasicLivingRosterVO);
            PageInfo<ResidentBasicLivingRosterVO> pageInfo = new PageInfo<>(residentBasicLivingRosterVOList);
            BaseVo baseVo = new BaseVo();
            baseVo.setData(pageInfo);
            return baseVo;
        }else if(residentApplyGuideVO.getExtendType().equals(GUIDE_EXTEND_TYPE_HOME_CARE.getCode())){

            ResidentHomeCareRosterVO residentHomeCareRosterVO = new ResidentHomeCareRosterVO();
            residentHomeCareRosterVO.setPageNum(pageNum);
            residentHomeCareRosterVO.setPageSize(pageSize);
            residentHomeCareRosterVO.setServiceId(serviceId);

            if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
                //获取社区ID
                residentHomeCareRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
            }

            PageHelper.startPage(residentHomeCareRosterVO.getPageNum(),residentHomeCareRosterVO.getPageSize());
            List<ResidentHomeCareRosterVO> residentHomeCareRosterVOList = residentRosterDao.findAllHomeCareRoster(residentHomeCareRosterVO);
            PageInfo<ResidentHomeCareRosterVO> pageInfo = new PageInfo<>(residentHomeCareRosterVOList);
            BaseVo baseVo = new BaseVo();
            baseVo.setData(pageInfo);
            return baseVo;

        }

        return successVo();
    }

    /**
     * 根据事项筛选查询服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param residentBasicLivingRosterVO : 查询明细
     * @return : BaseVo
     */
    @Override
    public BaseVo findAllLivingRoster(Long serviceId, ResidentBasicLivingRosterVO residentBasicLivingRosterVO) {

        UserInfo userInfo = getCurrentUserInfo();

        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //获取社区ID
            residentBasicLivingRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        residentBasicLivingRosterVO.setServiceId(serviceId);

        //分页查询
        PageHelper.startPage(residentBasicLivingRosterVO.getPageNum(),residentBasicLivingRosterVO.getPageSize());
        List<ResidentBasicLivingRosterVO> residentBasicLivingRosterVOList = residentRosterDao.findAllBasicLivingRoster(residentBasicLivingRosterVO);

        if(residentBasicLivingRosterVOList.size() == Const.COUNT){
            PageInfo<ResidentBasicLivingRosterVO> pageInfo = new PageInfo<>(residentBasicLivingRosterVOList);
            BaseVo baseVo = new BaseVo();
            baseVo.setData(pageInfo);
            return baseVo;
        }

        //如果是社区办 还要获取社区名称
        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //如果是社区办
            List<Long> ids = new ArrayList<>();
            residentBasicLivingRosterVOList.forEach(base->ids.add(base.getOrgId()));
            List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

            //转换Map
            Map<Long,OaOrganization>  oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

            for (ResidentBasicLivingRosterVO living : residentBasicLivingRosterVOList){
                living.setOrgName(oaOrganizationMap.get(living.getOrgId()).getName());
            }
        }else {
            //不是社区办取当前社区名称
            residentBasicLivingRosterVOList.forEach(rl->rl.setOrgName(userInfo.getCboInfo().getCboOrgs().get(0).getName()));
        }


        PageInfo<ResidentBasicLivingRosterVO> pageInfo = new PageInfo<>(residentBasicLivingRosterVOList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;

    }

    /**
     * 根据事项查询居家养老服务名单
     * @author liyang
     * @date 2019-08-15
     * @param serviceId : 事项ID
     * @param residentHomeCareRosterVO : 查询明细
     * @return : BaseVo
     */
    @Override
    public BaseVo findAllHomeCareRoster(Long serviceId, ResidentHomeCareRosterVO residentHomeCareRosterVO) {

        UserInfo userInfo = getCurrentUserInfo();

        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //获取社区ID
            residentHomeCareRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        residentHomeCareRosterVO.setServiceId(serviceId);

        //分页查询
        PageHelper.startPage(residentHomeCareRosterVO.getPageNum(),residentHomeCareRosterVO.getPageSize());
        List<ResidentHomeCareRosterVO> residentHomeCareRosterVOList = residentRosterDao.findAllHomeCareRoster(residentHomeCareRosterVO);

        if(residentHomeCareRosterVOList.size() == Const.COUNT){
            PageInfo<ResidentHomeCareRosterVO> pageInfo = new PageInfo<>(residentHomeCareRosterVOList);
            BaseVo baseVo = new BaseVo();
            baseVo.setData(pageInfo);
            return baseVo;
        }

        //如果是社区办 还要获取社区名称
        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //如果是社区办
            List<Long> ids = new ArrayList<>();
            residentHomeCareRosterVOList.forEach(base->ids.add(base.getOrgId()));
            List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

            //转换Map
            Map<Long,OaOrganization>  oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

            for (ResidentHomeCareRosterVO homeCare : residentHomeCareRosterVOList){
                homeCare.setOrgName(oaOrganizationMap.get(homeCare.getOrgId()).getName());
            }
        }else {
            //不是社区办取当前社区名称
            residentHomeCareRosterVOList.forEach(rl->rl.setOrgName(userInfo.getCboInfo().getCboOrgs().get(0).getName()));
        }

        PageInfo<ResidentHomeCareRosterVO> pageInfo = new PageInfo<>(residentHomeCareRosterVOList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

	/**
	 * 根据事项查询残疾人服务名单
	 * @param serviceId
	 * @param residentDisableIndividualRosterPageVO
	 * @return
	 */
	@Override
	public BaseVo findAllDisableRoster(Long serviceId, ResidentDisableIndividualRosterPageVO residentDisableIndividualRosterPageVO) {
		UserInfo userInfo = getCurrentUserInfo();

		if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			//获取社区ID
			residentDisableIndividualRosterPageVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
		}

		residentDisableIndividualRosterPageVO.setServiceId(serviceId);
		//分页查询
		PageHelper.startPage(residentDisableIndividualRosterPageVO.getPageNum(),residentDisableIndividualRosterPageVO.getPageSize());
		List<ResidentDisableIndividualRosterVO> allDisableRoster = residentRosterDao.findAllDisableRoster(residentDisableIndividualRosterPageVO);

		if(allDisableRoster.size() == Const.COUNT){
			PageInfo<ResidentDisableIndividualRosterVO> pageInfo = new PageInfo<>(allDisableRoster);
			BaseVo baseVo = new BaseVo();
			baseVo.setData(pageInfo);
			return baseVo;
		}

		//如果是社区办 还要获取社区名称
		if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			//如果是社区办
			List<Long> ids = new ArrayList<>();
			allDisableRoster.forEach(base->ids.add(base.getOrgId()));
			List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

			//转换Map
			Map<Long,OaOrganization>  oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

			for (ResidentDisableIndividualRosterVO homeCare : allDisableRoster){
				homeCare.setOrgName(oaOrganizationMap.get(homeCare.getOrgId()).getName());
			}
		}else {
            //不是社区办取当前社区名称
            allDisableRoster.forEach(rl->rl.setOrgName(userInfo.getCboInfo().getCboOrgs().get(0).getName()));
        }

		PageInfo<ResidentDisableIndividualRosterVO> pageInfo = new PageInfo<>(allDisableRoster);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 根据事项查询特殊扶助服务名单
	 * @param serviceId
	 * @param residentSpecialSupportRosterPageVO
	 * @return
	 */
	@Override
	public BaseVo findAllSpecialSupport(Long serviceId, ResidentSpecialSupportRosterPageVO residentSpecialSupportRosterPageVO) {
		UserInfo userInfo = getCurrentUserInfo();

		if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			//获取社区ID
			residentSpecialSupportRosterPageVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
		}

		residentSpecialSupportRosterPageVO.setServiceId(serviceId);
		//分页查询
		PageHelper.startPage(residentSpecialSupportRosterPageVO.getPageNum(),residentSpecialSupportRosterPageVO.getPageSize());
		List<ResidentSpecialSupportRosterVO> allSpecialRoster = residentRosterDao.findAllSpecialRoster(residentSpecialSupportRosterPageVO);

		if(allSpecialRoster.size() == Const.COUNT){
			PageInfo<ResidentSpecialSupportRosterVO> pageInfo = new PageInfo<>(allSpecialRoster);
			BaseVo baseVo = new BaseVo();
			baseVo.setData(pageInfo);
			return baseVo;
		}

		//如果是社区办 还要获取社区名称
		if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			//如果是社区办
			List<Long> ids = new ArrayList<>();
			allSpecialRoster.forEach(base->ids.add(base.getOrgId()));
			List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

			//转换Map
			Map<Long,OaOrganization>  oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

			for (ResidentSpecialSupportRosterVO homeCare : allSpecialRoster){
				homeCare.setOrgName(oaOrganizationMap.get(homeCare.getOrgId()).getName());
			}
		}else {
            //不是社区办取当前社区名称
            allSpecialRoster.forEach(rl->rl.setOrgName(userInfo.getCboInfo().getCboOrgs().get(0).getName()));
        }


		PageInfo<ResidentSpecialSupportRosterVO> pageInfo = new PageInfo<ResidentSpecialSupportRosterVO>(allSpecialRoster);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
     * 修改低保信息服务名单
     * @author liyang
     * @date 2019-08-16
     * @param residentBasicLivingRosterVO : 修改详情
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo modifyLivingRoster(ResidentBasicLivingRosterVO residentBasicLivingRosterVO) {
    	//先要查询原来的业务扩展信息 用于存放到mongo中
		ResidentApplyBasicLivingAllowances old = residentApplyBaseDao.queryResidentApplyBasicLivingAllowancesByRosterId(residentBasicLivingRosterVO.getId());

		UserInfo userInfo = getCurrentUserInfo();

        residentBasicLivingRosterVO.setUpdateUserId(userInfo.getId());

        residentBasicLivingRosterVO.setUpdateTime(new Date());

        //先修改服务名单表
        residentRosterDao.modifyLivingRoster(residentBasicLivingRosterVO);

        //在修改补充信息表
        ResidentApplyBasicLivingAllowances residentApplyBasicLivingAllowances = residentBasicLivingRosterVO.getResidentApplyBasicLivingAllowances();

        residentRosterDao.modifyBasicLiving(residentApplyBasicLivingAllowances);

		//组装扩展信息
		BasicLivingAllowanceChangeLog livingLog = new BasicLivingAllowanceChangeLog();

		BeanUtils.copyProperties(old,livingLog);

		if (!residentBasicLivingRosterVO.getResidentApplyBasicLivingAllowances().equals(old)){
			//添加修改的内容到mongo里
			ResidentApplyLog<BasicLivingAllowanceChangeLog> log = new ResidentApplyLog<BasicLivingAllowanceChangeLog>(new Date(),userInfo.getId(),userInfo.getRealName(),residentBasicLivingRosterVO.getId(),livingLog);
			residentApplyLogUtil.addApplyLog(ResidentApplyTypeEumn.LIVING_ALLOWANCES,log);
		}

		return successVo();
    }

    /**
     * 修改居家养老服务名单
     * @author liyang
     * @date 2019-08-16
     * @param residentHomeCareRosterVO : 修改详情
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo modifyHomeCareRoster(ResidentHomeCareRosterVO residentHomeCareRosterVO) {
		//先要查询原来的业务扩展信息 用于存放到mongo中
		ResidentApplyHomeCare old = residentApplyBaseDao.queryResidentApplyHomeCareByRosterId(residentHomeCareRosterVO.getId());

		UserInfo userInfo = getCurrentUserInfo();

        residentHomeCareRosterVO.setUpdateUserId(userInfo.getId());

        residentHomeCareRosterVO.setUpdateTime(new Date());

        //先修改服务名单表
        residentRosterDao.modifyHomeCareRoster(residentHomeCareRosterVO);

        //在修改补充信息表
        ResidentApplyHomeCare residentApplyHomeCare = residentHomeCareRosterVO.getResidentApplyHomeCare();

        residentRosterDao.modifyHomeCare(residentApplyHomeCare);

		//组装扩展信息
		HomeCareChangeLog homeLog = new HomeCareChangeLog();
		BeanUtils.copyProperties(old,homeLog);

		//查询字典表
		Dict dict = new Dict();
		List<String> modules = new ArrayList<>();
		modules.add(Const.RESIDENCE_TYPE);
		modules.add(Const.TREATMENT_TYPE);
		modules.add(Const.BASE_LEVEL);
		modules.add(Const.LIVING_ABILITY);
		dict.setModules(modules);
		Object object = sysServiceFeign.findByModules(dict).getData();
		Map<String, List<Dict>> map = new HashMap<String, List<Dict>>();
		String ss = JSON.toJSONString(object);
		Gson gson = new Gson();
		map = gson.fromJson(ss,map.getClass());

		List<Dict> residenceTypeList = JSON.parseArray(JSON.toJSONString(map.get(Const.RESIDENCE_TYPE)),Dict.class);
		List<Dict> treatTypeList = JSON.parseArray(JSON.toJSONString(map.get(Const.TREATMENT_TYPE)),Dict.class);
		List<Dict> levelList = JSON.parseArray(JSON.toJSONString(map.get(Const.BASE_LEVEL)),Dict.class);
		List<Dict> livingAbilityList = JSON.parseArray(JSON.toJSONString(map.get(Const.LIVING_ABILITY)),Dict.class);

		homeLog.setResidenceType(commonUtil.getDictNameByDictCodeAndModule(residenceTypeList,homeLog.getResidenceType()));
		homeLog.setTreatmentType(commonUtil.getDictNameByDictCodeAndModule(treatTypeList,homeLog.getTreatmentType()));
		homeLog.setLevel(commonUtil.getDictNameByDictCodeAndModule(levelList,homeLog.getLevel()));
		homeLog.setLivingAbility(commonUtil.getDictNameByDictCodeAndModule(livingAbilityList,homeLog.getLivingAbility()));

		if (!residentHomeCareRosterVO.getResidentApplyHomeCare().equals(old)){
			//添加修改的内容到mongo里
			ResidentApplyLog<HomeCareChangeLog> log = new ResidentApplyLog<HomeCareChangeLog>(new Date(),userInfo.getId(),userInfo.getRealName(),residentHomeCareRosterVO.getId(),homeLog);

			residentApplyLogUtil.addApplyLog(ResidentApplyTypeEumn.HOME_CARE,log);
		}

        return successVo();
    }
	/**
	 * 修改残疾人服务名单
	 * @param residentDisableIndividualRosterVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo modifyDisableRoster(ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO) {
		//先要查询原来的业务扩展信息 用于存放到mongo中
		ResidentApplyDisabledIndividuals old = residentApplyBaseDao.queryResidentApplyDisabledIndividualsByRosterId(residentDisableIndividualRosterVO.getId());
		UserInfo userInfo = getCurrentUserInfo();
		residentDisableIndividualRosterVO.setUpdateUserId(userInfo.getId());
		residentDisableIndividualRosterVO.setUpdateTime(new Date());
		//修改服务名单的冗余字段
		residentDisableIndividualRosterVO.setDisabilityLevel(residentDisableIndividualRosterVO.getResidentApplyDisabledIndividuals().getDisabilityLevel());
		residentDisableIndividualRosterVO.setDisabilityCategory(residentDisableIndividualRosterVO.getResidentApplyDisabledIndividuals().getDisabilityCategory());
		residentDisableIndividualRosterVO.setLivingAble(residentDisableIndividualRosterVO.getResidentApplyDisabledIndividuals().getLivingAble());
		//先修改服务名单表
		residentRosterDao.modifyDisableRoster(residentDisableIndividualRosterVO);

		//在修改补充信息表
		ResidentApplyDisabledIndividuals residentApplyDisabledIndividuals = residentDisableIndividualRosterVO.getResidentApplyDisabledIndividuals();

		residentRosterDao.modifyDisable(residentApplyDisabledIndividuals);

		//组装扩展信息
		DisableChangeLog disablelog = new DisableChangeLog();
		BeanUtils.copyProperties(old,disablelog);
		//查询字典表
		Dict dict = new Dict();
		List<String> modules = new ArrayList<>();
		modules.add(Const.DISABLE_CATEGORY);
		modules.add(Const.DISABLE_LEVEL);
		dict.setModules(modules);
		Object object = sysServiceFeign.findByModules(dict).getData();
		Map<String, List<Dict>> map = new HashMap<String, List<Dict>>();
		String ss = JSON.toJSONString(object);
		Gson gson = new Gson();
		map = gson.fromJson(ss,map.getClass());

		List<Dict> disableCategoryList = JSON.parseArray(JSON.toJSONString(map.get(Const.DISABLE_CATEGORY)),Dict.class);
		List<Dict> disableLevelList = JSON.parseArray(JSON.toJSONString(map.get(Const.DISABLE_LEVEL)),Dict.class);

		//转换Map
		if (StringUtil.isNotEmpty(disablelog.getDisabilityCategory())){
			disablelog.setDisabilityCategory(commonUtil.getDictNameByDictCodeAndModule(disableCategoryList,disablelog.getDisabilityCategory()));
		}
		if (StringUtil.isNotEmpty(disablelog.getDisabilityLevel())){
			disablelog.setDisabilityLevel(commonUtil.getDictNameByDictCodeAndModule(disableLevelList,disablelog.getDisabilityLevel()));
		}

		if (!residentDisableIndividualRosterVO.getResidentApplyDisabledIndividuals().equals(old)){
			//添加修改的内容到mongo里
			ResidentApplyLog<DisableChangeLog> log = new ResidentApplyLog<DisableChangeLog>(new Date(),userInfo.getId(),userInfo.getRealName(),residentDisableIndividualRosterVO.getId(),disablelog);

			residentApplyLogUtil.addApplyLog(ResidentApplyTypeEumn.DISABLED_INDIVIDUALS,log);
		}

		return successVo();
	}
	/**
	 * 修改特殊扶助服务名单
	 * @param residentSpecialSupportRosterVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo modifySpecialSupportRoster(ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO) {
		//先要查询原来的业务扩展信息 用于存放到mongo中
		ResidentApplySpecialSupport old = residentApplyBaseDao.queryResidentApplySpecialSupportByRosterId(residentSpecialSupportRosterVO.getId());
		UserInfo userInfo = getCurrentUserInfo();
		residentSpecialSupportRosterVO.setUpdateUserId(userInfo.getId());
		residentSpecialSupportRosterVO.setUpdateTime(new Date());
		//修改服务名单的冗余字段
		residentSpecialSupportRosterVO.setSupportType(residentSpecialSupportRosterVO.getResidentApplySpecialSupport().getSupportType());
		//先修改服务名单表
		residentRosterDao.modifySpecialSupportRoster(residentSpecialSupportRosterVO);
		//在修改补充信息表
		ResidentApplySpecialSupport residentApplySpecialSupport = residentSpecialSupportRosterVO.getResidentApplySpecialSupport();

		residentRosterDao.modifySpecialSupport(residentApplySpecialSupport);

		//组装residentSpecialSupportRosterVO 的中文值
		List<Long> orgIds = new ArrayList<>();
		orgIds.add(residentSpecialSupportRosterVO.getOrgId());
		List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(orgIds);

		residentSpecialSupportRosterVO.setOrgName(commonUtil.getOrgName(residentSpecialSupportRosterVO.getOrgId(),oaOrganizationList));
		//查询字典表
		Dict dict = new Dict();
		List<String> modules = new ArrayList<>();
		modules.add(Const.SPECIAL_SUPPORT);
		dict.setModules(modules);
		Object object = sysServiceFeign.findByModules(dict).getData();
		Map<String, List<Dict>> map = new HashMap<String, List<Dict>>();
		String ss = JSON.toJSONString(object);
		Gson gson = new Gson();
		map = gson.fromJson(ss,map.getClass());
		List<Dict> specialSupportTypes = JSON.parseArray(JSON.toJSONString(map.get(Const.SPECIAL_SUPPORT)),Dict.class);

		residentSpecialSupportRosterVO.setSupportType(commonUtil.getDictNameByDictCodeAndModule(specialSupportTypes,residentSpecialSupportRosterVO.getSupportType()));

		//组装扩展信息
		SpecialSupportChangeLog supportlog = new SpecialSupportChangeLog();
		BeanUtils.copyProperties(old,supportlog);
		supportlog.setSupportType(commonUtil.getDictNameByDictCodeAndModule(specialSupportTypes,supportlog.getSupportType()));
		if (!residentSpecialSupportRosterVO.getResidentApplySpecialSupport().equals(old)){
			//添加修改的内容到mongo里
			ResidentApplyLog<SpecialSupportChangeLog> log = new ResidentApplyLog<SpecialSupportChangeLog>(new Date(),userInfo.getId(),userInfo.getRealName(),residentSpecialSupportRosterVO.getId(),supportlog);

			residentApplyLogUtil.addApplyLog(ResidentApplyTypeEumn.SPECIAL_SUPPORT,log);
		}

		return successVo();
	}

	/**
     * 根据ID删除低保信息服务名单
     * @author liyang
     * @date 2019-08-16
     * @param id : 删除的ID
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo deleteLivingRoster(Long id) {

        //先检查是否已经并发删除
		ResidentBasicLivingRoster livingRosterById = residentRosterDao.findLivingRosterById(id);
        if(livingRosterById!=null){
			if (livingRosterById.getDataType().equals(ResidentRosterEnum.DATA_TYPE_INSERT.getCode())){
				//先删除低保服务名单
				residentRosterDao.deleteLivingRosterById(id);

				//再删除低保明细
				residentRosterDao.deleteLivingByRosterId(id);
			}else {
				throwBusinessException("只有手动创建的服务名单可以删除！");
			}
        }else {
            throwBusinessException("此条服务名单已删除！");
        }

        return successVo();
    }


    /**
     * 根据ID删除居家养老服务名单
     * @author liyang
     * @date 2019-08-16
     * @param id : 删除的ID
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo deleteHomeCareRoster(Long id) {

        //先检查是否已经并发删除
		ResidentHomeCareRoster homeCareRosterById = residentRosterDao.findHomeCareRosterById(id);

        if(homeCareRosterById!=null) {
			if (homeCareRosterById.getDataType().equals(ResidentRosterEnum.DATA_TYPE_INSERT.getCode())){
				//先删除低保服务名单
				residentRosterDao.deleteHomeCareRosterById(id);

				//再删除低保明细
				residentRosterDao.deleteDisableById(id);
			}else {
				throwBusinessException("只有手动创建的服务名单可以删除！");
			}

        }else {
            throwBusinessException("此条服务名单已删除！");
        }

        return successVo();
    }

	/**
	 * 根据id删除残疾人服务名单
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo deleteDisableRoster(Long id) {
		//先检查是否已经并发删除
		ResidentDisableIndividualRoster disableRosterById = residentRosterDao.findDisableRosterById(id);
		if(disableRosterById !=null) {
			if (disableRosterById.getDataType().equals(ResidentRosterEnum.DATA_TYPE_INSERT.getCode())){
				//先删除残疾人服务名单
				residentRosterDao.deleteDisableByRosterId(id);

				//再删除残疾人明细
				residentRosterDao.deleteDisableByRosterId(id);
			}else {
				throwBusinessException("只有手动创建的服务名单可以删除！");
			}

		}else {
			throwBusinessException("此条服务名单已删除！");
		}

		return successVo();
	}

	/**
	 * 根据id删除特殊扶助服务名单
	 * @param id
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo deleteSpecialSupportRoster(Long id) {
		//先检查是否已经并发删除
		ResidentSpecialSupportRoster specialSupportById = residentRosterDao.findSpecialSupportById(id);
		if(specialSupportById!=null) {
			if (specialSupportById.getDataType().equals(ResidentRosterEnum.DATA_TYPE_INSERT.getCode())){
				//先删除残疾人服务名单
				residentRosterDao.deleteSpecialSupportByRosterId(id);

				//再删除残疾人明细
				residentRosterDao.deleteSpecialSupportById(id);
			}else {
				throwBusinessException("只有手动创建的服务名单可以删除！");
			}

		}else {
			throwBusinessException("此条服务名单已删除！");
		}

		return successVo();
	}


	/**
     * 获取低保服务名单详情
     * @author liyang
     * @date 2019-08-21
     * @param id ： 获取详情的ID
     * @return : BaseVo
     */
    @Override
    public BaseVo livingAllowanceDetail(Long id) {

        ResidentBasicLivingRosterVO livingRosterVO = new ResidentBasicLivingRosterVO();

        //查询业务名单详情
        livingRosterVO = residentRosterDao.findLivingAllowanceRosterDetailById(id);

        List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findAllGuide();

        //办事指南分类汇总
        Map<Long,ResidentApplyGuide> residentApplyGuideMap = residentApplyGuideList.stream().collect(Collectors.toMap(ResidentApplyGuide::getId, residentApplyGuide -> residentApplyGuide));

        //获取办事指南类别名称
        livingRosterVO.setCategoryName(residentApplyGuideMap.get(livingRosterVO.getCategoryId()).getName());

        //获取办事指南事项名称
        livingRosterVO.setServiceName(residentApplyGuideMap.get(livingRosterVO.getServiceId()).getName());

        //查询补充业务信息详情
        ResidentApplyBasicLivingAllowances basicLiving = residentRosterDao.findLivingAllowanceDetailByRosterId(id);

        livingRosterVO.setResidentApplyBasicLivingAllowances(basicLiving);

		//查询修改记录
		List<ResidentApplyLog> logs = residentApplyLogUtil.getLogs(ResidentApplyTypeEumn.LIVING_ALLOWANCES, id,new ResidentApplyLog<ResidentApplyBasicLivingAllowances>().getClass());

		livingRosterVO.setLogs(logs);

		//查询附件
		String fileId = livingRosterVO.getAttachedIds();
		livingRosterVO.setFileInfos(commonUtil.getFileInfos(fileId));

        BaseVo baseVo = new BaseVo();
        baseVo.setData(livingRosterVO);

        return baseVo;
    }


	/**
     * 获取居家养老服务名单详情
     * @author liyang
     * @date 2019-08-21
     * @param id ： 获取详情的ID
     * @return : BaseVo
     */
    @Override
    public BaseVo homeCareDetail(Long id) {

        ResidentHomeCareRosterVO homeCareRosterVO = new ResidentHomeCareRosterVO();

        //查询业务名单详情
        homeCareRosterVO = residentRosterDao.findHomeCareRosterDetailById(id);

        //获取所有办事指南
        List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findAllGuide();

        //办事指南分类汇总
        Map<Long,ResidentApplyGuide> residentApplyGuideMap = residentApplyGuideList.stream().collect(Collectors.toMap(ResidentApplyGuide::getId, residentApplyGuide -> residentApplyGuide));

        //获取办事指南类别名称
        homeCareRosterVO.setCategoryName(residentApplyGuideMap.get(homeCareRosterVO.getCategoryId()).getName());

        //获取办事指南事项名称
        homeCareRosterVO.setServiceName(residentApplyGuideMap.get(homeCareRosterVO.getServiceId()).getName());

        //查询补充业务信息详情
        ResidentApplyHomeCare homeCare = residentRosterDao.findHomeCareDetailByRosterId(id);

        homeCareRosterVO.setResidentApplyHomeCare(homeCare);

		//查询修改记录
        List<ResidentApplyLog> logs = residentApplyLogUtil.getLogs(ResidentApplyTypeEumn.HOME_CARE, id,new ResidentApplyLog<ResidentApplyHomeCare>().getClass());

		homeCareRosterVO.setLogs(logs);

		//查询附件
		String fileId = homeCareRosterVO.getAttachedIds();
		homeCareRosterVO.setFileInfos(commonUtil.getFileInfos(fileId));

        BaseVo baseVo = new BaseVo();
        baseVo.setData(homeCareRosterVO);

        return baseVo;

    }
	/**
	 * 注销服务名单
	 * @param rosterCancelVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo revokeRoster(RosterCancelVO rosterCancelVO) {
		//服务名单类型id
		Integer serviceId = rosterCancelVO.getServiceId();
		//记录id
		Long recordId = rosterCancelVO.getRecordId();
		//撤销备注
		String remark = rosterCancelVO.getCancellationRemarks();
		if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_LIVING.getCode())){
			ResidentBasicLivingRosterVO rosterVO = new ResidentBasicLivingRosterVO();
			rosterVO.setId(recordId);
			rosterVO.setStatus(Const.STATUS_NOT_ACTIVE);
			rosterVO.setCancellationRemarks(remark);
			residentRosterDao.modifyLivingRoster(rosterVO);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_HOME_CARE.getCode())){
			ResidentHomeCareRosterVO rosterVO = new ResidentHomeCareRosterVO();
			rosterVO.setId(recordId);
			rosterVO.setStatus(Const.STATUS_NOT_ACTIVE);
			rosterVO.setCancellationRemarks(remark);
			residentRosterDao.modifyHomeCareRoster(rosterVO);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_DISABLE.getCode())){
			ResidentDisableIndividualRosterVO rosterVO = new ResidentDisableIndividualRosterVO();
			rosterVO.setId(recordId);
			rosterVO.setStatus(Const.STATUS_NOT_ACTIVE);
			rosterVO.setCancellationRemarks(remark);
			residentRosterDao.modifyDisableRoster(rosterVO);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_SPECIAL_SUPPORT.getCode())){
			ResidentSpecialSupportRosterVO rosterVO = new ResidentSpecialSupportRosterVO();
			rosterVO.setId(recordId);
			rosterVO.setStatus(Const.STATUS_NOT_ACTIVE);
			rosterVO.setCancellationRemarks(remark);
			residentRosterDao.modifySpecialSupportRoster(rosterVO);
		}

		return successVo();
	}
	/**
	 * 根据业务id和记录id 删除数据
	 * @param serviceId
	 * @param recordId
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo deleteByServiceIdAndRecordId(Integer serviceId, Long recordId) {

		if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_LIVING.getCode())){
			//先检查是否已经并发删除
			ResidentBasicLivingRoster livingRosterById = residentRosterDao.findLivingRosterById(recordId);
			if(livingRosterById!=null){
				if (livingRosterById.getDataType().equals(ResidentRosterEnum.DATA_TYPE_INSERT.getCode())){
					//先删除低保服务名单
					residentRosterDao.deleteLivingRosterById(recordId);

					//再删除低保明细
					residentRosterDao.deleteLivingByRosterId(recordId);
				}else {
					throwBusinessException("只有手动创建的服务名单可以删除！");
				}
			}else {
				throwBusinessException("此条服务名单已删除！");
			}

		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_HOME_CARE.getCode())){
			//先检查是否已经并发删除
			ResidentHomeCareRoster homeCareRosterById = residentRosterDao.findHomeCareRosterById(recordId);
			if(homeCareRosterById!=null) {
				if (homeCareRosterById.getDataType().equals(ResidentRosterEnum.DATA_TYPE_INSERT.getCode())){
					//先删除低保服务名单
					residentRosterDao.deleteHomeCareRosterById(recordId);

					//再删除低保明细
					residentRosterDao.deleteDisableById(recordId);
				}else {
					throwBusinessException("只有手动创建的服务名单可以删除！");
				}
			}else {
				throwBusinessException("此条服务名单已删除！");
			}
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_DISABLE.getCode())){
			//先检查是否已经并发删除
			ResidentDisableIndividualRoster disableRosterById = residentRosterDao.findDisableRosterById(recordId);
			if(disableRosterById!=null) {
				if (disableRosterById.getDataType().equals(ResidentRosterEnum.DATA_TYPE_INSERT.getCode())){
					//先删除残疾人服务名单
					residentRosterDao.deleteDisableByRosterId(recordId);

					//再删除残疾人明细
					residentRosterDao.deleteHomeCareByRosterId(recordId);
				}else {
					throwBusinessException("只有手动创建的服务名单可以删除！");
				}
			}else {
				throwBusinessException("此条服务名单已删除！");
			}
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_SPECIAL_SUPPORT.getCode())){
			//先检查是否已经并发删除
			ResidentSpecialSupportRoster specialSupportById = residentRosterDao.findSpecialSupportById(recordId);
			if(specialSupportById!=null) {
				if (specialSupportById.getDataType().equals(ResidentRosterEnum.DATA_TYPE_INSERT.getCode())){
					//先删除残疾人服务名单
					residentRosterDao.deleteSpecialSupportByRosterId(recordId);

					//再删除残疾人明细
					residentRosterDao.deleteSpecialSupportById(recordId);
				}else {
					throwBusinessException("只有手动创建的服务名单可以删除！");
				}
			}else {
				throwBusinessException("此条服务名单已删除！");
			}
		}
		return successVo();
	}

	/**
	 * 根据业务id和记录id 查询详情
	 * @param serviceId
	 * @param recordId
	 * @return
	 */
	@Override
	public BaseVo serviceDetail(Integer serviceId, Long recordId) {
		if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_LIVING.getCode())){
			return this.livingAllowanceDetail(recordId);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_HOME_CARE.getCode())){
			return this.homeCareDetail(recordId);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_DISABLE.getCode())){
			return this.disableDetail(recordId);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_SPECIAL_SUPPORT.getCode())){
			return this.specialSupportDetail(recordId);
		}
		return successVo();
	}
	/**
	 * 更新服务名单的附件
	 * @param rosterCancelVO
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo updateRosterFile(RosterCancelVO rosterCancelVO) {
		Integer serviceId = rosterCancelVO.getServiceId();
		Long recordId = rosterCancelVO.getRecordId();
		if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_LIVING.getCode())){
			ResidentBasicLivingRosterVO obj = new ResidentBasicLivingRosterVO();
			obj.setId(recordId);
			obj.setAttachedIds(rosterCancelVO.getAttachedIds());
			residentRosterDao.modifyLivingRoster(obj);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_HOME_CARE.getCode())){
			ResidentHomeCareRosterVO obj = new ResidentHomeCareRosterVO();
			obj.setId(recordId);
			obj.setAttachedIds(rosterCancelVO.getAttachedIds());
			residentRosterDao.modifyHomeCareRoster(obj);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_DISABLE.getCode())){
			ResidentDisableIndividualRosterVO obj = new ResidentDisableIndividualRosterVO();
			obj.setId(recordId);
			obj.setAttachedIds(rosterCancelVO.getAttachedIds());
			residentRosterDao.modifyDisableRoster(obj);
		}else if (serviceId.equals(ResidentApplyBaseEnum.GUIDE_EXTEND_TYPE_SPECIAL_SUPPORT.getCode())){
			ResidentSpecialSupportRosterVO obj = new ResidentSpecialSupportRosterVO();
			obj.setId(recordId);
			obj.setAttachedIds(rosterCancelVO.getAttachedIds());
			residentRosterDao.modifySpecialSupportRoster(obj);
		}
		return successVo();
	}

    /**
     * 根据查询条件导出低保服务名单
     * @author liyang
     * @date 2019-12-09
     * @param serviceId : 事项ID
     * @param residentBasicLivingRosterVO : 查询条件
     */
    @Override
    public void basicLivingExportToExcel(Long serviceId, ResidentBasicLivingRosterVO residentBasicLivingRosterVO,HttpServletResponse response) {
        UserInfo userInfo = getCurrentUserInfo();

        String name = "服务名单-低保";

        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //获取社区ID
            residentBasicLivingRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        residentBasicLivingRosterVO.setServiceId(serviceId);

        //分页查询

        List<ResidentBasicLivingRosterExcelVO> residentBasicLivingRosterVOList = residentRosterDao.ExportBasicLivingRoster(residentBasicLivingRosterVO);

        if(residentBasicLivingRosterVOList.size() == Const.COUNT){
            String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
            ExcelHandler.exportExcel(residentBasicLivingRosterVOList,fileName,name,ResidentBasicLivingRosterExcelVO.class,response);
        }

        //如果是社区办 还要获取社区名称
        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            residentBasicLivingRosterVOList.forEach(base -> base.setApplyTime(base.getApplyTime().substring(0,10)));
            //如果是社区办
            List<Long> ids = new ArrayList<>();
            residentBasicLivingRosterVOList.forEach(base->ids.add(base.getOrgId()));
            List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

            //转换Map
            Map<Long,OaOrganization>  oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

            for (ResidentBasicLivingRosterExcelVO living : residentBasicLivingRosterVOList){
                living.setOrgName(oaOrganizationMap.get(living.getOrgId()).getName());
            }
        }

        String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        ExcelHandler.exportExcel(residentBasicLivingRosterVOList,fileName,name,ResidentBasicLivingRosterExcelVO.class,response);

    }

    /**
     * 根据查询条件导出特扶服务名单
     * @author liyang
     * @date 2019-12-10
     * @param serviceId : 事项ID
     * @param : residentSpecialSupportRosterVO：查询条件
     */
    @Override
    public void specialSupportExportToExcel(Long serviceId, ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO, HttpServletResponse response) {

        UserInfo userInfo = getCurrentUserInfo();
        String name = "服务名单-特扶";

        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //获取社区ID
            residentSpecialSupportRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        residentSpecialSupportRosterVO.setServiceId(serviceId);
        //查询
        List<ResidentSpecialSupportRosterExcelVO> allSpecialRoster = residentRosterDao.exportToSpecialRoster(residentSpecialSupportRosterVO);

        if(allSpecialRoster.size() == Const.COUNT){
            String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
            ExcelHandler.exportExcel(allSpecialRoster,fileName,name,ResidentSpecialSupportRosterExcelVO.class,response);
        }

        //如果是社区办 还要获取社区名称
        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			allSpecialRoster.forEach(base -> base.setApplyTime(base.getApplyTime().substring(0,10)));
            //如果是社区办
            List<Long> ids = new ArrayList<>();
            allSpecialRoster.forEach(base->ids.add(base.getOrgId()));
            List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

            //转换Map
            Map<Long,OaOrganization>  oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

            for (ResidentSpecialSupportRosterExcelVO homeCare : allSpecialRoster){
                homeCare.setOrgName(oaOrganizationMap.get(homeCare.getOrgId()).getName());
            }
        }

        //获取特扶类别名称
        BaseVo baseVo = sysServiceFeign.findByModule(Const.SPECIALSUPPORTNAME);
        String dictListStr = JSON.toJSONString(baseVo.getData());
        List<Dict> dictList = JSONArray.parseArray(dictListStr,Dict.class);
        Map<String,Dict> dictMap = dictList.stream().collect(Collectors.toMap(Dict::getDictCode,d->d));
        for(ResidentSpecialSupportRosterExcelVO re: allSpecialRoster){
            re.setSupportTypeName(dictMap.get(re.getSupportType()).getDictName());
        }

        String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        ExcelHandler.exportExcel(allSpecialRoster,fileName,name,ResidentSpecialSupportRosterExcelVO.class,response);


    }

    /**
     * 根据条件导出残疾服务名单
     * @author liyang
     * @date 2019-12-10
     * @param serviceId : 事项ID
     * @param : residentDisableIndividualRosterVO：查询条件
     */
    @Override
    public void disableExportToExcel(Long serviceId, ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO, HttpServletResponse response) {
        UserInfo userInfo = getCurrentUserInfo();

        //设置导出名称
        String name = "服务名单-残疾申请";
        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //获取社区ID
            residentDisableIndividualRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        residentDisableIndividualRosterVO.setServiceId(serviceId);

        //查询
        List<ResidentDisableIndividualRosterExcelVO> allDisableRoster = residentRosterDao.ExportDisableRoster(residentDisableIndividualRosterVO);

        if(allDisableRoster.size() == Const.COUNT){
            String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
            ExcelHandler.exportExcel(allDisableRoster,fileName,name,ResidentDisableIndividualRosterExcelVO.class,response);
        }

        //如果是社区办 还要获取社区名称
        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			allDisableRoster.forEach(base -> base.setApplyTime(base.getApplyTime().substring(0,10)));
            //如果是社区办
            List<Long> ids = new ArrayList<>();
            allDisableRoster.forEach(base->ids.add(base.getOrgId()));
            List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

            //转换Map
            Map<Long,OaOrganization>  oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

            for (ResidentDisableIndividualRosterExcelVO homeCare : allDisableRoster){
                homeCare.setOrgName(oaOrganizationMap.get(homeCare.getOrgId()).getName());
            }
        }

        //获取残疾类别
        BaseVo baseVo = sysServiceFeign.findByModule(Const.DISABLE_CATEGORY);
        String dictListStr = JSON.toJSONString(baseVo.getData());
        List<Dict> dictList = JSONArray.parseArray(dictListStr,Dict.class);
        Map<String,Dict> dictMap = dictList.stream().collect(Collectors.toMap(Dict::getDictCode,d->d));
        for(ResidentDisableIndividualRosterExcelVO re: allDisableRoster){
            re.setDisabilityCategoryName(dictMap.get(re.getDisabilityCategory()).getDictName());
        }

        String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        ExcelHandler.exportExcel(allDisableRoster,fileName,name,ResidentDisableIndividualRosterExcelVO.class,response);

    }

    /**
     * 根据条件导出居家养老服务名单
     * @author liyang
     * @date 2019-12-10
     * @param serviceId : 事项ID
     * @param : residentDisableIndividualRosterVO：查询条件
     */
    @Override
    public void homeCareRosterExportToExcel(Long serviceId, ResidentHomeCareRosterVO residentHomeCareRosterVO, HttpServletResponse response) {
        UserInfo userInfo = getCurrentUserInfo();

        String name = "服务名单-居家养老";

        if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
            //获取社区ID
            residentHomeCareRosterVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
        }

        residentHomeCareRosterVO.setServiceId(serviceId);

        //查询
        List<ResidentHomeCareRosterExcelVO> residentHomeCareRosterVOList = residentRosterDao.exportHomeCareRoster(residentHomeCareRosterVO);

        //空集合直接导出
        if(residentHomeCareRosterVOList.size() == Const.COUNT){
            String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
            ExcelHandler.exportExcel(residentHomeCareRosterVOList,fileName,name,ResidentHomeCareRosterExcelVO.class,response);
        }

        //如果是社区办 还要获取社区名称
        if(userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
			residentHomeCareRosterVOList.forEach(base -> base.setApplyTime(base.getApplyTime().substring(0,10)));
            //如果是社区办
            List<Long> ids = new ArrayList<>();
            residentHomeCareRosterVOList.forEach(base->ids.add(base.getOrgId()));
            List<OaOrganization> oaOrganizationList = sysServiceFeign.batchSelectOaDepartmentByIds(ids);

            //转换Map
            Map<Long,OaOrganization>  oaOrganizationMap = oaOrganizationList.stream().collect(Collectors.toMap(OaOrganization::getId,oaOrganization -> oaOrganization));

            for (ResidentHomeCareRosterExcelVO homeCare : residentHomeCareRosterVOList){
                homeCare.setOrgName(oaOrganizationMap.get(homeCare.getOrgId()).getName());
            }
        }

        //获取 待遇类别	户口类别	评估等级 生活处理能力
        Dict dict = new Dict();
        List<String> modules = new ArrayList<>();

        //户口类别
        modules.add(Const.RESIDENCE_TYPE);

        //待遇类别
        modules.add(Const.TREATMENT_TYPE);

        //评估等级
        modules.add(Const.BASE_LEVEL);

        //生活自理能力
        modules.add(Const.LIVING_ABILITY);

        dict.setModules(modules);
        BaseVo baseVo = sysServiceFeign.findByModules(dict);
        String dicts = JSON.toJSONString(baseVo.getData());

        Map<String,List<Dict>> mapType = JSON.parseObject(dicts,Map.class);

        //户口类别map转换
        List<Dict> residenceList = JSON.parseArray(JSON.toJSONString(mapType.get(Const.RESIDENCE_TYPE)),Dict.class);
        Map<String,Dict> residenceMap = residenceList.stream().collect(Collectors.toMap(Dict::getDictCode,d -> d));

        //待遇类别map转换
        List<Dict> treatmentList = JSONArray.parseArray(JSON.toJSONString(mapType.get(Const.TREATMENT_TYPE)),Dict.class);
        Map<String,Dict> treatmentMap = treatmentList.stream().collect(Collectors.toMap(Dict::getDictCode,d -> d));

        //评估等级map转换
        List<Dict> levelList = JSONArray.parseArray(JSON.toJSONString(mapType.get(Const.BASE_LEVEL)),Dict.class);
        Map<String,Dict> levelMap = levelList.stream().collect(Collectors.toMap(Dict::getDictCode,d -> d));

        //生活自理能力map转换
        List<Dict> livingAbilityList = JSONArray.parseArray(JSON.toJSONString(mapType.get(Const.LIVING_ABILITY)),Dict.class);
        Map<String,Dict> livingAbilityMap = livingAbilityList.stream().collect(Collectors.toMap(Dict::getDictCode,d -> d));

        residentHomeCareRosterVOList.forEach(re->{

            //户口类别
            if(re.getResidenceType() != null && !re.getResidenceType().equals("")){
                re.setResidenceTypeName(residenceMap.get(String.valueOf(re.getResidenceType())).getDictName());
            }

            //待遇类别
            if(re.getTreatmentType() != null && !re.getTreatmentType().equals("")){
                re.setTreatmentTypeName(treatmentMap.get(String.valueOf(re.getTreatmentType())).getDictName());
            }

            //评估等级
            if(re.getLevel() != null && !re.getLevel().equals("")){
                re.setLevelName(levelMap.get(String.valueOf(re.getLevel())).getDictName());
            }

            //生活自理能力
            if(re.getLivingAbility() != null && !re.getLivingAbility().equals("")){
                re.setLivingAbilityName(livingAbilityMap.get(String.valueOf(re.getLivingAbility())).getDictName());
            }

        });

        String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        ExcelHandler.exportExcel(residentHomeCareRosterVOList,fileName,name,ResidentHomeCareRosterExcelVO.class,response);
    }

    /**
	 * 查询残疾人服务详情
	 * @param recordId
	 * @return
	 */
	private BaseVo disableDetail(Long recordId){
		BaseVo baseVo = new BaseVo();
		ResidentDisableIndividualRosterVO residentDisableIndividualRosterVO = new ResidentDisableIndividualRosterVO();
		//查询业务名单详情
		residentDisableIndividualRosterVO = residentRosterDao.findDisbaleDetailsById(recordId);
		if (residentDisableIndividualRosterVO!=null){
			//获取所有办事指南
			List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findAllGuide();
			//办事指南分类汇总
			Map<Long,ResidentApplyGuide> residentApplyGuideMap = residentApplyGuideList.stream().collect(Collectors.toMap(ResidentApplyGuide::getId, residentApplyGuide -> residentApplyGuide));
			//获取办事指南类别名称
			residentDisableIndividualRosterVO.setCategoryName(residentApplyGuideMap.get(residentDisableIndividualRosterVO.getCategoryId()).getName());
			//获取办事指南事项名称
			residentDisableIndividualRosterVO.setServiceName(residentApplyGuideMap.get(residentDisableIndividualRosterVO.getServiceId()).getName());
			//查询补充业务信息详情
			ResidentApplyDisabledIndividuals diableDetailByRosterId = residentRosterDao.findDisableDetailByRosterId(recordId);

			residentDisableIndividualRosterVO.setResidentApplyDisabledIndividuals(diableDetailByRosterId);

			//查询修改记录
			List<ResidentApplyLog> logs = residentApplyLogUtil.getLogs(ResidentApplyTypeEumn.DISABLED_INDIVIDUALS, recordId,new ResidentApplyLog<ResidentApplyDisabledIndividuals>().getClass());

			residentDisableIndividualRosterVO.setLogs(logs);

			//查询附件
			String fileId = residentDisableIndividualRosterVO.getAttachedIds();
			residentDisableIndividualRosterVO.setFileInfos(commonUtil.getFileInfos(fileId));

			baseVo.setData(residentDisableIndividualRosterVO);
		}
		return baseVo;
	}

	/**
	 * 查询特殊扶助服务详情
	 * @param recordId
	 * @return
	 */
	private BaseVo specialSupportDetail(Long recordId){
		BaseVo baseVo = new BaseVo();
		ResidentSpecialSupportRosterVO residentSpecialSupportRosterVO= new ResidentSpecialSupportRosterVO();
		//查询业务名单详情
		residentSpecialSupportRosterVO = residentRosterDao.findSpecialSupportDetailsById(recordId);
		if (residentSpecialSupportRosterVO!=null){
			//获取所有办事指南
			List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findAllGuide();
			//办事指南分类汇总
			Map<Long,ResidentApplyGuide> residentApplyGuideMap = residentApplyGuideList.stream().collect(Collectors.toMap(ResidentApplyGuide::getId, residentApplyGuide -> residentApplyGuide));
			//获取办事指南类别名称
			if (residentApplyGuideMap.get(residentSpecialSupportRosterVO.getCategoryId())!=null){
				residentSpecialSupportRosterVO.setCategoryName(residentApplyGuideMap.get(residentSpecialSupportRosterVO.getCategoryId()).getName());
			}

			//获取办事指南事项名称
			residentSpecialSupportRosterVO.setServiceName(residentApplyGuideMap.get(residentSpecialSupportRosterVO.getServiceId()).getName());
			//查询补充业务信息详情
			ResidentApplySpecialSupport residentApplySpecialSupport = residentRosterDao.findSpecialSupportDetailByRosterId(recordId);

			residentSpecialSupportRosterVO.setResidentApplySpecialSupport(residentApplySpecialSupport);

			//查询修改记录
			List<ResidentApplyLog> logs = residentApplyLogUtil.getLogs(ResidentApplyTypeEumn.SPECIAL_SUPPORT, recordId, new ResidentApplyLog<ResidentApplySpecialSupport>().getClass());

			residentSpecialSupportRosterVO.setLogs(logs);

			//查询附件
			String fileId = residentSpecialSupportRosterVO.getAttachedIds();
			residentSpecialSupportRosterVO.setFileInfos(commonUtil.getFileInfos(fileId));

			baseVo.setData(residentSpecialSupportRosterVO);
		}
		return baseVo;
	}
}
