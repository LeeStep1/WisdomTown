package com.bit.module.manager.service.Impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.module.manager.bean.BaseTableInfo;
import com.bit.module.manager.bean.Dict;
import com.bit.module.manager.bean.PortalOaLeader;
import com.bit.module.manager.dao.PortalContentDao;
import com.bit.module.manager.dao.PortalOaLeaderDao;
import com.bit.module.manager.feign.SysFeign;
import com.bit.module.manager.service.PortalOaLeaderService;
import com.bit.util.CommonUntil;
import com.bit.util.MongoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bit.common.Const.DUTY_MODULE;
import static com.bit.common.cmsenum.cmsEnum.*;

/**
 * PortalOaLeader的Service实现类
 * @author liuyancheng
 *
 */
@Service("portalOaLeaderService")
public class PortalOaLeaderServiceImpl extends BaseService implements PortalOaLeaderService{
	
	private static final Logger logger = LoggerFactory.getLogger(PortalOaLeaderServiceImpl.class);

	/**
	 * 领导介绍数据库管理
	 */
	@Autowired
	private PortalOaLeaderDao portalOaLeaderDao;

	/**
	 * 文章内容数据库管理
	 */
	@Autowired
	private PortalContentDao portalContentDao;

	/**
	 * 日志数据库管理
	 */
	@Autowired
	private MongoUtil mongoUtil;

	/**
	 * 通用工具类
	 */
	@Autowired
	private CommonUntil commonUntil;

	/**
	 * feign 调用
	 */
	@Autowired
	private SysFeign sysFeign;

	/**
	 * 通过主键查询单个PortalOaLeader
	 * @param id
	 * @return
	 */
	@Override
	public PortalOaLeader findById(Long id){
		return portalOaLeaderDao.findById(id);
	}
	
	/**
	 * 保存PortalOaLeader
	 * @param portalOaLeader
	 */
	@Override
	public void add(PortalOaLeader portalOaLeader, HttpServletRequest request){

		UserInfo userInfo = getCurrentUserInfo();

		//获取最大ID和排名
		BaseTableInfo baseTableInfo = commonUntil.getMaxIdAndRank(0L,LEADER_INTURDUCED_TABLE.getInfo(),0L);
		portalOaLeader.setRank(baseTableInfo.getRank());
		portalOaLeader.setOperationUserId(userInfo.getId());
		portalOaLeader.setOperationUserName(userInfo.getRealName());

		portalOaLeaderDao.add(portalOaLeader);

		//获取关联关系
		String categroyRelation = portalContentDao.getCategoryRelation(portalOaLeader.getCategoryId());

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(portalOaLeader.getId(),
				String.valueOf(OPERATION_TYPE_CREATE.getCode()),
				OPERATION_TYPE_CREATE.getInfo(),
				portalOaLeader.getName(),
				categroyRelation,
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				LEADER_INTURDUCED_TABLE.getCode());
	}
	/**
	 * 更新PortalOaLeader
	 * @param portalOaLeader
	 */
	@Override
	public void update(PortalOaLeader portalOaLeader){
		portalOaLeaderDao.update(portalOaLeader);
	}
	/**
	 * 删除PortalOaLeader
	 * @param id
	 */
	@Override
	public void delete(Long id, HttpServletRequest request){

		UserInfo userInfo = getCurrentUserInfo();

		//获取要删除的记录明细
		PortalOaLeader portalOaLeader = portalOaLeaderDao.findById(id);
		portalOaLeader.setDelStatus(DEL_FLAG.getCode());

		portalOaLeaderDao.delete(portalOaLeader);

		//获取关联关系
		String categroyRelation = portalContentDao.getCategoryRelation(portalOaLeader.getCategoryId());

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(portalOaLeader.getId(),
				String.valueOf(OPERATION_TYPE_DELETE.getCode()),
				OPERATION_TYPE_DELETE.getInfo(),
				portalOaLeader.getName(),
				categroyRelation,
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				LEADER_INTURDUCED_TABLE.getCode());
	}

	/**
	 * 获取领导介绍内容列表
	 * @author liyang
	 * @date 2019-05-15
	 * @param staionId : 站点ID
	 * @param categoryId : 栏目ID
	 * @return : BaseVo
	 */
	@Override
	public BaseVo getLeaderIntroduce(Long staionId, Long categoryId) {

		//排序字段
		String sorter = Const.ORDER_FIELD;

		List<PortalOaLeader> portalOaLeaderList = portalOaLeaderDao.findAll(sorter,UNDEL_FLAG.getCode());

		//获取职务名称
		BaseVo baseVoDict = sysFeign.findByModule(DUTY_MODULE);
		String dictStr = JSON.toJSONString(baseVoDict.getData());
		List<Dict> dicts = JSONArray.parseArray(dictStr, Dict.class);

		Map<String,Dict> dictMap = dicts.stream().collect(Collectors.toMap(Dict::getDictCode,Dict ->Dict));

		//将职位名称拼装进集合
		for (PortalOaLeader portalOaLeader : portalOaLeaderList){
			String name = dictMap.get(portalOaLeader.getDutyCode().toString()).getDictName();
			portalOaLeader.setDutyName(name);
		}

		BaseVo baseVo = new BaseVo();
		baseVo.setData(portalOaLeaderList);
		return baseVo;
	}

	/**
	 * 领导介绍排序
	 * @param portalOaLeaderList
	 * @return
	 */
    @Override
    public BaseVo serviceSort(List<PortalOaLeader> portalOaLeaderList) {

		BaseVo baseVo = new BaseVo();
		if (portalOaLeaderList==null || portalOaLeaderList.size()<=0){
			baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
			baseVo.setMsg(ResultCode.PARAMETER_ERROR.getInfo());
			baseVo.setData(null);
			return baseVo;
		}
		List<PortalOaLeader> list = new ArrayList<>();
		//批量更新排序值
		//过滤参数 有空值的淘汰
		for (PortalOaLeader portalOaLeader : portalOaLeaderList) {
			if (portalOaLeader.getId()!=null && portalOaLeader.getRank()!=null){
				list.add(portalOaLeader);
			}

		}
		portalOaLeaderDao.batchUpdateRank(list);
		return new BaseVo();
    }
}
