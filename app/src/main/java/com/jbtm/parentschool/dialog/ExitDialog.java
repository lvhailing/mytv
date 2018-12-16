package com.jbtm.parentschool.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jbtm.parentschool.R;

public class ExitDialog extends Dialog {
    private Context context;
    private MyClickListener listener;

    public interface MyClickListener {
        void MoreTime();

        void Exit();
    }

    public ExitDialog(Context context) {
        super(context, R.style.MyDialogStyle);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    public void init() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_exit, null);
        setContentView(view);

        TextView tv_more_time = view.findViewById(R.id.tv_more_time);
        TextView tv_exit = view.findViewById(R.id.tv_exit);

        tv_more_time.setOnClickListener(new clickListener());
        tv_exit.setOnClickListener(new clickListener());

        listenFocus(tv_more_time);
        listenFocus(tv_exit);

        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.65);
        lp.height = (int) (d.heightPixels * 0.55);
        dialogWindow.setAttributes(lp);

        //默认退出按钮获得焦点 注意：动态获取焦点会走OnFocusChangeListener，xml里获取焦点不会
        tv_exit.requestFocus();
    }

    public void setOnMyClickListener(MyClickListener myClickListener) {
        this.listener = myClickListener;
    }

    private class clickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_more_time:
                    listener.MoreTime();
                    break;
                case R.id.tv_exit:
                    listener.Exit();
                    break;
            }
        }
    }

    private void listenFocus(View view) {
        view.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                Log.i("aaa", "llll " + hasFocus);
                //获取焦点时变化
                if (hasFocus) {
                    ViewCompat.animate(v)
                            .scaleX(1.1f)
                            .setDuration(200)
                            .start();
                } else {
                    ViewCompat.animate(v)
                            .scaleX(1)
                            .start();
                }
            }
        });
    }
}
