package com.bit.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bit.base.vo.BaseVo;
import com.bit.common.consts.RedisKey;
import com.bit.core.utils.CacheUtil;
import com.bit.module.vol.bean.Dict;
import com.bit.module.vol.feign.SysServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-27 15:09
 * 处理活动类型
 */
@Component
public class TypeUtil {

    @Autowired
    private SysServiceFeign sysServiceFeign;
    @Autowired
    private CacheUtil cacheUtil;

    public List<String> getType(String s){
        List<String> list = new ArrayList<>();
        if (StringUtil.isEmpty(s)){
            return list;
        }
        List<Dict> dictList = (List<Dict>)cacheUtil.get(RedisKeyUtil.getRedisKey(RedisKey.DICT_CACHE,"service_type"));
        if (dictList==null || dictList.size()==0){
            BaseVo b1 = sysServiceFeign.findByModule("service_type");
            String s1 = JSON.toJSONString(b1.getData());
            dictList = JSONObject.parseArray(s1,Dict.class);
        }

        String[] ss = s.split(",");
        if (ss.length>0){
            for (String s2 : ss) {
                for (Dict dict : dictList) {
                    if (s2.equals(dict.getDictCode())){
                        list.add(dict.getDictName());
                    }
                }
            }
        }
        return list;
    }


}
