package com.dyj.szweather.module.main.commonview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author ：Dyj
 * @date ：Created in 2022/6/6 19:56
 * @description：RV OF VP
 * @modified By：
 * @version: 1.0
 */
public class RecycleViewVp extends RecyclerView {
    public RecycleViewVp(@NonNull Context context) {
        super(context);
    }

    public RecycleViewVp(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RecycleViewVp(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private int startX, startY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);//告诉viewgroup不要去拦截我
                break;
            case MotionEvent.ACTION_MOVE:
                int endX = (int) ev.getX();
                int endY = (int) ev.getY();
                int disX = endX - startX;
                int disY = endY - startY;
                //设定内部是否可滑动
                boolean hasScrollView = false;
                if (canScrollHorizontally(-1) && disX > 0){
                    hasScrollView = true;
                }
                if (canScrollHorizontally(1) && disX < 0){
                    hasScrollView = true;
                }
                //角度正确且内部可滑动，则让上层view别拦截我的事件
                float r = (float)Math.abs(disY)/Math.abs(disX);
                getParent().requestDisallowInterceptTouchEvent(r < 0.7f && hasScrollView);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

}
