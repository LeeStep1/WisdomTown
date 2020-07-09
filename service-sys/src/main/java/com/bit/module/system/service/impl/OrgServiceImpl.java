package com.bit.module.system.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.module.system.bean.*;
import com.bit.module.system.dao.OaDepartmentDao;
import com.bit.module.system.dao.PbOrganizationDao;
import com.bit.module.system.dao.UserRelPbOrgDao;
import com.bit.module.system.feign.PbServiceFeign;
import com.bit.module.system.service.OrgService;
import com.bit.utils.RadixUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-02-14 16:22
 */
@Service("orgService")
public class OrgServiceImpl extends BaseService implements OrgService {
    @Autowired
    private PbOrganizationDao pbOrganizationDao;
    @Autowired
    private OaDepartmentDao oaDepartmentDao;
    @Autowired
    private PbServiceFeign pbServiceFeign;
    @Autowired
    private UserRelPbOrgDao userRelPbOrgDao;


    /**
     * 党组织组织树
     * @return
     */
    @Override
    public List<Organization> treePbOrg() {
        //查询所有的机构
        List<OrganizationPb> organizationPbs = pbServiceFeign.queryAll();
        List<PbOrganization> organizationList = new ArrayList<>();
        for (OrganizationPb organizationPb : organizationPbs) {
            PbOrganization org = new PbOrganization();
            BeanUtils.copyProperties(organizationPb,org);
            org.setPcode(organizationPb.getPCode());
            organizationList.add(org);
        }

        List<Organization> organizationParamList = new ArrayList<>();
        for (PbOrganization obj : organizationList) {
            Organization param = new Organization();
            BeanUtils.copyProperties(obj,param);
            param.setId(Long.valueOf(obj.getId()));
            organizationParamList.add(param);
        }
        //根节点
        List<Organization> rootList = new ArrayList<>();
        for (Organization o : organizationParamList){
            String str = RadixUtil.toFullBinaryString(o.getId());
            o.setStrId(str);
            int level = RadixUtil.getlevel(str);
            o.setLevel(level);
            if (level == 1){
                rootList.add(o);
            }
        }
        for (Organization organization : rootList) {
            organization.setChildList(getPbChild(organizationParamList,organization.getStrId(),organization.getLevel()));
        }
        return rootList;
    }

    /**
     * 政务组织树
     * @return
     */
    @Override
    public List<Organization> treeOaOrg() {
        List<OaDepartment> oaDepartmentList = oaDepartmentDao.findAll(null);
        List<Organization> organizationParamList = new ArrayList<>();
        for (OaDepartment obj : oaDepartmentList) {
            Organization param = new Organization();
            BeanUtils.copyProperties(obj,param);
            param.setId(obj.getId().longValue());
            organizationParamList.add(param);
        }
        //根节点
        List<Organization> rootList = new ArrayList<>();
        for (Organization o : organizationParamList){
//            String str = RadixUtil.toFullBinaryString(o.getId());
            o.setStrId(o.getId().toString());
            int level = RadixUtil.getOAlevel(o.getId().toString());
            o.setLevel(level);
            if (level == 1){
                rootList.add(o);
            }
        }
        for (Organization organization : rootList) {
            organization.setChildList(getOAChild(organizationParamList,organization.getStrId(),organization.getLevel()));
        }
        return rootList;
    }
    /**
     * 党建结构集合
     * @return
     */
    @Override
    public List<PbOrganization> queryPbOrg() {
        UserInfo userInfo = getCurrentUserInfo();
        Long userId = userInfo.getId();
        List<UserRelPbOrg> byUserId = userRelPbOrgDao.findByUserId(userId);
        List<Long> orgIds = new ArrayList<>();
        if (byUserId!=null && byUserId.size()>0){
            for (UserRelPbOrg userRelPbOrg : byUserId) {
                orgIds.add(userRelPbOrg.getPborgId());
            }
        }
        List<PbOrganization> pbOrganizationList = new ArrayList<>();
        //批量调feign查询组织信息
        List<OrganizationPb> organizationPbs = pbServiceFeign.batchSelectByIdsAndActive(orgIds);
        for (OrganizationPb organizationPb : organizationPbs) {
            PbOrganization pbOrganization = new PbOrganization();
            BeanUtils.copyProperties(organizationPb,pbOrganization);
            pbOrganization.setPcode(organizationPb.getPCode());
            pbOrganizationList.add(pbOrganization);
        }
//        List<PbOrganization> organizationList = pbOrganizationDao.findAllActive(userId);

        return pbOrganizationList;
    }
    /**
     * 政务结构集合
     * @return
     */
    @Override
    public List<OaDepartment> queryOaOrg() {
        UserInfo userInfo = getCurrentUserInfo();
        Long userId = userInfo.getId();
        List<OaDepartment> oaDepartmentList = oaDepartmentDao.findAllActive(userId);
        return oaDepartmentList;
    }

    /**
     * 遍历OA子组织
     * @param list
     * @param strid
     * @param level
     * @return
     */
    private List<Organization> getOAChild(List<Organization> list, String strid, Integer level){
        List<Organization> obj = new ArrayList<>();
        for (Organization organizationParam : list){
            int l = organizationParam.getLevel();
            int y=level+1;
            //如果层级等于下一层级
            if (l==y){
                //上一级的id
                String str = strid.substring(0, level * 3);
                //当前节点的id
                String pbstr = organizationParam.getStrId().substring(0, level * 3);
                if (str.equals(pbstr)){
                    obj.add(organizationParam);
                }

            }
        }
        for (Organization organizationParam : obj){
            organizationParam.setChildList(getOAChild(list,organizationParam.getStrId(),organizationParam.getLevel()));
        }
        if(obj.size()==0){
            return  null;
        }
        return obj;
    }

    /**
     * 遍历党组织子组织
     * @param list
     * @param strid
     * @param level
     * @return
     */
    private List<Organization> getPbChild(List<Organization> list, String strid, Integer level){
        List<Organization> obj = new ArrayList<>();
        for (Organization organizationParam : list){
            int l = organizationParam.getLevel();
            int y=level+1;
            //如果层级等于下一层级
            if (l==y){
                //上一级的二进制id
                String str = strid.substring(0, level * 8);
                //当前节点的二进制id
                String pbstr = organizationParam.getStrId().substring(0, level * 8);
                if (str.equals(pbstr)){
                    obj.add(organizationParam);
                }

            }
        }
        for (Organization organizationParam : obj){
            organizationParam.setChildList(getPbChild(list,organizationParam.getStrId(),organizationParam.getLevel()));
        }
        if(obj.size()==0){
            return  null;
        }
        return obj;
    }



}
