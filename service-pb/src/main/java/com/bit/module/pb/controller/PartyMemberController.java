package com.bit.module.pb.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.bit.base.vo.BaseVo;
import com.bit.module.pb.bean.*;
import com.bit.module.pb.feign.SysServiceFeign;
import com.bit.module.pb.service.ExServicemanService;
import com.bit.module.pb.service.OrganizationService;
import com.bit.module.pb.service.PartyMemberService;
import com.bit.module.pb.vo.DictVO;
import com.bit.module.pb.vo.partyMember.*;
import com.bit.utils.CheckUtils;
import com.bit.utils.DateUtil;
import com.bit.utils.ExcelHandler;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 党员接口
 *
 * @author generator
 */
@Slf4j
@RestController
@RequestMapping(value = "/partyMember")
public class PartyMemberController {

    @Autowired
    private PartyMemberService partyMemberService;

    @Autowired
    private ExServicemanService exServicemanService;

    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private SysServiceFeign sysServiceFeign;

    /**
     * 选择党员
     *
     * @param query
     * @return
     */
    @PostMapping("/choiceListPage")
    public BaseVo choiceListPage(@Valid @RequestBody PartyMemberQuery query) {
        if (query.getStatus() != null) {
            query.setStatuses(Arrays.asList(new Integer[]{query.getStatus()}));
        }
        PageInfo<PartyMemberPageVO> pageInfo = partyMemberService.findByConditionPage(query);
        return new BaseVo(pageInfo);
    }

    /**
     * 分页查询PartyMember列表
     * <p>
     * 参数：
     * 姓名：name
     * 党支部：orgId
     * 党籍状态：memberType
     * 状态：status
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody PartyMemberQuery partyMemberQuery) {
        PageInfo pageInfo = partyMemberService.findByPMStatusPage(partyMemberQuery);
        return new BaseVo(pageInfo);
    }

    /**
     * 获取退伍党员分页
     *
     * @param partyMemberQuery
     * @return
     */
    @PostMapping("/exServiceman/listPage")
    public BaseVo listExServicemanPage(@RequestBody PartyMemberQuery partyMemberQuery) {
        List<Organization> organizationList = organizationService.findByBasicUnit(null);
        if (organizationList.size() == 0) {
            partyMemberQuery.setOrgId(Long.parseLong(partyMemberService.getUserInfo().getPbOrgId()));
        }
        PageInfo pageInfo = partyMemberService.findExServicemanPage(partyMemberQuery);
        return new BaseVo(pageInfo);
    }

    /**
     * 根据党员ID获取信息
     *
     * @param id
     * @return
     */
    @GetMapping("/query/{id}")
    public BaseVo query(@PathVariable(value = "id") Long id) {
        // 获取党员信息
        PartyMember partyMember = partyMemberService.findById(id);
        // 获取退伍军人信息
        ExServiceman exServiceman = exServicemanService.findByIdCard(partyMember.getIdCard());
        PartyMemberSummary partyMemberSummary = new PartyMemberSummary();
        BeanUtils.copyProperties(partyMember, partyMemberSummary);
        partyMemberSummary.setExServiceman(exServiceman);
        return new BaseVo(partyMemberSummary);
    }

    /**
     * 根据身份证获取党员信息
     *
     * @param idCard
     * @return
     */
    @GetMapping("/findByIdCard/{idCard}")
    public BaseVo queryByIdCard(@PathVariable(value = "idCard") String idCard) {
        PartyMember partyMember = partyMemberService.findByIdCard(idCard);
        return new BaseVo(partyMember);
    }

    /**
     * 根据用户ID获取党员信息
     *
     * @return
     */
    @GetMapping("/findByUserId")
    public BaseVo queryByUserId() {
        PartyMember partyMember = partyMemberService.findByIdCard(null);
        return new BaseVo(partyMember);
    }

