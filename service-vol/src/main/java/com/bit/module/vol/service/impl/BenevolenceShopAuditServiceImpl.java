package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.ApplyLogCode;
import com.bit.common.AuditMessageTemplate;
import com.bit.common.BaseConst;
import com.bit.common.ResultCode;
import com.bit.common.enumerate.BenevolenceShopEnum;
import com.bit.module.applylogs.bean.ApplyLogs;
import com.bit.module.applylogs.repository.ApplyRepository;

import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.bean.FileInfo;
import com.bit.module.vol.dao.BenevolenceShopAuditDao;
import com.bit.module.vol.dao.BenevolenceShopDao;
import com.bit.module.vol.feign.FileServiceFeign;
import com.bit.module.vol.service.BenevolenceShopAuditService;
import com.bit.module.vol.service.StationService;
import com.bit.module.vol.vo.BenevolenceShopVO;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.utils.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * BenevolenceShopAudit的Service实现类
 * @author liuyancheng
 *
 */
@Service("benevolenceShopAuditService")
public class BenevolenceShopAuditServiceImpl extends BaseService implements BenevolenceShopAuditService{
	
	private static final Logger logger = LoggerFactory.getLogger(BenevolenceShopAuditServiceImpl.class);

	@Autowired
	private BenevolenceShopAuditDao benevolenceShopAuditDao;
	@Autowired
	private BenevolenceShopDao benevolenceShopDao;
	@Autowired
	private ApplyRepository applyRepository;
	@Autowired
	private StationService stationService;

	@Autowired
	private SendMqPushUtil sendMqPushUtil;

	/**
	 * 图片服务调用
	 */
	@Autowired
	private FileServiceFeign fileServiceFeign;
	
	/**
	 * 根据条件查询BenevolenceShopAudit
	 * @param benevolenceShopVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(BenevolenceShopVO benevolenceShopVO){
		PageHelper.startPage(benevolenceShopVO.getPageNum(), benevolenceShopVO.getPageSize());
		benevolenceShopVO.setOrderBy("create_time");
		benevolenceShopVO.setOrder("desc");
		List<BenevolenceShop> list = benevolenceShopAuditDao.findByConditionPage(benevolenceShopVO);
		PageInfo<BenevolenceShop> pageInfo = new PageInfo<BenevolenceShop>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有BenevolenceShopAudit
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<BenevolenceShop> findAll(String sorter){
		return benevolenceShopAuditDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个BenevolenceShopAudit
	 * @param id
	 * @return
	 */
	@Override
	public BenevolenceShop findById(Long id){
		BenevolenceShop bs = benevolenceShopAuditDao.findById(id);
		if(bs.getImgId() != null){
			BaseVo baseVo = fileServiceFeign.findById(bs.getImgId());
			String strFileInfo = JSON.toJSONString(baseVo.getData());
			FileInfo fileInfo = JSON.parseObject(strFileInfo, FileInfo.class);
			bs.setImgPath(fileInfo.getPath());
		}

		return bs;
	}
	
