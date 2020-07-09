/**
 * 身份证前6位【ABCDEF】为行政区划数字代码（简称数字码）说明（参考《GB/T 2260-2007 中华人民共和国行政区划代码》）：
 * 该数字码的编制原则和结构分析，它采用三层六位层次码结构，按层次分别表示我国各省（自治区，直辖市，特别行政区）、
 * 市（地区，自治州，盟）、县（自治县、县级市、旗、自治旗、市辖区、林区、特区）。
 * 数字码码位结构从左至右的含义是：
 * 第一层为AB两位代码表示省、自治区、直辖市、特别行政区；
 * 第二层为CD两位代码表示市、地区、自治州、盟、直辖市所辖市辖区、县汇总码、省（自治区）直辖县级行政区划汇总码，其中：
 * ——01~20、51~70表示市，01、02还用于表示直辖市所辖市辖区、县汇总码；
 * ——21~50表示地区、自治州、盟；
 * ——90表示省（自治区）直辖县级行政区划汇总码。
 * 第三层为EF两位表示县、自治县、县级市、旗、自治旗、市辖区、林区、特区，其中：
 * ——01~20表示市辖区、地区（自治州、盟）辖县级市、市辖特区以及省（自治区）直辖县级行政区划中的县级市，01通常表示辖区汇总码；
 * ——21~80表示县、自治县、旗、自治旗、林区、地区辖特区；
 * ——81~99表示省（自治区）辖县级市。
 */
package com.bit.utils;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * <p>
 * 类说明:身份证合法性校验
 * </p>
 * <p>
 * --15位身份证号码：第7、8位为出生年份(两位数)，第9、10位为出生月份，第11、12位代表出生日期，第15位代表性别，奇数为男，偶数为女。
 * --18位身份证号码：第7、8、9、10位为出生年份(四位数)，第11、第12位为出生月份，第13、14位代表出生日期，第17位代表性别，奇数为男，偶数为女。
 * </p>
 */
public class CheckUtils {

    @Data
    public static class IdentityCardMeta {

        private String birthday;

        private Integer sex;

        private String province;

        private String provinceId;

    }

    /**
     * 省，直辖市代码表： { 11:"北京",12:"天津",13:"河北",14:"山西",15:"内蒙古",
     * 21:"辽宁",22:"吉林",23:"黑龙江",31:"上海",32:"江苏",
     * 33:"浙江",34:"安徽",35:"福建",36:"江西",37:"山东",41:"河南",
     * 42:"湖北",43:"湖南",44:"广东",45:"广西",46:"海南",50:"重庆",
     * 51:"四川",52:"贵州",53:"云南",54:"西藏",61:"陕西",62:"甘肃",
     * 63:"青海",64:"宁夏",65:"新疆",71:"台湾",81:"香港",82:"澳门",91:"国外"}
     */
    private static final String CODE_AND_CITY[][] = { { "11", "北京" }, { "12", "天津" },
            { "13", "河北" }, { "14", "山西" }, { "15", "内蒙古" }, { "21", "辽宁" },
            { "22", "吉林" }, { "23", "黑龙江" }, { "31", "上海" }, { "32", "江苏" },
            { "33", "浙江" }, { "34", "安徽" }, { "35", "福建" }, { "36", "江西" },
            { "37", "山东" }, { "41", "河南" }, { "42", "湖北" }, { "43", "湖南" },
            { "44", "广东" }, { "45", "广西" }, { "46", "海南" }, { "50", "重庆" },
            { "51", "四川" }, { "52", "贵州" }, { "53", "云南" }, { "54", "西藏" },
            { "61", "陕西" }, { "62", "甘肃" }, { "63", "青海" }, { "64", "宁夏" },
            { "65", "新疆" }, { "71", "台湾" }, { "81", "香港" }, { "82", "澳门" },
            { "91", "国外" } };

    protected static final Map<String, String> CODE_AND_CITY_MAP = new HashMap<>(CODE_AND_CITY.length);

    // 每位加权因子
    private static final int POWER[] = { 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };

    // 第18位校检码
    private static final String VERIFY_CODE[] = { "1", "0", "X", "9", "8", "7", "6", "5",
            "4", "3", "2" };

    private static final Pattern IDENTITY_CARD_PATTERN = Pattern.compile("(^\\d{15}$)|(\\d{17}(?:\\d|x|X)$)");

    private static final Pattern IDENTITY_CARD_15_PATTERN = Pattern.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");

    private static final Pattern IDENTITY_CARD_18_PATTERN = Pattern.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([\\d|x|X]{1})$");

    private static final Pattern DIGITAL_PATTERN = Pattern.compile("^[0-9]*$");

    private static final SimpleDateFormat SHORT_DATE = new SimpleDateFormat("yyMMdd");

    static {
        for (String[] codeAndCity : CODE_AND_CITY) {
            CODE_AND_CITY_MAP.put(codeAndCity[0], codeAndCity[1]);
        }
    }

    /**
     * 验证所有的身份证的合法性
     *
     * @param identityCard
     * @return
     */
    public static boolean isValidIdentityCard(String identityCard) {
        if (identityCard.length() == 15) {
            identityCard = convertIdentityCardTo18bit(identityCard);
        }
        return isValid18IdentityCard(identityCard);
    }

