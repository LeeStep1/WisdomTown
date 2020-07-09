package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Identity;
import com.bit.module.system.service.IdentityService;
import com.bit.module.system.vo.IdentityVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Identity的相关请求
 * @author liqi
 */
@RestController
@RequestMapping(value = "/identity")
public class IdentityController {
	private static final Logger logger = LoggerFactory.getLogger(IdentityController.class);
	@Autowired
	private IdentityService identityService;
	

    /**
     * 业务分页    查询Identity列表   真正分页
     */
    @PostMapping("/findRealPage")
    public BaseVo findRealPage(@RequestBody IdentityVO identityVO) {
        return identityService.findRealPage(identityVO);
    }

    /**
     * 分页查询Identity列表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody IdentityVO identityVO) {
        return identityService.findByConditionPage(identityVO);
    }

    /**
     * 根据主键ID查询Identity
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        Identity identity = identityService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(identity);
		return baseVo;
    }

    /**
     * 根据主键ID查询Identity  业务查询
     * @param id
     * @return
     */
    @GetMapping("/findBykey/{id}")
    public BaseVo findBykey(@PathVariable(value = "id") Long id) {
        Identity identity = identityService.findBykey(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(identity);
		return baseVo;
    }

    /**
     * 新增Identity
     * @param identity Identity实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody Identity identity) {
        identityService.add(identity);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 修改Identity
     * @param identity Identity实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody Identity identity) {
		identityService.update(identity);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除Identity
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        identityService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 校验删除  大于0不能删除  ---查询和身份有关系的数据
     * @param id
     * @return
     */
    @GetMapping("/checkCountNexus/{id}")
    public BaseVo checkCountNexus(@PathVariable(value = "id") Long id) {
        return identityService.checkCountNexus(id);
    }

    /**
     * 根据appid查询
     * @param appId
     * @return
     */
    @GetMapping("/findIdentListByAppId/{appId}")
    public BaseVo findIdentListByAppId(@PathVariable(value = "appId") Long appId) {
        List<Identity> list=identityService.findIdentListByAppId(appId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(list);
        return baseVo;
    }

    /**
     * 条件查询全部
     * @param identity Identity实体
     * @return
     */
    @PostMapping("/findAllByParam")
    public BaseVo findAllByParam(@RequestBody Identity identity) {
        List<Identity> allByParam = identityService.findAllByParam(identity);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(allByParam);
        return baseVo;
    }

    /**
     * 校验默认身份  --一个应用下只有一个默认身份
     * @param identity Identity实体
     * @return
     */
    @PostMapping("/checkAcquiesce")
    public BaseVo checkAcquiesce(@RequestBody Identity identity) {
       return identityService.checkAcquiesce(identity);
    }
    /**
     * 校验身份名称唯一性
     * @param identity Identity实体
     * @return
     */
    @PostMapping("/checkNameUnique")
    public BaseVo checkNameUnique(@RequestBody Identity identity) {
        return identityService.checkNameUnique(identity);
    }

    /**
     * 根据appID查询默认身份
     * @param appId
     * @return
     */
    @PostMapping("/defaultIdentityByAppId/{appId}")
    public BaseVo defaultIdentityByAppId(@PathVariable(value = "appId") Long appId){
        Identity identity=identityService.getDefaultIdentityByAppId(appId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(identity);
        return baseVo;

    }
}
