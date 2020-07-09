package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.enumerate.BenevolenceShopEnum;
import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.bean.BenevolenceShopProduct;
import com.bit.module.vol.bean.Dict;
import com.bit.module.vol.bean.FileInfo;
import com.bit.module.vol.dao.BenevolenceShopDao;
import com.bit.module.vol.dao.BenevolenceShopProductDao;
import com.bit.module.vol.dao.ProductExchangeAuditDao;
import com.bit.module.vol.feign.FileServiceFeign;
import com.bit.module.vol.feign.SysServiceFeign;
import com.bit.module.vol.service.BenevolenceShopService;
import com.bit.module.vol.vo.*;
import com.bit.utils.DateUtil;
import com.bit.utils.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * BenevolenceShop的Service实现类
 * @author liuyancheng
 *
 */
@Service("benevolenceShopService")
public class BenevolenceShopServiceImpl extends BaseService implements BenevolenceShopService{
	
	private static final Logger logger = LoggerFactory.getLogger(BenevolenceShopServiceImpl.class);
	
	@Autowired
	private BenevolenceShopDao benevolenceShopDao;
	@Autowired
	private BenevolenceShopProductDao benevolenceShopProductDao;
	@Autowired
    private ProductExchangeAuditDao productExchangeAuditDao;
	@Autowired
	private SysServiceFeign sysServiceFeign;
	@Autowired
	private FileServiceFeign fileServiceFeign;

	/**
	 * 根据条件查询BenevolenceShop
	 * @param benevolenceShopVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(BenevolenceShopVO benevolenceShopVO){
		PageHelper.startPage(benevolenceShopVO.getPageNum(), benevolenceShopVO.getPageSize());
		benevolenceShopVO.setOrderBy("create_time");
		benevolenceShopVO.setOrder("desc");
		List<BenevolenceShop> list = benevolenceShopDao.findByConditionPage(benevolenceShopVO);
		if (CollectionUtils.isNotEmpty(list)){
			//用于存放codes
			List<String> codes = new ArrayList<>();
			//用于存放图片id
			List<Long> imgIds = new ArrayList<>();
			for (BenevolenceShop benevolenceShop : list) {
				codes.add(benevolenceShop.getDiscount());
				imgIds.add(benevolenceShop.getImgId());
			}

			//调用微服务接口查询折扣
			DictVO dictVO = new DictVO();
			dictVO.setModule("discount");
			dictVO.setCodes(codes);
			BaseVo resultVO = sysServiceFeign.findByModuleAndCodes(dictVO);
			String s = JSON.toJSONString(resultVO.getData());
			List<Dict> dicts = JSON.parseArray(s, Dict.class);
			for (BenevolenceShop shop : list) {
				for (Dict dict : dicts) {
					if (StringUtils.isNotEmpty(shop.getDiscount())){
						if (shop.getDiscount().equals(dict.getDictCode())){
							shop.setDiscount(dict.getDictName());
						}
					}
				}
			}

			if (imgIds!=null){
				//调用微服务文件接口查询图片path
				FileInfoVO fileInfoVO = new FileInfoVO();
				fileInfoVO.setFileIds(imgIds);
				BaseVo fileIdsVO = fileServiceFeign.findByIds(fileInfoVO);
				String files = JSON.toJSONString(fileIdsVO.getData());
				List<FileInfo> fileInfos = JSON.parseArray(files, FileInfo.class);
				for (BenevolenceShop benevolenceShop : list) {
					for (FileInfo fileInfo : fileInfos) {
						if (benevolenceShop.getImgId() != null){
							if (benevolenceShop.getImgId().equals(fileInfo.getId())){
								benevolenceShop.setImgPath(fileInfo.getPath());
							}
						}
					}
				}
			}
            //查询商品种类 兑换总积分
			for (BenevolenceShop benevolenceShop : list){
			    //设置商品种类
			    BenevolenceShopProductVO obj = new BenevolenceShopProductVO();
                obj.setShopId(benevolenceShop.getId());
                List<BenevolenceShopProduct> byShopId = benevolenceShopProductDao.findByShopId(obj);
                if (CollectionUtils.isNotEmpty(byShopId)){
                    benevolenceShop.setMerchandizeTypes(byShopId.size());
                }else {
                    benevolenceShop.setMerchandizeTypes(0);
                }
                //设置兑换总积分
                Integer amount = productExchangeAuditDao.countExchangeIntegralAmountByProductId(benevolenceShop.getId());
                benevolenceShop.setExchangeIntegralAmount(amount);
            }
		}


		PageInfo<BenevolenceShop> pageInfo = new PageInfo<BenevolenceShop>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有BenevolenceShop
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<BenevolenceShop> findAll(String sorter){
		return benevolenceShopDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个BenevolenceShop
	 * @param id
	 * @return
	 */
	@Override
	public BenevolenceShop findById(Long id){
		BenevolenceShop benevolenceShop = benevolenceShopDao.findById(id);
		//调用微服务文件接口查询图片path
		if (benevolenceShop.getImgId() != null){
			BaseVo baseVo = fileServiceFeign.findById(benevolenceShop.getImgId());
			String s = JSON.toJSONString(baseVo.getData());
			FileInfo fileInfo = JSON.parseObject(s, FileInfo.class);
			if (fileInfo != null){
				benevolenceShop.setImgPath(fileInfo.getPath());
			}
		}

		if (StringUtils.isNotEmpty(benevolenceShop.getDiscount())){
			//用于存放codes
			List<String> codes = new ArrayList<>();
			codes.add(benevolenceShop.getDiscount());

			//调用微服务接口查询折扣
			DictVO dictVO = new DictVO();
			dictVO.setModule("discount");
			dictVO.setCodes(codes);
			BaseVo resultVO = sysServiceFeign.findByModuleAndCodes(dictVO);
			String s = JSON.toJSONString(resultVO.getData());
			List<Dict> dicts = JSON.parseArray(s, Dict.class);
			for (Dict dict : dicts) {
				if (benevolenceShop.getDiscount().equals(dict.getDictCode())){
					benevolenceShop.setDiscount(dict.getDictName());
				}
			}
		}

		return benevolenceShop;
	}
	
