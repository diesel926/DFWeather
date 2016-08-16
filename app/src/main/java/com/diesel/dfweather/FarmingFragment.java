package com.diesel.dfweather;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.diesel.dfweather.base.BaseFragment;
import com.diesel.dfweather.constant.DFConsts;
import com.diesel.dfweather.widget.ContentScrollView;
import com.diesel.dfweather.widget.RefreshScrollView;
import com.diesel.dfweather.widget.TitleRefreshView;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Comments：农事
 *
 * @author Diesel
 *         Time: 2016/8/13
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class FarmingFragment extends BaseFragment {

    @BindView(R.id.weather_pager)
    ViewPager mWeatherPager;

    /**
     * 缓存城市天气页面View，其中key值为城市名
     */
    private Map<String, RefreshScrollView> mViewMap = new HashMap<>();

    private WeatherPagerAdapter mAdapter;

    public static FarmingFragment newInstance() {
        return new FarmingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_farming, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new WeatherPagerAdapter();
        mWeatherPager.setAdapter(mAdapter);
        mWeatherPager.addOnPageChangeListener(mPageChangeListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWeatherPager.removeOnPageChangeListener(mPageChangeListener);
    }

    private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private RefreshScrollView createPageView() {
        LayoutInflater mInflater = mActivity.getLayoutInflater();
        final View titleView = mInflater.inflate(R.layout.weather_page_title_layout, null);
        final View showView = mInflater.inflate(R.layout.weather_page_show_layout, null);
        final View hideView = mInflater.inflate(R.layout.weather_page_hide_layout, null);
        final RefreshScrollView scrollView = new RefreshScrollView(mActivity, titleView, showView,
                hideView);
        scrollView.setTag(null); // FIXME: 正式传入页面数据时绑定当前城市名
        scrollView.setOnScrollingListener(new RefreshScrollView.OnScrollingListener() {
            @Override
            public void onScrolling(int scrollY, float cx) {

            }
        });
        scrollView.setRefreshListener(new TitleRefreshView.RefreshViewListener() {
            @Override
            public void onRefresh() {

            }
        });

        return scrollView;
    }

    private class WeatherPagerAdapter extends PagerAdapter {

        public WeatherPagerAdapter() {
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            final String cityName = null;
            RefreshScrollView pageView = null;
//            if (mViewMap.containsKey(cityName)) {
//                pageView = mViewMap.get(cityName);
//            } else {
//                pageView = createPageView();
//                mViewMap.put(cityName, pageView);
//            }
            // FIXME: 正式接入数据时删除
            pageView = createPageView();
            container.addView(pageView);
            final ContentScrollView contentScrollView = pageView.getScrollView();
            contentScrollView.post(new Runnable() {
                @Override
                public void run() {
                    contentScrollView.scrollTo(0, DFConsts.sScrollY);
                }
            });
            return pageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


    }

}
