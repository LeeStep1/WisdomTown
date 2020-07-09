package com.bit.module.oa.controller;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Risk;
import com.bit.module.oa.service.RiskService;
import com.bit.module.oa.utils.EmojiRegexUtil;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.risk.RiskExportQO;
import com.bit.module.oa.vo.risk.RiskExportVO;
import com.bit.module.oa.vo.risk.RiskVO;
import com.bit.utils.CheckUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Risk的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/risk")
public class RiskController {
	private static final Logger logger = LoggerFactory.getLogger(RiskController.class);
	@Autowired
	private RiskService riskService;

    /**
     * 分页查询Risk列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody RiskVO riskVO) {
    	//分页对象，前台传递的包含查询的参数

        return riskService.findByConditionPage(riskVO);
    }

    /**
     * 根据主键ID查询Risk
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        Risk risk = riskService.findDetailById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(risk);
		return baseVo;
    }
    
    /**
     * 新增Risk
     *
     * @param risk Risk实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Risk risk) {
        CheckUtil.notNull(risk.getName());
        CheckUtil.notNull(risk.getReportDepId());
        CheckUtil.notNull(risk.getReportDepName());
        CheckUtil.notNull(risk.getReportLocation());
        CheckUtil.notNull(risk.getLat());
        CheckUtil.notNull(risk.getLng());
        CheckUtil.notNull(risk.getExceptionContent());
        CheckUtil.notNull(risk.getNeedFix());
        riskService.add(risk);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Risk
     *
     * @param risk Risk实体
     * @return
     */
    @PostMapping("/feedback")
    public BaseVo feedback(@RequestBody Risk risk) {
        CheckUtil.notNull(risk.getId());
		riskService.feedback(risk);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 隐患报告导出
     * @param no
     * @param name
     * @param reporterName
     * @param reportDepId
     * @param status
     * @param reportStartTime
     * @param reportEndTime
     * @param response
     */
    @PostMapping("/export")
    public void export(@RequestParam(name = "no", required = false) String no,
                       @RequestParam(name = "name", required = false) String name,
                       @RequestParam(name = "reporterName", required = false) String reporterName,
                       @RequestParam(name = "reportDepId", required = false) Long reportDepId,
                       @RequestParam(name = "status", required = false) Integer status,
                       @RequestParam(name = "reportStartTime", required = false) Long reportStartTime,
                       @RequestParam(name = "reportEndTime", required = false) Long reportEndTime,
                       HttpServletResponse response) {
        try {
            Date startTime = reportStartTime == null ? null : new Date(reportStartTime);
            Date endTime = reportEndTime == null ? null : new Date(reportEndTime);
            RiskExportQO riskExportQO = new RiskExportQO();
            riskExportQO.setReporterName(reporterName);
            riskExportQO.setNo(no);
            riskExportQO.setName(name);
            riskExportQO.setReportDepId(reportDepId);
            riskExportQO.setStatus(status);
            riskExportQO.setReportStartTime(startTime);
            riskExportQO.setReportEndTime(endTime);
            List<RiskExportVO> riskExportVOList = riskService.exportByConditionPage(riskExportQO);
            AtomicLong num = new AtomicLong(1L);
            riskExportVOList.forEach(risk -> risk.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, riskExportVOList, RiskExportVO.class, "隐患报告管理");
        } catch (IOException e) {
            logger.error("隐患报告管理导出异常 : {}", e);
        }
    }
}
