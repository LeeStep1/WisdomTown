package com.bit.officialdoc.controller;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.service.InboxService;
import com.bit.officialdoc.vo.DocQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * 待办、已办公文
 *
 * @author terry.jiang[taoj555@163.com] on 2019-01-16.
 */
@RestController
@RequestMapping(value = "/inbox")
public class InboxController {

    @Autowired
    private InboxService inboxService;

    /**
     * 列表分页（待办公文、已办公文）
     *
     * @param query
     * @return
     */
    @PostMapping("/query")
    public BaseVo query(@RequestBody DocQuery query) {
        return inboxService.query(query);
    }

    /**
     * 办理
     *
     * @param id
     * @return
     */
    @PutMapping("/dispose")
    public BaseVo handle(@NotBlank @RequestParam(value = "id") long id) {
        inboxService.dispose(id);
        return new BaseVo();
    }

}
