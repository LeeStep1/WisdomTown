package com.bit.util;

import com.bit.common.Const;
import com.bit.module.manager.bean.BaseTableInfo;
import com.bit.module.manager.bean.PortalCategory;
import com.bit.module.manager.dao.CommonUntilDao;
import com.bit.module.website.dao.SpecialPageDisplayDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.bit.common.cmsenum.cmsEnum.SERVICETYPE_CATEGORY_TABLE;
import static com.bit.common.cmsenum.cmsEnum.USING_FLAG;

/**
 * @description:
 * @author: liyang
 * @date: 2019-05-13
 **/
@Component
public class CommonUntil {

    /**
     * 工具类数据库管理
     */
    @Autowired
    private CommonUntilDao commonUntilDao;

    /**
     * 特殊类数据库管理
     */
    @Autowired
    private SpecialPageDisplayDao specialPageDisplayDao;

    /**
     * 获取栏目表中最大ID和最大排行
     * @author liyang
     * @date 2019-05-13
     * @param parentId : 父ID
     * @param tableName : 表名
     * @return : BaseTableInfo
     */
    public BaseTableInfo getMaxIdAndRank(Long parentId, String tableName,Long navigationId) {
        Long newId = null;
        int rank = 1;
        Integer idLength = Const.ID_LENGTH;
        BaseTableInfo baseTableInfo = new BaseTableInfo();

        //判断是否是栏目表
        if(tableName.equals(SERVICETYPE_CATEGORY_TABLE.getInfo())){

            //获得子栏目最大ID和最大排名
            BaseTableInfo baseTableInfoTemp = commonUntilDao.getMaxIdAndRankForCategorySql(parentId,tableName,idLength,navigationId);

            //判断是否存在子栏目
            if(baseTableInfoTemp != null){
                newId = baseTableInfoTemp.getId()+1;
                rank = baseTableInfoTemp.getRank()+1;
                baseTableInfoTemp.setId(newId);
                baseTableInfoTemp.setRank(rank);

                return baseTableInfoTemp;
            }else{
                newId = Long.valueOf(String.valueOf(parentId) + Const.INIT_ID);
                baseTableInfo.setId(newId);
                baseTableInfo.setRank(rank);
                return baseTableInfo;
            }

        }else {

            //自增表求最大ID和最大排名
            BaseTableInfo baseTableInfoTemp = commonUntilDao.getMaxIdAndRankSql(tableName);

            //判断表中是否有数据
            if(baseTableInfoTemp != null){
                newId = baseTableInfoTemp.getId()+1;
                rank = baseTableInfoTemp.getRank()+1;
                baseTableInfoTemp.setId(newId);
                baseTableInfoTemp.setRank(rank);
                return baseTableInfoTemp;
            }else{
                baseTableInfo.setId(newId);
                baseTableInfo.setRank(rank);
                return baseTableInfo;
            }
        }


    }

    /**
     * 根据导航递归查询该导航下所有一级二级栏目
     * @date 2019-05-10
     * @param portalCategory : 栏目明细
     * @param idLength : ID长度
     * @return : BaseVo
     */
    public List<PortalCategory> checkDownCategory(PortalCategory portalCategory, Integer idLength) {
        portalCategory.setStatus(USING_FLAG.getCode());
        List<PortalCategory> portalCategoryList = new ArrayList<>();
        List<PortalCategory> portalCategoryListRetrun = new ArrayList<>();
        if(portalCategory.getId().equals(0L)){

            PortalCategory portalCategoryFinal = new PortalCategory();
            //查询顶级栏目
            portalCategoryList = specialPageDisplayDao.getTopCategoryByIdSql(idLength,portalCategory);
            if(portalCategoryList.size()>0){
                for (PortalCategory pc : portalCategoryList){

                    List<PortalCategory> portalCategoryListTemp = checkDownCategory(pc,idLength);
                    portalCategoryListRetrun.addAll(portalCategoryListTemp);
                }
            }

            return portalCategoryListRetrun;
        }else {
            List<PortalCategory> portalCategoryTemp = new ArrayList<>();

            //查询子栏目
            Integer newIdLength = idLength + 3;
            portalCategoryList = specialPageDisplayDao.getSubCategoryByIdSql(portalCategory,newIdLength);

            //存在子栏目继续递归
            if(portalCategoryList.size()>0){
                for(PortalCategory pc : portalCategoryList){
                    List<PortalCategory> portalCategoryListTemp = checkDownCategory(pc,newIdLength);

                    //将子栏目插入父栏目中
                    if(portalCategory.getSecondMenu() != null){
                        portalCategory.getSecondMenu().addAll(portalCategoryListTemp);
                    }else {
                        portalCategory.setSecondMenu(portalCategoryListTemp);
                    }
                }
                portalCategoryListRetrun.add(portalCategory);
                return portalCategoryListRetrun;
            }else {

                //最底层直接返回该栏目
                portalCategoryListRetrun.add(portalCategory);
                return portalCategoryListRetrun;
            }

        }
    }

}
