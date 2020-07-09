package com.bit.utils;

import java.util.Arrays;

public class RadixUtil {
    /**
     * 获取第i位的二进制数
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
     * @param a
     * @param b
     * @return
     */
    public static String toAdd(String a,String b){
        StringBuilder sb=new StringBuilder();
        int x=0;
        int y=0;
        int pre=0;//进位
        int sum=0;//存储进位和另两个位的和

        while(a.length()!=b.length()){//将两个二进制的数位数补齐,在短的前面添0
            if(a.length()>b.length()){
                b="0"+b;
            }else{
                a="0"+a;
            }
        }
        for(int i=a.length()-1;i>=0;i--){
            x=a.charAt(i)-'0';
            y=b.charAt(i)-'0';
            sum=x+y+pre;//从低位做加法
            if(sum>=2){
                pre=1;//进位
                sb.append(sum-2);
            }else{
                pre=0;
                sb.append(sum);
            }
        }
        if(pre==1){
            sb.append("1");
        }
        return sb.reverse().toString();//翻转返回
    }

    /**
     * 二进制string 转数组
     * @param str
     * @return
     */
    public static String[] toArray(String str) {
        String[] strs=new String[8];
        int j=0;
        for(int i=0;i<str.length()/8;i++){
            String aa=str.substring(i*8, (i+1)*8);
            strs[j]=aa;
            j++;
        }
        System.out.println(Arrays.toString(strs));
        return strs;
    }

    /**
     * 等级
     * @param str
     * @return
     */
    public static int getlevel(String str) {
        int n=0;
        for(int i=0;i<str.length()/3;i++){
            String str2=str.substring(i*3, (i+1)*3);
            long valueOf = Long.valueOf(str2);
            if (valueOf==0){
                break;
            }
            n++;
        }
        return n;
    }

    public static int getOAlevel(String str) {
        int n=0;
        for(int i=0;i<str.length()/3;i++){
            String str2=str.substring(i*3, (i+1)*3);
            long valueOf = Long.valueOf(str2);
            if (valueOf==0){
                break;
            }
            n++;
        }
        return n;
    }

    /**
     * 二进制转十进制
     * @param str
     * @param n
     */
    public static int binaryToDecimal(String str,int n) {
        String substring = str.substring(0, n * 8);
        String[] name = toArray(substring);
        String str2 = name[(n-1)];
        int parseInt = Integer.parseInt(str2,2);
        return parseInt;
    }

    /**
     * 求最大值  最小值
     * @param a
     * @return
     */
    public static int maxnum(int[] a){
        int n;
        int min=a[0];
        int i;
        int max=a[0];
        for(n=0;n<=9;n++) {
            if(max<a[n])max=a[n];
        }
        for(i=0;i<=9;i++) {
            if(min>a[i])min=a[i];
        }
        System.out.print("max="+max);
        System.out.print("min="+min);
        return max;
    }

    /**
     * String二进制 转Long
     * @param str
     * @return
     */
    public static Long toLong(String str){
        return Long.parseLong(str,2);
    }

}