	/**
	 * 保存BenevolenceShop
	 * @param benevolenceShop
	 */
	@Override
	@Transactional
	public void add(BenevolenceShop benevolenceShop){
		//校验商家名称是否重复
		if (StringUtils.isNotEmpty(benevolenceShop.getName())){
			int i = benevolenceShopDao.findByName(benevolenceShop);
			if (i > 0){
				throwBusinessException("商家名称重复，请重新输入");
			}else {
				//创建时间
				benevolenceShop.setCreateTime(new Date());
				//创建者id
				UserInfo userInfo = getCurrentUserInfo();
				if (userInfo != null){
					benevolenceShop.setCreateUserId(userInfo.getId());
				}
				//镇团委新增爱心商家，状态为1-启用状态
				benevolenceShop.setEnable(BenevolenceShopEnum.BENEVOLENCE_SHOP_ENABLE_YES.getCode());
				benevolenceShopDao.add(benevolenceShop);
			}
		}
	}
	/**
	 * 更新BenevolenceShop
	 * @param benevolenceShop
	 */
	@Override
	@Transactional
	public void update(BenevolenceShop benevolenceShop){
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo != null){
            benevolenceShop.setUpdateUserId(userInfo.getId());
        }
	    if (benevolenceShop.getEnable() != null){
	        if (benevolenceShop.getEnable().equals(BenevolenceShopEnum.BENEVOLENCE_SHOP_ENABLE_NO.getCode())){
                //1.判断当前商家下是否有在线商品
                BenevolenceShopProductVO benevolenceShopProductVO = new BenevolenceShopProductVO();
                benevolenceShopProductVO.setShopId(benevolenceShop.getId());
                List<BenevolenceShopProduct> products = benevolenceShopProductDao.findByShopId(benevolenceShopProductVO);
                if (products.size() > 0){
                    //2.如果有，提示 此商家有商品在线，不能停用
                    throwBusinessException("此商家有商品在线，不能停用");
                }else {
                    //3.如果没有，执行停用
                    benevolenceShop.setUpdateTime(new Date());
                    benevolenceShopDao.update(benevolenceShop);
                }
            }else {
                benevolenceShop.setUpdateTime(new Date());
                benevolenceShopDao.update(benevolenceShop);
            }
        }else {
			benevolenceShop.setUpdateTime(new Date());
			benevolenceShopDao.update(benevolenceShop);
		}
	}

	@Override
	public BaseVo getShopProduct(BenevolenceShopProductVO benevolenceShopProductVO) {
		BaseVo baseVo = new BaseVo();
		PageHelper.startPage(benevolenceShopProductVO.getPageNum(), benevolenceShopProductVO.getPageSize());
		benevolenceShopProductVO.setOrderBy("create_time");
		benevolenceShopProductVO.setOrder("desc");
		List<BenevolenceShopProduct> products = benevolenceShopProductDao.findByShopId(benevolenceShopProductVO);
		PageInfo<BenevolenceShopProduct> pageInfo = new PageInfo<BenevolenceShopProduct>(products);
		baseVo.setData(pageInfo);
		return baseVo;
	}

    @Override
    public void exportToExcel(String shopName,String contacts,Integer enable,String ids, HttpServletResponse response) {
		List<BenevolenceShop> shops = new ArrayList<>();
		List<BenevolenceShopTemplate> list = new ArrayList<>();
		//封装参数
		BenevolenceShopExcelVO benevolenceShopVO = new BenevolenceShopExcelVO();
		benevolenceShopVO.setName(shopName);
		benevolenceShopVO.setContacts(contacts);
		benevolenceShopVO.setEnable(enable);
		//按,拆分字符串并转为Long
		if (StringUtils.isNotEmpty(ids)){
			String[] split = ids.split(",");
			List<Long> idList = new ArrayList<>();
			for (String s : split) {
				idList.add(Long.valueOf(s));
			}
			benevolenceShopVO.setIdList(idList);
		}else {
			benevolenceShopVO.setIdList(new ArrayList<>());
		}
		//用于存字典code
		List<String> codes = new ArrayList<>();
		int i = 1;

		if (benevolenceShopVO.getIdList().size() > 0){
			//批量查询
			shops.addAll(benevolenceShopDao.batchSelect(benevolenceShopVO));
		}else {
			shops.addAll(benevolenceShopDao.findByParam(benevolenceShopVO));
		}
		for (BenevolenceShop shop : shops) {
			codes.add(shop.getDiscount());
			BenevolenceShopTemplate benevolenceShopTemplate = new BenevolenceShopTemplate();
			BeanUtils.copyProperties(shop,benevolenceShopTemplate);
			benevolenceShopTemplate.setId(Long.valueOf(i));
			i++;
			list.add(benevolenceShopTemplate);
		}

		//调用微服务接口
		DictVO dictVO = new DictVO();
		dictVO.setModule("discount");
		dictVO.setCodes(codes);
		BaseVo resultVO = sysServiceFeign.findByModuleAndCodes(dictVO);
		String s = JSON.toJSONString(resultVO.getData());
		List<Dict> dicts = JSON.parseArray(s, Dict.class);
		for (BenevolenceShopTemplate shop : list) {
			for (Dict dict : dicts) {
				if(StringUtils.isNotEmpty(shop.getDiscount())){
					if (shop.getDiscount().equals(dict.getDictCode())){
						shop.setDiscount(dict.getDictName());
					}
				}else {
					shop.setDiscount("");
				}
			}
		}

		try {
			String fileName = "爱心商家_"+ DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
			fileName = new String(fileName.getBytes("UTF-8"), "UTF-8")+".xls";
			response.setHeader("content-type","application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
			//导出操作
			ExcelUtil.exportExcel(list,null,"爱心商家",BenevolenceShopTemplate.class,fileName,response);
		}catch (Exception e){
			throwBusinessException("导出错误");
		}
    }

}
