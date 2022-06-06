package com.dyj.szweather.module.home.commonview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.dyj.szweather.R;
import com.dyj.szweather.util.DisplayUtil;
import com.dyj.szweather.util.MyUtil;

/**
 * @author ：Dyj
 * @date ：Created in 2022/6/2 22:39
 * @description：
 * @modified By：
 * @version:
 */
public class AirConditionView extends View {
    public AirConditionView(Context context) {
        this(context, null);
    }

    public AirConditionView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AirConditionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    // 底部偏移量
    float bottomOffset = 0f;

    // 0，500的x坐标
    float x0 = 0f;
    float x500 = 0f;

    int mStrokeWidth = 0;
    int mWidth = 0;
    float centerY = 0f;
    RectF bound = new RectF();

    float mAngle = 0f;
    int mAqi = 0;
    String mCondition = "";

    float numX = 0f;
    float numY = 0f;
    float textX = 0f;
    float textY = 0f;

    Paint bgPaint;
    Paint progressPaint;
    Paint rangePaint;
    Paint numPaint;


    public void setValue(int aqi, String condition) {
        mAqi = aqi;
        mAngle = 300 * aqi / 500f;
        mCondition = condition;

        numX = -getNumPaint().measureText(Integer.toString(aqi)) / 2;
        textX = -getRangePaint().measureText(condition) / 2;
        getProgressPaint().setColor(MyUtil.getAirColor(getContext(),aqi));

        invalidate();
    }

    /**
     * <p>
     * Measure the view and its content to determine the measured width and the
     * measured height. This method is invoked by {@link #measure(int, int)} and
     * should be overridden by subclasses to provide accurate and efficient
     * measurement of their contents.
     * </p>
     *
     * <p>
     * <strong>CONTRACT:</strong> When overriding this method, you
     * <em>must</em> call {@link #setMeasuredDimension(int, int)} to store the
     * measured width and height of this view. Failure to do so will trigger an
     * <code>IllegalStateException</code>, thrown by
     * {@link #measure(int, int)}. Calling the superclass'
     * {@link #onMeasure(int, int)} is a valid use.
     * </p>
     *
     * <p>
     * The base class implementation of measure defaults to the background size,
     * unless a larger size is allowed by the MeasureSpec. Subclasses should
     * override {@link #onMeasure(int, int)} to provide better measurements of
     * their content.
     * </p>
     *
     * <p>
     * If this method is overridden, it is the subclass's responsibility to make
     * sure the measured height and width are at least the view's minimum height
     * and width ({@link #getSuggestedMinimumHeight()} and
     * {@link #getSuggestedMinimumWidth()}).
     * </p>
     *
     * @param widthMeasureSpec  horizontal space requirements as imposed by the parent.
     *                          The requirements are encoded with
     *                          {@link MeasureSpec}.
     * @param heightMeasureSpec vertical space requirements as imposed by the parent.
     *                          The requirements are encoded with
     *                          {@link MeasureSpec}.
     * @see #getMeasuredWidth()
     * @see #getMeasuredHeight()
     * @see #setMeasuredDimension(int, int)
     * @see #getSuggestedMinimumHeight()
     * @see #getSuggestedMinimumWidth()
     * @see MeasureSpec#getMode(int)
     * @see MeasureSpec#getSize(int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        float bottom = getRangePaint().getFontMetrics().bottom;
        float top = getRangePaint().getFontMetrics().top;
        bottomOffset = (bottom - top) * 0.5f;

        centerY = (getMeasuredHeight() - bottomOffset) / 2f;
        bound.left = -centerY + (float) mStrokeWidth / 2;
        bound.top = -centerY + (float) mStrokeWidth / 2;
        bound.right = centerY - (float) mStrokeWidth / 2;
        bound.bottom = centerY - (float) mStrokeWidth / 2;

        float descent = getNumPaint().getFontMetrics().descent;
        float ascent = getNumPaint().getFontMetrics().ascent;
        numY = (descent - ascent) / 2;

        descent = getRangePaint().getFontMetrics().descent;
        ascent = getRangePaint().getFontMetrics().ascent;
        textY = -(descent - ascent);

        x0 = bound.left / 2 - getRangePaint().measureText("0") / 2;
        x500 = bound.right / 2 - getRangePaint().measureText("500") / 2;
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mWidth / 2f, centerY);

        canvas.drawText("0", x0, centerY + bottomOffset, getRangePaint());
        canvas.drawText("500", x500, centerY + bottomOffset, getRangePaint());
        canvas.drawArc(bound, 120f, 300f, false, getBgPaint());
        if (mAngle != 0f) {
            canvas.drawArc(bound, 120f, mAngle, false, getProgressPaint());
        }
        if (mCondition.length() > 0) {
            canvas.drawText(mCondition, textX, textY, getRangePaint());
        }

        if (mAqi != 0) {
            canvas.drawText(Integer.toString(mAqi), numX, numY, getNumPaint());
        }
    }

    public synchronized Paint getBgPaint(){
        if (bgPaint == null) {
            bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            bgPaint.setColor(Color.parseColor("#20000000"));
            mStrokeWidth = DisplayUtil.dp2px(5f);
            bgPaint.setStrokeWidth((float) mStrokeWidth);
            bgPaint.setStrokeCap(Paint.Cap.ROUND);
            bgPaint.setStyle(Paint.Style.STROKE);
        }
        return bgPaint;
    }

    public synchronized Paint getProgressPaint(){
        if (progressPaint == null){
            progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            progressPaint.setColor(MyUtil.ViewGetColor(getContext(), R.color.color_air_leaf_1));
            mStrokeWidth = DisplayUtil.dp2px(5f);
            progressPaint.setStrokeWidth((float) mStrokeWidth);
            progressPaint.setStrokeCap(Paint.Cap.ROUND);
            progressPaint.setStyle(Paint.Style.STROKE);
        }
        return progressPaint;
    }

    public synchronized Paint getRangePaint(){
        if (rangePaint == null){
            rangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            rangePaint.setColor(Color.parseColor("#66000000"));
            rangePaint.setTextSize((float) DisplayUtil.sp2px(13f));
        }
        return rangePaint;
    }

    public synchronized Paint getNumPaint(){
        if (numPaint == null){
            numPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            numPaint.setColor(Color.parseColor("#99000000"));
            numPaint.setTextSize((float) DisplayUtil.sp2px(28f));
        }
        return numPaint;
    }

}
