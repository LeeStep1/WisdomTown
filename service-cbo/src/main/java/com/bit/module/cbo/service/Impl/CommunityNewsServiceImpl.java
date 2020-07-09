package com.bit.module.cbo.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.module.cbo.bean.Community;
import com.bit.module.cbo.bean.CommunityNews;
import com.bit.module.cbo.dao.CommunityDao;
import com.bit.module.cbo.dao.CommunityNewsDao;
import com.bit.module.cbo.feign.FileServiceFeign;
import com.bit.module.cbo.service.CommunityNewsService;
import com.bit.module.cbo.vo.CommunityNewsPageVO;
import com.bit.module.cbo.vo.FileInfo;
import com.bit.module.cbo.vo.FileInfoVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bit.common.enumerate.CommunityNewsEumn.STATUS_PUBLISH;

/**
 * @description: 社区新闻相关实现
 * @author: liyang
 * @date: 2019-08-29
 **/
@Service
public class CommunityNewsServiceImpl extends BaseService implements CommunityNewsService {

    /**
     * 社区新闻相关dao
     */
    @Autowired
    private CommunityNewsDao communityNewsDao;

    /**
     * 小区相关dao
     */
    @Autowired
    private CommunityDao communityDao;

    /**
     * 文件相关服务
     */
    @Autowired
    private FileServiceFeign fileServiceFeign;

    /**
     * 新增社区新闻
     * @author liyang
     * @date 2019-08-29
     * @param communityNews : 新增详情
     * @return BaseVo
     */
    @Override
    public BaseVo add(CommunityNews communityNews) {

        UserInfo userInfo = getCurrentUserInfo();

        Date now = new Date();

        //创建人ID
        communityNews.setCreateUserId(userInfo.getId());

        //创建人名称
        communityNews.setCreateUserName(userInfo.getRealName());

        //创建时间
        communityNews.setCreateTime(now);

        if(communityNews.getStatus().equals(STATUS_PUBLISH.getCode())){
            //发布人ID
            communityNews.setPublishUserId(userInfo.getId());

            //发布人名称
            communityNews.setPublishUserName(userInfo.getRealName());

            //发布时间
            communityNews.setPublishTime(now);
        }

        //社区ID
        communityNews.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());

        //社区名称
        communityNews.setOrgName(userInfo.getCboInfo().getCurrentCboOrg().getName());

        communityNewsDao.add(communityNews);

