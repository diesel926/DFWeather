package com.diesel.dfweather.map;

import android.content.Context;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * @author Diesel
 *
 *         Time: 2016/8/13
 *
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 1.0.0
 */
public class BMapLocationClient {

    private LocationClient mLocationClient;

    private BMapLocationListener mLocationListener;

    public BMapLocationClient(Context context) {
        mLocationClient = new LocationClient(context.getApplicationContext());
        mLocationListener = new BMapLocationListener();

        final LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(10 * 1000); // 默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        mLocationClient.setLocOption(option);
    }

    public void startLocation() {
        mLocationClient.registerLocationListener(mLocationListener);
        mLocationClient.start();
    }

    public void stopLocation() {
        mLocationClient.unRegisterLocationListener(mLocationListener);
        mLocationClient.stop();
    }
}
