package com.diesel.dfweather.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.diesel.dfweather.R;
import com.diesel.dfweather.util.ViewUtils;


/**
 * Copyright2012-2015  CST.All Rights Reserved
 *
 * Comments：头部导航栏
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
public class CommonHeaderView extends RelativeLayout {

    /**
     * 头部左侧显示类型 - 文本
     */
    private static final int LEFT_TYPE_TEXT = 0;

    /**
     * 头部左侧显示类型 - 文本+副文本
     */
    private static final int LEFT_TYPE_TEXT_AND_TEXT = 1;

    /**
     * 头部左侧显示类型 - 图标
     */
    private static final int LEFT_TYPE_ICON = 2;

    /**
     * 头部左侧显示类型 - 无左侧显示
     */
    private static final int LEFT_TYPE_NONE = 3;

    /**
     * 头部右侧显示类型 - 图标
     */
    private static final int RIGHT_TYPE_ICON = 0;

    /**
     * 头部右侧显示类型 - 图标+图标
     */
    private static final int RIGHT_TYPE_ICON_AND_ICON = 1;

    /**
     * 头部右侧显示类型 - 文本
     */
    private static final int RIGHT_TYPE_TEXT = 2;

    /**
     * 头部右侧显示类型 - 图标+小红点
     */
    private static final int RIGHT_TYPE_ICON_AND_DOT = 3;

    /**
     * 头部右侧显示类型 - 文本或者图标
     */
    private static final int RIGHT_TYPE_TEXT_OR_ICON = 4;

    /**
     * 头部右侧显示类型 - 无右侧显示
     */
    private static final int RIGHT_TYPE_NONE = 5;

    LinearLayout mHeaderLayout;

    LinearLayout mHeaderRightLayout;

    /**
     * 标题左侧图标
     */
    ImageButton mIbLeftIcon;

    TextView mTvLeftTitle;

    TextView mTvLeftSubTitle;

    TextView mHeaderCenterTitle;

    /**
     * 头部右侧图标-right
     */
    ImageButton mIbRightIcon;

    /**
     * 头部右侧图标-left
     */
    ImageButton mIbRightSubIcon;

    TextView mTvRightTitle;

    ImageView mIvRedDot;

    public CommonHeaderView(Context context) {
        this(context, null);
    }

    public CommonHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = LayoutInflater.from(context).inflate(R.layout.common_header_layout, this);
        mHeaderLayout = (LinearLayout) view.findViewById(R.id.header_center_title_layout);
        mIbLeftIcon = (ImageButton) view.findViewById(R.id.header_left_icon);
        mTvLeftTitle = (TextView) view.findViewById(R.id.header_left_title);
        mTvLeftSubTitle = (TextView) view.findViewById(R.id.header_left_sub_title);
        mHeaderCenterTitle = (TextView) view.findViewById(R.id.header_center_title);
        mHeaderRightLayout = (LinearLayout) view.findViewById(R.id.header_right_title_layout);
        mIbRightIcon = (ImageButton) view.findViewById(R.id.header_right_icon);
        mIbRightSubIcon = (ImageButton) view.findViewById(R.id.header_right_sub_icon);
        mTvRightTitle = (TextView) view.findViewById(R.id.header_right_title);
        mIvRedDot = (ImageView) view.findViewById(R.id.header_right_dot_icon);

        TypedArray ta = context
                .obtainStyledAttributes(attrs, R.styleable.CommonHeaderView, defStyleAttr, 0);
        String centerTitle = ta.getString(R.styleable.CommonHeaderView_center_title);
        int leftType = ta.getInt(R.styleable.CommonHeaderView_left_type, LEFT_TYPE_NONE);
        int rightType = ta.getInt(R.styleable.CommonHeaderView_right_type, RIGHT_TYPE_NONE);

        mHeaderCenterTitle.setText(centerTitle);
        switch (leftType) {
            case LEFT_TYPE_TEXT:
                ViewUtils.visible(mTvLeftTitle);
                ViewUtils.gone(mTvLeftSubTitle, mIbLeftIcon);
                break;
            case LEFT_TYPE_TEXT_AND_TEXT:
                ViewUtils.visible(mTvLeftTitle, mTvLeftSubTitle);
                ViewUtils.gone(mIbLeftIcon);
                break;
            case LEFT_TYPE_ICON:
                ViewUtils.visible(mIbLeftIcon);
                ViewUtils.gone(mTvLeftTitle, mTvLeftSubTitle);
                break;
            default:
                ViewUtils.gone(mTvLeftTitle, mTvLeftSubTitle, mIbLeftIcon);
                break;
        }

        switch (rightType) {
            case RIGHT_TYPE_ICON:
                ViewUtils.visible(mIbRightIcon);
                ViewUtils.gone(mIbRightSubIcon, mTvRightTitle, mIvRedDot);
                break;
            case RIGHT_TYPE_ICON_AND_ICON:
                ViewUtils.visible(mIbRightSubIcon, mIbRightIcon);
                ViewUtils.gone(mTvRightTitle, mIvRedDot);
                break;
            case RIGHT_TYPE_TEXT:
                ViewUtils.visible(mTvRightTitle);
                ViewUtils.gone(mIbRightIcon, mIbRightSubIcon, mIvRedDot);
                break;
            case RIGHT_TYPE_ICON_AND_DOT:
                ViewUtils.visible(mIbRightIcon, mIvRedDot);
                ViewUtils.gone(mIbRightSubIcon, mTvRightTitle);
                break;
            case RIGHT_TYPE_TEXT_OR_ICON:
                ViewUtils.visible(mIbRightIcon, mTvRightTitle);
                ViewUtils.gone(mIbRightSubIcon, mIvRedDot);
                break;
            default:
                ViewUtils.gone(mIbRightIcon, mIbRightSubIcon, mTvRightTitle, mIvRedDot);
                break;
        }
        ta.recycle();
    }

    /**
     * 获取标题名称
     */
    public String getHeaderTitle() {
        return mHeaderCenterTitle.getText().toString();
    }

    /**
     * 添加右侧view
     *
     * @param view 标题位置添加的view (如：SlidingTabLayout)
     */
    public void addHeaderRightLayoutView(View view) {
        mHeaderRightLayout.addView(view);
    }

    /**
     * 添加标题view
     *
     * @param view 标题位置添加的view (如：SlidingTabLayout)
     */
    public void addHeaderLayoutView(View view) {
        mHeaderLayout.addView(view);
    }

    /**
     * 设置头部左边文字按钮
     */
    public void setHeaderLeftTitle(String leftTitle) {
        mTvLeftTitle.setText(leftTitle);
    }

