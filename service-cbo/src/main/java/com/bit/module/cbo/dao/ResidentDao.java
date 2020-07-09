package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.Resident;
import com.bit.module.cbo.vo.ResidentExcelVO;
import com.bit.module.cbo.vo.ResidentVO;
import com.bit.module.cbo.vo.ResidentPageVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ResidentDao {
	/**
	 * 校验手机号是否存在
	 * @param mobile
	 * @return
	 */
	int checkResidentExist(@Param(value = "mobile") String mobile);

	/**
	 * 根据手机号查询居民
	 * @param mobile
	 * @return
	 */
	Resident getResidentByMobile(@Param(value = "mobile") String mobile);

	/**
	 * 更新居民信息
	 * @param resident
	 * @return
	 */
	int updateResident(Resident resident);

	/**
	 * 新增居民
	 * @param resident
	 * @return
	 */
	int insertResident(Resident resident);

	/**
	 * 根据居民id查询居民信息
	 * @param residentId
	 * @return
	 */
	Resident getResidentById(@Param(value = "residentId")Long residentId);
	/**
	 * 居民信息分页查询
	 * @param residentPageModelVO
	 * @return
	 */
	List<ResidentPageVO> listPage(ResidentPageVO residentPageModelVO);

	/**
	 * 导出居民管理
	 * @param resident
	 * @return
	 */
	List<ResidentExcelVO> exportToExcel(Resident resident);

	/**
	 * 根据证件号码和证件类型返显居民记录
	 * @param residentVO
	 * @return
	 */
	Resident getResidentByCardNumAndCardType(ResidentVO residentVO);

	/**
	 * 获取所有居民基础信息
	 * @author liyang
	 * @date 2019-07-23
	 * @param residentPageVO : 分页
	 * @return : BaseVo
	 */
	List<Resident> findBaseResidentInfoSql(@Param("residentPageVO") ResidentPageVO residentPageVO);

	/**
	 * 校验居民证件号码是否重复
	 * @param cardType
	 * @param cardNum
	 * @param residentId
	 * @return
	 */
	Integer countResidentCardNumExist(@Param(value = "cardType")Integer cardType,@Param(value = "cardNum")String cardNum,@Param(value = "residentId")Long residentId);

	/**
	 * 根据id删除居民
	 * @param id
	 * @return
	 */
	int delById(@Param(value = "id")Long id);
}
