package com.diesel.dfweather.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Comments：
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
public class DateUtils {

    public static final String HH_MM = "HH:mm";

    public static final String TIME_YYYY_MM_DD = "yyyy-MM-dd";

    public static final String TIME_MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";

    public static final String TIME_YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";

    public static final String TIME_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间字符串格式化
     *
     * @param date      需要被处理的日期字符串
     * @param parseStr  需要被处理的日期的格式串
     * @param formatStr 最终返回的日期字符串的格式串
     * @return 已经格式化的日期字符串
     */
    public static String formatDate(String date, String parseStr, String formatStr) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(parseStr, Locale.getDefault());
        Date d;
        try {
            d = sdf.parse(date);
            sdf.applyPattern(formatStr);
            return sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatDate(long date, String formatStr) {
        if (date <= 0) {
            return "";
        }
        DateFormat sdf = new SimpleDateFormat(formatStr, Locale.getDefault());
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            return sdf.format(calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String formatDate(String date, String format) {
        if (TextUtils.isEmpty(date)) {
            return "";
        }
        String str = date;
        if (str.contains("T")) {
            str = str.replace("T", " ");
        }
        if (str.contains("/")) {
            str = str.replaceAll("/", "-");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        Date d;
        try {
            d = sdf.parse(str);
            return sdf.format(d);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWeak(long time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String week = "星期日";
        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
            default:
                break;
        }
        return week;
    }

    /**
     * 比较两个日期大小，如果第一个小于第二个返回true，
     */
    public static boolean compareDate(String first, String second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date firstDate;
        try {
            firstDate = sdf.parse(first);
            Date secondDate = sdf.parse(second);
            return firstDate.before(secondDate);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 返回指定时间字符串
     *
     * @return 刚刚------2分钟及以内的
     *         今天 hh:mm------除了刚刚，且在当天24:00内的
     *         yy-mm-dd hh:mm----除了以上的，就是它
     */
    public static String getProductHistory(long time) {
        String result = null;
        if ((System.currentTimeMillis() - time) <= 2 * 1000 * 60) {
            result = "刚刚";
        } else if (isSameDay(time, System.currentTimeMillis())) {
            result = "今天" + formatDate(time, HH_MM);
        } else {
            result = formatDate(time, TIME_YYYY_MM_DD_HH_MM);
        }

        return result;
    }

    public static boolean isSameDay(long timeOne, long timeTwo) {
        Date dateOne = new Date(timeOne);
        Date dateTwo = new Date(timeTwo);
        if (dateOne.getYear() == dateTwo.getYear()
                && dateOne.getMonth() == dateTwo.getMonth()
                && dateOne.getDay() == dateTwo.getDay()) {
            return true;
        }
        return false;
    }

}
