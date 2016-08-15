package com.diesel.dfweather.widget;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.TextView;

import com.diesel.dfweather.R;

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

    private enum State {
        DONE, LOADING, RELEASE_To_REFRESH, PULL_To_REFRESH, REFRESHING
    }

    // 实际的padding的距离与界面上偏移距离的比例
    private static final int RATIO = 3;

    private ContentScrollView mScrollView;

    private LinearLayout mRefreshHeaderView;
    private ImageView arrowImageView;
    private ProgressBar progressBar;
    private TextView tipsTextview;
    private TextView lastUpdatedTextView;

    private FrameLayout titleLayout;
    private View mTitleLayout;

    private int headContentHeight;
    private int headPaddingTop;
    private int mTitleHeight;

    private boolean isRefreshable;
    private boolean canReturn;

    private State state;
    private Scroller mScroller;

    private RotateAnimation animation;
    private RotateAnimation reverseAnimation;

    private RefreshViewListener mRefreshListener;
    private HeaderViewPaddingChangeListener mListener;

    public TitleRefreshView(Context context, View titleLayout) {
        super(context);
        mTitleLayout = titleLayout;
        initView();
    }

    public TitleRefreshView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleRefreshView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        View view = inflate(getContext(), R.layout.elastic_title_view_layout, this);
        mRefreshHeaderView = (LinearLayout) view.findViewById(R.id.refresh_header_layout);
        arrowImageView = (ImageView) view.findViewById(R.id.head_arrowImageView);
        progressBar = (ProgressBar) view.findViewById(R.id.head_progressBar);
        tipsTextview = (TextView) view.findViewById(R.id.head_tipsTextView);
        lastUpdatedTextView = (TextView) view.findViewById(R.id.head_lastUpdatedTextView);
        titleLayout = (FrameLayout) view.findViewById(R.id.top_header_layout);

        measureView(mRefreshHeaderView);

        headContentHeight = mRefreshHeaderView.getMeasuredHeight();
        headPaddingTop = -1 * headContentHeight;
        mRefreshHeaderView.setPadding(0, headPaddingTop, 0, 0);
        mRefreshHeaderView.invalidate();

        setTitleLayout(mTitleLayout);

        animation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(250);
        animation.setFillAfter(true);

        reverseAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        reverseAnimation.setInterpolator(new LinearInterpolator());
        reverseAnimation.setDuration(200);
        reverseAnimation.setFillAfter(true);

        state = State.DONE;
        isRefreshable = true;
        canReturn = false;

        mScroller = new Scroller(getContext(), new DecelerateInterpolator());
    }

    private void measureView(View child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
    }

    private void setTitleLayout(View titleView) {
        titleLayout.addView(titleView);
        measureView(titleLayout);
        mTitleHeight = titleLayout.getMeasuredHeight();
    }

    public void setTitleLayout(@LayoutRes int titleResId) {
        View titleView = inflate(getContext(), titleResId, null);
        setTitleLayout(titleView);
    }

    public void setScrollView(ContentScrollView scrollView) {
        mScrollView = scrollView;
    }

    public void setHeaderViewPaddingChangeListener(HeaderViewPaddingChangeListener listener) {
        mListener = listener;
    }

    public void setRefreshListener(RefreshViewListener listener) {
        mRefreshListener = listener;
    }

    public int getTitleHeight() {
        return 0;
    }

    public TextView getRefreshTimeView() {
        return lastUpdatedTextView;
    }

    @Override
    public void fling(int velocityY) {
        super.fling(velocityY / 2);
    }

    private int touchY;
    private boolean isDown;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                touchY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (mScrollView == null) {
                    break;
                }
                if (isDown && mScrollView.getScrollY() == 0 && ev.getY() - touchY < 0) { // 当前只能是没有滚动状态才能下拉刷新
                    isRefreshable = false;
                }
                isDown = false;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                isRefreshable = true;
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            mRefreshHeaderView.setPadding(0, -mScroller.getCurrY(), 0, 0);
            if (mListener != null) {
                mListener.onPaddingChanged(headPaddingTop - mRefreshHeaderView.getPaddingTop());
            }
            postInvalidate();

            if(mScroller.getCurrY() == headContentHeight) {
                state = State.DONE;
                mRefreshHeaderView.setPadding(0, -1 * headContentHeight, 0, 0);
                if (mListener != null) {
                    mListener.onPaddingChanged(0);
                }
                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.ic_refresh_arrow_bottom);
                tipsTextview.setText("下拉刷新");
