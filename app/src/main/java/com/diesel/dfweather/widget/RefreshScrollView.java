package com.diesel.dfweather.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Comments：
 *
 * @author Diesel
 *         Time: 2016/8/15
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class RefreshScrollView extends LinearLayout {

    private View mTitleLayout;

    private View mShowLayout;

    private View mHideLayout;

    private ContentScrollView mContentScrollView;

    private TitleRefreshView mTitleRefreshView;

    private OnScrollingListener mOnScrollingListener;

    public RefreshScrollView(Context context, View titleLayout, View showLayout,
            View hideLayout) {
        super(context);
        mTitleLayout = titleLayout;
        mShowLayout = showLayout;
        mHideLayout = hideLayout;
        initView();
    }

    public RefreshScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public interface OnScrollingListener {

        void onScrolling(int scrollY, float cx);
    }

    private void initView() {
        setOrientation(VERTICAL);

        mTitleRefreshView = new TitleRefreshView(getContext(), mTitleLayout);
        mContentScrollView = new ContentScrollView(getContext(), mTitleRefreshView, mShowLayout,
                mHideLayout);
        mContentScrollView.setVerticalScrollBarEnabled(false);
        addView(mTitleRefreshView);
        addView(mContentScrollView);
    }

    public void setOnScrollingListener(OnScrollingListener listener) {
        this.mOnScrollingListener = listener;
    }

    public void setRefreshListener(TitleRefreshView.RefreshViewListener listener) {
        mTitleRefreshView.setRefreshListener(listener);
    }

    public void refreshComplete() {
        mTitleRefreshView.onRefreshComplete();
    }

    /**
     * 把刷新状态改成正在刷新
     */
    public void refreshing() {
        mTitleRefreshView.onRefreshing();
    }

    public TextView getRefreshTimeView() {
        return mTitleRefreshView.getRefreshTimeView();
    }

    public int getScrollViewY() {
        return mContentScrollView.getScrollY();
    }

    public void setScrollViewY(int value) {
        mContentScrollView.scrollTo(mContentScrollView.getScrollX(), value);
//        mContentScrollView.changeBgAlpha(value);
    }

    public ContentScrollView getScrollView() {
        return mContentScrollView;
    }

    public View getTitleLayout() {
        return mTitleLayout;
    }

    public View getShowLayout() {
        return mShowLayout;
    }

    public View getHideLayout() {
        return mHideLayout;
    }

    float cx = 0;

    private int lastScrollY;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            int scrollY = getScrollViewY();

            //此时的距离和记录下的距离不相等，在隔5毫秒给handler发送消息
            if (lastScrollY != scrollY) {
                lastScrollY = scrollY;
                handler.sendMessageDelayed(handler.obtainMessage(), 10);
            }
            if (mOnScrollingListener != null) {
                mOnScrollingListener.onScrolling(scrollY, cx);
            }
        }
    };

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mOnScrollingListener != null) {
            if (cx == 0) {
                float ch = mContentScrollView.getContentViewHeight();
                float sh = mContentScrollView.getHeight();
                cx = ch - sh;
            }
            lastScrollY = this.getScrollY();
//			  mOnScrollingListener.onScrolling(lastScrollY = this.getScrollY(),cx);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
                handler.sendMessageDelayed(handler.obtainMessage(), 10);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}
