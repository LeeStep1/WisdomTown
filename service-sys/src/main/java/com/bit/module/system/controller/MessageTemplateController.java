package com.bit.module.system.controller;

import com.bit.base.vo.BaseVo;
import com.bit.module.system.bean.MessageTemplate;
import com.bit.module.system.bean.MessageTemplateRelTid;
import com.bit.module.system.service.MessageTemplateService;
import com.bit.module.system.vo.MessageTemplatePageVO;
import com.bit.module.system.vo.MessageTemplateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-09 9:52
 */
@RestController
@RequestMapping(value = "/messagetemplate")
public class MessageTemplateController {
    @Autowired
    private MessageTemplateService messageTemplateService;

    /**
     * 查询消息模板
     * @param messageTemplate
     * @return
     */
    @PostMapping("/queryByMessage")
    public BaseVo queryByMessage(@RequestBody MessageTemplate messageTemplate){
        return messageTemplateService.queryByMessage(messageTemplate);
    }


    /**
     * 新增数据
     * @param messageTemplateVO
     * @author chenduo
     * @since ${date}
     */
    @PostMapping("/add")
    public BaseVo add(@RequestBody MessageTemplateVO messageTemplateVO){
        return messageTemplateService.add(messageTemplateVO);
    }

    /**
     * 配置模板 add
     * @param messageTemplateRelTids
     * @return
     */
    @PostMapping("/settingAdd")
    public BaseVo settingAdd(@RequestBody List<MessageTemplateRelTid> messageTemplateRelTids){
        return messageTemplateService.settingAdd(messageTemplateRelTids);
    }

	/**
	 * 配置模板 update
	 * @param messageTemplateRelTids
	 * @return
	 */
	@PostMapping("/settingUpdate")
	public BaseVo settingUpdate(@RequestBody List<MessageTemplateRelTid> messageTemplateRelTids){
		return messageTemplateService.settingUpdate(messageTemplateRelTids);
	}

	/**
	 * 配置模板 返显
	 * @param templateId
	 * @return
	 */
	@GetMapping("/settingReflect/{templateId}")
	public BaseVo settingReflect(@PathVariable(value = "templateId")Long templateId){
		return messageTemplateService.settingReflect(templateId);
	}


    /**
     * 编辑数据
     * @param messageTemplateVO
     * @author chenduo
     * @since ${date}
     */
    @PutMapping("/update")
    public BaseVo update(@RequestBody MessageTemplateVO messageTemplateVO){
        return messageTemplateService.update(messageTemplateVO);
    }




    /**
     * 删除数据
     * @param id
     * @author chenduo
     * @since ${date}
     */
    @DeleteMapping("/delete/{id}")
    public BaseVo delete(@PathVariable(value = "id") Long id){
        return messageTemplateService.delete(id);
    }


    /**
     * 多参数查询数据
     * @param messageTemplate
     * @author chenduo
     * @since ${date}
     * @return List<MessageTemplate>
     */
    @PostMapping("/findByParam")
    public BaseVo findByParam(@RequestBody MessageTemplate messageTemplate){
        return messageTemplateService.findByParam(messageTemplate);
    }

    /**
     * 单查数据
     * @param id
     * @author chenduo
     * @since ${date}
     * @return ${entity}
     */
    @GetMapping("/reflectById/{id}")
    public BaseVo reflectById(@PathVariable(value = "id") Long id){
        return messageTemplateService.reflectById(id);
    }

    /**
     * 分页查询
     * @param messageTemplatePageVO
     * @return
     */
    @PostMapping("/listPage")
    public BaseVo listPage(@RequestBody MessageTemplatePageVO messageTemplatePageVO){
        return messageTemplateService.listPage(messageTemplatePageVO);
    }

	/**
	 * 校验模板与接入端和应用的关系
	 * @return
	 */
	@PostMapping("/distinctRelParams")
    public BaseVo distinctRelParams(@RequestBody MessageTemplateRelTid messageTemplateRelTid){
    	return messageTemplateService.distinctRelParams(messageTemplateRelTid);
	}

}
