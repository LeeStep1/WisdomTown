package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.Community;
import com.bit.module.cbo.service.CommunityService;
import com.bit.module.cbo.vo.CommunityVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/7/17 19:35
 **/
@RestController
@RequestMapping("/community")
public class CommunityController {

	@Autowired
	private CommunityService communityService;


	/**
	 * 批量根据社区id查询小区
	 * @param orgIds
	 * @return
	 */
	@PostMapping("/batchSelectByOrgIds")
	public List<Community> batchSelectByOrgIds(@RequestBody List<Long> orgIds){
		return communityService.batchSelectByOrgIds(orgIds);
	}

	/**
	 * app端展示所有的小区 不需要分页
	 * @return
	 */
	@PostMapping("/appListPage")
	public BaseVo appListPage(){
		return communityService.appListPage();
	}

	/**
	 * 新增小区
	 * @author liyang
	 * @date 2019-07-18
	 * @param community : 新增小区详情
	 * @return : BaseVo
	 */
	@PostMapping("/add")
	public BaseVo add(@RequestBody Community community){

		return communityService.add(community);
	}

	/**
	 * 修改小区
	 * @author liyang
	 * @date 2019-07-19
	 * @param community : 修改详情
	 * @return : BaseVo
	*/
	@PostMapping("/modify")
	public BaseVo modify(@RequestBody Community community){
		return communityService.modify(community);
	}

	/**
	 * 查询小区列表（分页）
	 * @author liyang
	 * @date 2019-07-19
	 * @param communityVO : 查询条件
	 * @return : BaseVo
	*/
	@PostMapping("/findAll")
	public BaseVo findAll(@RequestBody CommunityVO communityVO){
		return communityService.findAll(communityVO);
	}

	/**
	 * 根据ID 删除小区
	 * @author liyang
	 * @date 2019-07-19
	 * @param id : 小区ID
	 * @return : BaseVo
	*/
	@DeleteMapping("/delete/{id}")
	public BaseVo delete(@PathVariable(value = "id") Long id){
		return communityService.delete(id);
	}

	/**
	 * 根据ID查询明细
	 * @author liyang
	 * @date 2019-07-22
	 * @param id : 查询的明细
	 * @return : BaseVo
	*/
	@GetMapping("/detail/{id}")
	public BaseVo findById(@PathVariable(value = "id") Long id){
		return communityService.findById(id);
	}

	/**
	 * 获取物业公司关联小区
	 * @author liyang
	 * @date 2019-07-23
	 * @param id : 物业公司ID
	 * @return : BaseVo
	*/
	@GetMapping("/companyRelationCommunity/{id}")
	public BaseVo findCommunityByCompanyId(@PathVariable(value = "id") Long id){
		return communityService.findCommunityByCompanyId(id);
	}
}
