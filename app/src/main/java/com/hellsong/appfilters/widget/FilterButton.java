package com.hellsong.appfilters.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by weiruyou on 2015/11/24.
 */
public class FilterButton extends View {

    private FilterDrawable mFilterDrawable;

    public FilterButton(Context context) {
        super(context);
        init(context);
    }

    public FilterButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FilterButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public FilterButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        mFilterDrawable = new FilterDrawable(context);
        mFilterDrawable.setCallback(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getWidth() - getPaddingRight();
        int bottom = getHeight() - getPaddingBottom();
        mFilterDrawable.setBounds(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mFilterDrawable.draw(canvas);
        super.onDraw(canvas);
    }

    public void animateToClose() {
        mFilterDrawable.animateToClose();
    }

    public void animateToNormal() {
        mFilterDrawable.animateToNormal();
    }

    public void setProcess(float process) {
        mFilterDrawable.setProgress(process);
    }

    public float getProcess() {
        return mFilterDrawable.getProgress();
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return who == mFilterDrawable || super.verifyDrawable(who);
    }
}
