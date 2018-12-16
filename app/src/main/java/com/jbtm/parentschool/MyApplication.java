package com.jbtm.parentschool;

import android.app.Application;

/**
 * Created by lvhailing on 2018/12/14.
 */

public class MyApplication extends Application {
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }
}
