package com.jbtm.parentschool.border;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

public class MyBorderView<X extends View> implements
        ViewTreeObserver.OnGlobalFocusChangeListener {

    private MyControllerInterface mController;
    private X highLightView;    //边框
    private boolean isFirstTime = true;   //是否第一次

    public MyBorderView(Context context) {
        this(context, null, 0);
    }

    public MyBorderView(Context context, AttributeSet attrs, int defStyleAttr) {
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        mController = new MyBorderController();
        highLightView = (X) new View(context, attrs, defStyleAttr);
    }

    public void setBackgroundResource(int resId) {
        if (highLightView != null)
            highLightView.setBackgroundResource(resId);
    }

    @Override
    public void onGlobalFocusChanged(View oldFocus, View newFocus) {
        mController.onFocusChanged(oldFocus, newFocus);
    }

    public void attachTo(ViewGroup rv) {
        if (isFirstTime) {
            isFirstTime = false;
            ViewTreeObserver viewTreeObserver = rv.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalFocusChangeListener(this);
            }
        }
        mController.onAttach(highLightView, rv);
    }
}
