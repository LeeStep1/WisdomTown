package com.bit.module.system.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.OrganizationPb;
import com.bit.module.system.bean.PbOrganization;
import com.bit.module.system.vo.PbOrganizationVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * PbOrganization的Service
 * @author liqi
 */
public interface PbOrganizationService {
	/**
	 * 根据条件查询PbOrganization
	 * @param pbOrganizationVO
	 * @return
	 */
	BaseVo findByConditionPage(PbOrganizationVO pbOrganizationVO);

	/**
	 * 查询所有PbOrganization
	 * @param sorter 排序字符串
	 * @return
	 */
	List<PbOrganization> findAll(String sorter);

	/**
	 * 通过主键查询单个PbOrganization
	 * @param strId
	 * @return
	 */
	BaseVo findById(String strId);

	/**
	 * 保存PbOrganization
	 * @param pbOrganization
	 */
	//void add(PbOrganization pbOrganization);

	/**
	 * 更新PbOrganization
	 * @param pbOrganization
	 */
	//void update(PbOrganization pbOrganization);

	/**
	 * 删除PbOrganization
	 * @param strId
	 */
	//void delete(String strId);

	/**
	 * 批量删除PbOrganization
	 * @param ids
	 */
	//void batchDelete(List<Long> ids);

	/**
	 * 查询党政组织如果有下级组织不能删除  如果被用户用了也不能删除
	 * @param id
	 * @return
	 */
	BaseVo checkNexusByPbId(String id);

	/**
	 * 查询
	 * @param strId
	 * @return
	 */
	BaseVo findChildListByPid(String strId);

	/**
	 * 根据参数查询
	 * @param pbOrganization
	 * @return
	 */
	List<PbOrganization> findAllByParam(PbOrganization pbOrganization);

	/**
	 * 查询树
	 * @param pbOrganization
	 * @return
	 */
	//List<PbOrganization> findTreeByParam(PbOrganization pbOrganization);

	/**
	 * 校验组织编码
	 * @param pbOrganization
	 * @return
	 */
    //BaseVo checkPcode(PbOrganizationVO pbOrganization);

    /**
     * 批量查询党建组织下所有用户
     * @author liyang
     * @date 2019-04-04
     * @param targetIds :
    */
	BaseVo getAllUserIdsByPbOrgIds(Long[] targetIds);

	/**
	 * 批量查询指定组织指定方式注册用户
	 * @author liyang
	 * @date 2019-04-04
	 * @param messageTemplate : 组织ID集合与人员类型
	*/
	BaseVo getUserIdsByOrgIds(MessageTemplate messageTemplate);

	/**
	 * 获取党建组织下所有用户
	 * @author liyang
	 * @date 2019-04-09
	 * @return : List<Long> ：用户ID集合
	 */
	List<Long> getAllUserIdsForPbOrg();

    /**
     * 根据组织id查询党组织信息 feign调用
     * @param id
     * @return
     */
    OrganizationPb findPbOrgById(@Param(value = "id") Long id);
}
