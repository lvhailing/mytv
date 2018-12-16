package com.jbtm.parentschool.widget;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.models.PayModel;
import com.jbtm.parentschool.utils.ToastUtil;


public class LoginYesView extends RelativeLayout implements View.OnClickListener {
    private TextView tv_my_phone;
    private TextView tv_version;
    private Button btn_login_out;
    private Button btn_check_update;
    private Context mContext;

    public LoginYesView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public LoginYesView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.view_login_yes, this);

        tv_version = view.findViewById(R.id.tv_version);
        tv_my_phone = view.findViewById(R.id.tv_my_phone);
        btn_login_out = view.findViewById(R.id.btn_login_out);
        btn_check_update = view.findViewById(R.id.btn_check_update);

        btn_login_out.setOnClickListener(this);
        btn_check_update.setOnClickListener(this);
    }

    public void setData(String phone, String version) {
        //手机号
        tv_my_phone.setText("我的手机号:" + phone);

        //最新版本
        tv_version.setText("已是最新版本:" + version);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_out:    //退出登录
                ToastUtil.showToast("退出登录");
                break;
            case R.id.btn_check_update:    //版本更新
                ToastUtil.showToast("版本更新");
                break;
        }
    }
}
