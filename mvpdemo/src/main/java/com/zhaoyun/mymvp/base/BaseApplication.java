package com.zhaoyun.mymvp.base;

import android.app.Application;

/**
 * Created by zhaoyun on 17-3-1.
 */

public class BaseApplication extends Application {
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized BaseApplication getInstance() {
        return mInstance;
    }
}