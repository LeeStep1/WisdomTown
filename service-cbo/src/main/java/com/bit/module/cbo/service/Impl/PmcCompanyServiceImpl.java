package com.bit.module.cbo.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.cbo.bean.PmcCompany;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.PmcCompanyDao;
import com.bit.module.cbo.dao.PmcStaffDao;
import com.bit.module.cbo.service.PmcCompanyService;
import com.bit.module.cbo.vo.PmcCompanyVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.bit.common.enumerate.PmcCompanyEnum.DISABLE_FLAG;
import static com.bit.common.enumerate.PmcCompanyEnum.USING_FLAG;
import static com.bit.common.enumerate.PmcStaffStatusEnum.PMC_STAFF_STATUS_ACTIVE;

/**
 * @description: 物业公司相关实现
 * @author: liyang
 * @date: 2019-07-18
 **/
@Service
public class PmcCompanyServiceImpl extends BaseService implements PmcCompanyService {

    /**
     * 物业公司相关数据库操作
     */
    @Autowired
    private PmcCompanyDao pmcCompanyDao;

    /**
     * 物业公司员工相关数据库操作
     */
    @Autowired
    private PmcStaffDao pmcStaffDao;

    /**
     * 小区相关数据库操作
     */
    @Autowired
    private CommunityDao communityDao;

    /**
     * 新增物业公司
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompany : 新增详情
     * @return : BaseVo
    */
    @Override
    public BaseVo add(PmcCompany pmcCompany) {

        UserInfo userInfo = getCurrentUserInfo();

        //设置新增数据状态 0停用 1启用
        pmcCompany.setStatus(USING_FLAG.getCode());

        //获取最新时间
        Date now = new Date();

        //设置创建时间
        pmcCompany.setCreateTime(now);

        //设置修改时间（与创建时间一致）
        pmcCompany.setUpdateTime(now);

        //设置数据创建人
        pmcCompany.setCreateUserId(userInfo.getId());

        //设置更新人（与创建人一致）
        pmcCompany.setUpdateUserId(userInfo.getId());

        //去重检查
        Integer count = pmcCompanyDao.findCompanyByNameSql(pmcCompany.getCompanyName().trim());
        if(count.equals(Const.COUNT)){
            pmcCompanyDao.add(pmcCompany);
        }else {
            throwBusinessException("物业公司名称重复，请重新输入！");
        }

        return successVo();
    }

    /**
     * 修改物业公司
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompany : 修改详情
     * @return : BaseVo
     */
    @Override
    public BaseVo modify(PmcCompany pmcCompany) {
        UserInfo userInfo = getCurrentUserInfo();

        //获取修改人
        pmcCompany.setUpdateUserId(userInfo.getId());

        //获取修改时间
        pmcCompany.setUpdateTime(new Date());

        //去重检查
        Integer count = pmcCompanyDao.modifyFindCompanyByNameSql(pmcCompany.getCompanyName().trim(),pmcCompany.getId());
        if(count.equals(Const.COUNT)){
            pmcCompanyDao.modify(pmcCompany);
        }else {
            throwBusinessException("物业公司名称重复，请重新输入！");
        }



        return successVo();
    }

    /**
     * 物业公司列表展示
     * @author liyang
     * @date 2019-07-18
     * @param pmcCompanyVO : 查询条件
     * @return : BaseVo
     */
    @Override
    public BaseVo findAll(PmcCompanyVO pmcCompanyVO) {

        //分页查询
        PageHelper.startPage(pmcCompanyVO.getPageNum(), pmcCompanyVO.getPageSize());
        List<PmcCompany> pmcCompanyList = pmcCompanyDao.findAllSql(pmcCompanyVO);
        PageInfo<PmcCompany> pageInfo = new PageInfo<PmcCompany>(pmcCompanyList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }

    /**
     * 根据ID删除物业公司
     * @author liyang
     * @date 2019-07-18
     * @param id : 物业公司ID
     * @return : BaseVo
    */
    @Override
    public BaseVo deleteById(Long id) {

        //检查物业公司下是否存在物业人员
        Integer staffCount = pmcStaffDao.staffByPmcCountSql(id);
        if(staffCount.equals(Const.COUNT)){

            //再检查物业公司下是否存关联小区
            Integer communityCount = communityDao.communityByPmcCountSql(id);
            if(communityCount.equals(Const.COUNT)){

                //都没有 可以删除
                pmcCompanyDao.delete(id);
            }else {
                throwBusinessException("此物业公司存在物业人员信息或关联小区信息，不允许删除！");
            }

        }else {
            throwBusinessException("此物业公司存在物业人员信息或关联小区信息，不允许删除！");
        }

        return successVo();
    }

    /**
     * 获取所有物业公司
     * @author liyang
     * @date 2019-07-18
     * @return : BaseVo
     */
    @Override
    public BaseVo findAllUseCompany(PmcCompany pmcCompany) {

        List<PmcCompany> pmcCompanyList = pmcCompanyDao.findPmcCompanySql(pmcCompany);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pmcCompanyList);
        return baseVo;
    }

    /**
     * 修改物业公司状态
     * @author liyang
     * @date 2019-07-23
     * @param id : 物业公司ID
     * @param status : 即将成为的状态
     * @return : BaseVo
     */
    @Override
    public BaseVo modifyFlg(Long id, Integer status) {

        UserInfo userInfo = getCurrentUserInfo();

        //如果是停用  进行判断
        if(status.equals(DISABLE_FLAG.getCode())){
            //判断物业公司关联小区的个数
            Integer communityCount = communityDao.communityByPmcCountSql(id);
            if(communityCount.equals(Const.COUNT)){

                //再判断物业公司下是否关联状态为正常的员工
                Integer staffCount = pmcStaffDao.staffCountByCompanyId(id,PMC_STAFF_STATUS_ACTIVE.getCode());
                if(staffCount.equals(Const.COUNT)){

                    //都没有。可以停用
                    PmcCompany pmcCompany = new PmcCompany();
                    pmcCompany.setId(id);
                    pmcCompany.setStatus(status);

                    //获取修改人
                    pmcCompany.setUpdateUserId(userInfo.getId());

                    //获取修改时间
                    pmcCompany.setUpdateTime(new Date());

                    pmcCompanyDao.modify(pmcCompany);
                }else {
                    throwBusinessException("该公司存在状态为“正常”的物业人员信息或关联小区信息，如需停用，请先停用该公司的物业人员信息或去掉关联的小区信息！");
                }
            }else {
                throwBusinessException("该公司存在状态为“正常”的物业人员信息或关联小区信息，如需停用，请先停用该公司的物业人员信息或去掉关联的小区信息！");
            }
        }else {
            PmcCompany pmcCompany = new PmcCompany();
            pmcCompany.setId(id);
            pmcCompany.setStatus(status);

            //获取修改人
            pmcCompany.setUpdateUserId(userInfo.getId());

            //获取修改时间
            pmcCompany.setUpdateTime(new Date());

            pmcCompanyDao.modify(pmcCompany);
        }
        return successVo();
    }
}
