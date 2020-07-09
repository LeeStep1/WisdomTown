package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.UserRelVolStation;
import com.bit.module.system.service.UserRelVolStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-22 14:41
 */
@RestController
@RequestMapping("/userRelVolStation")
public class UserRelVolStationController {

    @Autowired
    private UserRelVolStationService userRelVolStationService;

    /**
     * 新增用户与志愿者站点关系
     * @param userRelVolStation
     * @return
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody UserRelVolStation userRelVolStation){
        return userRelVolStationService.add(userRelVolStation);
    }
    /**
     * 根据用户id查询记录
     * @param userId
     * @return
     */
    @GetMapping("/queryByUserId/{userId}")
    public BaseVo queryByUserId(@PathVariable(value = "userId")Long userId){
        return userRelVolStationService.queryByUserId(userId);
    }

    /**
     * 根据服务站ID查询服务站管理员
     * @author liyang
     * @date 2019-04-04
     * @param messageTemplate : 组织ID集合 和 人员类型
     */
    @PostMapping("/getAdminUserByStationOrgIds")
    public BaseVo getAdminUserByStationOrgIds(@RequestBody MessageTemplate messageTemplate){
        return userRelVolStationService.getAdminUserByStationOrgIds(messageTemplate);
    }

    /**
     * 获取所有志愿者管理员
     * @author liyang
     * @date 2019-04-10
     * @return : List<Long> : userIds
     */
    @GetMapping("/getAllUserIdForVolOrg")
    public List<Long> getAllUserIdForVolOrg(){
        return userRelVolStationService.getAllUserIdForVolOrg();
    }
    /**
     * 根据服务站id查询所有人
     * @param stationId
     * @return
     */
    @GetMapping("/queryAllUserBystationId/{stationId}")
    public List<Long> queryAllUserBystationId(@PathVariable(value = "stationId")Long stationId){
        return userRelVolStationService.queryAllUserBystationId(stationId);
    }
    /**
     * 根据主键删除记录
     * @param id
     * @return
     */
    @GetMapping("/delRelation/{id}")
    public BaseVo delRelation(@PathVariable(value = "id")Long id){
        return userRelVolStationService.delRelation(id);
    }

}
