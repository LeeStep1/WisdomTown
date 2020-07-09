package com.bit.ms.feign;

import com.bit.base.vo.BaseVo;
import com.bit.ms.bean.Message;
import com.bit.ms.bean.OrgAndName;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**
 * @author chenduo
 * @create 2019-01-22 8:28
 */

@Component
@Service("sysServiceHystrix")
public class SysServiceHystrix implements SysServiceFeign {


   /* @Override
    public BaseVo add(Message message) {
        System.out.println("hello world");
        return null;
    }*/

    @Override
    public BaseVo queryUserByAppIdOrgIdsName(OrgAndName orgAndName) {
        return null;
    }

    @Override
    public Boolean verifyToken(String token, Integer tid) {
        return null;
    }


}
