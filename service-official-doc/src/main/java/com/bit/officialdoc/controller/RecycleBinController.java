package com.bit.officialdoc.controller;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.service.RecycleBinService;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.SimpleDoc;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Arrays;

/**
 * 回收站
 *
 * @autor xiaoyu.fang
 * @date 2019/1/18 14:05
 */
@RestController
@RequestMapping(value = "/recycleBin")
public class RecycleBinController {

    @Autowired
    private RecycleBinService recycleBinService;

    /**
     * 列表分页（待发公文、已发公文）
     *
     * @param query
     * @return
     */
    @PostMapping("/query")
    public BaseVo<PageInfo<SimpleDoc>> query(@RequestBody DocQuery query) {
        return recycleBinService.query(query);
    }

    /**
     * 恢复删除
     *
     * @param ids
     * @return
     */
    @PutMapping("/restore")
    public BaseVo restore(@NotBlank @RequestParam("ids") String ids) {
        long[] idsArray = Arrays.stream(StringUtils.split(ids, ',')).mapToLong(Long::valueOf).toArray();
        recycleBinService.restore(idsArray);
        return new BaseVo();
    }

}
