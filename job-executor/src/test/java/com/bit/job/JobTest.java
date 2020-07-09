package com.bit.job;

import com.bit.job.feign.PartyDueFeign;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Description :
 * @Date ： 2019/1/1 16:39
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = JobExecutorApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class JobTest {
    @Autowired
    private PartyDueFeign partyDueFeign;

    @Test
    public void testOrg() {
        partyDueFeign.generateMonthlyPartyDueForSubordinates(2019, 1);
    }

    @Test
    public void testPersonal() {
        partyDueFeign.generatePartyDueMonthly();
    }
}
