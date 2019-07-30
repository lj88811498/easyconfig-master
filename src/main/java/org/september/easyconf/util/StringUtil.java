package org.september.easyconf.util;

import org.apache.commons.lang3.StringUtils;

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
           if (str.contains(":") && !str.contains("://")) {
               str = str.replaceAll(":", ".");
           }
            return str;
        } else {
            return s;
        }
    }
}
