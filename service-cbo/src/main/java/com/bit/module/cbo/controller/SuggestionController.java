package com.bit.module.cbo.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.ResidentSuggestion;
import com.bit.module.cbo.service.SuggestionService;
import com.bit.module.cbo.vo.ResidentSuggestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/6 15:10
 **/
@RestController
@RequestMapping("/suggest")
public class SuggestionController {

	@Autowired
	private SuggestionService suggestionService;


	/**
	 * 新增意见
	 * @param residentSuggestion
	 * @return
	 */
	@PostMapping("/add")
	public BaseVo add(@RequestBody @Valid ResidentSuggestion residentSuggestion){
		return suggestionService.add(residentSuggestion);
	}

	/**
	 * 修改意见
	 * @param residentSuggestion
	 * @return
	 */
	@PostMapping("/modify")
	public BaseVo modify(@RequestBody ResidentSuggestion residentSuggestion){
		return suggestionService.modify(residentSuggestion);
	}

	/**
	 * web端分页插查询
	 * @param residentSuggestionVO
	 * @return
	 */
	@PostMapping("/webListPage")
	public BaseVo webListPage(@RequestBody ResidentSuggestionVO residentSuggestionVO){
		return suggestionService.webListPage(residentSuggestionVO);
	}

	/**
	 * 返显或单查意见
	 * @param id
	 * @return
	 */
	@GetMapping("/reflectById/{id}")
	public BaseVo reflectById(@PathVariable(value = "id")Long id){
		return suggestionService.reflectById(id);
	}

	/**
	 * app 居民端 居委会端 分页查询
	 * @param residentSuggestionVO
	 * @return
	 */
	@PostMapping("/appListPage")
	public BaseVo appListPage(@RequestBody ResidentSuggestionVO residentSuggestionVO){
		return suggestionService.appListPage(residentSuggestionVO);
	}

	/**
	 * 删除意见
	 * @param id
	 * @return
	 */
	@GetMapping("/del/{id}")
	public BaseVo delById(@PathVariable(value = "id")Long id){
		return suggestionService.delById(id);
	}

}
