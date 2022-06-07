package com.dyj.szweather.module.main.activity;


import android.os.Handler;

import com.dyj.szweather.base.BaseActivity;
import com.dyj.szweather.bean.CityDB;
import com.dyj.szweather.databinding.MainActivitySplashBinding;
import com.dyj.szweather.module.main.presenter.SplashPresenter;
import com.dyj.szweather.module.main.view.ISplashView;
import com.dyj.szweather.module.search.activity.SearchActivity;
import com.dyj.szweather.util.ActivityUtil;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import org.litepal.LitePal;

import java.util.List;

public class SplashActivity extends BaseActivity<SplashPresenter, MainActivitySplashBinding> implements ISplashView {

    /**
     * 初始化presenter，也是与Activity的绑定
     *
     * @return 返回new的Presenter层的值
     */
    @Override
    protected SplashPresenter createPresenter() {
        return new SplashPresenter(this);
    }

    /**
     * 载入view的一些操作
     */
    @Override
    protected void initView() {
        CityDB cityDB = LitePal.findFirst(CityDB.class);
        if (cityDB != null) new Handler().postDelayed(() -> ActivityUtil.startActivity(MainActivity.class,true),1000);
        else new Handler().postDelayed(() -> ActivityUtil.startActivity(SearchActivity.class,true),1000);

    }

    /**
     * 载入数据操作
     */
    @Override
    protected void initData() {

    }
}