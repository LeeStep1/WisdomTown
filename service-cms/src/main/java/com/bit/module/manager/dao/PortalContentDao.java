package com.bit.module.manager.dao;

import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.vo.PortalContentVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PortalContent管理的Dao
 * @author liuyancheng
 *
 */
public interface PortalContentDao {
	/**
	 * 根据条件查询PortalContent
	 * @param portalContentVO
	 * @return
	 */
	public List<PortalContent> findByConditionPage(@Param(value = "portalContentVO") PortalContentVO portalContentVO);
	/**
	 * 查询所有PortalContent
	 * @return
	 */
	public List<PortalContent> findAll(@Param(value = "sorter") String sorter);
	/**
	 * 通过主键查询单个PortalContent
	 * @param id	 	 
	 * @return
	 */
	public PortalContent findById(@Param(value = "id") Long id);
	/**
	 * 保存PortalContent
	 * @param portalContent
	 */
	public void add(PortalContent portalContent);
	/**
	 * 更新PortalContent
	 * @param portalContent
	 */
	public void update(PortalContent portalContent);

	/**
	 * 取消发布专用
	 * @param portalContent
	 */
	public void cancelPublish(PortalContent portalContent);
	/**
	 * 删除PortalContent
	 * @param id
	 */
	public void delete(@Param(value = "id") Long id,@Param(value = "status") Integer status);

	/**
	 * 根据获取内容列表(分页)
	 * @author liyang
	 * @date 2019-05-14
	 * @param portalContentVo ：查询条件
	 * @return : List<PortalContent>
	 */
	List<PortalContent> getContentListByCategoryIdSql(@Param(value = "portalContentVo") PortalContentVO portalContentVo);

	/**
	 * 获得关联栏目关系
	 * @author liyang
	 * @date 2019-05-16
	 * @param categoryId : 栏目ID
	 * @return : String
	*/
	String getCategoryRelation(Long categoryId);

	/**
	 * 获取内容表所有关联关系
	 * @author liyang
	 * @date 2019-05-28
	 * @param categoryStatus : 栏目限制条件
	 * @return : List<PortalContent>
	 */
	List<PortalContent> getCategoryRelationAllSql(@Param(value = "categoryStatus") Integer categoryStatus,
												  @Param(value = "categoryType") Integer categoryType);
}
