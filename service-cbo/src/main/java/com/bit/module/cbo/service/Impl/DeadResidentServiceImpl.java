package com.bit.module.cbo.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.module.cbo.bean.DeadResident;
import com.bit.module.cbo.dao.DeadResidentDao;
import com.bit.module.cbo.service.DeadResidentService;
import com.bit.module.cbo.vo.DeadResidentExportVO;
import com.bit.module.cbo.vo.DeadResidentVO;
import com.bit.module.cbo.vo.ResidentExcelVO;
import com.bit.utils.DateUtil;
import com.bit.utils.ExcelHandler;
import com.bit.utils.ExcelUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import static com.bit.common.enumerate.DeadResidentTypeEnum.*;

/**
 * @description: 死亡居民信息相关实现
 * @author: liyang
 * @date: 2019-07-22
 **/
@Service
public class DeadResidentServiceImpl extends BaseService implements DeadResidentService {

    /**
     * 死亡人员信息相关dao
     */
    @Autowired
    private DeadResidentDao deadResidentDao;

    /**
     * 新增死亡居民信息
     * @author liyang
     * @date 2019-07-22
     * @param deadResident : 新增详情
     * @return : BaseVo
     */
    @Override
    public BaseVo add(DeadResident deadResident) {

        UserInfo userInfo = getCurrentUserInfo();

        Date now = new Date();

        //创建时间
        deadResident.setCreateTime(now);

        //创建人员id
        deadResident.setCreateUserId(userInfo.getId());

        //更新时间
        deadResident.setUpdateTime(now);

        //更新者的ID(与创建者相同)
        deadResident.setUpdateUserId(userInfo.getId());

        //重复值校验
        Integer count = deadResidentDao.countByCardTypeAndCardNum(deadResident.getCardType(),deadResident.getCardNum().trim());
        if(count.equals(Const.COUNT)){
            deadResidentDao.add(deadResident);
        }else {
            throwBusinessException("证件号码重复，请重新输入！");
        }

        return successVo();
    }

    /**
     * 修改死亡居民信息
     * @author liyang
     * @date 2019-07-23
     * @param deadResident : 修改详情
     * @return : BaseVo
     */
    @Override
    public BaseVo modify(DeadResident deadResident) {

        UserInfo userInfo = getCurrentUserInfo();

        //更新修改人
        deadResident.setUpdateUserId(userInfo.getId());

        //更新修改时间
        deadResident.setUpdateTime(new Date());

        //重复值校验
        Integer count = deadResidentDao.modifyCountByCardTypeAndCardNum(deadResident.getCardType(),deadResident.getCardNum().trim(),deadResident.getId());
        if(count.equals(Const.COUNT)){
            deadResidentDao.modify(deadResident);
        }else {
            throwBusinessException("证件号码重复，请重新输入！");
        }


        return successVo();
    }

    /**
     * 返回死亡居民信息列表接口
     * @author liyang
     * @date 2019-07-23
     * @param deadResidentVO : 查询条件
     * @return : BaseVo
     */
    @Override
    public BaseVo findAll(DeadResidentVO deadResidentVO) {

        PageHelper.startPage(deadResidentVO.getPageNum(), deadResidentVO.getPageSize());
        List<DeadResident> deadResidentList = deadResidentDao.findAll(deadResidentVO);
        PageInfo<DeadResident> pageInfo = new PageInfo<>(deadResidentList);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);

        return baseVo;
    }

    /**
     * 批量导出死亡居民信息
     * @author liyang
     * @date 2019-12-06
     * @param deadResident : 查询条件
     * @return : response
     */
    @Override
    public void exportToExcel(DeadResident deadResident, HttpServletResponse response) {
        String name = "死亡人员信息";

        //根据条件查询结果
        List<DeadResidentExportVO> deadResidentList = deadResidentDao.findAllForExcel(deadResident);

        //空文件直接导出
        if(deadResidentList.isEmpty()){
            String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
            ExcelHandler.exportExcel(deadResidentList,fileName,name,DeadResidentExportVO.class,response);
        }

        //解析人群类型
        for(DeadResidentExportVO drev : deadResidentList){
            String typeName = this.typeNumTOtypeName(drev.getType());
            drev.setTypeName(typeName);
        }

        //导出excel
        String fileName = name + "_" + DateUtil.date2String(new Date(), DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        ExcelHandler.exportExcel(deadResidentList,fileName,name,DeadResidentExportVO.class,response);


    }

    /**
     * 返回死亡信息详情
     * @author liyang
     * @date 2019-07-23
     * @param id : id
     * @return : BaseVo
     */
    @Override
    public BaseVo detail(Long id) {

        DeadResident deadResident = deadResidentDao.detail(id);

        BaseVo baseVo = new BaseVo();
        baseVo.setData(deadResident);
        return baseVo;
    }

    /**
     * 删除死亡居民信息
     * @author liyang
     * @date 2019-07-23
     * @param id : 信息id
     * @return : BaseVo
     */
    @Override
    public BaseVo delete(Long id) {

        deadResidentDao.delete(id);

        return successVo();
    }

    /**
     * 将人群类型拼接字符串转换成String
     * @author liyang
     * @date 2019-12-06
     * @param typeNum : 1,2,3
     * @return : String
    */
    public String typeNumTOtypeName(String typeNum) {
        String[] types = typeNum.split(",");
        String typeName = "";
        for(String type : types){
            String temType;
            switch (Integer.valueOf(type)){
                case 1:
                    temType = DEAD_RESIDENT_TYPE_DISABLE.getInfo();
                    break;
                case 2:
                    temType = DEAD_RESIDENT_TYPE_OLD.getInfo();
                    break;
                case 3:
                    temType = DEAD_RESIDENT_TYPE_LONELY.getInfo();
                    break;
                case 4:
                    temType = DEAD_RESIDENT_TYPE_LOW.getInfo();
                    break;
                default:
                    temType = DEAD_RESIDENT_TYPE_OTHER.getInfo();
            }
            typeName = typeName + (typeName.equals("")?"":",") + temType;
        }
        return typeName;
    }

}
