package com.dyj.szweather.util;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import com.dyj.szweather.R;
import com.tamsiree.rxui.view.dialog.RxDialogLoading;
import com.tamsiree.rxui.view.dialog.RxDialogShapeLoading;

/**
 * @author ：Dyj
 * @date ：Created in 2022/4/12 22:08
 * @description：汇聚各种其他功能
 * @modified By：
 * @version: 1.0
 */
public class MyUtil {

    /**
     * 全局context
     */
    private static Application mApplicationContext;

    private static RxDialogLoading rxDialogLoading;


    /**
     * @param app 初始化全局context
     */
    public static void initialize(Application app) {
        mApplicationContext = app;
    }


    /**
     * 获得全局context
     *
     * @return 当前的全局context
     */
    public static Application getApplication() {
        return mApplicationContext;
    }


    /**
     * 关闭键盘
     */
    public static void closeSoftKeyboard() {
        InputMethodManager inputManger = (InputMethodManager) ActivityUtil.getCurrentActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManger != null) {
            inputManger.hideSoftInputFromWindow(ActivityUtil.getCurrentActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    public static String getString(int id) {
        return getApplication().getResources().getString(id);
    }

    /**
     * 展示dialog
     *
     * @param activity 传入当前Acitivity
     */
    public static void showLoading(Context context){
        rxDialogLoading = new RxDialogLoading(context);
        rxDialogLoading.setCanceledOnTouchOutside(false);
        rxDialogLoading.show();
    }

    /**
     * 成功隐藏dialog，显示成功
     */
    public static void dismissSuccessLoading(){
        rxDialogLoading.cancel(RxDialogLoading.RxCancelType.success,getString(R.string.load_success));
    }


    /**
     * 失败隐藏dialog，显示失败
     */
    public static void dismissFailedLoading(){
        rxDialogLoading.cancel(RxDialogLoading.RxCancelType.error,getString(R.string.load_error));
    }

    public static int getColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getApplication().getResources().getColor(id, getApplication().getTheme());
        } else {
            //noinspection deprecation
            return getApplication().getResources().getColor(id);
        }

    }




}
