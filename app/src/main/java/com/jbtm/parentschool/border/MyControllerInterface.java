package com.jbtm.parentschool.border;

import android.view.View;

/**
 * Created by lvhailing on 2018/11/26.
 */

public interface MyControllerInterface {
    void onFocusChanged(View oldFocus, View newFocus);

    void onAttach(View target, View attachView);
}
