package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.service.OaDepartmentService;
import com.bit.module.system.vo.OaDepartmentResultVO;
import com.bit.module.system.vo.OaDepartmentVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * OaDepartment的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/oaDepartment")
public class OaDepartmentController {
	private static final Logger logger = LoggerFactory.getLogger(OaDepartmentController.class);
	@Autowired
	private OaDepartmentService oaDepartmentService;
	

    /**
     * 分页查询OaDepartment列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody OaDepartmentVO oaDepartmentVO) {
    	//分页对象，前台传递的包含查询的参数
        return oaDepartmentService.findByConditionPage(oaDepartmentVO);
    }

    /**
     * 根据主键ID查询OaDepartment
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        OaDepartmentResultVO oaDepartment = oaDepartmentService.findResultById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(oaDepartment);
		return baseVo;
    }
    
    /**
     * 新增OaDepartment
     *
     * @param oaDepartmentResultVO
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody OaDepartmentResultVO oaDepartmentResultVO) {
        oaDepartmentService.add(oaDepartmentResultVO);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改OaDepartment
     *
     * @param oaDepartmentResultVO
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody OaDepartmentResultVO oaDepartmentResultVO) {
		oaDepartmentService.updateResult(oaDepartmentResultVO);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除OaDepartment
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        oaDepartmentService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 政务列表-不分页
     * @return
     */
    @PostMapping("/getAll")
    public BaseVo getAll() {
        List<OaDepartment> all = oaDepartmentService.findAll("");
        BaseVo baseVo = new BaseVo();
        baseVo.setData(all);
        return baseVo;
    }

    /**
     * 根据用户userId 查询
     * @param userId
     * @return
     */
    @GetMapping("/findByUserId/{userId}")
    public BaseVo findByUserId(@PathVariable(value = "userId") Long userId){
        return oaDepartmentService.findByUserId(userId);
    }

    /**
     * 校验组织编号唯一
     * @param resultVO
     * @return
     */
    @PostMapping("/checkPcodeUnique")
    public BaseVo checkPcodeUnique(@RequestBody OaDepartmentResultVO resultVO){
        return oaDepartmentService.checkPcodeUnique(resultVO);
    }

    /**
     * 校验是否存在下级
     * @param resultVO
     * @return
     */
    @PostMapping("/checkdown")
    public BaseVo checkdown(@RequestBody OaDepartmentResultVO resultVO){
        return oaDepartmentService.checkdown(resultVO);
    }

    /**
     * 查询组织结构明细
     * @param oaDepartment
     * @return
     */
    @PostMapping("/getOaDepartment")
    public BaseVo getOaDepartment(@RequestBody OaDepartment oaDepartment){
        return oaDepartmentService.findOaDepartment(oaDepartment.getId());
    }

    /**
     * 批量查询政务组织下的用户
     * @author liyang
     * @date 2019-04-04
     * @param targetIds :
     */
    @GetMapping("/getAllUserIdsByOaOrgIds/{targetIds}")
    public BaseVo getAllUserIdsByOaOrgIds(@RequestParam(value = "targetIds") Long[] targetIds){
        return oaDepartmentService.getAllUserIdsByOaOrgIds(targetIds);
    }

    /**
     * 获取党建组织下所有用户
     * @author liyang
     * @date 2019-04-09
     * @return : List<Long> ：用户ID集合
     */
    @GetMapping("/getAllUserIdsForOaOrg")
    public List<Long> getAllUserIdsForOaOrg(){
        return oaDepartmentService.getAllUserIdsForOaOrg();
    }

    /**
     * 获取社区信息
     * @return
     */
    @PostMapping("/getCommunity")
    public BaseVo getCommunity(){
		return oaDepartmentService.getCommunity();
    }

    /**
     * 根据社区id批量查询
     * @param ids
     * @return
     */
    @PostMapping("/batchSelectByIds")
    public List<OaDepartment> batchSelectByIds(@RequestBody List<Long> ids){
		return oaDepartmentService.batchSelectByIds(ids);
    }

    /**
     * 根据当前用户查询社区
     * @return
     */
    @PostMapping("/getCommunityByToken")
    public BaseVo getCommunityByToken(){
		return oaDepartmentService.getCommunityByToken();
    }
}
