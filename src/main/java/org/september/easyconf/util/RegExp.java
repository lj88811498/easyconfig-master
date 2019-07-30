package org.september.easyconf.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.*;

/**
 * @Author: Monkey
 * @Date: Created in 11:15  2019/6/24.
 * @Description:
 */
public class RegExp {

    /**
     * 定义yaml文件的分隔符
     */
    public static String CHAR = "\n";

    /**
     * 去除字符串前面的空格
     * @param s1
     * @return
     */
    public static String matcher(String s1) {
        //TODO 匹配一个或多个非空字符
        Pattern pattern = compile("\\S+");
        Matcher matcher = pattern.matcher(s1);
        // 字符串是否与正则表达式相匹配
        int start = matcher.find() ? matcher.start() : 0;
        return s1.substring(start, s1.length());
    }

    /**
     * 去除字符串前面的空格
     * @param s1
     * @return
     */
    public static String replaceFirst(String s1, String charstr) {
        //TODO 匹配一个或多个非空字符
        if (s1.startsWith(charstr)) {

            return s1.replaceFirst(charstr, "");
        }
        return s1;
    }

}
