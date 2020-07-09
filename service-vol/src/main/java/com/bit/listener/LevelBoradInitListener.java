package com.bit.listener;

import com.bit.module.vol.service.VolunteerService;
import com.bit.module.vol.vo.VolunteerVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Description:  监听缓存
 * @Author: liyujun
 * @Date: 2019-03-27
 **/
@Component

public class LevelBoradInitListener  {

    private Logger logger = LoggerFactory.getLogger(LevelBoradInitListener.class);

    @Autowired
    private VolunteerService volunteerService;

    @EventListener
    @Async
    public void onApplicationEvent(ApplicationReadyEvent event) {

        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        if(event!=null){

            logger.info("-------------服务启动成功执行此方法-----------------");
            if(volunteerService==null){
                 volunteerService=applicationContext.getBean(VolunteerService.class);
            }
            VolunteerVO volunteerVO=new VolunteerVO();
            volunteerVO.setPageNum(1);
            volunteerVO.setPageSize(10);
            volunteerService.board(volunteerVO);

        }

        //logger.error("-------------服务监听执行失败-----------------");
    }
}