//    /**
//     * 设置头部左边文字按钮
//     *
//     * @param leftTitle 左侧显示文字 (不显示 < 图标)
//     */
//    public void setHeaderLeftTitle(String leftTitle) {
//        mTvLeftTitle.setText(leftTitle);
//        mTvLeftTitle.setCompoundDrawables(null, null, null, null);
//    }

    public void setHeaderLeftSubTitle(@StringRes String title) {
        mTvLeftSubTitle.setText(title);
    }

    /**
     * 设置头部左边图片
     */
    public void setHeaderLeftIcon(@DrawableRes int imageSrcId) {
        mIbLeftIcon.setImageResource(imageSrcId);
    }

    /**
     * 设置页面右边图片(右边图标中的右边一个)
     */
    public void setHeaderRightIcon(@DrawableRes int imageSrcId) {
        mIbRightIcon.setImageResource(imageSrcId);
    }

    /**
     * 设置页面右边图片(右边图标中的左边一个)
     */
    public void setHeaderRightSubIcon(@DrawableRes int imageSrcId) {
        mIbRightSubIcon.setImageResource(imageSrcId);
    }

    /**
     * 设置头部右边文字按钮
     */
    public void setHeaderRightTitle(@StringRes String rightText) {
        mTvRightTitle.setText(rightText);
    }

    /**
     * 设置头部title
     *
     * @param title 标题
     */
    public void setHeaderTitle(@StringRes String title) {
        mHeaderCenterTitle.setText(title);
    }

    /**
     * 隐藏头部右边ICON
     */
    public void hiddenHeaderRightIcon() {
        ViewUtils.gone(mIbRightIcon);
    }

    /**
     * 隐藏头部右边文字
     */
    public void hiddenHeaderRightTitle() {
        ViewUtils.gone(mTvRightTitle);
    }
}