    public static IdentityCardMeta getIdentityCardMeta(String identityCard) {
        if (identityCard.length() == 15) {
            identityCard = convertIdentityCardTo18bit(identityCard);
        }

        return get18IdentityCardMeta(identityCard);
    }

    /**
     * <p>
     * 判断18位身份证的合法性
     * </p>
     * 根据〖中华人民共和国国家标准GB11643-1999〗中有关公民身份号码的规定，公民身份号码是特征组合码，由十七位数字本体码和一位数字校验码组成。
     * 排列顺序从左至右依次为：六位数字地址码，八位数字出生日期码，三位数字顺序码和一位数字校验码。
     * <p>
     * 顺序码: 表示在同一地址码所标识的区域范围内，对同年、同月、同 日出生的人编定的顺序号，顺序码的奇数分配给男性，偶数分配 给女性。
     * </p>
     * <p>
     * 1.前1、2位数字表示：所在省份的代码； 2.第3、4位数字表示：所在城市的代码； 3.第5、6位数字表示：所在区县的代码；
     * 4.第7~14位数字表示：出生年、月、日； 5.第15、16位数字表示：所在地的派出所的代码；
     * 6.第17位数字表示性别：奇数表示男性，偶数表示女性；
     * 7.第18位数字是校检码：也有的说是个人信息码，一般是随计算机的随机产生，用来检验身份证的正确性。校检码可以是0~9的数字，有时也用x表示。
     * </p>
     * <p>
     * 第十八位数字(校验码)的计算方法为： 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：7 9 10 5 8 4
     * 2 1 6 3 7 9 10 5 8 4 2
     * </p>
     * <p>
     * 2.将这17位数字和系数相乘的结果相加。
     * </p>
     * <p>
     * 3.用加出来和除以11，看余数是多少？
     * </p>
     * 4.余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证的号码为1 0 X 9 8 7 6 5 4 3
     * 2。
     * <p>
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。如果余数是10，身份证的最后一位号码就是2。
     * </p>
     *
     * @param identityCard
     * @return
     */
    public static boolean isValid18IdentityCard(String identityCard) {
        // 非18位为假
        if (identityCard.length() != 18) {
            return false;
        }
        // 获取前17位
        String front17 = identityCard.substring(0, 17);
        // 获取第18位
        String the18Code = identityCard.substring(17, 18);
        char c[] = null;
        String checkCode = "";
        // 是否都为数字
        if (isDigital(front17)) {
            c = front17.toCharArray();
        } else {
            return false;
        }

        int bit[] = new int[front17.length()];

        bit = conventCharToInt(c);

        int sum17 = 0;

        sum17 = getPowerSum(bit);

        // 将和值与11取模得到余数进行校验码判断
        checkCode = getCheckCodeBySum(sum17);
        if (null == checkCode) {
            return false;
        }
        // 将身份证的第18位与算出来的校码进行匹配，不相等就为假
        if (!the18Code.equalsIgnoreCase(checkCode)) {
            return false;
        }

        return true;
    }

    /**
     * 验证15位身份证的合法性,该方法验证不准确，最好是将15转为18位后再判断，该类中已提供。
     *
     * @param identityCard
     * @return
     */
    public static boolean isValid15IdentityCard(String identityCard) {
        // 非15位为假
        if (identityCard.length() != 15) {
            return false;
        }

        // 是否全都为数字
        if (!isDigital(identityCard)) {
            return false;
        }

        String provinceId = identityCard.substring(0, 2);
        String birthday = identityCard.substring(6, 12);
        int year = Integer.parseInt(identityCard.substring(6, 8));
        int month = Integer.parseInt(identityCard.substring(8, 10));
        int day = Integer.parseInt(identityCard.substring(10, 12));

        // 判断是否为合法的省份
        boolean flag = CODE_AND_CITY_MAP.containsKey(provinceId);
        if (!flag) {
            return false;
        }
        // 该身份证生出日期在当前日期之后时为假
        Date birthDate = null;
        try {
            birthDate = SHORT_DATE.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

        if (new Date().before(birthDate)) {
            return false;
        }

        // 判断是否为合法的年份
        GregorianCalendar curDay = new GregorianCalendar();
        int curYear = curDay.get(Calendar.YEAR);
        int year2bit = Integer.parseInt(String.valueOf(curYear)
                .substring(2));

        // 判断该年份的两位表示法，小于50的和大于当前年份的，为假
        if ((year < 50 && year > year2bit)) {
            return false;
        }

        // 判断是否为合法的月份
        if (month < 1 || month > 12) {
            return false;
        }

        // 判断是否为合法的日期
        boolean legal = false;
        curDay.setTime(birthDate); // 将该身份证的出生日期赋于对象curDay
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                legal = (day >= 1 && day <= 31);
                break;
            case 2: // 公历的2月非闰年有28天,闰年的2月是29天。
                if (curDay.isLeapYear(curDay.get(Calendar.YEAR))) {
                    legal = (day >= 1 && day <= 29);
                } else {
                    legal = (day >= 1 && day <= 28);
                }
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                legal = (day >= 1 && day <= 30);
                break;
        }

        return legal;
    }

