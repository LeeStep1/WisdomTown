package com.bit.module.sv.service.impl;

import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.module.sv.bean.Item;
import com.bit.module.sv.dao.ItemDao;
import com.bit.module.sv.service.ItemService;
import com.bit.module.sv.utils.DateUtils;
import com.bit.module.sv.vo.ElementVO;
import com.bit.module.sv.vo.ItemVO;
import com.bit.utils.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("itemService")
@Transactional
public class ItemServiceImpl extends BaseService implements ItemService {

    private static final Logger logger = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Autowired
    private ItemDao itemDao;

    @Override
    public void addItem(ItemVO itemVO) {
        Item toAdd = new Item();
        BeanUtils.copyProperties(itemVO, toAdd);
        toAdd.setCreateAt(DateUtils.getCurrentDate());
        toAdd.setUpdateAt(toAdd.getCreateAt());
        itemDao.insert(toAdd);
    }

    @Override
    public BaseVo listItems(ItemVO itemVO) {
        PageHelper.startPage(itemVO.getPageNum(), itemVO.getPageSize());
        List<Item> itemList = itemDao.findBySourceAndName(itemVO.getSource(), itemVO.getName());
        PageInfo<Item> pageInfo = new PageInfo<>(itemList);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(pageInfo);
        return baseVo;
    }

    @Override
    public BaseVo deleteById(Long id) {
        itemDao.deleteById(id);
        return successVo();
    }

    @Override
    public BaseVo modifyById(ItemVO itemVO) {
        Item toUpdate = new Item();
        BeanUtils.copyProperties(itemVO, toUpdate);
        toUpdate.setUpdateAt(DateUtils.getCurrentDate());
        itemDao.updateByIdSelective(toUpdate);
        return successVo();
    }

    public BaseVo listItemTreeBySource(Integer appId) {
        List<Item> itemList = itemDao.findAllBySource(appId);
        BaseVo baseVo = new BaseVo();
        baseVo.setData(buildItemTree(itemList));
        return baseVo;
    }

    @Override
    public List<Item> listItemsByIds(List<Long> itemIds) {
        return itemDao.findByIdIn(itemIds);
    }

    /**
     * 组装排查项目树形结构
     * @param items
     * @return
     */
    private List<ElementVO> buildItemTree(List<Item> items) {
        LinkedList<Item> queue = new LinkedList<>(items);
        Map<Long, ElementVO> elementMap = new HashMap<>(queue.size());
        List<ElementVO> elementVOS = new ArrayList<>();
        Map<Long, List<Item>> parentChildren = new HashMap<>();

        Item element;
        while ((element = queue.poll()) != null) {
            ElementVO parent = null;
            if (element.getParentId() != null && (parent = elementMap.get((element.getParentId()))) == null) {
                parentChildren.computeIfAbsent(element.getParentId(), k -> new LinkedList<>()).add(element);
                continue;
            }

            ElementVO elementVO = new ElementVO();
            elementVO.setId(element.getId());
            elementVO.setParentId(element.getParentId());
            elementVO.setName(StringUtil.isEmpty(element.getName()) ? element.getContent() : element.getName());
            elementMap.put(element.getId(), elementVO);

            if (parent == null) {
                elementVO.setLevel(1);
                elementVOS.add(elementVO);
            } else {
                if (parent.getChildren() == null) {
                    parent.setChildren(new ArrayList<>());
                }
                elementVO.setLevel(parent.getLevel());
                parent.getChildren().add(elementVO);
            }

            parentChildren.computeIfPresent(elementVO.getId(), (k, v) -> {
                v.forEach(queue::push);
                return null;
            });
        }
        return elementVOS;
    }
}
