package com.bit.module.sv.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Item;
import com.bit.module.sv.service.ItemService;
import com.bit.module.sv.service.RegulationService;
import com.bit.module.sv.vo.ItemVO;
import com.bit.module.sv.vo.RegulationPreviewVO;
import com.bit.module.sv.vo.RequestVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.bit.module.sv.utils.StringUtils.getSourceByHttpServletRequest;

/**
 * 排查项目 api
 * @author decai.liu
 * @create 2019-07-16
 */
@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @Autowired
    private RegulationService regulationService;

    @PostMapping(name = "新增排查项目", path = "/add")
    public BaseVo addItem(@RequestBody @Validated(ItemVO.AddItem.class) ItemVO itemVO, HttpServletRequest request){
        itemVO.setSource(getSourceByHttpServletRequest(request));
        itemService.addItem(itemVO);
        return new BaseVo();
    }

    @PostMapping(name = "补充排查项目清单", path = "/supplement")
    public BaseVo addItemList(@RequestBody @Validated(ItemVO.AddItemList.class) ItemVO itemVO){
        itemVO.setParentId(itemVO.getId());
        itemVO.setId(null);
        itemVO.setSource(null);
        itemService.addItem(itemVO);
        return new BaseVo();
    }

    @PostMapping(name = "排查项目分页查询")
    public BaseVo listItems(@RequestBody @Validated(ItemVO.PageSearch.class) ItemVO itemVO, HttpServletRequest request){
        itemVO.setSource(getSourceByHttpServletRequest(request));
        return itemService.listItems(itemVO);
    }

    @DeleteMapping(name = "删除排查项目", path = "/{id}")
    public BaseVo deleteItem(@PathVariable("id") Long id){
        return itemService.deleteById(id);
    }

    @PostMapping(name = "更新排查项目", path = "/modify")
    public BaseVo modifyItem(@RequestBody @Validated(ItemVO.Modify.class) ItemVO itemVO){
        itemVO.setSource(null);
        return itemService.modifyById(itemVO);
    }

    @PostMapping(name = "根据排查清单获取执法依据", path = "/regulations")
    public BaseVo listRegulationByItemList(@RequestBody RequestVO requestVO){
        List<Item> items = itemService.listItemsByIds(requestVO.getIds());
        Set<Long> contentIds = new HashSet<>();
        items.forEach(item -> {
            if (item.getRegulations() != null && item.getRegulations().size() > 0) {
                contentIds.addAll(item.getRegulations().stream().map(Integer::longValue).collect(Collectors.toSet()));
            }
        });
        if (CollectionUtils.isEmpty(contentIds)) {
            return new BaseVo();
        }
        List<RegulationPreviewVO> regulations = regulationService.getRegulationByContentIds(contentIds);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(regulations);
        return baseVo;
    }

    @GetMapping(name = "排查项目清单树形结构", path = "/tree")
    public BaseVo itemTree(HttpServletRequest request){
        return itemService.listItemTreeBySource(getSourceByHttpServletRequest(request));
    }
}
