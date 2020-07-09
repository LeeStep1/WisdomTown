package com.bit.module.system.dao;

import java.util.List;

import com.bit.module.system.bean.Identity;
import org.apache.ibatis.annotations.Param;
import com.bit.module.system.bean.Dict;
import com.bit.module.system.vo.DictVO;
import org.springframework.stereotype.Repository;

/**
 * Dict管理的Dao
 * @author zhangjie
 * @date 2018-12-28
 */
@Repository
public interface DictDao {
	/**
	 * 根据条件查询Dict
	 * @param dictVO
	 * @return
	 */
	List<Dict> findByConditionPage(DictVO dictVO);

	/**
	 * 通过主键查询单个Dict
	 * @param id	 	 
	 * @return
	 */
	Dict findById(@Param(value = "id") Long id);

	/**
	 * 保存Dict
	 * @param dict
	 */
	void add(Dict dict);

	/**
	 * 更新Dict
	 * @param dict
	 */
	void update(Dict dict);

	/**
	 * 删除Dict
	 * @param id
	 */
	void delete(@Param(value = "id") Long id);

	/**
	 * 根据模块查询字典
	 * @param module
	 * @return
	 */
    List<Dict> findByModule(@Param(value = "module") String module);

	/**
	 * 根据module 和 dictcode 查询结果
	 * @param dict
	 * @return
	 */
	Dict findByModuleAndDictCode(Dict dict);

	/**
	 * 根据module和codes查询
	 * @param dictVO
	 * @return
	 */
    List<Dict> findByModuleAndCodes(DictVO dictVO);
	/**
	 * 根据模块名称集合批量查询字典表
	 * @param dict
	 * @return
	 */
    List<Dict> findByModules(Dict dict);
}
