package com.zhaoyun.liteormdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhaoyun on 17-3-28.
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
