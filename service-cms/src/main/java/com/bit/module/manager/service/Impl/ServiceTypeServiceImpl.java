package com.bit.module.manager.service.Impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.Const;
import com.bit.common.ResultCode;
import com.bit.module.manager.bean.BaseTableInfo;
import com.bit.module.manager.bean.ServiceType;
import com.bit.module.manager.dao.PortalContentDao;
import com.bit.module.manager.dao.ServiceTypeDao;
import com.bit.module.manager.service.ServiceTypeService;
import com.bit.module.manager.vo.ServiceTypeVO;
import com.bit.util.CommonUntil;
import com.bit.util.MongoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

import static com.bit.common.Const.ORDER_FIELD;
import static com.bit.common.cmsenum.cmsEnum.*;

/**
 * ServiceType的Service实现类
 * @author liuyancheng
 *
 */
@Service("serviceTypeService")
public class ServiceTypeServiceImpl extends BaseService implements ServiceTypeService{
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceTypeServiceImpl.class);

	/**
	 * 数据库管理
	 */
	@Autowired
	private ServiceTypeDao serviceTypeDao;

	/**
	 * Mongo 工具类
	 */
	@Autowired
	private MongoUtil mongoUtil;

	/**
	 * 通用工具类
	 */
	@Autowired
	private CommonUntil commonUntil;

	/**
	 * 内容数据库管理
	 */
	@Autowired
	private PortalContentDao portalContentDao;
	
	/**
	 * 查询所有ServiceType
	 * @return
	 */
	@Override
	public List<ServiceType> findAll(){
		String sorter = ORDER_FIELD;
		return serviceTypeDao.findAll(sorter,UNDEL_FLAG.getCode());
	}
	/**
	 * 通过主键查询单个ServiceType
	 * @param id
	 * @return
	 */
	@Override
	public ServiceType findById(Long id){
		return serviceTypeDao.findById(id);
	}
	
	/**
	 * 保存ServiceType
	 * @param serviceTypeVo
	 */
	@Override
	public void add(ServiceTypeVO serviceTypeVo, HttpServletRequest request){

		UserInfo userInfo = getCurrentUserInfo();

		//获取排序和ID
		BaseTableInfo baseTableInfo = commonUntil.getMaxIdAndRank(serviceTypeVo.getCategoryId(), SERVICETYPE_CONTENT_TABLE.getInfo(),null);
		serviceTypeVo.setId(baseTableInfo.getId());
		serviceTypeVo.setRank(baseTableInfo.getRank());
		serviceTypeVo.setOperationUserId(userInfo.getId());
		serviceTypeVo.setOperationUserName(userInfo.getRealName());

		serviceTypeDao.add(serviceTypeVo);

		//获取父栏目id
		String categoryId=String.valueOf(serviceTypeVo.getCategoryId()).substring(0,String.valueOf(serviceTypeVo.getCategoryId()).length()- Const.INCREMENT_COUNT);

		//获取关联关系
		String categroyRelation = portalContentDao.getCategoryRelation(Long.valueOf(categoryId));

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(serviceTypeVo.getId(),
				String.valueOf(OPERATION_TYPE_CREATE.getCode()),
				OPERATION_TYPE_CREATE.getInfo(),
				serviceTypeVo.getTitle(),
				categroyRelation,
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				CONTENT_TABLE.getCode());
	}
	/**
	 * 更新ServiceType
	 * @param serviceType
	 */
	@Override
	public void update(ServiceType serviceType){
		serviceTypeDao.update(serviceType);
	}
	/**
	 * 删除ServiceType
	 * @param id
	 */
	@Override
	public void delete(Long id,HttpServletRequest request){

		UserInfo userInfo = getCurrentUserInfo();

		//查询明细
		ServiceType serviceType = serviceTypeDao.findById(id);

		//设置删除标识
		serviceType.setDelStatus(DEL_FLAG.getCode());

		serviceTypeDao.delete(serviceType);

		//获取父栏目id
		String categoryId=String.valueOf(serviceType.getCategoryId()).substring(0,String.valueOf(serviceType.getCategoryId()).length()- Const.INCREMENT_COUNT);

		//获取关联关系
		String categroyRelation = portalContentDao.getCategoryRelation(Long.valueOf(categoryId));

		//日志处理
		String ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		mongoUtil.addOperationToMongo(serviceType.getId(),
				String.valueOf(OPERATION_TYPE_DELETE.getCode()),
				OPERATION_TYPE_DELETE.getInfo(),
				serviceType.getTitle(),
				categroyRelation,
				ip,
				userInfo.getUsername(),
				userInfo.getRealName(),
				CONTENT_TABLE.getCode());
	}
	/**
	 * 根据栏目ID查询服务类型
	 * @param categoryId
	 * @return
	 */
	@Override
	public BaseVo queryBycategoryId(Long categoryId) {
        List<ServiceType> serviceTypes = serviceTypeDao.queryByCategoryId(categoryId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(serviceTypes);
        return baseVo;
	}
    /**
     * 办事指南服务类型排序
     * @param serviceTypeList
     * @return
     */
    @Override
    @Transactional
    public BaseVo serviceTypeSort(List<ServiceType> serviceTypeList) {
        BaseVo baseVo = new BaseVo();
        if (serviceTypeList==null || serviceTypeList.size()<=0){
            baseVo.setCode(ResultCode.PARAMETER_ERROR.getCode());
            baseVo.setMsg(ResultCode.PARAMETER_ERROR.getInfo());
            baseVo.setData(null);
            return baseVo;
        }
        //批量更新排序值
        //过滤参数 有空值的淘汰
        List<ServiceType> list = new ArrayList<>();
        for (ServiceType serviceType : serviceTypeList) {
            if (serviceType.getId()!=null && serviceType.getRank()!=null){
                list.add(serviceType);
            }
        }
        serviceTypeDao.batchUpdateRank(list);
        return new BaseVo();
    }
}
