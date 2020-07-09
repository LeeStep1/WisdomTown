package com.bit.module.system.controller;


import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.Notice;
import com.bit.module.system.bean.OrgAndName;
import com.bit.module.system.service.NoticeService;
import com.bit.module.system.vo.NoticeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author chenduo
 * @create 2019-02-14 10:10
 */
@RestController
@RequestMapping(value = "/notice")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    /**
     * 添加通知公告 或 直接发布
     * @param notice
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody Notice notice){
        return noticeService.add(notice);
    }

    /**
     * 修改通知公告 或 发布
     * @param notice
     * @return
     */
    @PostMapping("/update")
    public BaseVo update(@RequestBody Notice notice){
        return noticeService.update(notice);
    }

    /**
     * 删除通知公告
     * @param id
     * @return
     */
    @GetMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id){
        return noticeService.delete(id);
    }

    /**
     * 反显通知公告 不做状态改变
     * @param id
     * @return
     */
//    @GetMapping("/query/{id}")
//    public BaseVo queryById(@PathVariable(value = "id") Long id){
//        return noticeService.queryById(id);
//    }

    /**
     * 通知管理 公告管理 的 反显通知公告
     * @param id
     * @return
     */
    @GetMapping("/reflect/{id}")
    public BaseVo reflectById(@PathVariable(value = "id") Long id){
        return noticeService.reflectById(id);
    }


    /**
     * 分页查询通知公告 这个用没用
     * @param noticeVO
     * @return
     */
//    @PostMapping("/anlistPage")
//    public BaseVo anlistPage(@RequestBody NoticeVO noticeVO){
//        return noticeService.anlistPage(noticeVO);
//    }

    /**
     * 分页查询通知 公告 待办 已办 消息
     * @param noticeVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody NoticeVO noticeVO){
        return noticeService.listPage(noticeVO);
    }

    /**
     * 分页查询用户发送的通知公告
     * @param noticeVO
     * @return
     */
    @PostMapping("/userlistPage")
    public BaseVo userlistPage(@RequestBody NoticeVO noticeVO){
        return noticeService.userlistPage(noticeVO);
    }

    /**
     * 查询当前用户下的应用
     * @return
     */
    @PostMapping("/getApp")
    public BaseVo getApp(){
        return noticeService.getApp();
    }

    /**
     * 根据应用查询组织结构
     * @param appId
     * @return
     */
    @GetMapping("/queryOrgByAppId/{appId}")
    public BaseVo queryOrgByAppId(@PathVariable(value = "appId")Long appId){
        return noticeService.queryOrgByAppId(appId);
    }

    /**
     * 根据应用和用户查询组织结构
     * @param appId
     * @return
     */
    @GetMapping("/queryOrgByAppIdUser/{appId}")
    public BaseVo queryOrgByAppIdUser(@PathVariable(value = "appId")Long appId){
        return noticeService.queryOrgByAppIdUser(appId);
    }

    /**
     * 根据应用 组织id 查询人员
     * @param appId
     * @param orgId
     * @return
     */
    @GetMapping("/queryUserByAppId/{appId}/{orgId}")
    public BaseVo queryUserByAppId(@PathVariable(value = "appId")Long appId,@PathVariable(value = "orgId")Long orgId){
        return noticeService.queryUserByAppId(appId, orgId);
    }

    /**
     * 根据通知公告类型更改已读状态
     * @param noticeVO
     * @return
     */
    @PostMapping("/readAll")
    public BaseVo readAll(@RequestBody NoticeVO noticeVO){
        return noticeService.readAll(noticeVO);
    }
    /**
     * 根据用户id 查询最大的公告id
     * @param userId
     * @return
     */
//    @GetMapping("/getMaxId/{userId}")
//    public BaseVo getMaxId(@PathVariable(value = "userId") Long userId){
//        return noticeService.getMaxId(userId);
//    }

    /**
     * 更新mongo的msgtype
     * @param mongoId
     * @return
     */
    @GetMapping("/updateMsgType/{mongoId}")
    public BaseVo updateMongoHandled(@PathVariable(value = "mongoId")String mongoId){
        return noticeService.updateMsgType(mongoId);
    }


    /**
     * 根据应用 组织id 姓名 查询人员
     * @param orgAndName
     * @return
     */
    @PostMapping("/queryUserByAppIdOrgIdsName")
    public BaseVo queryUserByAppIdOrgIdsName(@RequestBody OrgAndName orgAndName){
        return noticeService.queryUserByAppIdOrgIdsName(orgAndName);
    }

    /**
     * 铃铛的查询
     * @return
     */
    @PostMapping("/listPageForBell")
    public BaseVo listPageForBell(){
        return noticeService.listPageForBell();
    }
    /**
     * 更新mongo的status
     * @param mongoId
     * @return
     */
    @GetMapping("/updateMongoMsgStatus/{mongoId}")
    public BaseVo updateMongoMsgStatus(@PathVariable(value = "mongoId")String mongoId){
        return noticeService.updateMongoMsgStatus(mongoId);
    }
    /**
     * 删除mongo的消息
     * @param
     * @return
     */
    @GetMapping("/deleteMongoMessage/{mongoId}")
    public BaseVo deleteMongoMessage(@PathVariable(value = "mongoId")String mongoId){
        return noticeService.deleteMongoMessage(mongoId);
    }
}
