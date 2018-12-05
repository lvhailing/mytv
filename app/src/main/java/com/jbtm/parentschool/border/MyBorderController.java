package com.jbtm.parentschool.border;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class MyBorderController implements MyControllerInterface {
    private View lastFocus;
    private AnimatorSet mAnimatorSet;
    private List<Animator> mAnimatorList = new ArrayList<>();
    private View mHighLightView;
    private List<FocusListener> mFocusListener;
    private boolean isScrolling = false;

    public MyBorderController() {
        mFocusListener = new ArrayList<>(1);
        mFocusListener.add(focusMoveListener);
        mFocusListener.add(focusScaleListener);
        mFocusListener.add(focusPlayListener);
    }

    public interface FocusListener {
        void onFocusChanged(View oldFocus, View newFocus);
    }

    public FocusListener focusMoveListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            if (newFocus == null) return;
            mAnimatorList.addAll(getMoveAnimator(newFocus));
        }
    };

    public FocusListener focusScaleListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            mAnimatorList.addAll(getScaleAnimator(newFocus, true));
            if (oldFocus != null) {
                mAnimatorList.addAll(getScaleAnimator(oldFocus, false));
            }
        }
    };

    public FocusListener focusPlayListener = new FocusListener() {
        @Override
        public void onFocusChanged(View oldFocus, View newFocus) {
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.setInterpolator(new DecelerateInterpolator(1));
            animatorSet.setDuration(200);
            animatorSet.playTogether(mAnimatorList);
            mAnimatorSet = animatorSet;
            if (oldFocus == null) {
                animatorSet.setDuration(0);
                mHighLightView.setVisibility(View.VISIBLE);
            }
            animatorSet.start();
        }
    };

    protected List<Animator> getScaleAnimator(View view, boolean isScale) {
        List<Animator> animatorList = new ArrayList<>(2);
        try {
            float scaleBefore = isScale ? 1.0f : 1.1f;
            float scaleAfter = isScale ? 1.1f : 1.0f;
            ObjectAnimator scaleX = new ObjectAnimator().ofFloat(view, "scaleX", scaleBefore, scaleAfter);
            ObjectAnimator scaleY = new ObjectAnimator().ofFloat(view, "scaleY", scaleBefore, scaleAfter);
            animatorList.add(scaleX);
            animatorList.add(scaleY);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return animatorList;
    }

    protected List<Animator> getMoveAnimator(View newFocus) {
        List<Animator> animatorList = new ArrayList<>();
        int[] newXY = getLocation(newFocus);
        int[] fromXY = getLocation(mHighLightView);

        int oldWidth = mHighLightView.getMeasuredWidth();
        int oldHeight = mHighLightView.getMeasuredHeight();
        int newWidth = (int) (newFocus.getMeasuredWidth() * 1.1f);
        int newHeight = (int) (newFocus.getMeasuredHeight() * 1.1f);

        int[] toXY = new int[2];
        toXY[0] = (int) (newXY[0] - (newWidth - newFocus.getMeasuredWidth()) / 2.0f);
        toXY[1] = (int) (newXY[1] - (newHeight - newFocus.getMeasuredHeight()) / 2.0f);
        if (oldHeight == 0 && oldWidth == 0) {
            oldHeight = newHeight;
            oldWidth = newWidth;
        }

        PropertyValuesHolder valuesWithdHolder = PropertyValuesHolder.ofInt("width", oldWidth, newWidth);
        PropertyValuesHolder valuesHeightHolder = PropertyValuesHolder.ofInt("height", oldHeight, newHeight);
        PropertyValuesHolder valuesXHolder = PropertyValuesHolder.ofFloat("translationX", fromXY[0], toXY[0]);
        PropertyValuesHolder valuesYHolder = PropertyValuesHolder.ofFloat("translationY", fromXY[1], toXY[1]);
        final ObjectAnimator moveAnimator = ObjectAnimator.ofPropertyValuesHolder(mHighLightView, valuesWithdHolder, valuesHeightHolder, valuesYHolder, valuesXHolder);

        moveAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public synchronized void onAnimationUpdate(ValueAnimator animation) {
                int width = (int) animation.getAnimatedValue("width");
                int height = (int) animation.getAnimatedValue("height");
                View view = (View) moveAnimator.getTarget();
                view.getLayoutParams().width = width;
                view.getLayoutParams().height = height;
                if (width > 0) {
                    view.requestLayout();
                    view.postInvalidate();
                }
            }
        });
        animatorList.add(moveAnimator);
        return animatorList;
    }

    protected int[] getLocation(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    @Override
    public void onFocusChanged(View oldFocus, View newFocus) {
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.end();
        }
        if (isScrolling || newFocus == null || newFocus.getWidth() <= 0 || newFocus.getHeight() <= 0) {
            return;
        }
        lastFocus = newFocus;
        mAnimatorList.clear();
        for (FocusListener f : this.mFocusListener) {
            f.onFocusChanged(oldFocus, newFocus);
        }
    }

    @Override
    public void onAttach(View highLightView, View rv) {
        mHighLightView = highLightView;
        ViewGroup vg = (ViewGroup) rv.getRootView();
        vg.addView(highLightView);
        highLightView.setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) rv;
        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    isScrolling = false;
                    View newFocus = lastFocus;
                    AnimatorSet animatorSet = new AnimatorSet();
                    List<Animator> list = new ArrayList<>();
                    list.addAll(getScaleAnimator(newFocus, true));
                    list.addAll(getMoveAnimator(newFocus));
                    animatorSet.setDuration(200);
                    animatorSet.playTogether(list);
                    animatorSet.start();
                } else if (newState == RecyclerView.SCROLL_STATE_SETTLING) {
                    isScrolling = true;
                    if (lastFocus != null) {
                        List<Animator> list = getScaleAnimator(lastFocus, false);
                        AnimatorSet animatorSet = new AnimatorSet();
                        animatorSet.setDuration(150);
                        animatorSet.playTogether(list);
                        animatorSet.start();
                    }
                }
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
    }
}
