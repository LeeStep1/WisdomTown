package com.bit.pb.dao;

import com.bit.ServicePbApplication;
import com.bit.module.pb.bean.PartyMemberApproval;
import com.bit.module.pb.dao.PartyMemberApprovalDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.List;

/**
 * Desc
 *
 * @author jianming.fan
 * @date 2018-12-19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(
        classes = ServicePbApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
public class PartyMemberApprovalTest {

    @Resource
    private PartyMemberApprovalDao partyMemberApprovalDao;

    @Test
    public void testFindAllPartyMemberApprovals() {
        //List<PartyMemberApproval> partyMemberApprovals = partyMemberApprovalDao.findAll("id");
        //Assert.notEmpty(partyMemberApprovals);
    }

}
