package com.diesel.dfweather.common;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;

/**
 * Copyright2012-2016  CST.All Rights Reserved
 *
 * Comments：<p> Using static set of drawables allows us to easily determine state of image request
 * by simply looking what kind of drawable is passed to image view. </p>
 *
 * @author Diesel
 *
 *         Time: 2016/8/12
 *
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class Drawables {

    public static Drawable sPlaceholderDrawable;

    public static Drawable sErrorDrawable;

    public static void init(final Resources resources) {
        if (null == sPlaceholderDrawable) {

        }
        if (null == sErrorDrawable) {

        }
    }

}
