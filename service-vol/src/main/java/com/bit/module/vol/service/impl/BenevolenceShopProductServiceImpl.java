package com.bit.module.vol.service.impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.enumerate.BenevolenceShopEnum;
import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.bean.BenevolenceShopProduct;
import com.bit.module.vol.bean.FileInfo;
import com.bit.module.vol.bean.ProductExchangeAudit;
import com.bit.module.vol.dao.BenevolenceShopDao;
import com.bit.module.vol.dao.BenevolenceShopProductDao;
import com.bit.module.vol.dao.ProductExchangeAuditDao;
import com.bit.module.vol.feign.FileServiceFeign;
import com.bit.module.vol.service.BenevolenceShopProductService;
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
 * BenevolenceShopProduct的Service实现类
 *
 * @author liuyancheng
 */
@Service("benevolenceShopProductService")
public class BenevolenceShopProductServiceImpl extends BaseService implements BenevolenceShopProductService {

    private static final Logger logger = LoggerFactory.getLogger(BenevolenceShopProductServiceImpl.class);

    @Autowired
    private BenevolenceShopProductDao benevolenceShopProductDao;
    @Autowired
    private ProductExchangeAuditDao productExchangeAuditDao;
    @Autowired
    private BenevolenceShopDao benevolenceShopDao;
    @Autowired
    private FileServiceFeign fileServiceFeign;

