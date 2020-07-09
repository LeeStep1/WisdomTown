package com.bit.module.syslog.feign;

import com.bit.base.vo.BaseVo;
import com.bit.module.syslog.bean.MessageTemplate;
import com.bit.module.syslog.bean.MessageTemplateRelTid;
import com.bit.module.syslog.vo.MessageTemplateVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * sys微服务调用
 * @author liyang
 * @date 2019-04-04
 */
@FeignClient(value = "service-sys")
public interface SysServiceFeign {

    /**
     * 根据模板ID查询APPID
     * @author liyang
     * @date 2019-04-04
     * @param id : 模板ID
     */
   /* @RequestMapping(value = "/message/getMessageTempByMessageTempId/{id}", method = RequestMethod.GET)
    BaseVo getMessageTempByMessageTempId(@PathVariable(value = "id") Long id);*/

    /**
     * 批量查询党建组织下所有用户
     * @author liyang
     * @date 2019-04-04
     * @param targetIds : 组织ID集合
     */
    @RequestMapping(value = "/pbOrganization/getAllUserIdsByPbOrgIds/{targetIds}", method = RequestMethod.GET)
    BaseVo getAllUserIdsByPbOrgIds(@RequestParam(value = "targetIds") Long[] targetIds);

    /**
     * 批量查询指定党建组织指定方式注册用户
     * @author liyang
     * @date 2019-04-04
     * @param messageTemplate : 组织ID集合与人员类型
     */
    @RequestMapping(value = "/pbOrganization/getUserIdsByOrgIds", method = RequestMethod.POST)
    BaseVo getUserIdsByOrgIds(@RequestBody() MessageTemplate messageTemplate);

    /**
     * 批量查询政务组织下的用户
     * @author liyang
     * @date 2019-04-04
     * @param targetIds : 组织ID集合
     */
    @RequestMapping(value ="/oaDepartment/getAllUserIdsByOaOrgIds/{targetIds}", method = RequestMethod.GET)
    BaseVo getAllUserIdsByOaOrgIds(@RequestParam(value = "targetIds") Long[] targetIds);

    /**
     * 根据服务站ID查询服务站管理员
     * @author liyang
     * @date 2019-04-04
     * @param messageTemplate : 组织ID集合 和 人员类型
     */
    @RequestMapping(value ="/userRelVolStation/getAdminUserByStationOrgIds", method = RequestMethod.POST)
    BaseVo getAdminUserByStationOrgIds(@RequestBody MessageTemplate messageTemplate);

    /**
     * 获取消息标题
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String : 消息标题
     */
  /*  @RequestMapping(value ="/message/getMessageTitle", method = RequestMethod.POST)
    Map<String,String> getMessageTitle(@RequestBody MessageTemplate messageTemplate);*/

    /**
     * 根据模板ID获取相应的接入端ID
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String ：接入端ID
     */
  /*  @RequestMapping(value ="/message/getTidByMessageTemplate", method = RequestMethod.POST)
    List<String> getTidByMessageTemplate(@RequestBody MessageTemplate messageTemplate);*/

    /**
     * 获取党建组织下所有用户
     * @author liyang
     * @date 2019-04-09
     * @return : List<Long> ：用户ID集合
     */
    @RequestMapping(value ="/pbOrganization/getAllUserIdsForPbOrg", method = RequestMethod.GET)
    List<Long> getAllUserIdsForPbOrg();

    /**
     * 获取政务组织下所有用户
     * @author liyang
     * @date 2019-04-09
     * @return : List<Long> ：用户ID集合
     */
    @RequestMapping(value ="/oaDepartment/getAllUserIdsForOaOrg", method = RequestMethod.GET)
    List<Long> getAllUserIdsForOaOrg();

    /**
     * 获取所有志愿者管理员
     * @author liyang
     * @date 2019-04-10
     * @return : List<Long> : userIds
     */
    @RequestMapping(value ="/userRelVolStation/getAllUserIdForVolOrg", method = RequestMethod.GET)
    List<Long> getAllUserIdForVolOrg();


    /**
     * 根据模板ID获取相应的接入端ID
     * @author liyang
     * @date 2019-04-08
     * @param messageTemplate : 模板详情
     * @return : String ：接入端ID
     */
    @RequestMapping(value ="/message/getTemplateTidConfig", method = RequestMethod.POST)
    List<MessageTemplateRelTid> getTemplateTidConfig(@RequestBody MessageTemplate messageTemplate);


    @RequestMapping(value="/message/getTemplateCategory/{id}",method =RequestMethod.GET )
    public MessageTemplateVO getTemplateCategory(@RequestParam(value = "id")  Long id);
}
