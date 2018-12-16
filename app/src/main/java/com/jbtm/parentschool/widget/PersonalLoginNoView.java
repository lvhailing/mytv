package com.jbtm.parentschool.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.utils.ToastUtil;
import com.jbtm.parentschool.utils.Util;

import java.util.ArrayList;
import java.util.List;


public class PersonalLoginNoView extends RelativeLayout {
    private EditText et_phone;
    private EditText et_code;
    private TextView tv_get_code;
    private Button btn_sure;

    private Context mContext;

    public PersonalLoginNoView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public PersonalLoginNoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public void setData(String phone, String version) {
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.view_login_no, this);

        et_phone = (EditText) view.findViewById(R.id.et_phone);
        et_code = (EditText) view.findViewById(R.id.et_verification_code);
        tv_get_code = (TextView) view.findViewById(R.id.tv_get_code);
        btn_sure = (Button) view.findViewById(R.id.btn_sure);

        //验证码焦点动画
        tv_get_code.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    tv_get_code.setTextColor(mContext.getResources().getColor(R.color.textColorOrange));
                    startAnim(tv_get_code);
                    Log.i("aaa", "tv_get_code anim start");
                } else {
                    tv_get_code.setTextColor(mContext.getResources().getColor(R.color.textColor));
                    stopAnim(tv_get_code);
                    Log.i("aaa", "tv_get_code anim stop");
                }
            }
        });

        //验证码发送
        tv_get_code.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_phone.getText())) {
                    ToastUtil.showCustom("请输入手机号");
                    return;
                }
                if (!Util.isPhoneNum(et_phone.getText().toString())) {
                    ToastUtil.showCustom("请输入正确的手机号");
                    return;
                }
                CountDownTimer timer = new CountDownTimer(10_000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        tv_get_code.setEnabled(false);
                        tv_get_code.setText(millisUntilFinished / 1000 + "秒");
                    }

                    @Override
                    public void onFinish() {
                        tv_get_code.setEnabled(true);
                        tv_get_code.setText("获取验证码");
                    }
                }.start();
            }
        });

        //确认
        btn_sure.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_phone.getText())) {
                    ToastUtil.showCustom("请输入手机号");
                    return;
                }
                if (!Util.isPhoneNum(et_phone.getText().toString())) {
                    ToastUtil.showCustom("请输入正确的手机号");
                    return;
                }
                if (TextUtils.isEmpty(et_phone.getText().toString().trim())) {
                    ToastUtil.showCustom("请输入验证码");
                    return;
                }
                ToastUtil.showCustom("请等待...");
            }
        });
    }

    private void startAnim(View view) {
        List<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleX = new ObjectAnimator().ofFloat(view, "scaleX", 1.0f, 1.1f);
        ObjectAnimator scaleY = new ObjectAnimator().ofFloat(view, "scaleY", 1.0f, 1.1f);
        animatorList.add(scaleX);
        animatorList.add(scaleY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator(1));
        animatorSet.setDuration(200);
        animatorSet.playTogether(animatorList);
        animatorSet.start();
    }

    private void stopAnim(View view) {
        List<Animator> animatorList = new ArrayList<Animator>();
        ObjectAnimator scaleBackX = new ObjectAnimator().ofFloat(view, "scaleX", 1.1f, 1.0f);
        ObjectAnimator scaleBackY = new ObjectAnimator().ofFloat(view, "scaleY", 1.1f, 1.0f);
        animatorList.add(scaleBackX);
        animatorList.add(scaleBackY);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator(1));
        animatorSet.setDuration(200);
        animatorSet.playTogether(animatorList);
        animatorSet.start();
    }
}
