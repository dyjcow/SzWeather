package com.dyj.szweather.app;

import android.app.Application;
import android.content.ContentProvider;
import android.content.Context;
import android.os.Build;
import com.didichuxing.doraemonkit.DoKit;
import com.dyj.szweather.util.ActivityUtil;
import com.dyj.szweather.util.LogUtil;
import com.dyj.szweather.util.MyUtil;
import com.tamsiree.rxkit.RxTool;
import org.litepal.LitePal;

/**
 * @author ：Dyj
 * @date ：Created in 2022/5/24 12:42
 * @description： From Application
 * @modified By：
 * @version: 1.0
 */
public class App extends Application {

    /**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     *
     * <p>Implementations should be as quick as possible (for example using
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.</p>
     *
     * <p>If you override this method, be sure to call {@code super.onCreate()}.</p>
     *
     * <p class="note">Be aware that direct boot may also affect callback order on
     * Android {@link Build.VERSION_CODES#N} and later devices.
     * Until the user unlocks the device, only direct boot aware components are
     * allowed to run. You should consider that all direct boot unaware
     * components, including such {@link ContentProvider}, are
     * disabled until user unlock happens, especially when component callback
     * order matters.</p>
     */
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
        //载入Dokit监测
        new DoKit.Builder(this)
                .productId("b8175642d4047afe7352d3a3faee2b74")
                .build();
        //载入数据库映射工具
        LitePal.initialize(this);
        //初始化
        MyUtil.initialize(this);
        //设置UI工具
        RxTool.init(this);
        //设置打印开关
        LogUtil.setIsLog(true);
        //注册Activity生命周期
        registerActivityLifecycleCallbacks(ActivityUtil.getActivityLifecycleCallbacks());
    }

    public static Context getContext() {
        return context;
    }
}
