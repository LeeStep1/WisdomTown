package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.service.PortalContentService;
import com.bit.module.manager.vo.PortalContentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * PortalContent的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/manager/portalContent")
public class PortalContentController {
	private static final Logger logger = LoggerFactory.getLogger(PortalContentController.class);
	@Autowired
	private PortalContentService portalContentService;
	

    /**
     * 分页查询PortalContent列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PortalContentVO portalContentVO) {
    	//分页对象，前台传递的包含查询的参数
        return portalContentService.findByConditionPage(portalContentVO);
    }

    /**
     * 根据主键ID查询PortalContent
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        PortalContent portalContent = portalContentService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalContent);
		return baseVo;
    }
    
    /**
     * 新增PortalContent
     *
     * @param portalContent PortalContent实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody PortalContent portalContent,HttpServletRequest request) {
        portalContentService.add(portalContent,request);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改PortalContent
     *
     * @param portalContent PortalContent实体
     * @return
     */
    @PutMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PortalContent portalContent) {
		portalContentService.update(portalContent);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除PortalContent
     *
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id,HttpServletRequest request) {
        portalContentService.delete(id,request);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 复制
     * @param id
     * @return
     */
    @GetMapping("/copy/{id}")
    public BaseVo copy(@PathVariable(value = "id") Long id,HttpServletRequest request){
        return portalContentService.copy(id,request);
    }

    /**
     * 取消发布
     * @param id
     * @return
     */
    @GetMapping("/cancelPublish/{id}")
    public BaseVo cancelPublish(@PathVariable(value = "id") Long id,HttpServletRequest request){
        return portalContentService.cancelPublish(id,request);
    }

    /**
     * 发布
     * @param portalContent
     * @return
     */
    @PostMapping("/publish")
    public BaseVo publish(@RequestBody PortalContent portalContent,HttpServletRequest request){
        return portalContentService.publish(portalContent,request);
    }

    /**
     * @description: 根据获取内容列表(分页)
     * @author: liyang
     * @param stationId  站点ID
     * @param portalContentVo  查询条件
     * @date: 2019-05-14
     **/
    @PostMapping("/contentList/{stationId}")
    public BaseVo contentList(@PathVariable(value = "stationId") Long stationId,@RequestBody PortalContentVO portalContentVo){
        portalContentVo.setStationId(stationId);
        return portalContentService.getContentListByCategoryId(portalContentVo);
    }

    /**
     * 内容所属栏目权限校验
     * @author liyang
     * @date 2019-05-27
     * @param categoryId : 栏目ID
     * @return : BaseVo
    */
    @GetMapping("/contentCheck/{categoryId}")
    public BaseVo contentCheck(@PathVariable(value = "categoryId") Long categoryId){
        return portalContentService.getContentCheck(categoryId);
    }

    /**
     * 获取内容表所有关联关系
     * @author liyang
     * @date 2019-05-28
     * @return : BaseVo
     */
    @GetMapping("/categoryRelation")
    public BaseVo categoryRelation(){
        return portalContentService.getCategoryRelationAll();
    }


}
