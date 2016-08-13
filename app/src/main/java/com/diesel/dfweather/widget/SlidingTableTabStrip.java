package com.diesel.dfweather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Comments：
 * 可滑动Table选项卡控件，与{@link SlidingTableTabLayout}联合使用，处理tab底部分隔线的滑动等
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
public class SlidingTableTabStrip extends LinearLayout {

    private static final float TEXT_AND_DIVIDER_SPACE_DP = 10; // 文本与分隔线之间的间距

    private static final float DIVIDER_LINE_HEIGHT_DP = 1.5f; // 分隔线的高度

    private static final float BOTTOM_LINE_HEIGHT_DP = .5f; // 底部横线的高度

    private static final float TAB_DIVIDER_LINE_WIDTH_DP = .5f; // tab间分隔线的宽度

    private static final float TAB_DIVIDER_LINE_HEIGHT_DP = 22f; // tab间分隔线的高度

    private static final int DIVIDER_LINE_COLOR = 0xFFFFAE00; // 分隔线默认颜色值

    private static final int BOTTOM_LINE_COLOR = 0xFFDDDDDD; // 底部横线默认颜色值

    private static final int TAB_DIVIDER_LINE_COLOR = 0xFFEAEAEA; // tab间分隔线默认颜色值

    private static final int TAB_TITLE_TEXT_COLOR_NORMAL = 0xFF666666; // tab标题文本默认颜色值

    private static final int TAB_TITLE_TEXT_COLOR_SELECT = 0xFFFFAE00; // tab标题文本默认选中颜色值

    private int mTextDividerSpace;

    private int mDividerLineHeight, mBottomLineHeight, mTabDividerLineWidth, mTabDividerLineHeight;

    private int mSelectedPosition;

    private float mSelectionOffset;

    private Paint mDividerLinePaint, mBottomLinePaint, mTabDividerLinePaint;

    public SlidingTableTabStrip(Context context) {
        this(context, null);
    }

    public SlidingTableTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTableTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);

        mTextDividerSpace = dip2px(context, TEXT_AND_DIVIDER_SPACE_DP);
        mDividerLineHeight = dip2px(context, DIVIDER_LINE_HEIGHT_DP);
        mBottomLineHeight = dip2px(context, BOTTOM_LINE_HEIGHT_DP);
        mTabDividerLineWidth = dip2px(context, TAB_DIVIDER_LINE_WIDTH_DP);
        mTabDividerLineHeight = dip2px(context, TAB_DIVIDER_LINE_HEIGHT_DP);
        Log.v("SlidingTabView",
                "constructor() mTextDividerSpace=" + mTextDividerSpace + "; mDividerLineHeight="
                        + mDividerLineHeight + "; mBottomLineHeight=" + mBottomLineHeight
                        + "; mTabDividerLineWidth=" + mTabDividerLineWidth
                        + "; mTabDividerLineHeight=" + mTabDividerLineHeight);

        mDividerLinePaint = new Paint();
        mDividerLinePaint.setColor(DIVIDER_LINE_COLOR);

        mBottomLinePaint = new Paint();
        mBottomLinePaint.setColor(BOTTOM_LINE_COLOR);

        mTabDividerLinePaint = new Paint();
        mTabDividerLinePaint.setColor(TAB_DIVIDER_LINE_COLOR);
    }

    public void onViewPagerPageChanged(int position, float positionOffset) {
        mSelectedPosition = position;
        mSelectionOffset = positionOffset;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int height = getHeight();
        int childCount = getChildCount();

        // Thin underline along the entire bottom edge
        canvas.drawRect(0, height - mBottomLineHeight, getWidth(), height, mBottomLinePaint);

        // Thick colored underline below the current selection
        if (childCount > 0) {
            View selectedTabView = getChildAt(mSelectedPosition);
            int left = selectedTabView.getLeft();
            int right = selectedTabView.getRight();
            int width = selectedTabView.getWidth();
            int textWidth;
            int dividerPaddingLeft; // 分割线距子控件左边距距离
            if (mSelectionOffset > 0f && mSelectedPosition < (childCount - 1)) {
                View nextTabView = getChildAt(mSelectedPosition + 1);
                left = (int) (mSelectionOffset * nextTabView.getLeft()
                        + (1 - mSelectionOffset) * left);
                right = (int) (mSelectionOffset * nextTabView.getRight()
                        + (1 - mSelectionOffset) * right);
            }
            if (selectedTabView instanceof TextView) {
                textWidth = getTextWidth(((TextView) selectedTabView));
            } else {
                textWidth = width - selectedTabView.getPaddingLeft() - getPaddingRight();
            }
            dividerPaddingLeft = (width - textWidth - mTextDividerSpace) / 2;
            dividerPaddingLeft = dividerPaddingLeft < 0 ? 0 : dividerPaddingLeft;
            left += dividerPaddingLeft;
            right -= dividerPaddingLeft;
            right -= mTabDividerLineWidth * 2;
            Log.v("SlidingTabView",
                    "onDraw() width(" + width + "), textWidth(" + textWidth + "), left(" + left
                            + "), top(" + (height - mDividerLineHeight) + "), right(" + right
                            + "), bottom(" + height + ")");

            canvas.drawRect(left, height - mDividerLineHeight, right, height, mDividerLinePaint);
        }

        // Vertical separators between the titles
        int separatorTop = (height - mTabDividerLineHeight) / 2;
        for (int i = 0; i < childCount - 1; i++) {
            View child = getChildAt(i);
            canvas.drawRect(child.getRight(), separatorTop, child.getRight() + mTabDividerLineWidth,
                    separatorTop + mTabDividerLineHeight, mTabDividerLinePaint);
        }

        // change selected tab text's color
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child instanceof TextView) {
                ((TextView) child).setTextColor(i == mSelectedPosition ? TAB_TITLE_TEXT_COLOR_SELECT
                        : TAB_TITLE_TEXT_COLOR_NORMAL);
            }
        }
    }

    private int getTextWidth(TextView textView) {
        Paint paint = textView.getPaint();
        return (int) paint.measureText(textView.getText().toString());
    }

    private int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