        return successVo();
    }

    /**
     * 修改社区风采
     * @author liyang
     * @date 2019-08-30
     * @param communityNews : 修改详情
     * @return : BaseVo
     */
    @Override
    public BaseVo modify(CommunityNews communityNews) {

        //异常校验
        CommunityNews cn = communityNewsDao.selectByPrimaryKey(communityNews.getId());
        if(cn == null){
            throwBusinessException("此条风采已删除！");
        }

        UserInfo userInfo = getCurrentUserInfo();

        if(communityNews.getStatus().equals(STATUS_PUBLISH.getCode())){
            //发布人ID
            communityNews.setPublishUserId(userInfo.getId());

            //发布人名称
            communityNews.setPublishUserName(userInfo.getRealName());

            //发布时间
            communityNews.setPublishTime(new Date());
        }

        communityNewsDao.modify(communityNews);

        return successVo();
    }

    /**
     * 删除一条风采
     * @author liyang
     * @date 2019-08-30
     * @param id : 要删除的ID
     * @return : BaseVo
     */
    @Override
    public BaseVo delete(Long id) {
        //异常校验
        CommunityNews communityNews = communityNewsDao.selectByPrimaryKey(id);
        if(communityNews == null){
            throwBusinessException("此条风采已删除！");
        }

        communityNewsDao.deleteByPrimaryKey(id);

        return successVo();
    }

    /**
     * 查询社区风采列表
     * @author liyang
     * @date 2019-08-30
     * @param communityNewsPageVO : 查询明细
     * @return : BaseVo
     */
    @Override
    public BaseVo findAll(CommunityNewsPageVO communityNewsPageVO) {

        BaseVo baseVo = new BaseVo();

        //判断是否是游客登录
        if(communityNewsPageVO.getCommunityId() != null){

            //查询小区所属社区
            Community community = communityDao.getCommunityById(communityNewsPageVO.getCommunityId());
            communityNewsPageVO.setOrgId(community.getOrgId());

            //查询分页列表
            PageHelper.startPage(communityNewsPageVO.getPageNum(),communityNewsPageVO.getPageSize());
            List<CommunityNews> communityNewsList = communityNewsDao.findAll(communityNewsPageVO);

            if(communityNewsList.size() > Const.COUNT){
                //插入封面地址
                communityNewsList = this.getFileInfo(communityNewsList);
            }

            //返回数据
            PageInfo<CommunityNews> pageInfo = new PageInfo<>(communityNewsList);
            baseVo.setData(pageInfo);

        }else {
            UserInfo userInfo = getCurrentUserInfo();

            //判断是否是社区办
            if(!userInfo.getCboInfo().getUserType().equals(Const.USER_TYPE_ORG_SHE_QU_BAN)){
                //插入社区ID
                communityNewsPageVO.setOrgId(userInfo.getCboInfo().getCurrentCboOrg().getId());
            }else{
                communityNewsPageVO.setStatus(STATUS_PUBLISH.getCode());
            }

            //查询分页列表
            PageHelper.startPage(communityNewsPageVO.getPageNum(),communityNewsPageVO.getPageSize());
            List<CommunityNews> communityNewsList = communityNewsDao.findAll(communityNewsPageVO);

            if(communityNewsList.size() > Const.COUNT){
                //插入封面地址
                communityNewsList = this.getFileInfo(communityNewsList);
            }

            PageInfo<CommunityNews> pageInfo = new PageInfo<>(communityNewsList);

            baseVo.setData(pageInfo);
        }

        return baseVo;
    }

    /**
     * 查询明细
     * @author liyang
     * @date 2019-08-31
     * @param id :  id
     * @return : BaseVo
     */
    @Override
    public BaseVo detail(Long id) {
        CommunityNews communityNews = communityNewsDao.selectByPrimaryKey(id);
        BaseVo baseVo = new BaseVo();
        if(communityNews!=null){
            //获取图片地址
            BaseVo baseFile = fileServiceFeign.findById(Long.valueOf(communityNews.getPic()));
            String fileInfoStr = JSON.toJSONString(baseFile.getData());
            FileInfo fileInfo = JSONArray.parseObject(fileInfoStr,FileInfo.class);
            communityNews.setPicAddress(fileInfo.getPath());
            baseVo.setData(communityNews);
        }else{
            baseVo.setCode(ResultCode.RECORD_ALREADY_DELETED.getCode());
            baseVo.setMsg(ResultCode.RECORD_ALREADY_DELETED.getInfo());
        }
        return baseVo;
    }

    /**
     * 获取新闻列表图片地址
     * @author liyang
     * @date 2019-08-31
     * @param communityNewsList : 要获取的列表
     * @return : List<CommunityNews> 获取后的列表
    */
    public List<CommunityNews> getFileInfo(List<CommunityNews> communityNewsList){

        List<Long> fileIds = new ArrayList<>();
        FileInfoVO fileInfoVO = new FileInfoVO();
        communityNewsList.forEach(CommunityNews->fileIds.add(Long.valueOf(CommunityNews.getPic())));
        fileInfoVO.setFileIds(fileIds);
        BaseVo bsFile = fileServiceFeign.findByIds(fileInfoVO);
        String filesStr = JSON.toJSONString(bsFile.getData());
        List<FileInfo> fileInfoList = JSON.parseArray(filesStr,FileInfo.class);
        Map<Long,FileInfo> fileInfoMap = fileInfoList.stream().collect(Collectors.toMap(FileInfo::getId, fileInfo -> fileInfo));
        for (CommunityNews communityNews : communityNewsList){
            communityNews.setPicAddress(fileInfoMap.get(Long.valueOf(communityNews.getPic())).getPath());
        }

        return communityNewsList;
    }
}