    /**
     * 将15位的身份证转成18位身份证
     *
     * @param identityCard
     * @return
     */
    public static String convertIdentityCardTo18bit(String identityCard) {
        String front17 = null;
        // 非15位身份证
        if (identityCard.length() != 15) {
            return null;
        }

        if (!isDigital(identityCard)) {
            return null;
        }

        // 获取出生年月日
        String birthday = identityCard.substring(6, 12);
        Date birthDate = null;
        try {
            birthDate = SHORT_DATE.parse(birthday);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(birthDate);
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        front17 = identityCard.substring(0, 6) + year + identityCard.substring(8);

        char c[] = front17.toCharArray();
        String checkCode = "";

        // 将字符数组转为整型数组
        int[] bit = conventCharToInt(c);
        int sum17 = 0;
        sum17 = getPowerSum(bit);

        // 获取和值与11取模得到余数进行校验码
        checkCode = getCheckCodeBySum(sum17);
        // 获取不到校验位
        if (null == checkCode) {
            return null;
        }

        // 将前17位与第18位校验码拼接
        front17 += checkCode;

        return front17;
    }

    /**
     * 15位和18位身份证号码的基本数字和位数验校
     *
     * @param identityCard
     * @return
     */
    public static boolean isIdentityCard(String identityCard) {
        return identityCard != null
                && !"".equals(identityCard)
                && IDENTITY_CARD_PATTERN.matcher(identityCard).matches();
    }

    /**
     * 15位身份证号码的基本数字和位数验校
     *
     * @param identityCard
     * @return
     */
    public static boolean is15IdentityCard(String identityCard) {
        return identityCard != null
                && !"".equals(identityCard)
                && IDENTITY_CARD_15_PATTERN.matcher(identityCard).matches();
    }

    /**
     * 18位身份证号码的基本数字和位数验校
     *
     * @param identityCard
     * @return
     */
    public static boolean is18IdentityCard(String identityCard) {
        return identityCard != null
                && !"".equals(identityCard)
                && IDENTITY_CARD_18_PATTERN.matcher(identityCard).matches();
    }

    /**
     * 数字验证
     *
     * @param str
     * @return
     */
    public static boolean isDigital(String str) {
        return str != null && !"".equals(str) && DIGITAL_PATTERN.matcher(str).matches();
    }

    /**
     * 将身份证的每位和对应位的加权因子相乘之后，再得到和值
     *
     * @param bit
     * @return
     */
    public static int getPowerSum(int[] bit) {

        int sum = 0;

        if (POWER.length != bit.length) {
            return sum;
        }

        for (int i = 0; i < bit.length; i++) {
            for (int j = 0; j < POWER.length; j++) {
                if (i == j) {
                    sum = sum + bit[i] * POWER[j];
                }
            }
        }
        return sum;
    }

    /**
     * 将和值与11取模得到余数进行校验码判断
     *
     * @param sum17
     * @return 校验位
     */
    public static String getCheckCodeBySum(int sum17) {
        return VERIFY_CODE[sum17 % 11];
    }

    /**
     * 将字符数组转为整型数组
     *
     * @param c
     * @return
     * @throws NumberFormatException
     */
    public static int[] conventCharToInt(char[] c) throws NumberFormatException {
        int[] a = new int[c.length];
        int k = 0;
        for (char temp : c) {
            a[k++] = Integer.parseInt(String.valueOf(temp));
        }
        return a;
    }

    public static IdentityCardMeta get18IdentityCardMeta(String identityCard) {
        if (!isValid18IdentityCard(identityCard))
            return null;

        IdentityCardMeta identityCardMeta = new IdentityCardMeta();
        identityCardMeta.setProvinceId(identityCard.substring(0, 2));
        identityCardMeta.setProvince(CODE_AND_CITY_MAP.get(identityCardMeta.getProvinceId()));
        identityCardMeta.setBirthday(identityCard.substring(6, 10).concat("-").concat(identityCard.substring(10, 12))
            .concat("-").concat(identityCard.substring(12, 14)));
        identityCardMeta.setSex(2 - Integer.valueOf(identityCard.substring(16, 17)) % 2);
        return identityCardMeta;
    }


    /**
     * 判断是不是合法手机号码
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        Pattern pattern = Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");
        return pattern.matcher(mobile).matches();

    }

    /**
     * 是否为座机 (010-66571346)
     *
     * @param phone
     * @return
     */
    public static boolean isPhone(String phone) {
        Pattern pattern = Pattern.compile("^0[0-9]{2,3}[-|－][0-9]{7,8}([-|－][0-9]{1,4})?$");
        return pattern.matcher(phone).matches();
    }


    public static void main(String[] args) {
        boolean flag = isValidIdentityCard("110101199003071292");
        System.out.println(flag);

        boolean flag1 = isMobile("196666555");
        boolean flag2 = isPhone("010-66571346");
        System.out.println(flag1);
        System.out.println(flag2);
    }


}
