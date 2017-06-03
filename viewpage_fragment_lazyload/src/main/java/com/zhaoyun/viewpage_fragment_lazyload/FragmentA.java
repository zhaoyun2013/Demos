package com.zhaoyun.viewpage_fragment_lazyload;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by zhaoyun on 17-3-31.
 */

public class FragmentA extends BaseFragment {

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_a,null);
        return v;
    }

    @Override
    public void initData() {
        Log.d(TAG, "initData: FragmentA");
    }

    @Override
    protected void onInvisible() {
        Log.d(TAG, "onInvisible: FragmentA");
    }
}
