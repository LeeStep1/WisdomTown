package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.PbOrganization;
import com.bit.module.system.service.PbOrganizationService;
import com.bit.module.system.vo.PbOrganizationVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * PbOrganization的相关请求
 * @author liqi
 */
@RestController
@RequestMapping(value = "/pbOrganization")
public class PbOrganizationController {
	private static final Logger logger = LoggerFactory.getLogger(PbOrganizationController.class);
	@Autowired
	private PbOrganizationService pbOrganizationService;
	

    /**
     * 分页查询PbOrganization列表
     */
    /*@PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PbOrganizationVO pbOrganizationVO) {
        return pbOrganizationService.findByConditionPage(pbOrganizationVO);
    }*/

    /**
     * 根据主键ID查询PbOrganization
     * @param strId
     * @return
     */
    @GetMapping("/findById/{strId}")
    public BaseVo findById(@PathVariable(value = "strId") String strId) {
		return pbOrganizationService.findById(strId);
    }
    
    /**
     * 新增PbOrganization
     * @param pbOrganization PbOrganization实体
     * @return
     */
   /* @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody PbOrganization pbOrganization) {
        pbOrganizationService.add(pbOrganization);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }*/

    /**
     * 修改PbOrganization
     * @param pbOrganization PbOrganization实体
     * @return
     */
    /*@PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody PbOrganization pbOrganization) {
		pbOrganizationService.update(pbOrganization);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }*/
    
    /**
     * 根据主键ID删除PbOrganization
     * @param strId
     * @return
     */
    /*@GetMapping("/delete/{strId}")
    public BaseVo delete(@PathVariable(value = "strId")String strId) {
        pbOrganizationService.delete(strId);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }*/

    /**
     * 查询党政组织如果有下级组织不能删除  如果被用户用了也不能删除
     * @param strId
     * @return
     */
    @GetMapping("/checkNexusByPbId/{strId}")
    public BaseVo checkNexusByPbId(@PathVariable(value = "strId") String strId) {
        return pbOrganizationService.checkNexusByPbId(strId);
    }

    /**
     * 根据条件查询所有
     * @param pbOrganization
     * @return
     */
    @PostMapping("/findAllByParam")
    public BaseVo findAllByParam( @RequestBody PbOrganization pbOrganization) {
        List<PbOrganization> list=pbOrganizationService.findAllByParam(pbOrganization);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(list);
        return baseVo;
    }

    /**
     * 根据id查询子节点集合
     * @param strId
     * @return
     */
    @GetMapping("/findChildListByPid/{strId}")
    public BaseVo findChildListByPid(@PathVariable(value = "strId") String strId) {
        return pbOrganizationService.findChildListByPid(strId);
    }

    /**
     * 查询树 ---党建组织
     * @param pbOrganization
     * @return
     */
   /* @PostMapping("/findTreeByParam")
    public BaseVo findTreeByParam( @RequestBody PbOrganization pbOrganization) {
        List<PbOrganization> list=pbOrganizationService.findTreeByParam(pbOrganization);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(list);
        return baseVo;
    }*/

    /**
     * 校验 组织编码
     * @param pbOrganization
     * @return
     */
   /* @PostMapping("/checkPcode")
    public BaseVo checkPcode(@RequestBody PbOrganizationVO pbOrganization){
        return pbOrganizationService.checkPcode(pbOrganization);
    }*/

    /**
     * 批量查询党建组织下所有用户
     * @author liyang
     * @date 2019-04-04
     * @param targetIds : 组织ID集合
    */
    @GetMapping("/getAllUserIdsByPbOrgIds/{targetIds}")
    public BaseVo getAllUserIdsByPbOrgIds(@RequestParam(value = "targetIds") Long[] targetIds){
        return pbOrganizationService.getAllUserIdsByPbOrgIds(targetIds);
    }


    /**
     * 批量查询指定组织指定方式注册用户
     * @author liyang
     * @date 2019-04-04
     * @param messageTemplate : 组织ID集合与人员类型
    */
    @PostMapping("/getUserIdsByOrgIds")
    public BaseVo getUserIdsByOrgIds(@RequestBody() MessageTemplate messageTemplate){
        return pbOrganizationService.getUserIdsByOrgIds(messageTemplate);
    }

    /**
     * 获取党建组织下所有用户
     * @author liyang
     * @date 2019-04-09
     * @return : List<Long> ：用户ID集合
     */
    @GetMapping("/getAllUserIdsForPbOrg")
    public List<Long> getAllUserIdsForPbOrg(){
        return pbOrganizationService.getAllUserIdsForPbOrg();
    }

}
