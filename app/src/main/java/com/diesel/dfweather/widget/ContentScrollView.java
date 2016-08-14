package com.diesel.dfweather.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.diesel.dfweather.R;
import com.diesel.dfweather.util.DimensionUtils;

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
public class ContentScrollView extends ScrollView
        implements TitleRefreshView.HeaderViewPaddingChangeListener {

    private View mScrollView;

    private FrameLayout mShowContentLayout;

    private FrameLayout mHideContentLayout;

    private TitleRefreshView mTitleRefreshView;

    private View mShowContent;

    private View mHideContent;

    /**
     * mScrollView相对于顶部的高度
     */
    private int mTopMargin;

    private boolean mIsDispatchTouch;

    public ContentScrollView(Context context) {
        this(context, null);
    }

    public ContentScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ContentScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public ContentScrollView(Context context, TitleRefreshView titleRefreshView, View showContent,
            View hideContent) {
        super(context);
        mTitleRefreshView = titleRefreshView;
        mShowContent = showContent;
        mHideContent = hideContent;
        initView();
        titleRefreshView.setScrollView(this);
        titleRefreshView.setHeaderViewPaddingChangeListener(this);
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.elastic_scroll_view_layout, this);
        mScrollView = view.findViewById(R.id.scroll_layout);
        mShowContentLayout = (FrameLayout) view.findViewById(R.id.show_content_layout);
        mHideContentLayout = (FrameLayout) view.findViewById(R.id.hide_content_layout);

        setContentView(mShowContent, mHideContent);

        measureScrollView();

        calcTopMargin();

        setScrollViewTopMargin();

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    public void setContentView(@LayoutRes int showResId, @LayoutRes int hideResId) {
        View showContent = inflate(getContext(), showResId, null);
        View hideContent = inflate(getContext(), hideResId, null);
        setContentView(showContent, hideContent);
    }

    private void setContentView(View showContent, View hideContent) {
        mShowContentLayout.addView(mShowContent);
        mHideContentLayout.addView(mHideContent);
    }

    private void measureScrollView() {
        ViewGroup.LayoutParams lp = mScrollView.getLayoutParams();
        if (null == lp) {
            lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int widthSpec = ViewGroup.getChildMeasureSpec(0, 0, lp.width);
        int heightSpec = MeasureSpec.makeMeasureSpec(lp.height,
                lp.height > 0 ? MeasureSpec.EXACTLY : MeasureSpec.UNSPECIFIED);
        mScrollView.measure(widthSpec, heightSpec);
    }

    private void calcTopMargin() {
        Rect outRect = new Rect();
        getWindowVisibleDisplayFrame(outRect); // 获取当前屏幕大小
        int statusBarHeight = DimensionUtils.getStatusBarHeight(getContext());
        mTopMargin = outRect.bottom - mTitleRefreshView.getTitleHeight() - statusBarHeight;
    }

    private void setScrollViewTopMargin() {
        if (mHideContentLayout.getVisibility() == GONE) {
            FrameLayout.LayoutParams lp = (LayoutParams) mShowContent.getLayoutParams();
            if (null == lp) {
                lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            }
            lp.height = mTopMargin;
            mShowContent.setLayoutParams(lp);
            mHideContentLayout.setVisibility(VISIBLE);
            mScrollView.invalidate();
        }
    }

    public int getContentViewHeight() {
        return mScrollView.getMeasuredHeight();
    }

    @Override
    public void fling(int velocityY) {
        super.fling((int) (velocityY / 1.5));
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (getScaleY() == 0) { // 当没有滚动时才能触发下拉刷新
                mIsDispatchTouch = true;
            }
        }

        if (mIsDispatchTouch) { // 传递touch事件到TitleRefreshView布局中达到触摸屏幕下拉刷新动作
            if (null != mTitleRefreshView) {
                MotionEvent newEvent = MotionEvent.obtain(ev);
                newEvent.setLocation(-1, ev.getY()); // +1000避免show.layout和title.layout中的点击事件冲突
                mTitleRefreshView.dispatchTouchEvent(newEvent);
                newEvent.recycle();
            }
        }

        if (ev.getAction() == MotionEvent.ACTION_MOVE) { // 改变背景透明度
            changeBackgroundAlpha(getScrollY());
        }

        if (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_OUTSIDE
                || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            mIsDispatchTouch = false;
        }

        return super.dispatchTouchEvent(ev);
    }

    /**
     * 改变背景透明度
     */
    public void changeBackgroundAlpha(int scrollY) {
        scrollY = scrollY < 0 ? 0 : scrollY;
        scrollY = scrollY > 100 ? 100 : scrollY;
        String alpha = Integer.toHexString(scrollY * 2);
        alpha = alpha.length() == 2 ? alpha : "0" + alpha;
        //mBgAlphaView.setBackgroundColor(Color.parseColor("#"+alpha+"000000"));
    }

    @Override
    public void onPaddingChanged(int offsetY) {

    }
}
