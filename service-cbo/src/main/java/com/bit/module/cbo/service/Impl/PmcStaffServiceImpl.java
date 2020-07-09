package com.bit.module.cbo.service.Impl;


import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.PmcStaffStatusEnum;
import com.bit.module.cbo.bean.PmcCompany;
import com.bit.module.cbo.bean.PmcStaff;
import com.bit.module.cbo.bean.PmcStaffRelCommunity;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.PmcStaffDao;
import com.bit.module.cbo.dao.PmcStaffRelCommunityDao;
import com.bit.module.cbo.service.PmcStaffService;
import com.bit.module.cbo.vo.PmcStaffRelCommunityVO;
import com.bit.module.cbo.vo.PmcStaffVO;
import com.bit.utils.TokenUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.bit.common.enumerate.PmcStaffRoleTypeEnum.CREATE_TYPE_APP;
import static com.bit.common.enumerate.PmcStaffRoleTypeEnum.CREATE_TYPE_WEB;
import static com.bit.common.enumerate.PmcStaffRoleTypeEnum.COMMUNITYMODIFYFLG_TRUE;
import static com.bit.common.enumerate.PmcStaffStatusEnum.PMC_STAFF_STATUS_ACTIVE;

/**
 * @description: 物业公司员工相关实现
 * @author: liyang
 * @date: 2019-07-18
 **/
@Service
public class PmcStaffServiceImpl extends BaseService implements PmcStaffService {

    /**
     * 物业公司员工相关dao
     */
    @Autowired
    private PmcStaffDao pmcStaffDao;

    /**
     * 员工小区关系表相关dao
     */
    @Autowired
    private PmcStaffRelCommunityDao pmcStaffRelCommunityDao;

    /**
     * 小区相关dao
     */
    @Autowired
    private CommunityDao communityDao;

    /**
     * token 相关工具类
     */
    @Autowired
    private TokenUtil tokenUtil;

