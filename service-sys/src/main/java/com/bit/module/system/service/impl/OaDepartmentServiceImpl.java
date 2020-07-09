package com.bit.module.system.service.impl;

import com.bit.base.dto.UserInfo;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.SysConst;
import com.bit.module.system.bean.OaDepartment;
import com.bit.module.system.bean.User;
import com.bit.module.system.dao.OaDepartmentDao;
import com.bit.module.system.dao.UserRelCboDepDao;
import com.bit.module.system.service.OaDepartmentService;
import com.bit.module.system.vo.OaDepartmentResultVO;
import com.bit.module.system.vo.OaDepartmentVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * OaDepartment的Service实现类
 * @author codeGenerator
 *
 */
@Service("oaDepartmentService")
public class OaDepartmentServiceImpl extends BaseService implements OaDepartmentService {
	
	private static final Logger logger = LoggerFactory.getLogger(OaDepartmentServiceImpl.class);
	
	@Autowired
	private OaDepartmentDao oaDepartmentDao;
	@Autowired
	private UserRelCboDepDao userRelCboDepDao;
	
	/**
	 * 根据条件查询OaDepartment
	 * @param oaDepartmentVO
	 * @return
	 */
	@Override
	public BaseVo findByConditionPage(OaDepartmentVO oaDepartmentVO){
		PageHelper.startPage(oaDepartmentVO.getPageNum(), oaDepartmentVO.getPageSize());
		if (StringUtils.isNotEmpty(oaDepartmentVO.getDeptCode())){
			oaDepartmentVO.setOrderBy("dept_code");
			oaDepartmentVO.setOrder("asc");
		}else {
			oaDepartmentVO.setOrderBy("create_time");
			oaDepartmentVO.setOrder("desc");
		}
		List<OaDepartment> list = oaDepartmentDao.findByConditionPage(oaDepartmentVO);
		PageInfo<OaDepartment> pageInfo = new PageInfo<OaDepartment>(list);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(pageInfo);
		return baseVo;
	}
	/**
	 * 查询所有OaDepartment
	 * @param sorter 排序字符串
	 * @return
	 */
	@Override
	public List<OaDepartment> findAll(String sorter){
		return oaDepartmentDao.findAll(sorter);
	}

	@Override
	public OaDepartment findById(Long id) {
		return oaDepartmentDao.findById(id);
	}

	/**
	 * 通过主键查询单个OaDepartment
	 * @param id
	 * @return
	 */
	@Override
	public OaDepartmentResultVO findResultById(Long id){
		OaDepartmentResultVO resultVO = oaDepartmentDao.findResultById(id);
		if (resultVO != null){
			//处理一下，id-3位，如果有则表示有上级，如果没有则表示顶级
			String idStr = String.valueOf(resultVO.getId());
			String substring = idStr.substring(0, idStr.length() - 3);
			if (StringUtils.isNotEmpty(substring)){
				OaDepartment up = oaDepartmentDao.findById(Long.valueOf(substring));
				if (up != null && StringUtils.isNotEmpty(up.getName())){
					resultVO.setUpName(up.getName());
				}
			}
		}
		return resultVO;
	}
	
	/**
	 * 保存OaDepartment
	 * @param oaDepartmentResultVO
	 */
	@Override
	@Transactional
	public void add(OaDepartmentResultVO oaDepartmentResultVO){
		//创建时间
		oaDepartmentResultVO.setCreateTime(new Date());
		//先判断上级
		String upLevel = oaDepartmentResultVO.getStrPid();
		if (upLevel == null){
			// 如果没有上级，则表示顶级
			List<Long> ids = new ArrayList<>();
			List<OaDepartment> all = oaDepartmentDao.findTopLevel();
			for (OaDepartment oaDepartment : all) {
				int length = oaDepartment.getId().toString().length();
				if (length == 3){
					ids.add(oaDepartment.getId());
				}
			}
			Collections.sort(ids);
			Long id = ids.get(ids.size() - 1);
			oaDepartmentResultVO.setId(id+=1);
			oaDepartmentDao.addResultVO(oaDepartmentResultVO);
		}else {
			//如有有上级，则根据上级查询下级
			List<OaDepartment> oaDepartmentList = oaDepartmentDao.findByIdLike(oaDepartmentResultVO.getStrPid(),"temp");
			//如果list只有一条，那么表示顶级下面的第一个第二级菜单
            if (oaDepartmentList.size() == 0){
                String s = String.valueOf(upLevel) + "101";
                oaDepartmentResultVO.setId(Long.valueOf(s));
                oaDepartmentDao.addResultVO(oaDepartmentResultVO);
            }
//			if (oaDepartmentList.size() == 1){
//				for (OaDepartment oaDepartment : oaDepartmentList) {
//					String s = String.valueOf(oaDepartment.getId()) + "101";
//					oaDepartmentResultVO.setId(Long.valueOf(s));
//				}
//				oaDepartmentDao.addResultVO(oaDepartmentResultVO);
//			}
			else {
				List<Long> ids = new ArrayList<>();
				for (OaDepartment oaDepartment : oaDepartmentList) {
					ids.add(oaDepartment.getId());
				}
				Collections.sort(ids);
				Long id = ids.get(ids.size() - 1);
				oaDepartmentResultVO.setId(id+=1);
				oaDepartmentDao.addResultVO(oaDepartmentResultVO);
			}
		}
	}
	/**
	 * 更新OaDepartment
	 * @param oaDepartmentResultVO
	 */
	@Override
	@Transactional
	public void updateResult(OaDepartmentResultVO oaDepartmentResultVO) {
		oaDepartmentDao.updateResult(oaDepartmentResultVO);
	}

