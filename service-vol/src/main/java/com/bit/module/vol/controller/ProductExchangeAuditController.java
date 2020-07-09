package com.bit.module.vol.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bit.base.vo.SuccessVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bit.module.vol.bean.ProductExchangeAudit;
import com.bit.module.vol.vo.ProductExchangeAuditVO;
import com.bit.module.vol.service.ProductExchangeAuditService;
import com.bit.base.vo.BaseVo;

/**
 * ProductExchangeAudit的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/productExchangeAudit")
public class ProductExchangeAuditController {
	private static final Logger logger = LoggerFactory.getLogger(ProductExchangeAuditController.class);
	@Autowired
	private ProductExchangeAuditService productExchangeAuditService;
	

    /**
     * 分页查询ProductExchangeAudit列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ProductExchangeAuditVO productExchangeAuditVO) {
    	//分页对象，前台传递的包含查询的参数
        return productExchangeAuditService.findByConditionPage(productExchangeAuditVO);
    }

    /**
     * 分页查询ProductExchangeAudit列表 兑换确认
     *
     */
    @PostMapping("/listPageConfirm")
    public BaseVo listPageConfirm(@RequestBody ProductExchangeAuditVO productExchangeAuditVO) {
        //分页对象，前台传递的包含查询的参数
        return productExchangeAuditService.listPageConfirm(productExchangeAuditVO);
    }
    /**
     * 根据主键ID查询ProductExchangeAudit
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        ProductExchangeAudit productExchangeAudit = productExchangeAuditService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(productExchangeAudit);
		return baseVo;
    }
    
    /**
     * 新增ProductExchangeAudit
     *
     * @param productExchangeAudit ProductExchangeAudit实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody ProductExchangeAudit productExchangeAudit) {
        productExchangeAuditService.add(productExchangeAudit);
        return new SuccessVo();
    }
    /**
     * 审核
     * 爱心商家商品审核-审核
     * @param productExchangeAudit ProductExchangeAudit实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody ProductExchangeAudit productExchangeAudit, HttpServletRequest request) {
        return productExchangeAuditService.update(productExchangeAudit,request);
    }
    
    /**
     * 领取
     * @param productExchangeAudit
     * @param request
     * @return
     */
    @PostMapping("/updateGet")
    public BaseVo updateGet(@RequestBody ProductExchangeAudit productExchangeAudit, HttpServletRequest request){
        return productExchangeAuditService.updateGet(productExchangeAudit,request);
    }

    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    @PostMapping("/exportToExcel")
    public void exportToExcel(@RequestParam(value = "productName",required = false) String productName,
                              @RequestParam(value = "shopName",required = false) String shopName,
                              @RequestParam(value = "proposerName",required = false) String proposerName,
                              @RequestParam(value = "getStatus",required = false) Integer getStatus,
                              @RequestParam(value = "ids",required = false) String ids,
                              HttpServletResponse response){
        productExchangeAuditService.exportToExcel(productName,shopName,proposerName,getStatus,ids,response);
    }

    /**
     * 批量设置领取状态
     * @param productExchangeAuditVO
     * @return
     */
    @PostMapping("/batchGet")
    public BaseVo batchGet(@RequestBody ProductExchangeAuditVO productExchangeAuditVO){
        return productExchangeAuditService.batchGet(productExchangeAuditVO);
    }

    /**
     * 定时查询商品统计的积分和总兑换的数量
     * 更新商户表中的 exchange_integral_amount 兑换总积分 和 商品兑换表中的 exchange_number 兑换数量
     */
    @GetMapping("/timingIntegral")
    public void timingIntegral(){
        productExchangeAuditService.timingIntegral();
    }

}
