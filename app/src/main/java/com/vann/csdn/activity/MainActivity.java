package com.vann.csdn.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.vann.csdn.R;
import com.vann.csdn.fragment.MainFragment;

public class MainActivity extends BaseActivity {

    private TabLayout mTab;
    private ViewPager mViewpager;
    private FragmentPagerAdapter mAdapter;
    private String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTab = (TabLayout) findViewById(R.id.id_tablayout);
        mViewpager = (ViewPager) findViewById(R.id.id_viewpager);
//        mViewpager.setOffscreenPageLimit(4);
        initData();
    }

    private void initData() {
        titles = new String[]{getResources().getString(R.string.yejie), getResources().getString(R.string.yidong)
                , getResources().getString(R.string.yunjisuan), getResources().getString(R.string.yanfa)};
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return MainFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position % titles.length];
            }
        };
        mViewpager.setAdapter(mAdapter);
        mTab.setupWithViewPager(mViewpager);
    }

}
