package com.bit.module.pb.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.MonthlyPartyDue;
import com.bit.module.pb.service.MonthlyPartyDueService;
import com.bit.utils.ExcelHandler;
import com.bit.module.pb.vo.MonthlyOrganizationPartyDueExportVO;
import com.bit.module.pb.vo.MonthlyPartyDueVO;
import com.bit.module.pb.vo.OrganizationMonthlyPartyDueVO;
import com.bit.utils.CheckUtil;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 月度党费接口
 * @author generator
 */
@RestController
@RequestMapping(value = "/monthlyPartyDue")
public class MonthlyPartyDueController {
	private static final Logger logger = LoggerFactory.getLogger(MonthlyPartyDueController.class);
	@Autowired
	private MonthlyPartyDueService monthlyPartyDueService;
	

    /**
     * 分页查询MonthlyPartyDue列表
     * 下级党组织交纳情况表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody MonthlyPartyDueVO monthlyPartyDueVO) {
    	//分页对象，前台传递的包含查询的参数

        return monthlyPartyDueService.findByConditionPage(monthlyPartyDueVO);
    }

    /**
     * 统计月度党费收费情况
     *
     */
    @PostMapping("/count")
    public BaseVo count(@RequestBody MonthlyPartyDue monthlyPartyDue) {
        CheckUtil.notNull(monthlyPartyDue.getOrgId());
        CheckUtil.notNull(monthlyPartyDue.getYear());
        //分页对象，前台传递的包含查询的参数
        return monthlyPartyDueService.countPartyDue(monthlyPartyDue);
    }

    /**
     * 新增MonthlyPartyDue
     *
     * @param monthlyPartyDue MonthlyPartyDue实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@Valid @RequestBody MonthlyPartyDue monthlyPartyDue) {
        monthlyPartyDueService.add(monthlyPartyDue);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改MonthlyPartyDue
     *
     * @param monthlyPartyDue MonthlyPartyDue实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Valid @RequestBody MonthlyPartyDue monthlyPartyDue) {
		monthlyPartyDueService.update(monthlyPartyDue);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除MonthlyPartyDue
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        monthlyPartyDueService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 生成下级组织机构某年某月党费记录
     * @param year
     * @param month
     * @return
     */
    @GetMapping("/subOrganization/{year}/{month}/generate")
    public BaseVo addMonthlyPartyDueForSubordinates(@PathVariable Integer year, @PathVariable Integer month) {
        LocalDate createDate = LocalDate.of(year, month, 15);
        monthlyPartyDueService.addMonthlyPartyDueForAllOrganization(createDate);
        return new BaseVo();
    }

    @PostMapping("/organization/export")
    public void exportMonthlyOrganizationPartyDue(@RequestParam(name = "year") Integer year,
                                                  @RequestParam(name = "orgId", required = false) String orgId,
                                                  HttpServletResponse response) {
        try {
            List<OrganizationMonthlyPartyDueVO> organizationMonthlyPartyDueVOS = monthlyPartyDueService
                    .findOrganizationMonthlyPartyDue(year, orgId);
            List<MonthlyOrganizationPartyDueExportVO> exportData = determineOrganizationMonthlyExportData(organizationMonthlyPartyDueVOS);

            ExcelHandler.exportExcelFile(response, exportData, MonthlyOrganizationPartyDueExportVO.class, "党组织交纳情况表");
        } catch (Exception e) {
            logger.error("党费导出异常 : {}", e);
        }
    }

    private List<MonthlyOrganizationPartyDueExportVO> determineOrganizationMonthlyExportData(
            List<OrganizationMonthlyPartyDueVO> organizationMonthlyPartyDueVOS) {

        List<MonthlyOrganizationPartyDueExportVO> monthlyPersonalPartyDueExportVOS = new ArrayList<>();
        AtomicInteger num = new AtomicInteger(0);
        organizationMonthlyPartyDueVOS.forEach(orgMonthlyPartyDue -> {
            MonthlyOrganizationPartyDueExportVO monthlyOrganizationPartyDueExportVO =
                    new MonthlyOrganizationPartyDueExportVO();
            monthlyOrganizationPartyDueExportVO.setId(num.addAndGet(1));
            monthlyOrganizationPartyDueExportVO.setOrgName(orgMonthlyPartyDue.getOrgName());
            setExportMonthlyData(orgMonthlyPartyDue, monthlyOrganizationPartyDueExportVO);
            monthlyPersonalPartyDueExportVOS.add(monthlyOrganizationPartyDueExportVO);
        });
        return monthlyPersonalPartyDueExportVOS;
    }

    private void setExportMonthlyData(OrganizationMonthlyPartyDueVO orgMonthlyPartyDue,
                                      MonthlyOrganizationPartyDueExportVO monthlyOrganizationPartyDueExportVO) {
        if (CollectionUtils.isEmpty(orgMonthlyPartyDue.getMonthlyPartyDues())) {
            return;
        }
        for (MonthlyPartyDue monthlyPartyDue : orgMonthlyPartyDue.getMonthlyPartyDues()) {
            matchMonthData(monthlyPartyDue, monthlyOrganizationPartyDueExportVO);
        }
    }

    private void matchMonthData(MonthlyPartyDue monthlyPartyDue,
                                MonthlyOrganizationPartyDueExportVO monthlyOrganizationPartyDueExportVO) {
        monthlyOrganizationPartyDueExportVO.setMonthData(monthlyPartyDue);
    }

}
