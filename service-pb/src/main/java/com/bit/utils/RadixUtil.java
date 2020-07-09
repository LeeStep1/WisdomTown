package com.bit.utils;

import java.util.Arrays;

public class RadixUtil {
    /**
     * 获取第i位的二进制数
     *
     * @param num
     * @return
     */
    public static String toFullBinaryString(long num) {
        //规定输出的long型最多有42位（00 00000000 00000000 00000000 00000000 00000000）
        final int size = 64;
        char[] chs = new char[size];
        for (int i = 0; i < size; i++) {
            chs[size - 1 - i] = (char) (((num >> i) & 1) + '0');
        }
        return new String(chs);
    }

    /**
     * 二进制加1
     *
     * @param a
     * @param b
     * @return
     */
    public static String toAdd(String a, String b) {
        StringBuilder sb = new StringBuilder();
        int x = 0;
        int y = 0;
        int pre = 0;//进位
        int sum = 0;//存储进位和另两个位的和

        while (a.length() != b.length()) {//将两个二进制的数位数补齐,在短的前面添0
            if (a.length() > b.length()) {
                b = "0" + b;
            } else {
                a = "0" + a;
            }
        }
        for (int i = a.length() - 1; i >= 0; i--) {
            x = a.charAt(i) - '0';
            y = b.charAt(i) - '0';
            sum = x + y + pre;//从低位做加法
            if (sum >= 2) {
                pre = 1;//进位
                sb.append(sum - 2);
            } else {
                pre = 0;
                sb.append(sum);
            }
        }
        if (pre == 1) {
            sb.append("1");
        }
        return sb.reverse().toString();//翻转返回
    }

    /**
     * 二进制string 转数组
     *
     * @param str
     * @return
     */
    public static String[] toArray(String str) {
        String[] strs = new String[8];
        int j = 0;
        for (int i = 0; i < str.length() / 8; i++) {
            String aa = str.substring(i * 8, (i + 1) * 8);
            strs[j] = aa;
            j++;
        }
        System.out.println(Arrays.toString(strs));
        return strs;
    }

    /**
     * 等级
     *
     * @param str
     * @return
     */
    public static int getlevel(String str) {
        int n = 0;
        int length = str.length() / 8;
        for (int i = 0; i < length; i++) {
            String str2 = str.substring(i * 8, (i + 1) * 8);
            long valueOf = Long.valueOf(str2);
            if (valueOf == 0) {
                break;
            }
            n++;
        }
        return n;
    }

    /**
     * 获取等级
     *
     * @param orgId  党组织ID
     * @param ridTop 是否包含第一位
     * @return
     */
    public static int getLevel(String orgId, boolean ridTop) {
        int n = 0;
        // 将orgId从十进制转化二进制
        String str = change(orgId, 10, 2);
        if (ridTop) {
            if (str.length() > 0) {
                // 去掉第一级
                str = str.substring(1, str.length());
                n += 1;
            }
        }
        int length = str.length() / 8;
        for (int i = 0; i < length; i++) {
            String str2 = str.substring(i * 8, (i + 1) * 8);
            long valueOf = Long.valueOf(str2);
            if (valueOf == 0) {
                break;
            }
            n++;
        }
        return n;
    }

    /**
     * 获取该等级的尾级
     * orgId不能是顶级
     *
     * @param orgId
     * @return
     */
    public static String getLevelEndID(String orgId) {
        StringBuilder basicNo = new StringBuilder();
        int no = 0;
        String str = change(orgId, 10, 2);
        if (str.length() > 0) {
            basicNo.append(str.substring(0, 1));
            str = str.substring(1, str.length());
        }
        int length = str.length() / 8;
        for (int i = 0; i < length; i++) {
            String str2 = str.substring(i * 8, (i + 1) * 8);
            long valueOf = Long.valueOf(str2);
            if (valueOf == 0) {
                break;
            }
            basicNo.append(str2);
            no = i;
        }
        String maxOrgId = toAdd(basicNo.toString(), "1");
        basicNo.delete(0, basicNo.length()).append(maxOrgId);
        if (basicNo.length() < 8) {
            basicNo.append("00000000");
        }
        while (no < 6) {
            basicNo.append("00000000");
            no++;
        }
        // 将二进制转化十进制
        return change(basicNo.toString(), 2, 10);
    }

