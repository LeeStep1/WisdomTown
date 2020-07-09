package com.bit.module.manager.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.vo.PortalContentVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
/**
 * PortalContent的Service
 * @author liuyancheng
 */
public interface PortalContentService {
	/**
	 * 根据条件查询PortalContent
	 * @param portalContentVO
	 * @return
	 */
	BaseVo findByConditionPage(PortalContentVO portalContentVO);
	/**
	 * 查询所有PortalContent
	 * @param sorter 排序字符串
	 * @return
	 */
	List<PortalContent> findAll(String sorter);
	/**
	 * 通过主键查询单个PortalContent
	 * @param id
	 * @return
	 */
	PortalContent findById(Long id);

	/**
	 * 保存PortalContent
	 * @param portalContent
	 */
	void add(PortalContent portalContent,HttpServletRequest request);
	/**
	 * 更新PortalContent
	 * @param portalContent
	 */
	void update(PortalContent portalContent);
	/**
	 * 删除PortalContent
	 * @param id
	 */
	void delete(Long id,HttpServletRequest request);

	/**
	 * 复制
	 * @param id
	 * @return
	 */
	BaseVo copy(Long id,HttpServletRequest request);

	/**
	 * 取消发布
	 * @param id
	 * @return
	 */
	BaseVo cancelPublish(Long id,HttpServletRequest request);

	/**
	 * 发布
	 * @param portalContent
	 * @return
	 */
    BaseVo publish(PortalContent portalContent,HttpServletRequest request);

	/**
	 * 根据获取内容列表(分页)
	 * @author liyang
	 * @date 2019-05-14
	 * @param portalContentVo ：查询条件
	 * @return : BaseVo
	 */
	BaseVo getContentListByCategoryId(PortalContentVO portalContentVo);

	/**
	 * 内容所属栏目权限校验
	 * @author liyang
	 * @date 2019-05-27
	 * @param categoryId : 栏目ID
	 * @return : BaseVo
	 */
	BaseVo getContentCheck(Long categoryId);

	/**
	 * 获取内容表所有关联关系
	 * @author liyang
	 * @date 2019-05-28
	 * @return : BaseVo
	*/
	BaseVo getCategoryRelationAll();

}
