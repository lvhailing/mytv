package com.jbtm.parentschool.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jbtm.parentschool.R;
import com.jbtm.parentschool.activity.CourseDetailActivity;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private String[] mData;
    private Context mContext;
    private float scaleValue = 1.25f;
    private int scaleTime = 200;

    public HomeAdapter(Context context, String[] data) {
        super();
        mContext = context;
        mData = data;
    }

    public void setData(String[] data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
//        viewHolder.mTextView.setText(mData[i]);

    }

    @Override
    public int getItemCount() {
        return mData.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        private ImageView v_bg;    //边框

        public ViewHolder(View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv);
            v_bg = itemView.findViewById(R.id.v_bg);

            listenViewFocus(itemView, v_bg);
            listenViewClick(itemView);
        }
    }

    private void listenViewClick(View itemView) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseDetailActivity.startActivity(mContext);
            }
        });
    }

    private void listenViewFocus(View itemView, final ImageView v_bg) {
        itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {
                //获取焦点时变化
                if (hasFocus) {
                    ViewCompat.animate(v)
                            .scaleX(scaleValue)
                            .scaleY(scaleValue)
                            .setDuration(scaleTime)
                            .setListener(new ViewPropertyAnimatorListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationStart(View view) {
                                    v_bg.setVisibility(View.VISIBLE);
                                    view.bringToFront();
                                    view.setElevation(100f);   //防止被其他view z轴方向覆盖
                                }

                                @Override
                                public void onAnimationEnd(View view) {
                                }

                                @Override
                                public void onAnimationCancel(View view) {
                                }
                            })
                            .start();
                } else {
                    ViewCompat.animate(v)
                            .scaleX(1)
                            .scaleY(1)
                            .setListener(new ViewPropertyAnimatorListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationStart(View view) {
                                    v_bg.setVisibility(View.GONE);
                                    view.setElevation(0f); //防止z轴方向，覆盖其他view
                                }

                                @Override
                                public void onAnimationEnd(View view) {
                                }

                                @Override
                                public void onAnimationCancel(View view) {
                                }
                            })
                            .start();
                }
            }
        });
    }
}
