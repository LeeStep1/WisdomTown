package com.bit.utils;

import com.bit.common.enumerate.StationTypeEnum;
import com.bit.module.vol.bean.Station;
import com.bit.module.vol.dao.StationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author chenduo
 * @create 2019-03-13 17:11
 */
@Component
public class CodeUtil {
    @Autowired
    private StationDao stationDao;

    public static String getCode(Station station, Integer num){
        String s = station.getStationCode();
        return s+"-"+String.format("%04d",num);
    }

    public static String getStationCode(String type,Integer num){
        return type+String.format("%03d",num);
    }

    /**
     * 生成站点code
     * @param station
     * @return
     */
    public String genStationCode(Station station){
        String type = "";
        //镇团委
        if (station.getStationType().equals(StationTypeEnum.STATION_TYPE_ZHENTUANWEI.getCode())){
            type = StationTypeEnum.STATION_TYPE_ZHENTUANWEI.getInfo();
        }
        //村服务站
        if (station.getStationType().equals(StationTypeEnum.STATION_TYPE_CUN.getCode())){
            type = StationTypeEnum.STATION_TYPE_CUN.getInfo();
        }
        //社区服务站
        if (station.getStationType().equals(StationTypeEnum.STATION_TYPE_SHEQU.getCode())){
            type = StationTypeEnum.STATION_TYPE_SHEQU.getInfo();
        }
        //企业服务站
        if (station.getStationType().equals(StationTypeEnum.STATION_TYPE_QIYE.getCode())){
            type = StationTypeEnum.STATION_TYPE_QIYE.getInfo();
        }
        //共建单位服务站
        if (station.getStationType().equals(StationTypeEnum.STATION_TYPE_GONGJIAN.getCode())){
            type = StationTypeEnum.STATION_TYPE_GONGJIAN.getInfo();
        }
        List<String> stationCodeByLetter = stationDao.findStationCodeByLetter(type);
        List<Integer> codeInts = new ArrayList<>();
        //转换stationcode成数字
        for (String code : stationCodeByLetter) {
            String cc = code.substring(1,code.length());
            Integer tt = Integer.valueOf(cc);
            codeInts.add(tt);
        }
        Integer id = 0;
        if (codeInts.size() > 0){
            id = Collections.max(codeInts);
            id = id + 1;
        }

        //生产stationcode
        String stationCode = CodeUtil.getStationCode(type, id);
        return stationCode;
    }
}
