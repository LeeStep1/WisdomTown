package com.bit.officialdoc.controller;

import com.bit.base.vo.BaseVo;
import com.bit.officialdoc.entity.CustomFolder;
import com.bit.officialdoc.service.ArchiveService;
import com.bit.officialdoc.vo.DocQuery;
import com.bit.officialdoc.vo.FolderQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 个人归档
 *
 * @autor xiaoyu.fang
 * @date 2019/1/18 17:32
 */
@RestController
@RequestMapping(value = "/archive")
public class ArchiveController {

    @Autowired
    private ArchiveService archiveService;

    /**
     * 文件夹分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryFolder")
    public BaseVo queryFolder(@RequestBody FolderQuery query) {
        return archiveService.queryFolder(query);
    }

    /**
     * 获取个人文件夹列表
     *
     * @return
     */
    @GetMapping("/getFolders")
    public BaseVo getFolders() {
        List<CustomFolder> customFolders = archiveService.getFolders();
        BaseVo baseVo = new BaseVo();
        baseVo.setData(customFolders);
        return baseVo;
    }

    /**
     * 获取个人文件夹和存档数量
     *
     * @return
     */
    @PostMapping("/getFoldersAndNum")
    public BaseVo findCustorAndNumById(@RequestBody FolderQuery query) {
        return archiveService.findCustorAndNumById(query);
    }

    /**
     * 获取各个箱的数量
     *
     * @return
     */
    @GetMapping("/getDocsCount")
    public BaseVo findCountByOwnerId() {
        int[] nums = archiveService.findCountByOwnerId();
        Map map = new HashMap();
        if (nums.length == 5) {
            map.put("待发公文", nums[0]);
            map.put("已发公文", nums[1]);
            map.put("待办公文", nums[2]);
            map.put("已办公文", nums[3]);
            map.put("已删公文", nums[4]);
        }
        BaseVo baseVo = new BaseVo();
        baseVo.setData(map);
        return baseVo;
    }

    /**
     * 新增个人文件夹
     *
     * @param customFolder
     * @return
     */
    @PostMapping("/createFolder")
    public BaseVo createFolder(@RequestBody CustomFolder customFolder) {
        archiveService.createFolder(customFolder);
        return new BaseVo();
    }

    /**
     * 更新名称
     *
     * @param id
     * @param name
     * @return
     */
    @PutMapping("/modifyFolder")
    public BaseVo modifyFolder(@NotNull @RequestParam(value = "id") long id, @NotBlank @RequestParam(value = "name") String name) {
        archiveService.modifyFolder(id, name);
        return new BaseVo();
    }

    /**
     * 清空文件夹
     *
     * @param id
     * @return
     */
    @PutMapping("/cleanFolder")
    public BaseVo cleanFolder(@NotNull @RequestParam(value = "id") long id) {
        archiveService.cleanFolder(id);
        return new BaseVo();
    }

    // ================ 个人存档 =============== //


    /**
     * 存档分页
     *
     * @param query
     * @return
     */
    @PostMapping("/queryArchive")
    public BaseVo queryArchive(@RequestBody DocQuery query) {
        return archiveService.queryArchive(query);
    }

}
