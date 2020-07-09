package com.bit.module.manager.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.base.vo.SuccessVo;
import com.bit.common.Const;
import com.bit.module.manager.bean.PortalCategory;
import com.bit.module.manager.bean.PortalContent;
import com.bit.module.manager.dao.PortalCategoryDao;
import com.bit.module.manager.dao.PortalContentDao;
import com.bit.module.manager.service.PortalContentService;
import com.bit.module.manager.vo.PortalContentVO;
import com.bit.util.MongoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

import static com.bit.common.cmsenum.cmsEnum.*;

/**
 * PortalContent的Service实现类
 * @author liuyancheng
 *
 */
@Service("portalContentService")
public class PortalContentServiceImpl extends BaseService implements PortalContentService{
	
	private static final Logger logger = LoggerFactory.getLogger(PortalContentServiceImpl.class);
	
	@Autowired
	private PortalContentDao portalContentDao;
	@Autowired
	private PortalCategoryDao portalCategoryDao;

	/**
	 * mongo工具类
	 */
	@Autowired
	private MongoUtil mongoUtil;

	/**
	 * 根据条件查询PortalContent
	 * @param portalContentVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(PortalContentVO portalContentVO){
		PageHelper.startPage(portalContentVO.getPageNum(), portalContentVO.getPageSize());
		if (StringUtils.isNotEmpty(portalContentVO.getBeginTime())){
			portalContentVO.setBeginTime(portalContentVO.getBeginTime() + " 00:00:00");
		}
		if (StringUtils.isNotEmpty(portalContentVO.getEndTime())){
			portalContentVO.setEndTime(portalContentVO.getEndTime() + " 23:59:59");
		}
		portalContentVO.setStatus(Const.DELETE_STATUS_NO);
		List<PortalContent> list = portalContentDao.findByConditionPage(portalContentVO);
		PageInfo<PortalContent> pageInfo = new PageInfo<PortalContent>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有PortalContent
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<PortalContent> findAll(String sorter){
		return portalContentDao.findAll(sorter);
	}
	/**
	 * 通过主键查询单个PortalContent
	 * @param id
	 * @return
	 */
	@Override
	public PortalContent findById(Long id){
		return portalContentDao.findById(id);
	}
	
	/**
	 * 保存PortalContent
	 * @param portalContent
	 */
	@Override
	@Transactional
	public void add(PortalContent portalContent, HttpServletRequest request){

		UserInfo userInfo = getCurrentUserInfo();

		// 默认未发布
		portalContent.setPublishStatus(Const.PUBLISH_STATUS_NO);
		// 创建人id
		portalContent.setOperationUserId(userInfo.getId());
		// 创建人姓名
		portalContent.setOperationUserName(userInfo.getRealName());

		portalContent.setCreateTime(new Date());

		// 删除状态
		portalContent.setStatus(Const.DELETE_STATUS_NO);
		portalContentDao.add(portalContent);

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(portalContent.getId(),
				String.valueOf(OPERATION_TYPE_CREATE.getCode()),
				OPERATION_TYPE_CREATE.getInfo(),
				portalContent.getTitle(),
				"",
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				CONTENT_TABLE.getCode());
	}

	/**
	 * 更新PortalContent
	 * @param portalContent
	 */
	@Override
	@Transactional
	public void update(PortalContent portalContent){
		portalContentDao.update(portalContent);
	}

	/**
	 * 删除PortalContent
	 * @param id
	 */
	@Override
	public void delete(Long id,HttpServletRequest request){

		UserInfo userInfo = getCurrentUserInfo();

		// 1. 先通过id查询
		PortalContent portalContent = portalContentDao.findById(id);
		String title = portalContent.getTitle();
		String categoryName = portalContent.getCategoryName();

		Integer status = DEL_FLAG.getCode();
		portalContentDao.delete(id,status);

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(id,
				String.valueOf(OPERATION_TYPE_DELETE.getCode()),
				OPERATION_TYPE_DELETE.getInfo(),
				title,
				categoryName,
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				CONTENT_TABLE.getCode());
	}

	/**
	 * 复制
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo copy(Long id,HttpServletRequest request) {

		UserInfo userInfo = getCurrentUserInfo();

		// 1. 先通过id查询
		PortalContent portalContent = portalContentDao.findById(id);
		// 2. 去除发布状态，关联栏目等
		if (portalContent != null){
			portalContent.setPublishStatus(Const.PUBLISH_STATUS_NO);
			portalContent.setPublishTime(null);
			portalContent.setCreateTime(new Date());
			portalContent.setCategoryId(null);
			portalContent.setStationId(null);
			portalContent.setId(null);
			portalContentDao.add(portalContent);

			//日志处理
			String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			mongoUtil.addOperationToMongo(portalContent.getId(),
					String.valueOf(OPERATION_TYPE_CREATE.getCode()),
					OPERATION_TYPE_CREATE.getInfo(),
					portalContent.getTitle(),
					"",
					ip,
					userInfo.getUsername(),
					userInfo.getRealName(),
					CONTENT_TABLE.getCode());
		}
		return new SuccessVo();
	}

	/**
	 * 取消发布
	 * @param id
	 * @return
	 */
	@Override
	public BaseVo cancelPublish(Long id,HttpServletRequest request) {

		UserInfo userInfo = getCurrentUserInfo();

		// 1. 先通过id查询
		PortalContent portalContent = portalContentDao.findById(id);

		//关联栏目
		String categroyRelation = portalContent.getCategoryName();
		portalContent.setCategoryName(categroyRelation);

		// 2. 去除发布状态和发布时间和关联栏目
		if (portalContent != null){
			portalContent.setPublishStatus(Const.PUBLISH_STATUS_NO);
			portalContent.setPublishTime(null);
			portalContent.setOperationUserId(userInfo.getId());
			portalContent.setOperationUserName(userInfo.getRealName());
			portalContentDao.cancelPublish(portalContent);
		}

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(portalContent.getId(),
				String.valueOf(OPERATION_TYPE_UNPUBLISH.getCode()),
				OPERATION_TYPE_UNPUBLISH.getInfo(),
				portalContent.getTitle(),
				categroyRelation,
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				CONTENT_TABLE.getCode());
		return new SuccessVo();
	}

