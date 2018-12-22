package com.jbtm.parentschool;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * Created by lvhailing on 2018/12/14.
 */

public class MyApplication extends Application {
    public static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        initBugly();
    }


    private void initBugly() {
        CrashReport.initCrashReport(getApplicationContext(), "024cdf058a", false);
    }
}