	@Override
	public BaseVo findByUserId(Long userId) {
		List<OaDepartment> departmentList = oaDepartmentDao.findByUserId(userId);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(departmentList);
		return baseVo;
	}

	/**
	 * 删除OaDepartment
	 * @param id
	 */
	@Override
	@Transactional
	public void delete(Long id){
		oaDepartmentDao.delete(id);
	}

	@Override
	public BaseVo checkPcodeUnique(OaDepartmentResultVO resultVO) {
		//判断组织编号是否唯一
		int i = oaDepartmentDao.findByCode(resultVO.getPcode());
		BaseVo baseVo = new BaseVo();
		Boolean flag;
		//更新校验
		if (resultVO.getId() != null) {
			//原数据（旧数据）
			OaDepartmentResultVO resultById = oaDepartmentDao.findResultById(resultVO.getId());
			String pcode = resultById.getPcode();
			if (pcode.equals(resultVO.getPcode())) {
				flag = true;
			} else {
				int count = oaDepartmentDao.findByCode(resultVO.getPcode());
				if (count > 0) {
					flag = false;
				} else {
					flag = true;
				}
			}
		} else {
			//保存校验
			int count = oaDepartmentDao.findByCode(resultVO.getPcode());
			if (count > 0) {
				flag = false;
			} else {
				flag = true;
			}
		}
		baseVo.setData(flag);
		return baseVo;
	}

	@Override
	public BaseVo checkdown(OaDepartmentResultVO resultVO) {
		BaseVo baseVo = new BaseVo();
		List<OaDepartment> departmentList = oaDepartmentDao.findByIdLike(String.valueOf(resultVO.getId()),null);
		if (departmentList.size() == 1){
			// 等于1 表示没有下级,查出来的是当前自己
			baseVo.setData(true);
		}else if (departmentList.size() > 1){
			//大于1的时候表示有下级，则不能直接删除
			baseVo.setData(false);
		}
		return baseVo;
	}

	/**
	 * 查询组织结构明细
	 * @param id 要查询的组织ID
	 * @return
	 */
	@Override
	public BaseVo findOaDepartment(Long id) {
		OaDepartment oaDepartment = new OaDepartment();

		if(id != 0){
            oaDepartment = oaDepartmentDao.findOaDepartmentByIdSql(id);
        }

		List<OaDepartment> oaDepartmentReturn  = checkdownOaDepartment(id,oaDepartment);

		BaseVo baseVo = new BaseVo();
		baseVo.setData(oaDepartmentReturn);
		
		return baseVo;
	}

	/**
	 * 批量查询政务组织下的用户
	 * @author liyang
	 * @date 2019-04-04
	 * @param targetIds : 组织ID集合
	*/
	@Override
	public BaseVo getAllUserIdsByOaOrgIds(Long[] targetIds) {

		List<Long> targetIdList = new ArrayList<>();
		for (Long targetId : targetIds){
			targetIdList.add(targetId);
		}

		List<Long> userIdList = oaDepartmentDao.getAllUserIdsByOaOrgIdsSql(targetIdList);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(userIdList);

		return baseVo;
	}

