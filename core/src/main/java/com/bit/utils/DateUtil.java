package com.bit.utils;

import org.apache.commons.lang3.time.DateUtils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 统一日期处理类,
 * 不要直接在代码中引入其它第三方日期处理类,
 * 统一使用这里的
 */
public class DateUtil {
    public enum DatePattern {

        YYYYMMDDHHmmss("yyyy-MM-dd HH:mm:ss"),
        YYYYMMDD("yyyy-MM-dd"),
        YYYYMMDDHHmmss1("yyyy/MM/dd HH:mm:ss"),
        YYYYMMDD1("yyyyMMdd"),
        YYYY("yyyy"),
        YYYYMMDDHHmmss2("yyyyMMddHHmmss");


        private String value;

        DatePattern(String value) {
            this.value = value;
        }

        public String getValue() {

            return this.value;
        }
    }



    private static final int MORNING_WORK = 0;

    private static final int NIGHT_WORK = 1;

    /**
     * 返回当前时间字符串
     *
     * @return
     */
    public static String getCurrentTimeString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DatePattern.YYYYMMDDHHmmss.getValue());
        return dateFormat.format(new Date());
    }

    /**
     * 根据年月日构造date
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getTime(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    /**
     * 根据年、月、日、小时构造date
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static Date getTime(int year, int month, int day, int hour) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, hour, 0);
        return calendar.getTime();
    }



    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取当前毫秒时间
     *
     * @return
     */
    public static long getCurrentDateMillis() {
        return System.currentTimeMillis();
    }



    /**
     * 字符串转时间
     *
     * @param date    需要转换的时间
     * @param pattern 格式
     * @return
     * @throws ParseException
     */
    public static Date string2Date(String date, String pattern) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.parse(date);
    }


    /**
     * 日期转化为字符串
     *
     * @param date    需要转化的日期
     * @param pattern 转化格式
     * @return
     */
    public static String date2String(Date date, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }

    /**
     * 获得当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前日
     *
     * @return
     */
    public static int getCurrentDayOfMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前小时数，以24小时制计算
     *
     * @return
     */
    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获得当前分钟数
     *
     * @return
     */
    public static int getCurrentMinute() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取当前秒
     *
     * @return
     */
    public static int getCurrentSecond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取当前毫秒
     *
     * @return
     */
    public static int getCurrentMilliSeccond() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MILLISECOND);
    }

    /**
     * 对时间做加减法
     * 例如求
     * 2016/10/12日的5天后的时间
     * 2016/10/12 22：00 前三小时的时间
     *
     * @param date          需要计算的基础时间
     * @param amount        需要加或者减去的时间数量（负值为减）
     * @param calendarField 时间单位，支持毫秒、分钟、小时、天、年等，详见Calendar中的静态变量
     * @return
     */
    public static Date addTime(Date date, int amount, int calendarField) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(calendarField, amount);
        return calendar.getTime();
    }

    /**
     * 对时间做加减法，支持对时间做取整
     * 例如求
     * 2016/10/12日的5天后的时间
     * 2016/10/12 22：00 前三小时的时间
     *
     * @param date          需要计算的基础时间
     * @param amount        需要加或者减去的时间数量（负值为减）
     * @param calendarField 时间单位，支持毫秒、分钟、小时、天、年等，详见Calendar中的静态变量
     * @param truncateUnit  对时间取整的单位，例如：
     *                      让输入Date(2016/12/10), 30,
     *                      传Calendar.DAY_OF_MONTH结果取整到年，传Calendar.YEAR
     *                      结果为 2017/1/1 00:00:00.000
     * @return
     */
    public static Date addTime(Date date, int amount, int calendarField, int truncateUnit) {
        Date result = addTime(date, amount, calendarField);
        result = DateUtils.truncate(result, truncateUnit);
        return result;
    }

    /**
     * 求两个日期之间相差的毫秒
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDurationMillis(Date date1, Date date2) {
        return date2.getTime() - date1.getTime();
    }



    /**
     * 根据年和月份获得该月有几周
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    public static int getWeekCount(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int startWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int endWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        return endWeek - startWeek + 1;
    }

    /**
     * 根据年和月获取该月有几天
     *
     * @param year  年份
     * @param month 月份
     * @return
     */
    public static int getDayCount(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);
        return endDay - startDay + 1;
    }







    /**
     * 获取某年/月/周的第一天，时间以当天00:00:00.000为准
     *
     * @param date          要求的时间范围，例如求1994年的第一天，则date为1994
     * @param calendarField 时间范围的类型，例如求1994年的第一天，则calendarField为Calendar.YEAR
     * @return
     */
    public static Date getFirstDay(int date, int calendarField) {
        if (!(calendarField == Calendar.YEAR || calendarField == Calendar.MONTH
                || calendarField == Calendar.WEEK_OF_MONTH || calendarField == Calendar.WEEK_OF_YEAR)) {
            throw new IllegalArgumentException("所求范围无法求第一天");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(calendarField, date);
        return calendar.getTime();
    }

    /**
     * 获取某年/月/周的最后天，时间以当天23:59:59.999为准
     *
     * @param date          要求的时间范围，例如求1994年的最后天，则date为1994
     * @param calendarField 时间范围的类型，例如求1994年的最后天，则calendarField为Calendar.YEAR
     * @return
     */
    public static Date getLastDay(int date, int calendarField) {
        if (!(calendarField == Calendar.YEAR || calendarField == Calendar.MONTH
                || calendarField == Calendar.WEEK_OF_MONTH || calendarField == Calendar.WEEK_OF_YEAR)) {
            throw new IllegalArgumentException("所求范围无法求最后1天");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(calendarField, date);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return new Date(calendar.getTime().getTime() - 1);
    }

    /**
     * 将日期取整（去零头）
     * 例如求 2016/10/24 12:10:30.333 对于日期取整，
     * 则calendarField为Calendar.DAY_OF_MONTH，
     * 结果为 2016/10/24 00:00:00.000
     * <p>
     * 例如求 2016/10/24 12:10:30.333 对于分钟取整，
     * 则calendarField为Calendar.HOUR，
     * 结果为 2016/10/24 12:10:00.000
     *
     * @param date          需要取整的日期
     * @param calendarField 取整单位
     * @return
     */
    public static Date truncate(Date date, int calendarField) {
        return DateUtils.truncate(date, calendarField);
    }

    /**
     * 将日期取整（进位与舍位）
     * 例如, 对时间： 2002/3/28 13:45:01.231, 若对 HOUR进行取整,
     * 结果为：2002/3/28 14:00:00.000.
     * 若对MONTH进行取整，
     * 结果为： 2002/4/1 0:00:00.000.
     *
     * @param date          需要取整的日期
     * @param calendarField 取整单位
     * @return
     */
    public static Date round(Date date, int calendarField) {
        return DateUtils.round(date, calendarField);
    }

    /**
     * 获得日期是当年的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获得日期是当月的第几天
     *
     * @param date
     * @return
     */
    public static int getDayOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得日期是当周的第几天（周几）
     *
     * @param date
     * @return
     */
    public static int getDayOfWeek(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得时间是当天的第几个小时
     *
     * @param date
     * @return
     */
    public static int getHourOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }



    /**
     * LocalDateTime 转换 String
     *
     * @param localDateTime
     * @return
     */
    public static String localDateTime2String(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ofPattern(DatePattern.YYYYMMDDHHmmss.getValue()));
    }

    /**
     * LocalDate 转换 String
     *
     * @param localDate
     * @return
     */
    public static String localDate2String(LocalDate localDate) {
        return localDate.format(DateTimeFormatter.ofPattern(DatePattern.YYYYMMDDHHmmss.getValue()));
    }


    //获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if(null != d) {
            calendar.setTime(d);
        }
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d) calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //获取本周的开始时间
    public static Date getBeginDayOfWeek() {
        Date date = new Date();
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayofweek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayofweek == 1) {
            dayofweek += 7;
        }
        cal.add(Calendar.DATE, 2 - dayofweek);
        return getDayStartTime(cal.getTime());
    }

    //获取本周的结束时间
    public static Date getEndDayOfWeek() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getBeginDayOfWeek());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        return getDayEndTime(weekEndSta);
    }

    //比较日期
    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 1;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    //获取指定毫秒数的对应星期
    public static String getWeek(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        String week = "";
        int cweek = calendar.get(Calendar.DAY_OF_WEEK);
        switch (cweek) {
            case 1:
                week = "日";
                break;
            case 2:
                week = "一";
                break;
            case 3:
                week = "二";
                break;
            case 4:
                week = "三";
                break;
            case 5:
                week = "四";
                break;
            case 6:
                week = "五";
                break;
            case 7:
                week = "六";
                break;
        }
        return "周" + week;

    }

    //格式：2017-10-10 （周一） 10:10:10
    public static String transTime(String date) throws ParseException {
        Date dddd = string2Date(date, "yyyy-MM-dd HH:mm:ss");
        String week = getWeek(dddd.getTime());
        String strTime = date.substring(0, 10) + "(" + week + ")" + date.substring(11, 16);
        return strTime;
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<String> getDatesBetweenTwoDate(String beginDate, String endDate) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sdf.parse(beginDate);
        Date dEnd = sdf.parse(endDate);
        List<String> lDate = new ArrayList<String>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(dBegin);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (dEnd.after(cal.getTime())) {
                lDate.add(sdf.format(cal.getTime()));
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }
    private static ThreadLocal<List<DateFormat>> threadLocal = new ThreadLocal<List<DateFormat>>() {
        @Override
        protected List<DateFormat> initialValue() {

            List<DateFormat> a= new ArrayList<>(2);
            a.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
            a.add(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
            return  a;
        }
    };


    //设置日期格式
   // public static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //设置日期格式 取消活动专用
    //public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public static String getNowDate(){
        return threadLocal.get().get(0).format(new Date());
    }

    public static String format(Object o){
        return   threadLocal.get().get(0).format(o);
    }

    public static Date parse(String time) throws ParseException {

        return  threadLocal.get().get(0).parse(time);
    }

    public static Date getStartOrCancelDate(String startday,String startTime){
        String year = startday.toString().substring(0,4);
        String month  = startday.toString().substring(4,6);
        String day  = startday.toString().substring(6,startday.toString().length());
        Date date= null;

        try {
            date= threadLocal.get().get(1).parse(year+"-"+month+"-"+day+" "+startTime);
            return date;
        } catch (ParseException e) {
            throw new RuntimeException("日期转换异常",e);
        }
    }
}
