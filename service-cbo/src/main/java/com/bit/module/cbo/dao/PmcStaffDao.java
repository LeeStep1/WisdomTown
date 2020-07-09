package com.bit.module.cbo.dao;

import com.bit.module.cbo.bean.PmcCompany;
import com.bit.module.cbo.bean.PmcStaff;
import com.bit.module.cbo.vo.PmcStaffVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface PmcStaffDao {
	/**
	 * 校验物业工作人员手机号是否存在
	 * @param mobile
	 * @return
	 */
	int checkPmcStaffExist(@Param(value = "mobile")String mobile);

	/**
	 * 校验物业工作人员手机号是否存在（修改时调用）
	 * @param mobile
	 * @return
	 */
	int modifyCheckPmcStaffExist(@Param(value = "mobile")String mobile,@Param("id") Long id);

	/**
	 * 根据手机号查询物业工作人员信息
	 * @param mobile
	 * @return
	 */
	PmcStaff getPmcStaffByMobile(@Param(value = "mobile")String mobile);

	/**
	 * 根据id查询物业工作人员信息
	 * @param id
	 * @return
	 */
	PmcStaff getPmcStaffById(@Param(value = "id")Long id);


	/**
	 * 更新物业员工信息
	 * @param pmcStaff
	 * @return
	 */
	int updatePmcStaff(PmcStaff pmcStaff);

	/**
	 * 新增物业员工信息
	 * @param pmcStaff
	 * @return
	 */
	int insertPmcstaff(PmcStaff pmcStaff);

	/**
	 * 根据物业公司ID查询该公司下的员工个数
	 * @param companyId
	 * @return
	 */
	Integer staffByPmcCountSql(Long companyId);

	/**
	 * 修改物业公司人员
	 * @param pmcStaff 修改明细
	 */
	void modify(@Param("pmcStaff") PmcStaff pmcStaff);

	/**
	 * 根据ID获取用户详情
	 * @param id
	 * @return
	 */
	PmcStaff findByIdSql(Long id);

	/**
	 * 返回物业人员列表
	 * @param pmcStaffVO 查询条件
	 * @return
	 */
	List<PmcStaff> findAll(@Param("pmcStaff") PmcStaffVO pmcStaffVO);

	/**
	 * 根据物业员工查询所属公司明细
	 * @param StaffId 员工ID
	 * @return
	 */
	PmcCompany findCompanyByStaffId(@Param("StaffId") Long StaffId);

	/**
	 * 查询物业公司下面的员工
	 * @author liyang
	 * @date 2019-07-23
	 * @param companyId : 物业公司ID
	 * @param status : 员工状态
	 * @return : Integer
	 */
	Integer staffCountByCompanyId(@Param("companyId") Long companyId,@Param("status") Integer status);

	/**
	 * 根据id删除记录
	 * @param id
	 * @return
	 */
	int delById(@Param(value = "id")Long id);

	/**
	 * 根据id批量查询物业人员信息
	 * @param ids
	 * @return
	 */
	List<PmcStaff> batchSelectByIds(@Param(value = "ids") List<Long> ids);

	/**
	 * 多参数查询
	 * @param pmcStaff
	 * @return
	 */
	List<PmcStaff> findByParam(PmcStaff pmcStaff);
}
