package com.bit.module.manager.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalOaLeader;
import com.bit.module.manager.service.PortalOaLeaderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * 领导介绍的相关请求
 * @author liuyancheng
 */
@RestController
@RequestMapping(value = "/manager/portalOaLeader")
public class PortalOaLeaderController {
	private static final Logger logger = LoggerFactory.getLogger(PortalOaLeaderController.class);
	@Autowired
	private PortalOaLeaderService portalOaLeaderService;

    /**
     * 根据主键ID查询PortalOaLeader
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        PortalOaLeader portalOaLeader = portalOaLeaderService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalOaLeader);
		return baseVo;
    }
    
    /**
     * 新增PortalOaLeader
     *
     * @param portalOaLeader PortalOaLeader实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PortalOaLeader portalOaLeader, HttpServletRequest request) {
        portalOaLeaderService.add(portalOaLeader,request);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改PortalOaLeader
     *
     * @param portalOaLeader PortalOaLeader实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PortalOaLeader portalOaLeader) {
		portalOaLeaderService.update(portalOaLeader);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除PortalOaLeader
     * @param id
     * @return
     */
    @DeleteMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id, HttpServletRequest request) {
        portalOaLeaderService.delete(id,request);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 获取领导介绍内容列表
     * @author liyang
     * @date 2019-05-15
     * @param staionId : 站点ID
     * @param categoryId : 栏目ID
     * @return : BaseVo
    */
    @GetMapping("/leaderIntroduce/{staionId}/{categoryId}")
    public BaseVo leaderIntroduce(@PathVariable(value = "staionId") Long staionId,
                                  @PathVariable(value = "categoryId") Long categoryId){

        return portalOaLeaderService.getLeaderIntroduce(staionId,categoryId);
    }

    /**
     * 领导介绍排序
     * @param portalOaLeaderList
     * @return
     */
    @PostMapping("/serviceSort")
    public BaseVo serviceSort(@RequestBody List<PortalOaLeader> portalOaLeaderList){
        return portalOaLeaderService.serviceSort(portalOaLeaderList);
    }

}
