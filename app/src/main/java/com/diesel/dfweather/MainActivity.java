package com.diesel.dfweather;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.diesel.dfweather.base.MainPagerAdapter;
import com.diesel.dfweather.widget.NoScrollViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_pager)
    NoScrollViewPager mMainPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(FarmingFragment.newInstance());
        fragments.add(DepthServiceFragment.newInstance());
        fragments.add(UserInfoFragment.newInstance());
        fragments.add(UserInfoFragment.newInstance());
        MainPagerAdapter adapter = new MainPagerAdapter(getSupportFragmentManager());
        adapter.setList(fragments);
        mMainPager.setAdapter(adapter);
        mMainPager.setOffscreenPageLimit(4);
        mMainPager.setCurrentItem(0, false);
    }
}