    /**
     * 获取一级除外的其它级别对应的二级最小ID，用于获取全部支部
     *
     * @param orgId
     * @return
     */
    public static String getMinSecord(String orgId) {
        // 排除一级
        int level = getLevel(orgId, true);
        if (level > 1) {
            StringBuilder basicNo = new StringBuilder();
            int no = 0;
            String str = change(orgId, 10, 2);
            if (str.length() > 0) {
                basicNo.append(str.substring(0, 1));
                str = str.substring(1, str.length());
            }
            int length = str.length() / 8;
            if (length > 0) {
                length = length - 1;
            }
            // 获取二级
            basicNo.append(str.substring(0, 8));
            while (length > 0) {
                basicNo.append("00000000");
                length--;
            }
            System.out.println(basicNo.toString());
            // 将二进制转化十进制
            return change(basicNo.toString(), 2, 10);
        }
        return null;
    }

    /**
     * 将字符串转化二进制
     *
     * @param num  要转换的数
     * @param from 源数的进制，如十进制  10
     * @param to   要转换成的进制，如二进制  2
     * @return 返回字符串
     */
    private static String change(String num, int from, int to) {
        return new java.math.BigInteger(num, from).toString(to);
    }

    /**
     * 获取顶级节点
     *
     * @param orgId
     * @return
     */
    public static String getTopOrgId(String orgId) {
        // 将orgId从十进制转化二进制
        String str = change(orgId, 10, 2);
        if (str.length() > 56) {
            StringBuilder stringBuilder = new StringBuilder(str);
            stringBuilder.replace(str.length() - 56, 57, "00000000000000000000000000000000000000000000000000000000");
            // 将二进制转化十进制
            return change(stringBuilder.toString(), 2, 10);
        }
        return null;
    }

    /**
     * 获取上级组织ID
     *
     * @param orgId
     * @return
     */
    public static String getNextOrgId(String orgId) {
        int level = getLevel(orgId, true);
        if (level == 1)
            return orgId;
        String str = change(orgId, 10, 2);
        StringBuilder stringBuffer = new StringBuilder(str);
        if (str.length() > 56) {
            StringBuffer tail = new StringBuffer();
            for (int i = 0; i < 9 - level; i ++) {
                tail.append("00000000");
            }
            // 一级节点位数
            int firstNum = str.length() - 56;
            int beginNum = firstNum + (level - 2)*8;
            stringBuffer.replace(beginNum, str.length(), tail.toString());
            // 将二进制转化十进制
            return change(stringBuffer.toString(), 2, 10);
        }
        return null;
    }

    /**
     * 获取指定节点组织ID
     *
     * @param orgId 原组织ID
     * @param fixed 需要获取的级别
     * @return
     */
    public static String getFixedOrgId(String orgId, int fixed) {
        int level = getLevel(orgId, true);
        if (level < fixed)
            return null;
        String str = change(orgId, 10, 2);
        StringBuilder stringBuffer = new StringBuilder(str);
        if (str.length() > 56) {
            int firstNum = str.length() - 56;
            int beginNum = firstNum + (fixed - 1) * 8;
            StringBuffer tail = new StringBuffer();
            for (int i = 0; i < 8 - fixed; i ++) {
                tail.append("00000000");
            }
            stringBuffer.replace(beginNum, str.length(), tail.toString());
            // 将二进制转化十进制
            return change(stringBuffer.toString(), 2, 10);
        }
        return null;
    }



    public static void main(String args[]) {
        System.out.println(getNextOrgId("72340168526266368"));
        System.out.println(getFixedOrgId("72340168526266368", 1));
//        int n = getlevel("80512838455525376");
//        System.out.println(n);
    }


    /**
     * 二进制转十进制
     *
     * @param str
     * @param n
     */
    public static int binaryToDecimal(String str, int n) {
        String substring = str.substring(0, n * 8);
        String[] name = toArray(substring);
        String str2 = name[(n - 1)];
        int parseInt = Integer.parseInt(str2, 2);
        return parseInt;
    }

    /**
     * 求最大值  最小值
     *
     * @param a
     * @return
     */
    public static int maxnum(int[] a) {
        int n;
        int min = a[0];
        int i;
        int max = a[0];
        for (n = 0; n <= 9; n++) {
            if (max < a[n]) max = a[n];
        }
        for (i = 0; i <= 9; i++) {
            if (min > a[i]) min = a[i];
        }
        System.out.print("max=" + max);
        System.out.print("min=" + min);
        return max;
    }

    /**
     * String二进制 转Long
     *
     * @param str
     * @return
     */
    public static Long toLong(String str) {
        return Long.parseLong(str, 2);
    }

}
