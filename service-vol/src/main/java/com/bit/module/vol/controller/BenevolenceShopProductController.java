package com.bit.module.vol.controller;

import java.io.IOException;
import java.util.List;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bit.base.vo.SuccessVo;
import com.bit.module.vol.vo.BenevolenceShopVO;
import com.bit.module.vol.vo.ProductExchangeAuditVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bit.base.exception.BusinessException;
import com.bit.module.vol.bean.BenevolenceShopProduct;
import com.bit.module.vol.vo.BenevolenceShopProductVO;
import com.bit.module.vol.service.BenevolenceShopProductService;
import com.bit.base.vo.BaseVo;

/**
 * BenevolenceShopProduct的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/benevolenceShopProduct")
public class BenevolenceShopProductController {
	private static final Logger logger = LoggerFactory.getLogger(BenevolenceShopProductController.class);
	@Autowired
	private BenevolenceShopProductService benevolenceShopProductService;
	

    /**
     * 分页查询BenevolenceShopProduct列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody BenevolenceShopProductVO benevolenceShopProductVO) {
    	//分页对象，前台传递的包含查询的参数
        return benevolenceShopProductService.findByConditionPage(benevolenceShopProductVO);
    }

    /**
     * 根据主键ID查询BenevolenceShopProduct
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        BenevolenceShopProduct benevolenceShopProduct = benevolenceShopProductService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(benevolenceShopProduct);
		return baseVo;
    }
    
    /**
     * 新增BenevolenceShopProduct
     * 爱心商家商品管理-新增商品
     * @param benevolenceShopProduct BenevolenceShopProduct实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody BenevolenceShopProduct benevolenceShopProduct) {
        benevolenceShopProductService.add(benevolenceShopProduct);
        return new SuccessVo();
    }
    /**
     * 修改BenevolenceShopProduct
     * 爱心商家商品管理-上下架
     * @param benevolenceShopProduct BenevolenceShopProduct实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody BenevolenceShopProduct benevolenceShopProduct) {
		benevolenceShopProductService.update(benevolenceShopProduct);
        return new SuccessVo();
    }

    /**爱心商家商品管理-记录
     * 领取记录
     * @return
     */
    @PostMapping("/getRecord")
    public BaseVo getRecord(@RequestBody ProductExchangeAuditVO productExchangeAuditVO){
        return benevolenceShopProductService.getRecord(productExchangeAuditVO);
    }

    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    @PostMapping("/exportToExcel")
    public void exportToExcel(@RequestParam(value = "productName",required = false) String productName,
                              @RequestParam(value = "shopName",required = false) String shopName,
                              @RequestParam(value = "productState",required = false) Integer productState,
                              @RequestParam(value = "ids",required = false) String ids,
                              HttpServletResponse response){
        benevolenceShopProductService.exportToExcel(productName,shopName,productState,ids,response);
    }

}
