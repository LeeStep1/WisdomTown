package com.bit.module.cbo.service.Impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.common.consts.RedisKey;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.common.enumerate.PmcStaffStatusEnum;
import com.bit.module.cbo.bean.PmcStaff;
import com.bit.module.cbo.bean.Resident;
import com.bit.module.cbo.dao.PmcStaffDao;
import com.bit.module.cbo.dao.ResidentDao;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.vo.ChangePassWordMobileVO;
import com.bit.module.cbo.vo.CommonVO;
import com.bit.module.cbo.service.AppCommonService;
import com.bit.core.utils.CacheUtil;
import com.bit.soft.sms.bean.SmsCodeRequest;
import com.bit.soft.sms.client.SmsFeignClient;
import com.bit.soft.sms.common.SmsAccountTemplateEnum;
import com.bit.utils.MD5Util;
import com.bit.utils.RedisKeyUtil;
import com.bit.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description app端公用的服务
 * @Author chenduo
 * @Date 2019/7/17 15:20
 **/
@Service("appCommonService")
public class AppCommonServiceImpl extends BaseService implements AppCommonService {

    @Autowired
    private ResidentDao residentDao;
    @Autowired
    private CacheUtil cacheUtil;
    @Autowired
    private PmcStaffDao pmcStaffDao;
    @Autowired
    private SmsFeignClient smsFeignClient;


    /**
     * 三个app重置密码
     *
     * @param commonVO
     * @return
     */
    @Override
    @Transactional
    public BaseVo resetPassword(CommonVO commonVO, HttpServletRequest request) {
        String mobile = commonVO.getMobile();
        String tid = request.getHeader("tid");
        //判断接入端
        if (tid.equals(String.valueOf(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid()))) {

            //判断手机号是否在信息表中
            boolean flag = checkResidentExist(mobile);
            if (!flag) {
                throwBusinessException("该账号不存在");
            }
            /*String smsCode = commonVO.getSmsCode();
			if (StringUtil.isEmpty(smsCode)){
				throwBusinessException("验证码失效,请重新获取");
			}
			String regisVerCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_BACKPWD_VERIFICATION_CODE,mobile, String.valueOf(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid()));
			String sms = (String) cacheUtil.get(regisVerCodeKey);
			if (StringUtil.isEmpty(sms)){
				throwBusinessException("验证码失效");
			}
			if (!smsCode.equals(sms)){
				throwBusinessException("验证码不正确");
			}*/
			/*修改调用后*/
           /* SmsCodeRequest smsCodeRequest = new SmsCodeRequest();
            smsCodeRequest.setMobileNumber(mobile);
            smsCodeRequest.setSmsCode(commonVO.getSmsCode());
            smsCodeRequest.setTerminalId(Long.valueOf(TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid()));
            smsCodeRequest.setSmsTemplateId(SmsAccountTemplateEnum.SMS_ACCOUNT_RESET_PASSWORD.getSmsTemplateType());
            BaseVo feignDto = smsFeignClient.checkCode(smsCodeRequest);
            if (feignDto.getCode() == ResultCode.SMS_CHECKED_FAIL.getCode()) {
                throwBusinessException("验证码不正确");
            } else if ((feignDto.getCode() == ResultCode.SMS_CHECKED_NO_EFFECT.getCode())) {
                throwBusinessException("验证码失效");
            }*/
            checkSmsCode(mobile,TerminalTypeEnum.TERMINALTOCBOORGNTAPP.getTid(),SmsAccountTemplateEnum.SMS_ACCOUNT_RESET_PASSWORD.getSmsTemplateType(),commonVO.getSmsCode());
            //查询旧记录
            Resident residentByMobile = residentDao.getResidentByMobile(mobile);
            String salt = residentByMobile.getSalt();
            String encryptedPassword = MD5Util.compute(commonVO.getPassword() + salt);
            Resident rm = new Resident();
            rm.setId(residentByMobile.getId());
            rm.setPassword(encryptedPassword);
            int i = residentDao.updateResident(rm);
            if (i <= 0) {
                throwBusinessException("重置密码失败");
            }
            return successVo();
        } else if (tid.equals(String.valueOf(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid()))) {
            boolean flag = checkPmcStaffExist(mobile);
            if (!flag) {
                throwBusinessException("该账号不存在");
            }
            PmcStaff pmc = pmcStaffDao.getPmcStaffByMobile(mobile);
            if (pmc != null) {
                //如果停用
                if (pmc.getStatus().equals(PmcStaffStatusEnum.PMC_STAFF_STATUS_INACTIVE.getCode())) {
                    throwBusinessException("该账号已停用");
                }
            }
            String smsCode = commonVO.getSmsCode();
            if (StringUtil.isEmpty(smsCode)) {
                throwBusinessException("验证码为空,请重新获取验证码");
            }
            //查询旧记录
            PmcStaff pmcStaffByMobile = pmcStaffDao.getPmcStaffByMobile(mobile);
            if (pmcStaffByMobile.getStatus().equals(PmcStaffStatusEnum.PMC_STAFF_STATUS_INACTIVE)) {
                throwBusinessException("该账号已经停用");
            }

			/*String regisVerCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_BACKPWD_VERIFICATION_CODE,mobile, String.valueOf(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid()));
			String sms = (String) cacheUtil.get(regisVerCodeKey);
			if (!smsCode.equals(sms)){
				throwBusinessException("验证码不正确");
			}*/

			/*修改后*/
            checkSmsCode(mobile,TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid(),SmsAccountTemplateEnum.SMS_ACCOUNT_RESET_PASSWORD.getSmsTemplateType(),smsCode);
            String salt = pmcStaffByMobile.getSalt();
            String encryptedPassword = MD5Util.compute(commonVO.getPassword() + salt);
            PmcStaff pmcStaff = new PmcStaff();
            pmcStaff.setId(pmcStaffByMobile.getId());
            pmcStaff.setPassword(encryptedPassword);
            int i = pmcStaffDao.updatePmcStaff(pmcStaff);
            if (i <= 0) {
                throwBusinessException("重置密码失败");
            }
            return successVo();
        } else {
            throwBusinessException("非法的接入端");
        }

        return null;
    }

