package com.bit.module.oa.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.oa.bean.Company;
import com.bit.module.oa.enums.CompanyStatusEnum;
import com.bit.module.oa.service.CompanyService;
import com.bit.module.oa.utils.ExcelHandler;
import com.bit.module.oa.vo.company.CompanyExportVO;
import com.bit.module.oa.vo.company.CompanyPageVO;
import com.bit.module.oa.vo.company.CompanyVO;
import com.bit.module.oa.vo.company.SimpleCompanyVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Company的相关请求
 * @author generator
 */
@RestController
@RequestMapping(value = "/company")
public class CompanyController {
	private static final Logger logger = LoggerFactory.getLogger(CompanyController.class);
	@Autowired
	private CompanyService companyService;
	

    /**
     * 分页查询Company列表
     *
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody CompanyVO companyVO) {
    	//分页对象，前台传递的包含查询的参数

        return companyService.findByConditionPage(companyVO);
    }

    /**
     * 导出第三方公司文档
     * @param nature
     * @param name
     * @param response
     */
    @PostMapping("/export")
    public void export(@RequestParam(name = "nature", required = false) String nature,
                       @RequestParam(name = "name", required = false) String name,
                       HttpServletResponse response) {
        try {
            List<CompanyExportVO> companyExportVOList = companyService.findExportCompany(nature, name);
            AtomicLong num = new AtomicLong(1L);
            companyExportVOList.forEach(due -> due.setId(num.getAndAdd(1L)));
            ExcelHandler.exportExcelFile(response, companyExportVOList, CompanyExportVO.class, "三方服务公司管理");
        } catch (IOException e) {
            logger.error("三方服务公司管理导出异常 : {}", e);
        }
    }

    /**
     * 根据主键ID查询Company
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {

        CompanyPageVO company = companyService.findById(id);
		BaseVo baseVo = new BaseVo();
		baseVo.setData(company);
		return baseVo;
    }
    
    /**
     * 新增Company
     *
     * @param company Company实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Company company) {
        company.checkAdd();
        companyService.add(company);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    /**
     * 修改Company
     *
     * @param company Company实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@RequestBody Company company) {
        company.checkUpdate();
		companyService.update(company);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }
    
    /**
     * 根据主键ID删除Company
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        companyService.delete(id);
		BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 启用第三方公司
     * @param id
     * @return
     */
    @PostMapping("/{id}/enable")
    public BaseVo enableCompany(@PathVariable("id") Long id) {
        companyService.convertStatus(id, CompanyStatusEnum.ENABLED.getKey());
        return new BaseVo();
    }

    /**
     * 停用第三方公司
     * @param id
     * @return
     */
    @PostMapping("/{id}/disable")
    public BaseVo disableCompany(@PathVariable("id") Long id) {
        companyService.convertStatus(id, CompanyStatusEnum.DISABLE.getKey());
        return new BaseVo();
    }

    /**
     * 第三方公司下拉列表
     * @return
     */
    @GetMapping("/list")
    public BaseVo findAll() {
        List<SimpleCompanyVO> company = companyService.findAll("id");
        return new BaseVo<>(company);
    }
}
