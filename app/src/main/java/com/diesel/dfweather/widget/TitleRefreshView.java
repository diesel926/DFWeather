package com.diesel.dfweather.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Comments：
 *
 * @author Diesel
 *         Time: 2016/8/13
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class TitleRefreshView extends ScrollView {

    private ContentScrollView mScrollView;

    private HeaderViewPaddingChangeListener mListener;

    public TitleRefreshView(Context context) {
        this(context, null);
    }

    public TitleRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public int getTitleHeight() {
        return 0;
    }

    public void setScrollView(ContentScrollView scrollView) {
        mScrollView = scrollView;
    }

    public void setHeaderViewPaddingChangeListener(HeaderViewPaddingChangeListener listener) {
        mListener = listener;
    }

    protected interface HeaderViewPaddingChangeListener {
        void onPaddingChanged(int offsetY);
    }
}
