package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.enumerate.ApplyGuideEumn;
import com.bit.module.cbo.bean.ResidentApplyGuide;
import com.bit.module.cbo.bean.ResidentApplyGuideItems;
import com.bit.module.cbo.dao.ResidentApplyGuideDao;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.service.ResidentApplyGuideService;
import com.bit.module.cbo.vo.FileInfo;
import com.bit.module.cbo.vo.FileInfoVO;
import com.bit.module.cbo.vo.ResidentApplyGuideVO;
import com.bit.utils.StringUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.bit.common.enumerate.ApplyGuideEumn.*;

/**
 * @description: 办事指南，类别与事项 相关 实现
 * @author: liyang
 * @date: 2019-08-06
 **/
@Service
public class ResidentApplyGuideServiceImpl extends BaseService implements ResidentApplyGuideService {

    /**
     * 办事指南，类别与事项相关dao
     */
    @Autowired
    private ResidentApplyGuideDao residentApplyGuideDao;
    @Autowired
	private FileServiceFeign fileServiceFeign;

    /**
     * 增加 办事指南类别
     * @author liyang
     * @date 2019-08-06
     * @param residentApplyGuideVO :  办事指南类别
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo add(ResidentApplyGuideVO residentApplyGuideVO) {

        UserInfo userInfo = getCurrentUserInfo();

        //获取当前最大排序号
        int maxSort = residentApplyGuideDao.getMaxSort(residentApplyGuideVO.getPid());
        residentApplyGuideVO.setSort(maxSort);

        //创建时间
        Date now = new Date();
        residentApplyGuideVO.setCreateTime(now);

        //创建人
        residentApplyGuideVO.setCreateUserId(userInfo.getId());

        //修改时间(新增时与创建时间一致)
        residentApplyGuideVO.setUpdateTime(now);

        //修改人(新增时与创建人一致)
        residentApplyGuideVO.setUpdateUserId(userInfo.getId());

        //异常校验
        Integer count = residentApplyGuideDao.findGuideCountByNameSql(residentApplyGuideVO);
        if(count> Const.COUNT){
            throwBusinessException("此 类别/事项 名称已存在，请重新输入");
        }

        //插入指南主表
        residentApplyGuideDao.addGuid(residentApplyGuideVO);

        //如果是增加事项还需插入事项明细表
        if(residentApplyGuideVO.getPid() != null){

            //插入事项明细
            this.addGuideItems(residentApplyGuideVO.getId(),residentApplyGuideVO.getResidentApplyGuideItemsList()) ;

        }

        return successVo();
    }

    /**
     * 办事指南类别和事项排序
     * @author liyang
     * @date 2019-08-07
     * @param residentApplyGuideList : 办事指南类别和事项最新的顺序
     * @return : BaseVo
     */
    @Override
    public BaseVo sortGuid(List<ResidentApplyGuide> residentApplyGuideList) {

        residentApplyGuideDao.sortGuidSql(residentApplyGuideList);
        return successVo();
    }

    /**
     * 获得类别和事项列表
     * @author liyang
     * @date 2019-08-07
     * @param residentApplyGuide : 查询条件
     * @return : BaseVo
     */
    @Override
    public BaseVo findGuide(ResidentApplyGuide residentApplyGuide) {

        //先查询所有的类别与事项
        List<ResidentApplyGuide> residentApplyGuideList = residentApplyGuideDao.findGuideSql(residentApplyGuide);

        //取出所有类别项
        List<ResidentApplyGuide> residentApplyGuideParent = new ArrayList<>();
        List<ResidentApplyGuide> residentApplyGuideChild = new ArrayList<>();
        for (ResidentApplyGuide residentApplyGuideTemp : residentApplyGuideList){
            if(residentApplyGuideTemp.getPid() != null){
                residentApplyGuideChild.add(residentApplyGuideTemp);
            }else {
                residentApplyGuideParent.add(residentApplyGuideTemp);
            }
        }

        //将子类别分组
        Map<Long,List<ResidentApplyGuide>> residentApplyGuideMap = residentApplyGuideChild.stream().collect(Collectors.groupingBy(ResidentApplyGuide::getPid));

        //组装返回值
        for(ResidentApplyGuide residentApplyGuideTemp : residentApplyGuideParent){
            residentApplyGuideTemp.setResidentApplyGuideList(residentApplyGuideMap.get(residentApplyGuideTemp.getId()));
        }

        BaseVo baseVo = new BaseVo();
        baseVo.setData(residentApplyGuideParent);

        return baseVo;
    }

