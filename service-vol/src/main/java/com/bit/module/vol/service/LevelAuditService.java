package com.bit.module.vol.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.vol.bean.LevelAudit;
import com.bit.module.vol.vo.LevelAuditVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author chenduo
 * @create 2019-03-20 13:57
 */
public interface LevelAuditService {
    /**
     * 新增审核记录
     * @param levelAudit
     * @return
     */
    BaseVo add(LevelAudit levelAudit,HttpServletRequest request);

    /**
     * 镇团委更新审核记录
     * @param levelAudit
     * @return
     */
    BaseVo zupdate(LevelAudit levelAudit,HttpServletRequest request);
    /**
     * 服务站更新审核记录
     * @param levelAudit
     * @return
     */
    BaseVo fupdate(LevelAudit levelAudit,HttpServletRequest request);
    /**
     * 反显记录
     * @param id
     * @return
     */
    BaseVo reflect(Long id,HttpServletRequest request);

    /**
     * 镇团委审核记录分页查询
     * @param levelAuditVO
     * @return
     */
    BaseVo zlistPage(LevelAuditVO levelAuditVO);
    /**
     * 服务站审核记录分页查询
     * @param levelAuditVO
     * @return
     */
    BaseVo flistPage(LevelAuditVO levelAuditVO);

    /**
     * app查询自身星级
     * @return
     */
    BaseVo starlevel();

    /**
     * 审核记录
     * @param levelAudit
     * @return
     */
    BaseVo log(LevelAudit levelAudit);
}
