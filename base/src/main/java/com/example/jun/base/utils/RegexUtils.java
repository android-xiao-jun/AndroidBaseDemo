package com.example.jun.base.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式工具，用来校验是否符合基本规则
 */
public class RegexUtils {
    /*
    中国电信号段   133,149,153,173,174,177,180,181,189,199
    中国联通号段   130,131,132,145,146,155,156,166,175,176,185,186
    中国移动号段   134(0-8),135,136,137,138,139,147,148,150,151,152,157,158,159,165,178,182,183,184,187,188,198，190、197、196、192
     */
    // 验证手机号
    public static boolean isMobile(String phoneNum) {
//        String regex = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][0,1,2,6,7,8,9]))[\\d]{8}$";
        String regex = "^[1]([3456789])[\\d]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNum);
        return matcher.matches();
    }

    /**
     * 姓名 为 中文 大于 2 不大于 5个字
     **/
    public static boolean isName(String realname) {
        int length = countLength(realname);
        if (length < 4 || length > 10) return false;
        Pattern pattern = Pattern.compile("^[\u4E00-\u9FA5]+$");
        Matcher matcher = pattern.matcher(realname);
        return matcher.matches();
    }

    /**
     * 邮箱
     */
    public static boolean isEmail(String email) {
//        ^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    /**
     * 身份证号 为 0-9 a-z A-Z 不大于18个字
     **/
    public static boolean isIdCard(String idNum) {

        int length = countLength(idNum);
        if (length != 18) return false;
        String front17 = idNum.substring(0, 17);//前面17个字符
        Pattern pattern = Pattern.compile("^[0-9]+$");
        Matcher matcher = pattern.matcher(front17);
        String last1 = idNum.substring(17);//最后一个字符
        Pattern patternLast = Pattern.compile("^[a-zA-Z0-9]+$");
        Matcher matcherLast = patternLast.matcher(last1);
        return matcher.matches() && matcherLast.matches();
    }

    /**
     * 将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**）。
     * 然后再获取长度。
     */
    public static int countLength(String string) {
        string = string.replaceAll("[^\\x00-\\xff]", "**");
        return string.length();
    }

    //支付密码 6~20数字或字母或组合
    public static String RegexUtils_PAY = "请输入支付密码6~20位数字或字母或组合";

    public static boolean isPay(String pay) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9]{6,20}$");
        Matcher matcher = pattern.matcher(pay);
        return matcher.matches();
    }

    //登录密码8~20数字、字母组合
    public static String RegexUtils_PSW = "请输入登录密码8~20位数字、字母组合";

    public static boolean isPsw(String psw) {
        Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,20}$");
        Matcher matcher = pattern.matcher(psw);
        return matcher.matches();
    }
}
