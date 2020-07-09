package com.bit.module.vol.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.common.*;
import com.bit.common.consts.TerminalTypeEnum;
import com.bit.common.enumerate.BenevolenceShopEnum;
import com.bit.module.applylogs.bean.ApplyLogs;
import com.bit.module.applylogs.repository.ApplyRepository;
import com.bit.soft.push.msEnum.MessageTemplateEnum;
import com.bit.soft.push.payload.MqMessage;
import com.bit.soft.push.payload.MqSendMessage;
import com.bit.soft.push.utils.AppPushMessageUtil;
import com.bit.soft.push.utils.SendMqPushUtil;
import com.bit.module.vol.bean.*;
import com.bit.module.vol.dao.*;
import com.bit.module.vol.feign.FileServiceFeign;
import com.bit.module.vol.feign.SysServiceFeign;
import com.bit.module.vol.service.StationService;
import com.bit.module.vol.service.VolunteerService;
import com.bit.module.vol.vo.*;
import com.bit.utils.DateUtil;
import com.bit.utils.ExcelUtil;
import com.bit.utils.VolUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bit.base.vo.BaseVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.bit.module.vol.service.ProductExchangeAuditService;
import com.bit.base.service.BaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ProductExchangeAudit的Service实现类
 * @author liuyancheng
 *
 */
@Service("productExchangeAuditService")
public class ProductExchangeAuditServiceImpl extends BaseService implements ProductExchangeAuditService{
	
	private static final Logger logger = LoggerFactory.getLogger(ProductExchangeAuditServiceImpl.class);
	
	@Autowired
	private ProductExchangeAuditDao productExchangeAuditDao;
	@Autowired
	private ApplyRepository applyRepository;
	@Autowired
	private VolunteerService volunteerService;
	@Autowired
	private VolunteerDao volunteerDao;
	@Autowired
	private BenevolenceShopProductDao benevolenceShopProductDao;
	@Autowired
	private FileServiceFeign fileServiceFeign;
	@Autowired
	private BenevolenceShopDao benevolenceShopDao;
	@Autowired
	private StationService stationService;
	@Autowired
	private StationDao stationDao;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private VolUtil volUtil;

	@Autowired
	private SendMqPushUtil sendMqPushUtil;

	/**
	 * 根据条件查询ProductExchangeAudit
	 * @param productExchangeAuditVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(ProductExchangeAuditVO productExchangeAuditVO){
		PageHelper.startPage(productExchangeAuditVO.getPageNum(), productExchangeAuditVO.getPageSize());
		productExchangeAuditVO.setOrderBy("create_time");
		productExchangeAuditVO.setOrder("desc");
		List<ProductExchangeAudit> list = productExchangeAuditDao.findByConditionPage(productExchangeAuditVO);
		//处理总积分
		for (ProductExchangeAudit productExchangeAudit : list) {
			//商品积分值（单价）
			Integer integralValue = productExchangeAudit.getIntegralValue();
			//兑换数量
			Integer exchangeNumber = productExchangeAudit.getExchangeNumber();
			Integer result = integralValue * exchangeNumber;
			productExchangeAudit.setExchangeIntegralAmount(result);
		}
		PageInfo<ProductExchangeAudit> pageInfo = new PageInfo<ProductExchangeAudit>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 根据条件查询ProductExchangeAudit 兑换确认
	 * @param productExchangeAuditVO
	 * @return
	 */
	@Override
	public BaseVo listPageConfirm(ProductExchangeAuditVO productExchangeAuditVO) {
		PageHelper.startPage(productExchangeAuditVO.getPageNum(), productExchangeAuditVO.getPageSize());
		productExchangeAuditVO.setOrderBy("create_time");
		productExchangeAuditVO.setOrder("desc");
		productExchangeAuditVO.setAuditStatus(BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_PASSED.getCode());
		List<ProductExchangeAudit> list = productExchangeAuditDao.findByConditionPage(productExchangeAuditVO);
		//处理总积分
		for (ProductExchangeAudit productExchangeAudit : list) {
			//商品积分值（单价）
			Integer integralValue = productExchangeAudit.getIntegralValue();
			//兑换数量
			Integer exchangeNumber = productExchangeAudit.getExchangeNumber();
			Integer result = integralValue * exchangeNumber;
			productExchangeAudit.setExchangeIntegralAmount(result);
		}
		PageInfo<ProductExchangeAudit> pageInfo = new PageInfo<ProductExchangeAudit>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}