	/**
	 * 保存BenevolenceShopAudit
	 * @param benevolenceShop
	 */
	@Override
	@Transactional
	public BaseVo add(BenevolenceShop benevolenceShop){
        BaseVo baseVo = new BaseVo();
		//校验商家名称是否重复
		if (StringUtils.isNotEmpty(benevolenceShop.getName())){
			int byName = benevolenceShopDao.findByName(benevolenceShop);
			if (byName>0){
				baseVo.setData(null);
				baseVo.setCode(ResultCode.WRONG.getCode());
				baseVo.setMsg("商家名称重复，请重新输入");
				return baseVo;
			}
			int i = benevolenceShopAuditDao.findByName(benevolenceShop.getName());
			if (i > 0){
                baseVo.setData(null);
                baseVo.setCode(ResultCode.WRONG.getCode());
                baseVo.setMsg("商家名称重复，请重新输入");
                return baseVo;
			}else {
				//创建时间
				benevolenceShop.setCreateTime(new Date());
				//锁
				benevolenceShop.setVersion(0);
				//app新增爱心商家，状态为0-停用状态
				benevolenceShop.setAuditState(BenevolenceShopEnum.BENEVOLENCE_SHOP_AUDIT_TOAUDIT.getCode());
				benevolenceShopAuditDao.add(benevolenceShop);

				// 发送mq消息
				//MqMessage mqMessage = new MqMessage();
				// 镇团委id
				Long topStationId = stationService.findTopStation();
				Long[] targetId = new Long[]{topStationId};
				// 模板参数
				String[] params = new String[]{benevolenceShop.getContacts()};

				MqSendMessage mqSendMessageTask= AppPushMessageUtil.pushOrgTaskByAlias(MessageTemplateEnum.VOL_BENEVOLENCE_SHOP_APPLY,targetId,params,benevolenceShop.getId(),0,"系统",new Date(),null);
				sendMqPushUtil.sendMqMessage(mqSendMessageTask);
			}
		}
		return baseVo;
	}
	/**
	 * 更新BenevolenceShopAudit
	 * @param benevolenceShop
	 */
	@Override
	@Transactional
	public BaseVo update(BenevolenceShop benevolenceShop, HttpServletRequest request){
		BaseVo baseVo = new BaseVo();
		//获取token
		UserInfo userInfo = getCurrentUserInfo();
		benevolenceShop.setUpdateUserId(userInfo.getId());
		benevolenceShop.setUpdateTime(new Date());
		try {
			BenevolenceShop byId = benevolenceShopAuditDao.findById(benevolenceShop.getId());
			if (byId == null){
				throwBusinessException("该条审核不存在");
			}else {
				benevolenceShop.setVersion(byId.getVersion());
				//如果更新状态和数据库中的状态都为1，表示该条记录已被其他管理员审核通过，则给出提示
				if (byId.getAuditState().equals(BenevolenceShopEnum.BENEVOLENCE_SHOP_AUDIT_PASSED.getCode())){
					baseVo.setCode(ResultCode.ADUITS_ALREADY_HANDLE.getCode());
					baseVo.setMsg(ResultCode.ADUITS_ALREADY_HANDLE.getInfo());
					return baseVo;
				}else {
					benevolenceShopAuditDao.update(benevolenceShop);
					if (benevolenceShop.getAuditState().equals(BenevolenceShopEnum.BENEVOLENCE_SHOP_AUDIT_PASSED.getCode())){
						BenevolenceShop shop = benevolenceShopAuditDao.findById(benevolenceShop.getId());
						int i = benevolenceShopDao.findByName(shop);
						if (i > 0){
							throwBusinessException("商家名称重复，请重新输入");
						}else {
							//设置为启用
							shop.setEnable(BenevolenceShopEnum.BENEVOLENCE_SHOP_ENABLE_YES.getCode());
							shop.setCreateUserId(userInfo.getId());
							benevolenceShopDao.add(shop);
						}
					}
				}
			}
		}catch (Exception e){

		}finally {
			ApplyLogs applyLogs = new ApplyLogs();
			//审核通过
			if (benevolenceShop.getAuditState().equals(BenevolenceShopEnum.BENEVOLENCE_SHOP_AUDIT_PASSED.getCode())){
				applyLogs.setTerminalId(userInfo.getTid());
				applyLogs.setServiceId(BaseConst.AUDIT_LOG_TYPE_VOL_BENEVOLENCE_SHOP);
				applyLogs.setFormId(benevolenceShop.getId());
				applyLogs.setOperUserId(userInfo.getId());
				applyLogs.setOperUserName(userInfo.getRealName());
				applyLogs.setOperationName(AuditMessageTemplate.AUDIT_BENEVOLENCE_SHOP);
				applyLogs.setCreateTime(new Date());
				applyLogs.setOperationCode(ApplyLogCode.AUDIT_PASS.getCode());
				applyLogs.setOperationTime(DateUtil.format(new Date()));
				applyLogs.setOperationContent(AuditMessageTemplate.AUDIT_BENEVOLENCE_SHOP);
				applyRepository.addApplyLogs(applyLogs);
			}else if (benevolenceShop.getAuditState().equals(BenevolenceShopEnum.BENEVOLENCE_SHOP_AUDIT_BACK.getCode())){
				//已退回
				applyLogs.setTerminalId(userInfo.getTid());
				applyLogs.setServiceId(BaseConst.AUDIT_LOG_TYPE_VOL_BENEVOLENCE_SHOP);
				applyLogs.setFormId(benevolenceShop.getId());
				applyLogs.setOperUserId(userInfo.getId());
				applyLogs.setOperUserName(userInfo.getRealName());
				applyLogs.setOperationName(AuditMessageTemplate.AUDIT_BENEVOLENCE_SHOP_REJECT);
				applyLogs.setCreateTime(new Date());
				applyLogs.setOperationCode(ApplyLogCode.AUDIT_BACK.getCode());
				applyLogs.setOperationTime(DateUtil.format(new Date()));
				applyLogs.setOperationContent(AuditMessageTemplate.AUDIT_BENEVOLENCE_SHOP_REJECT);
				//退回原因
				applyLogs.setParam(benevolenceShop.getReturnReason());
				applyRepository.addApplyLogs(applyLogs);
			}
		}
		return baseVo;
	}
	/**
	 * 删除BenevolenceShopAudit
	 * @param id
	 */
	@Override
	public void delete(Long id){
		benevolenceShopAuditDao.delete(id);
	}
}
