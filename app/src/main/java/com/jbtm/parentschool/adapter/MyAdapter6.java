package com.jbtm.parentschool.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jbtm.parentschool.R;

public class MyAdapter6 extends RecyclerView.Adapter<MyAdapter6.ViewHolder> {
    private String[] mData;
    private Context mContext;
    private float scaleValue = 1.2f;
    private int scaleTime = 200;

    public MyAdapter6(Context context, String[] data) {
        super();
        mContext = context;
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.rv_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
//        viewHolder.mTextView.setText(mData[i]);
    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    private boolean hadAdded = false;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        private ImageView v_bg;    //边框

        public ViewHolder(View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.iv);
            v_bg = itemView.findViewById(R.id.v_bg);

            itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(final View v, boolean hasFocus) {
                    //获取焦点时变化
                    if (hasFocus) {
                        Log.i("aaa", "hasFocus " + hasFocus + " , view " + v);

                        ViewCompat.animate(v)
                                .scaleX(scaleValue)
                                .scaleY(scaleValue)
                                .setDuration(scaleTime)
                                .setListener(new ViewPropertyAnimatorListener() {
                                    @Override
                                    public void onAnimationStart(View view) {
                                    }

                                    @Override
                                    public void onAnimationEnd(View view) {
                                        v_bg.setVisibility(View.VISIBLE);
                                        v.bringToFront();
                                    }

                                    @Override
                                    public void onAnimationCancel(View view) {
                                    }
                                })
                                .start();
                    } else {
                        Log.i("aaa", "hasFocus " + hasFocus + " , view " + v);
                        ViewCompat.animate(v)
                                .scaleX(1)
                                .scaleY(1)
                                .start();
                        v_bg.setBackgroundResource(R.color.transparent);
                    }
                }
            });
        }
    }

    public void setData(String[] data) {
        this.mData = data;
    }
}
