package com.bit.module.pb.controller;

import com.bit.base.exception.BusinessException;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.Organization;
import com.bit.module.pb.bean.PartyDue;
import com.bit.module.pb.bean.PartyMember;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.service.PartyDueService;
import com.bit.module.pb.service.PartyMemberService;
import com.bit.module.pb.service.impl.PartyMemberServiceImpl;
import com.bit.module.pb.vo.*;
import com.bit.utils.CheckUtil;
import com.bit.utils.ExcelHandler;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * 党费接口
 *
 * @author generator
 */
@RestController
@RequestMapping(value = "/partyDue")
public class PartyDueController {
    private static final Logger logger = LoggerFactory.getLogger(PartyDueController.class);
    @Autowired
    private PartyDueService partyDueService;

    @Autowired
    private PartyMemberService partyMemberService;

    @Autowired
    private OrganizationService organizationService;

    @Value("${party.due.charge.time}")
    private Integer chargeAt;

    @Value("${party.due.export.excel.password}")
    private String excelPassword;

    /**
     * 分页查询PartyDue列表 党员个人交纳情况表
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PartyDueVO partyDueVO) {
        //分页对象，前台传递的包含查询的参数
        return partyDueService.findByOrgConditionPage(partyDueVO);
    }

    /**
     * 统计月度党费收费情况
     */
    @PostMapping("/count")
    public BaseVo count(@RequestBody PartyDue partyDue) {
        CheckUtil.notNull(partyDue.getOrgId());
        CheckUtil.notNull(partyDue.getYear());
        //分页对象，前台传递的包含查询的参数
        return partyDueService.countPartyDue(partyDue);
    }
	/**
	 * APP查询个人党费
	 * @param partyDueVO
	 * @return
	 */
    @PostMapping("/member/listPage")
    public BaseVo memberListPage(@RequestBody PartyDueVO partyDueVO) {
        PartyDueVO toQuery = new PartyDueVO();
        toQuery.setYear(partyDueVO.getYear());
        toQuery.setPageNum(partyDueVO.getPageNum());
        toQuery.setPageSize(partyDueVO.getPageSize());
        return partyDueService.findByMemberConditionPage(toQuery);
    }

    /**
     * 新增PartyDue
     *
     * @param partyDue PartyDue实体
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody PartyDue partyDue) {
        partyDueService.add(partyDue);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 修改PartyDue
     *
     * @param partyDue PartyDue实体
     * @return
     */
    @PostMapping("/amount/modify")
    public BaseVo modifyPartyDue(@RequestBody PartyDue partyDue) {
        return partyDueService.updateAmountById(partyDue);
    }

	/**
	 * 批量设置修改金额接口
	 * @param partyDueBatchVO
	 */
    @PostMapping("/amount/batch/modify")
    public BaseVo batchModifyPartyDue(@RequestBody PartyDueBatchVO partyDueBatchVO) {
        partyDueService.batchModifyAmount(partyDueBatchVO);
        return new BaseVo();
    }

    /**
     * 党员交纳党费明细
     * @param partyDueVO
     * @return
     */
    @PostMapping("/monthly/listPage")
    public BaseVo listPageMonthly(@RequestBody PartyDueVO partyDueVO) {
        return partyDueService.findPersonalMonthlyPartyDue(partyDueVO);
    }

    /**
     * 根据主键ID删除PartyDue
     *
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        partyDueService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }
	/**
	 * 每月定时生成党员党费数据
	 */
    @GetMapping("/generate")
    public BaseVo generate() {
        partyDueService.createPartyDueEveryMonth();
        return new BaseVo();
    }
	/**
	 * 导出党员个人党费
	 * @param year
	 * @param month
	 * @param orgId
	 * @return
	 */
    @PostMapping("/personal/export")
    public void exportPartyDue(@RequestParam(name = "year") Integer year, @RequestParam(name = "month") Integer month,
                               @RequestParam(name = "orgId", required = false) String orgId,
                               HttpServletResponse response) {
        try {
            List<PersonalPartyDueExportVO> personalPartyDueExportVOList = partyDueService.findExportPersonalPartyDue(
                    year,
                    month,
                    orgId);

            AtomicInteger num = new AtomicInteger(0);
            personalPartyDueExportVOList.forEach(due -> {
                due.setId(num.addAndGet(1));
                // 修改导出党费金额单位
                due.determineExportParam();
            });
            ExcelHandler.exportExcelFile(response,
                                         personalPartyDueExportVOList,
                                         PersonalPartyDueExportVO.class,
                                         "党员个人党费交纳情况表");
        } catch (IOException e) {
            logger.error("党费导出异常 : {}", e);
        }
    }

