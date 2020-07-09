package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.ResidentSuggestion;
import com.bit.module.cbo.vo.ResidentSuggestionVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description
 * @Author chenduo
 * @Date 2019/8/6 15:25
 **/
public interface SuggestDao {
	/**
	 * 新增居民意见
	 * @param residentSuggestion
	 * @return
	 */
	int add(ResidentSuggestion residentSuggestion);

	/**
	 * 修改居民意见
	 * @param residentSuggestion
	 * @return
	 */
	int modify(ResidentSuggestion residentSuggestion);

	/**
	 * web端分页查询
	 * @param residentSuggestionVO
	 * @return
	 */
	List<ResidentSuggestion> webListPage(ResidentSuggestionVO residentSuggestionVO);

	/**
	 * 单查意见记录
	 * @param id
	 * @return
	 */
	ResidentSuggestion getSuggestionById(@Param(value = "id") Long id);

	/**
	 * app 居民端 居委会端 分页查询
	 * @param residentSuggestionVO
	 * @return
	 */
	List<ResidentSuggestion> appListPage(ResidentSuggestionVO residentSuggestionVO);

	/**
	 * 删除id
	 * @param id
	 * @return
	 */
	int delById(@Param(value = "id") Long id);
}
