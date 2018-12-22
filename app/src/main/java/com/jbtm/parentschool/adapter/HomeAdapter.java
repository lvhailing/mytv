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

import com.bumptech.glide.Glide;
import com.jbtm.parentschool.R;
import com.jbtm.parentschool.activity.CourseDetailActivity;
import com.jbtm.parentschool.models.HomeCourseModel;
import com.jbtm.parentschool.utils.UIUtil;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Context mContext;
    private List<HomeCourseModel> courseList;
    private int scaleTime = 200;

    public HomeAdapter(Context context, List<HomeCourseModel> courseList) {
        super();
        mContext = context;
        this.courseList = courseList;
    }

    public void setData(List<HomeCourseModel> courseList) {
        this.courseList = courseList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_home, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Glide.with(mContext)
                .load(courseList.get(i).photo)
                .into(viewHolder.iv);

        listenViewFocus(viewHolder.itemView, viewHolder.v_bg);
        listenViewClick(viewHolder.itemView, i);
    }

    @Override
    public int getItemCount() {
        return courseList == null ? 0 : courseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv;
        private ImageView v_bg;    //边框

        public ViewHolder(View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv);
            v_bg = itemView.findViewById(R.id.v_bg);
        }
    }

    private void listenViewClick(View itemView, final int position) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CourseDetailActivity.startActivity(mContext, courseList.get(position).course_id);
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
                            .scaleX(1.05f)
                            .scaleY(1.05f)
                            .setDuration(scaleTime)
                            .setListener(new ViewPropertyAnimatorListener() {
                                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                @Override
                                public void onAnimationStart(View view) {
                                    v_bg.setVisibility(View.VISIBLE);
                                    //防止被其他view z轴方向覆盖
                                    UIUtil.bringToFront(view);
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
                                    //防止z轴方向，覆盖其他view
                                    UIUtil.bringToBackground(view);
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