    /**
     * 修改办事指南类别和事件
     * @author liyang
     * @date 2019-08-07
     * @param residentApplyGuideVO : 修改详情
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo modify(ResidentApplyGuideVO residentApplyGuideVO) {

        UserInfo userInfo = getCurrentUserInfo();

        residentApplyGuideVO.setUpdateTime(new Date());

        residentApplyGuideVO.setUpdateUserId(userInfo.getId());

        //异常校验
        Integer count = residentApplyGuideDao.findGuideCountByNameSql(residentApplyGuideVO);
        if(count> Const.COUNT){
            throwBusinessException("此 类别/事项 名称已存在，请重新输入");
        }

        //先修改事项表名称
        residentApplyGuideDao.modifyGuideSql(residentApplyGuideVO);

        //如果是修改事项  还需修改明细
        if(residentApplyGuideVO.getType().equals(APPLY_GUIDE_ITEMS.getCode())){

            //判断当前事项的状态，如果是草稿状态，事项明细先删再插入，如果是启用状态，直接修改内容
            if(!residentApplyGuideVO.getEnable().equals(APPLY_GUIDE_DRAFT.getCode())){

                residentApplyGuideDao.modifyGuideItems(residentApplyGuideVO.getResidentApplyGuideItemsList());
            }else {
                //再删除明细表之前所有明细
                residentApplyGuideDao.deleteGuidItemsByGuideIdSql(residentApplyGuideVO.getId());

                //再插入新的明细
                this.addGuideItems(residentApplyGuideVO.getId(),residentApplyGuideVO.getResidentApplyGuideItemsList());
            }

        }

        return successVo();
    }

    /**
     * 修改办事指南类别和事项所属ID
     * @author liyang
     * @date 2019-08-07
     * @param id : 办事指南类别和事项ID
     * @param type ：数据类别:1 类别，0事项
     * @param enable : 是否停用：1 启用，0 停用
     * @return : BaseVo
     */
    @Override
    @Transactional
    public BaseVo modifyFlg(Long id,Integer type,Integer enable) {

        UserInfo userInfo = getCurrentUserInfo();

        //先判断是修改类别还是事项
        if(type.equals(APPLY_GUIDE_GUIDE.getCode())){

            //判断是要停用还是启用  停用是联动的  启用只是启用自己
            if(enable.equals(APPLY_GUIDE_DISABLE.getCode())){

                //如果是类别停用 需要先停用该类别下的所有事项（不包括草稿）
                ResidentApplyGuide residentApplyGuide = new ResidentApplyGuide();
                residentApplyGuide.setUpdateTime(new Date());
                residentApplyGuide.setUpdateUserId(userInfo.getId());
                residentApplyGuide.setPid(id);
                residentApplyGuide.setEnable(enable);
                residentApplyGuideDao.modifyGuideFalgSql(residentApplyGuide);
            }

            //在停用类别
            ResidentApplyGuide residentApplyGuideCategory = new ResidentApplyGuide();
            residentApplyGuideCategory.setUpdateTime(new Date());
            residentApplyGuideCategory.setUpdateUserId(userInfo.getId());
            residentApplyGuideCategory.setId(id);
            residentApplyGuideCategory.setEnable(enable);
            residentApplyGuideDao.modifyGuideFalgSql(residentApplyGuideCategory);
        }else {

            //如果是启用事项需要检查类别的状态
            if(enable.equals(APPLY_GUIDE_USING.getCode())){

                //如果是启用某一个事项，则所属类别自动启用
                ResidentApplyGuideVO residentApplyGuideVO = residentApplyGuideDao.queryByIdSql(id);

                //先查询所属类别是否启用
                ResidentApplyGuideVO residentApplyGuideVOGuide = residentApplyGuideDao.queryByIdSql(id);
                if(residentApplyGuideVOGuide.getEnable().equals(APPLY_GUIDE_DISABLE.getCode())){

                    //如果是停用需要启用类别
                    ResidentApplyGuide residentApplyGuideCategoryTemp = new ResidentApplyGuide();
                    residentApplyGuideCategoryTemp.setUpdateTime(new Date());
                    residentApplyGuideCategoryTemp.setUpdateUserId(userInfo.getId());
                    residentApplyGuideCategoryTemp.setId(residentApplyGuideVO.getPid());
                    residentApplyGuideCategoryTemp.setEnable(enable);
                    residentApplyGuideDao.modifyGuideFalgSql(residentApplyGuideCategoryTemp);
                }
            }

            //如果是停用某一个事项
            ResidentApplyGuide residentApplyGuideCategory = new ResidentApplyGuide();
            residentApplyGuideCategory.setUpdateTime(new Date());
            residentApplyGuideCategory.setUpdateUserId(userInfo.getId());
            residentApplyGuideCategory.setId(id);
            residentApplyGuideCategory.setEnable(enable);
            residentApplyGuideDao.modifyGuideFalgSql(residentApplyGuideCategory);

        }

        return successVo();
    }


