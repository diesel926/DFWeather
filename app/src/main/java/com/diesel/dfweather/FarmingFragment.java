package com.diesel.dfweather;

import android.support.v4.view.PagerAdapter;
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

    /**
     * 缓存城市天气页面View，其中key值为城市名
     */
    private Map<String, RefreshScrollView> mViewMap = new HashMap<>();

    public static FarmingFragment newInstance() {
        return new FarmingFragment();
    }

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
            return 0;
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
            if (mViewMap.containsKey(cityName)) {
                pageView = mViewMap.get(cityName);
            } else {
                pageView = createPageView();
                mViewMap.put(cityName, pageView);
            }
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
