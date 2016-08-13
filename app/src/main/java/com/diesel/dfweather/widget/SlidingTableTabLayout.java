package com.diesel.dfweather.widget;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Comments：
 * 可滑动Table选项卡，底部带可滑动分隔线
 *
 * @author Diesel
 *
 *         Time: 2016/8/10
 *
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class SlidingTableTabLayout extends LinearLayout {

    private static final int TAB_VIEW_PADDING_DIP = 5;

    private static final int TAB_VIEW_TEXT_SIZE_DIP = 14;

    private SlidingTableTabStrip mTabStrip;

    private ViewPager mViewPager;

    private ViewPager.OnPageChangeListener mViewPagerPageChangeListener;

    public SlidingTableTabLayout(Context context) {
        this(context, null);
    }

    public SlidingTableTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTableTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mTabStrip = new SlidingTableTabStrip(context);
        addView(mTabStrip, FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
    }

    public void setViewPager(ViewPager viewPager) {
        mTabStrip.removeAllViews();

        mViewPager = viewPager;
        if (null != mViewPager) {
            mViewPager.addOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        mViewPagerPageChangeListener = listener;
    }

    private void populateTabStrip() {
        final PagerAdapter adapter = mViewPager.getAdapter();
        final OnClickListener tabClickListener = new TabClickListener();

        int count = adapter.getCount();
        for (int i = 0; i < count; i++) {
            TextView tabView = createDefaultTabView(getContext(), count);
            tabView.setText(adapter.getPageTitle(i));
            tabView.setOnClickListener(tabClickListener);
            mTabStrip.addView(tabView);
        }
    }

    protected TextView createDefaultTabView(Context context, int count) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, TAB_VIEW_TEXT_SIZE_DIP);
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            // If we're running on Honeycomb or newer, then we can use the Theme's
            // selectableItemBackground to ensure that the View has a pressed state
            TypedValue outValue = new TypedValue();
            getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground,
                    outValue, true);
            textView.setBackgroundResource(outValue.resourceId);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // If we're running on ICS or newer, enable all-caps to match the Action Bar tab style
            textView.setAllCaps(true);
        }

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int padding = (int) (TAB_VIEW_PADDING_DIP * metrics.density);
        int widthPixels = metrics.widthPixels / count;
        textView.setPadding(padding, 0, padding, 0);
        textView.setLayoutParams(new LinearLayoutCompat.LayoutParams(widthPixels, LayoutParams.MATCH_PARENT));

        return textView;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mViewPager != null) {
            scrollToTab(mViewPager.getCurrentItem(), 0);
        }
    }

    private void scrollToTab(int tabIndex, int positionOffset) {
//        final int tabStripChildCount = mTabStrip.getChildCount();
//        if (tabStripChildCount == 0 || tabIndex < 0 || tabIndex >= tabStripChildCount) {
//            return;
//        }
//
//        View selectedChild = mTabStrip.getChildAt(tabIndex);
//        if (selectedChild != null) {
//            int targetScrollX = selectedChild.getLeft() + positionOffset;
////            if (tabIndex > 0 || positionOffset > 0) {
////                // If we're not at the first child and are mid-scroll, make sure we obey the offset
////                targetScrollX -= mTitleOffset;
////            }
//            scrollTo(targetScrollX, 0);
//        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        private int mScrollState;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount = mTabStrip.getChildCount();
            if ((tabStripChildCount == 0) || (position < 0) || (position >= tabStripChildCount)) {
                return;
            }

            mTabStrip.onViewPagerPageChanged(position, positionOffset);

            View selectedTitle = mTabStrip.getChildAt(position);
            int extraOffset = (selectedTitle != null)
                    ? (int) (positionOffset * selectedTitle.getWidth())
                    : 0;
            scrollToTab(position, extraOffset);

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                mTabStrip.onViewPagerPageChanged(position, 0f);
                scrollToTab(position, 0);
            }

            if (mViewPagerPageChangeListener != null) {
                mViewPagerPageChangeListener.onPageSelected(position);
            }
        }

    }

    private class TabClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            for (int i = 0; i < mTabStrip.getChildCount(); i++) {
                if (v == mTabStrip.getChildAt(i)) {
                    mViewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }
}
