package com.bit.module.oa.utils;

import org.springframework.util.StringUtils;

import java.time.LocalDate;

/**
 * @Description :
 * @Date ： 2019/2/14 16:50
 */
public class ApplyNoGenerator {

    /**
     * 生成单号
     * 规则 : 拼音首字母-年月日-当日派单号
     * 如公务用车 2018年12月1日第二张派车单 : GW-20181201-0002
     * @param lastApplyNo 对应的最后一张单号
     * @param prefix 单号前缀
     * @return
     */
    public static String generateApplyNo(String lastApplyNo, String prefix) {
        LocalDate now = LocalDate.now();
        String month = now.getMonthValue() < 10 ? "0" + now.getMonthValue() : String.valueOf(now.getMonthValue());
        String day = now.getDayOfMonth() < 10 ? "0" + now.getDayOfMonth() : String.valueOf(now.getDayOfMonth());
        String applyNo = prefix + "-" + now.getYear() + month + day + "-0001";
        if (!StringUtils.isEmpty(lastApplyNo)) {
            String serialNumber = String.valueOf(Integer.valueOf(lastApplyNo.substring(12)) + 1);
            applyNo = applyNo.substring(0, applyNo.length() - serialNumber.length()) + serialNumber;
        }
        return applyNo;
    }

    public static String generateChildApplyNo(String applyNo, String lastChildApplyNo) {
        if (StringUtils.isEmpty(lastChildApplyNo)) {
            return applyNo + "-1";
        }
        String index = lastChildApplyNo.substring(lastChildApplyNo.lastIndexOf("-") + 1);
        int newIndex = Integer.valueOf(index) + 1;
        return applyNo + "-" + newIndex;
    }
}