    /**
     * 根据ID查询办事指南明细
     * @author liyang
     * @date 2019-08-08
     * @param id : id
     * @param type : 数据类别:1 类别，0事项
     * @return : BaseVo
     */
    @Override
    public BaseVo queryId(Long id, Integer type) {

        BaseVo baseVo = new BaseVo();

        //先判断是类别还是事项
        if(type.equals(APPLY_GUIDE_ITEMS.getCode())){

            //如果是事项，先查事项名称，再查询事项明细
            ResidentApplyGuideVO residentApplyGuideVO = residentApplyGuideDao.queryByIdSql(id);

            //查询事项明细
            List<ResidentApplyGuideItems> residentApplyGuideItemsList = residentApplyGuideDao.queryByGuideIdSql(id);

            residentApplyGuideVO.setResidentApplyGuideItemsList(residentApplyGuideItemsList);

            //todo 办事指南优化if for
            if (CollectionUtils.isNotEmpty(residentApplyGuideItemsList)){
                residentApplyGuideItemsList.forEach(residentApplyGuideItems -> {
                    if (residentApplyGuideItems.getType().equals(ApplyGuideEumn.APPLY_ITEMS_TYPE_ATTACH_FILE.getCode())
                            && StringUtil.isNotEmpty(residentApplyGuideItems.getName())){
                        List<Long> fileIds = Arrays.asList(residentApplyGuideItems.getName().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                        FileInfoVO fileInfoVO = new FileInfoVO();
                        fileInfoVO.setFileIds(fileIds);
                        BaseVo byIds = fileServiceFeign.findByIds(fileInfoVO);
                        if (byIds.getData()!=null){
                            String s = JSON.toJSONString(byIds.getData());
                            List<FileInfo> fileInfos = JSONArray.parseArray(s,FileInfo.class);
                            residentApplyGuideVO.setFileInfos(fileInfos);
                        }
                    }
                });
			}
            baseVo.setData(residentApplyGuideVO);

        }else {

            //如果是类别  查询类别表后直接返回
            ResidentApplyGuideVO residentApplyGuideVO = residentApplyGuideDao.queryByIdSql(id);
            baseVo.setData(residentApplyGuideVO);

        }
        return baseVo;
    }

    /**
     * 查询含有补充业务的类别和事项
     * @author liyang
     * @date 2019-08-19
     * @return : BaseVo
     */
    @Override
    public BaseVo queryGuideRoster() {

        //先获取所有有补充业务信息的事项(所有停用，启用状态)
        List<ResidentApplyGuide> guideItemList = residentApplyGuideDao.queryGuideItemParmExtend();

        //获取所属类别集合
        List<ResidentApplyGuideVO> guideCategoryList = residentApplyGuideDao.findAllGuideForCategory();

        //过滤掉没有事项的类别
        List<ResidentApplyGuideVO> guideReturn = new ArrayList<>();
        Map<Long,List<ResidentApplyGuide>> guideItemMap= guideItemList.stream().collect(Collectors.groupingBy(ResidentApplyGuide::getPid));
        for (ResidentApplyGuideVO guide : guideCategoryList){
            List<ResidentApplyGuide> temp = guideItemMap.get(guide.getId());
            if(temp != null){
                guide.setResidentApplyGuideList(temp);
                guideReturn.add(guide);
            }
        }

        BaseVo baseVo = new BaseVo();
        baseVo.setData(guideReturn);

        return baseVo;
    }

    /**
     * 插入事项明细表
     * @author liyang
     * @date 2019-08-07
     * @param guideId : 所属类别
     * @param residentApplyGuideItemsList : 事项明细
    */
    private void addGuideItems(Long guideId,List<ResidentApplyGuideItems> residentApplyGuideItemsList){

        //将ID插入集合中的每条数据
        residentApplyGuideItemsList.forEach(residentApplyGuideItems -> residentApplyGuideItems.setGuideId(guideId));

        residentApplyGuideDao.addGuidItems(residentApplyGuideItemsList);
    }

}