    /**
     * 根据条件查询BenevolenceShopProduct
     *
     * @param benevolenceShopProductVO
     * @return
     */
    @Override
    public BaseVo findByConditionPage(BenevolenceShopProductVO benevolenceShopProductVO) {
        PageHelper.startPage(benevolenceShopProductVO.getPageNum(), benevolenceShopProductVO.getPageSize());
        benevolenceShopProductVO.setOrderBy("create_time");
        benevolenceShopProductVO.setOrder("desc");
        List<BenevolenceShopProduct> list = benevolenceShopProductDao.findByConditionPage(benevolenceShopProductVO);
        PageInfo<BenevolenceShopProduct> pageInfo = new PageInfo<BenevolenceShopProduct>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有BenevolenceShopProduct
     *
     * @param sorter 排序字符串
     * @return
     */
    @Override
    public List<BenevolenceShopProduct> findAll(String sorter) {
        return benevolenceShopProductDao.findAll(sorter);
    }

    /**
     * 通过主键查询单个BenevolenceShopProduct
     *
     * @param id
     * @return
     */
    @Override
    public BenevolenceShopProduct findById(Long id) {
        return benevolenceShopProductDao.findById(id);
    }

    /**
     * 保存BenevolenceShopProduct
     *
     * @param benevolenceShopProduct
     */
    @Override
    @Transactional
    public void add(BenevolenceShopProduct benevolenceShopProduct) {
        benevolenceShopProduct.setCreateTime(new Date());
        UserInfo userInfo = getCurrentUserInfo();
        benevolenceShopProduct.setCreateUserId(userInfo.getId());
        //默认数量为0
        benevolenceShopProduct.setExchangeNum(0);
        //上架状态
        benevolenceShopProduct.setProductState(BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_STATE_YES.getCode());
        benevolenceShopProductDao.add(benevolenceShopProduct);
    }

    /**
     * 更新BenevolenceShopProduct
     *
     * @param benevolenceShopProduct
     */
    @Override
    @Transactional
    public void update(BenevolenceShopProduct benevolenceShopProduct) {
        benevolenceShopProduct.setUpdateTime(new Date());
        UserInfo userInfo = getCurrentUserInfo();
        benevolenceShopProduct.setCreateUserId(userInfo.getId());
//		benevolenceShopProduct.setUpdateUserId(1L);
        benevolenceShopProductDao.update(benevolenceShopProduct);
    }

    /**
     * 领取记录
     *
     * @param productExchangeAuditVO
     * @return
     */
    @Override
    public BaseVo getRecord(ProductExchangeAuditVO productExchangeAuditVO) {
        PageHelper.startPage(productExchangeAuditVO.getPageNum(), productExchangeAuditVO.getPageSize());
        productExchangeAuditVO.setOrderBy("create_time desc");
        List<ProductExchangeAudit> list = productExchangeAuditDao.findByShopId(productExchangeAuditVO);
        PageInfo<ProductExchangeAudit> pageInfo = new PageInfo<>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public void exportToExcel(String productName, String shopName, Integer productState, String ids, HttpServletResponse response) {
        List<BenevolenceShopProduct> shops = new ArrayList<>();
        List<BenevolenceShopProductTemplate> list = new ArrayList<>();
        BenevolenceShopProductExcelVO benevolenceShopProductVO = new BenevolenceShopProductExcelVO();
        benevolenceShopProductVO.setProductName(productName);
        benevolenceShopProductVO.setShopName(shopName);
        benevolenceShopProductVO.setProductState(productState);
        if (StringUtils.isNotEmpty(ids)) {
            String[] split = ids.split(",");
            List<Long> idList = new ArrayList<>();
            for (String s : split) {
                idList.add(Long.valueOf(s));
            }
            benevolenceShopProductVO.setIdList(idList);
        } else {
            benevolenceShopProductVO.setIdList(new ArrayList<>());
        }
        int i = 1;

        if (CollectionUtils.isNotEmpty(benevolenceShopProductVO.getIdList())) {
            shops.addAll(benevolenceShopProductDao.batchSelect(benevolenceShopProductVO));
        } else {
            shops.addAll(benevolenceShopProductDao.findByParam(benevolenceShopProductVO));
        }

        for (BenevolenceShopProduct shop : shops) {
            BenevolenceShopProductTemplate benevolenceShopProductTemplate = new BenevolenceShopProductTemplate();
            BeanUtils.copyProperties(shop, benevolenceShopProductTemplate);
            benevolenceShopProductTemplate.setId(Long.valueOf(i));
            i++;
            list.add(benevolenceShopProductTemplate);
        }

        try {
            String fileName = "商品列表_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
            fileName = new String(fileName.getBytes("UTF-8"), "UTF-8") + ".xls";
            response.setHeader("content-type", "application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName);
            //导出操作
            ExcelUtil.exportExcel(list, null, "商品列表", BenevolenceShopProductTemplate.class, fileName, response);
        } catch (Exception e) {
            throwBusinessException("导出错误");
        }
    }

    @Override
    public BaseVo queryProduct(Long id) {
        BenevolenceShopProduct benevolenceShopProduct = benevolenceShopProductDao.findById(id);
        BenevolenceShop benevolenceShop = benevolenceShopDao.findById(benevolenceShopProduct.getShopId());

        //调用微服务接口，获取文件路径
        BaseVo feign = fileServiceFeign.findById(benevolenceShopProduct.getProductImgId());
        String s = JSON.toJSONString(feign.getData());
        FileInfo fileInfo = JSON.parseObject(s, FileInfo.class);

        ProductMobileResultVO productMobileResultVO = new ProductMobileResultVO();
        productMobileResultVO.setId(benevolenceShopProduct.getId());
        productMobileResultVO.setShopId(benevolenceShop.getId());
        productMobileResultVO.setShopName(benevolenceShop.getName());
        productMobileResultVO.setShopAddress(benevolenceShop.getAddress());
        if (fileInfo != null) {
            productMobileResultVO.setProductImgPath(fileInfo.getPath());
        }
        productMobileResultVO.setIntegralValue(benevolenceShopProduct.getIntegralValue());
        productMobileResultVO.setProductIntroduction(benevolenceShopProduct.getProductIntroduction());
        productMobileResultVO.setExchangeExplain(benevolenceShopProduct.getExchangeExplain());
        productMobileResultVO.setProductName(benevolenceShopProduct.getProductName());

        BaseVo baseVo = new BaseVo();
        baseVo.setData(productMobileResultVO);
        return baseVo;
    }

    @Override
    public BaseVo findPageMobile(BenevolenceShopProductVO benevolenceShopProductVO) {
        PageHelper.startPage(benevolenceShopProductVO.getPageNum(), benevolenceShopProductVO.getPageSize());
        benevolenceShopProductVO.setOrderBy("create_time");
        benevolenceShopProductVO.setOrder("desc");
        List<BenevolenceShopProduct> list = benevolenceShopProductDao.findByConditionPage(benevolenceShopProductVO);

        List<Long> imgIds = new ArrayList<>();
        for (BenevolenceShopProduct benevolenceShopProduct : list) {
            imgIds.add(benevolenceShopProduct.getProductImgId());
        }

        FileInfoVO fileInfoVO = new FileInfoVO();
        fileInfoVO.setFileIds(imgIds);
        BaseVo imgs = fileServiceFeign.findByIds(fileInfoVO);
        if (imgs.getData() != null) {
            String s = JSON.toJSONString(imgs.getData());
            List<FileInfo> files = JSON.parseArray(s, FileInfo.class);

            for (BenevolenceShopProduct benevolenceShopProduct : list) {
                for (FileInfo file : files) {
                    if (benevolenceShopProduct.getProductImgId() != null && benevolenceShopProduct.getProductImgId().equals(file.getId())) {
                        benevolenceShopProduct.setImgPath(file.getPath());
                    }
                }
            }
        }

        PageInfo<BenevolenceShopProduct> pageInfo = new PageInfo<BenevolenceShopProduct>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

}