	/**
	 * 党费模板下载
	 * @param orgId
	 * @param response
	 */
    @PostMapping("/personal/export/template")
    public void exportPartyDueTemplate(@RequestParam(name = "orgId", required = false) String orgId,
                                       HttpServletResponse response) {
        try {
            LocalDate now = LocalDate.now();
            List<PersonalPartyDueExportVO> template = partyDueService.findExportPersonalPartyDue(now.getYear(),
                                                                                                 now.getMonthValue(),
                                                                                                 orgId);
            AtomicInteger num = new AtomicInteger(0);
            template.forEach(due -> {
                due.setId(num.addAndGet(1));
                due.setAmount(null);
                due.setBase(null);
                due.setRemark(null);
                due.setPaidAmount(null);
            });

            ExcelHandler.exportProtection(response,
                                          template,
                                          PersonalPartyDueExportVO.class,
                                          "党员个人党费交纳情况模板表",
                                          Arrays.asList(0, 1, 2, 3),
                                          excelPassword);
        } catch (IOException e) {
            logger.error("模板导出异常 : {}", e);
        }
    }
	/**
	 * 导出数据
	 * @param year
	 * @param orgId
	 * @return
	 */
    @PostMapping("/monthly/export")
    public void exportPersonalMonthlyPartyDue(@RequestParam(name = "year") Integer year,
                                              @RequestParam(name = "orgId", required = false) String orgId,
                                              HttpServletResponse response) {
        try {
            List<PersonalMonthlyPartyDueVO> personalMonthlyPartyDueVOS = partyDueService.exportPersonalMonthlyPartyDue(
                    year,
                    orgId);
            List<MonthlyPersonalPartyDueExportVO> exportData = determineMonthlyPersonalExportData(
                    personalMonthlyPartyDueVOS);
            ExcelHandler.exportExcelFile(response, exportData, MonthlyPersonalPartyDueExportVO.class, "党员缴纳党费明细表");
        } catch (Exception e) {
            logger.error("党费导出异常 : {}", e);
        }
    }
	/**
	 * 批量导入党费
	 * @param excel
	 */
    @PostMapping(value = "/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseVo excelUpload(@RequestPart(value = "excel") MultipartFile excel) {
        LocalDate now = LocalDate.now();
        if (chargeAt < now.getDayOfMonth()) {
            logger.warn("无法在 {} 月 {} 日之后, 编辑当前记录", now.getMonthValue(), chargeAt);
            throw new BusinessException("无法修改已归档的党费数据");
        }
        List<PartyDueImportVO> importData = ExcelHandler.importExcelFile(excel, 0, 1, PartyDueImportVO.class);
        List<PartyDueImportVO> collect = filterIdCardIfEmpty(importData);
        // 身份证不能为空
        if (CollectionUtils.isNotEmpty(collect)) {
            return throwIdCardSerialNoIfEmpty(collect);
        }

        List<String> idCardList = importData.stream().map(PartyDueImportVO::getIdCard).collect(Collectors.toList());
        List<PartyMember> members = partyMemberService.findByIdCards(idCardList);
        List<PartyMember> notNormalStatusMember = findNotNormalStatusMember(members);

        List<PartyMember> insertPartyDueMember = new ArrayList<PartyMember>(CollectionUtils.subtract(members,notNormalStatusMember));

        if (CollectionUtils.isEmpty(insertPartyDueMember)) {
            return new BaseVo();
        }

        List<PartyDue> partyDueImportData = obtainInsertPartyDueData(importData, insertPartyDueMember);
        partyDueService.batchUpdate(partyDueImportData);
        return new BaseVo();
    }


    // 是否有非正常状态的党员
    private List<PartyMember> findNotNormalStatusMember(List<PartyMember> members) {
        // todo 之后加上判断是否党员是否属于当前管理员
        return members.stream().filter(partyMember -> partyMember.getStatus() !=
                                                      PartyMemberServiceImpl.PMStatusEnum.NORMAL.getValue() &&
                                                      partyMember.getStatus() !=
                                                      PartyMemberServiceImpl.PMStatusEnum.DRAFT.getValue()).collect(
                Collectors.toList());
    }

