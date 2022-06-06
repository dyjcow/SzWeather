package com.dyj.szweather.util;


import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import com.dyj.szweather.R;
import com.tamsiree.rxui.view.dialog.RxDialogLoading;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
     * @param context 传入当前Acitivity
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

    /**
     * Application 层面下调用颜色资源
     *
     * @param id 颜色的资源 id
     * @return 对应的颜色值
     */
    public static int AppGetColor(int id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return getApplication().getResources().getColor(id, getApplication().getTheme());
        } else {
            //noinspection deprecation
            return getApplication().getResources().getColor(id);
        }

    }

    /**
     * View 层面下调用资源 id
     *
     * @param context View 的 context 值
     * @param id 颜色的资源 id
     * @return 对应的颜色值
     */
    public static int ViewGetColor(Context context,int id){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return context.getResources().getColor(id, context.getTheme());
        } else {
            //noinspection deprecation
            return context.getResources().getColor(id);
        }
    }

    /**
     * 获取当前天气状况的图标
     *
     * @param context 对应的 context 值
     * @param icon 传入的icon 字段
     * @return 返回拼接后查询到的资源 id
     */
    public static int getWeatherIcon(Context context, String icon){
        return context.getResources().getIdentifier("icon_"+icon,"drawable",context.getPackageName());
    }

    /**
     * 获得空气状态的颜色值
     *
     * @param context 对应的 context 值
     * @param aqi  传入的 aqi 字段
     * @return 判断后返回的资源颜色
     */
    public static int getAirColor(Context context,int aqi){
        String flag;
        if (aqi <= 50) flag = "1";
        else if (aqi <= 100) flag = "2";
        else if (aqi <= 150) flag = "3";
        else if (aqi <= 200) flag = "4";
        else if (aqi <= 250) flag = "5";
        else flag = "6";
        int id = context.getResources().getIdentifier("color_air_leaf_"+flag,"color",context.getPackageName());
        return context.getResources().getColor(id, context.getTheme());
    }


    /**
     * 将图片压缩到指定大小
     *
     * @param w
     * @param h
     * @return
     */
    public static Bitmap compressBySize(Context context, int resourceId, float w, float h) {
        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(resourceId,context.getTheme());
        Matrix matrix = new Matrix();
        Bitmap src = bd.getBitmap();
        matrix.postScale(w / src.getWidth(), h / src.getHeight());
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static String split(String time){
        return time.split("T|\\+")[1];
    }

    public static int getNowHour(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }


    /**
     * HH:mm
     */
    public static String getNowTime() {
        SimpleDateFormat simpleDateFormat = new  SimpleDateFormat("HH:mm", Locale.US);
        // 获取当前时间
        Date date = new  Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    public static int getWeatherIcon(String icon){
        switch (icon) {
            case "100":
                return R.drawable.icon_100;
            case "101":
                return R.drawable.icon_101;
            case "102":
                return R.drawable.icon_102;
            case "103":
                return R.drawable.icon_103;
            case "104":
                return R.drawable.icon_104;
            case "150":
                return R.drawable.icon_150;
            case "153":
                return R.drawable.icon_153;
            case "154":
                return R.drawable.icon_151;
            case "300":
                return R.drawable.icon_300;
            case "301":
                return R.drawable.icon_301;
            case "302":
                return R.drawable.icon_302;
            case "303":
                return R.drawable.icon_303;
            case "304":
                return R.drawable.icon_304;
            case "305":
                return R.drawable.icon_305;
            case "306":
                return R.drawable.icon_306;
            case "307":
                return R.drawable.icon_307;
            case "308":
                return R.drawable.icon_308;
            case "309":
                return R.drawable.icon_309;
            case "310":
                return R.drawable.icon_310;
            case "311":
                return R.drawable.icon_311;
            case "312":
                return R.drawable.icon_312;
            case "313":
                return R.drawable.icon_313;
            case "314":
                return R.drawable.icon_314;
            case "315":
                return R.drawable.icon_315;
            case "316":
                return R.drawable.icon_316;
            case "317":
                return R.drawable.icon_317;
            case "318":
                return R.drawable.icon_318;
            case "350":
                return R.drawable.icon_350;
            case "351":
                return R.drawable.icon_351;
            case "399":
                return R.drawable.icon_399;
            case "400":
                return R.drawable.icon_400;
            case "401":
                return R.drawable.icon_401;
            case "402":
                return R.drawable.icon_402;
            case "403":
                return R.drawable.icon_403;
            case "404":
                return R.drawable.icon_404;
            case "405":
                return R.drawable.icon_405;
            case "406":
                return R.drawable.icon_406;
            case "407":
                return R.drawable.icon_407;
            case "408":
                return R.drawable.icon_408;
            case "409":
                return R.drawable.icon_409;
            case "410":
                return R.drawable.icon_410;
            case "456":
                return R.drawable.icon_456;
            case "457":
                return R.drawable.icon_457;
            case "499":
                return R.drawable.icon_499;
            case "500":
                return R.drawable.icon_500;
            case "501":
                return R.drawable.icon_501;
            case "502":
                return R.drawable.icon_502;
            case "503":
                return R.drawable.icon_503;
            case "504":
                return R.drawable.icon_504;
            case "507":
                return R.drawable.icon_507;
            case "508":
                return R.drawable.icon_509;
            case "510":
                return R.drawable.icon_510;
            case "511":
                return R.drawable.icon_511;
            case "512":
                return R.drawable.icon_512;
            case "513":
                return R.drawable.icon_513;
            case "514":
                return R.drawable.icon_514;
            case "515":
                return R.drawable.icon_515;
            case "900":
                return R.drawable.icon_900;
            case "901":
                return R.drawable.icon_901;
            case "999":
                return R.drawable.icon_999;
        }
        return R.drawable.icon_100;
    }



}
