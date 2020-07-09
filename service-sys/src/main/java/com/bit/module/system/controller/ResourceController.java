package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Resource;
import com.bit.module.system.service.ResourceService;
import com.bit.module.system.vo.ResourceVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Resource的相关请求
 * @author liqi
 */
@RestController
@RequestMapping(value = "/resource")
public class ResourceController {
	private static final Logger logger = LoggerFactory.getLogger(ResourceController.class);
	@Autowired
	private ResourceService resourceService;
	

    /**
     * 分页查询Resource列表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ResourceVO resourceVO) {
        return resourceService.findByConditionPage(resourceVO);
    }

    /**
     * 根据主键ID查询Resource
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        Resource resource = resourceService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(resource);
		return baseVo;
    }

    /**
     * 新增Resource
     * @param resource Resource实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody Resource resource) {
        resourceService.add(resource);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 修改Resource
     * @param resource Resource实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody Resource resource) {
		resourceService.update(resource);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据主键ID删除Resource （包括删除下级的所有）
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        resourceService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 条件查询所有--- 树
     * @param resource
     * @return
     */
    @PostMapping("/findAllTreeByParam")
    public BaseVo findAllTreeByParam( @RequestBody ResourceVO resource){
        List<Resource> allTree= resourceService.findAllTreeByParam(resource);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(allTree);
        return baseVo;
    }

    /**
     * 条件查询所有--- 树
     * @param resource
     * @return
     */
    @PostMapping("/findResourcesByidentity")
    public BaseVo findResourcesByidentity( @RequestBody Resource resource){
        List<Resource> allTree= resourceService.findResourcesByidentity(resource);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(allTree);
        return baseVo;
    }

    /**
     * 条件查询(用户权限)--- 树
     * @param resource
     * @return
     */
    @PostMapping("/findTreeByParam")
    public BaseVo findTreeByParam( @RequestBody Resource resource){
        List<Resource> tree= resourceService.findTreeByParam(resource);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(tree);
        return baseVo;
    }

    /**
     * 条件查询所有
     * @param resource
     * @return
     */
    @PostMapping("/findAllByParam")
    public BaseVo findAllByParam( @RequestBody Resource resource){
        List<Resource> allTree= resourceService.findAllByParam(resource);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(allTree);
        return baseVo;
    }
    /**
     * 根据角色id 终端id appid 查询 资源
     * @param resource
     * @return
     */
    @PostMapping("/findRolePermssion")
    public BaseVo findRolePermssion( @RequestBody Resource resource){
        List<Resource> allTree= resourceService.findRolePermssion(resource);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(allTree);
        return baseVo;
    }
}