    /**
     * 新增物业人员
     * @author liyang
     * @date 2019-07-19
     * @param pmcStaff : 物业员工信息
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo add(PmcStaff pmcStaff) {
        UserInfo userInfo = getCurrentUserInfo();

        //创建时间
        pmcStaff.setCreateTime(new Date());

        //创建人
        pmcStaff.setCreateUserId(userInfo.getId());

        //账号来源
        Integer tid = userInfo.getTid();
        if(tid.equals(CREATE_TYPE_WEB.getCode())){
            pmcStaff.setCreateType(CREATE_TYPE_WEB.getCode());
        }else {
            pmcStaff.setCreateType(CREATE_TYPE_APP.getCode());
        }

        //账号状态
        pmcStaff.setStatus(PMC_STAFF_STATUS_ACTIVE.getCode());

        //去重检查
        int count = pmcStaffDao.checkPmcStaffExist(pmcStaff.getMobile().trim());
        if(Const.COUNT.equals(count)){
            //先更新物业人员
            pmcStaffDao.insertPmcstaff(pmcStaff);

            //查询小区所属社区
            List<PmcStaffRelCommunity> pmcStaffRelCommunityList = communityDao.findOrgIdsByCommunityIdSql(pmcStaff.getCommunityIds());
            pmcStaffRelCommunityList.forEach(pmcStaffRelCommunity->pmcStaffRelCommunity.setStaffId(pmcStaff.getId()));

            //再更新所属小区
            pmcStaffRelCommunityDao.add(pmcStaffRelCommunityList);
        }else {
            throwBusinessException("手机号码重复，请重新输入！");
        }

        return successVo();
    }

    /**
     * 修改物业公司员工信息
     * @author liyang
     * @date 2019-07-19
     * @param pmcStaff : 修改详情
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo modify(PmcStaff pmcStaff) {

        UserInfo userInfo = getCurrentUserInfo();

        //去重检查
        int count = pmcStaffDao.modifyCheckPmcStaffExist(pmcStaff.getMobile().trim(),pmcStaff.getId());
        if(Const.COUNT.equals(count)){
            //判断是否修改过小区
            if(pmcStaff.getCommunityModifyFlg().equals(COMMUNITYMODIFYFLG_TRUE.getCode())){

                //如果修改过小区，先删除原有小区关联表
                pmcStaffRelCommunityDao.deleteByPmcStaffId(pmcStaff.getId());

                //查询小区所属社区
                List<PmcStaffRelCommunity> pmcStaffRelCommunityList = communityDao.findOrgIdsByCommunityIdSql(pmcStaff.getCommunityIds());
                pmcStaffRelCommunityList.forEach(pmcStaffRelCommunity->pmcStaffRelCommunity.setStaffId(pmcStaff.getId()));

                //再更新所属小区
                pmcStaffRelCommunityDao.add(pmcStaffRelCommunityList);

                //修改物业员工表
                pmcStaffDao.modify(pmcStaff);

                //删除该用户的token
                PmcStaff pmc = pmcStaffDao.findByIdSql(pmcStaff.getId());
                if(pmc.getToken() != null){
                    tokenUtil.delToken(pmc.getToken());
                }


            }else {

                //修改物业员工表
                pmcStaffDao.modify(pmcStaff);
            }

        }else {
            throwBusinessException("手机号码重复，请重新输入！");
        }

        //修改成功后返回当前数据明细
        PmcStaff pmcStaffReturn = pmcStaffDao.findByIdSql(pmcStaff.getId());

        //通过员工ID获取员工小区
        List<Long> staffIds = new ArrayList<>();
        staffIds.add(pmcStaff.getId());
        List<PmcStaffRelCommunityVO> pmcStaffRelCommunityVOList = pmcStaffRelCommunityDao.findByStaffIds(staffIds);

        //拼接数据
        pmcStaffReturn.setCommunityName(pmcStaffRelCommunityVOList.get(0).getCommunityName());
        pmcStaffReturn.setCommunityIdsString(pmcStaffRelCommunityVOList.get(0).getCommunityIds());

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pmcStaffReturn);
        return baseVo;
    }

    /**
     * 返回物业人员列表
     * @author liyang
     * @date 2019-07-22
     * @param pmcStaffVO : 查询条件
     * @return : BaseVo
     */
    @Override
    public BaseVo findAll(PmcStaffVO pmcStaffVO) {

        //分页查询
        PageHelper.startPage(pmcStaffVO.getPageNum(), pmcStaffVO.getPageSize());
        List<PmcStaff> pmcStaffList = pmcStaffDao.findAll(pmcStaffVO);

        if(pmcStaffList.size()>Const.COUNT){
            //通过员工ID获取员工小区
            List<Long> staffIds = new ArrayList<>();
            pmcStaffList.forEach(pmcStaff -> staffIds.add(pmcStaff.getId()));
            List<PmcStaffRelCommunityVO> pmcStaffRelCommunityVOList = pmcStaffRelCommunityDao.findByStaffIds(staffIds);

            Map<Long,PmcStaffRelCommunityVO> pmcStaffRelCommunityVoMap = pmcStaffRelCommunityVOList.stream()
                    .collect(Collectors.toMap(PmcStaffRelCommunityVO::getStaffId, PmcStaffRelCommunityVO -> PmcStaffRelCommunityVO));

            //拼接数据
            for(PmcStaff pmcStaff : pmcStaffList){
                if (pmcStaffRelCommunityVoMap.get(pmcStaff.getId())!=null){
                    pmcStaff.setCommunityName(pmcStaffRelCommunityVoMap.get(pmcStaff.getId()).getCommunityName());
                    pmcStaff.setCommunityIdsString(pmcStaffRelCommunityVoMap.get(pmcStaff.getId()).getCommunityIds());
                }
            }
        }


        PageInfo<PmcStaff> pageInfo = new PageInfo<>(pmcStaffList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }

    /**
     * 物业人员停用/启用
     * @author liyang
     * @date 2019-07-22
     * @param id : 要修改的ID
     * @param status : 要修改的状态
     * @return : BaseVo
     */
    @Override
    public BaseVo modifyFlg(Long id, Integer status) {
        PmcStaff pmcStaff = new PmcStaff();
        pmcStaff.setId(id);
        pmcStaff.setStatus(status);

        UserInfo userInfo = getCurrentUserInfo();

        //如果是停用  需要清空token
        if(status.equals( PmcStaffStatusEnum.PMC_STAFF_STATUS_INACTIVE.getCode())){

            //修改物业员工表
            pmcStaffDao.modify(pmcStaff);

            //删除该用户的token
            PmcStaff pmc = pmcStaffDao.findByIdSql(pmcStaff.getId());
            if(pmc.getToken() != null){
                tokenUtil.delToken(pmc.getToken());
            }

        }else {
            //如果是启用，需要判断启用的物业人员信息所在的物业公司被停用
            PmcCompany pmcCompany = pmcStaffDao.findCompanyByStaffId(id);

            if(pmcCompany.getStatus().equals(PmcStaffStatusEnum.PMC_STAFF_STATUS_INACTIVE.getCode())){
                throwBusinessException("启用失败，该物业人员所在的物业公司已被停用，请先启用或修改物业公司！");
            }else {
                pmcStaffDao.modify(pmcStaff);
            }

        }

        return successVo();
    }

}