	/**
	 * 发布
	 * @param portalContent
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo publish(PortalContent portalContent,HttpServletRequest request) {

		UserInfo userInfo = getCurrentUserInfo();

		BaseVo baseVo = new BaseVo();
		// 1. 先根据栏目id找到对应的必填项
		PortalCategory portalCategory = portalCategoryDao.findById(portalContent.getCategoryId());
		// 根据内容id查询内容等信息
		PortalContent content = portalContentDao.findById(portalContent.getId());
		if (portalCategory != null){
			//2. 获取对应的必填项进行判断
			//3. 判断标题是否必填
			if (portalCategory.getCategoryTitleFlag().equals(Const.REQUIRED_FIELDS)){
				if (StringUtils.isEmpty(content.getTitle())){
					throw new BusinessException("不能发布至此栏目，缺少标题");
				}
			}
			//4. 判断内容名称是否必填
			if (portalCategory.getCategoryContentNameFlag().equals(Const.REQUIRED_FIELDS)){
				if (StringUtils.isEmpty(content.getContentName())){
					throw new BusinessException("不能发布至此栏目，缺少内容名称");
				}
			}
			//5. 判断发布方是否必填
			if (portalCategory.getCategoryCreateFlag().equals(Const.REQUIRED_FIELDS)){
				if (StringUtils.isEmpty(content.getPublisher())){
					throw new BusinessException("不能发布至此栏目，缺少发布方");
				}
			}
			//6. 判断封面是否必填
			if (portalCategory.getCategoryCoverFlag().equals(Const.REQUIRED_FIELDS)){
				if (StringUtils.isEmpty(content.getCoverUrl())){
					throw new BusinessException("不能发布至此栏目，缺少封面图片");
				}
			}
			//7. 判断副标题是否必填
			if (portalCategory.getCategorySubheadFlag().equals(Const.REQUIRED_FIELDS)){
				if (StringUtils.isEmpty(content.getSubTitle())){
					throw new BusinessException("不能发布至此栏目，缺少副标题");
				}
			}
			//8. 判断视频是否必填
			if (portalCategory.getCategoryVideoFlag().equals(Const.REQUIRED_FIELDS)){
				if (StringUtils.isEmpty(content.getVideoUrl())){
					throw new BusinessException("不能发布至此栏目，缺少视频");
				}
			}
			//8. 判断内容是否必填
			if (portalCategory.getCategoryContentFlag().equals(Const.REQUIRED_FIELDS)){
				if (StringUtils.isEmpty(content.getContent())){
					throw new BusinessException("不能发布至此栏目，缺少内容");
				}
			}

			// 发布时间
			portalContent.setPublishTime(new Date());
			// 发布状态
			portalContent.setPublishStatus(Const.PUBLISH_STATUS_YES);

			//关联栏目
			String categroyRelation = portalContentDao.getCategoryRelation(portalContent.getCategoryId());
			portalContent.setCategoryName(categroyRelation);

			portalContentDao.update(portalContent);

			//日志处理
			String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			mongoUtil.addOperationToMongo(portalContent.getId(),
					String.valueOf(OPERATION_TYPE_PUBLISH.getCode()),
					OPERATION_TYPE_PUBLISH.getInfo(),
					content.getTitle(),
					categroyRelation,
					ip,
					userInfo.getUsername(),
					userInfo.getRealName(),
					CONTENT_TABLE.getCode());

			return baseVo;
		}else {
			throw new BusinessException("暂无次栏目");
		}
	}

	/**
	 * 根据获取内容列表(分页)
	 * @author liyang
	 * @date 2019-05-14
	 * @param portalContentVo ：查询条件
	 * @return : BaseVo
	 */
    @Override
    public BaseVo getContentListByCategoryId(PortalContentVO portalContentVo) {

		//插入站点查询条件
		portalContentVo.setStatus(USING_FLAG.getCode());

		//分页查询
		PageHelper.startPage(portalContentVo.getPageNum(), portalContentVo.getPageSize());
		List<PortalContent> portalContentList = portalContentDao.getContentListByCategoryIdSql(portalContentVo);
		PageInfo<PortalContent> pageInfo = new PageInfo<PortalContent>(portalContentList);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;

    }

	/**
	 * 内容所属栏目权限校验
	 * @author liyang
	 * @date 2019-05-27
	 * @param categoryId : 栏目ID
	 * @return : BaseVo
	 */
    @Override
    public BaseVo getContentCheck(Long categoryId) {

		PortalCategory portalCategory = portalCategoryDao.findById(categoryId);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalCategory);

        return baseVo;
    }

	/**
	 * 获取内容表所有关联关系
	 * @author liyang
	 * @date 2019-05-28
	 * @return : BaseVo
	 */
    @Override
    public BaseVo getCategoryRelationAll() {
		Integer categoryStatus = UNPUBLISH_FLAG.getCode();
		Integer categoryType = NORMAL_CATEGORY.getCode();
		
		List<PortalContent> portalContentList = portalContentDao.getCategoryRelationAllSql(categoryStatus,categoryType);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalContentList);
        return baseVo;
    }
}