	/**
	 * 获取党建组织下所有用户
	 * @author liyang
	 * @date 2019-04-09
	 * @return : List<Long> ：用户ID集合
	 */
	@Override
	public List<Long> getAllUserIdsForOaOrg() {
		User user = new User();

		//设置筛选条件为用户可用
		user.setStatus(SysConst.USER_STATUS);

		List<Long> userIdList = oaDepartmentDao.getAllUserIdsForOaOrgSql(user);
		return userIdList;
	}
	/**
	 * 获取社区信息
	 * @return
	 */
	@Override
	public BaseVo getCommunity() {
		List<OaDepartment> byIdLike = oaDepartmentDao.findByIdLike(SysConst.SHE_QU_BAN_ID, null);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(byIdLike);
		return baseVo;
	}
	/**
	 * 根据社区id批量查询
	 * @param ids
	 * @return
	 */
	@Override
	public List<OaDepartment> batchSelectByIds(List<Long> ids) {
		List<OaDepartment> oaDepartments = oaDepartmentDao.batchSelectByIds(ids);
		return oaDepartments;
	}
	/**
	 * 根据当前用户查询社区
	 * @return
	 */
	@Override
	public BaseVo getCommunityByToken() {
		BaseVo baseVo = new BaseVo();
		UserInfo userInfo = getCurrentUserInfo();
		//取得这个人管辖的所有社区
		Long userId = userInfo.getId();
		List<OaDepartment> oaOrganizations = new ArrayList<>();
		OaDepartment byUserId = userRelCboDepDao.findByUserId(userId);
		//如果是社区办
		if (byUserId.getId().equals(Long.valueOf(SysConst.SHE_QU_BAN_ID))){
			oaOrganizations = oaDepartmentDao.findByIdLike(SysConst.SHE_QU_BAN_ID, "6");
			baseVo.setData(oaOrganizations);
		}else {
			oaOrganizations.add(byUserId);
			baseVo.setData(oaOrganizations);
		}
		return baseVo;
	}

	/**
     * 递归获取党建组织数据数
     * @param id    上层组织ID 默认0
     * @param oaDepartment  上层组织明细
     * @return
     * @author Liy
     */
	private List<OaDepartment> checkdownOaDepartment(Long id, OaDepartment oaDepartment){
		List<OaDepartment> oaDepartmentTop = new ArrayList<>();
        List<OaDepartment> oaDepartmentReturn = new ArrayList<>();
		List<OaDepartment> oad = new ArrayList<>();

		//先判断是否是有条件查询 如果ID不存在说明是顶级开始查询
		if(id.equals(0L)){

			//先查询出所有顶级列表
			List<OaDepartment> oaDepartmentList = oaDepartmentDao.findOaDepartmentIncloudIdSql(id);
			for(OaDepartment oa : oaDepartmentList){
				String idStr = String.valueOf(oa.getId());
				String substring = idStr.substring(0, idStr.length() - 3);
				if(StringUtils.isEmpty(substring)){
					oaDepartmentTop.add(oa);
				}
			}

			//循环顶级列表，开始递归查询插入
			for(OaDepartment o : oaDepartmentTop){
				List<OaDepartment> oaDepartmentTempList = this.checkdownOaDepartment(o.getId(),o);
                oaDepartmentReturn.addAll(oaDepartmentTempList);
			}
            return oaDepartmentReturn;
		}else {
			//如果ID存在  说明是递归或者带条件查询,先查出此id的组织
			OaDepartment oaDepartmentThis = oaDepartmentDao.findOaDepartmentByIdSql(id);

			//根据ID查询组织结构下级明细
			List<OaDepartment> oaDepartmentList = oaDepartmentDao.findOaDepartmentIncloudIdSql(id);

			//说明有下级组织
            for(OaDepartment o :oaDepartmentList){

                //每个下级组织只比此级位数高3位
                if(o.getId().toString().length() == (oaDepartmentThis.getId().toString().length() + 3)){
                    oaDepartmentTop.add(o);
                    oaDepartment.setOaDepartmentList(oaDepartmentTop);
                }else {
                    oad.add(o);
                }
            }

            //存在下下级  取出id-3位继续递归
            if(oad.size()>0){
                for(OaDepartment o : oad){
					Long inId = Long.valueOf(o.getId().toString().substring(0,(o.getId().toString().length()-3)));

                    //查询上级组织
                    OaDepartment oaDepartmentUp = oaDepartmentDao.findOaDepartmentByIdSql(inId);
                    oaDepartmentTop.remove(oaDepartmentUp);
                    List<OaDepartment> oaDepartmentTemList = this.checkdownOaDepartment(inId,oaDepartmentUp);

                    //再把新生成的嵌套json插入
                    oaDepartmentTop.addAll(oaDepartmentTemList);
                }
                oaDepartment.setOaDepartmentList(oaDepartmentTop);
                oaDepartmentReturn.add(oaDepartment);
                return oaDepartmentReturn;
            }else {
                oaDepartmentReturn.add(oaDepartment);
                return  oaDepartmentReturn;
            }

		}


	}


}
