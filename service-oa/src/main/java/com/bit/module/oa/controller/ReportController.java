package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Report;
import com.bit.module.oa.service.ReportService;
import com.bit.module.oa.vo.report.ReportVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Report的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/report")
public class ReportController {
	private static final Logger logger = LoggerFactory.getLogger(ReportController.class);
	@Autowired
	private ReportService reportService;
	

    /**
     * 分页查询Report列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody ReportVO reportVO) {
    	//分页对象，前台传递的包含查询的参数

        return reportService.findByConditionPage(reportVO);
    }

    /**
     * 根据主键ID查询Report
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Report report = reportService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(report);
		return baseVo;
    }
    
    /**
     * 新增Report
     *
     * @param report Report实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Report report) {
        reportService.add(report);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Report
     *
     * @param report Report实体
     * @return
     */
    @PostMapping("/confirm")
    public BaseVo modify(@RequestBody Report report) {
		reportService.confirm(report);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
}