    private List<PartyDue> obtainInsertPartyDueData(List<PartyDueImportVO> importData, List<PartyMember> members) {
        List<Organization> allOrganization = organizationService.findAll(null);
        Date insertTime = new Date();
        Map<String, PartyMember> memberMap = members.stream()
                                                    .collect(Collectors.toMap(PartyMember::getIdCard,
                                                                                       partyMember -> partyMember));
        Map<String, Organization> organizationMap = allOrganization.stream()
                                                                   .collect(Collectors.toMap(Organization::getId,
                                                                                             organization -> organization));

        return importData.stream()
                         .filter(due -> memberMap.containsKey(due.getIdCard()))
                         .filter(due -> due.getBase() != null && due.getAmount() != null)
                         .map(data -> {
                             PartyMember partyMember = memberMap.get(data.getIdCard());
                             PartyDue partyDue = new PartyDue();
                             partyDue.setMemberId(Integer.valueOf(partyMember.getId().toString()));
                             partyDue.setOrgId(partyMember.getOrgId());
                             partyDue.setOrgName(organizationMap.get(partyMember.getOrgId()).getName());
                             partyDue.setBase((int) (data.getBase() * 100));
                             partyDue.setAmount((int) (data.getAmount() * 100));
                             partyDue.setPaidAmount((int) (data.getPaidAmount() * 100));
                             partyDue.setRemark(data.getRemark());
                             partyDue.setInsertTime(insertTime);
                             return partyDue;
                         })
                         .collect(Collectors.toList());
    }

    private BaseVo throwIdCardSerialNoIfEmpty(List<PartyDueImportVO> collect) {
        List<Integer> emptyIdCardNoList = collect.stream().map(PartyDueImportVO::getId).collect(Collectors.toList());
        logger.error("缺失身份证序号 : {}", emptyIdCardNoList);
        BaseVo baseVo = new BaseVo();
        baseVo.setMsg("身份证号不能为空");
        baseVo.setCode(-1);
        return baseVo;
    }

    private List<PartyDueImportVO> filterIdCardIfEmpty(List<PartyDueImportVO> importData) {
        return importData.stream().filter(data -> StringUtils.isEmpty(data.getIdCard())).collect(Collectors.toList());
    }

    private List<MonthlyPersonalPartyDueExportVO> determineMonthlyPersonalExportData(
            List<PersonalMonthlyPartyDueVO> personalMonthlyPartyDueVOS) {
        List<MonthlyPersonalPartyDueExportVO> monthlyPersonalPartyDueExportVOS = new ArrayList<>();
        AtomicInteger num = new AtomicInteger(0);
        personalMonthlyPartyDueVOS.forEach(monthlyPartyDueVO -> {

            MonthlyPersonalPartyDueExportVO monthlyPersonalPartyDueExportVO = new MonthlyPersonalPartyDueExportVO();
            monthlyPersonalPartyDueExportVO.setId(num.addAndGet(1));
            monthlyPersonalPartyDueExportVO.setMemberName(monthlyPartyDueVO.getName());
            monthlyPersonalPartyDueExportVO.setOrgName(monthlyPartyDueVO.getOrgName());
            setExportMonthlyData(monthlyPartyDueVO, monthlyPersonalPartyDueExportVO);

            // 防止因为一月没有数据导致数据为空
            if (CollectionUtils.isNotEmpty(monthlyPartyDueVO.getPartyDues()) &&
                (monthlyPersonalPartyDueExportVO.getBase() == null ||
                 monthlyPersonalPartyDueExportVO.getAmount() == null)) {
                monthlyPersonalPartyDueExportVO.setBase(
                        (double) monthlyPartyDueVO.getPartyDues().get(0).getBase() / 100);
                monthlyPersonalPartyDueExportVO.setAmount(
                        (double) monthlyPartyDueVO.getPartyDues().get(0).getAmount() / 100);
            }

            monthlyPersonalPartyDueExportVOS.add(monthlyPersonalPartyDueExportVO);
        });
        return monthlyPersonalPartyDueExportVOS;
    }

    private void setExportMonthlyData(PersonalMonthlyPartyDueVO monthlyPartyDueVO,
                                      MonthlyPersonalPartyDueExportVO monthlyPersonalPartyDueExportVO) {
        if (CollectionUtils.isEmpty(monthlyPartyDueVO.getPartyDues())) {
            return;
        }
        for (MonthlyPartyDueDetailVO partyDue : monthlyPartyDueVO.getPartyDues()) {
            matchMonthData(partyDue, monthlyPersonalPartyDueExportVO);
        }
    }

    private void matchMonthData(MonthlyPartyDueDetailVO partyDue,
                                MonthlyPersonalPartyDueExportVO monthlyPersonalPartyDueExportVO) {
        monthlyPersonalPartyDueExportVO.setMonthData(partyDue);
    }
}
