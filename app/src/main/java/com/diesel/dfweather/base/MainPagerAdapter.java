package com.diesel.dfweather.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Comments：
 *
 * @author Diesel
 *
 *         Time: 2016/8/16
 *
 *         Modified By:
 *         Modified Date:
 *         Why & What is modified:
 * @version 5.0.0
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(ArrayList<Fragment> mList) {
        if (mList != null) {
            fragmentList = mList;
        }
    }

    public ArrayList<Fragment> getList() {
        return fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return getFragment(position);
    }

    public Fragment getFragment(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
