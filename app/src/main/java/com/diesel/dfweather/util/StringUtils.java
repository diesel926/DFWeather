package com.diesel.dfweather.util;

import android.text.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Comments：字符串相关的工具类
 *
 * @author wangj
 *
 *         Time: 2016/8/9
 *
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class StringUtils {

    public static String inputStreamToString(InputStream in) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = -1;
        try {
            while ((count = in.read(buffer)) != -1) {
                out.write(buffer, 0, count);
            }
            return new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean hasContain(List<String> list, String content) {
        if (list != null && list.size() > 0) {
            for (String l : list) {
                if (!TextUtils.isEmpty(l) && l.equalsIgnoreCase(content)) {
                    return true;
                }
            }
        }
        return false;
    }
}