//				lastUpdatedTextView.setVisibility(View.VISIBLE);
                invalidate();
                scrollTo(0, 0);
            }
        }
    }

    private boolean isRecored;
    private int startY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isRefreshable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (getScrollY() == 0 && !isRecored) {
                        isRecored = true;
                        startY = (int) event.getY();
//					Log.i(TAG, "在down时候记录当前位置‘");
                    }
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_OUTSIDE:
                    if (state != State.REFRESHING && state != State.LOADING) {
                        if (state == State.DONE) {
                            // 什么都不做
                        }
                        if (state == State.PULL_To_REFRESH) {
                            state = State.DONE;
                            changeHeaderViewByState();
//						Log.i(TAG, "由下拉刷新状态，到done状态");
                        }
                        if (state == State.RELEASE_To_REFRESH) {
                            state = State.REFRESHING;
                            changeHeaderViewByState();
                            if (mRefreshListener != null) {
                                mRefreshListener.onRefresh();
                            } else {
                                onRefreshComplete();
//							Log.i(TAG, "未设置刷新listener");
                            }
//						Log.i(TAG, "由松开刷新状态，到done状态");
                        }
                    }
                    isRecored = false;
                    isBack = false;

                    break;
                case MotionEvent.ACTION_MOVE:
                    int tempY = (int) event.getY();
                    if (!isRecored && getScrollY() == 0) {
//					Log.i(TAG, "在move时候记录下位置");
                        isRecored = true;
                        startY = tempY;
                    }

                    if (state != State.REFRESHING && isRecored && state != State.LOADING) {
                        // 可以松手去刷新了
                        if (state == State.RELEASE_To_REFRESH) {
                            canReturn = true;

                            if (((tempY - startY) / RATIO < headContentHeight) && (tempY - startY) > 0) {
                                state = State.PULL_To_REFRESH;
                                changeHeaderViewByState();
//							Log.i(TAG, "由松开刷新状态转变到下拉刷新状态");
                            }
                            // 一下子推到顶了
                            else if (tempY - startY <= 0) {
                                state = State.DONE;
                                changeHeaderViewByState();
//							Log.i(TAG, "由松开刷新状态转变到done状态");
                            } else {
                                // 不用进行特别的操作，只用更新paddingTop的值就行了
                            }
                        }
                        // 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
                        if (state == State.PULL_To_REFRESH) {
                            canReturn = true;

                            // 下拉到可以进入RELEASE_TO_REFRESH的状态
                            if ((tempY - startY) / RATIO >= headContentHeight) {
                                state = State.RELEASE_To_REFRESH;
                                isBack = true;
                                changeHeaderViewByState();
//							Log.i(TAG, "由done或者下拉刷新状态转变到松开刷新");
                            }
                            // 上推到顶了
                            else if (tempY - startY <= 0) {
                                state = State.DONE;
                                changeHeaderViewByState();
//							Log.i(TAG, "由DOne或者下拉刷新状态转变到done状态");
                            }
                        }

                        // done状态下
                        if (state == State.DONE) {
                            if (tempY - startY > 0) {
                                state = State.PULL_To_REFRESH;
                                changeHeaderViewByState();
                            }
                        }

                        // 更新headView的size
                        if (state == State.PULL_To_REFRESH) {
                            mRefreshHeaderView.setPadding(0, -1 * headContentHeight + (tempY - startY) / RATIO, 0, 0);
                            if (mListener != null) {
                                mListener.onPaddingChanged(headPaddingTop - mRefreshHeaderView.getPaddingTop());
                            }
                        }

                        // 更新mRefreshHeaderView的paddingTop
                        if (state == State.RELEASE_To_REFRESH) {
                            mRefreshHeaderView.setPadding(0, (tempY - startY) / RATIO - headContentHeight, 0, 0);
                            if (mListener != null) {
                                mListener.onPaddingChanged(headPaddingTop - mRefreshHeaderView.getPaddingTop());
                            }
                        }
                        if (canReturn) {
                            canReturn = false;
                            return true;
                        }
                    }
                    break;
            }
        }
        return false;
    }

    private boolean isBack;

    // 当状态改变时候，调用该方法，以更新界面
    private void changeHeaderViewByState() {
        switch (state) {
            case RELEASE_To_REFRESH:
                arrowImageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
//			lastUpdatedTextView.setVisibility(View.VISIBLE);

                arrowImageView.clearAnimation();
                arrowImageView.startAnimation(animation);

                tipsTextview.setText("松开刷新");

//			Log.i(TAG, "当前状态，松开刷新");
                break;
            case PULL_To_REFRESH:
                progressBar.setVisibility(View.GONE);
                tipsTextview.setVisibility(View.VISIBLE);
//			lastUpdatedTextView.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.VISIBLE);
                // 是由RELEASE_To_REFRESH状态转变来的
                if (isBack) {
                    isBack = false;
                    arrowImageView.clearAnimation();
                    arrowImageView.startAnimation(reverseAnimation);

                    tipsTextview.setText("下拉刷新");
                } else {
                    tipsTextview.setText("下拉刷新");
                }
//			Log.i(TAG, "当前状态，下拉刷新");
                break;

            case REFRESHING:

                mRefreshHeaderView.setPadding(0, 0, 0, 0);

                if (mListener != null) {
                    mListener.onPaddingChanged(headPaddingTop - 0);
                }

                progressBar.setVisibility(View.VISIBLE);
                arrowImageView.clearAnimation();
                arrowImageView.setVisibility(View.GONE);
                tipsTextview.setText("正在刷新...");
//			lastUpdatedTextView.setVisibility(View.VISIBLE);

//			Log.i(TAG, "当前状态,正在刷新...");
                break;
            case DONE:
                mRefreshHeaderView.setPadding(0, -1 * headContentHeight, 0, 0);

                if (mListener != null) {
                    mListener.onPaddingChanged(0);
                }

                progressBar.setVisibility(View.GONE);
                arrowImageView.clearAnimation();
                arrowImageView.setImageResource(R.drawable.ic_refresh_arrow_bottom);
                tipsTextview.setText("下拉刷新");
//			lastUpdatedTextView.setVisibility(View.VISIBLE);

//			Log.i(TAG, "当前状态，done");
                break;
        }
    }

    /**
     * 刷新完成
     */
    public void onRefreshComplete() {
        //if(headView.getPaddingTop() >= 0) {
        mScroller.abortAnimation();
        mScroller.startScroll(0, 0, 0, headContentHeight, 1000);
        invalidate();
        //}
    }

    /**
     * 把刷新状态改成正在刷新
     */
    public void onRefreshing() {
        if(state == State.DONE) {
            mScroller.abortAnimation();
            mScroller.startScroll(0, headContentHeight, 0, -headContentHeight, 300);
            state = State.REFRESHING;
            progressBar.setVisibility(View.VISIBLE);
            arrowImageView.clearAnimation();
            arrowImageView.setVisibility(View.GONE);
            tipsTextview.setText("正在刷新...");
//			lastUpdatedTextView.setVisibility(View.VISIBLE);
            invalidate();
        }
    }

    protected interface HeaderViewPaddingChangeListener {
        void onPaddingChanged(int offsetY);
    }

    /**
     * 刷新事件监听
     */
    public interface RefreshViewListener {
        void onRefresh();
    }
}
