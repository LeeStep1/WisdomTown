package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.bit.base.dto.UserInfo;
import com.bit.common.Const;
import com.bit.module.cbo.bean.ExtendTypeBase;
import com.bit.module.cbo.bean.ResidentApplyHomeCare;
import com.bit.module.cbo.dao.ResidentApplyBaseDao;
import com.bit.module.cbo.dao.ResidentApplyGuideDao;
import com.bit.module.cbo.feign.SysServiceFeign;
import com.bit.module.cbo.vo.Dict;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description: 居家养老相关实现
 * @author: liyang
 * @date: 2020-06-09
 **/
@Component
public class ResidentApplyHomeCareImpl extends AbstractExtendType {

    /**
     * 办事指南相关dao
     */
    @Autowired
    private ResidentApplyGuideDao residentApplyGuideDao;

    /**
     * sys服务相关调用
     */
    @Autowired
    private SysServiceFeign sysServiceFeign;

    /**
     * 办事台账相关dao
     */
    @Autowired
    private ResidentApplyBaseDao residentApplyBaseDao;

    /**
     * 根据台账ID获取特殊扶助扩展信息明细
     * @param id
     * @return
     */
    @Override
    public ExtendTypeBase getExtendTypeBase(Long id) {
        ResidentApplyHomeCare resCare = new ResidentApplyHomeCare();
        resCare.setApplyId(id);
        ResidentApplyHomeCare resCareReturn = residentApplyGuideDao.queryResidentHomeCareByParmSql(resCare);

        if(resCareReturn != null){
            List<String> codes = new ArrayList<>();

            //居家养老户口类别
            codes.add(Const.RESIDENCE_TYPE);

            //待遇类别
            codes.add(Const.TREATMENT_TYPE);

            //评估等级
            codes.add(Const.BASE_LEVEL);
            Dict dict = new Dict();
            dict.setModules(codes);

            // 查询字典表
            Object object = sysServiceFeign.findByModules(dict).getData();
            Map<String, List<Dict>> map = new HashMap<String, List<Dict>>();
            String ss = JSON.toJSONString(object);
            Gson gson = new Gson();
            map = gson.fromJson(ss,map.getClass());

            //获取户口类别
            List<Dict> residenceTypeDictList = JSON.parseArray(JSON.toJSONString(map.get(Const.RESIDENCE_TYPE)),Dict.class);
            String residenceTypeName = this.getDictNameByDictCodeAndModule(residenceTypeDictList,resCareReturn.getResidenceType());
            resCareReturn.setResidenceTypeName(residenceTypeName);

            //获取待遇类别名称
            List<Dict> treatmentTypeDictList = JSON.parseArray(JSON.toJSONString(map.get(Const.TREATMENT_TYPE)),Dict.class);
            String treatmentTypeName = this.getDictNameByDictCodeAndModule(treatmentTypeDictList,resCareReturn.getTreatmentType());
            resCareReturn.setTreatmentTypeName(treatmentTypeName);

            //获取评估等级名称
            List<Dict> levelNameDictList = JSON.parseArray(JSON.toJSONString(map.get(Const.TREATMENT_TYPE)),Dict.class);
            String levelName = this.getDictNameByDictCodeAndModule(levelNameDictList,resCareReturn.getLevel());
            resCareReturn.setLevelName(levelName);
        }

        return resCareReturn;
    }

    /**
     * 居家养老补充业务信息
     * @author liyang
     * @date 2020-06-11
     * @param extendTypeBase : 补充业务信息详情
     * @param userInfo : 修改人信息
     * @param applyId : 申请人信息
     */
    @Override
    public void addExtendInfo(ExtendTypeBase extendTypeBase, UserInfo userInfo, Long applyId) throws Exception {

        //异常判断，如果有人已经完善了该信息 直接返回
        Integer count = residentApplyBaseDao.getHomeCareCountByApplyId(applyId);
        if(count > Const.COUNT){
            new Exception("该业务信息已完善！");
        }

        //插入居民养老表
        ResidentApplyHomeCare residentApplyHomeCare = (ResidentApplyHomeCare) extendTypeBase;
        Date now = new Date();
        residentApplyHomeCare.setCreateTime(now);
        residentApplyHomeCare.setCreateUserId(userInfo.getId());
        residentApplyHomeCare.setUpdateTime(now);
        residentApplyHomeCare.setUpdateUserId(userInfo.getId());
        residentApplyBaseDao.addHomeCare(residentApplyHomeCare);
    }

    /**
     * 获取字典表特殊字段名称
     * @author liyang
     * @date 2020-06-11
     * @param dictList : 字典表集合
     * @param dictCode : 想要获取的code
     * @return : 字段名称
    */
    private String getDictNameByDictCodeAndModule(List<Dict> dictList,String dictCode){
        Map<String,Dict> dictMap = dictList.stream().collect(Collectors.toMap(Dict::getDictCode, dict -> dict));
        String dictName = dictMap.get(dictCode).getDictName();

        return dictName;
    }
}
