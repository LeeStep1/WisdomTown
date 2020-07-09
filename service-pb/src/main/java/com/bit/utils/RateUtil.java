package com.bit.utils;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

/**
 * @author chenduo
 * @create 2019-04-01 13:10
 */
@Component
public class RateUtil {

    //计算出勤率
    public Integer signRate(Integer count, Integer all){

        BigDecimal b1 = new BigDecimal(all);
        BigDecimal b2 = new BigDecimal(count);

        if (b1.intValue()==0){
            return 0;
        }
        BigDecimal b3 = b2.divide(b1,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        int rate = b3.intValue();
        return rate;
    }

    //计算出勤率
    public Integer uploadRate(Integer count, Integer all){

        BigDecimal b1 = new BigDecimal(all);
        BigDecimal b2 = new BigDecimal(count);

        if (b1.intValue()==0){
            return 0;
        }
        BigDecimal b3 = b2.divide(b1,2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
        int rate = b3.intValue();
        return rate;
    }
}
