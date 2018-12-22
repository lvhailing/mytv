package com.jbtm.parentschool.utils;

import android.os.Build;
import android.view.View;

/**
 * Created by lvhailing on 2018/12/22.
 */

public class UIUtil {

    //防止被其他view z轴方向覆盖
    public static void bringToFront(View view) {
        //5.0以下系统会崩溃
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            view.bringToFront();
            view.setElevation(100f);   //防止被其他view z轴方向覆盖
        }
    }

    //防止z轴方向，覆盖其他view
    public static void bringToBackground(View view) {
        //5.0以下系统会崩溃
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            view.setElevation(0f);   //防止z轴方向，覆盖其他view
        }
    }
}
