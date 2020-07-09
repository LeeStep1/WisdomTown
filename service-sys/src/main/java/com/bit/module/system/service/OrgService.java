package com.bit.module.system.service;

import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.bean.Organization;
import com.bit.module.system.bean.PbOrganization;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-14 16:20
 */
public interface OrgService {
    /**
     * 树形党建结构图
     * @return
     */
    List<Organization> treePbOrg();

    /**
     * 树形政务结构图
     * @return
     */
    List<Organization> treeOaOrg();

    /**
     * 党建结构集合
     * @return
     */
    List<PbOrganization> queryPbOrg();
    /**
     * 政务结构集合
     * @return
     */
    List<OaDepartment> queryOaOrg();
}
