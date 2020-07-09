package com.bit.module.sv.controller;

import com.bit.base.vo.BaseVo;
import com.bit.base.vo.SuccessVo;
import com.bit.module.sv.bean.Law;
import com.bit.module.sv.bean.Regulation;
import com.bit.module.sv.bean.RegulationContent;
import com.bit.module.sv.service.LawService;
import com.bit.module.sv.service.RegulationService;
import com.bit.module.sv.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.bit.module.sv.utils.StringUtils.getSourceByHttpServletRequest;

/**
 * 法律法规api
 *
 * @author decai.liu
 * @create 2019-07-16
 */
@RestController
@RequestMapping("/laws")
public class LawController {

    @Autowired
    private LawService lawService;

    @Autowired
    private RegulationService regulationService;

    // -------------------------------------------- 法律 ------------------------------------------------------------

    @PostMapping(name = "新增法律条文", path = "/add")
    public BaseVo addLaw(@RequestBody @Validated LawVO lawVO) {
        lawService.addLaw(lawVO);
        return new BaseVo();
    }

    @PostMapping(name = "法律分页查询")
    public BaseVo listLaws(@RequestBody LawVO lawVO, HttpServletRequest request) {
        lawVO.setRange(getSourceByHttpServletRequest(request));
        return lawService.listLaws(lawVO);
    }

    @GetMapping(name = "所有法律列表", path = "/all")
    public BaseVo all(HttpServletRequest request) {
        return lawService.allLaws(getSourceByHttpServletRequest(request));
    }

    @DeleteMapping(name = "根据ID删除法律", path = "/{id}")
    public BaseVo deleteLaw(@PathVariable("id") Long id) {
        lawService.deleteById(id);
        return new SuccessVo();
    }

    @PostMapping(name = "编辑法律", path = "/update")
    public BaseVo updateLaw(@Validated(Law.Update.class) @RequestBody Law law) {
        lawService.update(law);
        return new SuccessVo();
    }

    // ------------------------------------------------ 法规 ------------------------------------------------

    @PostMapping(name = "根据法律查询法规列表", path = "/regulation/list")
    public BaseVo regulationList(@RequestBody RegulationVO regulation, HttpServletRequest request) {
        regulation.setRange(getSourceByHttpServletRequest(request));
        return regulationService.findByCondition(regulation);
    }

    @PostMapping(name = "新增法规", path = "/regulation/add")
    public BaseVo regulationAdd(@Validated(Regulation.Add.class) @RequestBody Regulation regulation) {
        regulationService.add(regulation);
        return new SuccessVo();
    }

    @PostMapping(name = "编辑法规", path = "/regulation/update")
    public BaseVo updateRegulation(@Validated(Regulation.Update.class) @RequestBody Regulation regulation) {
        regulationService.update(regulation);
        return new SuccessVo();
    }

    @DeleteMapping(name = "根据ID删除法规包括字节点及正文", path = "/regulation/{id}")
    public BaseVo deleteRegulation(@PathVariable("id") Long id) {
        regulationService.delete(id);
        return new SuccessVo();
    }

    @GetMapping(name = "根据法规ID查询法规正文列表", path = "/{regulationId}/content/list")
    public BaseVo listContentByRegulationId(@PathVariable("regulationId") Long regulationId) {
        return regulationService.findContentByRegulationId(regulationId);
    }

    // ------------------------------------- 法规正文 --------------------------------------------------------------------

    @PostMapping(name = "法规正文分页", path = "/content/page")
    public BaseVo contentListPage(@RequestBody RegulationContentVO content) {
        return regulationService.contentListPage(content);
    }

    @PostMapping(name = "新增法规正文", path = "/content/add")
    public BaseVo regulationContentAdd(@Validated(RegulationContent.Add.class) @RequestBody RegulationContent content) {
        regulationService.addContent(content);
        return new SuccessVo();
    }

    @PostMapping(name = "编辑法规正文", path = "/content/update")
    public BaseVo regulationContentUpdate(
            @Validated(RegulationContent.Update.class) @RequestBody RegulationContent content) {
        regulationService.updateContent(content);
        return new SuccessVo();
    }

    @DeleteMapping(name = "根据ID删除法规正文", path = "/content/{id}")
    public BaseVo deleteRegulationContent(@PathVariable("id") Long id) {
        regulationService.deleteContent(id);
        return new SuccessVo();
    }

    @GetMapping(name = "预览法律法规", path = "/{id}/all")
    public BaseVo<List<RegulationPreviewVO>> getAllRegulationByLawId(@PathVariable("id") Long id) {
        return regulationService.findAll(id);
    }

    @PostMapping(name = "根据正文ID集合展示法规列表", path = "/content/level")
    public BaseVo showLawLevelByContentIds(@RequestBody RequestVO requestVO) {
        return new BaseVo(regulationService.getRegulationByContentIds(requestVO.getIds()));
    }
}
