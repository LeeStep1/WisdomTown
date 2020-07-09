package com.bit.module.manager.dao;

import com.bit.module.manager.bean.BaseTableInfo;
import org.apache.ibatis.annotations.Param;

/**
 * @description: 通用工具类数据库管理
 * @author: liyang
 * @date: 2019-05-13
 **/
public interface CommonUntilDao {

	/**
	 * 获取栏目表指定父栏目中最大子栏目ID和最大子栏目排名
	 * @author liyang
	 * @date 2019-05-13
	 * @param tableName :  表名
	 * @param parentId :  父ID
	 * @return : BaseTableInfo
	*/
	BaseTableInfo getMaxIdAndRankForCategorySql(@Param("parentId") Long parentId,
												@Param("tableName") String tableName,
												@Param("idLength") Integer idLength,
												@Param("navigationId") Long navigationId);

	/**
	 * 获取指定表中当前栏目最大ID和最大排名
	 * @author liyang
	 * @date 2019-05-13
	 * @param tableName :  表名
	 * @return : BaseTableInfo
	 */
	BaseTableInfo getMaxIdAndRankSql(@Param("tableName") String tableName);
}
