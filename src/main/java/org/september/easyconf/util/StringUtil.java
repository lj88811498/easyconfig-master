package org.september.easyconf.util;

import com.alibaba.druid.sql.visitor.functions.Char;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: Monkey
 * @Date: Created in 10:36  2019/6/24.
 * @Description:
 */
public class StringUtil {

    public static String change (String s) {
        if (StringUtils.isNotBlank(s)) {
            String str = s.trim()
                    //                    .replaceAll(" ", "")
                    .replaceAll(": ", "=");
           if (str.contains(":") && !str.contains("://") && !str.contains("=")) {
               str = str.replaceAll(":", ".");
           }
           if(isContainChinese(str)){
               return newString(str);
           }else{
               return str;
           }
        } else {
            return s;
        }
    }

    /**
     * 获取转码后的新字符串
     * @param str
     * @return
     */
    public static String newString(String str){
        Matcher matcher = Pattern.compile("=").matcher(str);
        String resultStr = "";
        if(matcher.find()){
            //起始索引位置
            int start = matcher.start();
            //以第一个等号为界分割字符串
            String subStart = str.substring(0, start+1);
            String subEnd = str.substring(start+1);
            // TODO 汉字转Unicode码
            String unicode = getUnicode(subEnd);
            resultStr = String.valueOf(subStart + unicode);
        }else{
            resultStr = convert(str);
        }
        return resultStr;
    }

    /**
     * 把指定位置汉字替换成Unicode
     * @param str
     * @return
     */
    public static String getUnicode(String str){
        StringBuffer sb = new StringBuffer(str);
        if (sb != null && sb.length() > 0) {
            for (int i = 0; i < sb.length(); i++) {
                char c = sb.charAt(i);
                boolean bol = isContainChinese(String.valueOf(c));
                if(bol){
                    String cs = convert(String.valueOf(c));
                    sb.deleteCharAt(i);
                    sb.insert(i,cs);
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String str = "hh好he ha \n ba \n 合 \n 欢 拉拉";
        System.out.println(getUnicode(str));
    }

    /**
     * 字符串转你unicode码
     * @param str
     * @return
     */
    public static String convert(String str){
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++){
            c = str.charAt(i);
            sb.append("\\u");
            j = (c >>>8); //取出高8位 
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位 
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1) {
                sb.append("0");
            }
            sb.append(tmp);

        }
        return (new String(sb));
    }

    /**
     * 验证字符串是否包含中文
     * @param str
     * @return true 包含，false 不包含
     * @throws Exception
     */
    public static boolean isContainChinese(String str){
        if (com.alibaba.druid.util.StringUtils.isEmpty(str)) {
            RecordLogUtil.info("空字符串!");
        }
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|\\！|\\，|\\。|\\（|\\）|\\《|\\》|\\“|\\”|\\？|\\：|\\；|\\【|\\】]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /** 
     * 将unicode 字符串 
     * @param str 待转字符串 
     * @return 普通字符串 
     */
    public String revert(String str){
        str = (str == null ? "" : str);
        if (str.indexOf("\\u") == -1)//如果不是unicode码则原样返回 
            return str;

        StringBuffer sb = new StringBuffer(1000);

        for (int i = 0; i < str.length() - 6;){
            String strTemp = str.substring(i, i + 6);
            String value = strTemp.substring(2);
            int c = 0;
            for (int j = 0; j < value.length(); j++){
                char tempChar = value.charAt(j);
                int t = 0;
                switch (tempChar){
                    case 'a':
                        t = 10;
                        break;
                    case 'b':
                        t = 11;
                        break;
                    case 'c':
                        t = 12;
                        break;
                    case 'd':
                        t = 13;
                        break;
                    case 'e':
                        t = 14;
                        break;
                    case 'f':
                        t = 15;
                        break;
                    default:
                        t = tempChar - 48;
                        break;
                }

                c += t * ((int) Math.pow(16, (value.length() - j - 1)));
            }
            sb.append((char) c);
            i = i + 6;
        }
        return sb.toString();
    }
}