	/**
	 * 查询所有ProductExchangeAudit
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<ProductExchangeAudit> findAll(String sorter){
		return productExchangeAuditDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个ProductExchangeAudit
	 * @param id
	 * @return
	 */
	@Override
	public ProductExchangeAudit findById(Long id){
		ProductExchangeAudit productExchangeAudit = productExchangeAuditDao.findById(id);
		if (productExchangeAudit != null){
			//商品积分值（单价）
			Integer integralValue = productExchangeAudit.getIntegralValue();
			//兑换数量
			Integer exchangeNumber = productExchangeAudit.getExchangeNumber();
			Integer result = integralValue * exchangeNumber;
			productExchangeAudit.setExchangeIntegralAmount(result);
		}
		//查询mongo拿到退回原因
		List<ApplyLogs> applyLogs = applyRepository.queryApplyLogsList(TerminalTypeEnum.TERMINALTOWEB.getTid(), BaseConst.AUDIT_LOG_TYPE_VOL_BENEVOLENCE_SHOP_GOOD_EXCHANGE, id);
		if (CollectionUtils.isNotEmpty(applyLogs)){
			productExchangeAudit.setReturnReason(applyLogs.get(0).getParam());
		}
		return productExchangeAudit;
	}
	/**
	 * 保存ProductExchangeAudit
	 * @param productExchangeAudit
	 */
	@Override
	@Transactional
	public void add(ProductExchangeAudit productExchangeAudit){
		Volunteer volunteerInfo = volUtil.getVolunteerInfo();
		productExchangeAudit.setProposerId(volunteerInfo.getId());
		productExchangeAudit.setProposerName(volunteerInfo.getRealName());
		productExchangeAudit.setProposerMobile(volunteerInfo.getMobile());
		//创建时间
		productExchangeAudit.setCreateTime(new Date());
		//商品积分值（单价）
		Integer integralValue = productExchangeAudit.getIntegralValue();
		//兑换数量
		Integer exchangeNumber = productExchangeAudit.getExchangeNumber();
		Integer result = integralValue * exchangeNumber;
		productExchangeAudit.setExchangeIntegralAmount(result);
		//审核状态，默认待审核0
		productExchangeAudit.setAuditStatus(BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_TOAUDIT.getCode());
		//领取状态，默认为0 未领取
		productExchangeAudit.setGetStatus(BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_AUDIT_NO.getCode());
		// 锁，新增默认为0
		productExchangeAudit.setVersion(0);
		productExchangeAuditDao.add(productExchangeAudit);

		// 发送mq消息
		MqMessage mqMessage = new MqMessage();
		// 镇团委id
		Long topStationId = stationService.findTopStation();
		Long[] targetId = new Long[]{topStationId};
		String[] params = new String[]{volunteerInfo.getRealName()};

		MqSendMessage mqSendMessage=AppPushMessageUtil.pushOrgTaskByAlias(MessageTemplateEnum.VOL_VOLUNTEER_PRODUCT_APPLY,targetId ,params,productExchangeAudit.getId(),0,volunteerInfo.getRealName(),new Date(),null);
		sendMqPushUtil.sendMqMessage(mqSendMessage);
	}
	/**
	 * 更新ProductExchangeAudit
	 * @param productExchangeAudit
	 */
	@Override
	@Transactional
	public BaseVo update(ProductExchangeAudit productExchangeAudit, HttpServletRequest request){
		BaseVo baseVo = new BaseVo();
		ApplyLogs applyLogs = new ApplyLogs();
		UserInfo userInfo = getCurrentUserInfo();
		try {
			ProductExchangeAudit exchangeAudit = productExchangeAuditDao.findById(productExchangeAudit.getId());
			if (exchangeAudit == null){
				baseVo.setCode(ResultCode.WRONG.getCode());
				baseVo.setMsg("该条记录不存在");
				return baseVo;
			}
			if (exchangeAudit.getAuditStatus().equals(BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_PASSED.getCode())){
				baseVo.setCode(ResultCode.ADUITS_ALREADY_HANDLE.getCode());
				baseVo.setMsg(ResultCode.ADUITS_ALREADY_HANDLE.getInfo());
				return baseVo;
			}else {
				//1.查询这条审核记录的志愿者积分总数
				Volunteer volunteer = volunteerDao.findById(exchangeAudit.getProposerId());
				//2.判断积分总数和兑换积分总值，积分是否足够
				//单价
				Integer integralValue = exchangeAudit.getIntegralValue();
				//兑换数量
				Integer exchangeNumber = exchangeAudit.getExchangeNumber();
				//总积分
				Integer sum = integralValue * exchangeNumber;

				BigDecimal sumBig = new BigDecimal(sum.toString());
				BigDecimal point = volunteer.getPoint();

				//userId
				BaseVo byIdcard = sysServiceFeign.queryUserByIdcard(volunteer.getCardId());
				Long userId = null;
				if (byIdcard.getData() != null){
					String s1 = JSON.toJSONString(byIdcard.getData());
					User user = JSON.parseObject(s1,User.class);
					userId = user.getId();
				}

				if (point.compareTo(sumBig) != -1 && productExchangeAudit.getAuditStatus() == BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_PASSED.getCode()){
					//3.如果够，直接扣除
					productExchangeAudit.setUpdateUserId(userInfo.getId());
					productExchangeAudit.setUpdateTime(new Date());
					productExchangeAudit.setVersion(exchangeAudit.getVersion());
					productExchangeAuditDao.update(productExchangeAudit);
					Volunteer param = new Volunteer();
					param.setId(volunteer.getId());
					param.setPoint(volunteer.getPoint());
					param.setVersion(volunteer.getVersion());
					volunteerService.updatePointLock(param,new BigDecimal(sum.toString()));

					//商品兑换总数+兑换数量
					BenevolenceShopProduct shopProductDaoById = benevolenceShopProductDao.findById(exchangeAudit.getProductId());
					BenevolenceShopProduct benevolenceShopProduct = new BenevolenceShopProduct();
					benevolenceShopProduct.setId(shopProductDaoById.getId());
					benevolenceShopProduct.setExchangeNum(shopProductDaoById.getExchangeNum() + exchangeAudit.getExchangeNumber());
					benevolenceShopProductDao.update(benevolenceShopProduct);

					//日志
					applyLogs.setTerminalId(userInfo.getTid());
					applyLogs.setServiceId(BaseConst.AUDIT_LOG_TYPE_VOL_BENEVOLENCE_SHOP_GOOD_EXCHANGE);
					applyLogs.setFormId(productExchangeAudit.getId());
					applyLogs.setOperUserId(userInfo.getId());
					applyLogs.setOperUserName(userInfo.getRealName());
					applyLogs.setOperationName(AuditMessageTemplate.AUDIT_BENEVOLENCE_SHOP_PRODUCT);
					applyLogs.setCreateTime(new Date());
					applyLogs.setOperationCode(ApplyLogCode.AUDIT_PASS.getCode());
					applyLogs.setOperationTime(DateUtil.format(new Date()));
					applyLogs.setOperationContent(AuditMessageTemplate.AUDIT_BENEVOLENCE_SHOP_PRODUCT);
					applyRepository.addApplyLogs(applyLogs);

					// 发送mq消息
					MqMessage mqMessage = new MqMessage();
					// 推送人id,需要查询
					Long[] targetId = new Long[]{userId};
					// 镇团委
					Long topStationId = stationService.findTopStation();
					Station station = stationDao.findById(topStationId);
					// 模板参数
					String[] params = new String[]{};

					MqSendMessage mqSendMessage= AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(productExchangeAudit.getId(),MessageTemplateEnum.VOL_BENEVOLENCE_SHOP_PRODUCT_YES,targetId,params,station.getStationName(),new Date(),null);
					sendMqPushUtil.sendMqMessage(mqSendMessage);
				} else if ( productExchangeAudit.getAuditStatus() == BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_BACK.getCode()){
					productExchangeAudit.setUpdateUserId(userInfo.getId());
					productExchangeAudit.setUpdateTime(new Date());
					productExchangeAuditDao.update(productExchangeAudit);
					//已退回日志
					applyLogs.setTerminalId(userInfo.getTid());
					applyLogs.setServiceId(BaseConst.AUDIT_LOG_TYPE_VOL_BENEVOLENCE_SHOP_GOOD_EXCHANGE);
					applyLogs.setFormId(productExchangeAudit.getId());
					applyLogs.setOperUserId(userInfo.getId());
					applyLogs.setOperUserName(userInfo.getRealName());
					applyLogs.setOperationName(AuditMessageTemplate.AUDIT_BENEVOLENCE_SHOP_PRODUCT_REJECT);
					applyLogs.setCreateTime(new Date());
					applyLogs.setOperationCode(ApplyLogCode.AUDIT_BACK.getCode());
					applyLogs.setOperationTime(DateUtil.format(new Date()));
					applyLogs.setOperationContent(AuditMessageTemplate.AUDIT_BENEVOLENCE_SHOP_PRODUCT_REJECT);
					//退回原因
					applyLogs.setParam(productExchangeAudit.getReturnReason());
					applyRepository.addApplyLogs(applyLogs);

					// 发送mq消息
					MqMessage mqMessage = new MqMessage();

					// 推送人id,需要查询
					Long[] targetId = new Long[]{userId};
					// 镇团委
					Long topStationId = stationService.findTopStation();
					Station station = stationDao.findById(topStationId);
					// 模板参数
					String[] params = new String[]{productExchangeAudit.getReturnReason()};

					MqSendMessage mqSendMessage= AppPushMessageUtil.pushUserMessageWithBuzIdByAlias(productExchangeAudit.getId(),MessageTemplateEnum.VOL_BENEVOLENCE_SHOP_PRODUCT_NO,targetId,params,station.getStationName(),new Date(),null);
					sendMqPushUtil.sendMqMessage(mqSendMessage);

				}else if (point.compareTo(sumBig) == -1 && productExchangeAudit.getAuditStatus() != null){
					//4.如果不够，返回积分不足
					baseVo.setCode(ResultCode.WRONG.getCode());
					baseVo.setMsg("积分不足");
					return baseVo;
				}
			}
		}catch (Exception e){
		}
		return baseVo;
	}

