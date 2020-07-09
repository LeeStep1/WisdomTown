package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.ResidentSuggestion;
import com.bit.module.cbo.vo.ResidentSuggestionVO;
import org.springframework.web.bind.annotation.RequestBody;

public interface SuggestionService {
	/**
	 * 新增居民意见
	 * @param residentSuggestion
	 * @return
	 */
	BaseVo add(ResidentSuggestion residentSuggestion);

	/**
	 * 修改居民意见
	 * @param residentSuggestion
	 * @return
	 */
	BaseVo modify(ResidentSuggestion residentSuggestion);

	/**
	 * web端分页查询
	 * @param residentSuggestionVO
	 * @return
	 */
	BaseVo webListPage(ResidentSuggestionVO residentSuggestionVO);

	/**
	 * 返显或单查意见
	 * @param id
	 * @return
	 */
	BaseVo reflectById(Long id);
	/**
	 * app 居民端 居委会端 分页查询
	 * @param residentSuggestionVO
	 * @return
	 */
	BaseVo appListPage(ResidentSuggestionVO residentSuggestionVO);

	/**
	 * 删除id
	 * @param id
	 * @return
	 */
	BaseVo delById(Long id);
}
