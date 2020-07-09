package com.bit.vol;

import com.bit.ServiceVolApplication;
import com.bit.module.vol.bean.PartnerOrg;
import com.bit.module.vol.service.PartnerOrgService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: liyujun
 * @Date: 2020-06-12
 **/
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = ServiceVolApplication.class) // 指定我们SpringBoot工程的Application启动类,1.5.4摒弃了SpringApplicationConfiguration注解
public class SpringTest {


    @Autowired
    private PartnerOrgService  partnerOrgService;

    @Test
    public void TestTraction(){
        PartnerOrg partnerOrg=new PartnerOrg();
        partnerOrg.setId(59L);
        partnerOrg.setPartnerOrgName("个大");
        partnerOrg.setPartnerOrgAddress("GE");
        partnerOrg.setChargeMan("个");
        partnerOrg.setChargeManMobile("13011399819");
        partnerOrg.setStationLeader("cd");
        partnerOrg.setStationLeaderMobile("13011399819");
        partnerOrg.setViceStationLeader("cd");
        partnerOrg.setViceStationLeaderMobile("13011399819");
        partnerOrg.setPartnerOrgNumber(1);
        partnerOrg.setAuditState(2);
        partnerOrg.setRejectReason("ssdsd");

       /* ;
        {"id":76,"partnerOrgName":"个大","partnerOrgAddress":"个","chargeMan":"个","chargeManMobile":"13011399819","stationLeader":"cd","stationLeaderMobile":"13011399819","viceStationLeader":"cd","viceStationLeaderMobile":"13011399819","partnerOrgIntroduction":null,"partnerOrgNumber":1,"auditState":2,"createTime":1591865665000,"updateTime":1591924284000,"updateUserId":229,"version":4,"rejectReason":"ssdsd","upid":100}*/
        partnerOrgService.update(partnerOrg,null);
    }
}
