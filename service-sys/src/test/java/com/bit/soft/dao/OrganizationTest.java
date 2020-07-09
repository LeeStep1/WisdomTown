package com.bit.soft.dao;

import com.bit.ServiceSysApplication;
import com.bit.module.system.bean.PbOrganization;
import com.bit.module.system.dao.PbOrganizationDao;
import com.bit.module.system.service.UserService;
import com.bit.module.system.service.impl.OrgServiceImpl;
import com.bit.module.system.vo.UserVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * @Description: springboot 测试redis 集群
 * @Author: mifei
 * @Date: 2018-09-18
 **/
@RunWith(SpringJUnit4ClassRunner.class) // SpringJUnit支持，由此引入Spring-Test框架支持！
@SpringBootTest(classes = ServiceSysApplication.class)
// 指定我们SpringBoot工程的Application启动类,1.5.4摒弃了SpringApplicationConfiguration注解
@WebAppConfiguration
public class OrganizationTest {

    @Autowired
    private UserService userService;
    @Autowired
    private PbOrganizationDao pbOrganizationDao;
    @Autowired
    private OrgServiceImpl orgServiceImpl;
    @Test
    public void treePbOrg() {
        orgServiceImpl.treePbOrg();
    }


    @Test
    public void testLogin() {
        UserVO userVo = new UserVO();
        userVo.setUsername("mifei");
        userVo.setPassword("abc");
        //userService.login(userVo);
    }

    /**
     * 打印所有组织代码的二进制代码
     */
    @Test
    public void printPbOrganizationCode() {
        List<PbOrganization> list = pbOrganizationDao.findAll(null);
        for (PbOrganization pbo : list) {
            Long l = Long.parseLong(pbo.getId());
            printBinaryString(l);
            System.out.println(" "+pbo.getId() + " __" + pbo.getName() + "__ " + l);
        }
    }

    public static void main(String[] args) {
        printBinaryString(72057594037927936L);
    }


    public static void printBinaryString(Long l) {
        String s = toFullBinaryString(l);
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            if (i % 8 == 0) {
                System.out.print(" ");
            }
            System.out.print(cs[i]);
        }
    }


    public static String add(String a, String b) {
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

    private static String toFullBinaryString(long num) {
        //规定输出的long型最多有42位（00 00000000 00000000 00000000 00000000 00000000）
        final int size = 64;
        char[] chs = new char[size];
        for (int i = 0; i < size; i++) {
            /**
             * 目的：获取第i位的二进制数
             * 解析：
             *     ((num >> i) & 1)：
             *     因为1的二进制的特殊性（0000 0001），在进行“&”运算时，只有末位会被保留下来（比如，末位为“1”，运算
             * 后为1；末位为0，运算后为0），其他位置全部被置为0。
             *     这样当需要将一个整型转换成二进制表示时，需要取出每个位置的二进制数值（1或者0），那么就可以右移该位置
             * 数，与“1”进行“&”运算。
             *     比如：取出3（0000 0011）的第2位（也就是1），则右移1位--》得到“0000 0001”，然后与1（“0000 0001”）
             * 进行“&”运算，得到“1”。以此类推即可得到每个位置为二进制数
             *     “((num >> i) & 1) + '0'”：
             *     字符‘0’的“ASCLL”码对应的数值为48，比如"(char) (49)"那么计算字符的方法就是：49-48 = 1，所以对应的
             * 字符就是'1',所以“((num >> i) & 1) + '0'”这段代码的意思就是，取出“num”二进制时对应的每个位置数值（只能
             * 为“0”或“1”，不过此时还是二进制表示法：0 0000 0000 ； 1 0000 0001）。之后，与字符'0'对应的数值“48”进行
             * 加运算，结果只能为（48 或者 49），再经过（char）强转后（“char”强转就是根据传入的数值，与“48”进行比较，
             * 比如“49”，多1，那么对应的字符就是“1”），就变成了字符,“0”或者“1”，就取出了特定位置的二进制数值
             *
             */
            chs[size - 1 - i] = (char) (((num >> i) & 1) + '0');
        }
        return new String(chs);
    }
}
