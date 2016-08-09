package com.diesel.dfweather.util;

import android.support.annotation.StringRes;
import android.widget.Toast;

import com.diesel.dfweather.DFApplication;

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
 * @version 5.0.0
 */
public class ToastUtils {

    private static Toast mToast = null;

    public static void show(@StringRes String msg, boolean showLong) {
        if (null == mToast) {
            mToast = Toast.makeText(DFApplication.getInstance(), msg,
                    showLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        } else {
            mToast.setText(msg);
            mToast.setDuration(showLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void show(String msg) {
        show(msg, false);
    }

}
