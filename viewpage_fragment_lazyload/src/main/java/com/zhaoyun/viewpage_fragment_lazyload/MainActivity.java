package com.zhaoyun.viewpage_fragment_lazyload;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;

public class MainActivity extends FragmentActivity{

    private ViewPager mViewPager;
    private SparseArray<Fragment> fragments = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragments.put(0,new FragmentA());
        fragments.put(1,new FragmentB());
        fragments.put(2,new FragmentC());
        fragments.put(3,new FragmentD());

        mViewPager = (ViewPager)findViewById(R.id.viewPager);
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        });
    }

    public void clickA(View v){
        //默认有滑动过程动画
        mViewPager.setCurrentItem(0);
    }
    public void clickB(View v){
        //设置true，有滑动动画
        mViewPager.setCurrentItem(1,true);
    }
    public void clickC(View v){
        //设置false，没有滑动动画，直接跳到C
        mViewPager.setCurrentItem(2,false);
    }
    public void clickD(View v){
        mViewPager.setCurrentItem(3);
    }

}
