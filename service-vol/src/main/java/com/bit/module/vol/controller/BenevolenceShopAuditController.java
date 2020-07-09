package com.bit.module.vol.controller;

import java.io.IOException;
import java.util.List;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.bit.base.vo.SuccessVo;
import com.bit.module.vol.bean.BenevolenceShop;
import com.bit.module.vol.vo.BenevolenceShopVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.bit.base.exception.BusinessException;
import com.bit.module.vol.bean.BenevolenceShopAudit;
import com.bit.module.vol.vo.BenevolenceShopAuditVO;
import com.bit.module.vol.service.BenevolenceShopAuditService;
import com.bit.base.vo.BaseVo;

/**
 * BenevolenceShopAudit的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/benevolenceShopAudit")
public class BenevolenceShopAuditController {
	private static final Logger logger = LoggerFactory.getLogger(BenevolenceShopAuditController.class);
	@Autowired
	private BenevolenceShopAuditService benevolenceShopAuditService;
	

    /**
     * 分页查询BenevolenceShopAudit列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody BenevolenceShopVO benevolenceShopVO) {
    	//分页对象，前台传递的包含查询的参数
        return benevolenceShopAuditService.findByConditionPage(benevolenceShopVO);
    }

    /**
     * 根据主键ID查询BenevolenceShopAudit
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        BenevolenceShop benevolenceShop = benevolenceShopAuditService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(benevolenceShop);
		return baseVo;
    }
    
    /**
     * 新增BenevolenceShopAudit
     *
     * @param benevolenceShop
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody BenevolenceShop benevolenceShop) {
        return benevolenceShopAuditService.add(benevolenceShop);
    }
    /**
     * 修改BenevolenceShopAudit
     * 爱心商家-审核
     * @param benevolenceShop BenevolenceShopAudit实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody BenevolenceShop benevolenceShop,HttpServletRequest request) {
        return benevolenceShopAuditService.update(benevolenceShop,request);
    }
    
    /**
     * 根据主键ID删除BenevolenceShopAudit
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        benevolenceShopAuditService.delete(id);
        return new SuccessVo();
    }

}
