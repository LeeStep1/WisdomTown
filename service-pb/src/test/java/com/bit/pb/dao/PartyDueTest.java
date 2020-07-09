package com.bit.pb.dao;

import com.bit.ServicePbApplication;
import com.bit.module.pb.service.MonthlyPartyDueService;
import com.bit.module.pb.service.PartyDueService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

/**
 * @Description :
 * @Date ： 2018/12/26 10:29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = ServicePbApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class PartyDueTest {
    @Autowired
    private MonthlyPartyDueService monthlyPartyDueService;

    @Autowired
    private PartyDueService partyDueService;

    @Test
    public void testAddMonthlyPartyDue() {
        monthlyPartyDueService.addMonthlyPartyDueForAllOrganization(LocalDate.now());
    }

    @Test
    public void testPartyDue() {
        partyDueService.createPartyDueEveryMonth();
    }
}
