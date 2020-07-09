package com.bit.module.manager.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.module.manager.bean.BaseTableInfo;
import com.bit.module.manager.bean.PortalPbLeader;
import com.bit.module.manager.dao.PortalContentDao;
import com.bit.module.manager.dao.PortalPbLeaderDao;
import com.bit.module.manager.service.PortalPbLeaderService;
import com.bit.module.manager.vo.PortalPbLeaderVO;
import com.bit.util.CommonUntil;
import com.bit.util.MongoUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.bit.common.cmsenum.cmsEnum.*;


/**
 * PortalPbLeader的Service实现类
 * @author liuyancheng
 *
 */
@Service("portalPbLeaderService")
public class PortalPbLeaderServiceImpl extends BaseService implements PortalPbLeaderService{
	
	private static final Logger logger = LoggerFactory.getLogger(PortalPbLeaderServiceImpl.class);
	
	@Autowired
	private PortalPbLeaderDao portalPbLeaderDao;

	/**
	 * 通用工具类
	 */
	@Autowired
	private CommonUntil commonUntil;

	/**
	 * 日志工具类
	 */
	@Autowired
	private MongoUtil mongoUtil;

	/**
	 * 内容管理数据库操作
	 */
	@Autowired
	private PortalContentDao portalContentDao;
	
	/**
	 * 根据条件查询PortalPbLeader
	 * @param portalPbLeaderVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(PortalPbLeaderVO portalPbLeaderVO){
		PageHelper.startPage(portalPbLeaderVO.getPageNum(), portalPbLeaderVO.getPageSize());
		List<PortalPbLeader> list = portalPbLeaderDao.findByConditionPage(portalPbLeaderVO);
		PageInfo<PortalPbLeader> pageInfo = new PageInfo<PortalPbLeader>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有PortalPbLeader
	 * @return
	 */
	@Override
	public BaseVo findAll(){
		//排序字段
		String sorter = Const.ORDER_FIELD;

		List<PortalPbLeader> portalPbLeaderList = portalPbLeaderDao.findAll(sorter,UNDEL_FLAG.getCode());

		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalPbLeaderList);

		return baseVo;

	}
	/**
	 * 通过主键查询单个PortalPbLeader
	 * @param id
	 * @return
	 */
	@Override
	public PortalPbLeader findById(Long id){
		return portalPbLeaderDao.findById(id);
	}
	
	/**
	 * 新增领导班子头像
	 * @param portalPbLeader
	 */
	@Override
	public void add(PortalPbLeader portalPbLeader, HttpServletRequest request){
		UserInfo userInfo = getCurrentUserInfo();

		//获取当前最大排名
		BaseTableInfo baseTableInfo = commonUntil.getMaxIdAndRank(0L,LEADER_IMG_TABLE.getInfo(),0L);

		//存入最大排名
		portalPbLeader.setRank(baseTableInfo.getRank());

		portalPbLeaderDao.add(portalPbLeader);

		//获取关联关系
		String categroyRelation = portalContentDao.getCategoryRelation(portalPbLeader.getCategoryId());

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(portalPbLeader.getId(),
				String.valueOf(OPERATION_TYPE_CREATE.getCode()),
				OPERATION_TYPE_CREATE.getInfo(),
				portalPbLeader.getName(),
				categroyRelation,
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				LEADER_IMG_TABLE.getCode());
	}
	/**
	 * 更新PortalPbLeader
	 * @param portalPbLeader
	 */
	@Override
	public void update(PortalPbLeader portalPbLeader){
		UserInfo userInfo = getCurrentUserInfo();
		portalPbLeader.setOperationUserId(userInfo.getId());
		portalPbLeader.setOperationUserName(userInfo.getRealName());
		portalPbLeaderDao.update(portalPbLeader);
	}
	/**
	 * 删除PortalPbLeader
	 * @param id
	 */
	@Override
	public BaseVo delete(Long id, HttpServletRequest request){

		UserInfo userInfo = getCurrentUserInfo();

		PortalPbLeader portalPbLeaderDetail = portalPbLeaderDao.findById(id);

		//设置删除条件
		PortalPbLeader portalPbLeader = new PortalPbLeader();
		portalPbLeader.setId(id);
		portalPbLeader.setDelStatus(DEL_FLAG.getCode());
		portalPbLeaderDao.delete(portalPbLeader);

		//获取关联关系
		String categroyRelation = portalContentDao.getCategoryRelation(portalPbLeaderDetail.getCategoryId());

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(portalPbLeader.getId(),
				String.valueOf(OPERATION_TYPE_DELETE.getCode()),
				OPERATION_TYPE_DELETE.getInfo(),
				portalPbLeaderDetail.getName(),
				categroyRelation,
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				LEADER_IMG_TABLE.getCode());

		return successVo();
	}

	/**
	 * 办事指南服务类型排序
	 * @param portalPbLeaderList
	 * @return
	 */
	@Override
	@Transactional
	public BaseVo portalPbLeaderSort(List<PortalPbLeader> portalPbLeaderList) {
		BaseVo baseVo = new BaseVo();
		if (portalPbLeaderList==null || portalPbLeaderList.size()<=0){
			baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
			baseVo.setMsg(ResultCode.PARAMETER_ERROR.getInfo());
			baseVo.setData(null);
			return baseVo;
		}
		//批量更新排序值
		//过滤参数 有空值的淘汰
		List<PortalPbLeader> list = new ArrayList<>();
		for (PortalPbLeader portalPbLeader : portalPbLeaderList) {
			if (portalPbLeader.getId()!=null && portalPbLeader.getRank()!=null){
				list.add(portalPbLeader);
			}
		}
		portalPbLeaderDao.batchUpdateRank(list);
		return new BaseVo();
	}
}