    /**
     * 校验居民手机号是否存在
     *
     * @param mobile
     * @return
     */
    private boolean checkResidentExist(String mobile) {
        //查询居民表中这个手机号是否存在
        int i = residentDao.checkResidentExist(mobile);
        if (i >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 校验物业工作人员手机号是否存在
     *
     * @param mobile
     * @return
     */
    private boolean checkPmcStaffExist(String mobile) {
        int i = pmcStaffDao.checkPmcStaffExist(mobile);
        if (i >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 居民 和 物业 app更换密码
     *
     * @param changePassWordMobileVO
     * @return
     */
    @Override
    @Transactional
    public BaseVo changePassWord(ChangePassWordMobileVO changePassWordMobileVO) {
        Integer tid = getCurrentUserInfo().getTid();
        //校验密码
        checkPassword(changePassWordMobileVO, tid);
        if (tid.equals(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid())) {
            Long pmcId = getCurrentUserInfo().getId();
            PmcStaff pmcStaffById = pmcStaffDao.getPmcStaffById(pmcId);
            if (pmcStaffById == null) {
                throwBusinessException("该用户不存在");
            }
            //校验新密码是否合规
            String newpass = checkChangePassword(changePassWordMobileVO);
            //更新密码
            String encryptPass = MD5Util.compute(newpass + pmcStaffById.getSalt());
            PmcStaff pmcStaff = new PmcStaff();
            pmcStaff.setId(pmcId);
            pmcStaff.setPassword(encryptPass);
            pmcStaffDao.modify(pmcStaff);
            //强制退出
            String key = Const.TOKEN_PREFIX + tid + ":" + getCurrentUserInfo().getToken();
            cacheUtil.del(key);
        } else if (tid.equals(String.valueOf(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid()))) {
            Long residentId = getCurrentUserInfo().getId();
            Resident residentById = residentDao.getResidentById(residentId);
            if (residentById == null) {
                throwBusinessException("该用户不存在");
            }
            //校验新密码是否合规
            String newpass = checkChangePassword(changePassWordMobileVO);
            //更新密码
            String encryptPass = MD5Util.compute(newpass + residentById.getSalt());
            Resident resident = new Resident();
            resident.setId(residentId);
            resident.setPassword(encryptPass);
            int i = residentDao.updateResident(resident);
            if (i <= 0) {
                throwBusinessException("更新密码失败");
            }
            //强制退出
            String key = Const.TOKEN_PREFIX + tid + ":" + getCurrentUserInfo().getToken();
            cacheUtil.del(key);
        } else {
            throwBusinessException("非法的接入端");
        }
        return successVo();
    }

    /**
     * 校验物业端app 和 居民端app 的密码
     *
     * @param changePassWordMobileVO
     * @param tid
     * @return
     */
    private Boolean checkPassword(ChangePassWordMobileVO changePassWordMobileVO, Integer tid) {
        if (tid.equals(String.valueOf(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid()))) {
            Long pmcId = getCurrentUserInfo().getId();
            PmcStaff pmcStaffById = pmcStaffDao.getPmcStaffById(pmcId);
            if (pmcStaffById == null) {
                throwBusinessException("该用户不存在");
            }
            if (StringUtil.isEmpty(changePassWordMobileVO.getOldPassWord())) {
                throwBusinessException("旧密码不能为空");
            }
            String salt = pmcStaffById.getSalt();
            //如果旧密码与数据库中密码不同
            if (!MD5Util.compute(changePassWordMobileVO.getOldPassWord() + salt).equals(pmcStaffById.getPassword())) {
                throwBusinessException("旧密码错误");
                //redis中记录错误次数
//			String key = Const.CBO_CHANGE_PASSWORD + Const.TID_CBO_PMC_APP + pmcStaffById.getMobile();
//			Integer count = (Integer) cacheUtil.get(key);
//			if (count >= 5){
//				throwBusinessException("输入错误密码超过5次,请明天再试");
//			}else {
//				count = count + 1;
//				//持续时间
//				Long time = getRemainTime();
//				cacheUtil.set(key,count,time);
//				//提示密码错误 还可以在输入几次
//				throwBusinessException("密码错误!还可以在输入"+(5-count)+"次");
//			}
            }
        } else if (tid.equals(String.valueOf(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid()))) {
            Long residentId = getCurrentUserInfo().getId();
            Resident residentById = residentDao.getResidentById(residentId);
            if (residentById == null) {
                throwBusinessException("该用户不存在");
            }
            if (StringUtil.isEmpty(changePassWordMobileVO.getOldPassWord())) {
                throwBusinessException("旧密码不能为空");
            }
            String salt = residentById.getSalt();
            //如果旧密码与数据库中密码不同
            if (!MD5Util.compute(changePassWordMobileVO.getOldPassWord() + salt).equals(residentById.getPassword())) {
                throwBusinessException("旧密码错误");
                //redis中记录错误次数
//			String key = Const.CBO_CHANGE_PASSWORD + Const.TID_CBO_RESIDENT_APP + residentById.getMobile();
//			Integer count = (Integer) cacheUtil.get(key);
//			if (count >= 5){
//				throwBusinessException("输入错误密码超过5次,请明天再试");
//			}else {
//				count = count + 1;
//				//持续时间
//				Long time = getRemainTime();
//				cacheUtil.set(key,count,time);
//				//提示密码错误 还可以在输入几次
//				throwBusinessException("密码错误!还可以在输入"+(5-count)+"次");
//			}
            }
        }
        return true;
    }

    /**
     * 居民 和 物业 app更换手机号
     *
     * @param changePassWordMobileVO
     * @return
     */
    @Override
    @Transactional
    public BaseVo changeMobile(ChangePassWordMobileVO changePassWordMobileVO) {
        Integer tid = getCurrentUserInfo().getTid();
        if (tid.equals(String.valueOf(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid()))) {
            Long pmcId = getCurrentUserInfo().getId();
            PmcStaff pmcStaffById = pmcStaffDao.getPmcStaffById(pmcId);
            if (pmcStaffById == null) {
                throwBusinessException("该用户不存在");
            }
            //验证验证码
           /* String verCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_CBO_CHANGE_MOBILE_CODE, changePassWordMobileVO.getMobile(), String.valueOf(TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid()));
            String sms = (String) cacheUtil.get(verCodeKey);
            if (StringUtil.isEmpty(sms)) {
                throwBusinessException("验证码失效");
            }
            if (!changePassWordMobileVO.getSmsCode().equals(sms)) {
                throwBusinessException("验证码不正确");
            }*/
            checkSmsCode(changePassWordMobileVO.getMobile(),TerminalTypeEnum.TERMINALTOCBOPMCNTAPP.getTid(),SmsAccountTemplateEnum.CBO_SMS_OPERATION_CHANGE_MOBILE.getSmsTemplateType(),changePassWordMobileVO.getSmsCode());

            Boolean flag = checkPmcStaffExist(changePassWordMobileVO.getMobile());
            if (!flag) {
                throwBusinessException("手机号已存在");
            } else {
                PmcStaff pmcStaff = new PmcStaff();
                pmcStaff.setId(pmcId);
                pmcStaff.setMobile(changePassWordMobileVO.getMobile());
                int i = pmcStaffDao.updatePmcStaff(pmcStaff);
                if (i <= 0) {
                    throwBusinessException("更新物业员工失败");
                }
                //强制退出
                String key = Const.TOKEN_PREFIX + tid + ":" + getCurrentUserInfo().getToken();
                cacheUtil.del(key);
            }
        } else if (tid.equals(String.valueOf(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid()))) {
            Long residentId = getCurrentUserInfo().getId();
            Resident residentById = residentDao.getResidentById(residentId);
            if (residentById == null) {
                throwBusinessException("该用户不存在");
            }
            //验证验证码
            /*String verCodeKey = RedisKeyUtil.getRedisKey(RedisKey.SMS_CBO_CHANGE_MOBILE_CODE, changePassWordMobileVO.getMobile(), String.valueOf(TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid()));
            String sms = (String) cacheUtil.get(verCodeKey);
            if (StringUtil.isEmpty(sms)) {
                throwBusinessException("验证码失效");
            }
            if (!changePassWordMobileVO.getSmsCode().equals(sms)) {
                throwBusinessException("验证码不正确");
            }*/
            checkSmsCode(changePassWordMobileVO.getMobile(),TerminalTypeEnum.TERMINALTOCBORESIDENTAPP.getTid(),SmsAccountTemplateEnum.CBO_SMS_OPERATION_CHANGE_MOBILE.getSmsTemplateType(),changePassWordMobileVO.getSmsCode());
            Boolean flag = checkResidentExist(changePassWordMobileVO.getMobile());
            if (!flag) {
                throwBusinessException("手机号已存在");
            } else {
                Resident resident = new Resident();
                resident.setId(residentId);
                resident.setMobile(changePassWordMobileVO.getMobile());
                int i = residentDao.updateResident(resident);
                if (i <= 0) {
                    throwBusinessException("更新手机号失败");
                }
                //强制退出
                String key = Const.TOKEN_PREFIX + tid + ":" + getCurrentUserInfo().getToken();
                cacheUtil.del(key);
            }
        } else {
            throwBusinessException("非法的接入端");
        }
        return successVo();
    }

    /**
     * 校验新密码是否合规
     *
     * @param changePassWordMobileVO
     * @return
     */
    private String checkChangePassword(ChangePassWordMobileVO changePassWordMobileVO) {
        if (StringUtil.isEmpty(changePassWordMobileVO.getOldPassWord()) || StringUtil.isEmpty(changePassWordMobileVO.getNewPassWord())) {
            throwBusinessException("新密码或旧密码不能为空");
        }
        //校验新密码是否合规
        String newpass = changePassWordMobileVO.getNewPassWord();
        String passRegex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        if (!newpass.matches(passRegex)) {
            throwBusinessException("新密码格式不符合");
        }
        return newpass;
    }

    /**
     * @param mobile        :
     * @param tid           :
     * @param smsTemplateId :
     * @param smsCode       :
     * @return : void
     * @description:
     * @author liyujun
     * @date 2020-06-10
     */
    private void checkSmsCode(String mobile, long tid, int smsTemplateId, String smsCode) {
        SmsCodeRequest smsCodeRequest = new SmsCodeRequest();
        smsCodeRequest.setMobileNumber(mobile);
        smsCodeRequest.setSmsCode(smsCode);
        smsCodeRequest.setTerminalId(tid);
        smsCodeRequest.setSmsTemplateId(smsTemplateId);
        BaseVo feignDto = smsFeignClient.checkCode(smsCodeRequest);
        if (feignDto.getCode() == ResultCode.SMS_CHECKED_FAIL.getCode()) {
            throwBusinessException("验证码不正确");
        } else if ((feignDto.getCode() == ResultCode.SMS_CHECKED_NO_EFFECT.getCode())) {
            throwBusinessException("验证码失效");
        }
    }
}
