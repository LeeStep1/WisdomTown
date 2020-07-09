package com.bit.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bit.base.dto.UserInfo;
import com.bit.base.dto.VolunteerInfo;
import com.bit.base.exception.BusinessException;
import com.bit.base.service.BaseService;
import com.bit.base.vo.BaseVo;
import com.bit.common.consts.RedisKey;
import com.bit.core.utils.CacheUtil;
import com.bit.module.vol.bean.Dict;
import com.bit.module.vol.bean.Volunteer;
import com.bit.module.vol.feign.SysServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-27 14:42
 * 获得志愿者信息
 */
@Component
public class VolUtil extends BaseService{

    @Autowired
    private SysServiceFeign sysServiceFeign;
    @Autowired
    private CacheUtil cacheUtil;

    /**
     * 从token中获取志愿者信息
     * @return
     */
    public Volunteer getVolunteerInfo(){
        UserInfo userInfo = getCurrentUserInfo();
        if (userInfo==null){
            throw new BusinessException("请重新登陆");
        }
        Volunteer vi = new Volunteer();
        //从token中得到志愿者信息
        VolunteerInfo volunteerInfo = userInfo.getVolunteerInfo();
        if (volunteerInfo!=null){
            vi.setId(volunteerInfo.getVolunteerId());
            vi.setStationId(volunteerInfo.getStationId());
            vi.setRealName(userInfo.getRealName());
            vi.setCardId(userInfo.getIdcard());
            vi.setMobile(userInfo.getMobile());
        }


        return vi;
    }




    /**
     * 生成服务类型
     * @param s
     * @return
     */
    public String getServiceType(String s){

        StringBuilder serviceType = new StringBuilder();
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
                        serviceType.append(dict.getDictName());
                        serviceType.append(",");
                    }
                }
            }
        }
        String result = "";
        if (StringUtil.isNotEmpty(serviceType.toString())){
            result = serviceType.substring(0,serviceType.length()-1);
        }

        return result;
    }

    /**
     * 获得字典的中文值
     * @param moduleName
     * @param dictcode
     * @return
     */
    public String getDictName(String moduleName,Integer dictcode){
        Dict dict = new Dict();
        dict.setModule(moduleName);
        if (dictcode!=null){
            String code = dictcode.toString();
            if (StringUtil.isNotEmpty(code)){
                dict.setDictCode(code);
                BaseVo b1 = sysServiceFeign.findByModuleAndCode(dict);
                String bs1 = JSON.toJSONString(b1.getData());
                Dict obj = JSON.parseObject(bs1,Dict.class);
                return obj.getDictName();
            }
        }




        return "";
    }

    /**
     * 根据模块名称查询字典集合
     * @param moduleName
     * @return
     */
    public List<Dict> getDict(String moduleName){
        List<Dict> dictList = (List<Dict>)cacheUtil.get(RedisKeyUtil.getRedisKey(RedisKey.DICT_CACHE,moduleName));
        if (dictList==null || dictList.size()==0){
            BaseVo b1 = sysServiceFeign.findByModule(moduleName);
            String bs1 = JSON.toJSONString(b1.getData());
            dictList = JSON.parseArray(bs1,Dict.class);
        }

        return dictList;
    }

    /**
     * 生成服务类型
     * @param s
     * @return
     */
    public String getServiceTime(String s){
        StringBuilder serviceTime = new StringBuilder();
        List<Dict> dictList = (List<Dict>)cacheUtil.get(RedisKeyUtil.getRedisKey(RedisKey.DICT_CACHE,"week"));
        if (dictList==null || dictList.size()==0){
            BaseVo b1 = sysServiceFeign.findByModule("week");
            String s1 = JSON.toJSONString(b1.getData());
            dictList = JSONObject.parseArray(s1,Dict.class);
        }

        String[] ss = s.split(",");
        if (ss.length>0){
            for (String s2 : ss) {
                for (Dict dict : dictList) {
                    if (s2.equals(dict.getDictCode())){
                        serviceTime.append(dict.getDictName());
                        serviceTime.append(",");
                    }
                }
            }
        }
        String result = "";
        if (StringUtil.isNotEmpty(serviceTime.toString())){
            result = serviceTime.substring(0,serviceTime.length()-1);
        }
        return result;
    }

    /**
     * 获得站点类型
     * @param s
     * @return
     */
    public String getStationType(String s){
        String stationType = "";
        List<Dict> dictList = (List<Dict>)cacheUtil.get(RedisKeyUtil.getRedisKey(RedisKey.DICT_CACHE,"station_type"));
        if (dictList==null || dictList.size()==0){
            BaseVo b1 = sysServiceFeign.findByModule("station_type");
            String s1 = JSON.toJSONString(b1.getData());
            dictList = JSONObject.parseArray(s1,Dict.class);
        }

        for (Dict dict : dictList) {
            if (dict.getDictCode().equals(s)){
                stationType=dict.getDictName();
                break;
            }
        }
        return stationType;
    }
}
