package com.bit.module.vol.controller;


import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.bit.base.vo.SuccessVo;
import com.bit.module.vol.vo.BenevolenceShopProductVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.vo.BenevolenceShopVO;
import com.bit.module.vol.service.BenevolenceShopService;
import com.bit.base.vo.BaseVo;

/**
 * BenevolenceShop的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/benevolenceShop")
public class BenevolenceShopController {
	private static final Logger logger = LoggerFactory.getLogger(BenevolenceShopController.class);
	@Autowired
	private BenevolenceShopService benevolenceShopService;
	

    /**
     * 分页查询BenevolenceShop列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody BenevolenceShopVO benevolenceShopVO) {
    	//分页对象，前台传递的包含查询的参数
        return benevolenceShopService.findByConditionPage(benevolenceShopVO);
    }

    /**
     * 根据主键ID查询BenevolenceShop
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        BenevolenceShop benevolenceShop = benevolenceShopService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(benevolenceShop);
		return baseVo;
    }
    
    /**
     * 新增BenevolenceShop
     * 爱心商家管理-镇团委添加
     * @param benevolenceShop BenevolenceShop实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody BenevolenceShop benevolenceShop) {
        benevolenceShopService.add(benevolenceShop);
        return new SuccessVo();
    }
    /**
     * 修改BenevolenceShop
     * 爱心商家管理-停、启用爱心商家
     * 爱心商家管理-编辑爱心商家
     * @param benevolenceShop BenevolenceShop实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody BenevolenceShop benevolenceShop) {
		benevolenceShopService.update(benevolenceShop);
        return new SuccessVo();
    }
    
    /**
     * 根据商家id查询商品
     * @param benevolenceShopProductVO
     * @return
     */
    @PostMapping("/getShopProduct")
    public BaseVo getShopProduct(@RequestBody BenevolenceShopProductVO benevolenceShopProductVO){
        return benevolenceShopService.getShopProduct(benevolenceShopProductVO);
    }

    /**
     * 导出所有数据到excel
     * @param response
     * @return
     */
    @PostMapping("/exportToExcel")
    public void exportToExcel(@RequestParam(value = "shopName",required = false) String shopName,
                              @RequestParam(value = "contacts",required = false) String contacts,
                              @RequestParam(value = "enable",required = false) Integer enable ,
                              @RequestParam(value = "ids",required = false) String ids,
                              HttpServletResponse response){
        benevolenceShopService.exportToExcel(shopName,contacts,enable,ids,response);
    }
}