	/**
	 * 领取
	 * @param productExchangeAudit
	 * @param request
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo updateGet(ProductExchangeAudit productExchangeAudit, HttpServletRequest request) {
		BaseVo baseVo = new BaseVo();
		try {
			UserInfo userInfo = getCurrentUserInfo();
			productExchangeAudit.setUpdateUserId(userInfo.getId());
			productExchangeAudit.setUpdateTime(new Date());
			productExchangeAuditDao.update(productExchangeAudit);
		}catch (Exception e){
			throwBusinessException("领取失败");
		}
		return baseVo;
	}

	@Override
	public void exportToExcel(String productName,String shopName,String proposerName,Integer getStatus,String ids, HttpServletResponse response) {
		List<ProductExchangeAudit> shops = new ArrayList<>();
		List<ProductExchangeAuditTemplate> list = new ArrayList<>();

		ProductExchangeAuditExcelVO productExchangeAuditVO = new ProductExchangeAuditExcelVO();
		productExchangeAuditVO.setProductName(productName);
		productExchangeAuditVO.setShopName(shopName);
		productExchangeAuditVO.setProposerName(proposerName);
		productExchangeAuditVO.setGetStatus(getStatus);
		if (StringUtils.isNotEmpty(ids)){
			String[] split = ids.split(",");
			List<Long> idList = new ArrayList<>();
			for (String s : split) {
				idList.add(Long.valueOf(s));
			}
			productExchangeAuditVO.setIdList(idList);
		}else {
			productExchangeAuditVO.setIdList(new ArrayList<>());
		}
		int i = 1;

		if (productExchangeAuditVO.getIdList().size() > 0){
			shops.addAll(productExchangeAuditDao.batchSelect(productExchangeAuditVO));
		}else {
			shops.addAll(productExchangeAuditDao.findByParam(productExchangeAuditVO));
		}

		for (ProductExchangeAudit shop : shops) {
			ProductExchangeAuditTemplate productExchangeAuditTemplate = new ProductExchangeAuditTemplate();
			BeanUtils.copyProperties(shop,productExchangeAuditTemplate);
			productExchangeAuditTemplate.setId(Long.valueOf(i));
			i++;
			list.add(productExchangeAuditTemplate);
		}

		try {
			String fileName = "商品兑换确认列表_"+ DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
			fileName = new String(fileName.getBytes("UTF-8"), "UTF-8")+".xls";
			response.setHeader("content-type","application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			//导出操作
			ExcelUtil.exportExcel(list,null,"商品兑换确认列表",ProductExchangeAuditTemplate.class,fileName,response);
		}catch (Exception e){
			throwBusinessException("导出错误");
		}
	}

	/**
	 * 批量设置领取状态
	 * @param productExchangeAuditVO
	 * @return
	 */
	@Override
	public BaseVo batchGet(ProductExchangeAuditVO productExchangeAuditVO) {
		UserInfo userInfo = getCurrentUserInfo();
		if (userInfo != null){
			//更新人id
			productExchangeAuditVO.setUpdateUserId(userInfo.getId());
		}
		//更新时间
		productExchangeAuditVO.setUpdateTime(new Date());
		//领取状态
		productExchangeAuditVO.setGetStatus(BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_AUDIT_YES.getCode());
		productExchangeAuditDao.batchUpdate(productExchangeAuditVO);
		BaseVo baseVo = new BaseVo();
		return baseVo;
	}

