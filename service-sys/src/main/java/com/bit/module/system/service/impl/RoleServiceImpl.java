package com.bit.module.system.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Role;
import com.bit.module.system.dao.IdentityRelRoleDao;
import com.bit.module.system.dao.RoleDao;
import com.bit.module.system.dao.RoleRelResourceDao;
import com.bit.module.system.service.RoleService;
import com.bit.module.system.vo.RoleVO;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Role的Service实现类
 * @author zhangjie
 * @date 2018-12-27
 */
@Service("roleSevice")
public class RoleServiceImpl extends BaseService implements RoleService {

    private static final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private IdentityRelRoleDao identityRelRoleDao;

    @Autowired
    private RoleRelResourceDao roleRelResourceDao;
    /**
     * 根据条件查询Role，分页查询
     * @param roleVO
     * @return BaseVo
     * @author zhangjie
     * @date 2018-12-27
     */
    @Override
    public BaseVo findByConditionPage(RoleVO roleVO) {
        PageHelper.startPage(roleVO.getPageNum(), roleVO.getPageSize());
        roleVO.setOrderBy("create_time desc");
        List<Role> list = roleDao.findByConditionPage(roleVO);
        PageInfo<Role> pageInfo = new PageInfo<Role>(list);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    /**
     * 查询所有Role
     * @param sorter 排序字符串
     * @return List<Role>
     * @author zhangjie
     * @date 2018-12-27
     */
    @Override
    public List<Role> findAll(String sorter) {
        return roleDao.findAll(sorter);
    }

    /**
     * 通过主键查询单个Role
     * @param id
     * @return Role
     * @author zhangjie
     * @date 2018-12-27
     */
    @Override
    public Role findById(Long id) {
        return roleDao.findById(id);
    }

    /**
     * 保存Role
     * @param role
     * @author zhangjie
     * @date 2018-12-27
     */
    @Override
    @Transactional
    public void add(Role role) {
        UserInfo userInfo = getCurrentUserInfo();
        role.setCreateUserId(userInfo.getId());
        role.setCreateTime(new Date());
        role.setUpdateUserId(userInfo.getId());
        role.setUpdateTime(new Date());
        //自动生成rolecode
//        Integer appId = role.getAppId();
//        String rolecode = genRoleCode(appId);
//        role.setRoleCode(rolecode);
        roleDao.add(role);
    }

    private String genRoleCode(Integer appId){
        String rolecode = "";
        Role role = new Role();
        switch (appId){
            case 1:
                role.setAppId(appId);
                role.setRoleCode("DJ");
                break;
            case 2:
                role.setAppId(appId);
                role.setRoleCode("ZW");
                break;
            case 3:
                role.setAppId(appId);
                role.setRoleCode("AJ");
                break;
            case 4:
                role.setAppId(appId);
                role.setRoleCode("HB");
                break;
            case 5:
                role.setAppId(appId);
                role.setRoleCode("SQ");
                break;
            case 6:
                role.setAppId(appId);
                role.setRoleCode("CJ");
                break;
            case 7:
                role.setAppId(appId);
                role.setRoleCode("HT");
                break;
            case 8:
                role.setAppId(appId);
                role.setRoleCode("ZYZ");
                break;
            default:
                break;
        }
        if (role.getAppId()!=null && StringUtil.isNotEmpty(role.getRoleCode())){
            List<String> rolecodes = roleDao.selectLikeRoleCode(role);
            if (CollectionUtils.isNotEmpty(rolecodes)){
                List<Integer> codeInts = new ArrayList<>();
                //转换rolecode成数字
                for (String code : rolecodes) {
                    String cc = code.substring(2,code.length());
                    Integer tt = Integer.valueOf(cc);
                    codeInts.add(tt);
                }
                Integer id = 0;
                if (codeInts.size() > 0){
                    id = Collections.max(codeInts);
                    id = id + 1;
                }
                rolecode = role.getRoleCode()+String.format("%03d",id);
            }
        }
        return rolecode;
    }

    /**
     * 更新Role
     * @param role
     * @author zhangjie
     * @date 2018-12-27
     */
    @Override
    @Transactional
    public void update(Role role) {
        UserInfo userInfo = getCurrentUserInfo();
        role.setUpdateTime(new Date());
        role.setUpdateUserId(userInfo.getId());
        roleDao.update(role);
    }

    /**
     * 删除Role
     * @param id
     * @author zhangjie
     * @date 2018-12-27
     */
    @Override
    @Transactional
    public void delete(Long id) {
        roleDao.delete(id);
        roleRelResourceDao.delByRoleId(id);
    }

    /**
     * 校验名字不能重复
     * @param role
     * @author liqi
     * @return
     */
    @Override
    public BaseVo checkRoleName(Role role) {
        BaseVo baseVo = new BaseVo();
        Boolean flag;
        if (role.getId()!=null){
            Role byId = roleDao.findById(role.getId());
            String roleOldName = byId.getRoleName();
            if (roleOldName.equals(role.getRoleName())){
                flag=true;
            }else{
                int count = roleDao.checkRoleName(role.getRoleName());
                if (count>0){
                    flag=false;
                }else {
                    flag=true;
                }
            }
        }else {
            int count = roleDao.checkRoleName(role.getRoleName());
            if (count>0){
                flag=false;
            }else {
                flag=true;
            }
        }
        baseVo.setData(flag);
        return baseVo;
    }

    /**
     * 校验是否能删除  角色是否有被用或者使用
     * 大于0条的不能删除
     * @param id
     */
    @Override
    public BaseVo checkRoleNexus(Long id) {
        BaseVo baseVo = new BaseVo();
        int count=identityRelRoleDao.findCountByRoleId(id);
        baseVo.setData(count);
        return baseVo;
    }

    /**
     * 设置资源权限回显
     * @param id
     * @return
     */
    @Override
    public BaseVo findResourcesByRoleId(Long id) {
        BaseVo baseVo = new BaseVo();
        List<Long> resourceIds=roleRelResourceDao.findResourcesByRoleId(id);
        baseVo.setData(resourceIds);
        return  baseVo;
    }

    /**
     * 根据应用id 查询角色
     * @param appId
     * @return
     */
    @Override
    public BaseVo findRoleByApp(Integer appId) {
        List<Role> roleByApp = roleDao.findRoleByApp(appId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(roleByApp);
        return baseVo;
    }

    /**
     * 校验角色编号唯一性
     * @param role
     * @return
     */
    @Override
    public BaseVo checkRoleCodeUnique(Role role) {
        BaseVo baseVo = new BaseVo();
        Boolean flag;
        //更新校验
        if (role.getId() != null) {
            //原数据（旧数据）
            Role byId = roleDao.findById(role.getId());
            Integer appIdOld = byId.getAppId();
            String oldCode = byId.getRoleCode();
            if (appIdOld.equals(role.getAppId()) && oldCode.equals(role.getRoleCode())) {
                flag = true;
            } else {
                int count = roleDao.checkRoleCode(role);
                if (count > 0) {
                    flag = false;
                } else {
                    flag = true;
                }
            }
        } else {
            //保存校验
            int count = roleDao.checkRoleCode(role);
            if (count > 0) {
                flag = false;
            } else {
                flag = true;
            }
        }
        baseVo.setData(flag);
        return baseVo;
    }


}
