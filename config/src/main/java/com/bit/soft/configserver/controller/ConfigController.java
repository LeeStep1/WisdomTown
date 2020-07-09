package com.bit.soft.configserver.controller;


import com.bit.base.vo.BaseVo;
import com.bit.soft.configserver.bean.ConfigProperties;
import com.bit.soft.configserver.service.ConfigPropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

@RestController
@RefreshScope
@RequestMapping(value = "/config")
public class ConfigController {

    @Autowired
    private ConfigPropertiesService configPropertiesService;

    /**
     * 增加设置
     * @param configProperties
     * @return
     */
    @PostMapping("/insertConfigProperties")
    public BaseVo insertConfigProperties(@RequestBody ConfigProperties configProperties){

        return configPropertiesService.insertProperties(configProperties);
    }

    /**
     * 修改设置
     * @param configProperties
     * @return
     */
    @PostMapping("/modifyConfigProperties")
    public BaseVo modifyConfigProperties(@RequestBody ConfigProperties configProperties){

        return configPropertiesService.updateProperties(configProperties);
    }

    /**
     * 删除设置
     * @param id
     * @return
     */
    @PostMapping("/delConfigProperties/{id}")
    public BaseVo delConfigProperties(@PathVariable int id){

        return configPropertiesService.delProperties(id);
    }

    /**
     * 查看设置
     * @return
     */
    @PostMapping("/configProperties")
    public BaseVo configProperties(){

        return configPropertiesService.selectProperties();
    }
}
