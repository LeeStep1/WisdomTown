package com.bit.module.cbo.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.cbo.bean.ResidentApplyBase;
import com.bit.module.cbo.bean.ResidentApplyProgress;
import com.bit.module.cbo.vo.ResidentApplyBaseVO;
import com.bit.module.cbo.vo.ServiceInformationVO;

import javax.servlet.http.HttpServletResponse;

/**
 * @description: 台账管理相关service
 * @author: liyang
 * @date: 2019-08-09
 **/
public interface ResidentApplyBaseService {

    /**
     * 新增台账申请
     * @author liyang
     * @date 2019-08-09
     * @param residentApplyBase : 新增明细
     * @return : BaseVo
     */
    BaseVo add(ResidentApplyBase residentApplyBase);

    /**
     * 获取办事指南所有类别
     * @author liyang
     * @date 2019-08-12
     * @param status : 状态 0 停用 1启用 2 草稿
     * @return : BaseVo
     */
    BaseVo getGuide(Integer status);

    /**
     * 根据办事指南类别获取所有启用的事项
     * @author liyang
     * @date 2019-08-12
     * @param type : 数据类别:1 类别，0事项
     * @param pid : 办事指南类别ID
     * @param status  : 状态 0 停用 1启用 2 草稿
     * @return : BaseVo
     */
    BaseVo getGuideItems(Integer type,Long pid,Integer status);

    /**
     * 获取办事指南台账列表
     * @author liyang
     * @date 2019-08-12
     * @param residentApplyBaseVO : 查询条件
     * @return : BaseVo
     */
    BaseVo findAll(ResidentApplyBaseVO residentApplyBaseVO);

    /**
     * 根据ID查询台账明细
     * @author liyang
     * @date 2019-08-12
     * @param id : id
     * @return : BaseVo
    */
    BaseVo detail(Long id);

    /**
     * 台账办理步揍展示
     * @author liyang
     * @date 2019-08-13
     * @param applyId : 申请台账ID
     * @return : BaseVo
     */
    BaseVo itemsStep(Long applyId);

    /**
     * 审核办理流程
     * @author liyang
     * @date 2019-08-13
     * @param residentApplyProgress : 修改的明细
     * @return : BaseVo
     */
    BaseVo auditItems(ResidentApplyProgress residentApplyProgress);

    /**
     * 台账补充业务信息
     * @author liyang
     * @date 2019-08-14
     * @param extendType : 关联的扩展信息关联字典表中扩展信息类型：1 低保申请、2 居家养老
     * @param serviceInformationVO : 业务信息详情
     * @return : BaseVo
     */
    BaseVo addServiceInformation(ServiceInformationVO serviceInformationVO,Integer extendType);

	/**
	 * 台账进度反馈 修改附件 接口
	 * @param residentApplyBaseVO
	 * @return
	 */
    BaseVo updateApplyBaseInfo(ResidentApplyBaseVO residentApplyBaseVO);
	/**
	 * 返显台账的补充业务信息的附件
	 * @param id
	 * @return
	 */
	BaseVo reflectBase(Long id);

    /**
     * 导出办事指南列表
     * @author liyang
     * @date 2019-12-09
     * @param residentApplyBase : 过滤条件
     */
	void exportToExcel(ResidentApplyBase residentApplyBasem, HttpServletResponse response);

}