    /**
     * 编辑 PartyMember（待完善）
     *
     * @param partyMember PartyMember实体
     * @return
     */
    @PostMapping("/modify")
    public BaseVo modify(@Validated(PartyMemberParams.ModifyPartyMember.class) @RequestBody PartyMemberParams partyMember) {
        partyMemberService.update(partyMember);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 完善党员信息 PartyMember
     *
     * @param partyMember
     * @return
     */
    @PostMapping("/perfect")
    public BaseVo perfect(@Validated(PartyMemberParams.ModifyPartyMember.class) @RequestBody PartyMemberParams partyMember) {
        partyMemberService.perfect(partyMember);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 根据主键ID删除PartyMember
     *
     * @param id
     * @return
     */
    @Transactional
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id) {
        partyMemberService.delete(id);
        BaseVo baseVo = new BaseVo();
        return baseVo;
    }

    /**
     * 导出在线党员模版
     * 或列表
     *
     * @param partyMemberQuery
     * @param response
     */
    @PostMapping("/export/onLine/Template")
    public void exportPartyMemberTemplate(PartyMemberQuery partyMemberQuery, HttpServletResponse response) throws IOException {
        List<PartyMemberExportVO> template = new ArrayList<>();
        PartyMemberExportVO partyMemberExportVO = new PartyMemberExportVO();
        template.add(partyMemberExportVO);
        ExcelHandler.exportExcelFile(response, template, PartyMemberExportVO.class, "党员导入模版");
    }

    /**
     * 导出在线党员
     * 或列表
     *
     * @param partyMemberQuery
     * @param response
     */
    @PostMapping("/export/onLine")
    public void exportPartyMember(PartyMemberQuery partyMemberQuery, HttpServletResponse response) throws IOException {
        List<PartyMemberExportVO> template = partyMemberService.findAll(partyMemberQuery);
        Map<String, String> peopleDict = getPeopleDict();
        if (peopleDict != null) {
            template.parallelStream().forEach(partyMemberExportVO -> {
                partyMemberExportVO.setNation(peopleDict.get(partyMemberExportVO.getNation()));
            });
        }
        String fileName = "在册党员_"+ DateUtil.date2String(new Date(),DateUtil.DatePattern.YYYYMMDDHHmmss2.getValue());
        ExcelHandler.exportExcelFile(response, template, PartyMemberExportVO.class, fileName);
    }

    private Map<String, String> getPeopleDict() {
        BaseVo dictList = sysServiceFeign.findByModule("people");
        if (dictList.getData() != null) {
            String dictVOs = JSON.toJSONString(dictList.getData());
            List<DictVO> dicts = JSON.parseArray(dictVOs, DictVO.class);
            Map<String, String> peopleDict = dicts.parallelStream().collect(Collectors.toMap(DictVO::getDictCode, DictVO::getDictName));
            return peopleDict;
        }
        return null;
    }

    /**
     * 导出停用党员模版
     * 或列表
     *
     * @param partyMember
     * @param response
     */
    @PostMapping("/export/disable")
    public void exportPartyMemberDisable(PartyMemberQuery partyMember, HttpServletResponse response) throws IOException {
        // 停用
        List<DisablePartyExportVO> template = partyMemberService.findDisableParty(partyMember);
        Map<String, String> peopleDict = getPeopleDict();
        if (peopleDict != null) {
            template.parallelStream().forEach(disablePartyExportVO -> {
                disablePartyExportVO.setNation(peopleDict.get(disablePartyExportVO.getNation()));
            });
        }
        ExcelHandler.exportExcelFile(response, template, DisablePartyExportVO.class, "停用党员");
    }

    /**
     * 导出转出党员
     *
     * @param partyMemberQuery
     * @param response
     */
    @PostMapping("/export/transfer")
    public void exportTransferPartyMember(PartyMemberQuery partyMemberQuery, HttpServletResponse response) throws IOException {
        List<TransferPartyMemberExportVO> template = partyMemberService.findTransferPartyMember(partyMemberQuery);
        Map<String, String> peopleDict = getPeopleDict();
        if (peopleDict != null) {
            template.parallelStream().forEach(transferPartyMemberExportVO -> {
                transferPartyMemberExportVO.setNation(peopleDict.get(transferPartyMemberExportVO.getNation()));
            });
        }
        ExcelHandler.exportExcelFile(response, template, TransferPartyMemberExportVO.class, "已转出党员");
    }

    /**
     * 导出退伍党员
     *
     * @param partyMemberQuery
     * @param response
     */
    @PostMapping("/export/exServiceman")
    public void exportExServiceman(PartyMemberQuery partyMemberQuery, HttpServletResponse response) throws IOException {
        List<PartyMemberExportVO> template = partyMemberService.findExServiceman(partyMemberQuery);
        Map<String, String> peopleDict = getPeopleDict();
        if (peopleDict != null) {
            template.parallelStream().forEach(partyMemberExportVO -> {
                partyMemberExportVO.setNation(peopleDict.get(partyMemberExportVO.getNation()));
            });
        }
        ExcelHandler.exportExcelFile(response, template, PartyMemberExportVO.class, "退役军人");
    }

    /**
     * 党员信息导入
     *
     * @param excel
     * @return
     */
    @Transactional
    @PostMapping(value = "/excel/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void excelUpload(HttpServletResponse response,
                            @RequestPart(value = "excel") MultipartFile excel) throws ClassNotFoundException {
        List<Organization> organizationList = organizationService.findAll(null);
        Map<String, String> orgMap = organizationList.stream().collect(Collectors.toMap(Organization::getName, Organization::getId));
        List<PartyMemberExportVO> importData = ExcelHandler.importExcelFile(excel, 0, 1, PartyMemberExportVO.class);
        Map<String, String> peopleDict = new HashMap<>();
        BaseVo dictList = sysServiceFeign.findByModule("people");
        if (dictList.getData() != null) {
            String dictVOs = JSON.toJSONString(dictList.getData());
            List<DictVO> dicts = JSON.parseArray(dictVOs, DictVO.class);
            peopleDict = dicts.parallelStream().collect(Collectors.toMap(DictVO::getDictName, DictVO::getDictCode));
        }
        // 校验数据合法性
        Map<Integer, Map<Integer, String>> msgMap = checkImportData(importData, orgMap, peopleDict);
        if (!msgMap.isEmpty()) {
            log.info("导入的党员信息有错误...");
            // 导出数据并附带批注
            ExportParams params = new ExportParams();
            params.setSheetName("党员信息");
            Workbook workbook = ExcelExportUtil.exportExcel(params, PartyMemberExportVO.class, importData);
            Sheet sheet = workbook.getSheetAt(0);
            // 给单元格添加错误批注信息
            Drawing patriarch = sheet.createDrawingPatriarch();
            for (int r = 1; r < sheet.getLastRowNum() + 1; r++) {
                Map<Integer, String> columnMap = msgMap.get(r);
                if (columnMap == null) {
                    continue;
                }
                Row row = sheet.getRow(r);
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    String msg = columnMap.get(c);
                    if (StringUtil.isEmpty(msg)) {
                        continue;
                    }
                    Cell column = row.getCell(c);
                    HSSFComment comment = (HSSFComment) patriarch.createCellComment(
                            new HSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                    // 输入批注信息
                    comment.setString(new HSSFRichTextString(msg));
                    // 将批注添加到单元格对象中
                    column.setCellComment(comment);
                }
            }
            try {
                response.setContentType("application/x-download;charset=iso8859-1");
                response.setHeader("Content-Disposition",
                        "attachment;filename=" + URLEncoder.encode("党员信息有误.xls", "utf8"));
                workbook.write(response.getOutputStream());
            } catch (IOException e) {
                log.error("excelUpload IOException:", e);
            }
            return;
        }
        log.info("导入的党员信息数据校验通过，准备插入数据库...");
        List<PartyMember> partyMemberList = new ArrayList<>();
        List<ExServiceman> exServicemanList = new ArrayList<>();
        if (importData.size() > 0) {
            Map<String, String> finalPeopleDict = peopleDict;
            importData.stream().forEach(partyMemberExportVO -> {
                PartyMember partyMember = new PartyMember();
                String orgId = orgMap.get(partyMemberExportVO.getOrgName());
                BeanUtils.copyProperties(partyMemberExportVO, partyMember);
                partyMember.setOrgId(orgId);
                partyMember.setName(partyMemberExportVO.getName());
                partyMember.setSex(partyMemberExportVO.getSex());
                partyMember.setBirthdate(partyMemberExportVO.getBirthdate());
                partyMember.setNation(finalPeopleDict.get(partyMemberExportVO.getNation()));
                partyMember.setEducation(partyMemberExportVO.getEducation());
                partyMember.setIdCard(partyMemberExportVO.getIdCard());
                partyMember.setMobile(partyMemberExportVO.getMobile());
                partyMember.setJoinTime(partyMemberExportVO.getJoinTime());
                partyMember.setPoliceStation(partyMemberExportVO.getPoliceStation());
                partyMember.setMemberType(partyMemberExportVO.getMemberType() == null ? 1 : partyMemberExportVO.getMemberType());
                partyMember.setCompany(partyMemberExportVO.getCompany());
                partyMember.setAddress(partyMemberExportVO.getAddress());

                partyMember.setStatus(PartyMember.judgeStatus(partyMember));
                partyMember.setInsertTime(DateUtil.getCurrentDate());
                // 退伍军人
                if (partyMemberExportVO.getOriginalTroops() != null
                        && partyMemberExportVO.getRetireTime() != null
                        && partyMemberExportVO.getIsSelfEmployment() != null) {
                    ExServiceman exServiceman = new ExServiceman();
                    BeanUtils.copyProperties(partyMemberExportVO, exServiceman);
                    exServiceman.setRetireTime(partyMemberExportVO.getRetireTime() == null ? null : partyMemberExportVO.getRetireTime());
                    exServiceman.setOrgName(partyMemberExportVO.getBranchName());
                    exServiceman.setIdCard(partyMemberExportVO.getIdCard());
                    exServicemanList.add(exServiceman);
                }
                partyMemberList.add(partyMember);
            });
            if (partyMemberList.size() > 0) {
                partyMemberService.batchAdd(partyMemberList);
            }
            if (exServicemanList.size() > 0) {
                exServicemanService.batchAdd(exServicemanList);
            }
        }
    }

    private Map<Integer, Map<Integer, String>> checkImportData(List<PartyMemberExportVO> importData, Map<String, String> orgMap, Map<String, String> peopleDict)
            throws ClassNotFoundException {
        int page = 0;
        final int size = 1000;

        List<PartyMember> partyMemberList = new ArrayList<>();

        int count = 0;
        do {
            List<PartyMember> members = partyMemberService.findAllLimit(page, size);
            count = members.size();
            partyMemberList.addAll(members);
            page++;
        } while (count == size);
        Set<String> idCardSet = partyMemberList.stream().map(PartyMember::getIdCard).collect(Collectors.toSet());
        Set<String> phoneSet = partyMemberList.stream().map(PartyMember::getMobile).collect(Collectors.toSet());
        List<String> idCardList = importData.stream().map(PartyMemberExportVO::getIdCard).collect(Collectors.toList());
        List<String> phoneList = importData.stream().map(PartyMemberExportVO::getMobile).collect(Collectors.toList());
        Map<Integer, Map<Integer, String>> map = new HashMap<>(importData.size());
        Class exportClass = Class.forName(PartyMemberExportVO.class.getName());
        Field[] fields = exportClass.getDeclaredFields();
        for (int i = 0; i < importData.size(); i++) {
            PartyMemberExportVO vo = importData.get(i);
            Map<Integer, String> msgMap = new HashMap<>(fields.length);
            boolean errorFlag = false;
            for (Field field : fields) {
                if (field.getAnnotation(Excel.class) == null) {
                    continue;
                }
                String fieldName = field.getName();
                int column = Integer.parseInt(field.getAnnotation(Excel.class).orderNum()) - 1;
                String fieldDesc = field.getAnnotation(Excel.class).name();
                switch (fieldName) {
                    case "orgName":
                        if (StringUtil.isEmpty(vo.getOrgName()) || StringUtil.isEmpty(orgMap.get(vo.getOrgName()))) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不存在");
                        }
                        break;
                    case "name":
                        if (StringUtil.isEmpty(vo.getName())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能为空");
                        }
                        break;
                    case "sex":
                        if (StringUtil.isEmpty(vo.getSex())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能为空");
                        }
                        if (Arrays.binarySearch(new String[]{"1", "2"}, vo.getSex()) < 0) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "格式不正确");
                        }
                        break;
                    case "idCard":
                        if (StringUtil.isEmpty(vo.getIdCard())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能为空");
                            break;
                        }
//                        if (!CheckUtils.isValidIdentityCard(vo.getIdCard())) {
//                            errorFlag = true;
//                            msgMap.put(column, fieldDesc + "格式不正确");
//                            break;
//                        }
                        if (vo.getIdCard().length() > 18) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "格式不正确");
                            break;
                        }
                        if (Collections.frequency(idCardList, vo.getIdCard()) > 1 || idCardSet.contains(vo.getIdCard())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "重复");
                            break;
                        }
                        break;
                    case "mobile":
                        if (StringUtil.isEmpty(vo.getMobile())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能为空");
                            break;
                        }
                        if (!CheckUtils.isMobile(vo.getMobile()) && !CheckUtils.isPhone(vo.getMobile())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "格式不正确");
                            break;
                        }
                        if (Collections.frequency(phoneList, vo.getMobile()) > 1 || phoneSet.contains(vo.getMobile())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "重复");
                            break;
                        }
                        break;
                    case "nation":
                        if (StringUtil.isEmpty(vo.getNation())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能为空");
                            break;
                        }
                        String dictCode = peopleDict.get(vo.getNation());
                        if (StringUtil.isEmpty(dictCode)) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "格式不正确");
                            break;
                        }
                        break;
                    case "education":
                        if (StringUtil.isEmpty(vo.getEducation())) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能为空");
                            break;
                        }
                        if (Arrays.binarySearch(new String[]{"1", "2", "3", "4", "5"}, vo.getEducation()) < 0) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "格式不正确");
                            break;
                        }
                        break;
                    case "policeStation":
                        if (vo.getPoliceStation() != null && vo.getPoliceStation().length() > 50) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能超过50字");
                            break;
                        }
                        break;
                    case "company":
                        if (vo.getCompany() != null && vo.getCompany().length() > 50) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能超过50字");
                            break;
                        }
                        break;
                    case "address":
                        if (vo.getAddress() != null && vo.getAddress().length() > 50) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能超过50字");
                            break;
                        }
                        break;
                    case "origin":
                        if (vo.getOrigin() != null && vo.getOrigin().length() > 20) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能超过20字");
                            break;
                        }
                        break;
                    case "originalTroops":
                        if (vo.getOriginalTroops() != null && vo.getOriginalTroops().length() > 30) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能超过30字");
                            break;
                        }
                        break;
                    case "branchName":
                        if (vo.getBranchName() != null && vo.getBranchName().length() > 30) {
                            errorFlag = true;
                            msgMap.put(column, fieldDesc + "不能超过30字");
                            break;
                        }
                        break;
                    default:
                        break;
                }
            }
            if (errorFlag) {
                map.put(i + 1, msgMap);
            }
        }
        return map;
    }

    @GetMapping("/exist")
    public BaseVo existMember(@RequestParam(value = "name", required = false) String name,
                              @RequestParam(value = "idCard", required = false) String idCard) {
        Boolean exist = partyMemberService.existPartyMember(name, idCard);
        return new BaseVo(exist);
    }

    @GetMapping("/inside/{idCard}")
    public BaseVo findInsideMemberByIdCard(@PathVariable("idCard") String idCard) {
        PartyMember partyMember = partyMemberService.findByIdCardAndInside(idCard);
        return new BaseVo(partyMember);
    }

    /**
     * 根据党组织id查询党员信息
     *
     * @param orgId
     * @return
     */
    @GetMapping("/findMembersByOrgId/{orgId}")
    public BaseVo findMembersByOrgId(@PathVariable(value = "orgId") String orgId) {
        return partyMemberService.findMembersByOrgId(orgId);
    }

    /**
     * 通讯录
     *
     * @return
     */
    @PostMapping("/addressBook")
    public BaseVo addressBook() {
        return partyMemberService.addressBook();
    }

    /**
     * 获取党员通讯录
     *
     * @param orgId
     * @return
     */
    @GetMapping("/contacts")
    public BaseVo getPartyMemberContacts(@RequestParam(value = "orgId", required = false) String orgId) {
        return partyMemberService.queryContacts(orgId);
    }

    /**
     * 根据身份证获取党员信息 查询正常的
     *
     * @param idCard
     * @return
     */
    @GetMapping("/findByIdCardWithStatus/{idCard}")
    public BaseVo findByIdCardWithStatus(@PathVariable(value = "idCard") String idCard) {
        PartyMember partyMember = partyMemberService.findByIdCardWithStatus(idCard);
        return new BaseVo(partyMember);
    }

    /**
     * 三会一课 会议 和 学习计划选择人员专用 搜索人员
     *
     * @param partyMemberSearch
     * @return
     */
    @PostMapping("/getPartyMembersByLikeName")
    public BaseVo getPartyMembersByLikeName(@RequestBody PartyMemberSearch partyMemberSearch) {
        return partyMemberService.getPartyMembersByLikeName(partyMemberSearch);
    }

}