	@Override
	public BaseVo getExchangeRecord(ProductExchangeAuditVO productExchangeAuditVO) {
		PageHelper.startPage(productExchangeAuditVO.getPageNum(), productExchangeAuditVO.getPageSize());
		Volunteer volunteer = volUtil.getVolunteerInfo();
		productExchangeAuditVO.setProposerId(volunteer.getId());
		productExchangeAuditVO.setOrderBy("create_time");
		productExchangeAuditVO.setOrder("desc");
		//用于存商品id
		List<Long> ids = new ArrayList<>();
		//拿到当前志愿者的所有兑换
		List<ProductExchangeAudit> list = productExchangeAuditDao.findByConditionPage(productExchangeAuditVO);
		//如果查询到记录大于0
		if (CollectionUtils.isNotEmpty(list)){
			for (ProductExchangeAudit productExchangeAudit : list) {
				ids.add(productExchangeAudit.getProductId());
			}
			BenevolenceShopProductExcelVO benevolenceShopProductVO = new BenevolenceShopProductExcelVO();
			benevolenceShopProductVO.setIdList(ids);
			List<BenevolenceShopProduct> products = benevolenceShopProductDao.batchSelect(benevolenceShopProductVO);
			//用于存imgIds
			List<Long> imgIds = new ArrayList<>();
			for (BenevolenceShopProduct product : products) {
				imgIds.add(product.getProductImgId());
			}
			//微服务调用，获取file对象
			FileInfoVO fileInfoVO = new FileInfoVO();
			fileInfoVO.setFileIds(imgIds);
			BaseVo imgs = fileServiceFeign.findByIds(fileInfoVO);
			String s = JSON.toJSONString(imgs.getData());
			List<FileInfo> files = JSON.parseArray(s, FileInfo.class);

			//遍历，把对应的图片地址放入对象中
			for (BenevolenceShopProduct benevolenceShopProduct : products) {
				for (FileInfo file : files) {
					if (benevolenceShopProduct.getProductImgId().equals(file.getId())){
						benevolenceShopProduct.setImgPath(file.getPath());
					}
				}
			}
			//遍历，把对应的图片地址放入对象中
			for (ProductExchangeAudit productExchangeAudit : list) {
				for (BenevolenceShopProduct product : products) {
					if (productExchangeAudit.getProductId().equals(product.getId())){
						productExchangeAudit.setImgPath(product.getImgPath());
						productExchangeAudit.setProductIntroduction(product.getProductIntroduction());
					}
				}
			}
		}

		PageInfo<ProductExchangeAudit> page = new PageInfo<>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(page);
		return baseVo;
	}

