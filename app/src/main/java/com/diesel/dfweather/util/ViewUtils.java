package com.diesel.dfweather.util;

import android.view.View;

/**
 * @author Diesel
 *
 *         Time: 2016/8/12
 *
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class ViewUtils {

    /**
     * 显示视图
     */
    public static void visible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 隐藏视图
     */
    public static void invisible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 消失视图
     */
    public static void gone(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }

}
