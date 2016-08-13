package com.diesel.dfweather.util;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * Comments：
 *
 * @author Diesel
 *
 *         Time: 2016/8/13
 *
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 5.0.0
 */
public class DimensionUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dpValue 要转换的dp值
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param pxValue 要转换的px值
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕高度（除去顶部状态栏）
     */
    public static int getDecorViewHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels - getStatusBarHeight(
                context);
    }

    /**
     * 获取顶部状态栏高度
     */
    public static int getStatusBarHeight(Context context) {
        Class<?> c;
        Object obj;
        Field field;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception ex) {
            Log.e("ViewUtils", "get screen status bar height failed. " + ex.getMessage());
        }
        return statusBarHeight;
    }

}
