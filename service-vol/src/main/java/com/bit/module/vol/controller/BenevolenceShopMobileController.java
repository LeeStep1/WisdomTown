package com.bit.module.vol.controller;

import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.BenevolenceShopEnum;
import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.bean.ProductExchangeAudit;
import com.bit.module.vol.service.BenevolenceShopAuditService;
import com.bit.module.vol.service.BenevolenceShopProductService;
import com.bit.module.vol.service.BenevolenceShopService;
import com.bit.module.vol.service.ProductExchangeAuditService;
import com.bit.module.vol.vo.BenevolenceShopProductVO;
import com.bit.module.vol.vo.BenevolenceShopVO;
import com.bit.module.vol.vo.ProductExchangeAuditVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 移动端爱心商家相关接口
 * @author liuyancheng
 * @create 2019-03-27 13:05
 */
@RestController
@RequestMapping(value = "/shopMobile")
public class BenevolenceShopMobileController {

    @Autowired
    private BenevolenceShopProductService benevolenceShopProductService;
    @Autowired
    private BenevolenceShopService benevolenceShopService;
    @Autowired
    private ProductExchangeAuditService productExchangeAuditService;
    @Autowired
    private BenevolenceShopAuditService benevolenceShopAuditService;

    /**
     * 福利社商品列表
     * @param benevolenceShopProductVO
     * @return
     */
    @PostMapping("/getProduct")
    public BaseVo getProduct(@RequestBody BenevolenceShopProductVO benevolenceShopProductVO){
        benevolenceShopProductVO.setProductState(BenevolenceShopEnum.BENEVOLENCE_SHOP_PRODUCT_STATE_YES.getCode());
        return benevolenceShopProductService.findPageMobile(benevolenceShopProductVO);
    }

    /**
     * 折扣商家列表
     * @param benevolenceShopVO
     * @return
     */
    @PostMapping("/getShops")
    public BaseVo getShops(@RequestBody BenevolenceShopVO benevolenceShopVO){
        benevolenceShopVO.setEnable(BenevolenceShopEnum.BENEVOLENCE_SHOP_ENABLE_YES.getCode());
        return benevolenceShopService.findByConditionPage(benevolenceShopVO);
    }

    /**
     * 商品详情
     * @param id
     * @return
     */
    @GetMapping("/queryProduct/{id}")
    public BaseVo queryProduct(@PathVariable(value = "id") Long id){
        return benevolenceShopProductService.queryProduct(id);
    }

    /**
     * 商品审核
     * @param productExchangeAudit
     * @return
     */
    @PostMapping("/addProductAudit")
    public BaseVo addProductAudit(@RequestBody ProductExchangeAudit productExchangeAudit){
        productExchangeAuditService.add(productExchangeAudit);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 添加爱心商家,等待审核
     * @param benevolenceShop
     * @return
     */
    @PostMapping("/addShop")
    public BaseVo addShop(@RequestBody BenevolenceShop benevolenceShop){
        return benevolenceShopAuditService.add(benevolenceShop);
    }

    /**
     * 查看用户的兑换记录
     * @return
     */
    @PostMapping("/getExchangeRecord")
    public BaseVo getExchangeRecord(@RequestBody ProductExchangeAuditVO productExchangeAuditVO){
        return productExchangeAuditService.getExchangeRecord(productExchangeAuditVO);
    }
}