	@Override
	@Transactional
	public void timingIntegral() {
		//查询所有商家
		BenevolenceShopExcelVO benevolenceShopVO = new BenevolenceShopExcelVO();
		benevolenceShopVO.setEnable(BenevolenceShopEnum.BENEVOLENCE_SHOP_ENABLE_YES.getCode());
		List<BenevolenceShop> shops = benevolenceShopDao.findByParam(benevolenceShopVO);
		//用于存shopId
		List<Long> shopIds = new ArrayList<>();
		for (BenevolenceShop shop : shops) {
			shopIds.add(shop.getId());
		}
		//批量查出所有商品兑换记录
		ProductExchangeAuditExcelVO productExchangeAuditVO = new ProductExchangeAuditExcelVO();
		productExchangeAuditVO.setIdList(shopIds);
		productExchangeAuditVO.setAuditStatus(BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_PASSED.getCode());
		List<ProductExchangeAudit> productExchangeAudits = productExchangeAuditDao.batchSelectByShopId(productExchangeAuditVO);
		//用于存储商家对应的兑换列表
		Map<Long,List<ProductExchangeAudit>> map = new HashMap<>();
		for (BenevolenceShop shop : shops) {
			List<ProductExchangeAudit> exchangeAudits = new ArrayList<>();
			for (ProductExchangeAudit productExchangeAudit : productExchangeAudits) {
				if (productExchangeAudit.getShopId().equals(shop.getId())){
					exchangeAudits.add(productExchangeAudit);
				}
			}
			map.put(shop.getId(),exchangeAudits);
		}

		//查询所有商品
		List<BenevolenceShopProduct> benevolenceShopProducts = benevolenceShopProductDao.findAll("");
		//遍历取出对应的集合，计算总积分和总数量
		for (BenevolenceShop shop : shops) {
			List<ProductExchangeAudit> exchangeAudits = map.get(shop.getId());
			if (CollectionUtils.isNotEmpty(exchangeAudits)){
				//总积分计数
				int all = 0;
				//总数量计数
				int amount = 0;
				for (ProductExchangeAudit exchangeAudit : exchangeAudits) {
					//计算总积分
					Integer integralValue = exchangeAudit.getIntegralValue();
					Integer exchangeNumber = exchangeAudit.getExchangeNumber();
					all += integralValue * exchangeNumber;
					//总数
					for (BenevolenceShopProduct benevolenceShopProduct : benevolenceShopProducts) {
						if (exchangeAudit.getProductId().equals(benevolenceShopProduct.getId())){
							amount = exchangeNumber;
							benevolenceShopProduct.setExchangeNum(amount);
						}
					}
				}
				shop.setExchangeIntegralAmount(all);
			}
		}

		//分别更新数据库

		//截取更新list 1000条一更新
		if (shops.size()<1000){
			//批量更新志愿者信息
			benevolenceShopDao.batchUpdate(shops);
		}else {
			int len = 1000;
			int size = shops.size();
			int count = size % 1000==0?size /1000:size /1000+1;
			for (int i = 0;i<count;i++){
				//截取1000条数据
				List<BenevolenceShop> sublist = shops.subList(i*len,((i + 1) * len > size ? size : len * (i + 1)));
				benevolenceShopDao.batchUpdate(sublist);
			}
		}

		//截取更新list 1000条一更新
		if (benevolenceShopProducts.size()<1000){
			//批量更新志愿者信息
			benevolenceShopProductDao.batchUpdate(benevolenceShopProducts);
		}else {
			int len = 1000;
			int size = shops.size();
			int count = size % 1000==0?size /1000:size /1000+1;
			for (int i = 0;i<count;i++){
				//截取1000条数据
				List<BenevolenceShopProduct> sublist = benevolenceShopProducts.subList(i*len,((i + 1) * len > size ? size : len * (i + 1)));
				benevolenceShopProductDao.batchUpdate(sublist);
			}
		}
	}

}
