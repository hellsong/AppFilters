package com.hellsong.appfilters.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import com.hellsong.appfilters.R;

/**
 * Created by weiruyou on 2015/11/20.
 */
public class FilterDrawable extends Drawable {
    private static final String TAG = FilterDrawable.class.getSimpleName();
    private static final long FRAME_DURATION = 1000 / 60;
    private static final int ANIMATION_DURATION = 200;

    protected float mBarSize;
    protected float mCircleRadius;
    protected float mBarGapSize;
    protected final Paint mPaint = new Paint();
    protected final Path mPath = new Path();
    protected float mProgress;
    protected float mBarThickness;
    protected float mCloseStateWidth;
    protected boolean mRunning;
    protected boolean mReverse;
    private Interpolator mInterpolator;
    private long mStartTime;

    protected Context mContext;

    public FilterDrawable(Context context) {
        this.mContext = context;
        mPaint.setColor(context.getResources().getColor(R.color.white));
        mBarSize = context.getResources().getDimensionPixelSize(R.dimen.fliter_drawable_bar_size);
        mCircleRadius = context.getResources().getDimensionPixelSize(R.dimen.fliter_drawable_circle_radiu);
        mBarGapSize = context.getResources().getDimensionPixelSize(R.dimen.fliter_drawable_gap);
        mBarThickness = context.getResources().getDimensionPixelSize(R.dimen.fliter_drawable_thickness);
        mCloseStateWidth = context.getResources().getDimensionPixelSize(R.dimen.fliter_drawable_close_state_width);
        this.mPaint.setStyle(Paint.Style.STROKE);
        this.mPaint.setStrokeJoin(Paint.Join.ROUND);
        this.mPaint.setStrokeCap(Paint.Cap.SQUARE);
        this.mPaint.setStrokeWidth(this.mBarThickness);
        mInterpolator = new AccelerateDecelerateInterpolator();
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        if (progress < 0) {
            progress = 0;
        }
        if (progress > 1) {
            progress = 1f;
        }
        this.mProgress = progress;
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        Rect localRect = getBounds();
        float Bar1LPointY = (mCloseStateWidth) * mProgress;
        float Bar2LPointY = (mBarGapSize) * (1 - mProgress);
        float RPointX = mBarSize - (mBarSize - mCloseStateWidth) * mProgress;
        mPath.rewind();
        mPath.moveTo(-mBarSize / 2, Bar1LPointY - mBarGapSize / 2);
        mPath.rLineTo(RPointX, -Bar1LPointY);
        mPath.moveTo(-mBarSize / 2, Bar2LPointY - mBarGapSize / 2);
        mPath.rLineTo(RPointX, mCloseStateWidth * mProgress);
        if (mProgress == 0) {
            mPath.addCircle(mBarSize / 5f - mBarSize / 2, -mBarGapSize / 2, mCircleRadius, Path.Direction.CW);
            mPath.addCircle(mBarSize / 5f * 4 - mBarSize / 2, Bar2LPointY - mBarGapSize / 2, mCircleRadius, Path.Direction.CW);
        }
        mPath.close();
        canvas.save();
        canvas.translate(localRect.centerX(), localRect.centerY());
        canvas.drawPath(this.mPath, this.mPaint);
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        this.mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        this.mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public void animateToNormal() {
        unscheduleSelf(mUpdater);
        mReverse = true;
        mRunning = true;
        mStartTime = SystemClock.uptimeMillis();
        scheduleSelf(mUpdater, mStartTime + FRAME_DURATION);
    }

    public void animateToClose() {
        unscheduleSelf(mUpdater);
        mReverse = false;
        mRunning = true;
        mStartTime = SystemClock.uptimeMillis();
        scheduleSelf(mUpdater, mStartTime + FRAME_DURATION);
    }

    private final Runnable mUpdater = new Runnable() {

        @Override
        public void run() {
            long currentTime = SystemClock.uptimeMillis();
            long diff = currentTime - mStartTime;
            if (diff < ANIMATION_DURATION) {
                float input = mReverse ? 1 - (float) diff / (float) ANIMATION_DURATION : (float) diff / (float) ANIMATION_DURATION;
                float interpolation = mInterpolator.getInterpolation(input);
//                Log.d(TAG, "Interpolation:" + interpolation);
                scheduleSelf(mUpdater, currentTime + FRAME_DURATION);
                setProgress(interpolation);
            } else {
                unscheduleSelf(mUpdater);
                mRunning = false;
                setProgress(mReverse ? 0 : 1f);
            }
        }
    };
}
