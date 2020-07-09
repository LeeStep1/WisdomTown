package com.bit.utils;

import com.bit.common.enumerate.CampaignStatusEnum;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: chenduo
 * @create: 2019-04-03 14:25
 */
@Component
public class CampaignUtil {


    /**
     * 根据时间判断活动状态
     * @param startday
     * @param starttime
     * @param endday
     * @param endtime
     * @return
     */
    public Integer setCampaignStatus(Integer startday,String starttime,Integer endday,String endtime){

        Date start = DateUtil.getStartOrCancelDate(String.valueOf(startday),starttime);
        Date end = DateUtil.getStartOrCancelDate(String.valueOf(endday),endtime);
        Date current = new Date();
        Integer result = 0;
        if (current.before(start)){
            result = CampaignStatusEnum.CAMPAIGN_STATUS_NOT_START.getCode();
        }
        if (current.after(end)){
            result = CampaignStatusEnum.CAMPAIGN_STATUS_ENDED.getCode();
        }
        if (current.after(start) && current.before(end)){
            result = CampaignStatusEnum.CAMPAIGN_STATUS_RUNNING.getCode();
        }
        return result;
    }

    /**
     * 根据时间判断活动状态
     * @param beginDate
     * @param finishDate
     * @return
     */
    public Integer setCampaignStatusWithDate(Date beginDate,Date finishDate){


        Date current = new Date();
        Integer result = 0;
        if (current.before(beginDate)){
            result = CampaignStatusEnum.CAMPAIGN_STATUS_NOT_START.getCode();
        }
        if (current.after(finishDate)){
            result = CampaignStatusEnum.CAMPAIGN_STATUS_ENDED.getCode();
        }
        if (current.after(beginDate) && current.before(finishDate)){
            result = CampaignStatusEnum.CAMPAIGN_STATUS_RUNNING.getCode();
        }
        return result;
    }

    /**
     * 转换integer类型的日期 成 Date类型
     * @param date
     * @return
     */
    public Date convertIntegerDateToBeginDate(Integer date) {
        Date result = null;
        try {
            if (date!=null){
                String year = date.toString().substring(0,4);
                String month = date.toString().substring(4,6);
                String day  = date.toString().substring(6,date.toString().length());
                String temp = year + "-"+month+"-"+day+" 00:00:00";
                result = DateUtil.string2Date(temp,DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 转换integer类型的日期 成 Date类型
     * @param date
     * @return
     */
    public Date convertIntegerDateToEndDate(Integer date) {
        Date result = null;
        try {
            if (date!=null){
                String year = date.toString().substring(0,4);
                String month = date.toString().substring(4,6);
                String day  = date.toString().substring(6,date.toString().length());
                String temp = year + "-"+month+"-"+day+" 23:59:59";
                result = DateUtil.string2Date(temp,DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String,Object> getStartOrEndTime(Date beginDate,Date finishDate){
        String begin = DateUtil.date2String(beginDate,DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
        String finish = DateUtil.date2String(finishDate,DateUtil.DatePattern.YYYYMMDDHHmmss.getValue());
        begin = begin.replaceAll("-","").replaceAll(" ","");
        finish = finish.replaceAll("-","").replaceAll(" ","");
        Map<String,Object> result = new HashMap<>();
        result.put("startDate",Integer.valueOf(begin.substring(0,8)));
        result.put("endDate",Integer.valueOf(finish.substring(0,8)));
        result.put("startTime",begin.substring(8,13));
        result.put("endTime",finish.substring(8,13));
        return result;
    }
}
