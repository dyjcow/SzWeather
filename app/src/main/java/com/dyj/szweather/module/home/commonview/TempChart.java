package com.dyj.szweather.module.home.commonview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.View;

import com.dyj.szweather.R;
import com.dyj.szweather.bean.WeatherDay;
import com.dyj.szweather.util.DisplayUtil;
import com.dyj.szweather.util.MyUtil;


/**
 * @author ：Dyj
 * @date ：Created in 2022/6/2 17:26
 * @description：
 * @modified By：
 * @version:
 */
public class TempChart extends View {

    private int topBottom = 0;
    private int minTemp = 0;
    private int maxTemp = 0;
    private int lowTemp = 0;
    private int highTemp = 0;
    private float mHalfWidth = 0f;
    private float mHeight = 0f;
    private Paint mLowPaint;
    private Paint mHighPaint;
    private Paint mTextPaint;
    private int textHeight = 0;
    private String lowText = "";
    private String highText = "";
    private int lowTextWidth = 0;
    private int highTextWidth = 0;
    private int usableHeight = 0;
    private int tempDiff = 0;
    private float density = 0f;
    private float pntRadius = 0f;

    // 前一天数据
    private WeatherDay mPrev;

    // 后一天数据
    private WeatherDay mNext;

    public TempChart(Context context) {
        this(context, null);
    }

    public TempChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TempChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        topBottom = DisplayUtil.dp2px(8f);;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize((float) DisplayUtil.sp2px(context, 12f));
        mTextPaint.setColor(MyUtil.ViewGetColor(context,R.color.color_666));
        textHeight = (int) (mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top);
        int lineWidth = DisplayUtil.dp2px(2f);
        mLowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLowPaint.setStrokeWidth((float) lineWidth);
        // 设置线帽，方式折线陡峭时线中间出现裂痕
        mLowPaint.setStrokeCap(Paint.Cap.SQUARE);
        mLowPaint.setColor(Color.parseColor("#00A368"));
        mHighPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHighPaint.setStrokeWidth((float) lineWidth);
        mHighPaint.setStrokeCap(Paint.Cap.SQUARE);
        mHighPaint.setColor(Color.parseColor("#FF7200"));
        pntRadius = (float) DisplayUtil.dp2px(3f);
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
        mHalfWidth = getMeasuredWidth() / 2f;
        mHeight = (float) getMeasuredHeight();
        usableHeight =(int) (mHeight - topBottom * 2 - textHeight * 2);
        density = (float) usableHeight / tempDiff;
    }

    /**
     * Implement this to do your drawing.
     *
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(mHalfWidth, 0f);
        int topY = (int) ((maxTemp - highTemp) * density + topBottom + textHeight);
        int bottomY = (int) ((maxTemp - lowTemp) * density + topBottom + textHeight);
        canvas.drawCircle(0f, (float) topY, pntRadius, mHighPaint);
        canvas.drawCircle(0f, (float) bottomY, pntRadius, mLowPaint);
        canvas.drawText(
                highText,
                (float) (-lowTextWidth / 2),
                topY - mTextPaint.getFontMetrics().bottom * 2,
                mTextPaint
        );
        canvas.drawText(
                lowText,
                (float) (-lowTextWidth / 2),
                (float) (bottomY + textHeight),
                mTextPaint
        );
        // 绘制当前点给前一天数据的连线
        if (mPrev != null) {
            Pair<Integer, Integer> prev = getEnds(mPrev);
            canvas.drawLine(-mHalfWidth, (prev.first + topY) / 2f, 0f, (float) topY, mHighPaint);
            canvas.drawLine(
                    -mHalfWidth,
                    (prev.second + bottomY) / 2f,
                    0f,
                    (float) bottomY,
                    mLowPaint
            );
        }
        // 绘制当前点给后一天数据的连线
        if (mNext != null) {
            Pair<Integer, Integer> next = getEnds(mNext);
            canvas.drawLine(0f, (float) topY, mHalfWidth, (next.first + topY) / 2f, mHighPaint);
            canvas.drawLine(
                    0f,
                    (float) bottomY,
                    mHalfWidth,
                    (next.second + bottomY) / 2f,
                    mLowPaint
            );
        }
    }

    private Pair<Integer, Integer> getEnds(WeatherDay daily){
        int topY = (int) ((maxTemp - Integer.parseInt(daily.getTempMax())) * density + topBottom + textHeight);
        int bottomY = (int) ((maxTemp - Integer.parseInt(daily.getTempMin())) * density + topBottom + textHeight);
        return new Pair<>(topY, bottomY);
    }

    public void setData(int minTemp, int maxTemp,WeatherDay prev, WeatherDay current, WeatherDay next) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        lowTemp = Integer.parseInt(current.getTempMin());
        highTemp = Integer.parseInt(current.getTempMax());
        mPrev = prev;
        mNext = next;
        lowText = current.getTempMin()+"°C";
        highText = current.getTempMax()+"°C";
        lowTextWidth = (int) mTextPaint.measureText(lowText);
        highTextWidth = (int) mTextPaint.measureText(highText);
        tempDiff = maxTemp - minTemp;
        if (usableHeight != 0) {
            density = usableHeight / (float)tempDiff;
        }
    }
}
