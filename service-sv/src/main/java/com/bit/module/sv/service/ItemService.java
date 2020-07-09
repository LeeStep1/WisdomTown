package com.bit.module.sv.service;

import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Item;
import com.bit.module.sv.vo.ItemVO;

import java.util.List;

public interface ItemService {

    void addItem(ItemVO itemVO);

    BaseVo listItems(ItemVO itemVO);

    BaseVo deleteById(Long id);

    BaseVo modifyById(ItemVO itemVO);

    BaseVo listItemTreeBySource(Integer appId);

    List<Item> listItemsByIds(List<Long> itemIds);
}
