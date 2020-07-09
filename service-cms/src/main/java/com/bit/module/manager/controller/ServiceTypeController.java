package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.ServiceType;
import com.bit.module.manager.service.ServiceTypeService;
import com.bit.module.manager.vo.ServiceTypeVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * ServiceType的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/manager/serviceType")
public class ServiceTypeController {
	private static final Logger logger = LoggerFactory.getLogger(ServiceTypeController.class);
	@Autowired
	private ServiceTypeService serviceTypeService;

    /**
     * 根据主键ID查询ServiceType
     * @return
     */
    @GetMapping("/serviceTypes")
    public BaseVo query() {
        List<ServiceType> serviceTypeList = serviceTypeService.findAll();
        BaseVo baseVo = new BaseVo();
        baseVo.setData(serviceTypeList);
        return baseVo;
    }
	
    /**
     * 根据主键ID查询ServiceType
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        ServiceType serviceType = serviceTypeService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(serviceType);
		return baseVo;
    }
    
    /**
     * 新增ServiceType
     *
     * @param serviceTypeVo ServiceType实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody ServiceTypeVO serviceTypeVo, HttpServletRequest request) {
        serviceTypeService.add(serviceTypeVo, request);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改ServiceType
     *
     * @param serviceType ServiceType实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody ServiceType serviceType) {
		serviceTypeService.update(serviceType);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除ServiceType
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        serviceTypeService.delete(id,request);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据栏目ID查询服务类型
     * @param categoryId
     * @return
     */
    @GetMapping("/queryBycategoryId/{categoryId}")
    public BaseVo queryBycategoryId(@PathVariable(value = "categoryId") Long categoryId){
        return serviceTypeService.queryBycategoryId(categoryId);
    }

    /**
     * 办事指南服务类型排序
     * @param serviceTypeList
     * @return
     */
    @PostMapping("/serviceTypeSort")
    public BaseVo serviceTypeSort(@RequestBody List<ServiceType> serviceTypeList){
        return serviceTypeService.serviceTypeSort(serviceTypeList);
    }



}
