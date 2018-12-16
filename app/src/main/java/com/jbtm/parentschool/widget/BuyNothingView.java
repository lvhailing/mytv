package com.jbtm.parentschool.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.models.PayModel;


public class BuyNothingView extends LinearLayout {
    private Context mContext;

    public BuyNothingView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public BuyNothingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View.inflate(mContext, R.layout.view_buy_nothing, this);
    }
}
